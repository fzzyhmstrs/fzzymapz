package me.fzzyhmstrs.fzzymapz.theme.impl

import me.fzzyhmstrs.fzzymapz.registry.RegisterTheme
import me.fzzyhmstrs.fzzymapz.theme.Theme
import net.minecraft.util.Identifier

class IconTheme(private val map: MutableMap<Identifier, Identifier>): Theme(RegisterTheme.ICON) {

    override fun provide(inputId: Identifier): Identifier {
        return map[inputId] ?: FALLBACK
    }
}