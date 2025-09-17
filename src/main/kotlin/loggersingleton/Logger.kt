package loggersingleton

import enums.ActionType
import enums.LogLevel
import enums.PlantType
import java.io.PrintWriter

object Logger {
    private var level: LogLevel = LogLevel.INFO // Default level
    private lateinit var writer: PrintWriter

    fun setWriter(writer: PrintWriter) {
        this.writer = writer
    }

    fun setLogLevel(level: LogLevel) {
        this.level = level
    }

    private fun shouldLog(messageLevel: LogLevel): Boolean {
        return when (level) {
            LogLevel.IMPORTANT -> messageLevel == LogLevel.IMPORTANT
            LogLevel.INFO -> messageLevel == LogLevel.IMPORTANT || messageLevel == LogLevel.INFO
            LogLevel.DEBUG -> true
        }
    }

    private fun log(messageLevel: LogLevel, message: String) {
        if (shouldLog(messageLevel)) {
            writer.println(message)
        }
    }

    fun logSuccessfulParse(filename: String) {
        log(LogLevel.INFO, "[INFO] Initialization Info: $filename successfully parsed and validated.")
    }

    fun logInvalidFile(filename: String) {
        log(LogLevel.IMPORTANT, "[IMPORTANT] Initialization Info: $filename is invalid.")
    }

    fun logSimulationStart(yearTick: Int) {
        log(LogLevel.INFO, "[INFO] Simulation Info: Simulation started at tick $yearTick within the year.")
    }

    fun logSimulationEnd(tick: Int) {
        log(LogLevel.IMPORTANT, "[IMPORTANT] Simulation Info: Simulation ended at tick $tick.")
    }

    fun logSimulationTickStart(tick: Int, yearTick: Int) {
        log(LogLevel.INFO, "[INFO] Simulation Info: Tick $tick started at tick $yearTick within the year.")
    }

    fun logSoilMoistureBelowThreshold(fieldCount: Int, plantationCount: Int) {
        log(LogLevel.INFO, "[INFO] Soil Moisture: The soil moisture is below threshold in $fieldCount FIELD and $plantationCount PLANTATION tiles.")
    }

    fun logRain(cloudId: Int, tileId: Int, fluidAmount: Int) {
        log(LogLevel.IMPORTANT, "[IMPORTANT] Cloud Rain: Cloud $cloudId on tile $tileId rained down $fluidAmount L water.")
    }

    fun logCloudMovement(cloudId: Int, fluidAmount: Int, startTileId: Int, endTileId: Int) {
        log(LogLevel.INFO, "[INFO] Cloud Movement: Cloud $cloudId with $fluidAmount L water moved from tile $startTileId to tile $endTileId.")
    }

    fun logSunlightAmount(tileId: Int, sunlightAmount: Int) {
        log(LogLevel.DEBUG,
            "[DEBUG] Cloud Movement: On tile $tileId, the amount of sunlight is $sunlightAmount.")
    }

    fun logCloudMerge(movingCloudId: Int, movedToCloudId: Int,
                      newCloudId: Int, waterAmount: Int, duration: Int, tileId: Int) {
        log(LogLevel.IMPORTANT,
            "[IMPORTANT] Cloud Union: Clouds $movingCloudId and " +
                    "$movedToCloudId united to cloud $newCloudId with $waterAmount L water and duration " +
                    "$duration on tile $tileId.")
    }

    fun logCloudStuckInVillage(cloudId: Int, tileId: Int) {
        log(LogLevel.INFO,
            "[INFO] Cloud Dissipation: Cloud $cloudId got stuck on tile $tileId.")
    }

    fun logCloudDissipation(cloudId: Int, tileId: Int) {
        log(LogLevel.INFO,
            "[INFO] Cloud Dissipation: Cloud $cloudId dissipates on tile $tileId.")
    }

