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
        options.add(new MenuItem("Get shortest path between Clients", new GraphUI()));
        options.add(new MenuItem("Get smallest fully connected network", new GraphSmallestUI()));
        options.add(new MenuItem("Define the hubs of the distribution network", new NCloserPointsUI()));
        options.add(new MenuItem("Get closest hub", new ClosestHubUI()));
        options.add(new MenuItem("Register an irrigation device ", new IrrigationDeviceCreationUI()));
        options.add(new MenuItem("Check current irrigation ", new IrrigationDeviceUI()));
        options.add(new MenuItem("Import basket list", new BasketListUI()));
        options.add(new MenuItem("Generate a dispatch list without any restriction", new ExpeditionListNoRestUI()));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\nMain Menu\n");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        }
        while (option != -1);


        //System.out.println(App.getInstance().getCompany().getExcedents().size());
        Set<Integer> days = App.getInstance().getCompany().getExcedents().keySet();
        Iterator<Integer> daysIterator = days.iterator();
        for (int i = 0; i < days.size(); i++) {
            int day = daysIterator.next();
            System.out.println("Day " + day);
            ArrayList<ClientBasket> clientBaskets = App.getInstance().getCompany().getExcedents().get(day);
            for (int j = 0; j < clientBaskets.size(); j++) {
                System.out.println();
                System.out.println("Client " + clientBaskets.get(j).getEntity().getCode());
                System.out.println();
                ArrayList<Product> products = clientBaskets.get(j).getProducts();
                for (int k = 0; k < products.size(); k++) {
                    System.out.println("Product " + products.get(k).getName() + " quantity " + products.get(k).getQuantity());
                }
            }
            System.out.println();
        }
    }


}
