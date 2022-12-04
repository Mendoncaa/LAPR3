package test.domain.model;

import app.domain.model.ClientsProducers;
import app.domain.model.HubAndDist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HubAndDistTest {

    ClientsProducers clp = new ClientsProducers("CT1", 10, 10, "C1");

    @Test
    void getHub() {
        HubAndDist hd = new HubAndDist(clp, 13);
        assertEquals(clp, hd.getHub());
    }

    @Test
    void getDist() {
        HubAndDist hd = new HubAndDist(clp, 13);
        assertEquals(13, hd.getDist());
    }
}