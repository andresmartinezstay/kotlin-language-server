val exposedVersion = "0.37.3"
val lsp4jVersion = "0.21.2"

plugins {
    kotlin("jvm") version "2.0.20"
    id("application")
    id("com.github.jk1.tcdeps")
    id("com.jaredsburrows.license")
}

group = "org.javacs.kt"
version = "1.0.0"

repositories {
    mavenCentral()
    maven(url = "https://repo.gradle.org/gradle/libs-releases")
    maven { url = uri("$projectDir/lib") }
    maven(uri("$projectDir/lib"))
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j:$lsp4jVersion")
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j.jsonrpc:$lsp4jVersion")
    implementation(kotlin("compiler"))
    implementation(kotlin("scripting-compiler"))
    implementation(kotlin("scripting-jvm-host-unshaded"))
    implementation(kotlin("sam-with-receiver-compiler-plugin"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains:fernflower:1.0")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("com.h2database:h2:1.4.200")
    implementation("com.github.fwcd.ktfmt:ktfmt:b5d31d1")
    implementation("com.beust:jcommander:1.78")
    implementation("org.xerial:sqlite-jdbc:3.41.2.1")

    testImplementation(kotlin("test"))
    testImplementation("junit:junit:4.11")
    testImplementation("org.hamcrest:hamcrest-all:1.3")
    testImplementation("org.openjdk.jmh:jmh-core:1.20")

    // See https://github.com/JetBrains/kotlin/blob/65b0a5f90328f4b9addd3a10c6f24f3037482276/libraries/examples/scripting/jvm-embeddable-host/build.gradle.kts#L8
    compileOnly(kotlin("scripting-jvm-host"))
    testCompileOnly(kotlin("scripting-jvm-host"))

    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.20")
}

kotlin {
    jvmToolchain(21)
}
