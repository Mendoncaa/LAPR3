package app.stores;

import app.domain.model.ClientBasket;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Stock {

    private Map<Integer, ArrayList<ClientBasket>> stock;

    public Stock() {
        stock = new TreeMap<>();
    }

    public void addStock(int id, ClientBasket basket) {
        if (stock.containsKey(id)) {
            stock.get(id).add(basket);
        } else {
            stock.put(id, new ArrayList<>());
            stock.get(id).add(basket);
        }
    }

    public Map<Integer, ArrayList<ClientBasket>> getStock() {
        return stock;
    }

    public void updateStock(Map<Integer, ArrayList<ClientBasket>> stock) {
        this.stock = stock;
    }
}
