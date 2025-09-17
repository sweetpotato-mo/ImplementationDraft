package session

import controllers.EnvironmentController
import controllers.IncidentController
import controllers.MachineController
import map.SimMap
import enums.LogLevel

/**
 * The main entry point for the simulation program.
 */
class Main {
    /**
     * The main function that starts the program. It will likely handle command-line arguments,
     * initialize the parser, and start the simulation loop.
     */
    fun main(args: Array<String>) {
        // Implementation for argument parsing and starting the simulation.
    }
}

/**
 * The main class that orchestrates the simulation. It holds references to all controllers
 * and manages the simulation's state over time.
 *
 * @property simMap The representation of the simulation map.
 * @property tickInfo An object to manage the current tick number and year tick.
 * @property maxTicks The maximum number of ticks the simulation will run for.
 * @property environmentController The controller for environmental factors.
 * @property machineController The controller for machine and farm actions.
 * @property incidentController The controller for incidents.
 */
class Simulation(
    private val simMap: SimMap,
    private val tickInfo: TickInfo,
    private val maxTicks: Int,
    private val environmentController: EnvironmentController,
    private val machineController: MachineController,
    private val incidentController: IncidentController
) {
    /**
     * Initializes the simulation. This method is called once before the main loop begins.
     * It may set up initial conditions for the controllers.
     */
    fun initialize() {
        // Sets up the simulation's initial state.
    }

    /**
     * The main simulation loop. This method will call `tick()` repeatedly until the
     * simulation reaches the `maxTicks`.
     */
    private fun loop() {
        // Implements the main loop that iterates through each tick.
    }

    /**
     * Represents a single step in the simulation. This method orchestrates the actions
     * that occur in one tick, in the correct order as specified by the documentation.
     */
    private fun tick() {
        // Calls the controllers in the specified order:
        // 1. EnvironmentController (reduce moisture, clouds)
        // 2. MachineController (farm actions)
        // 3. IncidentController (incidents)
        // 4. Update harvest estimates
    }

    /**
     * Updates the state of all farmable tiles at the end of a tick.
     * This is where harvest estimates are recomputed.
     */
    private fun updateFarmableTiles() {
        // Updates the harvest estimate for each farmable tile.
    }

    /**
     * Prints the final simulation statistics to the output.
     * This is called after the main loop has completed.
     */
    private fun printStatistics() {
        // Gathers and logs final statistics as required by the specification.
    }

    /**
     * Sets the tick information for the simulation.
     * This is used to pass the tick information to the controllers.
     */
    fun setTickInfo(tickInfo: TickInfo) {
        // Passes the tick information to the relevant components.
    }
}

/**
 * A simple data holder for the `TickInfo` object.
 */
class TickInfoData(private val tickInfo: TickInfo) {
    /**
     * Gets the current simulation tick.
     * @return The current tick number.
     */
    fun getCurrentTick(): Int = tickInfo.getCurrentTick()

    /**
     * Gets the current year tick.
     * @return The current year tick.
     */
    fun getYearTick(): Int = tickInfo.getYearTick()
}

/**
 * Manages the current tick of the simulation.
 * It tracks both the total simulation tick and the tick within the year.
 */
class TickInfo(
    private var currentTick: Int = 0,
    private var yearTick: Int
) {
    /**
     * Increments the current tick and updates the year tick.
     */
    fun increment() {
        currentTick++
        yearTick = (yearTick % 24) + 1
    }

    /**
     * Gets the current simulation tick.
     * @return The current tick number.
     */
    fun getCurrentTick(): Int = currentTick

    /**
     * Gets the current year tick.
     * @return The current year tick.
     */
    fun getYearTick(): Int = yearTick
}