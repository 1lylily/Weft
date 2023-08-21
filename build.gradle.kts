plugins {
    kotlin("jvm") version "1.8.21"
    application
}

group = "me.lily"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
    implementation(kotlin("reflect"))
    api(libs.bundles.asm)
}

kotlin {
    jvmToolchain(8)
    explicitApiWarning()
}