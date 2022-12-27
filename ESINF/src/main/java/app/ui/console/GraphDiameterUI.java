package app.ui.console;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.domain.shared.FilesReaderApp;
import app.domain.shared.GraphDiameter;
import app.domain.shared.KruskalMST;
import app.graph.map.MapGraph;

public class GraphDiameterUI implements Runnable{

    static MapGraph<ClientsProducers, Integer> cpgraph = App.getInstance().getCompany().getClientsProducersGraph();

    @Override
    public void run() {

        if(!FilesReaderApp.graphImported) {
            System.out.println("Graph not yet imported!");
        }else {
            int diameter = GraphDiameter.getDiameter(cpgraph);
            System.out.printf("Graph diameter: %d \n", diameter);
        }
    }
}
