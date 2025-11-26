package me.alki4242.PlayerOrders.Commands;

import me.alki4242.PlayerOrders.Gui.MyOrdersMenu;
import me.alki4242.PlayerOrders.Managers.OrderManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class orderMyOrders  {

    private final MyOrdersMenu menu;

    public orderMyOrders(OrderManager orderManager) {
        this.menu = new MyOrdersMenu(orderManager);
    }

    public void tryOpen(CommandSender sender) {
        Player p = (Player) sender;
        menu.open(p);
    }
}
