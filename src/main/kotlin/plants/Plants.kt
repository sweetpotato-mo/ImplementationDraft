package plants

import enums.ActionType
import enums.PlantType
import tile.FarmableTile

/**
 * A data class representing a specific action to be performed on a plant.
 *
 * @property originalTick The tick when the action was originally scheduled or should have occurred.
 * @property tileId The ID of the tile where the action is to take place.
 * @property action The type of action to be performed (e.g., SOWING, HARVESTING).
 * @property plant The type of plant the action is for.
 */
data class Action(
    val originalTick: Int,
    val tileId: Int,
    val action: ActionType,
    val plant: PlantType
)

/**
 * The base class for all plant strategies. It handles properties and methods common to all plants.
 */
abstract class PlantStrategy(
    private val plantType: PlantType,
    var currentHarvestEstimate: Int
) {
    var pollinationEffect: Double = 1.0
    var animalAttackEffect: Double = 1.0
    var isDroughtDamaged: Boolean = false
    val actionList: MutableMap<Int, List<Action>> = mutableMapOf()

    /**
     * Retrieves a list of actions needed for the plant at a specific tick.
     *
     * @param currentTick The current tick number.
     * @return A list of `Action` objects that need to be performed.
     */
    abstract fun getNeededActions(currentTick: Int): List<Action>

    /**
     * Calculates the amount of harvest to be collected based on the current harvest estimate.
     * After harvesting, the estimate is set to 0.
     *
     * @return The amount of harvest collected.
     */
    fun harvest(): Int {
        val harvestAmount = currentHarvestEstimate
        currentHarvestEstimate = 0
        return harvestAmount
    }

    /**
     * Applies the effect of insect pollination on the harvest estimate.
     * This method is fully implemented and relies on the plant's blooming period.
     *
     * @param effect The percentage effect of the pollination.
     */
    fun pollinate(effect: Int) {
        pollinationEffect = pollinationEffect * (1 + effect / 100.0)
    }

    /**
     * Gets the type of the plant.
     *
     * @return The `PlantType` of the plant.
     */
    fun getPlantType(): PlantType = plantType

    /**
     * Calculates and updates the harvest estimate for a tile at the end of a tick.
     *
     * @param tileID The ID of the tile.
     * @param tickInfo The current tick information.
     * @param sunlight The amount of sunlight received this tick.
     * @param moisture The current soil moisture level.
     */
    abstract fun calculateHarvest(tileID: Int, tickInfo: TickInfoData, sunlight: Int, moisture: Int)

    /**
     * Resets temporary effects on the harvest estimate, such as from animal attacks or pollination.
     */
    fun resetEffects() {
        animalAttackEffect = 1.0
        pollinationEffect = 1.0
    }

    /**
     * Reduces the harvest estimate due to an animal attack.
     */
    abstract fun animalAttack()

    /**
     * Computes penalties for missed actions and environmental factors.
     */
    protected abstract fun computePenalties(
        tickInfo: TickInfoData,
        actionList: List<Action>,
        sunlight: Int,
        moisture: Int
    )
}

/**
 * Base class for annual plants (e.g., crops that need to be sown each year).
 */
abstract class AnnualStrategy(plantType: PlantType) : PlantStrategy(plantType) {
    val possibleSowingYearTicks: List<Int> = plantType.possibleSowingYearTicks

    /**
     * Applies a penalty if weeding is missed.
     */
    abstract fun weed()
}

/**
 * Base class for perennial plants (e.g., trees that grow back each year).
 */
abstract class PerennialStrategy(plantType: PlantType) : PlantStrategy(plantType) {
    /**
     * Applies a penalty if mowing is missed.
     */
    abstract fun mow()

    /**
     * Applies a penalty if cutting is missed.
     */
    abstract fun cut()
}

/**
 * Represents the Potato plant, an annual crop.
 */
class Potato(tickInfoData: TickInfoData, tileId: Int) : AnnualStrategy(PlantType.POTATO) {
    override fun getNeededActions(currentTick: Int): List<Action> {
        TODO("Not yet implemented")
    }

    override fun calculateHarvest(tileID: Int, tickInfo: TickInfoData, sunlight: Int, moisture: Int) {
        TODO("Not yet implemented")
    }

    override fun animalAttack() {
        TODO("Not yet implemented")
    }

    override fun weed() {
        TODO("Not yet implemented")
    }

    override fun computePenalties(
        tickInfo: TickInfoData,
        actionList: List<Action>,
        sunlight: Int,
        moisture: Int
    ) {
        TODO("Not yet implemented")
    }
}

// ... other annual plant classes (Wheat, Oat, Pumpkin) would follow a similar pattern ...

/**
 * Represents the Apple plant, a perennial tree.
 */
class Apple(tickInfoData: TickInfoData, tileId: Int) : PerennialStrategy(PlantType.APPLE) {
    override fun getNeededActions(currentTick: Int): List<Action> {
        TODO("Not yet implemented")
    }

    override fun calculateHarvest(tileID: Int, tickInfo: TickInfoData, sunlight: Int, moisture: Int) {
        TODO("Not yet implemented")
    }

    override fun animalAttack() {
        TODO("Not yet implemented")
    }

    override fun mow() {
        TODO("Not yet implemented")
    }

    override fun cut() {
        TODO("Not yet implemented")
    }

    override fun computePenalties(
        tickInfo: TickInfoData,
        actionList: List<Action>,
        sunlight: Int,
        moisture: Int
    ) {
        TODO("Not yet implemented")
    }
}

// ... other perennial plant classes (Grape, Almond, Cherry) would follow a similar pattern ...