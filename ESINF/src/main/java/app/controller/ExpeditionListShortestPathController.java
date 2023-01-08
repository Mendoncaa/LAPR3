package app.controller;

import app.domain.model.ClientsProducers;
import app.domain.model.Company;
import app.domain.model.ExpeditionList;
import app.domain.shared.ExpeditionListCreator;
import app.domain.shared.ExpeditionListShortestPath;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class ExpeditionListShortestPathController {

    static
    private Company company;
    public ExpeditionListShortestPathController() {
        this.company = App.getInstance().getCompany();
    }

    public ExpeditionListShortestPathController(Company company) {
        this.company = company;
    }

    public LinkedList<String> ExpeditionListShortestPath(ArrayList<ExpeditionList> exp) {

        return ExpeditionListShortestPath.getExpListShortestPath(exp);
    }

    public Map<String, Integer> generateToBePrintedMap () {
        return ExpeditionListShortestPath.generateToBePrintedMap();
    }
}
