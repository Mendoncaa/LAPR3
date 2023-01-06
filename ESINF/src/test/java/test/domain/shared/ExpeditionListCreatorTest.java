package test.domain.shared;

import app.domain.model.ClientBasket;
import app.domain.model.ClientsProducers;
import app.domain.model.ExpeditionList;
import app.domain.model.Product;
import app.domain.shared.ExpeditionListCreator;
import app.graph.map.MapGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpeditionListCreatorTest {

    MapGraph<ClientsProducers, Integer> testMap = new MapGraph<>(false);
    Map<Integer, ArrayList<ClientBasket>> stockExpected = new TreeMap<>();
    Map<Integer, ArrayList<ClientBasket>> ordersExpected = new TreeMap<>();


    @BeforeEach
    public void setUp() {

        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "C1");
        ClientsProducers c2 = new ClientsProducers("CT2", 38.0333f, -7.8833f, "C2");
        ClientsProducers c3 = new ClientsProducers("CT3", 41.5333f, -8.4167f, "C3");
        ClientsProducers c4 = new ClientsProducers("CT15", 41.7f, -8.8333f, "C4");
        ClientsProducers c5 = new ClientsProducers("CT16", 41.3002f, -7.7398f, "C5");
        ClientsProducers c6 = new ClientsProducers("CT12", 41.1495f, -8.6108f, "C6");
        ClientsProducers c7 = new ClientsProducers("CT7", 38.5667f, -7.9f, "C7");
        ClientsProducers c8 = new ClientsProducers("CT8", 37.0161f, -7.935f, "C8");
        ClientsProducers c9 = new ClientsProducers("CT13", 39.2369f, -8.685f, "C9");
        ClientsProducers e1 = new ClientsProducers("CT14", 38.5243f, -8.8926f, "E1");
        ClientsProducers e2 = new ClientsProducers("CT11", 39.3167f, -7.4167f, "E2");
        ClientsProducers e3 = new ClientsProducers("CT5", 39.823f, -7.4931f, "E3");
        ClientsProducers e4 = new ClientsProducers("CT9", 40.5364f, -7.2683f, "E4");
        ClientsProducers e5 = new ClientsProducers("CT4", 41.8f, -6.75f, "E5");
        ClientsProducers p1 = new ClientsProducers("CT17", 40.6667f, -7.9167f, "P1");
        ClientsProducers p2 = new ClientsProducers("CT6", 40.2111f, -8.4291f, "P2");
        ClientsProducers p3 = new ClientsProducers("CT10", 39.7444f, -8.8072f, "P3");

        testMap.addVertex(c1);
        testMap.addVertex(c2);
        testMap.addVertex(c3);
        testMap.addVertex(c4);
        testMap.addVertex(c5);
        testMap.addVertex(c6);
        testMap.addVertex(c7);
        testMap.addVertex(c8);
        testMap.addVertex(c9);

        testMap.addVertex(e1);
        testMap.addVertex(e2);
        testMap.addVertex(e3);
        testMap.addVertex(e4);
        testMap.addVertex(e5);

        testMap.addVertex(p1);
        testMap.addVertex(p2);
        testMap.addVertex(p3);

        testMap.addEdge(p3, c9, 63448);
        testMap.addEdge(p3, p2, 67584);
        testMap.addEdge(p3, c1, 110848);
        testMap.addEdge(p3, e3, 125041);
        testMap.addEdge(c6, c3, 50467);
        testMap.addEdge(c6, c1, 62877);
        testMap.addEdge(c6, c4, 70717);
        testMap.addEdge(e2, e3, 62655);
        testMap.addEdge(e2, c9, 121584);
        testMap.addEdge(e2, p3, 142470);
        testMap.addEdge(e1, c9, 89813);
        testMap.addEdge(e1, c7, 95957);
        testMap.addEdge(e1, c2, 114913);
        testMap.addEdge(e1, c8, 207558);
        testMap.addEdge(c9, c7, 111686);
        testMap.addEdge(c5, c3, 68957);
        testMap.addEdge(c5, p1, 79560);
        testMap.addEdge(c5, c6, 82996);
        testMap.addEdge(c5, e4, 103704);
        testMap.addEdge(c5, e5, 110133);
        testMap.addEdge(c4, c3, 43598);
        testMap.addEdge(p1, e4, 62879);
        testMap.addEdge(p1, c1, 69282);
        testMap.addEdge(p1, p2, 73828);
        testMap.addEdge(c1, p2, 56717);
        testMap.addEdge(c2, c7, 65574);
        testMap.addEdge(c2, c8, 125105);
        testMap.addEdge(c2, e2, 163996);
        testMap.addEdge(e5, c3, 157223);
        testMap.addEdge(e5, e4, 90186);
        testMap.addEdge(e3, e4, 162527);
        testMap.addEdge(e3, p2, 100563);
        testMap.addEdge(e3, p1, 111134);

        ArrayList<Product> productsC1 = new ArrayList<>();
        ArrayList<Product> productsC2 = new ArrayList<>();
        ArrayList<Product> productsP1 = new ArrayList<>();
        ArrayList<Product> productsP2 = new ArrayList<>();
        ArrayList<Product> products2C1 = new ArrayList<>();
        ArrayList<Product> products2C2 = new ArrayList<>();
        ArrayList<Product> products2P1 = new ArrayList<>();
        ArrayList<Product> products2P2 = new ArrayList<>();

        Product pr1 = new Product("Prod1", 0);
        Product pr2 = new Product("Prod2", 0);
        Product pr3 = new Product("Prod3", 0);
        Product pr4 = new Product("Prod4", 0);
        Product pr5 = new Product("Prod5", 5);
        Product pr6 = new Product("Prod6", 2);
        Product pr7 = new Product("Prod7", 0);
        Product pr8 = new Product("Prod8", 0);
        Product pr9 = new Product("Prod9", 0);
        Product pr10 = new Product("Prod10", 0);
        Product pr11 = new Product("Prod11", 2.5f);
        Product pr12 = new Product("Prod12", 0);
        Product pr13 = new Product("Prod1", 0);
        Product pr14 = new Product("Prod2", 5.5f);
        Product pr15 = new Product("Prod3", 4.5f);
        Product pr16 = new Product("Prod4", 0);
        Product pr17 = new Product("Prod5", 4);
        Product pr18 = new Product("Prod6", 0);
        Product pr19 = new Product("Prod7", 0);
        Product pr20 = new Product("Prod8", 0);
        Product pr21 = new Product("Prod9", 1);
        Product pr22 = new Product("Prod10", 9);
        Product pr23 = new Product("Prod11", 10);
        Product pr24 = new Product("Prod12", 0);
        Product pr25 = new Product("Prod1", 4.5f);
        Product pr26 = new Product("Prod2", 6);
        Product pr27 = new Product("Prod3", 3.5f);
        Product pr28 = new Product("Prod4", 0);
        Product pr29 = new Product("Prod5", 4);
        Product pr30 = new Product("Prod6", 0);
        Product pr31 = new Product("Prod7", 9);
        Product pr32 = new Product("Prod8", 3);
        Product pr33 = new Product("Prod9", 0);
        Product pr34 = new Product("Prod10", 5.5f);
        Product pr35 = new Product("Prod11", 1.5f);
        Product pr36 = new Product("Prod12", 0);
        Product pr85 = new Product("Prod1", 9);
        Product pr86 = new Product("Prod2", 7);
        Product pr87 = new Product("Prod3", 0);
        Product pr88 = new Product("Prod4", 1.5f);
        Product pr89 = new Product("Prod5", 6);
        Product pr90 = new Product("Prod6", 0);
        Product pr91 = new Product("Prod7", 5);
        Product pr92 = new Product("Prod8", 0);
        Product pr93 = new Product("Prod9", 5);
        Product pr94 = new Product("Prod10", 10);
        Product pr95 = new Product("Prod11", 1);
        Product pr96 = new Product("Prod12", 3);
        Product pr37 = new Product("Prod1", 0);
        Product pr38 = new Product("Prod2", 7.5f);
        Product pr39 = new Product("Prod3", 9);
        Product pr40 = new Product("Prod4", 2);
        Product pr41 = new Product("Prod5", 6);
        Product pr42 = new Product("Prod6", 0);
        Product pr43 = new Product("Prod7", 8.5f);
        Product pr44 = new Product("Prod8", 3);
        Product pr45 = new Product("Prod9", 3.5f);
        Product pr46 = new Product("Prod10", 9);
        Product pr47 = new Product("Prod11", 1);
        Product pr48 = new Product("Prod12", 0);
        Product pr49 = new Product("Prod1", 3);
        Product pr50 = new Product("Prod2", 0);
        Product pr51 = new Product("Prod3", 0);
        Product pr52 = new Product("Prod4", 0);
        Product pr53 = new Product("Prod5", 4.5f);
        Product pr54 = new Product("Prod6", 4);
        Product pr55 = new Product("Prod7", 0);
        Product pr56 = new Product("Prod8", 4);
        Product pr57 = new Product("Prod9", 5);
        Product pr58 = new Product("Prod10", 0);
        Product pr59 = new Product("Prod11", 0);
        Product pr60 = new Product("Prod12", 2.5f);
        Product pr61 = new Product("Prod1", 7.5f);
        Product pr62 = new Product("Prod2", 6.5f);
        Product pr63 = new Product("Prod3", 1.5f);
        Product pr64 = new Product("Prod4", 7);
        Product pr65 = new Product("Prod5", 4);
        Product pr66 = new Product("Prod6", 2.5f);
        Product pr67 = new Product("Prod7", 4.5f);
        Product pr68 = new Product("Prod8", 3.5f);
        Product pr69 = new Product("Prod9", 1);
        Product pr70 = new Product("Prod10", 0);
        Product pr71 = new Product("Prod11", 0);
        Product pr72 = new Product("Prod12", 0);
        Product pr73 = new Product("Prod1", 0);
        Product pr74 = new Product("Prod2", 0);
        Product pr75 = new Product("Prod3", 2.5f);
        Product pr76 = new Product("Prod4", 0);
        Product pr77 = new Product("Prod5", 5);
        Product pr78 = new Product("Prod6", 7.5f);
        Product pr79 = new Product("Prod7", 8.5f);
        Product pr80 = new Product("Prod8", 0);
        Product pr81 = new Product("Prod9", 3);
        Product pr82 = new Product("Prod10", 0);
        Product pr83 = new Product("Prod11", 0);
        Product pr84 = new Product("Prod12", 8.5f);

        productsC1.add(pr1);
        productsC1.add(pr2);
        productsC1.add(pr3);
        productsC1.add(pr4);
        productsC1.add(pr5);
        productsC1.add(pr6);
        productsC1.add(pr7);
        productsC1.add(pr8);
        productsC1.add(pr9);
        productsC1.add(pr10);
        productsC1.add(pr11);
        productsC1.add(pr12);
        productsC2.add(pr13);
        productsC2.add(pr14);
        productsC2.add(pr15);
        productsC2.add(pr16);
        productsC2.add(pr17);
        productsC2.add(pr18);
        productsC2.add(pr19);
        productsC2.add(pr20);
        productsC2.add(pr21);
        productsC2.add(pr22);
        productsC2.add(pr23);
        productsC2.add(pr24);
        products2C1.add(pr25);
        products2C1.add(pr26);
        products2C1.add(pr27);
        products2C1.add(pr28);
        products2C1.add(pr29);
        products2C1.add(pr30);
        products2C1.add(pr31);
        products2C1.add(pr32);
        products2C1.add(pr33);
        products2C1.add(pr34);
        products2C1.add(pr35);
        products2C1.add(pr36);
        products2C2.add(pr85);
        products2C2.add(pr86);
        products2C2.add(pr87);
        products2C2.add(pr88);
        products2C2.add(pr89);
        products2C2.add(pr90);
        products2C2.add(pr91);
        products2C2.add(pr92);
        products2C2.add(pr93);
        products2C2.add(pr94);
        products2C2.add(pr95);
        products2C2.add(pr96);
        productsP1.add(pr37);
        productsP1.add(pr38);
        productsP1.add(pr39);
        productsP1.add(pr40);
        productsP1.add(pr41);
        productsP1.add(pr42);
        productsP1.add(pr43);
        productsP1.add(pr44);
        productsP1.add(pr45);
        productsP1.add(pr46);
        productsP1.add(pr47);
        productsP1.add(pr48);
        productsP2.add(pr49);
        productsP2.add(pr50);
        productsP2.add(pr51);
        productsP2.add(pr52);
        productsP2.add(pr53);
        productsP2.add(pr54);
        productsP2.add(pr55);
        productsP2.add(pr56);
        productsP2.add(pr57);
        productsP2.add(pr58);
        productsP2.add(pr59);
        productsP2.add(pr60);
        products2P1.add(pr61);
        products2P1.add(pr62);
        products2P1.add(pr63);
        products2P1.add(pr64);
        products2P1.add(pr65);
        products2P1.add(pr66);
        products2P1.add(pr67);
        products2P1.add(pr68);
        products2P1.add(pr69);
        products2P1.add(pr70);
        products2P1.add(pr71);
        products2P1.add(pr72);
        products2P2.add(pr73);
        products2P2.add(pr74);
        products2P2.add(pr75);
        products2P2.add(pr76);
        products2P2.add(pr77);
        products2P2.add(pr78);
        products2P2.add(pr79);
        products2P2.add(pr80);
        products2P2.add(pr81);
        products2P2.add(pr82);
        products2P2.add(pr83);
        products2P2.add(pr84);

        ClientBasket basket1 = new ClientBasket(c1, productsC1);
        ClientBasket basket2 = new ClientBasket(c2, productsC2);
        ClientBasket basket3 = new ClientBasket(c1, products2C1);
        ClientBasket basket4 = new ClientBasket(c2, products2C2);
        ClientBasket basket5 = new ClientBasket(p1, productsP1);
        ClientBasket basket6 = new ClientBasket(p2, productsP2);
        ClientBasket basket7 = new ClientBasket(p1, products2P1);
        ClientBasket basket8 = new ClientBasket(p2, products2P2);

        stockExpected.put(1, new ArrayList<>());
        stockExpected.get(1).add(basket5);
        stockExpected.get(1).add(basket6);
        stockExpected.put(2, new ArrayList<>());
        stockExpected.get(2).add(basket7);
        stockExpected.get(2).add(basket8);

        ordersExpected.put(1, new ArrayList<>());
        ordersExpected.get(1).add(basket1);
        ordersExpected.get(1).add(basket2);
        ordersExpected.put(2, new ArrayList<>());
        ordersExpected.get(2).add(basket3);
        ordersExpected.get(2).add(basket4);

    }

    @Test
    public void thereIsProduct() {

        ArrayList<ClientBasket> baskets = new ArrayList<>();
        ArrayList<ClientBasket> old = new ArrayList<>();
        ArrayList<ClientBasket> recent = new ArrayList<>();

        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "P1");
        ClientsProducers c2 = new ClientsProducers("CT2", 38.0333f, -7.8833f, "P2");
        ClientsProducers p1 = new ClientsProducers("PT1", 40.6389f, -8.6553f, "P1");

        ClientBasket basket = new ClientBasket(c1, new ArrayList<>());
        basket.getProducts().add(new Product("Prod1", 0));
        basket.getProducts().add(new Product("Prod2", 1));

        ClientBasket basket2 = new ClientBasket(c2, new ArrayList<>());
        basket2.getProducts().add(new Product("Prod1", 1));
        basket2.getProducts().add(new Product("Prod2", 1));

        baskets.add(basket);
        baskets.add(basket2);

        Assertions.assertEquals(ExpeditionListCreator.thereIsProduct(baskets, old, recent, new Product("Prod2", 0), p1).getCode(), c2.getCode());
    }

    @Test
    public void returnProductID(){
        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "P1");
        ClientBasket basket = new ClientBasket(c1, new ArrayList<>());
        basket.getProducts().add(new Product("Prod1", 0));
        basket.getProducts().add(new Product("Prod2", 1));

        Assertions.assertEquals(ExpeditionListCreator.returnProductID(basket, new Product("Prod2", 0)), 1);
        Assertions.assertNotEquals(ExpeditionListCreator.returnProductID(basket, new Product("Prod2", 0)), 0);
        Assertions.assertEquals(ExpeditionListCreator.returnProductID(basket, new Product("Prod3", 0)), -1);
    }


    @Test
    public void findProductOwnerID(){

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

        Assertions.assertEquals(ExpeditionListCreator.findProductOwnerID(baskets, c1, 0), 0);
        Assertions.assertEquals(ExpeditionListCreator.findProductOwnerID(baskets, c2, 0), 1);
        Assertions.assertEquals(ExpeditionListCreator.findProductOwnerID(baskets, p1, 0), -1);
    }

    @Test
    public void findProductSurplus(){

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

        Assertions.assertEquals(ExpeditionListCreator.findProductSurplus(baskets, new Product("Prod1",0),1,0), 0);
        Assertions.assertEquals(ExpeditionListCreator.findProductSurplus(baskets, new Product("Prod2",0),0,0), 1);
        Assertions.assertEquals(ExpeditionListCreator.findProductSurplus(baskets, new Product("Prod3",0),1,0), -1);

    }

}

