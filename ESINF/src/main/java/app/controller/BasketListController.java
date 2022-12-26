package app.controller;

import app.domain.model.ClientsProducers;
import app.domain.model.Company;
import app.domain.model.Path;
import app.domain.shared.ClosestPointsCheck;
import app.domain.shared.FilesReaderApp;
import app.graph.Graph;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.Set;

public class BasketListController {

    private Company company;

    public BasketListController() {
        this.company = App.getInstance().getCompany();
    }

    public BasketListController(Company company) {
        this.company = company;
    }

    public Pair<Integer, Integer> importBasketList(File file) {
        return FilesReaderApp.importBasketList(file);
    }
}
