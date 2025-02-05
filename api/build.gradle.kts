plugins {
    id("buildsrc.convention.kotlin-jvm")
    id("com.google.devtools.ksp") version "2.0.21-1.0.28"
    id("org.jetbrains.kotlinx.rpc.plugin") version "0.4.0"
    kotlin("plugin.serialization") version "2.0.21"
    `java-library`
}

group = "demo.tictactoe.api"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    google()
}

dependencies {
    // Apply the kotlinx bundle of dependencies from the version catalog (`gradle/libs.versions.toml`).
    implementation(libs.bundles.kotlinxEcosystem)

    implementation("org.jetbrains.kotlinx:kotlinx-rpc-krpc-server:0.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-rpc-krpc-serialization-json:0.4.0")

    testImplementation(kotlin("test"))
}

tasks.named<Jar>("jar") {
    enabled = true
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}