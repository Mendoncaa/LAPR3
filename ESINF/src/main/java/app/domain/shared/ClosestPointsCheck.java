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

    public static Set<Path> getCloserPoints(Graph<ClientsProducers, Integer> graph) {

        ArrayList<ClientsProducers> companies = new ArrayList<>();
        ArrayList<ClientsProducers> vertices = graph.vertices();

        for (ClientsProducers vertex : vertices) {
            if (vertex.getType().equalsIgnoreCase("Empresa")) {
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

        Set<Path> closerPointsSet = new TreeSet<>(new AverageComparator());
        closerPointsSet.addAll(pathArrayList);

        return closerPointsSet;
    }

    public static ClientsProducers getClosestHub(ClientsProducers cp, Graph<ClientsProducers, Integer> graph) {

        ArrayList<LinkedList<ClientsProducers>> paths = new ArrayList<>();
        ArrayList<Integer> dists = new ArrayList<>();

        Algorithms.shortestPaths(graph, cp, Integer::compare, Integer::sum, 0, paths, dists);
        Set<HubAndDist> topHubs = new TreeSet<>(new DistanceComparator());

        for (int i = 0; i < paths.size(); i++) {
            if (paths.get(i).getLast().getType().equalsIgnoreCase("Empresa")) {
                topHubs.add(new HubAndDist(paths.get(i).getLast(), dists.get(i)));
            }
        }
        if (topHubs.isEmpty()){
            return null;
        }
        return topHubs.iterator().next().getHub();
    }
}

