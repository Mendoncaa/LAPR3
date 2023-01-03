package app.domain.shared;

import app.controller.App;
import app.domain.model.*;

import java.util.ArrayList;


public class ExpeditionListCreator {

    public static ArrayList<ExpeditionList> getExpeditionListNoRestrictions(int day) {

        ArrayList<ExpeditionList> expeditionListSet = new ArrayList<>();            // expedition list of day key

        // For each day
        if (App.getInstance().getCompany().getStock().getStockNoRest().containsKey(day) && App.getInstance().getCompany().getOrders().getOrders().containsKey(day)) {
            ArrayList<ClientBasket> clientBasketsOrders = new ArrayList<>(App.getInstance().getCompany().getOrders().getOrders().get(day));     // orders of day key
            ArrayList<ClientBasket> clientBasketsStock = new ArrayList<>(App.getInstance().getCompany().getStock().getStockNoRest().get(day));       // stock of day key

            ArrayList<ClientBasket> clientBasketsSurplusOlder = new ArrayList<>();                                              // surplus of day key - 1
            ArrayList<ClientBasket> clientBasketsSurplusRecent = new ArrayList<>();                                             // surplus of day key - 2

            if (App.getInstance().getCompany().getExcedents().containsKey(day - 2)) {
                clientBasketsSurplusOlder = App.getInstance().getCompany().getExcedents().get(day - 2);
            }

            if (App.getInstance().getCompany().getExcedents().containsKey(day - 1)) {
                clientBasketsSurplusRecent = App.getInstance().getCompany().getExcedents().get(day - 1);
            }

            boolean newer = !clientBasketsSurplusRecent.isEmpty();
            boolean older = !clientBasketsSurplusOlder.isEmpty();

            // for each client basket in orders
            for (ClientBasket clientBasketsOrder : clientBasketsOrders) {

                ArrayList<Product> productsOrder = clientBasketsOrder.getProducts();       // products of one order
                ExpeditionList expeditionList = new ExpeditionList(clientBasketsOrder.getEntity());     // expedition list of one order

                // for each product inside each client basket in order
                // product of one order
                for (Product productOrder : productsOrder) {

                    if (productOrder.getQuantity() > 0) {
                        ClientsProducers producer;
                        producer = thereIsProduct(clientBasketsStock, clientBasketsSurplusOlder, clientBasketsSurplusRecent, productOrder, clientBasketsOrder.getEntity());   // find product owner id
                        int productOwnerID;
                        int productOwnerSurplusOlderID = -1;
                        int productOwnerSurplusRecentID = -1;

                        if (producer != null) {

                            productOwnerID = findProductOwnerID(clientBasketsStock, producer, 0);

                            if (newer) {
                                productOwnerSurplusRecentID = findProductOwnerID(clientBasketsSurplusRecent, producer, 0);
                            }

                            if (older) {
                                productOwnerSurplusOlderID = findProductOwnerID(clientBasketsSurplusOlder, producer, 0);
                            }

                            int productInStockID = -1;
                            int productInSurplusRecentID = -1;
                            int productInSurplusOlderID = -1;

                            if (productOwnerID != -1) {
                                productInStockID = returnProductID(clientBasketsStock.get(productOwnerID), productOrder); // return product id that is in stock
                            }

                            if (older) {
                                productInSurplusOlderID = findProductSurplus(clientBasketsSurplusOlder, productOrder, productOwnerSurplusOlderID, 0);
                            }
                            if (newer) {
                                productInSurplusRecentID = findProductSurplus(clientBasketsSurplusRecent, productOrder, productOwnerSurplusRecentID, 0);
                            }

                            if (productInStockID != -1 || productInSurplusOlderID != -1 || productInSurplusRecentID != -1) {

                                Product productInSurplusRecent = null;
                                Product productInSurplusOlder = null;
                                Product productInStock = null;

                                if (productInStockID != -1) {
                                    productInStock = clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID);
                                }

                                if (productInSurplusOlderID != -1 && productOwnerSurplusOlderID != -1) {
                                    productInSurplusOlder = clientBasketsSurplusOlder.get(productOwnerSurplusOlderID).getProducts().get(productInSurplusOlderID);
                                }
                                if (productInSurplusRecentID != -1 && productOwnerSurplusRecentID != -1) {
                                    productInSurplusRecent = clientBasketsSurplusRecent.get(productOwnerSurplusRecentID).getProducts().get(productInSurplusRecentID);
                                }

                                float quantity_ordered = productOrder.getQuantity();
                                float rest = productOrder.getQuantity();
                                float quantity_delivered = 0;

                                if (productInSurplusOlder != null) {

                                    if (productInSurplusOlder.getQuantity() >= rest) {
                                        quantity_delivered = quantity_delivered + productOrder.getQuantity();
                                        rest = 0;
                                        clientBasketsSurplusOlder.get(productOwnerSurplusOlderID).getProducts().get(productInSurplusOlderID).setQuantity(productInSurplusOlder.getQuantity() - productOrder.getQuantity());
                                    } else {
                                        quantity_delivered = quantity_delivered + productInSurplusOlder.getQuantity();
                                        rest = rest - productInSurplusOlder.getQuantity();
                                        clientBasketsSurplusOlder.get(productOwnerSurplusOlderID).getProducts().get(productInSurplusOlderID).setQuantity(0);    //atualizar stock
                                    }

                                    if (productInSurplusRecent != null && rest > 0) {
                                        if (productInSurplusRecent.getQuantity() > rest) {
                                            quantity_delivered = quantity_delivered + rest;
                                            rest = 0;
                                            clientBasketsSurplusRecent.get(productOwnerSurplusRecentID).getProducts().get(productInSurplusRecentID).setQuantity(productInSurplusRecent.getQuantity() - rest);
                                        } else {
                                            quantity_delivered = quantity_delivered + productInSurplusRecent.getQuantity();
                                            rest = rest - productInSurplusRecent.getQuantity();
                                            clientBasketsSurplusRecent.get(productOwnerSurplusRecentID).getProducts().get(productInSurplusRecentID).setQuantity(0);
                                        }
                                    }

                                    if (productInStock != null && rest > 0) {
                                        if (productInStock.getQuantity() > rest) {
                                            quantity_delivered = quantity_delivered + rest;
                                            rest = 0;
                                            clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - rest);
                                        } else {
                                            quantity_delivered = quantity_delivered + productInStock.getQuantity();
                                            clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID).setQuantity(0);
                                        }
                                    }

                                    expeditionList.getBasketElements().add(new BasketElement(new Product(productOrder.getName(), quantity_delivered), clientBasketsStock.get(productOwnerID).getEntity()));          // add product to expedition list
                                    expeditionList.getBasketOrderedElements().add(new BasketElement(new Product(productOrder.getName(), quantity_ordered), clientBasketsStock.get(productOwnerID).getEntity()));

                                } else {
                                    if (productInSurplusRecent != null) {

                                        if (productInSurplusRecent.getQuantity() > rest) {
                                            quantity_delivered = quantity_delivered + rest;
                                            rest = 0;
                                            clientBasketsSurplusRecent.get(productOwnerSurplusRecentID).getProducts().get(productInSurplusRecentID).setQuantity(productInSurplusRecent.getQuantity() - rest);    //atualizar stock
                                        } else {
                                            quantity_delivered = quantity_delivered + productInSurplusRecent.getQuantity();
                                            rest = rest - productInSurplusRecent.getQuantity();
                                            clientBasketsSurplusRecent.get(productOwnerSurplusRecentID).getProducts().get(productInSurplusRecentID).setQuantity(0);    //atualizar stock
                                        }

                                        if (productInStock != null && rest > 0) {
                                            if (productInStock.getQuantity() > rest) {
                                                quantity_delivered = quantity_delivered + rest;
                                                rest = 0;
                                                clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - rest);    //atualizar stock
                                            } else {
                                                quantity_delivered = quantity_delivered + productInStock.getQuantity();
                                                clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID).setQuantity(0);    //atualizar stock
                                            }
                                        }

                                        expeditionList.getBasketElements().add(new BasketElement(new Product(productOrder.getName(), quantity_delivered), clientBasketsStock.get(productOwnerID).getEntity()));          // add product to expedition list
                                        expeditionList.getBasketOrderedElements().add(new BasketElement(new Product(productOrder.getName(), quantity_ordered), clientBasketsStock.get(productOwnerID).getEntity()));

                                    } else {
                                        if (productInStock != null) {
                                            if (productInStock.getQuantity() > productOrder.getQuantity()) {
                                                quantity_delivered = quantity_delivered + productOrder.getQuantity();
                                                clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - productOrder.getQuantity());    //atualizar stock
                                            } else {
                                                quantity_delivered = quantity_delivered + productInStock.getQuantity();
                                                clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID).setQuantity(0);    //atualizar stock
                                            }

                                            expeditionList.getBasketElements().add(new BasketElement(new Product(productOrder.getName(), quantity_delivered), clientBasketsStock.get(productOwnerID).getEntity()));          // add product to expedition list
                                            expeditionList.getBasketOrderedElements().add(new BasketElement(new Product(productOrder.getName(), quantity_ordered), clientBasketsStock.get(productOwnerID).getEntity()));

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (App.getInstance().getCompany().getStatistics().getStatisticsForBasket().containsKey(day)) {
                    App.getInstance().getCompany().getStatistics().getStatisticsForBasket().get(day).add(expeditionList);
                } else {
                    App.getInstance().getCompany().getStatistics().getStatisticsForBasket().put(day, new ArrayList<>());
                    App.getInstance().getCompany().getStatistics().getStatisticsForBasket().get(day).add(expeditionList);
                }

                expeditionListSet.add(expeditionList);

            }
        }
        return expeditionListSet;
    }

