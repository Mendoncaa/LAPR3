package app.ui.console;

import app.domain.model.ClientsProducers;
import app.domain.shared.FilesReaderApp;
import app.domain.shared.KruskalMST;
import app.graph.Edge;
import app.graph.map.MapGraph;

import java.util.*;

public class GraphSmallestUI implements Runnable{

    static MapGraph<ClientsProducers, Integer> mst = new MapGraph<>(false);

    @Override
    public void run() {
        if(!FilesReaderApp.graphImported) {
            System.out.println("Graph not yet imported!");
        }else {
            mst = KruskalMST.kruskal();
            printFormatMST(mst);
        }
    }

    private static void printFormatMST(MapGraph<ClientsProducers, Integer> mst) {

        ArrayList<ClientsProducers> cp = mst.vertices();
        ArrayList<String> cpPrint = new ArrayList<>();
        Collection<Edge<ClientsProducers, Integer>> ed = mst.edges();

        Iterator<Edge<ClientsProducers, Integer>> edgeIterator = ed.iterator();

        for (int i = 0; i < cp.size(); i++) {
            cpPrint.add(cp.get(i).getCode());
        }

        System.out.println("Vertexes:" + cpPrint + "\n");
        //System.out.println(mst);
        System.out.println("Edges:\n");

        while (edgeIterator.hasNext()) {
            Edge<ClientsProducers, Integer> edCurrent = edgeIterator.next();
            System.out.println(edCurrent.getVOrig().getCode() + "==>" + edCurrent.getVDest().getCode());
        }
    }
}