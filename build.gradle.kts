val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val reflectionsVersion: String by project
val jdaVersion: String by project
val jdaKtxVersion: String by project
val mysqlVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.5.0"
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
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.reflections:reflections:$reflectionsVersion")
    implementation("mysql:mysql-connector-java:$mysqlVersion")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("com.github.minndevelopment:jda-ktx:$jdaKtxVersion")
}
