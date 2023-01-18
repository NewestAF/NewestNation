package com.newestaf.newestnation

import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.startKoin
import org.koin.dsl.module

class NewestNationPlugin : JavaPlugin() {

    private lateinit var instance: NewestNationPlugin

    val directoryModule = module {

    }
    override fun onEnable() {
        instance = this
        startKoin {
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}

