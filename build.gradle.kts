import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        // Enables IDE autocompletion for declarations in gradle/libs.versions.toml
        classpath(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit4)
    testImplementation(libs.bundles.kotest)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}