package dynamicGraphs.test;

import dynamicGraphs.diff.Diff;
import dynamicGraphs.diff.DiffNotApplicableException;
import dynamicGraphs.diff.Series;
import dynamicGraphs.diff.generator.RandomDiff;
import dynamicGraphs.graph.Graph;
import dynamicGraphs.graph.generator.RandomGraph;
import dynamicGraphs.metrics.Metric;
import dynamicGraphs.metrics.triangles.open.OpenTriangleCCBasic;
import dynamicGraphs.metrics.triangles.open.OpenTriangleCounting;
import dynamicGraphs.util.ArrayUtils;
import dynamicGraphs.util.Stats;

public class TestSeries {

	/**
	 * @param args
	 * @throws DiffNotApplicableException
	 */
	public static void main(String[] args) throws DiffNotApplicableException {
		Stats stats = new Stats();

		int nodes = 500;
		int edges = 10 * nodes;
		int runs = 3;

		if (args.length > 0) {
			nodes = Integer.parseInt(args[0]);
			edges = Integer.parseInt(args[1]) * nodes;
			runs = Integer.parseInt(args[2]);
		}

		int add = edges;
		int[] steps = new int[] { 1, 2, 5, 20, 50, 100, 200, 500 };
		boolean incremental = true;

		System.out.println("nodes: " + nodes);
		System.out.println("edges: " + edges);
		System.out.println("runs: " + runs);
		System.out.println("add: " + add);
		System.out.println("steps: " + ArrayUtils.toString(steps));

		for (int s : steps) {
			runs(nodes, edges, s, add / s, runs, incremental);
			System.out.println("\n");
		}

		stats.end();
	}

	public static void runs(int nodes, int edges, int steps, int add, int runs,
			boolean incremental) throws DiffNotApplicableException {
		Series[] s = new Series[runs];
		for (int i = 0; i < runs; i++) {
			System.gc();
			s[i] = run(nodes, edges, steps, add, incremental);
		}
		Series.printStats(s);
	}

	public static Series run(int nodes, int edges, int steps, int add,
			boolean incremental) throws DiffNotApplicableException {
		Graph g = RandomGraph.generate(nodes, edges, true);
		Diff[] d = getDiffs(g, steps, add);
		OpenTriangleCounting otc = new OpenTriangleCounting(g);
		OpenTriangleCCBasic basic = new OpenTriangleCCBasic(g);
		Metric[] metrics = new Metric[] { otc, basic };

		Series s = new Series(g, d, metrics);
		s.processIncremental();
		return s;
	}

	public static Diff[] getDiffs(Graph g, int diffs, int add) {
		Diff[] d = new Diff[diffs];
		for (int i = 0; i < d.length; i++) {
			d[i] = RandomDiff.generate(g, add, 0, true, i, i + 1);
		}
		return d;
	}

}
