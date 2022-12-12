package app.stores;

import app.domain.model.ClientBasket;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Orders {

    private Map<Integer, ArrayList<ClientBasket>> orders;

    public Orders() {
        orders = new TreeMap<>();
    }

    public void addOrder(int id, ClientBasket basket) {
        if (orders.containsKey(id)) {
            orders.get(id).add(basket);
        } else {
            orders.put(id, new ArrayList<>());
            orders.get(id).add(basket);
        }
    }

    public Map<Integer, ArrayList<ClientBasket>> getOrders() {
        return orders;
    }

    public void updateOrders(Map<Integer, ArrayList<ClientBasket>> orders) {
        this.orders = orders;
    }

}
