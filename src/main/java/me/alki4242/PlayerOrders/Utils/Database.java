package me.alki4242.PlayerOrders.Utils;

import me.alki4242.PlayerOrders.Objects.Order;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.*;

public class Database {
    private Connection db;
    private final Plugin plugin;

    /**
     *  Initiate the class
     * @param plugin get plugin
     */
    public Database(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Initiate database connection
     * @param path get the path of the database
     */
    public void start(String path) {
        try {
            db = DriverManager.getConnection("jdbc:sqlite:" + path);
            try (Statement s = db.createStatement()) {
                s.execute("CREATE TABLE IF NOT EXISTS Orders (id INTEGER PRIMARY KEY, sender TEXT, item TEXT, amount INTEGER, reward REAL, completed INTEGER)");
                s.execute("CREATE TABLE IF NOT EXISTS Vaults (id INTEGER PRIMARY KEY, owner TEXT, item TEXT, amount INTEGER)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute SQL commands
     * @param ps get sql command
     */
    public void executeCommand(PreparedStatement ps) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (ps) {
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     *
     * @param sql get query
     * @return return PreparedStatement will be executed
     * @throws SQLException Exception
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return db.prepareStatement(sql);
    }

    public Order getOrder(int id) {
        try (PreparedStatement ps = db.prepareStatement("SELECT * FROM Orders WHERE id=?")) {
            ps.setInt(1,id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String sender = rs.getString("sender");
                    String item = rs.getString("item");
                    int amount = rs.getInt("amount");
                    double reward = rs.getDouble("reward");
                    boolean completed = rs.getBoolean("completed");
                    return new Order(id,sender,item,amount,reward,completed,this);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
