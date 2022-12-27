package app.stores;

import app.controller.App;
import app.domain.model.ClientBasket;
import app.domain.model.ClientsProducers;
import app.domain.model.Product;

import java.util.*;

public class Stock {

    private Map<Integer, ArrayList<ClientBasket>> stock;

    private Map<Integer, ArrayList<ClientBasket>> stockClone;

    public Stock() {
        stock = new TreeMap<>();
        stockClone = new TreeMap<>();
    }

    public void addStock(int id, ClientBasket basket) {
        if (stock.containsKey(id)) {
            stock.get(id).add(basket);
        } else {
            stock.put(id, new ArrayList<>());
            stock.get(id).add(basket);
        }
        this.stockClone = cloneMap(this.stock);
    }


    public Map<Integer, ArrayList<ClientBasket>> getStock() {
        return stock;
    }

    public Map<Integer, ArrayList<ClientBasket>> getStockClone() {
        return stockClone;
    }

    /**
     * adicionar os hubs inicializados c os produtos a 0, em cada dia do stock
     */
    public void addHubs() {
        if (!App.getInstance().getCompany().getHubStore().isEmpty()) {
            ArrayList<ClientsProducers> hubs = App.getInstance().getCompany().getHubStore().getHubs();
            Map<Integer, ArrayList<ClientBasket>> stock2 = cloneMap(stock);
            Map<Integer, ArrayList<ClientBasket>> stock3 = cloneMap(stock);

            Iterator<Integer> iterator = stock2.keySet().iterator();
            Iterator<Integer> iterator2 = stock3.keySet().iterator();

            int day = iterator.next();
            int size = stock2.get(day).get(0).getProducts().size();
            ArrayList<Product> products = new ArrayList<>(stock2.get(day).get(0).getProducts());
            String[] prods = new String[size];
            for (int i = 0; i < size; i++) {
                prods[i] = products.get(i).getName();
            }
            int day2 = 0;

            while (iterator2.hasNext()) {
                day2 = iterator2.next();
                for (ClientsProducers hub : hubs) {
                    addStock(day2, new ClientBasket(hub, createArray(prods)));
                }
            }

            for (ClientsProducers hub : hubs) {
                addStock(day2 + 1, new ClientBasket(hub, createArray(prods)));
            }
            this.stockClone = cloneMap(this.stock);
        }
    }

    private ArrayList<Product> createArray(String[] prods) {
        ArrayList<Product> array = new ArrayList<>();
        for (int i = 0; i < prods.length; i++) {
            array.add(new Product(prods[i], 0));
        }
        return array;
    }

    private static Map<Integer, ArrayList<ClientBasket>> cloneMap(Map<Integer, ArrayList<ClientBasket>> map) {
        return new TreeMap<>(map);
    }

    public void fillStockClone(){
        this.stockClone = cloneMap(this.stock);
    }
}
