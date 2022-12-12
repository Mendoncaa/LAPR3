package app.domain.shared;

import app.controller.App;
import app.domain.model.ClientBasket;
import app.domain.model.ExpeditionList;
import app.domain.model.Product;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ExpeditionListCreator {

    public static Set<ExpeditionList> getExpeditionListNoRestrictions(int day) {

        Map<Integer, ArrayList<ClientBasket>> orders = App.getInstance().getCompany().getOrders().getOrders();
        Map<Integer, ArrayList<ClientBasket>> stock = App.getInstance().getCompany().getStock().getStock();

        Set<Integer> ordersKeys = orders.keySet();
        Set<ExpeditionList> expeditionListSet = new TreeSet<>();            // expedition list of day key
        boolean changes = false;

        // For each day
        if (stock.containsKey(day)) {
            ArrayList<ClientBasket> clientBasketsOrders = orders.get(day);      // orders of day key
            ArrayList<ClientBasket> clientBasketsStock = stock.get(day);        // stock of day key

            // for each client basket in orders
            for (int k = 0; k < clientBasketsOrders.size(); k++) {

                ArrayList<Product> productsOrder = clientBasketsOrders.get(k).getProducts();       // products of one order

                // for each product inside each client basket in order
                for (int i = 0; i < productsOrder.size(); i++) {

                    Product productOrder = productsOrder.get(i);      // product of one order
                    int productOwner = findProductOwner(clientBasketsStock, productOrder);   // find product owner id
                    Product productInStock = returnProduct(clientBasketsStock.get(productOwner), productOrder); // return product that is in stock

                    if (productInStock != null) {

                        if (productInStock.getQuantity() >= productOrder.getQuantity()) {
                            productInStock.setQuantity(productInStock.getQuantity() - productOrder.getQuantity());
                            productsOrder.remove(i);                                                                        //analisar melhor
                            i--;
                            changes = true;
                        } else {
                            productOrder.setQuantity(productOrder.getQuantity() - productInStock.getQuantity());
                            productInStock.setQuantity(0);                                                                //analisar melhor
                            changes = true;
                        }
                    } else {
                        System.out.println("Product not found in stock --> " + productOrder.getName());
                    }

                }

                if (!changes) {
                    clientBasketsOrders.remove(k);
                    k--;
                }

                changes = false;
            }
        }

        App.getInstance().getCompany().updateStock(stock);
        App.getInstance().getCompany().updateOrders(orders);
        return expeditionListSet;
    }

    private static int findProductOwner(ArrayList<ClientBasket> stock, Product product) {

        for (int j = 0; j < stock.size(); j++) {
            ArrayList<Product> products = stock.get(j).getProducts();
            for (Product value : products) {
                if (value.equals(product) && value.getQuantity() > 0) {
                    return j;
                }
            }
        }
        return -1;
    }

    private static Product returnProduct(ClientBasket stock, Product product) {

        ArrayList<Product> products = stock.getProducts();
        for (Product value : products) {
            if (value.equals(product)) {
                return value;
            }
        }
        return null;
    }

}

