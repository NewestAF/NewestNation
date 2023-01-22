package com.newestaf.newestnation.event

import com.newestaf.newestnation.util.DatabaseManager
import com.newestaf.newestnation.util.DirectoryStructure
import com.newestaf.newestnation.util.SqlCondition
import com.newestaf.newestutil.config.ConfigurationManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File

class DefaultSpawnListener(private val directory: DirectoryStructure) : Listener, KoinComponent {

    private val configManager: ConfigurationManager by inject(named("mainConfig"))

    init {
        val column = listOf(
            "uuid VARCHAR(36) NOT NULL", "x DOUBLE NOT NULL", "y DOUBLE NOT NULL", "z DOUBLE NOT NULL"
        )

        getKoin().get<DatabaseManager> { parametersOf(File("${directory.getDataBaseDirectory().path}${File.separator}data.db")) }
            .use {
                it.createTable("start_location", column)
            }
    }

    @Suppress("UNCHECKED_CAST")
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        getKoin().get<DatabaseManager> { parametersOf(File("${directory.getDataBaseDirectory().path}${File.separator}data.db")) }
            .use {
                val isExists = it.isExists(
                    "start_location", SqlCondition(
                        "uuid", player.uniqueId.toString(), SqlCondition.ConditionType.EQUALS
                    )
                )
                if (isExists) {
                    return
                }
                val rawLocations = configManager.get("locations") as List<String>
                val rawLocation = rawLocations[(rawLocations.indices).random()]
                val split = rawLocation.split(", ")
                val location = player.world.getHighestBlockAt(split[0].toInt(), split[1].toInt()).location

                player.teleport(location)

                it.insert(
                    "start_location", listOf("uuid", "x", "y", "z"), listOf(
                        "\"${player.uniqueId}\"", location.x.toString(), location.y.toString(), location.z.toString()
                    )
                )
            }
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {

        if (event.isBedSpawn || event.isAnchorSpawn) {
            return
        }

        getKoin().get<DatabaseManager> { parametersOf(File("${directory.getDataBaseDirectory().path}${File.separator}data.db")) }
            .use { databaseManager ->
                databaseManager.select(
                    "start_location", listOf("x", "y", "z"), SqlCondition(
                        "uuid", event.player.uniqueId.toString(), SqlCondition.ConditionType.EQUALS
                    )
                ).let {
                    if (it.isEmpty()) {
                        return
                    }
                    val x = it[0]["x"] as Double
                    val z = it[0]["z"] as Double
                    event.respawnLocation = event.player.world.getHighestBlockAt(x.toInt(), z.toInt()).location
                }
            }
    }
}
