package map

import tile.Tile
import tile.Coordinate
import data.Machine
import enums.TileCategory

class MachineMap(private val map: SimMap) {

    /**
     * Retrieves a list of tiles within a specific radius of a central tile.
     * This is used for finding tiles for "similar actions" like sowing.
     *
     * @param center The central `Tile` object.
     * @param radius The radius around the center tile, measured in tiles.
     * @return A mutable list of `Tile` objects within the specified range.
     */
    fun getTilesInRange(center: Tile, radius: Int): MutableList<Tile> {
        TODO("Not yet implemented")
    }

    /**
     * Gets a tile by its unique ID.
     * This method is used to locate farmsteads and other specific tiles.
     *
     * @param id The unique ID of the tile.
     * @return The `Tile` object with the given ID.
     */
    fun getTileById(id: Int): Tile {
        TODO("Not yet implemented")
    }

    /**
     * Checks if a path exists between a starting tile and an ending tile for a machine.
     * This method considers a machine's ability to traverse different tile categories, as
     * well as whether it is carrying harvest.
     *
     * @param start The starting tile.
     * @param end The destination tile.
     * @param hasHarvest A boolean indicating if the machine is carrying harvest.
     * @param farmId The ID of the farm the machine belongs to.
     * @return True if a valid path exists, false otherwise.
     */
    fun isTraversable(start: Tile, end: Tile, hasHarvest: Boolean, farmId: Int): Boolean {
        TODO("Not yet implemented")
    }
}