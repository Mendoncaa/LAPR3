package app.domain.shared;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.domain.model.HubAndDist;
import app.domain.model.Path;
import app.graph.Algorithms;
import app.graph.Graph;
import app.graph.map.MapGraph;

import java.util.*;

public class ClosestPointsCheck {

    static MapGraph<ClientsProducers, Integer> clpGraph = App.getInstance().getCompany().getClientsProducersGraph();

    static LinkedList<ClientsProducers> path = new LinkedList<>();

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

    public static ArrayList<ClientsProducers> closestProducers (ClientsProducers hub, int n) {

        // System.out.println(clpGraph);
        ArrayList<ClientsProducers> closestPbuffer = new ArrayList<>();
        ArrayList<ClientsProducers> closestP = new ArrayList<>();

        ArrayList<ClientsProducers> producersToFilter = clpGraph.vertices();
        Map<ClientsProducers, Integer> producerDistance = new HashMap<>();

        producersToFilter.removeIf(p -> !p.getType().equalsIgnoreCase("Produtor"));

        //System.out.println(producersToFilter);

        for (ClientsProducers c : clpGraph.vertices()) {

            if (c.getLocationID().equalsIgnoreCase(hub.getLocationID())) {
                hub = c;
            }
        }

        //System.out.println(hub);

        for(ClientsProducers p: producersToFilter) {
            //System.out.println(clpGraph.vertices());
            Integer sPathResults = Algorithms.shortestPath(clpGraph, hub, p, Integer::compare, Integer::sum, 0, path);
            //System.out.println(clpGraph.validVertex(hub));
            //System.out.println(clpGraph.validVertex(p));
            //System.out.println(path);
            // System.out.println(p);
            // System.out.println(sPathResults);
            producerDistance.put(p, sPathResults);

            path.clear();
        }

        List<Integer> distances = new ArrayList<>(producerDistance.values());
        Collections.sort(distances);

        for (Integer distance : distances) {
            for(Map.Entry<ClientsProducers, Integer> pD : producerDistance.entrySet()) {
                if (pD.getValue().equals(distance)) {
                    closestPbuffer.add(pD.getKey());
                }
            }


        }

        for(int i = 0; i < n; i++) {
            closestP.add(closestPbuffer.get(i));
        }

        //System.out.println(closestP);
        //System.out.println(distances);
        //System.out.println(producerDistance);

        return closestP;
    }
}

