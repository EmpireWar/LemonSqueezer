package org.empirewar.lemonsqueezer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.empirewar.lemonadestand.event.KoFiTransactionEvent;
import org.empirewar.lemonadestand.kofi.ShopItem;
import org.empirewar.lemonadestand.kofi.ShopOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class LemonSqueezer extends JavaPlugin implements Listener {

    private final Map<String, Function<KoFiTransactionEvent, String>> VARIABLE_MAPPINGS = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        VARIABLE_MAPPINGS.put("player", event -> event.getPlayer().getName());
        VARIABLE_MAPPINGS.put("amount", event -> String.valueOf(event.getShopOrder().getAmount()));
        VARIABLE_MAPPINGS.put("tier", event -> event.getShopOrder().getTierName());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onShop(KoFiTransactionEvent event) {
        final ShopOrder shopOrder = event.getShopOrder();

        if (shopOrder.getShopItems() != null) {
            for (ShopItem shopItem : shopOrder.getShopItems()) {
                final String code = shopItem.getDirectLinkCode();
                List<String> commands = getConfig().getStringList("purchase.shop." + code);
                for (int i = 0; i < shopItem.getQuantity(); i++) {
                    processCommands(event, commands);
                }
            }
            return;
        }

        // If they didn't buy any shop items, check if they bought a membership tier.
        final String tierName = shopOrder.getTierName();
        if (tierName != null) {
            // Is this the first subscription? If so, we want the initial command set, otherwise the subsequent.
            String subscriptionType = shopOrder.isFirstSubscriptionPayment() ? "initial" : "subsequent";
            List<String> commands = getConfig().getStringList("purchase.membership." + tierName + "." + subscriptionType);
            processCommands(event, commands);
            return;
        }

        // Otherwise, this is a loving donation.
        processCommands(event, getConfig().getStringList("purchase.donation"));
    }

    private void processCommands(KoFiTransactionEvent event, List<String> commandList) {
        for (String command : commandList) {
            for (String key : VARIABLE_MAPPINGS.keySet()) {
                String varialised = "{" + key + "}";
                final Function<KoFiTransactionEvent, String> function = VARIABLE_MAPPINGS.get(key);
                final String apply = function.apply(event);
                command = command.replace(varialised, apply == null ? "null" : apply);
            }

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
}
