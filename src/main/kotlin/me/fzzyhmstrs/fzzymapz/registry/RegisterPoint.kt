package me.fzzyhmstrs.fzzymapz.registry

import me.fzzyhmstrs.fzzymapz.FM
import me.fzzyhmstrs.fzzymapz.point.DataPointType
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.util.Identifier

object RegisterPoint {

    val POINT_TYPE = FabricRegistryBuilder.createDefaulted(
        DataPointType::class.java, Identifier(FM.MOD_ID,"data_point_types"),
        Identifier(FM.MOD_ID,"empty_point_type")).buildAndRegister()

}