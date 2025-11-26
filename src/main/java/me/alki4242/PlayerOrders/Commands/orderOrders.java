package me.alki4242.PlayerOrders.Commands;

import me.alki4242.PlayerOrders.Gui.OrderMenu;
import me.alki4242.PlayerOrders.Managers.OrderManager;
import me.alki4242.PlayerOrders.Managers.VaultManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class orderOrders  {

    private final OrderMenu menu;

    public orderOrders(Plugin plugin, OrderManager orderManager, Economy economy, VaultManager vaultManager) {
        this.menu = new OrderMenu(plugin,orderManager,economy,vaultManager);
    }

    public void tryOpen(CommandSender sender) {
        Player p = (Player) sender;
        menu.open(p);
    }
}
