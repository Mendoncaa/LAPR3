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

        System.out.println(clpGraph);

        LinkedList<ClientsProducers> path = new LinkedList<>();
        ArrayList<ClientsProducers> closestP = new ArrayList<>();

        ArrayList<ClientsProducers> producersToFilter = clpGraph.vertices();
        Map<ClientsProducers, Integer> producerDistance = new HashMap<>();

        producersToFilter.removeIf(p -> !p.getType().equalsIgnoreCase("Produtor"));

        //System.out.println(producersToFilter);

        Iterator<ClientsProducers> it1 = clpGraph.vertices().iterator();

        while(it1.hasNext()) {

            ClientsProducers c = it1.next();

            if(c.getCode().equalsIgnoreCase("CT5")) {
                hub = c;
            }
        }

        System.out.println(hub);

        for(ClientsProducers p: producersToFilter) {
            Integer sPathResults = Algorithms.shortestPath(clpGraph, hub, p, Integer::compare, Integer::sum, 0, path);
            System.out.println(path);
            System.out.println(p);
            System.out.println(sPathResults);
            producerDistance.put(p, sPathResults);

            path.clear();
        }

        List<Integer> distances = new ArrayList<>(producerDistance.values());
        Collections.sort(distances);
        int i = 0;

        for(Map.Entry<ClientsProducers, Integer> pD : producerDistance.entrySet()) {
            if(pD.getValue().equals(distances.get(i))) {
                closestP.add(pD.getKey());
            }
            i++;
        }

        //System.out.println(producerDistance);
        //System.out.println(distances);
        //System.out.println(closestP);

        return closestP;
    }
}

