package test;

import app.domain.model.ClientsProducers;
import app.domain.model.HubAndDist;
import app.domain.shared.DistanceComparator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceComparatorTest {

    @Test
    public void testCompare() {

        HubAndDist hubAndDist1 = new HubAndDist(new ClientsProducers(), 1);
        HubAndDist hubAndDist2 = new HubAndDist(new ClientsProducers(), 2);

        DistanceComparator instance = new DistanceComparator();
        Assertions.assertEquals(-1, instance.compare(hubAndDist1, hubAndDist2));
        Assertions.assertEquals(1, instance.compare(hubAndDist2, hubAndDist1));
        Assertions.assertEquals(0, instance.compare(hubAndDist1, hubAndDist1));
    }
}
