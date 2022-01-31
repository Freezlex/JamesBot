val jdaVersion: String by project
val jdaKtxVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.6.0"
}

group = "com.freezlex"
version = "0.0.1"
application {
    mainClass.set("com.freezlex.ApplicationKt")
}

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io/")
}

dependencies {
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("com.github.minndevelopment:jda-ktx:$jdaKtxVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    implementation("org.reflections:reflections:0.10.2")
}
