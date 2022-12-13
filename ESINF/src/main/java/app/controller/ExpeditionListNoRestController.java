package app.controller;

import app.domain.model.Company;
import app.domain.model.ExpeditionList;
import app.domain.shared.ExpeditionListCreator;

import java.util.ArrayList;
import java.util.Set;

public class ExpeditionListNoRestController {

    private Company company;

    public ExpeditionListNoRestController() {
        this.company = App.getInstance().getCompany();
    }

    public ExpeditionListNoRestController(Company company) {
        this.company = company;
    }

    public ArrayList<ExpeditionList> getExpeditionListNoRestrictions(int day) {
        return ExpeditionListCreator.getExpeditionListNoRestrictions(day);
    }
}
