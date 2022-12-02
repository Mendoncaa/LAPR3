package test.domain.model;

import app.domain.model.Company;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    @Test
    void getDesignation() {
        Company c1 = new Company("Company1");
        Assertions.assertEquals("Company1", c1.getDesignation());

    }
}