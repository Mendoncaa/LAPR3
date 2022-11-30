package test;

import app.domain.model.ClientsProducers;
import app.domain.model.Path;
import app.domain.shared.AverageComparator;
import app.graph.Algorithms;
import app.graph.Graph;
import app.graph.map.MapGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

public class AverageComparatorTest {

    private Graph<ClientsProducers, Integer> completeMap = new MapGraph<>(false);

    @BeforeEach
    public void setUp() {

        ClientsProducers c1 = new ClientsProducers("CT1", 40.6389f, -8.6553f, "C1");
        ClientsProducers c2 = new ClientsProducers("CT2", 38.0333f, -7.8833f, "C2");
        ClientsProducers e1 = new ClientsProducers("CT3", 38.5243f, -8.8926f, "E1");
        ClientsProducers e2 = new ClientsProducers("CT4", 39.3167f, -7.4167f, "E2");


        completeMap.addVertex(c1);
        completeMap.addVertex(c2);
        completeMap.addVertex(e1);
        completeMap.addVertex(e2);


        completeMap.addEdge(c1, c2, 2);
        completeMap.addEdge(c2, e1, 1);
        completeMap.addEdge(e1, e2, 3);
        completeMap.addEdge(e2, c2, 2);

    }

    @Test
    public void AverageComparatorTest() {

        ArrayList<LinkedList<ClientsProducers>> paths = new ArrayList<>();
        ArrayList<Integer> dists = new ArrayList<>();
        Algorithms.shortestPaths(completeMap, completeMap.vertices().get(0), Integer::compare, Integer::sum, 0, paths, dists);
        ArrayList<LinkedList<ClientsProducers>> paths2 = new ArrayList<>();
        ArrayList<Integer> dists2 = new ArrayList<>();
        Algorithms.shortestPaths(completeMap, completeMap.vertices().get(1), Integer::compare, Integer::sum, 0, paths2, dists2);

        Path p1 = new Path(completeMap.vertices().get(0), paths, dists);
        Path p2 = new Path(completeMap.vertices().get(1), paths2, dists2);

        Assertions.assertEquals(0, new AverageComparator().compare(p1, p2));
    }
}
