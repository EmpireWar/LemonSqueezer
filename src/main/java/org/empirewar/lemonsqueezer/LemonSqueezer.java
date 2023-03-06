package org.empirewar.lemonsqueezer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.empirewar.lemonadestand.event.KoFiTransactionEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class LemonSqueezer extends JavaPlugin implements Listener {

    private final Map<String, Function<KoFiTransactionEvent, String>> VARIABLE_MAPPINGS = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        VARIABLE_MAPPINGS.put("player", (event) -> event.getPlayer().getName());
        VARIABLE_MAPPINGS.put("amount", event -> String.valueOf(event.getShopOrder().getAmount()));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onShop(KoFiTransactionEvent event) {
        for (String command : getConfig().getStringList("purchase.commands")) {
            for (String key : VARIABLE_MAPPINGS.keySet()) {
                String varialised = "{" + key + "}";
                final Function<KoFiTransactionEvent, String> function = VARIABLE_MAPPINGS.get(key);
                command = command.replace(varialised, function.apply(event));
            }

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
}
