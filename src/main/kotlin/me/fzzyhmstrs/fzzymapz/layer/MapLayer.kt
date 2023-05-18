package me.fzzyhmstrs.fzzymapz.layer

import me.fzzyhmstrs.fzzymapz.theme.ThemeType
import me.fzzyhmstrs.fzzymapz.tile.Tile
import net.minecraft.util.Identifier
import net.minecraft.world.World

abstract class MapLayer(val type: ThemeType){

    protected val data: MutableMap<Identifier,MutableMap<Int,MutableMap<Int,Tile>>> = mutableMapOf()

    // called by the chunk processor so the layer can grab and store any data it needs.
    abstract fun process(world: World)

    // methods for loading and saving the Layer data to the minecraft folder. Will be called by the layer registry on game close and periodically to save data, and on game join to load data
    abstract fun save()

    abstract fun load()

    // the main result of the layer. Provides the tile that the map renderer will use as needed to draw the screen
    open fun getTile(world: World, x: Int, z: Int): Tile{
        return data[world.dimensionKey.value]?.get(x)?.get(z)?:Tile.EMPTY
    }

    // true makes the tile "opaque". The highest layer with true as a return will be the only layer drawn at the current pos. The default structure layer, for example, is opaque so the biome bheind the structure isn't drawn.
    open fun opaque(): Boolean{
        return false
    }

}