    fun logCloudPosition(cloudId: Int, tileId: Int, sunlightAmount: Int) {
        log(LogLevel.DEBUG,
            "[DEBUG] Cloud Position: Cloud $cloudId is on tile $tileId, where the amount of sunlight is $sunlightAmount.")
    }

    fun logFarmActionStart(farmId: Int) {
        log(LogLevel.IMPORTANT, "[IMPORTANT] Farm: Farm $farmId starts its actions.")
    }

    fun logFarmSowingIntention(farmId: Int, sowingPlanIds: List<Int>) {
        val idsString = if (sowingPlanIds.isEmpty()) "" else sowingPlanIds.joinToString(",")
        log(LogLevel.DEBUG,
            "[DEBUG] Farm: Farm $farmId has the following active sowing plans it intends to pursue in this tick: $idsString.")
    }

    fun logMachineAction(machineId: Int, action: ActionType, tileId: Int, duration: Int) {
        log(LogLevel.IMPORTANT,
            "[IMPORTANT] Farm Action: Machine $machineId performs $action on tile $tileId for $duration days.")
    }

    fun logSowing(machineId: Int, plant: PlantType, sowingPlanId: Int) {
        log(LogLevel.IMPORTANT,
            "[IMPORTANT] Farm Sowing: Machine $machineId has sowed $plant according to sowing plan $sowingPlanId.")
    }

    fun logHarvest(machineId: Int, amount: Int, plant: PlantType) {
        log(LogLevel.IMPORTANT,
            "[IMPORTANT] Farm Harvest: Machine $machineId has collected $amount g of $plant harvest.")
    }

    fun logMachineActionFinish(machineId: Int, tileId: Int) {
        log(LogLevel.IMPORTANT,
            "[IMPORTANT] Farm Machine: Machine $machineId is finished and returns to the shed at $tileId.")
    }

    fun logMachineStuck(machineId: Int) {
        log(LogLevel.IMPORTANT,
            "[IMPORTANT] Farm Machine: Machine $machineId is finished but failed to return.")
    }

    fun logMachineUnload(machineId: Int, amount: Int, plant: PlantType) {
        log(LogLevel.IMPORTANT,
            "[IMPORTANT] Farm Machine: Machine $machineId unloads $amount g of $plant harvest in the shed.")
    }

    fun logFarmActionFinish(farmId: Int) {
        log(LogLevel.IMPORTANT, "[IMPORTANT] Farm: Farm $farmId finished its actions.")
    }

    fun logIncident(incidentId: Int, incidentType: String, affectTileIds: List<Int>) {
        val idsString = affectTileIds.sorted().joinToString(",")
        log(LogLevel.IMPORTANT,
            "[IMPORTANT] Incident: Incident $incidentId of type $incidentType happened and affected tiles $idsString.")
    }

    fun logUnperformedActions(tileId: Int, actions: List<ActionType>) {
        val actionsString = actions.sortedBy { it.name }.joinToString(",")
        log(LogLevel.DEBUG, "[DEBUG] Harvest Estimate: Required actions on tile $tileId were not performed: $actionsString.")
    }

    fun logHarvestEstimate(tileId: Int, amount: Int, plant: PlantType) {
        log(LogLevel.INFO, "[INFO] Harvest Estimate: Harvest estimate on tile $tileId changed to $amount g of $plant.")
    }

    fun logStatisticsCalculated() {
        log(LogLevel.IMPORTANT, "[IMPORTANT] Simulation Statistics: Simulation statistics are calculated.")
    }

    fun logFarmCollectedStatistics(farmId: Int, amount: Int) {
        log(LogLevel.IMPORTANT, "[IMPORTANT] Simulation Statistics: Farm $farmId collected $amount g of harvest.")
    }

    fun logPlantCollectedStatistics(plant: PlantType, amount: Int) {
        log(LogLevel.IMPORTANT, "[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: $amount g.")
    }

    fun logPlantLeftStatistics(amount: Int) {
        log(LogLevel.IMPORTANT, "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: $amount g.")
    }
}