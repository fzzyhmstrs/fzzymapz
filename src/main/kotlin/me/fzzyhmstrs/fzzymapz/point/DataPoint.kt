package me.fzzyhmstrs.fzzymapz.point

import me.fzzyhmstrs.fzzy_core.nbt_util.Nbt
import me.fzzyhmstrs.fzzymapz.registry.RegisterPoint
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

abstract class DataPoint(val type: DataPointType, val pos: BlockPos) {

    open fun writeNbtCustomData(nbtCompound: NbtCompound){}
    open fun readNbtCustomData(nbtCompound: NbtCompound){}

    fun toNbt(nbtCompound: NbtCompound){
        nbtCompound.putString("id",type.id.toString())
        Nbt.writeBlockPos("pos", pos, nbtCompound)
        writeNbtCustomData(nbtCompound)
    }

    companion object {
        fun fromNbt(nbtCompound: NbtCompound): DataPoint {
            val idString = nbtCompound.getString("id").ifBlank { throw IllegalStateException("DataPoint ID in nbt is blank!: $nbtCompound") }
            val id = Identifier.tryParse(idString)?: throw IllegalStateException("DataPoint ID $idString is not a valid Identifier!")
            val dataPointType = RegisterPoint.POINT_TYPE.get(id)
            val pos = Nbt.readBlockPos("pos",nbtCompound)
            val dataPoint = dataPointType.create(dataPointType, pos)
            dataPoint.readNbtCustomData(nbtCompound)
            return dataPoint
        }
    }

}