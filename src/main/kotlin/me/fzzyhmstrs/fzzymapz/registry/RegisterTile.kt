package me.fzzyhmstrs.fzzymapz.registry

import com.google.gson.JsonObject
import me.fzzyhmstrs.fzzymapz.FM
import me.fzzyhmstrs.fzzymapz.tile.Tile
import me.fzzyhmstrs.fzzymapz.tile.TileType
import me.fzzyhmstrs.fzzymapz.tile.impl.ConnectedBiomeType
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RegisterTile {

    val TILES = FabricRegistryBuilder.createDefaulted(TileType::class.java, Identifier(FM.MOD_ID,"tile_type"),
    Identifier(FM.MOD_ID,"empty_type")).buildAndRegister()
    val EMPTY = Registry.register(TILES, Identifier(FM.MOD_ID,"empty_type"), EmptyType)

    /////////////////////
    val CONNECTED_BIOME_TYPE = Registry.register(TILES,Identifier(FM.MOD_ID,"connected_biome_type"), ConnectedBiomeType)

    fun registerAll(){

    }

    object EmptyType: TileType(Identifier(FM.MOD_ID,"empty_tile_type")){
        override fun loadTile(json: JsonObject): Tile {
            return Tile.EMPTY
        }

        override fun saveTile(tile: Tile): JsonObject {
            return JsonObject()
        }

    }
}