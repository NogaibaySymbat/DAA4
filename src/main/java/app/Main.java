package app;

import com.google.gson.*;
import graph.common.Graph;
import graph.common.Metrics;
import graph.scc.TarjanSCC;
import graph.scc.CondensationBuilder;
import graph.topo.TopoKahn;
import graph.dagsp.DagShortestPath;
import graph.dagsp.DagLongestPath;

import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class Main {
    private static final String[] DATASETS = {
            "tasks.json",
            "small_1.json", "small_2.json", "small_3.json",
            "medium_1.json", "medium_2.json", "medium_3.json",
            "large_1.json", "large_2.json", "large_3.json"
    };

    public static void main(String[] args) {
        for (String fileName : DATASETS) {
            runForFile(fileName);
        }
    }

    private static void runForFile(String fileName) {
        System.out.println("--------------------------------------");
        System.out.println("Dataset: " + fileName);
        System.out.println("--------------------------------------");
        try {
            JsonObject root = JsonParser.parseReader(
                    new FileReader("src/main/resources/data/" + fileName)
            ).getAsJsonObject();

            boolean directed = root.get("directed").getAsBoolean();
            int n = root.get("n").getAsInt();
            int source = root.get("source").getAsInt();
            JsonArray edges = root.getAsJsonArray("edges");

            Graph g = new Graph(n, directed);
            for (JsonElement el : edges) {
                JsonObject e = el.getAsJsonObject();
                int u = e.get("u").getAsInt();
                int v = e.get("v").getAsInt();
                double w = e.get("w").getAsDouble();
                g.addEdge(u, v, w);
            }

            System.out.println("Loaded graph: n=" + n + ", edges=" + edges.size());


            Metrics mScc = new Metrics();
            mScc.start();
            TarjanSCC tarjan = new TarjanSCC(g);
            TarjanSCC.Result sccRes = tarjan.run(mScc);
            mScc.stop();

            System.out.println("\n--- SCC components ---");
            for (int i = 0; i < sccRes.components.size(); i++) {
                List<Integer> comp = sccRes.components.get(i);
                System.out.println("C" + i + " = " + comp + " (size=" + comp.size() + ")");
            }
            mScc.print("Tarjan SCC");


            Graph dag = CondensationBuilder.build(g, sccRes);
            System.out.println("\nCondensation DAG nodes = " + dag.size());


            Metrics mTopo = new Metrics();
            mTopo.start();
            TopoKahn topo = new TopoKahn(dag);
            List<Integer> order = topo.sort(mTopo);
            mTopo.stop();

            System.out.println("\n--- Topological order of components ---");
            System.out.println(order);
            mTopo.print("Topo Kahn");


            System.out.println("\n--- Derived order of original tasks ---");
            int[] compId = sccRes.compId;
            for (int comp : order) {
                for (int v = 0; v < n; v++) {
                    if (compId[v] == comp) {
                        System.out.print(v + " ");
                    }
                }
            }
            System.out.println();


            Metrics mSp = new Metrics();
            mSp.start();
            DagShortestPath sp = new DagShortestPath(dag);
            DagShortestPath.Result spRes = sp.shortestPaths(0, mSp);
            mSp.stop();

            System.out.println("\n--- Shortest paths from component 0 ---");
            System.out.println(spRes.dist);
            int lastNode = dag.size() - 1;
            System.out.println("Shortest path 0 -> " + lastNode + ": " + spRes.buildPath(lastNode));
            mSp.print("DAG shortest");

            Metrics mLp = new Metrics();
            mLp.start();
            DagLongestPath lp = new DagLongestPath(dag);
            DagLongestPath.Result lpRes = lp.longestPaths(0, mLp);
            mLp.stop();

            double best = Double.NEGATIVE_INFINITY;
            int bestNode = 0;
            for (Map.Entry<Integer, Double> e : lpRes.dist.entrySet()) {
                if (e.getValue() > best) {
                    best = e.getValue();
                    bestNode = e.getKey();
                }
            }

            System.out.println("\n--- Longest (critical) paths from component 0 ---");
            System.out.println(lpRes.dist);
            System.out.println("Critical length = " + best);
            System.out.println("Critical path   = " + lpRes.buildPath(bestNode));
            mLp.print("DAG longest");

            System.out.println("\nDone: " + fileName);

        } catch (Exception e) {
            System.out.println("Error on " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
