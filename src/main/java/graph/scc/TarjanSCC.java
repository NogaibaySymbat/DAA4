package graph.scc;

import graph.common.Graph;
import graph.common.Metrics;

import java.util.*;

public class TarjanSCC {

    public static class Result {
        public final List<List<Integer>> components;
        public final int[] compId;

        public Result(List<List<Integer>> components, int[] compId) {
            this.components = components;
            this.compId = compId;
        }
    }

    private final Graph g;
    private final int n;

    private int time = 0;
    private final int[] disc;
    private final int[] low;
    private final boolean[] onStack;
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final List<List<Integer>> components = new ArrayList<>();
    private final int[] compId;

    public TarjanSCC(Graph g) {
        this.g = g;
        this.n = g.size();
        this.disc = new int[n];
        this.low = new int[n];
        this.onStack = new boolean[n];
        this.compId = new int[n];
        Arrays.fill(disc, -1);
    }

    public Result run() {
        return run(new Metrics());
    }

    public Result run(Metrics m) {
        for (int i = 0; i < n; i++) {
            if (disc[i] == -1) {
                dfs(i, m);
            }
        }
        return new Result(components, compId);
    }

    private void dfs(int u, Metrics m) {
        disc[u] = low[u] = ++time;
        stack.push(u);
        onStack[u] = true;
        m.dfs++;

        for (Graph.Edge e : g.adj.get(u)) {
            int v = e.v;
            if (disc[v] == -1) {
                dfs(v, m);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<>();
            int x;
            do {
                x = stack.pop();
                onStack[x] = false;
                compId[x] = components.size();
                comp.add(x);
            } while (x != u);
            components.add(comp);
        }
    }
}
