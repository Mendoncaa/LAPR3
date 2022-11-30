package app.controller;

import app.domain.model.ClientsProducers;
import app.domain.model.Company;
import app.domain.shared.ClosestPointsCheck;
import app.graph.Graph;

public class ClosestHubController {

    private Company company;

    public ClosestHubController() {
        this.company = App.getInstance().getCompany();
    }

    public ClosestHubController(Company company) {
        this.company = company;
    }

    public ClientsProducers getClosestHub(ClientsProducers cp, Graph<ClientsProducers,Integer> graph){return ClosestPointsCheck.getClosestHub(cp, graph);
    }
}
