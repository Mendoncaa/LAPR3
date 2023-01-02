package app.domain.model;

import java.util.ArrayList;

public class ClientBasket {

    private ClientsProducers entity;
    private ArrayList<Product> products;

    public ClientBasket(ClientsProducers entity, ArrayList<Product> products) {
        this.entity = entity;
        this.products = products;
    }

    public void addBasket(ClientBasket basket) {
        this.entity = basket.getEntity();
        this.products.addAll(basket.getProducts());
    }

    public ClientsProducers getEntity() {
        return entity;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<Product> cloneProducts() {
        ArrayList<Product> clone = new ArrayList<>();
        for (Product p : products) {
            clone.add(new Product(p.getName(), new Float(p.getQuantity())));
        }
        return clone;
    }

    public ClientBasket clone() {
        return new ClientBasket(this.entity, cloneProducts());
    }
}
