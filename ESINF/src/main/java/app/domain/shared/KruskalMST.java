package app.domain.shared;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.graph.Algorithms;
import app.graph.Edge;
import app.graph.map.MapGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class KruskalMST {

    static MapGraph<ClientsProducers, Integer> clpGraph = App.getInstance().getCompany().getClientsProducersGraph();
    static MapGraph<ClientsProducers, Integer> clpGraphClone = clpGraph.clone();
    static MapGraph<ClientsProducers, Integer> mst = new MapGraph<>(false);
    static ArrayList<Edge<ClientsProducers, Integer>> edgeArrayList = new ArrayList<>();
    static Iterator<ClientsProducers> cpIt = App.getInstance().getCompany().getClientsProducersGraph().vertices().iterator();


    public static MapGraph<ClientsProducers, Integer> kruskal() {

        parseGraph(clpGraphClone);

        ArrayList<ClientsProducers> clpcV = clpGraphClone.vertices();

        for (ClientsProducers c :clpcV) {
            mst.addVertex(c);
        }

        Collection<Edge<ClientsProducers, Integer>> clpcE = clpGraphClone.edges();

        edgeArrayList.addAll(clpcE);
        Edge<ClientsProducers, Integer> edgebuffer;

        // System.out.println(edgeArrayList);

        //sorting array
        for(int i = 0; i < edgeArrayList.size(); i++) {
            for(int j = 1; j < edgeArrayList.size()-1; j++) {
                if(compareEdges(edgeArrayList.get(i), edgeArrayList.get(j)) < 0){
                    edgebuffer = edgeArrayList.get(i);
                    edgeArrayList.set(i,edgeArrayList.get(j));
                    edgeArrayList.set(j,edgebuffer);
                }
            }
        }

        //System.out.println(edgeArrayList);

        //removing duplicates
        edgeArrayList.remove(0);

        Iterator<Edge<ClientsProducers, Integer>> edgeIt = edgeArrayList.iterator();
        Edge<ClientsProducers, Integer> previous = null;
        Edge<ClientsProducers, Integer> current;

        //System.out.println(edgeArrayList.size());

        if (edgeIt.hasNext())
        {
            previous = edgeIt.next();
        }

        while (edgeIt.hasNext())
        {
            current = edgeIt.next();
            // Process previous and current here.
            if(current.getVOrig().equals(previous.getVDest()) && current.getVDest().equals(previous.getVOrig())) {
                edgeIt.remove();
            }
            // End of loop, after processing.  Maintain previous reference.
            previous = current;
        }
        //System.out.println(edgeArrayList.size());

        //System.out.println(edgeArrayList);
        LinkedList<ClientsProducers> connectedVerts = new LinkedList<>();

        for (Edge<ClientsProducers, Integer> e : edgeArrayList) {
            connectedVerts = Algorithms.DepthFirstSearch(mst, e.getVOrig());
            if(!connectedVerts.contains(e.getVDest())){
                mst.addEdge(e.getVOrig(), e.getVDest(), e.getWeight());
            }
        }

        return mst;
    }

    private static void parseGraph (MapGraph<ClientsProducers, Integer> clpGraphClone) {

        while(cpIt.hasNext()) {
            ClientsProducers c = cpIt.next();

            if (c.getType().equalsIgnoreCase("Empresa")) {

                clpGraphClone.removeVertex(c);

            }
        }
    }
    public static int compareEdges(Edge<ClientsProducers, Integer> e1, Edge<ClientsProducers, Integer> e2) {

        return e1.getWeight().compareTo(e2.getWeight());
    }



}