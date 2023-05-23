package me.fzzyhmstrs.fzzymapz.screen

import me.fzzyhmstrs.fzzymapz.map.MapStack
import me.fzzyhmstrs.fzzymapz.theme.ThemeType
import me.fzzyhmstrs.fzzymapz.theme.ThemeTypeProviding
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier

abstract class RightClickAction(override val type: ThemeType, override val id: Identifier): ThemeTypeProviding {

    abstract val name: Text

    open fun appendTooltip(mapStack: MapStack, list: MutableList<TooltipComponent>){
    }

    abstract fun action(mapStack: MapStack, playerEntity: PlayerEntity, screen: Screen)

}