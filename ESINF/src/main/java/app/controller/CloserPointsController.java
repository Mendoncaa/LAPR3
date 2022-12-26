package app.controller;

import app.domain.model.ClientsProducers;
import app.domain.model.Company;
import app.domain.model.Path;
import app.domain.shared.ClosestPointsCheck;
import app.graph.Graph;

import java.util.ArrayList;
import java.util.Set;

public class CloserPointsController {


    private Company company;

    public CloserPointsController() {
        this.company = App.getInstance().getCompany();
    }

    public CloserPointsController(Company company) {
        this.company = company;
    }

    public Set<Path> getCloserPoints(Graph<ClientsProducers, Integer> graph) {
        return ClosestPointsCheck.getCloserPoints(graph);
    }
}
