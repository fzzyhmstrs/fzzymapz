package me.fzzyhmstrs.imbued_ascendancy.map

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.fzzyhmstrs.imbued_ascendancy.IA
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.registry.Registries
import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.math.ChunkPos

object MapData: SimpleSynchronousResourceReloadListener{

    override fun reload(manager: ResourceManager) {
        loadTextures(manager)
    }

    override fun getFabricId(): Identifier {
        return Identifier(IA.MOD_ID,"map_tiles_loader")
    }

    private fun loadTextures(manager: ResourceManager){
        BIOMES.clear()
        STRUCTURES.clear()
        manager.findResources("biome_textures") { path -> path.path.endsWith(".json") }
            .forEach { (t, u) ->
                loadTexture(t,u, BIOMES)
        }
        manager.findResources("structure_textures") { path -> path.path.endsWith(".json") }
            .forEach { (t, u) ->
                loadTexture(t,u, STRUCTURES)
        }
    }

    private fun loadTexture(resourceId: Identifier, resource: Resource, map: MutableMap<Identifier,Pair<Identifier,Identifier>>){
        val reader = resource.reader
        try{
            val json = JsonParser.parseReader(reader).asJsonObject
            for (el in json.entrySet()) {
                if (!el.value.isJsonObject) {
                    println("element $el isn't a valid JsonObject")
                    continue
                }
                val obj = el.value.asJsonObject
                val idKey = el.key
                val id = Identifier.tryParse(idKey)
                if (id == null) {
                    println("$idKey could not be parsed as a valid identifier")
                    continue
                } else if (Registries.BIOME_SOURCE.get(id) == null) {
                    println("ID $id isn't a valid biome identifier")
                    continue
                }
                val textureEl = obj.get("texture")
                if (textureEl == null || !textureEl.isJsonPrimitive) {
                    println("Texture value $textureEl isn't valid")
                    continue
                }
                val texture = Identifier.tryParse(textureEl.asString)
                if (texture == null) {
                    println("Object $obj missing primary texture ID")
                    continue
                }
                val hcEl = obj.get("high_contrast")
                if (hcEl == null || !hcEl.isJsonPrimitive) {
                    println("High constrast value $hcEl isn't valid")
                    continue
                }
                val highContrast = Identifier.tryParse(hcEl.asString) ?: texture
                map[id] = Pair(texture, highContrast)

            }
        } catch(e: Exception){
            println("Resource $resourceId isn't valid JSON")
            e.printStackTrace()
        }
    }

    private fun getBiome(x: Int, z: Int, world: World): Biome{
        val y = world.getTopY(Heightmap.Type.MOTION_BLOCKING,x,z)
        //GET A BIOME HERE. MAYBE THE WAY THE F3 SCREEN GETS IT?
    }

    private fun getIdForBiome(biome: Biome, world: World): Identifier{
        return world.getRegistryManager().get(RegistryKeys.BIOME).getId(biome)
    }

    fun loadMapTiles(){
        //READ IN FROM FILE
        //PLACEHOLDER
        val json = JsonObject()
        for (entryZ in json.entrySet()){
            val jsonZ = if(entryZ.value.isJsonObject) entryZ.value.asJsonObject() else continue
            val mapZ = entryZ.key.toIntOrNull()?:continue
            for (entryX in jsonZ.entrySet()){
                val jsonX = if(entryX.value.isJsonObject) entryX.value.asJsonObject() else continue
                val mapX = entryX.key.toIntOrNull()?:continue
                val tile = BaseTile.fromJson(jsonX)
                TILE_DATA.getOrDefault(mapZ,mutableMapOf()).put(mapX, tile)
            }
        }

    }

    fun saveMapTiles(){
        val json = JsonObject()
        for (entryZ in TILE_DATA){
            val jsonZ = JsonObject()
            for (entryX in entryZ.value){
                val jsonX = entryX.value.toJson()
                jsonZ.add(entryX.key.toString(),jsonX)
            }
            json.add(entryZ.key.toString(), jsonZ)
        }
        //WRITE OUT TO FILE
    }

    //https://github.com/MattCzyr/NaturesCompass/blob/fabric-1.19.3/src/main/java/com/chaosthedude/naturescompass/utils/BiomeUtils.java

