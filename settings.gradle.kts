pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://cache-redirector.jetbrains.com/kotlin.bintray.com/kotlin-plugin")
    }

    plugins {
        id("application") apply false
        id("maven-publish") apply false

        kotlin("jvm") version "1.8.10" apply false
        id("com.github.jk1.tcdeps") version "1.2" apply false
        id("com.jaredsburrows.license") version "0.8.42" apply false
    }
}

rootProject.name = "kotlin-language-server"

include(
    "server"
)
