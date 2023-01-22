package com.newestaf.newestnation

import com.newestaf.newestnation.event.DefaultSpawnListener
import com.newestaf.newestnation.util.UtilModule
import com.newestaf.newestutil.config.ConfigurationManager.ConfigurationManagerBuilder
import com.newestaf.newestutil.gui.InventoryGUI
import com.newestaf.newestutil.util.Debugger
import com.newestaf.newestutil.util.LogUtils
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ksp.generated.module
import java.io.File


class NewestNationPlugin : JavaPlugin() {

    private lateinit var instance: NewestNationPlugin

    override fun onEnable() {
        instance = this

        val listeners: MutableList<Listener> = mutableListOf()
        var defaultSpawnListener: DefaultSpawnListener? = null

        startKoin {
            modules(
                module {
                    single(named("plugin")) { instance }
                    single(named("mainConfig")) {
                        ConfigurationManagerBuilder(instance).prefix("main").validate(true).build()
                    }
                    single(named("dbFile")) {
                        File("${instance.dataFolder}${File.separator}data", "data.db")
                    }
                    factory { params -> InventoryGUI(instance, params.get(), params.get()) }
                }
            )
            modules(
                UtilModule().module
            )

            defaultSpawnListener = DefaultSpawnListener(koin.get())
        }

        LogUtils.init(this)
        Debugger.getInstance().target = server.consoleSender

        listeners.add(defaultSpawnListener!!)

        listeners.forEach {
            server.pluginManager.registerEvents(it, this)
        }


    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}

