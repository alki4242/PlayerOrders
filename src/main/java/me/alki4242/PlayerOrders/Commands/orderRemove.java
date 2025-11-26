package me.alki4242.PlayerOrders.Commands;

import me.alki4242.PlayerOrders.Managers.OrderManager;
import me.alki4242.PlayerOrders.Objects.Order;
import me.alki4242.PlayerOrders.Utils.Lang;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;

public class orderRemove {
    private final OrderManager orderManager;
    private final Economy eco;

    public orderRemove(OrderManager orderManager,Economy eco) {
        this.orderManager = orderManager;
        this.eco = eco;
    }
    /*{
    @param1

     }*/
    public void tryRemove(CommandSender sender,String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Lang.get("usage.remove"));
            return;
        }
        int id = Integer.parseInt(args[1]);

        if (!orderManager.isValidOrder(id)) {
            sender.sendMessage(Lang.get("remove.not_valid_order"));
            return;
        }

        Order order = orderManager.getOrder(id);

        if (!order.getSender().equals(sender.getName())) {
            sender.sendMessage(Lang.get("remove.not_owner"));
            return;
        }

        //Give reward back
        eco.depositPlayer(sender.getName(), order.getReward());
        //Remove order
        orderManager.removeOrder(id);
        sender.sendMessage(Lang.get("remove.successful"));
    }
}
