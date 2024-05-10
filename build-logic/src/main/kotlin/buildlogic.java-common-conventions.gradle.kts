plugins {
    // Apply the java Plugin to add support for Java.
    java
    id("io.github.goooler.shadow")
    id("com.diffplug.spotless")
}

spotless {
    java {
        endWithNewline()
        removeUnusedImports()
        trimTrailingWhitespace()
        targetExclude("build/generated/**/*")
    }

    kotlinGradle {
        endWithNewline()
        indentWithSpaces(4)
        trimTrailingWhitespace()
    }
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/") // Paper
    maven("https://repo.convallyria.com/snapshots/")
}

project.version = "1.0.1-SNAPSHOT"

dependencies {
    constraints {
        // Define dependency versions as constraints
        compileOnly("org.joml:joml:1.10.5")
        testImplementation("org.joml:joml:1.10.5")
    }

    compileOnly("org.spongepowered:configurate-yaml:4.1.2")
    testImplementation("org.spongepowered:configurate-yaml:4.1.2")
    compileOnly("org.joml:joml")
    testImplementation("org.joml:joml")
    compileOnly("com.google.code.gson:gson:2.10.1")
    testImplementation("com.google.code.gson:gson:2.10.1")
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.10.1")
        }
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveBaseName.set("lemonsqueezer-${project.name}")
        archiveClassifier.set("")
    }
}