package map

import tile.Tile
import tile.FarmableTile
import data.Cloud
import enums.Direction
import enums.TileCategory

class EnvironmentMap(private val map: SimMap) {

    /**
     * Gets the tile adjoining a given tile in the direction of its airflow.
     * This is crucial for determining the next location of a cloud.
     *
     * @param tile The tile from which to find the neighbor.
     * @return The adjoining Tile in the airflow direction, or null if no such neighbor exists.
     */
    fun getAirflowNeighbour(tile: Tile): Tile? {
        TODO("Not yet implemented")
    }

    /**
     * Gets a list of all tiles that are of category FIELD or PLANTATION.
     *
     * @return A list of `FarmableTile` objects.
     */
    fun getFarmableTiles(): List<FarmableTile> {
        TODO("Not yet implemented")
    }

    /**
     * Gets a specific tile by its unique ID.
     *
     * @param id The unique ID of the tile.
     * @return The `Tile` object corresponding to the ID.
     */
    fun getTileById(id: Int): Tile {
        TODO("Not yet implemented")
    }

    /**
     * Gets a list of all tiles from the SimMap.
     *
     * @return A list of all `Tile` objects.
     */
    fun getTiles(): List<Tile> {
        TODO("Not yet implemented")
    }

    /**
     * Checks if a cloud is currently on a specific tile.
     *
     * @param tileId The ID of the tile to check.
     * @return True if a cloud is on the tile, false otherwise.
     */
    fun isCloudOnTile(tileId: Int): Boolean {
        TODO("Not yet implemented")
    }
}