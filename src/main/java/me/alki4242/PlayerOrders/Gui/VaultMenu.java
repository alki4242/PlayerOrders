package me.alki4242.PlayerOrders.Gui;

import me.alki4242.PlayerOrders.Managers.VaultManager;
import me.alki4242.PlayerOrders.Utils.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

public class VaultMenu implements Listener {

    private final VaultManager vaultManager;
    private final Plugin plugin;

    /**
     * Initate the class
     * @param plugin get plugin
     * @param vaultManager get VaultManager class
     */
    public VaultMenu(Plugin plugin, VaultManager vaultManager) {
        this.plugin = plugin;
        this.vaultManager = vaultManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void open(Player p) {
        List<Map<String, Object>> items = vaultManager.getVaultItems(p.getName());
        Inventory inv = Bukkit.createInventory(null, 54, Lang.getWithoutPrefix("vault.title"));

        int slot = 0;
        for (Map<String, Object> data : items) {
            if (slot >= inv.getSize()) break;

            String itemName = (String) data.get("item");
            int amount = (int) data.get("amount");
            int id = (int) data.get("id");

            Material mat = Material.matchMaterial(itemName);
            if (mat == null) continue;

            ItemStack stack = new ItemStack(mat, amount);
            inv.setItem(slot++, stack);
        }

        p.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;

        if (!e.getView().getTitle().equals(Lang.getWithoutPrefix("vault.title"))) return;

        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) return;

        e.setCancelled(true);

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        ItemMeta meta = clicked.getItemMeta();
        if (meta == null) return;

            // Remove from db
            vaultManager.removeItem(e.getSlot() + 1);
            // Remove from GUI
            e.getInventory().setItem(e.getSlot(), null);

            // Give item to player
            p.getInventory().addItem(clicked.clone());


            p.sendMessage(Lang.get("vault.item_taken"));

            // Refresh the GUI
            Bukkit.getScheduler().runTaskLater(plugin, () -> open(p), 2L);
    }

    @EventHandler
    public void onVaultClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals(Lang.getWithoutPrefix("vault.title"))) {
        }
    }
}
