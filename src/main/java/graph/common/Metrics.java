package graph.common;

public class Metrics {
    public long dfs = 0;
    public long relax = 0;

    private long start;
    private long end;

    public void start() {
        start = System.nanoTime();
    }

    public void stop() {
        end = System.nanoTime();
    }

    public void print(String name) {
        double ms = (end - start) / 1_000_000.0;
        System.out.printf("%-15s | time: %7.3f ms | dfs: %-4d | relax: %-4d%n",
                name, ms, dfs, relax);
    }
}
