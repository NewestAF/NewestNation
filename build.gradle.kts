@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id ("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
    id("com.vjh0107.bukkit-executor") version "1.0.1"
    id("com.vjh0107.bukkit-resource-generator") version "1.0.1"
    id("com.vjh0107.ksp-extension") version "1.0.2"
}

group = "com.newestaf"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.purpurmc.org/snapshots/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://mvnrepository.com/artifact/com.google.code.gson/gson")
}

dependencies {
    @Suppress("VulnerableLibrariesLocal")
    compileOnly("org.purpurmc.purpur:purpur-api:1.19.3-R0.1-SNAPSHOT")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    compileOnly("com.google.code.gson:gson:2.10.1")
    implementation("io.insert-koin:koin-core:3.3.2")
    implementation("io.insert-koin:koin-annotations:1.1.0")
    compileOnly("org.xerial:sqlite-jdbc:3.40.0.0")
    ksp("io.insert-koin:koin-ksp-compiler:1.1.0")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    testImplementation(kotlin("test"))
    testImplementation("org.xerial:sqlite-jdbc:3.40.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
//    testImplementation("io.insert-koin:koin-test:3.3.2")
//    testImplementation("io.insert-koin:koin-annotations:1.1.0")
}

barcodeTasks {
    bukkitExecutor {
        // 활성화여부를 결정할 수 있습니다.
        enabled.set(true)
        // 의존할 AbstractArchiveTask
        archiveTask.set(tasks.shadowJar)
        // 버킷이 있는 DirectoryProperty, 이 위에서 jvm 이 실행됩니다.
        bukkitDir.set(file("testBukkit/"))
        // 버킷의 jar 파일
        bukkitFileName.set("paper.jar")
    }
    bukkitResource {
        main = "com.newestaf.newestnation.NewestNationPlugin"
        name = "NewestNation"
        apiVersion = "1.19"
        author = "newestaf"
    }
}

tasks {
    shadowJar {
        destinationDirectory.set(file("testBukkit/plugins/"))
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
