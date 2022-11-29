package app.ui.console;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.graph.Algorithms;
import app.graph.Graph;
import app.graph.map.MapGraph;
import app.ui.console.utils.Utils;

import java.util.*;

public class GraphUI implements Runnable{

    MapGraph<ClientsProducers, Integer> clpGraph = App.getInstance().getCompany().getClientsProducersGraph();
    ClientsProducers clp1 = null;
    ClientsProducers clp2 = null;
    ArrayList<ClientsProducers> cp = clpGraph.vertices();
    LinkedList<String> toBePrinted = new LinkedList<>();
    String cpCodeBuffer;
    LinkedList<ClientsProducers> path = new LinkedList<>();

    ArrayList<String> codePath = new ArrayList<>();

    Comparator<Double> cpE;
    @Override
    public void run() {

        for(int i = 0; i < cp.size(); i++) {

            if(cp.get(i).getType().equalsIgnoreCase("Empresa")) {
                continue;
            }
            cpCodeBuffer = cp.get(i).getCode();

            toBePrinted.add(cpCodeBuffer);
        }

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

        Integer dfsResults = Algorithms.shortestPath(clpGraph, clp1, clp2, Integer::compare, Integer::sum, 0, path);

        for(int i = 0; i < path.size(); i++) {

            codePath.add(path.get(i).getCode());

        }

        System.out.printf("Smallest path from Origin to Destination is:" + codePath + "with size %d.\n", dfsResults);

    }


    public <V, E> E getShortestPath(Graph<V, E> clpGraph, ClientsProducers clp1, ClientsProducers clp2, LinkedList<ClientsProducers> path) {



        return null;
    }
}
