package me.alki4242.PlayerOrders.Utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class Lang {
    private static FileConfiguration langConfig;
    private static File langFile;
    private static String prefix;

    public static void load(Plugin plugin,String language) {
        langFile = new File(plugin.getDataFolder(), language +".yml");

        // Copy file if absent
        if (!langFile.exists()) {
            plugin.saveResource(language + ".yml", false);
        }

        langConfig = YamlConfiguration.loadConfiguration(langFile);

        // Get prefix
        prefix = color(langConfig.getString("Prefix", "&7[PlayerOrders] "));
    }

    public static String get(String path) {
        String value = langConfig.getString(path);
        if (value == null) {
            return prefix + ChatColor.RED + "Message could'nt be found: " + path;
        }
        return prefix + color(value);
    }

    public static String get(String path, String... placeholders) {
        String value = langConfig.getString(path);
        if (value == null) {
            return prefix + ChatColor.RED + "Message could'nt be found: " + path;
        }

        // Change placeholders
        for (int i = 0; i < placeholders.length; i += 2) {
            String key = placeholders[i];
            String replacement = placeholders[i + 1];
            value = value.replace("%" + key + "%", replacement);
        }

        return prefix + color(value);
    } public static String getWithoutPrefix(String path, String... placeholders) {
        String value = langConfig.getString(path);
        if (value == null) {
            return prefix + ChatColor.RED + "Message could'nt be found: " + path;
        }

        //Change placeholders
        for (int i = 0; i < placeholders.length; i += 2) {
            String key = placeholders[i];
            String replacement = placeholders[i + 1];
            value = value.replace("%" + key + "%", replacement);
        }

        return color(value);
    }

    public static void reload() {
        try {
            langConfig.load(langFile);
            prefix = color(langConfig.getString("Prefix", "&7[TrapPvP] "));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            langConfig.save(langFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
