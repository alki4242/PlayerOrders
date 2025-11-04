package me.alki4242.PlayerOrders.Gui;

import me.alki4242.PlayerOrders.Managers.OrderManager;
import me.alki4242.PlayerOrders.Objects.Order;
import me.alki4242.PlayerOrders.Utils.Lang;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MyOrdersMenu implements Listener {

    private final OrderManager orderManager;
    private final Map<UUID, Integer> currentPage;

    private static final int ORDERS_PER_PAGE = 45;

    /**
     * Initiate the class
     * @param orderManager get OrderManager class
     */
    public MyOrdersMenu(OrderManager orderManager) {
        this.orderManager = orderManager;
        this.currentPage = new HashMap<>();
    }

    public void open(Player player) {
        openPage(player, 0);
    }

    private void openPage(Player player, int page) {
        List<Order> orders = orderManager.getOrders();
        int maxPage = Math.max(0, (orders.size() - 1) / ORDERS_PER_PAGE);
        page = Math.max(0, Math.min(page, maxPage));
        currentPage.put(player.getUniqueId(), page);

        String title = Lang.getWithoutPrefix("mymenu.title") + " §7(Page " + (page + 1) + "/" + (maxPage + 1) + ")";
        Inventory inv = Bukkit.createInventory(null, 54, title);

        int start = page * ORDERS_PER_PAGE;
        int end = Math.min(start + ORDERS_PER_PAGE, orders.size());

        for (int i = start; i < end; i++) {
            Order order = orders.get(i);
            if (!order.getSender().equals(player.getName())) continue;
            Material mat = Material.matchMaterial(order.getItem());
            if (mat == null) mat = Material.PAPER;
            ItemStack item = new ItemStack(mat, Math.min(order.getAmount(), mat.getMaxStackSize()));
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(Lang.getWithoutPrefix("menu.item.name").replace("%id%", String.valueOf(order.getId())));
            List<String> lore = new ArrayList<>();
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
        if (!e.getView().getTitle().startsWith(Lang.getWithoutPrefix("mymenu.title"))) return;
        e.setCancelled(true);
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        ItemStack clicked = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();

        if (clicked.getType() == Material.ARROW && clicked.hasItemMeta()) {
            String name = clicked.getItemMeta().getDisplayName();
            int current = currentPage.getOrDefault(p.getUniqueId(), 0);
            if (name.equals(Lang.getWithoutPrefix("menu.next"))) openPage(p, current + 1);
            else if (name.equals(Lang.getWithoutPrefix("menu.previous"))) openPage(p, current - 1);
            return;
        }
    }

}
