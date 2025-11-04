package me.alki4242.PlayerOrders.Objects;

import me.alki4242.PlayerOrders.Utils.Database;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Order {
    private int id;
    private String sender;
    private String item;
    private int amount;
    private double reward;
    private boolean completed;
    private Database db;

    /**
     * Initiate the class
     * To get an order, all values must be taken from database
     * @param id the id of the order
     * @param sender the sender of the order
     * @param item the item of the order
     * @param amount the amount of the order
     * @param reward the reward of the order
     * @param completed the completed status of the order
     * @param db get Database class
     */
    public Order(int id,String sender,String item,int amount,double reward,boolean completed,Database db) {
        this.id = id;
        this.sender = sender;
        this.item = item;
        this.amount = amount;
        this.reward = reward;
        this.completed = completed;
        this.db = db;
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public double getReward() {
        return reward;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean value) {
        this.completed = value;
    }
    public void setReward(double value) {
        this.reward = value;
    }

    /**
     * Save all changes to the database
     */
    public void saveToDatabase() {
        try {
            PreparedStatement ps = db.prepareStatement("UPDATE Orders SET sender=?,item=?,amount=?,reward=?,completed=? WHERE id=?");
            ps.setString(1,sender);
            ps.setString(2,item);
            ps.setInt(3,amount);
            ps.setDouble(4,reward);
            ps.setBoolean(5,completed);
            ps.setInt(6,id);
            db.executeCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
