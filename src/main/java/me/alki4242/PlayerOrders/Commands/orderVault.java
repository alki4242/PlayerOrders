package me.alki4242.PlayerOrders.Commands;

import me.alki4242.PlayerOrders.Gui.VaultMenu;
import me.alki4242.PlayerOrders.Utils.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class orderVault {

    private final VaultMenu vaultGui;

    public orderVault(VaultMenu vaultGui) {
        this.vaultGui = vaultGui;
    }


    public void tryOpen(CommandSender sender) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(Lang.get("order.only_players"));
            return;
        }

        vaultGui.open(p);
    }

}
