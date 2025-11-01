package graph.dagsp;

import graph.common.Graph;
import graph.common.Metrics;
import graph.topo.TopoKahn;

import java.util.*;

public class DagShortestPath {
    private final Graph g;

    public DagShortestPath(Graph g) {
        this.g = g;
    }

    public static class Result {
        public final Map<Integer, Double> dist;
        public final Map<Integer, Integer> parent;
        public Result(Map<Integer, Double> dist, Map<Integer, Integer> parent) {
            this.dist = dist;
            this.parent = parent;
        }

        public List<Integer> buildPath(int target) {
            if (!dist.containsKey(target) || dist.get(target) == Double.POSITIVE_INFINITY) {
                return Collections.emptyList();
            }
            List<Integer> path = new ArrayList<>();
            Integer cur = target;
            while (cur != null) {
                path.add(cur);
                cur = parent.get(cur);
            }
            Collections.reverse(path);
            return path;
        }
    }

    public Result shortestPaths(int src) {
        return shortestPaths(src, new Metrics());
    }

    public Result shortestPaths(int src, Metrics m) {
        List<Integer> order = new TopoKahn(g).sort();
        Map<Integer, Double> dist = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();

        for (int i = 0; i < g.size(); i++) {
            dist.put(i, Double.POSITIVE_INFINITY);
            parent.put(i, null);
        }
        dist.put(src, 0.0);

        for (int u : order) {
            double du = dist.get(u);
            if (du == Double.POSITIVE_INFINITY) continue;
            for (Graph.Edge e : g.adj.get(u)) {
                double nd = du + e.w;
                if (nd < dist.get(e.v)) {
                    dist.put(e.v, nd);
                    parent.put(e.v, u);
                    m.relax++;
                }
            }
        }
        return new Result(dist, parent);
    }
}
