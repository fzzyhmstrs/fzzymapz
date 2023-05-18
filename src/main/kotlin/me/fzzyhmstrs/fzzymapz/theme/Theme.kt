package me.fzzyhmstrs.fzzymapz.theme

import me.fzzyhmstrs.fzzymapz.FM
import net.minecraft.util.Identifier
import net.minecraft.text.Text

abstract class Theme(val type: ThemeType){

    private var id: Identifier = Identifier(FM.MOD_ID,"missing_theme")
    private val name: Text by lazy {
        Text.translatable("fzzymapz.theme.${id.namespace}.${id.path}")
    }
    private var scale: Float = 1.0f

    // the name for the particular theme. Typical usage would be to display the current theme in a right click menu, for example.
    fun name(): Text{
        return this.name
    }

    fun getId(): Identifier{
        return this.id
    }
    fun setId(id: Identifier){
        this.id = id
    }

    //allows for themes to scale. Tiles use this to determine how to change their region area and tex size
    fun getScale(): Float{
        return this.scale
    }
    fun setScale(scale: Float){
        this.scale = scale
    }

    // core function of a theme is to provide a texture.
    // For example, a biome tile theme would accept a biome identifier as an input
    // and return the tile texture ID as it's result
    abstract fun provide(inputId: Identifier): Identifier



}