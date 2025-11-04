package me.alki4242.PlayerOrders.Gui;

import me.alki4242.PlayerOrders.Managers.OrderManager;
import me.alki4242.PlayerOrders.Managers.VaultManager;
import me.alki4242.PlayerOrders.Objects.Order;
import me.alki4242.PlayerOrders.PlayerOrders;
import me.alki4242.PlayerOrders.Utils.Database;
import me.alki4242.PlayerOrders.Utils.Lang;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class OrderMenu implements Listener {

    private final OrderManager orderManager;
    private final Economy economy;
    private final Map<UUID, Integer> currentPage;
    private final VaultManager vaultManager;
    private final Plugin plugin;
    private static final int ORDERS_PER_PAGE = 45;

    /**
     * Initate the class
     * @param plugin get plugin
     * @param orderManager get OrderManager class
     * @param economy get economy class
     * @param vaultManager get VaultManager class
     */
    public OrderMenu(Plugin plugin, OrderManager orderManager, Economy economy, VaultManager vaultManager) {
        this.plugin = plugin;
        this.orderManager = orderManager;
        this.economy = economy;
        this.vaultManager = vaultManager;
        this.currentPage = new HashMap<>();

    }

    /**
     * Open page for player
     * @param player player
     */
    public void open(Player player) {
        openPage(player, 0);
    }

    private void openPage(Player player, int page) {
        List<Order> orders = orderManager.getOrders();
        int maxPage = Math.max(0, (orders.size() - 1) / ORDERS_PER_PAGE);
        page = Math.max(0, Math.min(page, maxPage));
        currentPage.put(player.getUniqueId(), page);

        String title = Lang.getWithoutPrefix("menu.title") + " §7(Page " + (page + 1) + "/" + (maxPage + 1) + ")";
        Inventory inv = Bukkit.createInventory(null, 54, title);

        int start = page * ORDERS_PER_PAGE;
        int end = Math.min(start + ORDERS_PER_PAGE, orders.size());

        for (int i = start; i < end; i++) {
            Order order = orders.get(i);
            Material mat = Material.matchMaterial(order.getItem());
            if (mat == null) mat = Material.PAPER;
            ItemStack item = new ItemStack(mat, Math.min(order.getAmount(), mat.getMaxStackSize()));
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(Lang.getWithoutPrefix("menu.item.name").replace("%id%", String.valueOf(order.getId())));
            List<String> lore = new ArrayList<>();
            lore.add(Lang.getWithoutPrefix("menu.item.sender").replace("%sender%", order.getSender()));
            lore.add(Lang.getWithoutPrefix("menu.item.reward").replace("%reward%", String.valueOf(order.getReward())));
            lore.add(Lang.getWithoutPrefix("menu.item.completed").replace("%completed%", String.valueOf(order.isCompleted())));
            lore.add(Lang.getWithoutPrefix("menu.item.id").replace("%id%", String.valueOf(order.getId())));
            meta.setLore(lore);

            item.setItemMeta(meta);
            inv.addItem(item);
        }

        // Navigation buttons
        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName(Lang.getWithoutPrefix("menu.next"));
        next.setItemMeta(nextMeta);
        inv.setItem(53, next);

        ItemStack prev = new ItemStack(Material.ARROW);
        ItemMeta prevMeta = prev.getItemMeta();
        prevMeta.setDisplayName(Lang.getWithoutPrefix("menu.previous"));
        prev.setItemMeta(prevMeta);
        inv.setItem(45, prev);

        // Glass filler
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fm = filler.getItemMeta();
        fm.setDisplayName(" ");
        filler.setItemMeta(fm);
        for (int i = 46; i < 53; i++) inv.setItem(i, filler);

        player.openInventory(inv);
    }
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().startsWith(Lang.getWithoutPrefix("menu.title"))) return;
        e.setCancelled(true);
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

        Player p = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();

        if (clicked.getType() == Material.ARROW && clicked.hasItemMeta()) {
            String name = clicked.getItemMeta().getDisplayName();
            int current = currentPage.getOrDefault(p.getUniqueId(), 0);
            if (name.equals(Lang.getWithoutPrefix("menu.next"))) openPage(p, current + 1);
            else if (name.equals(Lang.getWithoutPrefix("menu.previous"))) openPage(p, current - 1);
            return;
        }

        if (!clicked.hasItemMeta() || !clicked.getItemMeta().hasLore()) return;
        List<String> lore = clicked.getItemMeta().getLore();
        int id = Integer.parseInt(ChatColor.stripColor(lore.get(3)).replaceAll("\\D+", ""));
        Order order = orderManager.getOrder(id);
        if (order == null) return;
        if (order.isCompleted()) return;
        if (order.getSender().equals(p.getName())) return;
        Material mat = Material.matchMaterial(order.getItem());
        if (mat == null) return;

        int amount = order.getAmount();
        if (p.getInventory().containsAtLeast(new ItemStack(mat), amount)) {
            p.getInventory().removeItem(new ItemStack(mat, amount));
            economy.depositPlayer(p, order.getReward());
            p.sendMessage(Lang.get("order.completed")
                    .replace("%id%", String.valueOf(id))
                    .replace("%reward%", String.valueOf(order.getReward())));
            vaultManager.addToVault(order.getSender(), order.getItem(), order.getAmount());
            order.setCompleted(true);
            order.saveToDatabase();
            if (plugin.getConfig().getBoolean("Remove-Completed-Orders")) orderManager.removeOrder(id);
            p.closeInventory();
        } else {
            p.sendMessage(Lang.get("order.not_enough"));
        }
    }

}
