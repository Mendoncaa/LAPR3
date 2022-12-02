package test.domain.model;

import app.domain.model.ClientsProducers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientsProducersTest {


    @Test
    void getLocationID() {
        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "C1");
        Assertions.assertEquals("CT1", c1.getLocationID());
    }

    @Test
    void getLatitude() {
        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "C1");
        Assertions.assertEquals(40.6389f, c1.getLatitude());
    }

    @Test
    void getLongitude() {
        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "C1");
        Assertions.assertEquals(-8.6553f, c1.getLongitude());
    }

    @Test
    void getCode() {
        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "C1");
        Assertions.assertEquals("C1", c1.getCode());
    }

    @Test
    void getType() {
        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "C1");
        Assertions.assertEquals("Cliente", c1.getType());
    }

    @Test
    void testEquals() {
        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "C1");
        ClientsProducers c2 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "C1");
        Assertions.assertEquals(c1, c2);
    }

    @Test
    void testHashCode() {
    }
}