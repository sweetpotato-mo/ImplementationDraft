package incidents

import enums.ActionType
import enums.PlantType
import tile.Tile
import data.Machine
import controllers.EnvironmentController

/**
 * Interface for all incident types. Each incident must have an ID, a tick, and an execute method.
 */
interface Incident {
    val id: Int
    val tick: Int

    /**
     * Executes the incident's effect on the simulation.
     */
    fun execute()
}

/**
 * Represents a Cloud Creation incident. It creates new clouds in a specified area.
 *
 * @property envCont The EnvironmentController to interact with for cloud creation.
 * @property tileIds A list of tile IDs where new clouds are to be created.
 * @property amount The amount of water in the newly created clouds.
 * @property duration The duration of the new clouds.
 */
class CloudCreationIncident(
    override val id: Int,
    override val tick: Int,
    private val envCont: EnvironmentController,
    private val tileIds: List<Int>,
    private val amount: Int,
    private val duration: Int
) : Incident {
    override fun execute() {
        TODO("Not yet implemented")
    }
}

/**
 * Represents an Animal Attack incident. It reduces the harvest on affected tiles.
 *
 * @property tiles A list of tiles affected by the attack.
 */
class AnimalAttackIncident(
    override val id: Int,
    override val tick: Int,
    private val tiles: List<Tile>
) : Incident {
    override fun execute() {
        TODO("Not yet implemented")
    }
}

/**
 * Represents a Bee Happy incident. It increases the harvest on pollinated plants.
 *
 * @property tiles A list of tiles affected by the incident.
 * @property effect The percentage effect on the harvest due to increased pollination.
 */
class BeeHappyIncident(
    override val id: Int,
    override val tick: Int,
    private val tiles: List<Tile>,
    private val effect: Int
) : Incident {
    override fun execute() {
        TODO("Not yet implemented")
    }
}

/**
 * Represents a Drought incident. It sets soil moisture to 0 and kills plants on affected tiles.
 *
 * @property tiles A list of tiles affected by the drought.
 */
class DroughtIncident(
    override val id: Int,
    override val tick: Int,
    private val tiles: List<Tile>
) : Incident {
    override fun execute() {
        TODO("Not yet implemented")
    }
}

/**
 * Represents a Broken Machine incident. It makes a machine unusable for a specified duration.
 *
 * @property duration The duration in ticks for which the machine is broken.
 * @property machine The machine affected by the incident.
 */
class BrokenMachineIncident(
    override val id: Int,
    override val tick: Int,
    private val duration: Int,
    private val machine: Machine
) : Incident {
    override fun execute() {
        TODO("Not yet implemented")
    }
}

/**
 * Represents a City Expansion incident. It transforms a tile into a village.
 *
 * @property tile The ID of the tile to be transformed.
 * @property envCont The EnvironmentController to notify about the tile transformation.
 */
class CityExpansionIncident(
    override val id: Int,
    override val tick: Int,
    private val tile: Int,
    private val envCont: EnvironmentController
) : Incident {
    override fun execute() {
        TODO("Not yet implemented")
    }
}