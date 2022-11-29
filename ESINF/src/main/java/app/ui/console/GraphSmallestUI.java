package app.ui.console;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.graph.Algorithms;
import app.graph.map.MapGraph;
import app.ui.console.utils.Utils;

import java.util.*;

public class GraphSmallestUI implements Runnable{

    MapGraph<ClientsProducers, Integer> clpGraph = App.getInstance().getCompany().getClientsProducersGraph();
    ClientsProducers clp1 = null;
    ArrayList<ClientsProducers> cp = clpGraph.vertices();
    LinkedList<String> toBePrinted = new LinkedList<>();
   ArrayList< LinkedList<ClientsProducers>> path = new ArrayList<>();

   ArrayList<Integer> dists = new ArrayList<>();
    ArrayList<String> codePath = new ArrayList<>();

    Comparator<Double> cpE;
    @Override
    public void run() {

        generateToBePrinted(cp, toBePrinted);

        System.out.printf(toBePrinted + "\n");

        String og = Utils.readLineFromConsole("Please Select Origin Vertex from list!\n");

        for(int i = 0; i < cp.size(); i++) {

            if (og.equalsIgnoreCase(cp.get(i).getCode())) {
                clp1 = cp.get(i);
            }
        }

        System.out.println(clp1);

        boolean sPathsResults = Algorithms.shortestPaths(clpGraph, clp1, Integer::compare, Integer::sum, 0, path, dists);

        for(int i = 0; i < path.size(); i++) {

        }

        System.out.printf("Smallest path from Origin to Destination is:" + codePath + " with size %d and distance in meters %d.\n", codePath.size(), sPathsResults);

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