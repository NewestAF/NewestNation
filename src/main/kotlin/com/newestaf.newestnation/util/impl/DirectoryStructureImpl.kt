package com.newestaf.newestnation.util.impl

import com.newestaf.newestnation.util.DirectoryStructure
import org.koin.core.annotation.Single
import java.io.File
import java.util.*

@Single(binds = [DirectoryStructure::class])
class DirectoryStructureImpl : DirectoryStructure {

    private val pluginName = "NewestNation"
    private val pluginFolder = File("plugins/$pluginName")
    private val dataFolder = File(pluginFolder, "data")

    override fun getPluginDirectory(): File {
        return pluginFolder
    }

    override fun getDataBaseDirectory(): File {
        return dataFolder
    }

    override fun getResourceFileToSave(dir: File, fileName: String): File {
        return File(dir, "custom" + File.separator + fileName.lowercase(Locale.getDefault()) + ".yml")
    }
}
