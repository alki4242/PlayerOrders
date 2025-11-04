package me.alki4242.PlayerOrders.Utils;

import me.alki4242.PlayerOrders.Managers.ExternalCommandManager;
import me.alki4242.PlayerOrders.Managers.OrderManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class tabCompleter implements TabCompleter {

    private final ArrayList<String> list;
    private final OrderManager orderManager;
    public tabCompleter(OrderManager orderManager) {
        this.list = new ArrayList<>();
        list.add("vault");
        list.add("orders");
        list.add("myorders");
        list.add("create");
        list.add("remove");
        list.addAll(ExternalCommandManager.getRegisteredSubs());
        this.orderManager = orderManager;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return null;

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            for (String cmd : list) {
                if (cmd.toLowerCase().startsWith(args[0].toLowerCase())) completions.add(cmd);
            }
            return completions;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 2) {
                // arg1: Items in minecraft
                return Arrays.stream(Material.values())
                        .map(Material::name)
                        .collect(Collectors.toList());
            } else if (args.length == 3) {
                // arg2: "Amount"
                return Arrays.asList(Lang.getWithoutPrefix("amount"));
            } else if (args.length == 4) {
                // arg3: "Reward"
                return Arrays.asList(Lang.getWithoutPrefix("reward"));
            }
        }
        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                return orderManager.getOrders().stream()
                        .map(order -> String.valueOf(order.getId()))
                        .collect(Collectors.toList());
            }
        }

        return null;
    }
}
