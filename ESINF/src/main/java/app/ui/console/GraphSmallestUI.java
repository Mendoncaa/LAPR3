package app.ui.console;

import app.controller.App;
import app.domain.model.ClientsProducers;
import app.domain.shared.DistanceComparator;
import app.domain.shared.KruskalMST;
import app.graph.Algorithms;
import app.graph.Edge;
import app.graph.map.MapGraph;
import app.graph.matrix.MatrixGraph;

import java.util.*;

public class GraphSmallestUI implements Runnable{

    static MapGraph<ClientsProducers, Integer> mst = new MapGraph<>(false);

    @Override
    public void run() {

        mst = KruskalMST.kruskal();
        printFormatMST(mst);

    }

    private static void printFormatMST(MapGraph<ClientsProducers, Integer> mst) {

        ArrayList<ClientsProducers> cp = mst.vertices();
        ArrayList<String> cpPrint = new ArrayList<>();
        Collection<Edge<ClientsProducers, Integer>> ed = mst.edges();

        Iterator<Edge<ClientsProducers, Integer>> edgeIterator = ed.iterator();

        for(int i = 0; i < cp.size(); i++) {
            cpPrint.add(cp.get(i).getCode());
        }

        System.out.println("Vertexes:" + cpPrint + "\n");
        System.out.println(mst);
       /* System.out.println("Edges:\n");

        while(edgeIterator.hasNext()) {
            System.out.println(edgeIterator.next().getVOrig().getCode() + "==>" + edgeIterator.next().getVDest().getCode());
        }
*/
    }
}