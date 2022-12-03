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
    @Override
    public void run() {

        KruskalMST.kruskal();
    }
}