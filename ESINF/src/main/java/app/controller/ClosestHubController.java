package app.controller;

import app.domain.model.ClientsProducers;
import app.domain.model.Company;
import app.domain.shared.ClosestPointsCheck;

public class ClosestHubController {

    private Company company;

    public ClosestHubController() {
        this.company = App.getInstance().getCompany();
    }

    public ClosestHubController(Company company) {
        this.company = company;
    }

    public ClientsProducers getClosestHub(ClientsProducers cp){return ClosestPointsCheck.getClosestHub(cp);
    }
}
