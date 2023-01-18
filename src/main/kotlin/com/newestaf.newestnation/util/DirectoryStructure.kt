package com.newestaf.newestnation.util

import java.io.File

interface DirectoryStructure {

    fun getPluginDirectory(): File
    fun getDataBaseDirectory(): File
    fun getResourceFileToSave(dir: File, fileName: String): File


}
