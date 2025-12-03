package me.alki4242.PlayerOrders.Events;

import me.alki4242.PlayerOrders.Objects.Order;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OrderCreateEvent extends Event  {

    private final String senderName;
    private final Order order;
    /**
     *
     * @param senderName the name of the player who has created the order
     * @param order the order
     */
    public OrderCreateEvent (String senderName, Order order) {
        this.order = order;
        this.senderName = senderName;
    }
    private final HandlerList handlerList = new HandlerList();

    /**
     *
     * @return returns the order
     */
    public Order getOrder() {
        return order;
    }

    /**
     *
     * @return returns the name of the sender
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     *
     * @return returns handlerList
     */
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
