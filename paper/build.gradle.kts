dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.5-R0.1-SNAPSHOT")
    compileOnly("org.empirewar.lemonadestand:LemonadeStand:1.0.0-SNAPSHOT")
    implementation(project(":common"))
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
    }
}
