import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp") version "2.0.21-1.0.28"
    id("org.jetbrains.kotlinx.rpc.plugin") version "0.4.0"
    alias(libs.plugins.kotlinPluginSerialization)
}

group = "demo.tictactoe.client"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Apply the kotlinx bundle of dependencies from the version catalog (`gradle/libs.versions.toml`).
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(compose.desktop.currentOs)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)

    implementation(project(":api"))

    implementation("io.ktor:ktor-client-cio-jvm:3.0.3")
    implementation("org.jetbrains.kotlinx:kotlinx-rpc-krpc-client:0.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-rpc-krpc-ktor-client:0.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-rpc-krpc-serialization-json:0.4.0")

    implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.10.1")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")

}

tasks.test {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "demo.tictactoe.client.ClientAppKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "demo.tictactoe.client"
            packageVersion = "1.0.0"
        }
    }
}
