import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    id("buildlogic.java-common-conventions")
    id("org.spongepowered.gradle.plugin") version("2.2.0")
}

dependencies {
    compileOnly("org.empirewar.lemonadestand:sponge-api:1.0.1-SNAPSHOT")
    implementation(project(":common")) {
        exclude("org.empirewar.lemonadestand")
    }
}

sponge {
    apiVersion("12.0.0-SNAPSHOT")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0.1-SNAPSHOT")
    }
    plugin("lemonsqueezer") {
        displayName("LemonSqueezer")
        entrypoint("org.empirewar.lemonsqueezer.sponge.LemonSqueezerSponge")
        description("Lemons for all!")
        license("MIT")
        version(project.version.toString())
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
        dependency("lemonadestand") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
            version("*")
        }
        contributors {
            contributor("SamB440") {}
            contributor("StealWonders") {}
        }
    }
}
