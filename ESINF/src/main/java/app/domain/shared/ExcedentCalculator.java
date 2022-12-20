package app.domain.shared;

import app.controller.App;
import app.domain.model.ClientBasket;
import app.domain.model.ClientsProducers;
import app.domain.model.Product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static app.domain.shared.ExpeditionListCreator.findProductOwner;
import static app.domain.shared.ExpeditionListCreator.returnProductID;

public class ExcedentCalculator {

    public static void CalculateExcedent(int actualDay) {
        Map<Integer, ArrayList<ClientBasket>> stock = App.getInstance().getCompany().getStock().getStock();
        Map<Integer, ArrayList<ClientBasket>> orders = App.getInstance().getCompany().getOrders().getOrders();

        Set<Integer> days = stock.keySet();
        Iterator<Integer> iterator = days.iterator();

        boolean older = false;
        boolean newer = false;

        for (int i = 0; i < days.size() - 1; i++) {

            int day = iterator.next();
            System.out.println(day);


            // For each day
            if (stock.containsKey(day)) {
                ArrayList<ClientBasket> clientBasketsOrders = orders.get(day);      // orders of day key
                ArrayList<ClientBasket> clientBasketsStock = stock.get(day);        // stock of day key
                ArrayList<ClientBasket> clientBasketsExcedentOlder = new ArrayList<>();
                ArrayList<ClientBasket> clientBasketsExcedentRecent = new ArrayList<>();

                // get excedent of the day before
                if (stock.containsKey(day - 1)) {
                    clientBasketsExcedentRecent = stock.get(day - 1);
                }

                newer = !clientBasketsExcedentRecent.isEmpty();

                // get excedent of 2 days before
                if (stock.containsKey(day - 2)) {
                    clientBasketsExcedentOlder = stock.get(day - 2);
                }

                older = !clientBasketsExcedentOlder.isEmpty();

                // for each client basket in orders
                for (ClientBasket clientBasketsOrder : clientBasketsOrders) {

                    ArrayList<Product> productsOrder = clientBasketsOrder.getProducts();       // products of one order

                    // for each product inside each client basket in order
                    // product of one order
                    for (Product productOrder : productsOrder) {

                        if (productOrder.getQuantity() > 0) {

                            int productOwner = findProductOwner(clientBasketsStock, productOrder);   // find product owner id

                            if (productOwner != -1) {

                                ClientsProducers producer = clientBasketsStock.get(productOwner).getEntity();     // get product owner
                                int productInStockID = returnProductID(clientBasketsStock.get(productOwner), productOrder); // return product id that is in stock

                                if (productInStockID != -1) {

                                    Product productInExcedentRecent = null;
                                    Product productInExcedentOlder = null;
                                    Product productInStock = clientBasketsStock.get(productOwner).getProducts().get(productInStockID); // product that is in stock

                                    if (older) {
                                        productInExcedentOlder = findProductExcedent(clientBasketsExcedentOlder, producer, productOrder, 0);
                                    }
                                    if (newer) {
                                        productInExcedentRecent = findProductExcedent(clientBasketsExcedentRecent, producer, productOrder, 0);
                                    }

                                    int rest = 0;

                                    if (productInStock.getQuantity() >= productOrder.getQuantity()) {
                                        clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - productOrder.getQuantity());    //atualizar stock
                                    } else {
                                        clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(0);    //atualizar stock
                                    }
                                }
                            }
                        }
                    }
                }

                stock.remove(day);
                stock.put(day, clientBasketsStock);

            }

        }

        App.getInstance().getCompany().updateExcedents(stock);      //adiciona excedentes

    }

    private static Product findProductExcedent(ArrayList<ClientBasket> stock, ClientsProducers cp, Product product, int idx) {

        for (ClientBasket clientBasket : stock) {
            if (clientBasket.getEntity().getCode().equalsIgnoreCase(cp.getCode())) {
                for (int j = 0; j < clientBasket.getProducts().size(); j++) {
                    if (clientBasket.getProducts().get(j).getName().equalsIgnoreCase(product.getName())) {
                        return clientBasket.getProducts().get(j);
                    }
                }
            }
        }

        return null;
    }
}
