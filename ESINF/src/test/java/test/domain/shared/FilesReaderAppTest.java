package test.domain.shared;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.domain.shared.FilesReaderApp;
import app.graph.map.MapGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static app.domain.shared.FilesReaderApp.isConnected;

class FilesReaderAppTest {

    MapGraph<ClientsProducers, Integer> testMap = new MapGraph<>(false);



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

    }

    @Test
    public void testReadIrrigationDeviceFile() {
        Assertions.assertTrue(FilesReaderApp.readIrrigationDeviceFile(new File("src/files/wateringController.csv")));
        Assertions.assertFalse(FilesReaderApp.readIrrigationDeviceFile(new File("src/files/wateringController.txt")));
    }


    @Test
    void testIsConnected() {

        ArrayList<ClientsProducers> cp = testMap.vertices();
        ClientsProducers c1 = cp.get(0);
        boolean connected = isConnected(testMap, c1, cp);
        Assertions.assertTrue(connected);

    }

    @Test
    void readProducerCSV() {

        File graphVertexFileSmall = new File("../ESINF/src/files/Small/clientes-produtores_small.csv");
        File graphEdgeFileSmall = new File("../ESINF/src/files/Small/distancias_small.csv");
        FilesReaderApp.readProducerCSV(graphVertexFileSmall,graphEdgeFileSmall);
        MapGraph<ClientsProducers, Integer> clpGraph = App.getInstance().getCompany().getClientsProducersGraph();

        Assertions.assertEquals(testMap.vertices().size(), clpGraph.vertices().size());
        Assertions.assertEquals(testMap.edges().size(), clpGraph.edges().size());

    }
}