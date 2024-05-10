package org.empirewar.lemonsqueezer;

import org.empirewar.lemonadestand.LemonadeStand;
import org.empirewar.lemonadestand.kofi.ShopItem;
import org.empirewar.lemonadestand.kofi.ShopOrder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public interface LemonSqueezer {

    Map<String, BiFunction<String, ShopOrder, String>> VARIABLE_MAPPINGS = Map.of(
            "player", (username, order) -> username,
            "amount", (username, order) -> String.valueOf(order.getAmount()),
            "tier", (username, order) -> order.getTierName()
    );

    ConfigurationNode config();

    LemonadeStand<?> lemonadeStand();

    default void handleOrder(String username, ShopOrder shopOrder) throws SerializationException {
        final String type = shopOrder.getType();

        if (shopOrder.getShopItems() != null && type.equals("Shop Order")) {
            for (ShopItem shopItem : shopOrder.getShopItems()) {
                final String code = shopItem.getDirectLinkCode();
                lemonadeStand().transactionLogger()
                        .info("Processing shop order item with code '" + code + "'.");
                List<String> commands = config().node("purchase", "shop", code).getList(String.class, List.of());
                for (int i = 0; i < shopItem.getQuantity(); i++) {
                    processCommands(username, shopOrder, commands);
                }
            }
            return;
        }

        // If they didn't buy any shop items, check if they bought a membership tier.
        final String tierName = shopOrder.getTierName();
        if (tierName != null) {
            // Is this the first subscription? If so, we want the initial command set, otherwise the
            // subsequent.
            String subscriptionType = shopOrder.isFirstSubscriptionPayment() ? "initial" : "subsequent";
            lemonadeStand().transactionLogger()
                    .info("Processing subscription for tier '" + tierName + "' of type '"
                            + subscriptionType + "'.");
            List<String> commands = config().node("purchase", "membership", tierName, subscriptionType).getList(String.class, List.of());
            processCommands(username, shopOrder, commands);
            return;
        }

        // Otherwise, this is a loving donation.
        lemonadeStand().transactionLogger().info("Processing normal donation.");
        processCommands(username, shopOrder, config().node("purchase", "donation").getList(String.class, List.of()));
    }

    default void processCommands(String username, ShopOrder order, List<String> commandList) {
        for (String command : commandList) {
            for (String key : VARIABLE_MAPPINGS.keySet()) {
                String varialised = "{" + key + "}";
                final BiFunction<String, ShopOrder, String> function = VARIABLE_MAPPINGS.get(key);
                final String apply = function.apply(username, order);
                command = command.replace(varialised, apply == null ? "null" : apply);
            }

            lemonadeStand().transactionLogger().info("Processing command '" + command + "'.");
            dispatchCommand(command);
        }
    }

    void dispatchCommand(String command);

}
