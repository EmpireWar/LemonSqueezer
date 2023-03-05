package org.empirewar.lemonsqueezer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.empirewar.lemonadestand.event.KoFiTransactionEvent;
import org.empirewar.lemonadestand.kofi.ShopOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class LemonSqueezer extends JavaPlugin {

    private final Map<String, Function<KoFiTransactionEvent, String>> VARIABLE_MAPPINGS = new HashMap<>();
    @Override
    public void onEnable() {
        saveDefaultConfig();
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
