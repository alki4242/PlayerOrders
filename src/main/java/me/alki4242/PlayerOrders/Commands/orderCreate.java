package me.alki4242.PlayerOrders.Commands;

import me.alki4242.PlayerOrders.Events.OrderCreateEvent;
import me.alki4242.PlayerOrders.Managers.OrderManager;
import me.alki4242.PlayerOrders.Utils.Lang;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.concurrent.ThreadLocalRandom;

public class orderCreate {
    private final OrderManager orderManager;
    private final Economy eco;
    public orderCreate(OrderManager orderManager,Economy eco) {
        this.orderManager = orderManager;
        this.eco = eco;
    }

    public void tryCreate(CommandSender sender,String[] args) {
        if (args.length < 4) {
            sender.sendMessage(Lang.get("usage.create"));
            return;
        }
        String itemName = args[1];
        int amount = Integer.parseInt(args[2]);
        double reward = Double.parseDouble(args[3]);
         Material mat = Material.getMaterial(itemName.toUpperCase());
        if (mat == null || !mat.isItem()) {
            sender.sendMessage(Lang.get("create.invalid_item"));
            return;
        }
        if (amount > 64) {
            sender.sendMessage(Lang.get("create.bigger_than_max"));
            return;
        }
        if (eco.getBalance(sender.getName()) < reward) {
            sender.sendMessage(Lang.get("create.not_enough_money").replace("%amount%",String.valueOf(reward)));
            return;
        }
        int id;
        do {
            id = ThreadLocalRandom.current().nextInt(1,10001);
        } while (orderManager.isValidOrder(id));

        //Withdraw reward
        eco.withdrawPlayer(sender.getName(), reward);
        //Create order
        orderManager.addOrder(id, sender.getName(), itemName,amount,reward,false);
        sender.sendMessage(Lang.get("create.successful"));
        Bukkit.getPluginManager().callEvent(new OrderCreateEvent(sender.getName(),orderManager.getOrder(id)));
    }
}
