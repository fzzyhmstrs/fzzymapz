package me.fzzyhmstrs.fzzymapz.registry

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.fzzyhmstrs.fzzymapz.FM
import me.fzzyhmstrs.fzzymapz.theme.Theme
import me.fzzyhmstrs.fzzymapz.theme.ThemeType
import me.fzzyhmstrs.fzzymapz.theme.impl.BiomeType
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.registry.Registry
import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier

object RegisterTheme: SimpleSynchronousResourceReloadListener{

    val TYPES = FabricRegistryBuilder.createDefaulted(ThemeType::class.java, Identifier(FM.MOD_ID,"theme_type"),
        Identifier(FM.MOD_ID,"empty_type")).buildAndRegister()
    val EMPTY_TYPE = Registry.register(TYPES, Identifier(FM.MOD_ID,"empty_type"),EmptyType)

    //////////////////////////

    val BIOME = Registry.register(TYPES, Identifier(FM.MOD_ID,"biome_type"), BiomeType)
    //val STRUCTURE = Registry.register(TYPES, Identifier(FM.MOD_ID,"structure_type"),StructureType)
    //val ICON = Registry.register(TYPES, Identifier(FM.MOD_ID,"icon_type"),IconType)

    //////////////////////////

    override fun reload(manager: ResourceManager) {
        loadThemes(manager)
    }

    override fun getFabricId(): Identifier {
        return Identifier(FM.MOD_ID,"themes_loader")
    }

    private fun loadThemes(manager: ResourceManager){
        manager.findResources("fzzymapz/themes") { path -> path.path.endsWith(".json") }
            .forEach { (t, u) ->
                loadTheme(t,u)
            }
    }

    fun loadTheme(resourceId: Identifier, resource: Resource){
        val reader = resource.reader
        try{
            val json = JsonParser.parseReader(reader).asJsonObject
            val typeEl = json.get("type")
            if (typeEl == null || !typeEl.isJsonPrimitive){
                println("Theme $resourceId doesn't have a valid theme type ID: $typeEl")
                return
            }
            val typeId = Identifier.tryParse(typeEl.asString)
            if (typeId == null){
                println("Theme $resourceId doesn't have a valid theme type ID: $typeEl")
                return
            }
            if (!TYPES.containsId(typeId)){
                    println("Theme $resourceId references an unregistered type: $typeEl")
                    return
                }
            val type = TYPES.get(typeId)

            val idEl =  json.get("id")
            if (idEl == null || !idEl.isJsonPrimitive){
                println("Theme $resourceId doesn't have a valid theme ID: $idEl")
                return
            }
            val id = Identifier.tryParse(idEl.asString)
            if (id == null){
                println("Theme $resourceId doesn't have a valid theme ID: $idEl")
                return
            }

            val scaleEl = json.get("scale")
            val scale = if (scaleEl == null || !scaleEl.isJsonPrimitive){
                1.0f
            } else {
                scaleEl.asFloat
            }

            val theme = type.loadTheme(json)
            theme.setId(id)
            theme.setScale(scale)
            type.addTheme(id, theme)
        } catch (e: Exception){
            println("Error while parsing theme $resourceId")
            e.printStackTrace()
        }
    }

    fun registerAll(){
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(this)
    }

    object EmptyType: ThemeType() {
        val EMPTY_THEME = object: Theme(EMPTY_TYPE) {
            override fun provide(inputId: Identifier): Identifier{
                return inputId
            }
        }
        override fun loadTheme(json: JsonObject): Theme{
            return EMPTY_THEME
        }

    }

}