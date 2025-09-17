package farms

import data.Machine
import tile.Tile
import enums.PlantType
import plants.Action

/**
 * The main class for a farm, which orchestrates machine actions and manages resources.
 *
 * @property id The unique identifier of the farm.
 * @property fields A list of field tiles owned by the farm.
 * @property plantations A list of plantation tiles owned by the farm.
 * @property farmsteads A list of farmstead tiles owned by the farm.
 * @property machines A list of machines owned by the farm.
 * @property sowingPlan A map of sowing plans, with a tick as the key and a list of plans as the value.
 */
class Farm(
    private val id: Int,
    private val fields: MutableList<Tile>,
    private val plantations: MutableList<Tile>,
    private val farmsteads: MutableList<Tile>,
    private val machines: List<Machine>,
    private val sowingPlan: MutableMap<Int, MutableList<SowingPlan>>
) {

    /**
     * Gets the unique identifier of the farm.
     * @return The farm's ID.
     */
    fun getFarmId(): Int {
        TODO("Not yet implemented")
    }

    /**
     * Updates the list of farmable tiles in case of a `CITY_EXPANSION` or `DROUGHT` incident.
     */
    fun updateFarmableTiles() {
        TODO("Not yet implemented")
    }

    /**
     * Gets a list of active sowing plans for the current tick.
     * The plans are sorted by priority.
     * @param currentTick The current simulation tick.
     * @return A list of active sowing plans.
     */
    fun getCurrentSowingPlans(currentTick: Int): MutableList<SowingPlan> {
        // This function and getCurrentActionsByPriority return the results sorted by priority.
        TODO("Not yet implemented")
    }

    /**
     * Handles the delaying of sowing plans that could not be fulfilled in the current tick.
     */
    fun delaySowingPlan() {
        TODO("Not yet implemented")
    }

    /**
     * Gets a pair of lists representing all pending actions, sorted by priority.
     * The first list contains high-priority actions, and the second contains low-priority actions.
     * @param currentTick The current simulation tick.
     * @return A pair of lists of `Action` objects.
     */
    fun getCurrentActionsByPriority(currentTick: Int): Pair<MutableList<Action>, MutableList<Action>> {
        TODO("Not yet implemented")
    }

    /**
     * Gets a list of all machines owned by the farm.
     * @return A list of machines, sorted first by duration, then by ID in ascending order.
     */
    fun getMachines(): List<Machine> {
        // Returns the machines sorted first by duration then by id (ascending).
        TODO("Not yet implemented")
    }

    /**
     * Gets a list of all shed tiles owned by the farm.
     * @return A list of shed tiles.
     */
    fun getSheds(): List<Tile> {
        TODO("Not yet implemented")
    }

    /**
     * Removes a machine from the farm's active machine list.
     * @param machine The machine to remove.
     */
    fun removeMachine(machine: Machine) {
        TODO("Not yet implemented")
    }

    /**
     * Deletes a sowing plan after it has been fulfilled.
     * @param sp The sowing plan to delete.
     */
    fun deleteSowingPlan(sp: SowingPlan) {
        TODO("Not yet implemented")
    }
}

/**
 * A data class representing a farm's sowing plan.
 *
 * @property id The unique identifier of the sowing plan.
 * @property plantToBeSown The type of plant to be sown.
 * @property affectedTiles The list of tiles targeted by this plan.
 */
data class SowingPlan(
    val id: Int,
    val plantToBeSown: PlantType,
    val affectedTiles: MutableList<Tile>
)