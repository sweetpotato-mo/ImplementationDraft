package controllers

import map.EnvironmentMap
import data.Cloud
import data.Cloud.Companion.RAIN_TRIGGER_AMOUNT
import data.Cloud.Companion.MAX_MOVEMENT_QUOTA
import enums.TileCategory
import loggersingleton.Logger
import session.TickInfo
import session.TickInfoData
import tile.FarmableTile
import tile.Tile
import java.io.PrintWriter

class EnvironmentController(
    private var tickInfo: TickInfoData?,
    private val environmentMap: EnvironmentMap,
    private var clouds: MutableList<Cloud>,
    private var tileIdCloudMap: MutableMap<Int, Cloud>,
    private var maxCloudId: Int //while parsing, please initialize to -1 if there is no cloud in the map. So
                                //that the first cloud from CloudCreationIncident has id 0.
) {
    // A map to store sunlight values for each tick, derived from the specification.
    private val tickSunlightMap: Map<Int, Int> = BASE_SUNLIGHT_HOURS

    companion object{
         val BASE_SUNLIGHT_HOURS : Map<Int, Int> = mapOf(
            1 to 98, 2 to 98, 3 to 112, 4 to 112, 5 to 126, 6 to 126, 7 to 140, 8 to 140,
            9 to 168, 10 to 168, 11 to 168, 12 to 168, 13 to 168, 14 to 168, 15 to 154,
            16 to 154, 17 to 126, 18 to 126, 19 to 112, 20 to 112, 21 to 98, 22 to 98,
            23 to 84, 24 to 84
        )
    }




    /**
     * Executes all environmental updates for a single simulation tick in the correct order.
     * The overall behavior of each tick, as it relates to the environment, is managed here:
     * 1. Soil moisture reduction.
     * 2. Cloud movement, rain, union, and dissipation.
     *
     */
    fun runTick() {
        // Step 1: Reduce soil moisture at the beginning of the tick.
        reduceSoilMoisture()

        //Intermediate Step, set base sunlight to the monthly average before cloud effects are applied.
        resetBaseSunlight()

        // Step 2: Simulate cloud behavior (rain, movement, union, dissipation).
        startCloudLoop()

        // Step 3: Handle the final sunlight reduction for clouds that ended up on a tile.Also decrement Duration by 1.
        // And remove Cloud if necessary.
        finalCloudBehaviour()

    }

    /**
     * Creates a new cloud and adds it to the list of active clouds.
     * This method is called by the `IncidentController` during a `CLOUD_CREATION` incident.
     *
     * @param tileId The ID of the tile where the cloud is created.
     * @param duration The duration of the new cloud.
     * @param amount The initial amount of water in the new cloud.
     */
    fun createCloud(tileId: Int, duration: Int, amount: Int) {

        val maxId = ++maxCloudId

        val createdCloud = Cloud(maxId, duration, amount,tileId)

        val theCloudThatWasThere = tileIdCloudMap[tileId]

        if(theCloudThatWasThere != null){

            val newCloud = merge(createdCloud, theCloudThatWasThere)

            Logger.logCloudMerge(
                createdCloud.id, theCloudThatWasThere.id, newCloud.id,
                newCloud.amount, newCloud.duration, newCloud.location
            )

            //We don't log the Incident here. IncidentController will log it.

            addCloud(newCloud)
            removeCloud(theCloudThatWasThere)
        }else{
            addCloud(createdCloud)
        }
    }

    /**
     * Adds a cloud to the list of active clouds.
     *
     * @param cloud The cloud to add.
     */
    fun addCloud(cloud: Cloud) {
        clouds.add(cloud)
        tileIdCloudMap[cloud.location] = cloud
    }

    /**
     * Removes a cloud from the list of active clouds.
     *
     * @param cloud The cloud to remove.
     */
    fun removeCloud(cloud: Cloud) {
        clouds.remove(cloud)
        tileIdCloudMap.remove(cloud.location)
    }

    /**
     * Resets the sunlight on all tiles to the monthly average before cloud effects are applied.
     *
     * will have to use tickInfo to get the current tick.
     */
    fun resetBaseSunlight() {
        TODO("Not yet implemented")
    }

    /**
     * Decreases the soil moisture for all `FIELD` and `PLANTATION` tiles.
     * The reduction rate is based on whether a plant is growing or the tile is fallow.
     * Also logs if the moisture level falls below the plant's required threshold.
     */
    private fun reduceSoilMoisture() {
        val currentYearTick = tickInfo!!.getCurrentTick()
        TODO("Not yet implemented")
    }


    /**
     * Manages all cloud-related activities for the current tick.
     * Clouds rain, move, merge, and dissipate in ascending order of their IDs,
     * processing merged clouds in subsequent turns within the same tick.
     */
    private fun startCloudLoop() {
        // A Set is used for fast lookup to track processed clouds.
        val processedClouds = mutableSetOf<Cloud>()

        // Process clouds in batches until no new clouds (via merge) are created.
        do {
            // 1. Get all clouds not yet processed, sorted by ID (for correct processing order).
            val cloudsToProcess = clouds
                .filterNot { processedClouds.contains(it) }
                .sortedBy { it.id }
                .toList()

            // 2. Break condition: If no new clouds were created since the last batch, stop.
            if (cloudsToProcess.isEmpty()) {
                break
            }

            // 3. Process the new batch.
            for (cloud in cloudsToProcess) {
                // Note: Cloud may have been removed if a *previous* cloud merged with it.
                // cloudLoopHelper handles the check if it still exists.
                cloudLoopHelper(cloud)

                // Mark cloud as processed for this batch.
                processedClouds.add(cloud)
            }

        } while (true)
    }

    /**
     * Executes the entire continuous movement and rain sequence for a single cloud within one tick.
     *
     * This function orchestrates the inner 'while' loop, which controls the cloud's actions until
     * its quota is exhausted or a hard-stop condition is met (dissipation, merge, or null tile).
     *
     * **This Function's Responsibilities: **
     * 1.  **Existence Check**: Ensures the cloud has not already been removed
     *      (e.g., by a merge from a previous cloud's iteration).
     * 2.  **Quota Reset**: Resets the cloud's `movementQuota` to `MAX_MOVEMENT_QUOTA` at the start of its turn.
     * 3.  **Continuous Loop Control**: Executes a `while` loop that continues as long as the
     *      cloud has movement quota remaining, a valid next move, and has not dissipated.
     * 4.  **Pre-Move Rain Check**: Checks for and executes rainfall (`rain(cloud)`) before
     *      attempting movement.
     * 5.  **Dissipation Handling**: If rain results in dissipation (`shouldDissipate` is true),
     *      the function logs the dissipation, calls `removeCloud(cloud)`, and terminates the loop for this cloud.
     * 6.  **Movement/Stop Delegation**: Calls `cloudMovement(cloud)` to attempt a step, which
     *      handles all complex termination conditions (village, merge, null path).
     *
     * @param cloud The cloud to process.
     */
    private fun cloudLoopHelper(cloud: Cloud){
        // Skip clouds that were already removed (e.g., if a previous cloud merged with this one)
        // or if they were removed due to rain/village in a previous iteration.
        if (!clouds.contains(cloud)) {
            return
        }

        cloud.setMovementQuota(MAX_MOVEMENT_QUOTA)

        var shouldDissipate = false
        var canStillMove = true

        while (cloud.movementQuota > 0 && canStillMove) {

            if (cloud.amount >= RAIN_TRIGGER_AMOUNT) {
                shouldDissipate = rain(cloud)   //make every cloud Rain.
                if (shouldDissipate) {
                    Logger.logCloudDissipation(cloud.id, cloud.location)
                    removeCloud(cloud)
                    break
                }
            }
            // cloudMovement returns false if movement stopped (merge/village/null tile).
            canStillMove = cloudMovement(cloud)

        }
    }

    /**
     * Simulates rainfall from a cloud onto its current tile.
     *
     * This function determines the amount of water to rain down, updates the cloud and tile moisture,
     * and handles the immediate consequence for the cloud (dissipation due to being empty).
     *
     * **Rainfall Logic Constraints: **
     * 1.  **Water Threshold**: A cloud must contain exactly or more than 5,000 L of water to rain.
     * 2.  **Tile Type: Field/Plantation (Capacity Tiles)**: It only rains if the current soil moisture is below the
     *      maximum capacity. The amount of rain is limited to just enough to fill the tile to its maximum capacity.
     * 3.  **Tile Type: Other Tiles (No Capacity)**: For tiles without soil moisture capacity (e.g., Road, Meadow,
     *      Forest, Farmstead), the rain always occurs, and the cloud rains down its whole amount, causing the cloud to
     *      dissipate immediately.
     * 4.  **Village Tile**: The specification states that a cloud gets stuck and dissipates immediately upon entering a
     *      Village tile. Therefore, a cloud should never be asked to rain while located on a Village tile. This
     *      scenario should be treated as an environmental integrity error.
     *
     * @param cloud The cloud to perform the rain action.
     * @return `True` if the cloud rained down its entire remaining amount (and will dissipate due to being empty),
     * otherwise `False`.
     */
    private fun rain(cloud: Cloud): Boolean {

        if(cloud.amount < RAIN_TRIGGER_AMOUNT){
            return false    //Cloud does not have enough water to rain and hence will not dissipate.
        }

        val tile = environmentMap.getTileById(cloud.location)
        var rainAmount = 0

        when(tile.getCategory()){
            TileCategory.PLANTATION , TileCategory.FIELD -> {
                if(tile is FarmableTile){

                    // If already at max capacity, no rain occurs. Cloud does not dissipate.
                    if (tile.currentMoisture == tile.moistureCapacity) {
                        return false // Cloud doesn't dissipate (no rain occurred)
                    }

                    //Calculate the amount of rain to be applied.
                    // rainAmount is set to the lesser of the cloud's amount and the tile's remaining capacity.
                    rainAmount = tile.calculateReqRain().coerceAtMost(cloud.amount)

                    // Apply rain effects: decreases cloud amount, increases tile moisture.
                    cloud.decreaseCloudAmount(rainAmount)
                    tile.increaseMoisture(rainAmount)

                }
                //Remove this check once the code is stable.
                else{
                    println("DEBUG. This should not happen. Tile with Id ${tile.id} is not a FarmableTile, " +
                            "but has categoryId: ${tile.getCategory()}")
                    error("Environmental Error: Tile with Id ${tile.id} has a farmable category but is not a " +
                            "FarmableTile.")
                }
            }
            TileCategory.FARMSTEAD, TileCategory.ROAD, TileCategory.FOREST
                 , TileCategory.MEADOW -> {
                    // No capacity. Rain the entire amount.
                    rainAmount = cloud.amount

                    // Apply rain effects: decreases cloud amount, but tile moisture is NOT updated.
                    cloud.decreaseCloudAmount(rainAmount)
                 }

            TileCategory.VILLAGE -> {
                println("DEBUG. This should not happen. Cloud with Id ${cloud.id} is located on a Village tile.")
                error("Environmental Error: Cloud with Id ${cloud.id} is located on a Village tile.")
            }
        }
        // Log the event only if rain occurred.
        if (rainAmount > 0) {
            Logger.logRain(cloud.id, tile.id, rainAmount)
        }

        // Return true if the cloud is now empty (dissipates), false otherwise (survives).
        // shouldDissipate() says amount <= 0 -> true
        return cloud.shouldDissipate()
    }

    /**
     * Retrieves the cloud located on the specified tile.
     *
     * @param tile The tile to check for a cloud.
     * @return The Cloud object on the tile, or null if no cloud is present.
     */
    fun lookupCloud(tile: Tile): Cloud? {
        return tileIdCloudMap[tile.getId()]
    }



    /**
     * Attempts a single movement step for a cloud along its airflow path.
     *
     * This function handles the initial validity check for the next tile and performs the 3-hour
     * sunlight reduction and movement logging for the current tile before delegating to the helper.
     *
     * **This Function's Responsibilities (What We Do Here):**
     * 1.  **Stop Check - No Path**: Returns `false` if `nextTile` is null (unspecified tile), ending movement.
     * 2.  **Sunlight Reduction (3h)**: Applies the 3-hour sunlight reduction for the *current* tile being traversed.
     * 3.  **Movement Logging**: Logs the movement (`logCloudMovement`) and sunlight reduction for the tile being left.
     * 4.  **Delegation**: Delegates the hard-stop logic (Village/Merge) and successful move
     *      update to `cloudMovementHelper`.
     *
     * **Deferred Responsibilities (What We Leave Out for Other Functions):**
     * 1.  **Movement Loop Control**: The `while` loop (quota check and continued movement)
     *      is handled by `cloudLoopHelper`.
     * 2.  **Rain Check**: Handled by `cloudLoopHelper`.
     * 3.  **Final Stop Logic**: Handled by `cloudMovementHelper`.
     *
     * @param cloud The cloud to move.
     * @return `True` if the cloud successfully moved and can continue, or `False`
     *      if movement stopped (a null path, village, or merge).
     */
    private fun cloudMovement(cloud: Cloud): Boolean {


                val currentTile = environmentMap.getTileById(cloud.location)
                val nextTile = environmentMap.getAirflowNeighbour(currentTile)

                if (nextTile == null) {
                    //Cloud has reached its destination
                    return false

                }else{

                    // It is now certain that the cloud will move to the next tile.
                    // So, safe to decrement sunlight of Tile by 3
                    if (currentTile is FarmableTile) {
                        currentTile.reduceSunlight(3)
                    }

                    //The Cloud certainly moves. So might as well log the movement.
                    Logger.logCloudMovement(cloud.id, cloud.amount, currentTile.id, nextTile.id)

                    if((currentTile.getCategory() == TileCategory.FIELD
                                || currentTile.getCategory() == TileCategory.PLANTATION)
                        &&  currentTile is FarmableTile) {
                        Logger.logSunlightAmount(currentTile.id, currentTile.currentSunlight)

                    }


                    return cloudMovementHelper(nextTile, cloud);
                }

    }

    /**
     * Executes the final outcome of a cloud movement attempt.
     *
     * This function determines if the cloud will be stopped by a village, stopped by a merge,
     * or if it will successfully move to the next tile.
     *
     * **This Function's Responsibilities (What We Do Here):**
     * 1.  **Stop Check - Village**: Logs cloud getting stuck, calls `removeCloud`, and returns `false`.
     * 2.  **Stop Check - Merge**: If a cloud is found on the destination tile, it executes the full merge logic:
     * calls `merge()`, logs the union, removes original clouds, adds the new one, and returns `false`.
     * 3.  **Success Condition**: If the tile is empty and non-village, it calls `updateCloudLocation()`
     *      and returns `true`, allowing the movement loop to continue.
     *
     * **Deferred Responsibilities (What We Leave Out for Other Functions):**
     * 1.  **Sunlight Reduction (3h)**: Handled by `cloudMovement` before this function is called.
     * 2.  **Movement Logging**: Handled by `cloudMovement` before this function is called.
     * 3.  **Final Sunlight (50h)**: Deferred to `finalCloudBehaviour()`.
     *
     * @param nextTile The tile the cloud intends to move to.
     * @param cloud The cloud that is moving.
     * @return `True` if the cloud successfully moved and can continue, or `False` if movement stopped
     *      (village or merge).
     */

    private fun cloudMovementHelper(nextTile: Tile, cloud: Cloud): Boolean {
        when (nextTile.getCategory()) {
            TileCategory.VILLAGE -> {
                //The next tile is a village tile. Cloud should dissipate.

                Logger.logCloudStuckInVillage(cloud.id, nextTile.id)
                removeCloud(cloud)
                return false


            }

            else -> {
                val cloudOnNextTile = lookupCloud(nextTile)

                if (cloudOnNextTile == null) {
                    updateCloudLocation(cloud, nextTile)
                    //This should not break the while loop. The loop continues.
                    return true
                } else {


                    val newCloud = merge(cloud, cloudOnNextTile)

                    Logger.logCloudMerge(
                        cloud.id, cloudOnNextTile.id, newCloud.id,
                        newCloud.amount, newCloud.duration, newCloud.location
                    )

                    removeCloud(cloudOnNextTile)
                    removeCloud(cloud)
                    addCloud(newCloud)

                    return false
                }
            }
        }
    }



    /**
     * Updates a cloud's state after a successful single-tile movement.
     *
     * **This Function's Responsibilities (What We Do Here): **
     * 1.  **Update Location**: Sets the cloud's internal `location` property to the `nextTile.id`
     *      using the controlled setter.
     * 2.  **Decrement Quota**: Calls `cloud.decrementMovementQuota()` to track the movement toward the 10-tile limit.
     *
     * **Deferred Responsibilities (What We Leave Out for Other Functions): **
     * 1.  **Logging**: Movement logging (`logCloudMovement`) is performed *before* this function is called,
     *      as the logs must record the cloud's old location.
     *
     * @param cloud The cloud that moved.
     * @param nextTile The tile the cloud moved to.
     */
    private fun updateCloudLocation(cloud: Cloud, nextTile: Tile) {
        cloud.setLocation(nextTile.id)
        cloud.decrementMovementQuota()
    }

    /**
     * Merges two clouds into a single new cloud.
     *
     * This function creates a new Cloud entity with combined resources and properties
     * and increments the global maximum cloud ID.
     *
     * @param current The cloud that is moving.
     * @param other The cloud already on the tile.
     * @return The new merged cloud.
     *
     * Make sure you take into account that duration -1 means duration is unlimited.
     */
    private fun merge(current: Cloud, other: Cloud): Cloud {
        val newId = ++maxCloudId
        val newAmount = current.amount + other.amount
        val newDuration = if (current.duration == -1 || other.duration == -1) -1
                            else minOf(current.duration, other.duration)
        val newLocation = other.location
        val newMovementQuota =maxOf(current.movementQuota, other.movementQuota)

        val newCloud = Cloud(newId, newDuration, newAmount, newLocation, newMovementQuota)

        return newCloud
    }

    /**
     * Handles the final behavior of clouds for the current tick,
     * including duration decrement and logging their final positions and sunlight effects (50h loss).
     * This runs after all cloud movements are complete.
     * */
    private fun finalCloudBehaviour() {

        // --- PHASE 1: Duration Decrement and Removal (Handles Dissipation) ---
        // This phase MUST happen first to correctly set the 'dissipated during this tick' status.
        // We assume Interpretation 1 (Duration before Sunlight) is managed by the execution order
        // and this function's logic.
        // we are assuming that clouds dissipating because of duration also don't affect the last tile's sunlight.
        // i.e., they are also considered dissipated during the tick. page16, ln8

        for (cloud in clouds.toList()) {
            //This makes a shallow copy of the list. Safe to remove from the original List then
            if (cloud.duration > 0) cloud.decrementDuration()

            if (cloud.duration == 0) {
                Logger.logCloudDissipation(cloud.id, cloud.location)
                removeCloud(cloud)
            }
        }


        val farmableTilesId = (tileIdCloudMap.keys.toList()).sortedBy { it }
        for (id in farmableTilesId) {
            val cloud = tileIdCloudMap[id]
            val tile = environmentMap.getTileById(id)
            if (cloud != null && tile is FarmableTile) {
                tile.reduceSunlight(50)

                Logger.logCloudPosition(cloud.id, id, tile.currentSunlight)
            }
        }





    }



    /**
     * Updates the environmental state of tiles affected by a `CITY_EXPANSION` incident.
     * This will remove any clouds on that tile as they dissipate.
     *
     * @param tileId The ID of the tile that has become a village.
     */
    fun villageCreatedAt(tileId: Int) {
        val tile = environmentMap.getTileById(tileId)
        if (tile is FarmableTile) {
            tile.currentSunlight = 0
            tile.currentMoisture = 0
            tile.setCategory(TileCategory.VILLAGE)
        }
        val cloud = tileIdCloudMap[tileId]
        if (cloud != null) {
            removeCloud(cloud)
        }
    }



    /**
     * Sets the tick information data.
     *
     * @param clock The `TickInfoData` object.
     */
    fun setTickInfoData(clock: TickInfoData) {
        this.tickInfo = clock
    }
}

