@file:Suppress("PropertyName")

package me.fzzyhmstrs.fzzymapz

import me.fzzyhmstrs.fzzymapz.registry.RegisterLayer
import me.fzzyhmstrs.fzzymapz.registry.RegisterTheme
import me.fzzyhmstrs.fzzymapz.registry.RegisterTile
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import kotlin.random.Random


object FM: ModInitializer {
    const val MOD_ID = "imbued_ascendancy"

    override fun onInitialize() {
    }

    fun fmRandom(): Random{
        return Random(System.currentTimeMillis())
    }
}

@Environment(value = EnvType.CLIENT)
object FMClient: ClientModInitializer{

    override fun onInitializeClient() {
        RegisterTheme.registerAll()
        RegisterLayer.registerAll()
        RegisterTile.registerAll()
    }

    fun fmRandom(): Random{
        return Random(System.currentTimeMillis())
    }
}