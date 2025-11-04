package me.alki4242.PlayerOrders.Managers;

import me.alki4242.PlayerOrders.Gui.MyOrdersMenu;
import me.alki4242.PlayerOrders.Gui.OrderMenu;
import me.alki4242.PlayerOrders.PlayerOrders;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ListenerManager {

    private final OrderManager orderManager;
    private final Economy eco;

    /**
     * Initate the class
     * @param orderManager get OrderManager
     * @param eco get Economy class
     */
    public ListenerManager(OrderManager orderManager, Economy eco) {
    this.orderManager = orderManager;
    this.eco = eco;
    }

    /**
     * Register the listeners of the events
     * @param PlayerOrders get plugin
     * @param vaultManager get VaultManager
     */
    public void RegisterAll(PlayerOrders PlayerOrders, VaultManager vaultManager) {
        Bukkit.getPluginManager().registerEvents(new OrderMenu(PlayerOrders,orderManager,eco,vaultManager),PlayerOrders);
        Bukkit.getPluginManager().registerEvents(new MyOrdersMenu(orderManager),PlayerOrders);
    }

    /**
     * Register the listener
     * @param listener get listener class
     * @param PlayerOrders get plugin
     */
    public void registerListener(Listener listener, Plugin PlayerOrders) {
        Bukkit.getPluginManager().registerEvents(listener,PlayerOrders);
    }
}
