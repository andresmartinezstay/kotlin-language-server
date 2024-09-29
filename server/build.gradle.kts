plugins {
    kotlin("jvm")
    id("maven-publish")
    id("application")
    id("com.github.jk1.tcdeps")
    id("com.jaredsburrows.license")
    id("kotlin-language-server.publishing-conventions")
    id("kotlin-language-server.distribution-conventions")
    id("kotlin-language-server.kotlin-conventions")
}

val debugPort = 8000
val debugArgs = "-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=n,quiet=y"

val serverMainClassName = "org.javacs.kt.MainKt"

val kotlinVersion = "2.0.20"
val exposedVersion = "0.37.3"
val lsp4jVersion = "0.21.2"

application {
    mainClass.set(serverMainClassName)
    description = "Code completions, diagnostics and more for Kotlin"
    applicationDefaultJvmArgs = listOf("-DkotlinLanguageServer.version=$version")
    applicationDistribution.into("bin") {
        fileMode = 755
    }
}

repositories {
    maven(url = "https://repo.gradle.org/gradle/libs-releases")
    maven { url = uri("$projectDir/lib") }
    maven(uri("$projectDir/lib"))
    maven("https://jitpack.io")
    mavenCentral()
}

dependencies {
    constraints {
        api("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
        api("org.hamcrest:hamcrest-all:1.3")
        api("junit:junit:4.11")
        api("org.eclipse.lsp4j:org.eclipse.lsp4j:$lsp4jVersion")
        api("org.eclipse.lsp4j:org.eclipse.lsp4j.jsonrpc:$lsp4jVersion")
        api("org.jetbrains.kotlin:kotlin-compiler:$kotlinVersion")
        api("org.jetbrains.kotlin:kotlin-scripting-compiler:$kotlinVersion")
        api("org.jetbrains.kotlin:kotlin-scripting-jvm-host-unshaded:$kotlinVersion")
        api("org.jetbrains.kotlin:kotlin-sam-with-receiver-compiler-plugin:$kotlinVersion")
        api("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        api("org.jetbrains.kotlin:kotlin-jvm:$kotlinVersion")
        api("org.jetbrains:fernflower:1.0")
        api("org.jetbrains.exposed:exposed-core:$exposedVersion")
        api("org.jetbrains.exposed:exposed-dao:$exposedVersion")
        api("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
        api("com.h2database:h2:1.4.200")
        api("com.github.fwcd.ktfmt:ktfmt:b5d31d1")
        api("com.beust:jcommander:1.78")
        api("org.hamcrest:hamcrest-all:1.3")
        api("junit:junit:4.11")
        api("org.openjdk.jmh:jmh-core:1.20")
        api("org.jetbrains.kotlin:kotlin-scripting-jvm-host:$kotlinVersion")
        api("org.jetbrains.kotlin:kotlin-scripting-jvm-host:$kotlinVersion")
        api("org.openjdk.jmh:jmh-generator-annprocess:1.20")
        api("org.xerial:sqlite-jdbc:3.41.2.1")
    }

    implementation(kotlin("stdlib"))
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j")
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j.jsonrpc")
    implementation(kotlin("compiler"))
    implementation(kotlin("scripting-compiler"))
    implementation(kotlin("scripting-jvm-host-unshaded"))
    implementation(kotlin("sam-with-receiver-compiler-plugin"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains:fernflower")
    implementation("org.jetbrains.exposed:exposed-core")
    implementation("org.jetbrains.exposed:exposed-dao")
    implementation("org.jetbrains.exposed:exposed-jdbc")
    implementation("com.h2database:h2")
    implementation("com.github.fwcd.ktfmt:ktfmt")
    implementation("com.beust:jcommander")
    implementation("org.xerial:sqlite-jdbc")

    testImplementation("org.hamcrest:hamcrest-all")
    testImplementation("junit:junit")
    testImplementation("org.openjdk.jmh:jmh-core")

    // See https://github.com/JetBrains/kotlin/blob/65b0a5f90328f4b9addd3a10c6f24f3037482276/libraries/examples/scripting/jvm-embeddable-host/build.gradle.kts#L8
    compileOnly(kotlin("scripting-jvm-host"))
    testCompileOnly(kotlin("scripting-jvm-host"))

    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess")
}

configurations.forEach { config ->
    config.resolutionStrategy {
        preferProjectModules()
    }
}

tasks.startScripts {
    applicationName = "kotlin-language-server"
}

tasks.register<Exec>("fixFilePermissions") {
    // When running on macOS or Linux the start script
    // needs executable permissions to run.

    onlyIf { !System.getProperty("os.name").lowercase().contains("windows") }
    commandLine("chmod", "+x", "${tasks.installDist.get().destinationDir}/bin/kotlin-language-server")
}

tasks.register<JavaExec>("debugRun") {
    mainClass.set(serverMainClassName)
    classpath(sourceSets.main.get().runtimeClasspath)
    standardInput = System.`in`

    jvmArgs(debugArgs)
    doLast {
        println("Using debug port $debugPort")
    }
}

tasks.register<CreateStartScripts>("debugStartScripts") {
    applicationName = "kotlin-language-server"
    mainClass.set(serverMainClassName)
    outputDir = tasks.installDist.get().destinationDir.toPath().resolve("bin").toFile()
    classpath = tasks.startScripts.get().classpath
    defaultJvmOpts = listOf(debugArgs)
}

tasks.register<Sync>("installDebugDist") {
    dependsOn("installDist")
    finalizedBy("debugStartScripts")
}

tasks.withType<Test>() {
    testLogging {
        events("failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

tasks.installDist {
    finalizedBy("fixFilePermissions")
}

tasks.build {
    finalizedBy("installDist")
}
