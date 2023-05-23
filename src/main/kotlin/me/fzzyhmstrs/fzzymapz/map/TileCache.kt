package me.fzzyhmstrs.fzzymapz.map

import me.fzzyhmstrs.fzzymapz.FM
import me.fzzyhmstrs.fzzymapz.layer.MapLayer
import me.fzzyhmstrs.fzzymapz.theme.Theme
import me.fzzyhmstrs.fzzymapz.tile.TileStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier
import kotlin.math.abs

class TileCache(private val mapStack: MapStack){

    private var cacheData: List<List<List<TileStack>>> = listOf()
    private var lastCachedDim: Identifier = Identifier(FM.MOD_ID,"dirty")
    private var lastCachedX: Int = Int.MAX_VALUE
    private var lastCachedZ: Int = Int.MAX_VALUE
    private var lastCachedW: Int = 0
    private var lastCachedH: Int = 0

    fun getTiles(player: PlayerEntity, dimKey: Identifier): List<List<List<TileStack>>>{
        return getTiles(player, dimKey,lastCachedW,lastCachedH)
    }

    fun getTiles(player: PlayerEntity, dimKey: Identifier,w: Int, h: Int, startW: Int = 0, startH: Int = 0): List<List<List<TileStack>>>{
        var chunkWidth = w / 16
        if (w % 16 != 0) chunkWidth += 1
        var chunkHeight = h / 16
        if (h % 16 != 0) chunkHeight += 1
        if (chunkWidth > lastCachedW || chunkHeight > lastCachedH){
            updateCacheTiles(player, dimKey, w, h)
        }
        val tiles: MutableList<List<List<TileStack>>> = mutableListOf()
        var lastWIndex = chunkWidth + startW - 1
        if (lastWIndex >= cacheData.size){
            lastWIndex = cacheData.size - 1
        }
        var lastHIndex = chunkHeight + startH - 1
        for (wid in startW.. lastWIndex){
            val list = cacheData[wid]
            if (lastHIndex >= list.size){
                lastHIndex = list.size - 1
            }
            tiles.add(list.subList(startH,lastHIndex))
        }
        return tiles
    }

    fun updateCacheThemes(){
        for (zList in cacheData){
            for (tileList in zList){
                for (tile in tileList){
                    val theme = mapStack.getCurrentTheme(tile.tile.layer)
                    tile.updateTheme(theme)
                }
            }
        }
    }

    fun updateCacheTiles(player: PlayerEntity, dimKey: Identifier){
        updateCacheTiles(player, dimKey,lastCachedW,lastCachedH)
    }
    
