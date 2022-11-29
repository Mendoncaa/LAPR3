package test;

import app.domain.model.ClientsProducers;
import app.domain.model.Path;
import app.domain.shared.AverageComparator;
import app.domain.shared.ClosestPointsCheck;
import app.graph.Graph;
import app.graph.map.MapGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class ClosestPointsTest {

    Graph<ClientsProducers, Integer> completeMap = new MapGraph<>(false);

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
    public void testClosestPoints() {

        ArrayList<Path> closerPoints = ClosestPointsCheck.getCloserPoints(completeMap);
        Set<Path> closerPointsSet = new TreeSet<>(new AverageComparator());
        closerPointsSet.addAll(closerPoints);

        Iterator<Path> itr = closerPointsSet.iterator();
        Path path = itr.next();
        Path path2 = itr.next();
        Assertions.assertEquals(path.getEntity().getCode(), "E1");
        Assertions.assertEquals(path2.getEntity().getCode(), "E2");
    }

    @Test
    public void testClosestHub() {
        ClientsProducers test = ClosestPointsCheck.getClosestHub(completeMap.vertices().get(0), completeMap);
        Assertions.assertEquals(test.getCode(), "E1");
    }
}