    public static ArrayList<ExpeditionList> getExpeditionListNClosest(int day, int n) {

        ArrayList<ExpeditionList> expeditionListSet = new ArrayList<>();            // expedition list of day key

        if (!App.getInstance().getCompany().getExpeditionListStore().existDay(day)) {

            // For each day
            if (App.getInstance().getCompany().getStock().getStockNoRest().containsKey(day) && App.getInstance().getCompany().getOrders().getOrders().containsKey(day)) {
                ArrayList<ClientBasket> clientBasketsOrders = new ArrayList<>(App.getInstance().getCompany().getOrders().getOrders().get(day));     // orders of day key
                ArrayList<ClientBasket> clientBasketsStock = new ArrayList<>(App.getInstance().getCompany().getStock().getStockNoRest().get(day));       // stock of day key

                ArrayList<ClientBasket> clientBasketsSurplusOlder = new ArrayList<>();                                              // surplus of day key - 1
                ArrayList<ClientBasket> clientBasketsSurplusRecent = new ArrayList<>();                                             // surplus of day key - 2

                if (App.getInstance().getCompany().getExcedents().containsKey(day - 2)) {
                    clientBasketsSurplusOlder = App.getInstance().getCompany().getExcedents().get(day - 2);
                }

                if (App.getInstance().getCompany().getExcedents().containsKey(day - 1)) {
                    clientBasketsSurplusRecent = App.getInstance().getCompany().getExcedents().get(day - 1);
                }

                boolean newer = !clientBasketsSurplusRecent.isEmpty();
                boolean older = !clientBasketsSurplusOlder.isEmpty();

                // for each client basket in orders
                for (ClientBasket clientBasketsOrder : clientBasketsOrders) {

                    ArrayList<Product> productsOrder = clientBasketsOrder.getProducts();       // products of one order
                    ExpeditionList expeditionList = new ExpeditionList(clientBasketsOrder.getEntity());     // expedition list of one order

                    // for each product inside each client basket in order
                    // product of one order
                    for (Product productOrder : productsOrder) {

                        ArrayList<ClientsProducers> producersFilteredClone = new ArrayList<>();


                        if (productOrder.getQuantity() > 0) {
                            ClientsProducers producer;
                            producer = thereIsProduct(clientBasketsStock, clientBasketsSurplusOlder, clientBasketsSurplusRecent, productOrder, clientBasketsOrder.getEntity());   // find product owner id
                            int productOwnerID;
                            int productOwnerSurplusOlderID = -1;
                            int productOwnerSurplusRecentID = -1;

                            if (producer != null) {

                                productOwnerID = findProductOwnerID(clientBasketsStock, producer, 0);

                                if (newer) {
                                    productOwnerSurplusRecentID = findProductOwnerID(clientBasketsSurplusRecent, producer, 0);
                                }

                                if (older) {
                                    productOwnerSurplusOlderID = findProductOwnerID(clientBasketsSurplusOlder, producer, 0);
                                }

                                int productInStockID = -1;
                                int productInSurplusRecentID = -1;
                                int productInSurplusOlderID = -1;

                                if (productOwnerID != -1) {
                                    productInStockID = returnProductID(clientBasketsStock.get(productOwnerID), productOrder); // return product id that is in stock
                                }

                                if (older) {
                                    productInSurplusOlderID = findProductSurplus(clientBasketsSurplusOlder, productOrder, productOwnerSurplusOlderID, 0);
                                }
                                if (newer) {
                                    productInSurplusRecentID = findProductSurplus(clientBasketsSurplusRecent, productOrder, productOwnerSurplusRecentID, 0);
                                }

                                if (productInStockID != -1 || productInSurplusOlderID != -1 || productInSurplusRecentID != -1) {

                                    Product productInSurplusRecent = null;
                                    Product productInSurplusOlder = null;
                                    Product productInStock = null;

                                    if (productInStockID != -1) {
                                        productInStock = clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID);
                                    }

                                    if (productInSurplusOlderID != -1 && productOwnerSurplusOlderID != -1) {
                                        productInSurplusOlder = clientBasketsSurplusOlder.get(productOwnerSurplusOlderID).getProducts().get(productInSurplusOlderID);
                                    }
                                    if (productInSurplusRecentID != -1 && productOwnerSurplusRecentID != -1) {
                                        productInSurplusRecent = clientBasketsSurplusRecent.get(productOwnerSurplusRecentID).getProducts().get(productInSurplusRecentID);
                                    }

                                    float quantity_ordered = productOrder.getQuantity();
                                    float rest = productOrder.getQuantity();
                                    float quantity_delivered = 0;

                                    if (productInSurplusOlder != null) {

                                        if (productInSurplusOlder.getQuantity() >= rest) {
                                            quantity_delivered = quantity_delivered + productOrder.getQuantity();
                                            rest = 0;
                                            clientBasketsSurplusOlder.get(productOwnerSurplusOlderID).getProducts().get(productInSurplusOlderID).setQuantity(productInSurplusOlder.getQuantity() - productOrder.getQuantity());
                                        } else {
                                            quantity_delivered = quantity_delivered + productInSurplusOlder.getQuantity();
                                            rest = rest - productInSurplusOlder.getQuantity();
                                            clientBasketsSurplusOlder.get(productOwnerSurplusOlderID).getProducts().get(productInSurplusOlderID).setQuantity(0);    //atualizar stock
                                        }

                                        if (productInSurplusRecent != null && rest > 0) {
                                            if (productInSurplusRecent.getQuantity() > rest) {
                                                quantity_delivered = quantity_delivered + rest;
                                                rest = 0;
                                                clientBasketsSurplusRecent.get(productOwnerSurplusRecentID).getProducts().get(productInSurplusRecentID).setQuantity(productInSurplusRecent.getQuantity() - rest);
                                            } else {
                                                quantity_delivered = quantity_delivered + productInSurplusRecent.getQuantity();
                                                rest = rest - productInSurplusRecent.getQuantity();
                                                clientBasketsSurplusRecent.get(productOwnerSurplusRecentID).getProducts().get(productInSurplusRecentID).setQuantity(0);
                                            }
                                        }

                                        if (productInStock != null && rest > 0) {
                                            if (productInStock.getQuantity() > rest) {
                                                quantity_delivered = quantity_delivered + rest;
                                                rest = 0;
                                                clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - rest);
                                            } else {
                                                quantity_delivered = quantity_delivered + productInStock.getQuantity();
                                                clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID).setQuantity(0);
                                            }
                                        }

                                        expeditionList.getBasketElements().add(new BasketElement(new Product(productOrder.getName(), quantity_delivered), clientBasketsStock.get(productOwnerID).getEntity()));          // add product to expedition list
                                        expeditionList.getBasketOrderedElements().add(new BasketElement(new Product(productOrder.getName(), quantity_ordered), clientBasketsStock.get(productOwnerID).getEntity()));

                                    } else {
                                        if (productInSurplusRecent != null) {

                                            if (productInSurplusRecent.getQuantity() > rest) {
                                                quantity_delivered = quantity_delivered + rest;
                                                rest = 0;
                                                clientBasketsSurplusRecent.get(productOwnerSurplusRecentID).getProducts().get(productInSurplusRecentID).setQuantity(productInSurplusRecent.getQuantity() - rest);    //atualizar stock
                                            } else {
                                                quantity_delivered = quantity_delivered + productInSurplusRecent.getQuantity();
                                                rest = rest - productInSurplusRecent.getQuantity();
                                                clientBasketsSurplusRecent.get(productOwnerSurplusRecentID).getProducts().get(productInSurplusRecentID).setQuantity(0);    //atualizar stock
                                            }

                                            if (productInStock != null && rest > 0) {
                                                if (productInStock.getQuantity() > rest) {
                                                    quantity_delivered = quantity_delivered + rest;
                                                    rest = 0;
                                                    clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - rest);    //atualizar stock
                                                } else {
                                                    quantity_delivered = quantity_delivered + productInStock.getQuantity();
                                                    clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID).setQuantity(0);    //atualizar stock
                                                }
                                            }

                                            expeditionList.getBasketElements().add(new BasketElement(new Product(productOrder.getName(), quantity_delivered), clientBasketsStock.get(productOwnerID).getEntity()));          // add product to expedition list
                                            expeditionList.getBasketOrderedElements().add(new BasketElement(new Product(productOrder.getName(), quantity_ordered), clientBasketsStock.get(productOwnerID).getEntity()));

                                        } else {
                                            if (productInStock != null) {
                                                if (productInStock.getQuantity() > productOrder.getQuantity()) {
                                                    quantity_delivered = quantity_delivered + productOrder.getQuantity();
                                                    clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID).setQuantity(productInStock.getQuantity() - productOrder.getQuantity());    //atualizar stock
                                                } else {
                                                    quantity_delivered = quantity_delivered + productInStock.getQuantity();
                                                    clientBasketsStock.get(productOwnerID).getProducts().get(productInStockID).setQuantity(0);    //atualizar stock
                                                }

                                                expeditionList.getBasketElements().add(new BasketElement(new Product(productOrder.getName(), quantity_delivered), clientBasketsStock.get(productOwnerID).getEntity()));          // add product to expedition list
                                                expeditionList.getBasketOrderedElements().add(new BasketElement(new Product(productOrder.getName(), quantity_ordered), clientBasketsStock.get(productOwnerID).getEntity()));

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (App.getInstance().getCompany().getStatistics().getStatisticsForBasket().containsKey(day)) {
                        App.getInstance().getCompany().getStatistics().getStatisticsForBasket().get(day).add(expeditionList);
                    } else {
                        App.getInstance().getCompany().getStatistics().getStatisticsForBasket().put(day, new ArrayList<>());
                        App.getInstance().getCompany().getStatistics().getStatisticsForBasket().get(day).add(expeditionList);
                    }

                    expeditionListSet.add(expeditionList);

                }
            }
        } else {
            expeditionListSet = App.getInstance().getCompany().getExpeditionListStore().getExpeditionListNoRestByDay(day);
        }

        return expeditionListSet;
    }

    public static ClientsProducers thereIsProduct(ArrayList<ClientBasket> stock, ArrayList<ClientBasket> older, ArrayList<ClientBasket> recent, Product product, ClientsProducers co) {

        for (ClientBasket clientBasket : older) {
            ClientsProducers cp = clientBasket.getEntity();
            ArrayList<Product> products = clientBasket.getProducts();
            for (Product value : products) {
                if (value.getName().equalsIgnoreCase(product.getName())) {
                    if (value.getQuantity() > 0 && !cp.getCode().equalsIgnoreCase(co.getCode())) {
                        return cp;
                    }
                }
            }
        }

        for (ClientBasket clientBasket : recent) {
            ClientsProducers cp = clientBasket.getEntity();
            ArrayList<Product> products = clientBasket.getProducts();
            for (Product value : products) {
                if (value.getName().equalsIgnoreCase(product.getName())) {
                    if (value.getQuantity() > 0 && !cp.getCode().equalsIgnoreCase(co.getCode())) {
                        return cp;
                    }
                }
            }
        }

        for (ClientBasket clientBasket : stock) {
            ClientsProducers cp = clientBasket.getEntity();
            ArrayList<Product> products = clientBasket.getProducts();
            for (Product value : products) {
                if (value.getName().equalsIgnoreCase(product.getName())) {
                    if (value.getQuantity() > 0 && !cp.getCode().equalsIgnoreCase(co.getCode())) {
                        return cp;
                    }
                }
            }
        }

        return null;
    }

    public static int returnProductID(ClientBasket stock, Product product) {

        ArrayList<Product> products = stock.getProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equalsIgnoreCase(product.getName()) && products.get(i).getQuantity() > 0) {
                return i;
            }
        }
        return -1;
    }

    public static int findProductOwnerID(ArrayList<ClientBasket> stock, ClientsProducers cp, int idx) {
        if (idx >= stock.size()) return -1;

        if (stock.get(idx).getEntity().getCode().equalsIgnoreCase(cp.getCode())) {
            return idx;
        } else {
            return findProductOwnerID(stock, cp, idx + 1);
        }
    }

    private static int findProductSurplus(ArrayList<ClientBasket> stock, Product product, int cpId, int idx) {
        if (idx >= stock.get(cpId).getProducts().size()) return -1;

        if (stock.get(cpId).getProducts().get(idx).getName().equalsIgnoreCase(product.getName()) && stock.get(cpId).getProducts().get(idx).getQuantity() > 0) {
            return idx;
        }

        return findProductSurplus(stock, product, cpId, idx + 1);
    }

}

