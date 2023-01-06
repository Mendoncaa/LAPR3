package app.controller;

import app.domain.model.Company;
import app.domain.model.ExpeditionList;
import app.domain.shared.ExpeditionListCreator;

import java.util.ArrayList;

public class ExpeditionListNClosestController {

    private Company company;

    public ExpeditionListNClosestController() {
        this.company = App.getInstance().getCompany();
    }

    public ExpeditionListNClosestController(Company company) {
        this.company = company;
    }

    public ArrayList<ExpeditionList> ExpeditionListNClosest(int day, int n) {
        return ExpeditionListCreator.getExpeditionListNClosest(day, n);
    }
}
