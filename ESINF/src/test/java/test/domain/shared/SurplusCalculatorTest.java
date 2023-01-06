package test.domain.shared;

import app.controller.App;
import app.domain.model.ClientBasket;
import app.domain.model.ClientsProducers;
import app.domain.model.Product;
import app.domain.shared.SurplusCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SurplusCalculatorTest {

    @Test
    public void isThisHub() {
        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "C1");
        ClientsProducers c2 = new ClientsProducers("CT2", 40.6389f, -8.6553f, "C2");
        App.getInstance().getCompany().getHubStore().addHub(c1);
        Assertions.assertTrue(isThisHub(c1,0));
        Assertions.assertFalse(isThisHub(c2,0));
    }

    @Test
    public void findHubID(){

        Map<Integer,ArrayList<ClientBasket>> stock = new TreeMap<>();
        ArrayList<ClientBasket> baskets = new ArrayList<>();
        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "P1");
        ClientsProducers c2 = new ClientsProducers("CT2", 38.0333f, -7.8833f, "P2");
        ClientsProducers p1 = new ClientsProducers("PT1", 40.6389f, -8.6553f, "P3");

        ClientBasket basket = new ClientBasket(c1, new ArrayList<>());
        basket.getProducts().add(new Product("Prod1", 0));
        basket.getProducts().add(new Product("Prod2", 1));

        ClientBasket basket2 = new ClientBasket(c2, new ArrayList<>());
        basket2.getProducts().add(new Product("Prod1", 1));
        basket2.getProducts().add(new Product("Prod2", 1));

        baskets.add(basket);
        baskets.add(basket2);

        stock.put(1,baskets);

        Assertions.assertEquals(0,findHubID(stock,c1,1,0));
        Assertions.assertEquals(1,findHubID(stock,c2,1,0));
        Assertions.assertEquals(-1,findHubID(stock,p1,1,0));

    }

    @Test
    public void findProductID(){
        Map<Integer,ArrayList<ClientBasket>> stock = new TreeMap<>();
        ArrayList<ClientBasket> baskets = new ArrayList<>();
        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "P1");
        ClientsProducers c2 = new ClientsProducers("CT2", 38.0333f, -7.8833f, "P2");
        ClientsProducers p1 = new ClientsProducers("PT1", 40.6389f, -8.6553f, "P3");

        ClientBasket basket = new ClientBasket(c1, new ArrayList<>());
        basket.getProducts().add(new Product("Prod1", 0));
        basket.getProducts().add(new Product("Prod2", 1));

        ClientBasket basket2 = new ClientBasket(c2, new ArrayList<>());
        basket2.getProducts().add(new Product("Prod1", 1));
        basket2.getProducts().add(new Product("Prod2", 1));

        baskets.add(basket);
        baskets.add(basket2);

        stock.put(1,baskets);

        Assertions.assertEquals(0,findProductID(stock,new Product("Prod1",0),1,0,0));
        Assertions.assertEquals(1,findProductID(stock,new Product("Prod2",1),1,1,0));
        Assertions.assertEquals(-1,findProductID(stock,new Product("Prod3",1),1,1,0));
    }






    private static boolean isThisHub(ClientsProducers entity, int idx) {
        if (idx >= App.getInstance().getCompany().getHubStore().getHubs().size()) return false;

        if (App.getInstance().getCompany().getHubStore().getHub(idx).equals(entity)) {
            return true;
        }
        return isThisHub(entity, idx + 1);
    }


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
