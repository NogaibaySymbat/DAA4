package graph.scc;

import graph.common.Graph;

public class CondensationBuilder {
    public static Graph build(Graph g, TarjanSCC.Result sccRes) {
        int compCount = sccRes.components.size();
        Graph dag = new Graph(compCount, true);

        int n = g.size();
        for (int u = 0; u < n; u++) {
            int cu = sccRes.compId[u];
            for (graph.common.Graph.Edge e : g.adj.get(u)) {
                int cv = sccRes.compId[e.v];
                if (cu != cv) {
                    dag.addEdge(cu, cv, e.w);
                }
            }
        }

        return dag;
    }
}
