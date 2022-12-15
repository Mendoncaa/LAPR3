package app.domain.model;

import java.util.ArrayList;

public class StatisticBasket {

    ArrayList<ClientBasket> productsOrdered;
    ArrayList<ClientBasket> productsDelivered;

    public StatisticBasket(ArrayList<ClientBasket> productsOrdered, ArrayList<ClientBasket> productsDelivered) {
        this.productsOrdered = productsOrdered;
        this.productsDelivered = productsDelivered;
    }

    public ArrayList<ClientBasket> getProductsOrdered() {
        return productsOrdered;
    }

    public ArrayList<ClientBasket> getProductsDelivered() {
        return productsDelivered;
    }
}
