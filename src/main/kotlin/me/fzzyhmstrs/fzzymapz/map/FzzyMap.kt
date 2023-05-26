package me.fzzyhmstrs.fzzymapz.map

import me.fzzyhmstrs.fzzymapz.layer.MapLayer
import me.fzzyhmstrs.fzzymapz.overlay.MapOverlay
import me.fzzyhmstrs.fzzymapz.overlay.impl.MapIcon
import me.fzzyhmstrs.fzzymapz.screen.RightClickAction
import me.fzzyhmstrs.fzzymapz.theme.Theme
import me.fzzyhmstrs.fzzymapz.theme.ThemeTypeProviding
import net.minecraft.util.Identifier
import java.util.*

class FzzyMap private constructor(
    val id: Identifier,
    private val layerMap: SortedMap<Int,MapLayer>,
    private val defaultThemes: Map<ThemeTypeProviding,Identifier> = mapOf(),
    private val overlays: List<MapOverlay> = listOf(),
    private val actions: List<RightClickAction> = listOf())
{

    private val builtDefaultThemes: Map<ThemeTypeProviding,Theme> by lazy{
        buildDefaultThemeMap()
    }
    private fun buildDefaultThemeMap(): Map<ThemeTypeProviding,Theme>{
        val map: MutableMap<ThemeTypeProviding,Theme> = mutableMapOf()
        for (entry in defaultThemes){
            map[entry.key] = entry.key.type.getTheme(entry.value)
        }
        return map
    }

    fun newStack(): MapStack{
        return MapStack(this)
    }

    fun getLayers(): Collection<MapLayer> {
        return layerMap.values
    }

    fun getDefaultThemes(): Map<ThemeTypeProviding,Theme>{
        return builtDefaultThemes
    }

    class Builder(private val id: Identifier){
        private val layerMap: SortedMap<Int,MapLayer> = sortedMapOf()
        private val defaultThemes: MutableMap<ThemeTypeProviding,Identifier> = mutableMapOf()
        private val overlays: MutableList<MapOverlay> = mutableListOf()
        private val actions: MutableList<RightClickAction> = mutableListOf()

        fun withLayer(layer: MapLayer,priority: Int): Builder{
            layerMap[priority] = layer
            return this
        }
        fun withLayer(layer: MapLayer,priority: Int, defaultTheme: Identifier): Builder{
            layerMap[priority] = layer
            defaultThemes[layer] = defaultTheme
            return this
        }
        fun withOverlay(overlay: MapOverlay): Builder{
            overlays.add(overlay)
            return this
        }
        fun withOverlay(overlay: MapOverlay, defaultTheme: Identifier): Builder{
            overlays.add(overlay)
            defaultThemes[overlay] = defaultTheme
            return this
        }
        fun withRightClickAction(action: RightClickAction): Builder{
            actions.add(action)
            return this
        }
        fun withRightClickAction(action: RightClickAction, defaultTheme: Identifier): Builder{
            actions.add(action)
            defaultThemes[action] = defaultTheme
            return this
        }
        fun withDefaultTheme(part: ThemeTypeProviding, defaultTheme: Identifier): Builder{
            defaultThemes[part] = defaultTheme
            return this
        }
    }

}