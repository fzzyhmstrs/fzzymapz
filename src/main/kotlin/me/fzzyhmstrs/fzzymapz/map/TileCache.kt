package me.fzzyhmstrs.fzzymapz.map

import me.fzzyhmstrs.fzzymapz.FM
import net.minecraft.util.math.ChunkPos

class TileCache(private val map: Map){

    private var cacheData: List<List<List<TileStack>>> = listOf()
    private var lastCachedDim: Identifier = Identifier(FM.MOD_ID,"dirty")
    private var lastCachedX: Int = Int.MAX_VALUE
    private var lastCachedZ: Int = Int.MAX_VALUE
    private var lastCachedW: Int = 0
    private var lastCachedH: Int = 0
    
    fun updateCacheTheme(){
        for (xList in cacheData){
            for (zList in xList){
                for (tileList in zList){
                    for (tile in tileList){
                        val theme = map.getCurrentTheme(tile.tile.layer)
                        tile.updateTheme(theme)
                    }
                }
            }
        }
    }
    
    fun updateCacheTiles(player: PlayerEntity, dimKey: Identifier, w: Int, h: Int){
        val chunkPos = player.chunkPos
        chunkWidth = w / 16
        if (w % 16 != 0) chunkWidth += 1
        chunkHeight = h / 16
        if (h % 16 != 0) chunkHeight += 1
        if(lastCachedDim == dimKey && lastCachedX == chunkPos.x && lastCachedZ == chunkPos.z && chunkWidth == lastCachedW && chunkHeight == lastCachedH) return
        val newCache: MutableList<List<List<TileStack>>> = mutableListOf()
        //if the cache is structurally changing in a major way (new dimension, new size, moved more than one entire frame from it's last caching) rebuild from scratch
        if(lastCachedDim != dimKey || abs(chunkPos.x - lastCachedX) >= lastCachedW || abs(chunkPos.z - lastCachedZ) >= lastCachedH || chunkWidth != lastCachedW || chunkHeight != lastCachedH){
            val chunkWidthRange = if(chunkWidth % 2 != 0){
                ((-chunkWidth/2)+chunkPos.x)..((chunkWidth/2)+chunkPos.x)
            } else {
                ((-chunkWidth/2)+chunkPos.x)..((chunkWidth/2)+chunkPos.x-1)
            }
            val chunkHeightRange = if(chunkHeight % 2 != 0){
                ((-chunkHeight/2)+chunkPos.z)..((chunkHeight/2)+chunkPos.z)
            } else {
                ((-chunkHeight/2)+chunkPos.z)..((chunkHeight/2)+chunkPos.z-1)
            }
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
            for (w in chunkWidthRange){
                if (xDeltaIndex < 0 || xDeltaIndex >= dataCache.size){
                    fullBuild(newCache, dimKey,w,player.blockPos.y,chunkHeightRange)
                } else {
                    copyOver(newCache, index)
                }
                xDeltaIndex++
            }
        } else if (zDelta > 0) {
            val partialChunkHeightRange = if(chunkHeight % 2 != 0){
                    ((chunkHeight/2)+chunkPos.z-zDelta+1)..((chunkHeight/2)+chunkPos.z)
                } else {
                    ((chunkHeight/2)+chunkPos.z-zDelta)..((chunkHeight/2)+chunkPos.z-1)
                }
            for (w in chunkWidthRange){
                if (xDeltaIndex < 0 || xDeltaIndex >= dataCache.size){
                    fullBuild(newCache, dimKey,w,player.blockPos.y,chunkHeightRange)
                } else {
                    partialBuildEnd(newCache,dimKey,w,player.blockPos.y,partialChunkHeightRange,cacheData.get(xDeltaIndex).subList(zDelta,cacheData.get(xDeltaIndex).size))
                }
                xDeltaIndex++
            }
        } else {
            val partialChunkHeightRange = ((-chunkHeight/2)+chunkPos.z)..((-chunkHeight/2)+chunkPos.z-zDelta-1)
            for (w in chunkWidthRange){
                if (xDeltaIndex < 0 || xDeltaIndex >= dataCache.size){
                    fullBuild(newCache, dimKey,w,player.blockPos.y,chunkHeightRange)
                } else {
                    partialBuildFront(newCache,dimKey,w,player.blockPos.y,partialChunkHeightRange,cacheData.get(xDeltaIndex).subList(0,cacheData.get(xDeltaIndex).size - zDelta))
                }
                xDeltaIndex++
            }
        }
        
    }

    private fun gatherLayers(dimKey: Identifier, x: Int, y: Int, z: Int): List<TileStack>{
        val layerList: MutableList<Tile> = mutableListOf()
        for (layer in map.getLayers()){
            val tile = layer.getTile(dimKey,x,y,z)
            if (tile.hasContent){
                layerList.add(tile)
            }
            if (layer.opaque()){
                break
            }
        }
        layerList.reverse()
        return layerList
    }
    
    privat fullNewBuild(newCache: MutableList<List<List<TileStack>>>, dimKey: Identifier, wRange: IntRange, hRange: IntRange, y: Int){
        for (w in wRange){
            fullBuild(newCache, dimKey, w, y, hRange)
        }
    }
    
    private fun fullBuild(newCache: MutableList<List<List<TileStack>>>,dimKey: Identifier, w: Int, y: Int, hRange: IntRange){
        val hList = mutableListOf()
        for (h in hRange){
            val list = gatherLayers(dimKey,w,y,h)
            hList.add(list)
        }
        newCache.add(hList)
    }
    
    private fun copyOver(newCache: MutableList<List<List<TileStack>>>, index: Int){
        newCache.add(cacheData.get(index))
    }
    
    private fun partialBuildEnd(newCache: MutableList<List<List<TileStack>>>,dimKey: Identifier, w: Int, y: Int, hRange: IntRange, existingData: List<TileStack>){
        val hList = mutableListOf()
        hList.addAll(existingData)
        for (h in hRange){
            val list = gatherLayers(dimKey,w,y,h)
            hList.add(list)
        }
        newCache.add(hList)
    }
    private fun partialBuildFront(newCache: MutableList<List<List<TileStack>>>,dimKey: Identifier, w: Int, y: Int, hRange: IntRange, existingData: List<TileStack>){
        val hList = mutableListOf()
        for (h in hRange){
            val list = gatherLayers(dimKey,w,y,h)
            hList.add(list)
        }
        hList.addAll(existingData)
        newCache.add(hList)
    }
  
}
