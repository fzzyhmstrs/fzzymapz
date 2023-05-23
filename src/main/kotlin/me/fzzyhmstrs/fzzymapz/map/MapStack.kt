package me.fzzyhmstrs.fzzymapz.map

import com.google.common.collect.ArrayListMultimap
import me.fzzyhmstrs.fzzymapz.point.DataPoint
import me.fzzyhmstrs.fzzymapz.theme.Theme
import me.fzzyhmstrs.fzzymapz.theme.ThemeTypeProviding
import net.minecraft.util.Identifier

class MapStack(val map: FzzyMap) {

    private val themeMap: MutableMap<ThemeTypeProviding, Theme> = mutableMapOf()
    private val tileCache: TileCache = TileCache(this)
    private val pointCache: PointCache = PointCache(this)

    internal var currentTarget: DataPoint? = null
    internal val pointData: ArrayListMultimap<Identifier,DataPoint> = ArrayListMultimap.create()

    init{
        for (layer in map.getLayers()){
            themeMap[layer] = map.getDefaultThemes()[layer] ?: layer.type.getDefaultTheme()
        }
    }

    fun getCurrentTheme(part: ThemeTypeProviding): Theme{
        return themeMap[part] ?: part.type.getDefaultTheme()
    }

    fun setCurrentTheme(part: ThemeTypeProviding,theme: Theme){
        themeMap[part] = theme
    }

}