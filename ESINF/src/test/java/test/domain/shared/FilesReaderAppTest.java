package test.domain.shared;

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

    MapGraph<ClientsProducers, Integer> completeMap = new MapGraph<>(false);
    @BeforeEach
    public void setUp() {

        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "C1");
        ClientsProducers c2 = new ClientsProducers("CT2", 38.0333f, -7.8833f, "C2");
        ClientsProducers e1 = new ClientsProducers("CT3", 38.5243f, -8.8926f, "E1");
        ClientsProducers e2 = new ClientsProducers("CT4", 39.3167f, -7.4167f, "E2");

        completeMap.addVertex(c1);
        completeMap.addVertex(c2);
        completeMap.addVertex(e1);
        completeMap.addVertex(e2);

        completeMap.addEdge(c1, c2, 2);
        completeMap.addEdge(c2, e1, 1);
        completeMap.addEdge(e1, e2, 3);
        completeMap.addEdge(e2, c2, 2);

    }

    @Test
    public void testReadIrrigationDeviceFile() {
        Assertions.assertTrue(FilesReaderApp.readIrrigationDeviceFile(new File("src/files/wateringController.csv")));
        Assertions.assertFalse(FilesReaderApp.readIrrigationDeviceFile(new File("src/files/wateringController.txt")));
    }

    @Test
    void testIsConnected() {

        ArrayList<ClientsProducers> cp = completeMap.vertices();
        ClientsProducers c1 = cp.get(0);
        boolean connected = isConnected(completeMap, c1, cp);
        Assertions.assertTrue(connected);

    }
}