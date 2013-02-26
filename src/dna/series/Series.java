package dna.series;

import java.io.IOException;
import java.util.HashMap;

import dna.diff.Diff;
import dna.diff.DiffNotApplicableException;
import dna.diff.generator.DiffGenerator;
import dna.graph.Edge;
import dna.graph.Graph;
import dna.graph.generator.GraphGenerator;
import dna.io.Path;
import dna.metrics.Metric;
import dna.util.Rand;
import dna.util.Timer;

public class Series {
	private Graph g;

	private GraphGenerator gg;

	private DiffGenerator dg;

	private Metric[] metrics;

	private String path;

	public Series(GraphGenerator gg, DiffGenerator dg, Metric[] metrics,
			String path) {
		this.g = null;
		this.gg = gg;
		this.dg = dg;
		this.metrics = metrics;
		this.path = path;
	}

	public SeriesData generate(int runs, int diffs) throws IOException,
			DiffNotApplicableException {

		SeriesData sd = new SeriesData(this.path, runs);

		// generate all runs
		for (int r = 0; r < runs; r++) {
			sd.addRun(this.generateRun(r, diffs));
		}

		return sd;
	}

	public RunData generateRun(int run, int diffs) throws IOException,
			DiffNotApplicableException {

		RunData rd = new RunData(run, diffs + 1);

		// generate initial data
		DiffData initialData = this.generateInitialData();
		rd.addDiff(initialData);
		initialData.write(Path.getPath(this.path, rd, initialData));

		// generate diff data
		for (int i = 0; i < diffs; i++) {
			DiffData diffData = this.generateNextDiff();
			rd.addDiff(diffData);
			diffData.write(Path.getPath(this.path, rd, diffData));
		}

		return rd;
	}

	public DiffData generateInitialData() {

		Timer totalTimer = new Timer("total");

		long seed = System.currentTimeMillis();
		Rand.init(seed);

		// generate graph
		Timer graphGenerationTimer = new Timer("graphGeneration");
		this.g = this.gg.generate();
		graphGenerationTimer.end();
		for (Metric m : this.metrics) {
			m.setGraph(this.g);
		}

		// initialize data
		DiffData initialData = new DiffData(this.g.getTimestamp(), 0, 4,
				this.metrics.length, this.metrics.length);

		// initial computation of all metrics
		Timer allMetricsTimer = new Timer("metrics");
		for (Metric m : metrics) {
			Timer metricTimer = new Timer(m.getName());
			m.compute();
			metricTimer.end();
			initialData.addMetric(m.getData());
			initialData.addMetricRuntime(metricTimer.getRuntime());
		}
		allMetricsTimer.end();

		totalTimer.end();

		// add general runtimes
		initialData.addGeneralRuntime(totalTimer.getRuntime());
		initialData.addGeneralRuntime(graphGenerationTimer.getRuntime());
		initialData.addGeneralRuntime(allMetricsTimer.getRuntime());
		Series.addSummaryRuntimes(initialData);

		// add values
		initialData.addValue(new Value("randomSeed", seed));

		return initialData;
	}

