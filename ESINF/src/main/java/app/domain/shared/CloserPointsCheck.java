package app.domain.shared;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.domain.model.Path;
import app.graph.Algorithms;
import app.graph.Graph;

import java.util.ArrayList;
import java.util.LinkedList;

public class CloserPointsCheck {

    public static ArrayList<Path> getCloserPoints() {

        Graph<ClientsProducers, Integer> graph = App.getInstance().getCompany().getClientsProducersGraph();

        ArrayList<ClientsProducers> companies = new ArrayList<>();
        ArrayList<ClientsProducers> vertices = graph.vertices();

        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getType(vertices.get(i).getCode()).equalsIgnoreCase("Empresa")) {
                companies.add(vertices.get(i));
            }
        }


        ArrayList<LinkedList<ClientsProducers>> paths = new ArrayList<>();
        ArrayList<Integer> dists = new ArrayList<>();
        ArrayList<Path> pathArrayList = new ArrayList<>();

        for (int i = 0; i < companies.size(); i++) {
            paths.clear();
            dists.clear();
            Algorithms.shortestPaths(graph, companies.get(i), Integer::compare, Integer::sum, 0, paths, dists);
            pathArrayList.add(new Path(companies.get(i),paths,dists));
        }

        return pathArrayList;

    }
}

