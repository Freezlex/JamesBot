import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jdaVersion: String by project
val jdaKtxVersion: String by project
val slf4jVersion: String by project
val gsonVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.6.0"
}

group = "com.freezlex"
version = "0.0.1"
application {
    mainClass.set("com.freezlex.kohanato.ApplicationKt")
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
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("ch.qos.logback", "logback-classic", "1.2.6")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf(
        "-Xjvm-default=all",
        "-Xlambdas=indy"
    )
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
    from(sourceSets.main.get().output)
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
