package app.domain.shared;

import app.domain.model.ClientsProducers;
import app.graph.Algorithms;
import app.graph.map.MapGraph;

import java.util.ArrayList;
import java.util.LinkedList;

public class GraphDiameter {

    public static int getDiameter(MapGraph<ClientsProducers, Integer> clpGraph) {

        int diameter = 0;

        ArrayList<ClientsProducers> cp = clpGraph.vertices();
        LinkedList<ClientsProducers> path = new LinkedList<>();

        for(int i = 0; i < cp.size(); i++) {
            for(int j = 0; j < cp.size(); j++) {

                Algorithms.shortestPath(clpGraph, cp.get(i), cp.get(j), Integer::compare, Integer::sum, 0, path);

                if(path.size() > diameter) {
                    diameter = path.size();
                }
                path.clear();
            }
        }

        return diameter;
    }

}
