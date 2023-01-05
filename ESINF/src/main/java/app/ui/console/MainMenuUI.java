package app.ui.console;

import app.controller.App;
import app.domain.model.ClientBasket;
import app.domain.model.ClientsProducers;
import app.domain.model.Product;
import app.ui.console.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class MainMenuUI {

    public MainMenuUI() {
    }

    public void run() throws IOException {
        List<MenuItem> options = new ArrayList<MenuItem>();
        //options.add(new MenuItem("Do Login", new AuthUI()));
        options.add(new MenuItem("Import clients and producers, generate graph", new ImportClientsProducersUI()));
        options.add(new MenuItem("Get graph diameter - note: option 1 must have been run", new GraphDiameterUI()));
        options.add(new MenuItem("Get shortest path between Clients - note: option 1 must have been run", new GraphUI()));
        options.add(new MenuItem("Get smallest fully connected network - note: option 1 must have been run", new GraphSmallestUI()));
        options.add(new MenuItem("Define the hubs of the distribution network", new NCloserPointsUI()));
        options.add(new MenuItem("Get closest hub", new ClosestHubUI()));
        options.add(new MenuItem("Register an irrigation device ", new IrrigationDeviceCreationUI()));
        options.add(new MenuItem("Check current irrigation ", new IrrigationDeviceUI()));
        options.add(new MenuItem("Import basket list", new BasketListUI()));
        options.add(new MenuItem("Generate a dispatch list without any restriction", new ExpeditionListNoRestUI()));
        options.add(new MenuItem("Debug", new DebugUI()));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\nMain Menu\n");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        }
        while (option != -1);
    }

}
