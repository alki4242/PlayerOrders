package me.alki4242.PlayerOrders.Managers;

import me.alki4242.PlayerOrders.Objects.Order;
import me.alki4242.PlayerOrders.Utils.Database;
import org.bukkit.plugin.Plugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private final Database db;

    /**
     * Initiate the class
     * @param db get Database class
     */
    public OrderManager(Database db) {
        this.db = db;
    }

    /**
     * Create new order. To create a new order, all values must be defined.
     * @param id the id of the order
     * @param sender the sender of the order
     * @param item the item of the order
     * @param amount the amount of the order
     * @param reward the reward of the order
     * @param completed the completed status of the order
     */
    public void addOrder(int id,String sender,String item,int amount,double reward,boolean completed) {
        try {
            PreparedStatement ps = db.prepareStatement("INSERT INTO Orders(id,sender,item,amount,reward,completed) VALUES(?,?,?,?,?,?)");
            ps.setInt(1,id);
            ps.setString(2,sender);
            ps.setString(3,item);
            ps.setInt(4,amount);
            ps.setDouble(5,reward);
            ps.setBoolean(6,completed);
            db.executeCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove order via its id
     * @param id the id of the order
     */
    public void removeOrder(int id) {
        try {
            PreparedStatement ps = db.prepareStatement("DELETE FROM Orders WHERE id=?");
            ps.setInt(1,id);
            db.executeCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get order class via id
     * @param id the id of the order
     * @return Order Class (Order)
     */
    public Order getOrder(int id) {
        return db.getOrder(id);
    }

    /**
     * Check for whether order is defined or not
     * @param id the id of the order
     * @return the result
     */
    public boolean isValidOrder(int id) {
        try (PreparedStatement ps = db.prepareStatement("SELECT * FROM Orders WHERE id=?")) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Get all orders
     * @return Orders
     */
    public List<Order> getOrders() {
        try {
            ArrayList list = new ArrayList<>();
            PreparedStatement ps = db.prepareStatement("SELECT * FROM Orders");
            ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               Order order = new Order(
                       rs.getInt("id"),
                       rs.getString("sender"),
                       rs.getString("item"),
                       rs.getInt("amount"),
                       rs.getDouble("reward"),
                       rs.getBoolean("completed"),db);
               list.add(order);
           }
           ps.close();
           return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
