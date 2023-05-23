package me.fzzyhmstrs.fzzymapz.registry

import me.fzzyhmstrs.fzzymapz.FM
import me.fzzyhmstrs.fzzymapz.layer.MapLayer
import me.fzzyhmstrs.fzzymapz.layer.impl.SurfaceBiomeLayer
import me.fzzyhmstrs.fzzymapz.tile.Tile
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.world.World
import net.minecraft.world.chunk.WorldChunk

object RegisterLayer {

    val LAYERS = FabricRegistryBuilder.createDefaulted(MapLayer::class.java, Identifier(FM.MOD_ID,"map_layer"),
        Identifier(FM.MOD_ID,"empty_layer")).buildAndRegister()
    val EMPTY_LAYER = Registry.register(LAYERS, Identifier(FM.MOD_ID,"empty_layer"),EmptyLayer)

    //////////////////////////

    val SURFACE_BIOMES = Registry.register(LAYERS, SurfaceBiomeLayer.id, SurfaceBiomeLayer)

    //////////////////////////

    fun registerAll(){

    }

    object EmptyLayer: MapLayer(RegisterTheme.EMPTY_TYPE, Identifier(FM.MOD_ID,"empty_layer")) {

        override fun shouldProcessChunk(x: Int, z: Int, world: World, chunk: WorldChunk): Boolean {
            return false
        }

        override fun processChunkForTile(world: World, chunk: WorldChunk): Tile {
            return Tile.EMPTY
        }

        override fun save(){}

        override fun load(){}

        override fun getTile(dimKey: Identifier, x: Int, y: Int, z: Int): Tile {
            return Tile.EMPTY
        }

    }

}