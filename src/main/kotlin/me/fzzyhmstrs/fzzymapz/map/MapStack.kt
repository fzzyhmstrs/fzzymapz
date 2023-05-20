package me.fzzyhmstrs.fzzymapz.map

import me.fzzyhmstrs.fzzymapz.layer.MapLayer
import me.fzzyhmstrs.fzzymapz.registry.RegisterTheme
import me.fzzyhmstrs.fzzymapz.theme.Theme
import net.minecraft.util.Identifier

class MapStack(val map: FzzyMap) {

    private val themeMap: MutableMap<MapLayer, Theme> = mutableMapOf()
    private val cache: TileCache = TileCache(map)

    private val pointData: MutableMap<Identifier,MutableMap<Int, MutableMap<Int, DataPoint>>> = mutableMapOf()

    init{
        for (layer in map.getLayers()){
            themeMap[layer] = map.getDefaultThemes()[layer] ?: layer.type.getDefaultTheme()
        }
    }

    fun getCurrentTheme(layer: MapLayer): Theme{
        return themeMap[layer] ?: RegisterTheme.EMPTY_TYPE.EMPTY_THEME
    }

}