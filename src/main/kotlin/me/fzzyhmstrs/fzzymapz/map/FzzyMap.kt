package me.fzzyhmstrs.fzzymapz.map

import me.fzzyhmstrs.fzzymapz.layer.MapLayer
import me.fzzyhmstrs.fzzymapz.theme.Theme
import net.minecraft.util.Identifier
import java.util.*

class FzzyMap(
    val id: Identifier,
    private val layerMap: SortedMap<Int,MapLayer>,
    private val defaultThemes: Map<MapLayer,Theme> = mapOf())
{

    fun newStack(): MapStack{
        return MapStack(this)
    }



    fun getLayers(): Collection<MapLayer> {
        return layerMap.values
    }

    fun getDefaultThemes(): Map<MapLayer,Theme>{
        return defaultThemes
    }

    class Builder(private val id: Identifier){
        private val layerMap: SortedMap<Int,MapLayer> = sortedMapOf()
        private val defaultThemes: MutableMap<MapLayer,Theme> = mutableMapOf()

        fun withLayer(layer: MapLayer,priority: Int): Builder{
            layerMap[priority] = layer
            return this
        }
        fun withLayer(layer: MapLayer,priority: Int, defaultTheme: Identifier): Builder{
            layerMap[priority] = layer
            val theme = layer.type.getTheme(defaultTheme)
            defaultThemes[layer] = theme
            return this
        }
    }

}