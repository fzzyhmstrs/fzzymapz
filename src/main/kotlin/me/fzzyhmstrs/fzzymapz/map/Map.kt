package me.fzzyhmstrs.fzzymapz.map

import me.fzzyhmstrs.fzzymapz.layer.MapLayer
import me.fzzyhmstrs.fzzymapz.registry.RegisterTheme
import me.fzzyhmstrs.fzzymapz.theme.Theme
import java.util.*

class Map(private val layerMap: SortedMap<Int,MapLayer>, defaultThemes: MutableMap<MapLayer,Theme> = mutableMapOf()) {

    private val themeMap: MutableMap<MapLayer,Theme> = mutableMapOf()
    val cache: TileCache by lazy{
        TileCache(this)
    }

    init{
        for (layer in layerMap.values){
            if (defaultThemes.containsKey(layer)){
                themeMap[layer] = defaultThemes[layer] ?: layer.type.getDefaultTheme()
            }
        }
    }

    fun getLayers(): Collection<MapLayer> {
        return layerMap.values
    }

    fun getCurrentTheme(layer: MapLayer): Theme{
        return themeMap[layer] ?: RegisterTheme.EMPTY_TYPE.EMPTY_THEME
    }

}