package org.empirewar.lemonsqueezer.sponge;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.empirewar.lemonadestand.LemonadeStand;
import org.empirewar.lemonadestand.kofi.ShopOrder;
import org.empirewar.lemonadestand.sponge.event.KoFiTransactionEvent;
import org.empirewar.lemonsqueezer.LemonSqueezer;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

@Plugin("lemonsqueezer")
public class LemonSqueezerSponge implements LemonSqueezer {

	private final PluginContainer pluginContainer;
	private final @ConfigDir(sharedRoot = false) Path dataFolder;
	private final Logger logger;

	public PluginContainer pluginContainer() {
		return pluginContainer;
	}

	public Path getDataFolder() {
		return dataFolder;
	}

	@Inject
	public LemonSqueezerSponge(PluginContainer pluginContainer, @ConfigDir(sharedRoot = false) Path dataFolder, Logger logger) {
		this.pluginContainer = pluginContainer;
		this.dataFolder = dataFolder;
		this.logger = logger;
	}

	@Listener
	public void onStarting(StartingEngineEvent<Server> event) {
		this.loadConfig();
	}

	@Listener
	public void onOrder(KoFiTransactionEvent event) {
		final User player = event.getPlayer();
		final ShopOrder shopOrder = event.getShopOrder();
		try {
			this.handleOrder(player.name(), shopOrder);
		} catch (SerializationException e) {
			lemonadeStand().transactionLogger().log(Level.SEVERE, "Error handling shop order", e);
			logger.error("Error handling shop order", e);
		}
	}

	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private ConfigurationNode rootNode;

	private void loadConfig() {
		try {
			if (!dataFolder.toFile().exists()) {
				dataFolder.toFile().mkdirs();
			}

			final Path configPath = dataFolder.resolve("config.yml");
			try {
				Files.copy(
						pluginContainer.openResource("/assets/lemonsqueezer/config.yml").orElseThrow(),
						configPath);
			} catch (FileAlreadyExistsException ignored) {
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			loader = YamlConfigurationLoader.builder().path(configPath).build();
			rootNode = loader.load();
		} catch (IOException e) {
			logger.error("Error loading config", e);
		}
	}

	@Override
	public ConfigurationNode config() {
		return rootNode;
	}

	@Override
	public LemonadeStand<?> lemonadeStand() {
		return (LemonadeStand<?>) Sponge.pluginManager().plugin("lemonadestand").orElseThrow().instance();
	}

	@Override
	public void dispatchCommand(String command) {
        try {
            Sponge.server().commandManager().process(Sponge.systemSubject(), command);
        } catch (CommandException e) {
            lemonadeStand().transactionLogger().log(Level.SEVERE, "Error processing command", e);
			logger.error("Error processing command", e);
        }
    }
}
