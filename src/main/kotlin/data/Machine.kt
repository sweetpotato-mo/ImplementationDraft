package data

import enums.ActionType
import enums.PlantType

class Machine(
    val id: Int,
    val farmId: Int,
    val homeShedId: Int,
    val capableActions: Set<ActionType>,
    val capablePlants: Set<PlantType>,
    val actionDuration: Int,

) {
    // Properties that change during simulation
    var brokenUntil: Int? = null
    var cropsCarried: Pair<PlantType, Int>? = null
    var spentDays: Int = 0

    /**
     * Breaks the machine for a specified duration.
     * * This method is called when a machine is affected by a `BROKEN_MACHINE` incident.
     * It sets the `brokenUntil` property to the tick when the machine will become operational again.
     *
     * @param brokenUntil The tick number until which the machine is broken.
     */
    fun breakMachine(brokenUntil: Int) {
        this.brokenUntil = brokenUntil
    }

    /**
     * Gets the set of actions the machine is capable of performing.
     *
     * @return A set of `ActionType` values representing the machine's capabilities.
     */
    fun getActions(): Set<ActionType> {
        return capableActions
    }

    /**
     * Gets the set of plants the machine is capable of operating on.
     *
     * @return A set of `PlantType` values.
     */
    fun getCapablePlants(): Set<PlantType> {
        return capablePlants
    }

    /**
     * Increments the number of days the machine has been working in the current tick.
     *
     * This method is called to track the time a machine spends on actions within a single tick.
     */
    fun incrementSpentDays() {
        spentDays++
    }

    /**
     * Resets the number of days the machine has spent on actions.
     *
     * This method should be called at the beginning of each tick to reset the machine's work time.
     */
    fun resetSpentDays() {
        spentDays = 0
    }
}