    fun addTile(startPos: ChunkPos, world: World){
        val chunkX = //???
        val chunkZ = //???
        val existingTile = TILE_DATA[chunkZ]?.[chunkX]
        if (existingTile?.isStructure == true) return
        val x = startPos.centerX
        val z = startPos.centerZ
        val biome = getBiome(x,z,world)
        val biomeId = getIdForBiome(biome, world)
        val textures = BIOMES[biomeId]?: return BaseTile.EMPTY_TILE
        var sum = 0
        val updateMap: MutableMap<Int,Int> = mutableMapOf()
        //north
        val tileNorth = TILE_DATA[chunkZ - 1]?.[chunkX]
        if (tileNorth != null){
            updateMap[chunkZ - 1] = chunkX
            if (tileNorth.biome == biomeId) sum += 1
        }
        //east
        val tileEast = TILE_DATA[chunkZ]?.[chunkX + 1]
        if (tileEast != null){
            updateMap[chunkZ] = chunkX + 1
            if (tileEast.biome == biomeId) sum += 2
        }
        //south
        val tileSouth = TILE_DATA[chunkZ + 1]?.[chunkX]
        if (tileSouth != null){
            updateMap[chunkZ + 1] = chunkX
            if (tileSouth.biome == biomeId) sum += 4
        }
        //west
        val tileWest = TILE_DATA[chunkZ]?.[chunkX - 1]
        if (tileWest != null){
            updateMap[chunkZ] = chunkX - 1
            if (tileWest.biome == biomeId) sum += 9
        }
        val uv = getTileUv(sum)
        val tile = Tile(textures.first, textures.second, uv.first, uv.second,biomeId)
        TILE_DATA.getOrDefault(chunkZ,mutableMapOf()).put(chunkX, tile)
        for (entry in updateMap){
            updateTile(entry.key, entry.value)
        }
        
    }
    
    private fun updateTile(chunkZ: Int, chunkX: Int){
        val existingTile = TILE_DATA[chunkZ]?.[chunkX]
        if (existingTile?.isStructure == true) return
        val textures = BIOMES[biomeId]?: return BaseTile.EMPTY_TILE
        var sum = 0
        //north
        val tileNorth = TILE_DATA[chunkZ - 1]?.[chunkX]
        if (tileNorth != null){
            if (tileNorth.biome == biomeId) sum += 1
        }
        //east
        val tileEast = TILE_DATA[chunkZ]?.[chunkX + 1]
        if (tileEast != null){
            if (tileEast.biome == biomeId) sum += 2
        }
        //south
        val tileSouth = TILE_DATA[chunkZ + 1]?.[chunkX]
        if (tileSouth != null){
            if (tileSouth.biome == biomeId) sum += 4
        }
        //west
        val tileWest = TILE_DATA[chunkZ]?.[chunkX - 1]
        if (tileWest != null){
            if (tileWest.biome == biomeId) sum += 9
        }
        val uv = getTileUV(sum)
        val tile = Tile(textures.first, textures.second, uv.first, uv.second,biomeId)
    }

    private fun getTileUV(sum: Int): Pair<Int, Int>{
        return when (sum){
            10 -> Pair(0,0)
            1 -> Pair(16,0)
            3 -> Pair(32,0)
            7 -> Pair(48,0)
            9 -> Pair(0,16)
            0 -> Pair(16,16)
            2 -> Pair(32,16)
            12 -> Pair(48,16)
            13 -> Pair(0,32)
            4 -> Pair(16,32)
            6 -> Pair(32,32)
            14 -> Pair(48,32)
            15 -> Pair(48,48)
            16 -> Pair(random.nextInt(3) * 16, 48)
        }
    }

    fun getTile(z: Int, x: Int): BaseTile{
        return TILE_DATA.getOrDefault(z,mutableMapOf()).getOrDefault(x,EMPTY_TILE)
    }

    private val random = Random(System.currentTimeMillis())
    internal val BIOMES: MutableMap<Identifier,Pair<Identifier,Identifier>> = mutableMapOf()
    internal val STRUCTURES: MutableMap<Identifier,Pair<Identifier,Identifier>> = mutableMapOf()
    //outer map Z, higher numbers more south, inner map X, higher numbers more east
    private val TILE_DATA: MutableMap<Int, MutableMap<Int,BaseTile>> = mutableMapOf()

}
