package me.alki4242.PlayerOrders.Events;

import me.alki4242.PlayerOrders.Objects.Order;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OrderCompleteEvent extends Event {

    private final String completerName;
    private final Order order;

    /**
     *
     * @param completerName the name of the player who has completed the order
     * @param order the order
     */
    public OrderCompleteEvent(String completerName, Order order) {
        this.order = order;
        this.completerName = completerName;
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
     * @return returns the name of the player who has completed the order
     */
    public String getCompleterName() {
        return completerName;
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
