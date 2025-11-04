package me.alki4242.PlayerOrders;

import me.alki4242.PlayerOrders.Gui.VaultMenu;
import me.alki4242.PlayerOrders.Managers.ExternalCommandManager;
import me.alki4242.PlayerOrders.Managers.ListenerManager;
import me.alki4242.PlayerOrders.Managers.OrderManager;
import me.alki4242.PlayerOrders.Managers.VaultManager;
import me.alki4242.PlayerOrders.Utils.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class PlayerOrders extends JavaPlugin {

    private OrderManager orderManager;
    private VaultManager vaultManager;
    private ListenerManager listenerManager;
    private ExternalCommandManager externalCommandManager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        new UpdateChecker(this, 129970).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is not a new update available.");
            } else {
                getLogger().info("There is a new update available.");
            }
        });
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
        if (!this.getConfig().getString("config-version").equals(this.getDescription().getVersion())) {
            getLogger().info("Updating config");
            this.getConfig().set("config-version",this.getDescription().getVersion());
            saveConfig();
            try (InputStream defaultConfig = getResource("config.yml")) {
                if (defaultConfig != null)
                    ConfigUpdater.update(defaultConfig, configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            reloadConfig();
        }
        //Load lang
        Lang.load(this,this.getConfig().getString("Language"));
        getLogger().info(Lang.get("plugin.enable"));
        //Initate the database class
        Database db = new Database(this);
        File dbFile = new File(this.getDataFolder(), "database.db");
        db.start(dbFile.getAbsolutePath());
        //Check for whether vault is installed or not
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().info(Lang.get("plugin.disable-due-to-vault"));
            Bukkit.getScheduler().cancelTasks(this);
            Bukkit.getPluginManager().disablePlugin(this);
        }
        RegisteredServiceProvider<Economy> rsp =  getServer().getServicesManager().getRegistration(Economy.class);
        orderManager = new OrderManager(db);
        CommandRegister cr = new CommandRegister(rsp.getProvider());
        vaultManager = new VaultManager(db);
        cr.registerAll(this,orderManager,new VaultMenu(this,vaultManager),vaultManager);
        listenerManager = new ListenerManager(orderManager,rsp.getProvider());
        listenerManager.RegisterAll(this,vaultManager);
        //Register ExternalCommandManager
        externalCommandManager = new ExternalCommandManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info(Lang.get("plugin.disable"));
    }

    //Return instance
    public Plugin getInstance() {
        return this;
    }
    //Return OrderManager
    public OrderManager getOrderManager() {
        return orderManager;
    }
    //Return VaultManager
    public VaultManager getVaultManager() {
        return vaultManager;
    }
    //Return ListenerManager

    public ListenerManager getListenerManager() {
        return listenerManager;
    }
    //Return ExternalCommandManager
    public ExternalCommandManager getExternalCommandManager() {
        return externalCommandManager;
    }
}
