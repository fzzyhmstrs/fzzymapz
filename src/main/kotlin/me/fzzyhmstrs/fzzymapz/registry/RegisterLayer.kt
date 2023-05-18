package me.fzzyhmstrs.fzzymapz.registry

import me.fzzyhmstrs.fzzymapz.FM
import me.fzzyhmstrs.fzzymapz.layer.MapLayer
import me.fzzyhmstrs.fzzymapz.tile.Tile
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.world.World

object RegisterLayer {

    val LAYERS = FabricRegistryBuilder.createDefaulted(MapLayer::class.java, Identifier(FM.MOD_ID,"map_layer"),
        Identifier(FM.MOD_ID,"empty_layer")).buildAndRegister()
    val EMPTY_LAYER = Registry.register(LAYERS, Identifier(FM.MOD_ID,"empty_layer"),EmptyLayer)

    //////////////////////////

    //val SURFACE_BIOMES = Registry.register(LAYERS, Identifier(FM.MOD_ID,"surface_biomes"),SurfaceBiomesLayer)

    //////////////////////////

    fun registerAll(){

    }

    object EmptyLayer: MapLayer(RegisterTheme.EMPTY_TYPE) {
        override fun process(world: World){}

        override fun save(){}

        override fun load(){}

        override fun getTile(world: World, x: Int, z: Int): Tile {
            return Tile.EMPTY
        }

    }

}