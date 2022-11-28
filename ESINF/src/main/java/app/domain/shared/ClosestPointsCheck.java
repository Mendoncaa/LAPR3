package app.domain.shared;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.domain.model.HubAndDist;
import app.domain.model.Path;
import app.graph.Algorithms;
import app.graph.Graph;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class ClosestPointsCheck {

    public static ArrayList<Path> getCloserPoints() {

        Graph<ClientsProducers, Integer> graph = App.getInstance().getCompany().getClientsProducersGraph();

        ArrayList<ClientsProducers> companies = new ArrayList<>();
        ArrayList<ClientsProducers> vertices = graph.vertices();

        for (ClientsProducers vertex : vertices) {
            if (vertex.getType(vertex.getCode()).equalsIgnoreCase("Empresa")) {
                companies.add(vertex);
            }
        }


        ArrayList<LinkedList<ClientsProducers>> paths = new ArrayList<>();
        ArrayList<Integer> dists = new ArrayList<>();
        ArrayList<Path> pathArrayList = new ArrayList<>();

        for (ClientsProducers company : companies) {
            paths.clear();
            dists.clear();
            Algorithms.shortestPaths(graph, company, Integer::compare, Integer::sum, 0, paths, dists);
            pathArrayList.add(new Path(company, paths, dists));
        }

        return pathArrayList;

    }

    public static ClientsProducers getClosestHub(ClientsProducers cp) {

        ArrayList<LinkedList<ClientsProducers>> paths = new ArrayList<>();
        ArrayList<Integer> dists = new ArrayList<>();

        Algorithms.shortestPaths(App.getInstance().getCompany().getClientsProducersGraph(), cp, Integer::compare, Integer::sum, 0, paths, dists);
        Set<HubAndDist> topHubs = new TreeSet<>(new DistanceComparator());

        for (int i = 0; i < paths.size(); i++) {
            if (paths.get(i).getLast().getType(paths.get(i).getLast().getCode()).equalsIgnoreCase("Empresa")) {
                topHubs.add(new HubAndDist(paths.get(i).getLast(), dists.get(i)));
            }
        }

        return topHubs.iterator().next().getHub();
    }
}

