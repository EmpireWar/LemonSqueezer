plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.0"
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.convallyria.com/empirewar") { // Treaty and others
        credentials { // This is read only so it doesn't matter
            username = "empirewar"
            password = "sGjUMQrfCUto7oxx2w+jB8ps0kUJPp89RA6ynpuDnFN7Mkr7Q/3u154GVFy6fxrP"
        }
        authentication {
            create<BasicAuthentication>("basic")
        }
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly("org.empirewar.lemonadestand:LemonadeStand:1.0.0-SNAPSHOT")
}

group = "org.empirewar.lemonsqueezer"
version = "1.0.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    processResources {
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
    }

    shadowJar {
        archiveClassifier.set("")
    }
}