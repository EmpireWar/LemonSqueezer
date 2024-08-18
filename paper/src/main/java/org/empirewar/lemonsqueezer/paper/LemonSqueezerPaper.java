package org.empirewar.lemonsqueezer.paper;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.empirewar.lemonadestand.LemonadeStand;
import org.empirewar.lemonadestand.kofi.ShopOrder;
import org.empirewar.lemonadestand.paper.event.KoFiTransactionEvent;
import org.empirewar.lemonsqueezer.LemonSqueezer;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

public final class LemonSqueezerPaper extends JavaPlugin implements Listener, LemonSqueezer {

    @Override
    public void onEnable() {
        this.loadConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onOrder(KoFiTransactionEvent event) {
        final OfflinePlayer player = event.getPlayer();
        final ShopOrder shopOrder = event.getShopOrder();
        try {
            this.handleOrder(player.getName(), shopOrder);
        } catch (SerializationException e) {
            lemonadeStand().transactionLogger().log(Level.SEVERE, "Error handling shop order", e);
            getSLF4JLogger().error("Error handling shop order", e);
        }
    }

    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private ConfigurationNode rootNode;

    private void loadConfig() {
        try {
            final Path configPath = getDataFolder().toPath().resolve("config.yml");
            saveResource("assets/lemonsqueezer/config.yml", false);

            loader = YamlConfigurationLoader.builder().path(configPath).build();
            rootNode = loader.load();
        } catch (IOException e) {
            getSLF4JLogger().error("Error loading config", e);
        }
    }

    @Override
    public ConfigurationNode config() {
        return rootNode;
    }

    @Override
    public LemonadeStand<?> lemonadeStand() {
        return (LemonadeStand<?>) Bukkit.getPluginManager().getPlugin("LemonadeStand");
    }

    @Override
    public void dispatchCommand(String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
