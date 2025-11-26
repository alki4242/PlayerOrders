package me.alki4242.PlayerOrders.Managers;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExternalCommandManager {

    private static final Map<String, ExternalCommand> subs = new HashMap<>();

    /**
     * Register your own sub-command
     * @param sub your sub command class which implement ExternalCommand
     */
    public static void register(ExternalCommand sub) {
        subs.put(sub.getName().toLowerCase(), sub);
    }

    /**
     * Plugin will look for external subcommands
     * @param name the name of the command
     * @param sender the sender of the command
     * @param args the args of the command
     * @return return valid
     */
    public static boolean run(String name, CommandSender sender, String[] args) {
        ExternalCommand sub = subs.get(name.toLowerCase());
        if (sub == null) return false;
        sub.execute(sender, args);
        return true;
    }

    /**
     * Get registered sub-commands
     * @return sub-commands
     */
    public static Set<String> getRegisteredSubs() {
        return subs.keySet();
    }
    /**
     * Interface for implementation
     */
    public interface ExternalCommand {
        String getName();
        void execute(CommandSender sender, String[] args);
    }

}
