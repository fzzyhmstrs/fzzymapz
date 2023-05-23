package me.fzzyhmstrs.fzzymapz.map

import me.fzzyhmstrs.fzzymapz.FM
import me.fzzyhmstrs.fzzymapz.point.DataPoint
import me.fzzyhmstrs.fzzymapz.point.DataPointRenderer
import net.minecraft.util.Identifier

class PointCache(val mapStack: MapStack) {

    private val pointRenderers: MutableMap<DataPoint,DataPointRenderer> = mutableMapOf()
    private var lastCachedDim = Identifier(FM.MOD_ID,"dirty")

}