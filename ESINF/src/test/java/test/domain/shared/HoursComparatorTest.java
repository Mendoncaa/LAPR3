package test.domain.shared;

import app.domain.model.HoursMinutes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HoursComparatorTest {

    @Test
    public void testCompare() {
        HoursMinutes hm1 = new HoursMinutes(10, 30);
        HoursMinutes hm2 = new HoursMinutes(10, 30);
        HoursMinutes hm3 = new HoursMinutes(10, 40);
        HoursMinutes hm4 = new HoursMinutes(11, 30);

        Assertions.assertEquals(0, hm1.compareTo(hm2));
        Assertions.assertEquals(1, hm3.compareTo(hm1));
        Assertions.assertEquals(-1, hm1.compareTo(hm4));
    }
}
