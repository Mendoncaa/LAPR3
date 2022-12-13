package app.domain.shared;

import app.controller.App;
import app.domain.model.*;

import java.util.ArrayList;
import java.util.Map;

public class ExpeditionListCreator {

    public static ArrayList<ExpeditionList> getExpeditionListNoRestrictions(int day) {

        float quantity_delivered;

        ArrayList<ExpeditionList> expeditionListSet = new ArrayList<>();            // expedition list of day key

        // For each day
        if (App.getInstance().getCompany().getStock().getStock().containsKey(day)) {
            ArrayList<ClientBasket> clientBasketsOrders = App.getInstance().getCompany().getOrders().getOrders().get(day);      // orders of day key
            ArrayList<ClientBasket> clientBasketsStock = App.getInstance().getCompany().getStock().getStock().get(day);        // stock of day key

            // for each client basket in orders
            for (ClientBasket clientBasketsOrder : clientBasketsOrders) {

                ArrayList<Product> productsOrder = clientBasketsOrder.getProducts();       // products of one order
                ExpeditionList expeditionList = new ExpeditionList(clientBasketsOrder.getEntity());     // expedition list of one order

                // for each product inside each client basket in order
                // product of one order
                for (Product productOrder : productsOrder) {

                    if (productOrder.getQuantity() > 0) {

                        int productOwner = findProductOwner(clientBasketsStock, productOrder);   // find product owner id

                        if (productOwner != -1) {

                            int productInStockID = returnProductID(clientBasketsStock.get(productOwner), productOrder); // return product id that is in stock

                            if (productInStockID != -1) {

                                Product productInStock = clientBasketsStock.get(productOwner).getProducts().get(productInStockID); // product that is in stock
                                float quantity_ordered = productOrder.getQuantity();

                                if (productInStock.getQuantity() >= productOrder.getQuantity()) {
                                    quantity_delivered = productOrder.getQuantity();
                                    clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - productOrder.getQuantity());    //atualizar stock
                                    expeditionList.getBasketElements().add(new BasketElement(new Product(productOrder.getName(), quantity_delivered), clientBasketsStock.get(productOwner).getEntity()));          // add product to expedition list
                                    expeditionList.getBasketOrderedElements().add(new BasketElement(new Product(productOrder.getName(), quantity_ordered), clientBasketsStock.get(productOwner).getEntity()));          // add product ordered to expedition list
                                } else {
                                    quantity_delivered = productInStock.getQuantity();
                                    clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(0);    //atualizar stock
                                    expeditionList.getBasketElements().add(new BasketElement(new Product(productOrder.getName(), quantity_delivered), clientBasketsStock.get(productOwner).getEntity()));          // add product to expedition list
                                    expeditionList.getBasketOrderedElements().add(new BasketElement(new Product(productOrder.getName(), quantity_ordered), clientBasketsStock.get(productOwner).getEntity()));
                                }
                            }
                        }
                    }
                }
                expeditionListSet.add(expeditionList);
            }

            App.getInstance().getCompany().getStock().getStock().remove(day);
            App.getInstance().getCompany().getStock().getStock().put(day, clientBasketsStock);
            App.getInstance().getCompany().getOrders().getOrders().remove(day);
            App.getInstance().getCompany().getOrders().getOrders().put(day, clientBasketsOrders);

        }

        return expeditionListSet;
    }

    private static int findProductOwner(ArrayList<ClientBasket> stock, Product product) {
        boolean found = false;
        for (int j = 0; j < stock.size(); j++) {
            ArrayList<Product> products = stock.get(j).getProducts();
            for (Product value : products) {
                if (value.getName().equalsIgnoreCase(product.getName())) {
                    found = true;
                    if (value.getQuantity() > 0) {
                        return j;
                    }
                }
            }
        }
        if (!found) {
            System.out.println("Product not found --> " + product.getName());
        }
        return -1;
    }

    private static int returnProductID(ClientBasket stock, Product product) {

        ArrayList<Product> products = stock.getProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equalsIgnoreCase(product.getName()) && products.get(i).getQuantity() > 0) {
                return i;
            }
        }
        return -1;
    }

}

