package app.controller;

import app.domain.model.Company;
import app.domain.model.Path;
import app.domain.shared.CloserPointsCheck;

import java.util.ArrayList;

public class CloserPointsController {


    private Company company;

    public CloserPointsController() {
        this.company = App.getInstance().getCompany();
    }

    public CloserPointsController(Company company) {
        this.company = company;
    }

    public ArrayList<Path> getCloserPoints() {
        return CloserPointsCheck.getCloserPoints();
    }
}
