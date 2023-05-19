package me.fzzyhmstrs.fzzymapz.layer.impl

import me.fzzyhmstrs.fzzymapz.layer.MapLayer
import me.fzzyhmstrs.fzzymapz.registry.RegisterTile
import me.fzzyhmstrs.fzzymapz.theme.impl.BiomeType
import me.fzzyhmstrs.fzzymapz.tile.Tile
import me.fzzyhmstrs.fzzymapz.tile.impl.ConnectedBiomeTile
import me.fzzyhmstrs.fzzymapz.tile.impl.ConnectedBiomeType
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.World
import net.minecraft.world.chunk.WorldChunk

object SurfaceBiomeLayer: MapLayer(BiomeType) {

    override fun processChunkForTile(world: World, chunk: WorldChunk): Tile {
        val biome = chunk.highestNonEmptySection?.getBiome(8,0,8) ?: return Tile.EMPTY
        val biomeId = world.registryManager.get(RegistryKeys.BIOME).getId(biome.value()) ?: return Tile.EMPTY
        val x = chunk.pos.x
        val z = chunk.pos.z
        val tile = ConnectedBiomeTile(biomeId,0,0)
        updateTile(tile, world, x, z)
        //update the four surrounding tiles, since this processor only looks directly at the current tile, the other tiles need to be updated once the chunks around it are loaded
        data[world.dimensionKey.value]?.get(z-1)?.get(x)?.let { if(it is ConnectedBiomeTile) updateTile(it,world, x, z-1) }
        data[world.dimensionKey.value]?.get(z)?.get(x+1)?.let { if(it is ConnectedBiomeTile) updateTile(it,world, x+1, z) }
        data[world.dimensionKey.value]?.get(z+1)?.get(x)?.let { if(it is ConnectedBiomeTile) updateTile(it,world, x, z+1) }
        data[world.dimensionKey.value]?.get(z)?.get(x-1)?.let { if(it is ConnectedBiomeTile) updateTile(it,world, x-1, z) }
        return tile
    }

    private fun updateTile(tile: ConnectedBiomeTile,world: World, x: Int, z: Int){
        val north = data[world.dimensionKey.value]?.get(z-1)?.get(x)?.id ?: RegisterTile.EMPTY.id
        val east = data[world.dimensionKey.value]?.get(z)?.get(x+1)?.id ?: RegisterTile.EMPTY.id
        val south = data[world.dimensionKey.value]?.get(z+1)?.get(x)?.id ?: RegisterTile.EMPTY.id
        val west = data[world.dimensionKey.value]?.get(z)?.get(x-1)?.id ?: RegisterTile.EMPTY.id
        ConnectedBiomeType.updateTile(tile, north, east, south, west)
    }
}