    fun updateCacheTiles(player: PlayerEntity, dimKey: Identifier, w: Int, h: Int){
        val chunkPos = player.chunkPos
        var chunkWidth = w / 16
        if (w % 16 != 0) chunkWidth += 1
        var chunkHeight = h / 16
        if (h % 16 != 0) chunkHeight += 1
        if(lastCachedDim == dimKey && lastCachedX == chunkPos.x && lastCachedZ == chunkPos.z && chunkWidth == lastCachedW && chunkHeight == lastCachedH) return
        val newCache: MutableList<List<List<TileStack>>> = mutableListOf()
        //if the cache is structurally changing in a major way (new dimension, new size, moved more than one entire frame from it's last caching) rebuild from scratch
        val chunkWidthRange = if(chunkWidth % 2 != 0){
            ((-chunkWidth/2)+chunkPos.x)..((chunkWidth/2)+chunkPos.x)
        } else {
            ((-chunkWidth/2)+chunkPos.x) until (chunkWidth/2)+chunkPos.x
        }
        val chunkHeightRange = if(chunkHeight % 2 != 0){
            ((-chunkHeight/2)+chunkPos.z)..((chunkHeight/2)+chunkPos.z)
        } else {
            ((-chunkHeight/2)+chunkPos.z) until (chunkHeight/2)+chunkPos.z
        }
        if(lastCachedDim != dimKey || abs(chunkPos.x - lastCachedX) >= lastCachedW || abs(chunkPos.z - lastCachedZ) >= lastCachedH || chunkWidth != lastCachedW || chunkHeight != lastCachedH){

            fullNewBuild(newCache, dimKey, chunkWidthRange, chunkHeightRange, player.blockPos.y)
            cacheData = newCache
            lastCachedDim = dimKey
            lastCachedX = chunkPos.x
            lastCachedZ = chunkPos.z
            lastCachedW = chunkWidth
            lastCachedH = chunkHeight
            return
        }
        //otherwise we are partially on the same cache frame we were before, lets reuse what we can
        val xDelta = chunkPos.x - lastCachedX
        var xDeltaIndex = xDelta
        val zDelta = chunkPos.z - lastCachedZ
        if (zDelta == 0){
            
            for (wid in chunkWidthRange){
                if (xDeltaIndex < 0 || xDeltaIndex >= cacheData.size){
                    fullBuild(newCache, dimKey,wid,player.blockPos.y,chunkHeightRange)
                } else {
                    copyOver(newCache, xDeltaIndex)
                }
                xDeltaIndex++
            }
        } else if (zDelta > 0) {
            val partialChunkHeightRange = if(chunkHeight % 2 != 0){
                    ((chunkHeight/2)+chunkPos.z-zDelta+1)..((chunkHeight/2)+chunkPos.z)
                } else {
                ((chunkHeight/2)+chunkPos.z-zDelta) until (chunkHeight/2)+chunkPos.z
                }
            for (wid in chunkWidthRange){
                if (xDeltaIndex < 0 || xDeltaIndex >= cacheData.size){
                    fullBuild(newCache, dimKey,wid,player.blockPos.y,chunkHeightRange)
                } else {
                    partialBuildEnd(newCache,dimKey,wid,player.blockPos.y,partialChunkHeightRange,
                        cacheData[xDeltaIndex].subList(zDelta, cacheData[xDeltaIndex].size))
                }
                xDeltaIndex++
            }
        } else {
            val partialChunkHeightRange = ((-chunkHeight/2)+chunkPos.z) until (-chunkHeight/2)+chunkPos.z-zDelta
            for (wid in chunkWidthRange){
                if (xDeltaIndex < 0 || xDeltaIndex >= cacheData.size){
                    fullBuild(newCache, dimKey,wid,player.blockPos.y,chunkHeightRange)
                } else {
                    partialBuildFront(newCache,dimKey,wid,player.blockPos.y,partialChunkHeightRange,
                        cacheData[xDeltaIndex].subList(0, cacheData[xDeltaIndex].size - zDelta))
                }
                xDeltaIndex++
            }
        }
        cacheData = newCache
        lastCachedDim = dimKey
        lastCachedX = chunkPos.x
        lastCachedZ = chunkPos.z
        lastCachedW = chunkWidth
        lastCachedH = chunkHeight
    }

    private fun gatherLayers(dimKey: Identifier, x: Int, y: Int, z: Int): List<TileStack>{
        val layerList: MutableList<TileStack> = mutableListOf()
        for (layer: MapLayer in mapStack.map.getLayers()){
            val tile = layer.getTile(dimKey,x,y,z)
            if (tile.hasContent){
                val theme: Theme = mapStack.getCurrentTheme(tile.layer)
                layerList.add(TileStack(tile, theme.getScale(),theme.provide(tile.id)))
            }
            if (layer.opaque()){
                break
            }
        }
        layerList.reverse()
        return layerList
    }
    
    private fun fullNewBuild(newCache: MutableList<List<List<TileStack>>>, dimKey: Identifier, wRange: IntRange, hRange: IntRange, y: Int){
        for (w in wRange){
            fullBuild(newCache, dimKey, w, y, hRange)
        }
    }
    
    private fun fullBuild(newCache: MutableList<List<List<TileStack>>>,dimKey: Identifier, w: Int, y: Int, hRange: IntRange){
        val hList: MutableList<List<TileStack>> = mutableListOf()
        for (h in hRange){
            val list = gatherLayers(dimKey,w,y,h)
            hList.add(list)
        }
        newCache.add(hList)
    }
    
    private fun copyOver(newCache: MutableList<List<List<TileStack>>>, index: Int){
        newCache.add(cacheData[index])
    }
    
    private fun partialBuildEnd(newCache: MutableList<List<List<TileStack>>>,dimKey: Identifier, w: Int, y: Int, hRange: IntRange, existingData: List<List<TileStack>>){
        val hList: MutableList<List<TileStack>> = mutableListOf()
        hList.addAll(existingData)
        for (h in hRange){
            val list = gatherLayers(dimKey,w,y,h)
            hList.add(list)
        }
        newCache.add(hList)
    }
    private fun partialBuildFront(newCache: MutableList<List<List<TileStack>>>,dimKey: Identifier, w: Int, y: Int, hRange: IntRange, existingData: List<List<TileStack>>){
        val hList: MutableList<List<TileStack>> = mutableListOf()
        for (h in hRange){
            val list = gatherLayers(dimKey,w,y,h)
            hList.add(list)
        }
        hList.addAll(existingData)
        newCache.add(hList)
    }
  
}
