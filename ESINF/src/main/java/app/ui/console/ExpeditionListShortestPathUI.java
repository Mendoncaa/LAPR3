package app.ui.console;

import app.controller.App;
import app.controller.ExpeditionListShortestPathController;
import app.ui.console.utils.Utils;

import java.util.Objects;

public class ExpeditionListShortestPathUI implements Runnable{

    private ExpeditionListShortestPathController ctrl = new ExpeditionListShortestPathController();

    @Override
    public void run() {

        int n = Integer.parseInt(Objects.requireNonNull(Utils.readLineFromConsole("Expedition list day:\n ")));

        System.out.println("Shortest delivery path");
        System.out.println(ctrl.ExpeditionListShortestPath(App.getInstance().getCompany().getStatistics().getStatisticsForBasket().get(n)));
        System.out.println("Distances between points");
        System.out.println(ctrl.generateTobePrintedDistanceMap());
        System.out.println("Total distance");
        System.out.println(ctrl.getTotalDistance());
        System.out.println("Hub/Baskets Delivered");
        System.out.println(ctrl.generateToBePrintedMap());


    }
}
