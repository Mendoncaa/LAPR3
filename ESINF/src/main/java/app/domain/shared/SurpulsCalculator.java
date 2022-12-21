package app.domain.shared;

import app.controller.App;
import app.domain.model.ClientBasket;
import app.domain.model.ClientsProducers;
import app.domain.model.Product;

import java.util.*;

import static app.domain.shared.ExpeditionListCreator.findProductOwner;
import static app.domain.shared.ExpeditionListCreator.returnProductID;

public class SurpulsCalculator {

    public static void CalculateSurplus(int actualDay) {
        Map<Integer, ArrayList<ClientBasket>> stock = cloneMap(App.getInstance().getCompany().getStock().getStock());
        Map<Integer, ArrayList<ClientBasket>> orders = cloneMap(App.getInstance().getCompany().getOrders().getOrders());

        Set<Integer> days = getDays(stock);
        days.removeIf(day -> day >= actualDay);
        Iterator<Integer> iterator = days.iterator();

        CalculateSurplusDay(iterator, stock, orders);
        App.getInstance().getCompany().getExcedents().putAll(stock);
    }

    private static void CalculateSurplusDay(Iterator<Integer> daysItr, Map<Integer, ArrayList<ClientBasket>> stock, Map<Integer, ArrayList<ClientBasket>> orders) {

        boolean older;
        boolean newer;

        int day = daysItr.next();

        // For each day
        if (stock.containsKey(day)) {
            ArrayList<ClientBasket> clientBasketsOrders = orders.get(day);      // orders of day key
            ArrayList<ClientBasket> clientBasketsStock = stock.get(day);        // stock of day key
            ArrayList<ClientBasket> clientBasketsSurplusOlder = new ArrayList<>();
            ArrayList<ClientBasket> clientBasketsSurplusRecent = new ArrayList<>();

            // get surplus of the day before
            if (stock.containsKey(day - 1)) {
                clientBasketsSurplusRecent = stock.get(day - 1);
            }

            newer = !clientBasketsSurplusRecent.isEmpty();

            // get surplus of 2 days before
            if (stock.containsKey(day - 2)) {
                clientBasketsSurplusOlder = stock.get(day - 2);
            }

            older = !clientBasketsSurplusOlder.isEmpty();

            // for each client basket in orders
            for (ClientBasket clientBasketsOrder : clientBasketsOrders) {

                ArrayList<Product> productsOrder = clientBasketsOrder.getProducts();       // products of one order

                // for each product inside each client basket in order
                // product of one order
                for (Product productOrder : productsOrder) {

                    if (productOrder.getQuantity() > 0) {

                        int productOwner = findProductOwner(clientBasketsStock, productOrder);   // find product owner id
                        int productOwnerRecent = -1;
                        int productOwnerOlder = -1;

                        if (productOwner != -1) {

                            ClientsProducers producer = clientBasketsStock.get(productOwner).getEntity();     // get product owner

                            if (newer) {
                                productOwnerRecent = findProductSurplusOwnerID(clientBasketsSurplusRecent, producer, 0);
                            }
                            if (older) {
                                productOwnerOlder = findProductSurplusOwnerID(clientBasketsSurplusOlder, producer, 0);
                            }

                            int productInStockID = returnProductID(clientBasketsStock.get(productOwner), productOrder); // return product id that is in stock
                            int productInSurplusRecentID = -1;
                            int productInSurplusOlderID = -1;

                            if (older) {
                                productInSurplusOlderID = findProductSurplus(clientBasketsSurplusOlder, productOrder, productOwnerOlder, 0);
                            }
                            if (newer) {
                                productInSurplusRecentID = findProductSurplus(clientBasketsSurplusRecent, productOrder, productOwnerRecent, 0);
                            }

                            if (productInStockID != -1 || productInSurplusOlderID != -1 || productInSurplusRecentID != -1) {

                                Product productInStock = null;
                                Product productInSurplusRecent = null;
                                Product productInSurplusOlder = null;

                                if (productInStockID != -1) {
                                    productInStock = clientBasketsStock.get(productOwner).getProducts().get(productInStockID); // product that is in stock
                                }
                                if (productInSurplusOlderID != -1 && productOwnerOlder != -1) {
                                    productInSurplusOlder = clientBasketsSurplusOlder.get(productOwnerOlder).getProducts().get(productInSurplusOlderID);
                                }
                                if (productInSurplusRecentID != -1 && productOwnerRecent != -1) {
                                    productInSurplusRecent = clientBasketsSurplusRecent.get(productOwnerRecent).getProducts().get(productInSurplusRecentID);
                                }

                                float rest = productOrder.getQuantity();

                                if (productInSurplusOlder != null) {
                                    if (productInSurplusOlder.getQuantity() >= rest) {
                                        rest = 0;
                                        clientBasketsSurplusOlder.get(productOwnerOlder).getProducts().get(productInSurplusOlderID).setQuantity(productInSurplusOlder.getQuantity() - rest);    //atualizar stock
                                    } else {
                                        rest = rest - productInSurplusOlder.getQuantity();
                                        clientBasketsSurplusOlder.get(productOwnerOlder).getProducts().get(productInSurplusOlderID).setQuantity(0);    //atualizar stock
                                    }

                                    if (productInSurplusRecent != null && rest > 0) {
                                        if (productInSurplusRecent.getQuantity() > rest) {
                                            rest = 0;
                                            clientBasketsSurplusRecent.get(productOwnerRecent).getProducts().get(productInSurplusRecentID).setQuantity(productInSurplusRecent.getQuantity() - rest);    //atualizar stock
                                        } else {
                                            rest = rest - productInSurplusRecent.getQuantity();
                                            clientBasketsSurplusRecent.get(productOwnerRecent).getProducts().get(productInSurplusRecentID).setQuantity(0);    //atualizar stock
                                        }
                                    }

                                    if (productInStock != null && rest > 0) {
                                        if (productInStock.getQuantity() > rest) {
                                            rest = 0;
                                            clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - rest);    //atualizar stock
                                        } else {
                                            clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(0);    //atualizar stock
                                        }
                                    }

                                } else {
                                    if (productInSurplusRecent != null) {

                                        if (productInSurplusRecent.getQuantity() > rest) {
                                            rest = 0;
                                            clientBasketsSurplusRecent.get(productOwnerRecent).getProducts().get(productInSurplusRecentID).setQuantity(productInSurplusRecent.getQuantity() - rest);    //atualizar stock
                                        } else {
                                            rest = rest - productInSurplusRecent.getQuantity();
                                            clientBasketsSurplusRecent.get(productOwnerRecent).getProducts().get(productInSurplusRecentID).setQuantity(0);    //atualizar stock
                                        }

                                        if (productInStock != null && rest > 0) {
                                            if (productInStock.getQuantity() > rest) {
                                                rest = 0;
                                                clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - rest);    //atualizar stock
                                            } else {
                                                clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(0);    //atualizar stock
                                            }
                                        }

                                    } else {
                                        if (productInStock != null) {
                                            if (productInStock.getQuantity() > productOrder.getQuantity()) {
                                                clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - productOrder.getQuantity());    //atualizar stock
                                            } else {
                                                clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(0);    //atualizar stock
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


            stock.remove(day);
            stock.put(day, clientBasketsStock);

            if (day - 1 > 0) {
                stock.remove(day - 1);
                stock.put(day - 1, clientBasketsSurplusRecent);
            }

            if (day - 2 > 0) {
                stock.remove(day - 2);
                stock.put(day - 2, clientBasketsSurplusOlder);
            }

        }

        if (daysItr.hasNext()) {
            CalculateSurplusDay(daysItr, stock, orders);
        }

    }

    private static int findProductSurplusOwnerID(ArrayList<ClientBasket> stock, ClientsProducers cp, int idx) {
        if (idx >= stock.size()) return -1;

        if (stock.get(idx).getEntity().getCode().equalsIgnoreCase(cp.getCode())) {
            return idx;
        } else {
            return findProductSurplusOwnerID(stock, cp, idx + 1);
        }
    }

    private static int findProductSurplus(ArrayList<ClientBasket> stock, Product product, int cpId, int idx) {
        if (idx >= stock.get(cpId).getProducts().size()) return -1;

        if (stock.get(cpId).getProducts().get(idx).getName().equalsIgnoreCase(product.getName()) && stock.get(cpId).getProducts().get(idx).getQuantity() > 0) {
            return idx;
        }

        return findProductSurplus(stock, product, cpId, idx + 1);
    }

    private static Set<Integer> getDays(Map<Integer, ArrayList<ClientBasket>> stock) {
        return new HashSet<>(stock.keySet());
    }

    private static Map<Integer, ArrayList<ClientBasket>> cloneMap(Map<Integer, ArrayList<ClientBasket>> map) {
        return new TreeMap<>(map);
    }
}
