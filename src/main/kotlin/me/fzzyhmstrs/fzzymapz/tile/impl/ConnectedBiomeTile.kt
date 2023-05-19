package me.fzzyhmstrs.fzzymapz.tile.impl

import me.fzzyhmstrs.fzzymapz.registry.RegisterTile
import me.fzzyhmstrs.fzzymapz.tile.Tile
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class ConnectedBiomeTile(id: Identifier, var u: Int, var v: Int): Tile(id,RegisterTile.CONNECTED_BIOME_TYPE) {

    override fun draw(matrices: MatrixStack, x: Int, y: Int, scale: Float, texture: Identifier) {
    }
}
