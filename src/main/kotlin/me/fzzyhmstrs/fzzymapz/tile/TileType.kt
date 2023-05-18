package me.fzzyhmstrs.fzzymapz.tile

import com.google.gson.JsonObject

abstract class TileType {

    abstract fun loadTile(json: JsonObject): Tile

    abstract fun saveTile(tile: Tile): JsonObject

}