package me.fzzyhmstrs.fzzymapz.tile.impl

import com.google.gson.JsonObject
import me.fzzyhmstrs.fzzymapz.FM
import me.fzzyhmstrs.fzzymapz.tile.Tile
import me.fzzyhmstrs.fzzymapz.tile.TileType
import net.minecraft.util.Identifier

object ConnectedBiomeType: TileType(Identifier(FM.MOD_ID,"connected_biome_type")) {

    fun getTileU(sum: Int): Int{
        return (sum % 4) * 16
    }

    fun getTileV(sum: Int): Int{
        return (sum / 4) * 16
    }

    fun updateTile(tile: ConnectedBiomeTile, north: Identifier, east: Identifier, south: Identifier, west: Identifier){
        val center = tile.id
        var sum = 0
        if (center == north) sum += 1
        if (center == east) sum += 2
        if (center == south) sum += 4
        if (center == west) sum += 8
        tile.u = getTileU(sum)
        tile.v = getTileV(sum)
    }

    override fun loadTile(json: JsonObject): Tile {
        TODO("Not yet implemented")
    }

    override fun saveTile(tile: Tile): JsonObject {
        TODO("Not yet implemented")
    }
}