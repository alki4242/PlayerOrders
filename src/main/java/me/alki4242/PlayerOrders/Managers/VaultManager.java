package me.alki4242.PlayerOrders.Managers;

import me.alki4242.PlayerOrders.Utils.Database;
import me.alki4242.PlayerOrders.Utils.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


import java.sql.*;
import java.util.*;

public class VaultManager {

    private final Database db;

    /**
     * Initiate the class
     * @param db get database class
     */
    public VaultManager(Database db) {
        this.db = db;
    }

    /**
     * Add item to the vault of the user
     * @param owner get owner
     * @param item get item
     * @param amount get amount
     */
    public void addToVault(String owner, String item, int amount) {
        try {
            int lastId = 0;
            try (PreparedStatement psMax = db.prepareStatement("SELECT MAX(id) AS maxId FROM Vaults")) {
                try (ResultSet rs = psMax.executeQuery()) {
                    if (rs.next()) {
                        lastId = rs.getInt("maxId");
                    }
                }
            }
            int newId = lastId + 1;
            PreparedStatement ps = db.prepareStatement("INSERT INTO Vaults(owner, item, amount, id) VALUES (?, ?, ?, ?)");
            ps.setString(1, owner);
            ps.setString(2, item);
            ps.setInt(3, amount);
            ps.setInt(4, newId);
            db.executeCommand(ps);

            Player p = Bukkit.getPlayerExact(owner);
            if (p != null)
                p.sendMessage(Lang.get("order.own_completed"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the vault items of the user
     * @param owner get owner
     * @return list
     */
    public List<Map<String, Object>> getVaultItems(String owner) {
        List<Map<String, Object>> list = new ArrayList<>();
        try (PreparedStatement ps = db.prepareStatement("SELECT * FROM Vaults WHERE owner=?")) {
            ps.setString(1, owner);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", rs.getInt("id"));
                    m.put("item", rs.getString("item"));
                    m.put("amount", rs.getInt("amount"));
                    list.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Remove item from the vault of the user
     * @param id get the id of the item
     */
    public void removeItem(int id) {
        try  {
            PreparedStatement ps = db.prepareStatement("DELETE FROM Vaults WHERE id=?");
            ps.setInt(1, id);
            db.executeCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
