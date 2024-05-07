package org.empirewar.lemonsqueeezer.sponge;

import com.google.inject.Inject;
import org.empirewar.lemonsqueezer.LemonSqueezer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.nio.file.Path;

@Plugin("lemonsqueezer")
public class LemonSqueezerSponge implements LemonSqueezer {

	private final Logger logger = LoggerFactory.getLogger("orbis");
	private final PluginContainer pluginContainer;

	private final @ConfigDir(sharedRoot = false) Path dataFolder;

	public PluginContainer pluginContainer() {
		return pluginContainer;
	}

	public Path getDataFolder() {
		return dataFolder;
	}

	@Inject
	public LemonSqueezerSponge(PluginContainer pluginContainer, @ConfigDir(sharedRoot = false) Path dataFolder) {
		this.pluginContainer = pluginContainer;
		this.dataFolder = dataFolder;
	}

}
