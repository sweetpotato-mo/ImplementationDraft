package tile

import enums.ActionType
import enums.Direction
import enums.PlantType
import enums.TileCategory
import plants.Action
import plants.PlantStrategy
import session.TickInfoData

data class Coordinate(
    val x: Int,
    val y: Int
)

abstract class Tile {
    abstract val id: Int
    abstract val coordinates: Coordinate
    abstract val category: TileCategory
    abstract val farmId: Int?
    abstract val direction: Direction?

    fun getCategory(): TileCategory = category
    fun getId(): Int = id
    fun getFarmIdOfTile(): Int? = farmId
}

class FarmableTile(
    override val id: Int,
    override val coordinates: Coordinate,
    override val category: TileCategory,
    override val farmId: Int?,
    override val direction: Direction?,
    val moistureCapacity: Int
) : Tile() {
    var plant: PlantStrategy? = null
    var currentMoisture: Int = moistureCapacity
    var currentSunlight: Int = 0
    var fallowUntilTick: Int = -1

    /**
     * Reduces the harvest estimate due to an animal attack.
     * The effect depends on the type of plant.
     */
    fun animalAttack() {
        plant?.animalAttack()
    }

    /**
     * Increases the harvest estimate due to insect pollination.
     *
     * @param effect The percentage effect of the pollination.
     */
    fun insectPollination(effect: Int) {
        plant?.pollinate(effect)
    }

    /**
     * Marks the tile as having an action committed to it in a specific tick.
     * This affects harvest estimation penalties later on.
     *
     * @param action The action being performed on the tile.
     * @param tickInfoData The current tick information.
     * @return The duration of the action in days.
     */
    fun commitAction(action: Action, tickInfoData: TickInfoData): Int? {
        // Implementation for committing an action on the tile.
    }

    /**
     * Sows a new plant on the tile, setting its initial harvest estimate.
     *
     * @param plantType The type of plant to sow.
     * @param tickInfoData The current tick information.
     */
    fun sow(plantType: PlantType, tickInfoData: TickInfoData) {
        // Implementation for sowing logic.
    }

    /**
     * Removes the current crop from the tile, typically after harvesting or a drought.
     */
    fun removeCrop() {
        plant = null
    }

    /**
     * Increases the soil moisture to its maximum capacity.
     */
    fun irrigate() {
        currentMoisture = moistureCapacity
    }

    /**
     * Reduces the soil moisture and checks if it falls below the plant's threshold.
     *
     * @return True if the moisture is below the plant's required threshold, false otherwise.
     */
    fun reduceMoistureAndCheckBelowThreshold(): Boolean {
        // Reduces 70L or 100L and also tells if moisture is below threshold.
    }

    /**
     * Reduces the amount of sunlight the tile receives in the current tick.
     *
     * @param amt The amount of sunlight to reduce.
     */
    fun reduceSunlight(amt: Int) {
        currentSunlight = maxOf(0, currentSunlight - amt)
    }

    /**
     * Calculates the amount of rain needed to reach full soil moisture capacity.
     *
     * @return The amount of rain needed.
     */
    fun calculateReqRain(): Int {
        return moistureCapacity - currentMoisture
    }

    /**
     * Increases the soil moisture of the tile.
     *
     * @param amt The amount of water to add.
     */
    fun increaseMoisture(amt: Int) {
        currentMoisture = minOf(moistureCapacity, currentMoisture + amt)
    }

    /**
     * Gets a list of actions required for the current plant at the specified tick.
     *
     * @param currentTick The current tick number.
     * @return A list of required actions.
     */
    fun getRequiredActions(currentTick: Int): List<Action> {
        // Implementation for determining needed actions based on plant strategy.
    }

    /**
     * Sets the current sunlight for the tile for the current tick.
     *
     * @param amount The amount of sunlight to set.
     */
    fun setCurrentSunlight(amount: Int) {
        currentSunlight = amount
    }

    /**
     * Checks if the tile has a growing plant.
     */
    fun hasGrowingPlant(): Boolean = plant != null

    /**
     * Checks if the tile category is FIELD.
     */
    fun isField(): Boolean = category == TileCategory.FIELD

    /**
     * Checks if moisture is below the plant's required threshold.
     */
    fun isMoistureBelowThreshold(): Boolean = plant != null && currentMoisture < plant!!.getPlantType().preferredMoisture
}

class UnfarmableTile(
    override val id: Int,
    override val coordinates: Coordinate,
    override val category: TileCategory,
    override val farmId: Int?,
    override val direction: Direction?,
    val hasShed: Boolean
) : Tile()