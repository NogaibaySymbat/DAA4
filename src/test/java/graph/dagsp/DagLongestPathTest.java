package graph.dagsp;

import graph.common.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DagLongestPathTest {

    @Test
    public void testLongest() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);
        g.addEdge(2, 3, 4);

        DagLongestPath lp = new DagLongestPath(g);
        DagLongestPath.Result res = lp.longestPaths(0);

        assertEquals(0.0, res.dist.get(0));
        assertEquals(2.0, res.dist.get(1));
        assertEquals(5.0, res.dist.get(2));
        assertEquals(9.0, res.dist.get(3));

        assertEquals(java.util.List.of(0, 1, 2, 3), res.buildPath(3));
    }
}
