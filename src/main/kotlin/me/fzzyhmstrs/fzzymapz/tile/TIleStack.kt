package me.fzzyhmstrs.fzzymapz.tile

import com.google.gson.JsonObject
import me.fzzyhmstrs.fzzymapz.FM
import me.fzzyhmstrs.fzzymapz.registry.RegisterTile
import me.fzzyhmstrs.fzzymapz.theme.Theme
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class TileStack(val tile: Tile, var scale: Float, var texture: Identifier){
    fun updateTheme(theme: Theme){
        this.scale = theme.getScale()
        this.texture = theme.provide(tile.id)
    }
    
    fun drawTile(matrices: MatrixStack, x: Int, y: Int){
        tile.drawTile(matrices, x, y, scale, texture)
    }
}
