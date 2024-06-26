plugins {
    id("buildlogic.java-common-conventions")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("org.empirewar.lemonadestand:paper-api:1.0.1-SNAPSHOT")
    implementation(project(":common"))
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
    }
}
