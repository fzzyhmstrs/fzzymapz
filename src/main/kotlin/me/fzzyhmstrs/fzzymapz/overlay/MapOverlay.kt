package me.fzzyhmstrs.fzzymapz.overlay

import me.fzzyhmstrs.fzzymapz.theme.ThemeType
import me.fzzyhmstrs.fzzymapz.theme.ThemeTypeProviding
import net.minecraft.util.Identifier

abstract class MapOverlay(override val type: ThemeType, override val id: Identifier): ThemeTypeProviding {
}