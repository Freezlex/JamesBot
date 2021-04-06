import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.5.0-M2"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.5.0-M2" apply true
}

group = "com.freezlex"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name="m2-dv8tion"
        url=uri("https://m2.dv8tion.net/releases")
    }
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("net.dv8tion:JDA:4.2.1_253")
    /*
     * This part will become later during the development
     * implementation("org.hibernate.orm", "hibernate-core","6.0.0.Alpha7")
     * implementation("org.hibernate", "hibernate-testing", "6.0.0.Alpha7")
     * implementation("mysql", "mysql-connector-java", "8.0.23")
     */
    implementation("org.reflections", "reflections", "0.9.10")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
