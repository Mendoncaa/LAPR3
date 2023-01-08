package app.ui.console;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.domain.shared.ExpeditionListShortestPath;
import app.graph.map.MapGraph;
import app.ui.console.utils.Utils;

import java.util.Objects;

public class DebugUI implements Runnable {

    static MapGraph<ClientsProducers, Integer> cpgraph = App.getInstance().getCompany().getClientsProducersGraph();

    static ClientsProducers hub = new ClientsProducers("CT5", 39.823f, -7.4931f,"E3");

    @Override
    public void run() {

        //ClosestPointsCheck.closestProducers(hub,2);
        //ExpeditionListCreator.getExpeditionListNClosest(2, 3);

        int n = Integer.parseInt(Objects.requireNonNull(Utils.readLineFromConsole("\nday ")));

        System.out.println(ExpeditionListShortestPath.getExpListShortestPath(App.getInstance().getCompany().getStatistics().getStatisticsForBasket().get(n)));

    }
}
