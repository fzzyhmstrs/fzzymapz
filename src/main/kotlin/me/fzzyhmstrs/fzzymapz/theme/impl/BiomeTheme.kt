package me.fzzyhmstrs.fzzymapz.theme.impl

import me.fzzyhmstrs.fzzymapz.FM
import me.fzzyhmstrs.fzzymapz.registry.RegisterTheme
import me.fzzyhmstrs.fzzymapz.theme.Theme
import net.minecraft.util.Identifier

class BiomeTheme(private val map: MutableMap<Identifier, Identifier>): Theme(RegisterTheme.BIOME){

    override fun provide(inputId: Identifier): Identifier{
        return map[inputId] ?: FALLBACK
    }

    companion object{
        private val FALLBACK = Identifier(FM.MOD_ID,"")
    }
}