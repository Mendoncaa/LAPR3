package app.ui.console;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.domain.shared.ClosestPointsCheck;
import app.graph.map.MapGraph;

public class DebugUI implements Runnable {

    static MapGraph<ClientsProducers, Integer> cpgraph = App.getInstance().getCompany().getClientsProducersGraph();


    ClientsProducers hub = new ClientsProducers("CT5", 39.823f, -7.4931f,"E3");

    @Override
    public void run() {
        ClosestPointsCheck.closestProducers(hub,2);
    }
}
