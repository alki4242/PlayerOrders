package me.alki4242.PlayerOrders.Utils;

import me.alki4242.PlayerOrders.Commands.CommandExecuter;
import me.alki4242.PlayerOrders.Gui.VaultMenu;
import me.alki4242.PlayerOrders.Managers.OrderManager;
import me.alki4242.PlayerOrders.Managers.VaultManager;
import me.alki4242.PlayerOrders.PlayerOrders;
import net.milkbowl.vault.economy.Economy;

public class CommandRegister {
    private final Economy eco;

    public CommandRegister(Economy provider) {
        this.eco = provider;
    }

    /**
     * @param PlayerOrders get plugin
     * @param orderManager get OrderManager
     * @param vaultMenu get VaultMenu
     * @param vaultManager get VaultManager
     */
    public void registerAll(PlayerOrders PlayerOrders, OrderManager orderManager, VaultMenu vaultMenu,VaultManager vaultManager) {
        PlayerOrders.getCommand("order").setExecutor(new CommandExecuter(PlayerOrders,orderManager,eco,vaultMenu,vaultManager));
        PlayerOrders.getCommand("order").setTabCompleter(new tabCompleter(orderManager));
    }

}
