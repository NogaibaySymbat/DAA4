package graph.scc;

import graph.common.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCTest {

    @Test
    public void testSimpleCycle() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);

        TarjanSCC scc = new TarjanSCC(g);
        TarjanSCC.Result res = scc.run();

        assertEquals(1, res.components.size());
        assertTrue(res.components.get(0).contains(0));
        assertTrue(res.components.get(0).contains(1));
        assertTrue(res.components.get(0).contains(2));
    }

    @Test
    public void testDisconnected() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(2, 3, 1);

        TarjanSCC scc = new TarjanSCC(g);
        TarjanSCC.Result res = scc.run();

        assertTrue(res.components.size() >= 2);
    }
}
