package me.fzzyhmstrs.fzzymapz.layer

import com.google.gson.JsonObject
import me.fzzyhmstrs.fzzymapz.registry.RegisterTile
import me.fzzyhmstrs.fzzymapz.theme.ThemeType
import me.fzzyhmstrs.fzzymapz.theme.ThemeTypeProviding
import me.fzzyhmstrs.fzzymapz.tile.Tile
import net.minecraft.util.Identifier
import net.minecraft.world.World
import net.minecraft.world.chunk.WorldChunk

abstract class MapLayer(override val type: ThemeType, override val id: Identifier): ThemeTypeProviding{
    
    // data is stored as (worldId -> Z -> X -> Tile)
    protected val data: MutableMap<Identifier,MutableMap<Int,MutableMap<Int,Tile>>> = mutableMapOf()

    fun processChunk(x: Int, z: Int, world: World, chunk: WorldChunk){
        if (!shouldProcessChunk(x,z,world,chunk)) return
        val tile = processChunkForTile(world, chunk)
        if (tile != Tile.EMPTY){
            data.getOrPut(world.dimensionKey.value) {mutableMapOf()} .getOrPut(z) {mutableMapOf()}[x] = tile
        }
    }
    
    // called first by the chunk processor to determine whether any actual processing should happen
    open fun shouldProcessChunk(x: Int, z: Int, world: World, chunk: WorldChunk): Boolean{
        return data[world.dimensionKey.value]?.get(z)?.containsKey(x) != true
    }
    
    // called by the chunk processor so the layer can grab and store any data it needs.
    abstract fun processChunkForTile(world: World, chunk: WorldChunk): Tile

    // methods for loading and saving the Layer data to the minecraft folder. Will be called by the layer registry on game close and periodically to save data, and on game join to load data
    open fun save(){
        //base layer is one JsonObject per world registry key, stored as identifier
        val json = JsonObject()
        for (entryWorld in data){
            //builds the type map, storing the Z/X located data into the map by the tile type it is at that location
            val typeMap: MutableMap<Identifier,MutableMap<Int,MutableMap<Int,JsonObject>>> = mutableMapOf()
            for (entryZ in entryWorld.value){
                for (entryX in entryZ.value){
                    val type = entryX.value.type.id
                    val jsonX = entryX.value.toJson()
                    val mapType = typeMap.getOrPut(type) {mutableMapOf()}
                    mapType.getOrPut(entryZ.key) {mutableMapOf()}[entryX.key] = jsonX
                }
            }
            //reads back the typeMap to serialize the data into a more packed order (type -> Z -> X -> Data) instead of (Z -> X -> Type -> Data) since type will be typically a constant
            val jsonWorld = JsonObject()
            for (entryType in typeMap){
                val jsonType = JsonObject()
                for (entryZ in entryType.value){
                    val jsonZ = JsonObject()
                    for (entryX in entryZ.value){
                        jsonZ.add(entryX.key.toString(),entryX.value)
                    }
                    jsonType.add(entryZ.key.toString(),jsonZ)
                }
                jsonWorld.add(entryType.key.toString(),jsonType)
            }
            json.add(entryWorld.key.toString(), jsonWorld)
        }
        
        //save data to file here
    }

    open fun load(){
        //Load in here, this is PLACEHOLDER
        val json = JsonObject()
        
        try {
            for (entryWorld in json.entrySet()){
                val worldId = Identifier.tryParse(entryWorld.key)?:continue
                val jsonWorld = entryWorld.value.asJsonObject
                val worldMap: MutableMap<Int,MutableMap<Int, Tile>> = mutableMapOf()
                for (entryType in jsonWorld.entrySet()){
                    val typeId = Identifier.tryParse(entryType.key)?:continue
                    val type = RegisterTile.TILES.get(typeId)?:continue
                    val jsonType = entryType.value.asJsonObject
                    for (entryZ in jsonType.entrySet()){
                        val z = entryZ.key.toIntOrNull()?:continue
                        val jsonZ = entryZ.value as JsonObject
                        for (entryX in jsonZ.entrySet()){
                            val x = entryX.key.toIntOrNull()?:continue
                            val jsonX = entryX.value.asJsonObject
                            val tile = type.loadTile(jsonX)
                            worldMap.getOrPut(z) {mutableMapOf()}[x] = tile
                        }
                    }
                }
                data[worldId] = worldMap
            }
        } catch(e: Exception){
            println("Exception while loading map layer data!")
            e.printStackTrace()
        }
    }

    // the main result of the layer. Provides the tile that the map renderer will use as needed to draw the screen
    open fun getTile(dimKey: Identifier, x: Int,y: Int, z: Int): Tile{
        return data[dimKey]?.get(x)?.get(z)?:fallback()
    }
    
    open fun fallback(): Tile{
        return Tile.EMPTY
    }

    // true makes the tile "opaque". The highest layer with true as a return will be the only layer drawn at the current pos. The default structure layer, for example, is opaque so the biome bheind the structure isn't drawn.
    open fun opaque(): Boolean{
        return false
    }

}
