package graph.topo;

import graph.common.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TopoKahnTest {

    @Test
    public void testSimpleDag() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(0, 3, 1);

        TopoKahn topo = new TopoKahn(g);
        List<Integer> order = topo.sort();

        assertEquals(4, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(0) < order.indexOf(3));
    }
}
