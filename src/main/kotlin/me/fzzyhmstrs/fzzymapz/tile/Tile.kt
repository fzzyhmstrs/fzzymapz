package me.fzzyhmstrs.fzzymapz.tile

import com.google.gson.JsonObject
import me.fzzyhmstrs.fzzymapz.FM
import me.fzzyhmstrs.fzzymapz.registry.RegisterTile
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

abstract class Tile(val id: Identifier, val layer: MapLayer, val type: TileType, val hasContent: Boolean = true){
    
    fun drawTile(matrices: MatrixStack, x: Int, y: Int, scale: Float, texture: Identifier){
        if(hasContent){
            draw(matrices, x, y, scale, texture)
        }
    }

    abstract fun draw(matrices: MatrixStack, x: Int, y: Int, scale: Float, texture: Identifier)

    open fun toJson(): JsonObject {
        return type.saveTile(this)
    }

    object EMPTY: Tile(Identifier(FM.MOD_ID,"empty_tile"), RegisterTile.EmptyType, false){
        override fun draw(matrices: MatrixStack, x: Int, y: Int, scale: Float, texture: Identifier) {
        }
    }
}
