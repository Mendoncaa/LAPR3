package app.stores;

import app.domain.model.ClientBasket;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Orders {

    private Map<Integer, ArrayList<ClientBasket>> orders;
    private Map<Integer, ArrayList<ClientBasket>> hubsOrders;

    public Orders() {
        orders = new TreeMap<>();
        hubsOrders = new TreeMap<>();
    }

    public void addOrder(int id, ClientBasket basket) {
        if (orders.containsKey(id)) {
            orders.get(id).add(basket);
        } else {
            orders.put(id, new ArrayList<>());
            orders.get(id).add(basket);
        }
    }

    public void addHubOrder(int id, ClientBasket basket) {
        if (hubsOrders.containsKey(id)) {
            hubsOrders.get(id).add(basket);
        } else {
            hubsOrders.put(id, new ArrayList<>());
            hubsOrders.get(id).add(basket);
        }
    }

    public Map<Integer, ArrayList<ClientBasket>> getOrders() {
        return orders;
    }

    public Map<Integer, ArrayList<ClientBasket>> getHubsOrders() {
        return hubsOrders;
    }

    public void updateOrders(Map<Integer, ArrayList<ClientBasket>> orders) {
        this.orders = orders;
    }

}
