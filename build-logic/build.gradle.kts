/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    // Support convention plugins written in Kotlin. Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
    `kotlin-dsl`
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("io.github.goooler.shadow:io.github.goooler.shadow.gradle.plugin:8.1.7")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.25.0")
    implementation("net.kyori:blossom:2.1.0")
}