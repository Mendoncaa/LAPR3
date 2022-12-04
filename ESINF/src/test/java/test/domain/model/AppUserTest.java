package test.domain.model;

import app.domain.model.AppUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppUserTest {

    @Test
    void getEmail() {
        AppUser au = new AppUser("Test", "emailTest", "tester");
        assertEquals("emailTest", au.getEmail());
    }

    @Test
    void getRole() {
        AppUser au = new AppUser("Test", "emailTest", "tester");
        assertEquals("tester", au.getRole());
    }

    @Test
    void getName() {
        AppUser au = new AppUser("Test", "emailTest", "tester");
        assertEquals("Test", au.getName());
    }

    @Test
    void testEquals() { //id will be different for every generated instance
        AppUser au = new AppUser("Test", "emailTest", "tester");
        AppUser au2 = new AppUser("Test", "emailTest", "tester");
        assertNotEquals(au, au2);
    }

}