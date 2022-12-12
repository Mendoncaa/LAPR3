package app.controller;

import app.domain.model.Company;
import app.domain.shared.ExpeditionListCreator;

public class ExpeditionListNoRestController {

    private Company company;

    public ExpeditionListNoRestController() {
        this.company = App.getInstance().getCompany();
    }

    public ExpeditionListNoRestController(Company company) {
        this.company = company;
    }

    public boolean getExpeditionListNoRestrictions() {
        return ExpeditionListCreator.getExpeditionListNoRestrictions();
    }
}
