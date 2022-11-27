package app.ui.console;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.graph.Algorithms;
import app.graph.Graph;
import app.graph.map.MapGraph;

import java.util.Comparator;
import java.util.LinkedList;

public class GraphUI implements Runnable{

    MapGraph<ClientsProducers, Double> clpGraph = App.getInstance().getCompany().getClientsProducersGraph();
    ClientsProducers clp1;
    ClientsProducers clp2;

    LinkedList<ClientsProducers> path = new LinkedList<>();

    Comparator<Double> cpE;
    @Override

    public void run() {


       // LinkedList<ClientsProducers> dfsResults = Algorithms.shortestPath(clpGraph, clp1, clp2, Integer::compare, Integer::sum, 0, path);

    }


    public <V, E> E getShortestPath(Graph<V, E> clpGraph, ClientsProducers clp1, ClientsProducers clp2, LinkedList<ClientsProducers> path) {



        return null;
    }
}
