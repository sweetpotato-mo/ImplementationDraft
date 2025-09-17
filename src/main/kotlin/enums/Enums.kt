package enums

// An enumeration of all possible actions a machine can perform.
enum class ActionType {
    BLOOMING,
    SOWING,
    CUTTING,
    MOWING,
    WEEDING,
    IRRIGATING,
    HARVESTING
}

// An enumeration of all plants that can be grown in the simulation.
// It includes properties for preferred moisture, sunlight, and
// the ticks for sowing and harvesting, which can be used to set
// values for specific plant strategies later on.
enum class PlantType(
    val preferredMoisture: Int,
    val preferredSunlight: Int,
    val possibleHarvestYearTicks: List<Int>,
    val possibleSowingYearTicks: List<Int>
) {
    POTATO(
        preferredMoisture = 500,
        preferredSunlight = 130,
        possibleSowingYearTicks = listOf(7, 8, 9, 10), // April, May, early June, late June
        possibleHarvestYearTicks = listOf(17, 18, 19, 20) // September, October
    ),
    WHEAT(
        preferredMoisture = 450,
        preferredSunlight = 90,
        possibleSowingYearTicks = listOf(19, 20), // October
        possibleHarvestYearTicks = listOf(11, 12, 13) // June, early July
    ),
    OAT(
        preferredMoisture = 300,
        preferredSunlight = 90,
        possibleSowingYearTicks = listOf(5), // late March
        possibleHarvestYearTicks = listOf(13, 14, 15, 16) // July, August
    ),
    PUMPKIN(
        preferredMoisture = 600,
        preferredSunlight = 120,
        possibleSowingYearTicks = listOf(9, 10, 11, 12), // May, June
        possibleHarvestYearTicks = listOf(17, 18, 19, 20) // September, October
    ),
    APPLE(
        preferredMoisture = 100,
        preferredSunlight = 50,
        possibleSowingYearTicks = emptyList(),
        possibleHarvestYearTicks = listOf(17, 18, 19) // September, early October
    ),
    GRAPE(
        preferredMoisture = 250,
        preferredSunlight = 150,
        possibleSowingYearTicks = emptyList(),
        possibleHarvestYearTicks = listOf(17, 18) // early September
    ),
    ALMOND(
        preferredMoisture = 400,
        preferredSunlight = 130,
        possibleSowingYearTicks = emptyList(),
        possibleHarvestYearTicks = listOf(16, 17, 18, 19) // August, September, early October
    ),
    CHERRY(
        preferredMoisture = 150,
        preferredSunlight = 120,
        possibleSowingYearTicks = emptyList(),
        possibleHarvestYearTicks = listOf(13, 14) // July
    )
}

// An enumeration of the different categories of tiles on the map.
// This is a static property of each tile, defining its purpose and rules.
enum class TileCategory {
    VILLAGE,
    FIELD,
    PLANTATION,
    ROAD,
    MEADOW,
    FARMSTEAD,
    FOREST
}

// An enumeration of the 8 cardinal and intercardinal directions.
// This is used for cloud movement based on airflow properties.
enum class Direction {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST
}

// An enumeration for the different log levels.
// This allows for filtering the output based on the desired level of detail.
enum class LogLevel {
    INFO,
    DEBUG,
    IMPORTANT
}