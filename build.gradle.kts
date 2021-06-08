val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposedVersion: String by project

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
    maven {
        name="m2-dv8tion"
        url=uri("https://m2.dv8tion.net/releases")
    }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("mysql", "mysql-connector-java", "8.0.25")
    implementation("org.reflections:reflections:0.9.12")
    implementation("net.dv8tion:JDA:4.2.1_253")


}
