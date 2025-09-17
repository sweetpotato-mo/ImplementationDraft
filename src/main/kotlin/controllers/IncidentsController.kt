package controllers

import incidents.Incident

class IncidentController {
    /**
     * A map where the key is the tick number and the value is a list of incidents that occur at that tick.
     */
    private val incidents: Map<Int, List<Incident>> = mutableMapOf()

    /**
     * Executes all incidents scheduled for the current simulation tick.
     */
    fun executeCurrentIncidents() {
        TODO("Not yet implemented")
    }

    /**
     * Sets the tick information data.
     *
     * @param clock The `TickInfoData` object.
     */
    fun setTickInfoData(clock: TickInfoData) {
        TODO("Not yet implemented")
    }
}