package me.alki4242.PlayerOrders.Commands;

import me.alki4242.PlayerOrders.Gui.VaultMenu;
import me.alki4242.PlayerOrders.Managers.ExternalCommandManager;
import me.alki4242.PlayerOrders.Managers.OrderManager;
import me.alki4242.PlayerOrders.Managers.VaultManager;
import me.alki4242.PlayerOrders.Utils.Lang;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandExecuter implements CommandExecutor {

    private final OrderManager orderManager;
    private final orderCreate oc;
    private final Economy eco;
    private final orderRemove or;
    private final orderOrders oo;
    private final orderVault ov;
    private final orderMyOrders omo;

    public CommandExecuter(Plugin plugin, OrderManager orderManager, Economy eco, VaultMenu vaultMenu, VaultManager vaultManager) {
        this.orderManager = orderManager;
        this.eco = eco;
        this.oc = new orderCreate(orderManager,eco);
        this.or = new orderRemove(orderManager,eco);
        this.oo = new orderOrders(plugin,orderManager,eco,vaultManager);
        this.ov = new orderVault(vaultMenu);
        this.omo = new orderMyOrders(orderManager);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1) {
            commandSender.sendMessage(Lang.get("usage.main"));
            return true;
        }
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "create":
                oc.tryCreate(commandSender,args);
                break;
            case "remove":
                or.tryRemove(commandSender,args);
                break;
            case "orders":
                oo.tryOpen(commandSender);
                break;
            case "vault":
                ov.tryOpen(commandSender);
                break;
            case "myorders":
                omo.tryOpen(commandSender);
                break;
            default:
                if (!ExternalCommandManager.run(sub, commandSender, args)) {
                    commandSender.sendMessage(Lang.get("usage.main"));
                }
                break;
        }



        return false;
    }
}
