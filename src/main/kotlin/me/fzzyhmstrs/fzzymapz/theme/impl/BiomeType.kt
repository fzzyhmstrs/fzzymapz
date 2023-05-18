package me.fzzyhmstrs.fzzymapz.theme.impl

import com.google.gson.JsonObject
import me.fzzyhmstrs.fzzymapz.registry.RegisterTheme
import me.fzzyhmstrs.fzzymapz.theme.Theme
import me.fzzyhmstrs.fzzymapz.theme.ThemeType
import net.minecraft.util.Identifier

object BiomeType: ThemeType(){


    //the theme loader will select a theme from the registry based on the
    override fun loadTheme(json: JsonObject): Theme {
        val biomeIds = json.get("biome_ids")
        if (biomeIds == null || !biomeIds.isJsonObject){
            println("Biome theme json not provded with biome ids")
            println(json)
            return RegisterTheme.EMPTY_TYPE.EMPTY_THEME
        }
        val map: MutableMap<Identifier, Identifier> = mutableMapOf()
        for (entry in (biomeIds as JsonObject).entrySet()){
            val valueEl = entry.value
            if (!valueEl.isJsonPrimitive) {
                println("Biome texture id $valueEl isn't properly formatted as a string, skipping.")
                continue
            }
            val value = Identifier.tryParse(valueEl.asString)
            if (value == null){
                println("Biome texture id $valueEl isn't a valid identifier, skipping.")
                continue
            }

            val key = Identifier.tryParse(entry.key)
            if (key == null){
                println("Biome id $key isn't a valid identifier, skipping.")
                continue
            }

        }
        return BiomeTheme(map)
    }
}