	public DiffData generateNextDiff() throws DiffNotApplicableException {

		long seed = System.currentTimeMillis();
		Rand.init(seed);

		int addedEdges = 0;
		int removedEdges = 0;

		Timer totalTimer = new Timer("total");

		Timer diffGenerationTimer = new Timer("diffGeneration");
		Diff d = dg.generate(g);
		diffGenerationTimer.end();

		DiffData diffData = new DiffData(d.getTo(), 5, 5, metrics.length,
				metrics.length);

		Timer graphUpdateTimer = new Timer("graphUpdate");

		// init metric timers
		HashMap<Metric, Timer> timer = new HashMap<Metric, Timer>();
		for (Metric m : this.metrics) {
			Timer t = new Timer(m.getName());
			t.end();
			timer.put(m, t);
		}
		Timer metricsTotal = new Timer("metrics");
		metricsTotal.end();

		// apply before diff
		for (Metric m : this.metrics) {
			if (m.isAppliedBeforeDiff()) {
				timer.get(m).restart();
				metricsTotal.restart();
				m.applyBeforeDiff(d);
				timer.get(m).end();
				metricsTotal.end();
			}
		}

		// remove edges
		for (Edge e : d.getRemovedEdges()) {
			graphUpdateTimer.restart();
			boolean removed = this.g.removeEdge(e);
			graphUpdateTimer.end();
			if (!removed) {
				continue;
			}
			removedEdges++;
			// apply after edge
			for (Metric m : this.metrics) {
				if (m.isAppliedAfterEdge()) {
					timer.get(m).restart();
					metricsTotal.restart();
					m.applyAfterEdgeRemoval(d, e);
					timer.get(m).end();
					metricsTotal.end();
				}
			}
		}

		// add edges
		for (Edge e : d.getAddedEdges()) {
			graphUpdateTimer.restart();
			boolean added = this.g.addEdge(e);
			graphUpdateTimer.end();
			if (!added) {
				continue;
			}
			addedEdges++;
			// apply after edge
			for (Metric m : this.metrics) {
				if (m.isAppliedAfterEdge()) {
					timer.get(m).restart();
					metricsTotal.restart();
					m.applyAfterEdgeAddition(d, e);
					timer.get(m).end();
					metricsTotal.end();
				}
			}
		}

		this.g.setTimestamp(d.getTo());

		// apply after diff
		for (Metric m : this.metrics) {
			if (m.isAppliedAfterDiff()) {
				timer.get(m).restart();
				metricsTotal.restart();
				m.applyAfterDiff(d);
				timer.get(m).end();
				metricsTotal.end();
			}
		}

		// compute / cleanup
		for (Metric m : this.metrics) {
			timer.get(m).restart();
			metricsTotal.restart();
			if (m.isComputed()) {
				m.compute();
			} else {
				m.cleanupApplication();
			}
			timer.get(m).end();
			metricsTotal.end();
		}

		totalTimer.end();

		// add values
		diffData.addValue(new Value("edgesToAdd", d.getAddedEdges().size()));
		diffData.addValue(new Value("addedEdges", addedEdges));
		diffData.addValue(new Value("edgesToRemove", d.getRemovedEdges().size()));
		diffData.addValue(new Value("removedEdges", removedEdges));
		diffData.addValue(new Value("randomSeed", seed));

		// add metric data
		for (Metric m : this.metrics) {
			diffData.addMetric(m.getData());
		}

		// add metric runtimes
		for (Metric m : this.metrics) {
			diffData.addMetricRuntime(timer.get(m).getRuntime());
		}

		// add general runtimes
		diffData.addGeneralRuntime(totalTimer.getRuntime());
		diffData.addGeneralRuntime(diffGenerationTimer.getRuntime());
		diffData.addGeneralRuntime(graphUpdateTimer.getRuntime());
		diffData.addGeneralRuntime(metricsTotal.getRuntime());
		Series.addSummaryRuntimes(diffData);

		return diffData;
	}

	private static void addSummaryRuntimes(DiffData diffData) {
		long total = diffData.getGeneralRuntime("total").getRuntime();
		long metrics = diffData.getGeneralRuntime("metrics").getRuntime();
		long sum = Series.sumRuntimes(diffData) - total - metrics;
		long overhead = total - sum;
		diffData.addGeneralRuntime(new RunTime("sum", sum));
		diffData.addGeneralRuntime(new RunTime("overhead", overhead));
	}

	private static long sumRuntimes(DiffData diffData) {
		long sum = 0;
		for (RunTime rt : diffData.getGeneralRuntimes()) {
			sum += rt.getRuntime();
		}
		for (RunTime rt : diffData.getMetricRuntimes()) {
			sum += rt.getRuntime();
		}
		return sum;
	}
}
