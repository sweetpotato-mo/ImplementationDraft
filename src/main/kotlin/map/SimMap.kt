package map

import Tile.Tile
import Tile.FarmableTile
import data.Machine
import data.Cloud
import Tile.Coordinate

class SimMap {
    val tiles: MutableMap<Int, Tile> = mutableMapOf()
    val cordTileMap: MutableMap<Coordinate, Tile> = mutableMapOf()
    val farmableTiles: MutableList<FarmableTile> = mutableListOf()
    val tickTileToVillageMap: MutableMap<Int, List<Tile>> = mutableMapOf()
    val tileIdToCloud: MutableMap<Int, Cloud> = mutableMapOf()

    /**
     * Updates the list of farmable tiles in case of a `CITY_EXPANSION` incident.
     * This method is called when a tile is transformed into a village.
     *
     * @return A list of `FarmableTile` objects that are remaining.
     */
    fun updateFarmableTiles(): List<FarmableTile> {
        TODO("Not yet implemented")
    }

    /**
     * Calculates and returns the total estimated harvest remaining on all `FIELD` and `PLANTATION` tiles.
     * Harvest counts as collected only if it is on a machine or has been unloaded at a shed.
     *
     * @return The total harvest estimate in grams.
     */
    fun getRemainingHarvest(): Int {
        TODO("Not yet implemented")
    }
}