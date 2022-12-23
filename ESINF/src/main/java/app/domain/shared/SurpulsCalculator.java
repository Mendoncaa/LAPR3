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

        if (!stock.isEmpty()) {
            Set<Integer> days = getDays(stock);
            days.removeIf(day -> day >= actualDay);
            Iterator<Integer> iterator = days.iterator();

            CalculateSurplusDay(iterator, stock, orders);
            App.getInstance().getCompany().getExcedents().putAll(stock);
        }
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
                clientBasketsSurplusRecent = new ArrayList<>(stock.get(day - 1));
            }

            newer = !clientBasketsSurplusRecent.isEmpty();

            // get surplus of 2 days before
            if (stock.containsKey(day - 2)) {
                clientBasketsSurplusOlder = new ArrayList<>(stock.get(day - 2));
            }

            older = !clientBasketsSurplusOlder.isEmpty();

            // for each client basket in orders
            for (ClientBasket clientBasketsOrder : clientBasketsOrders) {

                ArrayList<Product> productsOrder = new ArrayList<>(clientBasketsOrder.getProducts());       // products of one order
                boolean isHub = isThisHub(clientBasketsOrder.getEntity(), 0);
                int hubID = -1;
                if (isHub) {
                    hubID = findHubID(stock, clientBasketsOrder.getEntity(), day + 1, 0);
                }

                // for each product inside each client basket in order
                // product of one order
                for (Product productOrder : productsOrder) {
                    int productID = -1;

                    if (productOrder.getQuantity() > 0) {

                        if (isHub) {
                            productID = findProductID(stock, productOrder, day + 1, hubID, 0);
                        }

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
                                float quantity = 0;

                                if (productInSurplusOlder != null) {
                                    if (productInSurplusOlder.getQuantity() >= rest) {
                                        quantity = quantity + rest;
                                        rest = 0;
                                        clientBasketsSurplusOlder.get(productOwnerOlder).getProducts().get(productInSurplusOlderID).setQuantity(productInSurplusOlder.getQuantity() - rest);    //atualizar stock
                                    } else {
                                        quantity = quantity + productInSurplusOlder.getQuantity();
                                        rest = rest - productInSurplusOlder.getQuantity();
                                        clientBasketsSurplusOlder.get(productOwnerOlder).getProducts().get(productInSurplusOlderID).setQuantity(0);    //atualizar stock
                                    }

                                    if (productInSurplusRecent != null && rest > 0) {
                                        if (productInSurplusRecent.getQuantity() > rest) {
                                            quantity = quantity + rest;
                                            rest = 0;
                                            clientBasketsSurplusRecent.get(productOwnerRecent).getProducts().get(productInSurplusRecentID).setQuantity(productInSurplusRecent.getQuantity() - rest);    //atualizar stock
                                        } else {
                                            quantity = quantity + productInSurplusRecent.getQuantity();
                                            rest = rest - productInSurplusRecent.getQuantity();
                                            clientBasketsSurplusRecent.get(productOwnerRecent).getProducts().get(productInSurplusRecentID).setQuantity(0);    //atualizar stock
                                        }
                                    }

                                    if (productInStock != null && rest > 0) {
                                        if (productInStock.getQuantity() > rest) {
                                            quantity = quantity + rest;
                                            rest = 0;
                                            clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - rest);    //atualizar stock
                                        } else {
                                            quantity = quantity + productInStock.getQuantity();
                                            clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(0);    //atualizar stock
                                        }
                                    }

                                } else {
                                    if (productInSurplusRecent != null) {

                                        if (productInSurplusRecent.getQuantity() > rest) {
                                            quantity = quantity + rest;
                                            rest = 0;
                                            clientBasketsSurplusRecent.get(productOwnerRecent).getProducts().get(productInSurplusRecentID).setQuantity(productInSurplusRecent.getQuantity() - rest);    //atualizar stock
                                        } else {
                                            quantity = quantity + productInSurplusRecent.getQuantity();
                                            rest = rest - productInSurplusRecent.getQuantity();
                                            clientBasketsSurplusRecent.get(productOwnerRecent).getProducts().get(productInSurplusRecentID).setQuantity(0);    //atualizar stock
                                        }

                                        if (productInStock != null && rest > 0) {
                                            if (productInStock.getQuantity() > rest) {
                                                quantity = quantity + rest;
                                                rest = 0;
                                                clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - rest);    //atualizar stock
                                            } else {
                                                quantity = quantity + productInStock.getQuantity();
                                                clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(0);    //atualizar stock
                                            }
                                        }

                                    } else {
                                        if (productInStock != null) {
                                            if (productInStock.getQuantity() > productOrder.getQuantity()) {
                                                quantity = quantity + productOrder.getQuantity();
                                                clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - productOrder.getQuantity());    //atualizar stock
                                            } else {
                                                quantity = quantity + productInStock.getQuantity();
                                                clientBasketsStock.get(productOwner).getProducts().get(productInStockID).setQuantity(0);    //atualizar stock
                                            }
                                        }
                                    }
                                }
                                if (isHub) {
                                    stock.get(day + 1).get(hubID).getProducts().get(productID).setQuantity(quantity);
                                }
                            }
                        }
                    }
                }
            }

            //update stock

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

    private static boolean isThisHub(ClientsProducers entity, int idx) {
        if (idx >= App.getInstance().getCompany().getHubStore().getHubs().size()) return false;

        if (App.getInstance().getCompany().getHubStore().getHub(idx).equals(entity)) {
            return true;
        }
        return isThisHub(entity, idx + 1);
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
/*
    private static void findHubIndex(Map<Integer, ArrayList<ClientBasket>> stock, ClientsProducers hub, Product product, int day, int hubID, int productID) {
        hubID = findHubID(stock, hub, day, 0);
        productID = findProductID(stock, product, day, hubID, 0);
    }

 */

    private static int findHubID(Map<Integer, ArrayList<ClientBasket>> stock, ClientsProducers hub, int day, int idx) {
        if (idx >= stock.get(day).size()) return -1;

        if (stock.get(day).get(idx).getEntity().equals(hub)) return idx;

        return findHubID(stock, hub, day, idx + 1);
    }

    private static int findProductID(Map<Integer, ArrayList<ClientBasket>> stock, Product product, int day, int hubID, int idx) {
        if (idx >= stock.get(day).get(hubID).getProducts().size()) return -1;

        if (stock.get(day).get(hubID).getProducts().get(idx).getName().equalsIgnoreCase(product.getName())) return idx;

        return findProductID(stock, product, day, hubID, idx + 1);
    }


}

