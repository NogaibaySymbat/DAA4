package graph.topo;

import graph.common.Graph;
import graph.common.Metrics;

import java.util.*;

public class TopoKahn {
    private final Graph g;

    public TopoKahn(Graph g) {
        this.g = g;
    }

    public List<Integer> sort() {
        return sort(new Metrics());
    }

    public List<Integer> sort(Metrics m) {
        int n = g.size();
        int[] indeg = new int[n];

        for (int u = 0; u < n; u++) {
            for (Graph.Edge e : g.adj.get(u)) {
                indeg[e.v]++;
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) q.add(i);
        }

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            m.dfs++;
            for (Graph.Edge e : g.adj.get(u)) {
                indeg[e.v]--;
                if (indeg[e.v] == 0) q.add(e.v);
            }
        }
        return order;
    }
}
