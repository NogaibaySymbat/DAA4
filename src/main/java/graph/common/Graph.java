package graph.common;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int n;
    private final boolean directed;
    public final List<List<Edge>> adj;

    public static class Edge {
        public final int v;
        public final double w;
        public Edge(int v, double w) {
            this.v = v;
            this.w = w;
        }
    }

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, double w) {
        adj.get(u).add(new Edge(v, w));
        if (!directed) {
            adj.get(v).add(new Edge(u, w));
        }
    }

    public int size() {
        return n;
    }
}
