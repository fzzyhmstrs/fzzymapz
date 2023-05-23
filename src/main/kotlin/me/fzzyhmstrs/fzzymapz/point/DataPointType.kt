package me.fzzyhmstrs.fzzymapz.point

import me.fzzyhmstrs.fzzymapz.theme.ThemeType
import me.fzzyhmstrs.fzzymapz.theme.ThemeTypeProviding
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

abstract class DataPointType(override val type: ThemeType, override val id: Identifier): ThemeTypeProviding {

    abstract fun create(type: DataPointType, pos: BlockPos): DataPoint

}