package me.fzzyhmstrs.fzzymapz.theme.impl

import com.google.gson.JsonObject
import me.fzzyhmstrs.fzzymapz.FM
import me.fzzyhmstrs.fzzymapz.theme.Theme
import me.fzzyhmstrs.fzzymapz.theme.ThemeType
import net.minecraft.util.Identifier

object IconType: ThemeType() {


    override fun defaultTheme(): Identifier {
        return Identifier(FM.MOD_ID,"tan_monochrome_biomes")
    }

    override fun loadTheme(json: JsonObject): Theme {
        TODO("Not yet implemented")
    }

}