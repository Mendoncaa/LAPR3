package app.controller;

import app.domain.model.ClientsProducers;
import app.domain.model.Company;
import app.domain.model.Path;
import app.domain.shared.ClosestPointsCheck;
import app.domain.shared.FilesReaderApp;
import app.graph.Graph;

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

    public boolean importBasketList(File file) {
        return FilesReaderApp.importBasketList(file);
    }
}
