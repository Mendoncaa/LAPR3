package test.domain.model;

import app.domain.model.Irrigation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IrrigationTest {

    @Test
    void getSector() {
        Irrigation irg = new Irrigation("Sector1", 2, "t");
        assertEquals("Sector1", irg.getSector());
    }

    @Test
    void setSector() {
        Irrigation irg = new Irrigation(null, 2, "t");
        irg.setSector("Sector1");
        assertEquals("Sector1", irg.getSector());
    }

    @Test
    void getDuration() {
        Irrigation irg = new Irrigation("Sector1", 2, "t");
        assertEquals(2, irg.getDuration());
    }

    @Test
    void setDuration() {
        Irrigation irg = new Irrigation("Sector1", 0, "t");
        irg.setDuration(2);
        assertEquals(2, irg.getDuration());
    }

    @Test
    void getFrequency() {
        Irrigation irg = new Irrigation("Sector1", 2, "t");
        assertEquals("t", irg.getFrequency());
    }

    @Test
    void setFrequency() {
        Irrigation irg = new Irrigation("Sector1", 2, null);
        irg.setFrequency("t");
        assertEquals("t", irg.getFrequency());
    }

    @Test
    void testEquals() {
        Irrigation irg = new Irrigation("Sector1", 2, "t");
        Irrigation irg2 = new Irrigation("Sector1", 2, "t");
        assertEquals(irg, irg2);
    }

}