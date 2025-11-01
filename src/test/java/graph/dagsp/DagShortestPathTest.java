package graph.dagsp;

import graph.common.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DagShortestPathTest {

    @Test
    public void testShortest() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        g.addEdge(0, 3, 5);

        DagShortestPath sp = new DagShortestPath(g);
        DagShortestPath.Result res = sp.shortestPaths(0);

        assertEquals(0.0, res.dist.get(0));
        assertEquals(1.0, res.dist.get(1));
        assertEquals(3.0, res.dist.get(2));
        assertEquals(5.0, res.dist.get(3));

        assertEquals(java.util.List.of(0, 1, 2), res.buildPath(2));
    }
}
