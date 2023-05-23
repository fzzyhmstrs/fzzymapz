package me.fzzyhmstrs.fzzymapz.theme

import com.google.gson.JsonObject
import me.fzzyhmstrs.fzzymapz.registry.RegisterTheme
import net.minecraft.util.Identifier

abstract class ThemeType{

    private val themes: MutableMap<Identifier,Theme> = mutableMapOf()

    //the theme loader will select a theme from the registry based on the
    abstract fun loadTheme(json: JsonObject): Theme

    protected abstract fun defaultTheme(): Identifier

    fun getDefaultTheme(): Theme{
        return themes[defaultTheme()]?:RegisterTheme.EMPTY_TYPE.EMPTY_THEME
    }

    //provides the list of themes loaded to this type
    fun getThemes(): Collection<Theme>{
        return this.themes.values
    }
    fun getTheme(id: Identifier): Theme{
        return themes[id]?:getDefaultTheme()
    }
    fun addTheme(id: Identifier, theme: Theme){
        this.themes[id] = theme
    }

}