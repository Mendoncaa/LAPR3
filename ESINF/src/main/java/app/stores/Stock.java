package app.stores;

import app.controller.App;
import app.domain.model.ClientBasket;
import app.domain.model.ClientsProducers;
import app.domain.model.Product;

import java.util.*;

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

    public void addHubs() {
        if (!App.getInstance().getCompany().getHubStore().isEmpty()) {
            ArrayList<ClientsProducers> hubs = App.getInstance().getCompany().getHubStore().getHubs();
            Iterator<Integer> iterator = stock.keySet().iterator();
            int day = iterator.next();
            ArrayList<Product> arr= new ArrayList<>(cleanArray(stock.get(day).get(0).getProducts()));


            for (int i = 0; i < stock.keySet().size(); i++) {
                for (int j = 0; j < hubs.size(); j++) {
                    addStock(day, new ClientBasket(hubs.get(j), arr));
                }
                day = iterator.next();
            }
        }
    }

    private ArrayList<Product> cleanArray(ArrayList<Product> products) {
        ArrayList<Product> array = new ArrayList<>(products);
        for (int i = 0; i < products.size(); i++) {
            array.get(i).setQuantity(0);
        }
        return products;
    }
}
