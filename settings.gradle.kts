rootProject.name = "kotlin-lsp"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://cache-redirector.jetbrains.com/kotlin.bintray.com/kotlin-plugin")
    }

    plugins {
        id("com.github.jk1.tcdeps") version "1.2" apply false
        id("com.jaredsburrows.license") version "0.8.42" apply false
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    }
}

rootProject.name = "kotlin-language-server"

