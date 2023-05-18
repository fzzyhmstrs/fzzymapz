package me.fzzyhmstrs.fzzymapz.tile

import me.fzzyhmstrs.fzzymapz.FM
import me.fzzyhmstrs.fzzymapz.registry.RegisterTile
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

abstract class Tile(val texture: Identifier, val isEmpty: Boolean = false, val type: TileType){

      abstract fun draw(matrices: MatrixStack, x: Int, y: Int, hc: Boolean)
      
      open fun toJson(): JsonObject{
            return type.saveTile(this)
      }
      
      object EMPTY: Tile(Identifier(FM.MOD_ID,"empty"),true, RegisterTile.EmptyType){
          override fun draw(matrices: MatrixStack, x: Int, y: Int, hc: Boolean) {
          }
      }
  }
