package app.ui.console;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.graph.Algorithms;
import app.graph.map.MapGraph;
import app.ui.console.utils.Utils;

import java.util.*;

public class GraphUI implements Runnable{

    static MapGraph<ClientsProducers, Integer> clpGraph = App.getInstance().getCompany().getClientsProducersGraph();
    static ClientsProducers clp1 = null;
    static ClientsProducers clp2 = null;

    LinkedList<String> toBePrinted = new LinkedList<>();
    LinkedList<ClientsProducers> path = new LinkedList<>();
    ArrayList<String> codePath = new ArrayList<>();

    @Override
    public void run() {

        ArrayList<ClientsProducers> cp = clpGraph.vertices();

        generateToBePrinted(cp, toBePrinted);

        System.out.printf(toBePrinted + "\n");

        String og = Utils.readLineFromConsole("Please Select Origin Vertex from list!\n");

        for(int i = 0; i < cp.size(); i++) {

            if (og.equalsIgnoreCase(cp.get(i).getCode())) {
                clp1 = cp.get(i);
            }
        }

        System.out.println(clp1);

        String dest = Utils.readLineFromConsole("Please Select Destination Vertex from list!\n");

        for(int i = 0; i < cp.size(); i++) {

            if (dest.equalsIgnoreCase(cp.get(i).getCode())) {
                clp2 = cp.get(i);
            }
        }

        System.out.println(clp2);

        Integer sPathResults = Algorithms.shortestPath(clpGraph, clp1, clp2, Integer::compare, Integer::sum, 0, path);

        for(int i = 0; i < path.size(); i++) {

            codePath.add(path.get(i).getCode());

        }

        System.out.printf("Smallest path from Origin to Destination is:" + codePath + " with size %d and distance in meters %d.\n", codePath.size(), sPathResults);

        toBePrinted.clear();
        path.clear();
        codePath.clear();

    }
    private static void generateToBePrinted (ArrayList<ClientsProducers> cp, LinkedList<String> toBePrinted) {

        for(int i = 0; i < cp.size(); i++) {

            if(cp.get(i).getType().equalsIgnoreCase("Empresa")) {
                continue;
            }
            String cpCodeBuffer = cp.get(i).getCode();

            toBePrinted.add(cpCodeBuffer);
        }
    }
}
