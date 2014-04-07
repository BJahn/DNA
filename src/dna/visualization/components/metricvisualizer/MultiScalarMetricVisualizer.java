package dna.visualization.components.metricvisualizer;

import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.gui.chart.traces.painters.TracePainterDisc;
import info.monitorenter.gui.chart.traces.painters.TracePainterFill;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import dna.series.data.BatchData;
import dna.series.data.Distribution;
import dna.series.data.DistributionDouble;
import dna.series.data.DistributionInt;
import dna.series.data.DistributionLong;
import dna.series.data.MetricData;
import dna.series.data.NodeValueList;
import dna.util.Config;
import dna.visualization.MainDisplay;

public class MultiScalarMetricVisualizer extends Visualizer {
	// fonts
	private Font defaultFontBorders = MainDisplay.defaultFontBorders;

	// available values and traces
	private ArrayList<String> availableValues;
	private HashMap<String, ITrace2D> traces;

	// constructor
	public MultiScalarMetricVisualizer() {
		// initialization
		this.traces = new HashMap<String, ITrace2D>();
		this.availableValues = new ArrayList<String>();
		
		// set title and border of the metric visualizer
		TitledBorder title = BorderFactory
				.createTitledBorder("Multi-Scalar Visualizer");
		title.setBorder(BorderFactory
				.createEtchedBorder((EtchedBorder.LOWERED)));
		title.setTitleFont(this.defaultFontBorders);
		this.setBorder(title);

		// add menu bar
		super.addMenuBar(new Dimension(this.defaultMenuBarSize), true, false,
				true, true, true);

		// add coordinate parsing to mouseover on chart
		this.chart.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (chart.getPointFinder().getNearestPoint(e, chart) != null) {
					ITracePoint2D tempPointFinder = chart.getPointFinder()
							.getNearestPoint(e, chart);
					menuBar.updateCoordsPanel(
							(int) Math.floor(tempPointFinder.getX()),
							tempPointFinder.getY());
				}
			}

			public void mouseDragged(MouseEvent e) {
			}

		});
	}

	/** initializes the data with the first batch **/
	public void initData(BatchData b) {
		this.minTimestamp = b.getTimestamp();
		this.maxTimestamp = b.getTimestamp();
		this.minShownTimestamp = b.getTimestamp();
		this.maxShownTimestamp = this.minShownTimestamp + 10;

		for (ITrace2D t : this.chart.getTraces()) {
			t.removeAllPoints();
		}

		for (String metric : b.getMetrics().getNames()) {
			for (String dist : b.getMetrics().get(metric).getDistributions()
					.getNames()) {
				System.out.println(metric + " " + dist);
				this.availableValues.add(metric + "." + dist);
			}
		}

		for (String metric : b.getMetrics().getNames()) {
			for (String nvl : b.getMetrics().get(metric).getNodeValues()
					.getNames()) {
				System.out.println(metric + " " + nvl);
				this.availableValues.add(metric + "." + nvl);
			}
		}

		// init addbox
		String[] tempValues = this.availableValues
				.toArray(new String[this.availableValues.size()]);
		tempValues = this.gatherValues(b);
		this.toggleYAxisVisibility();
		this.legend.updateAddBox(tempValues);
		this.validate();
	}

	public void updateData(BatchData b) {
		long timestamp = b.getTimestamp();
		this.clearPoints();
		System.out.println("updating: " + timestamp);
		System.out.println("metrics: " + b.getMetrics().size());
		/*
		 * for (String metric : b.getMetrics().getNames()) { for (String value :
		 * b.getMetrics().get(metric).getValues() .getNames()) { if
		 * (this.traces.containsKey(metric + "." + value)) { String tempName =
		 * metric + "." + value; double tempValue =
		 * b.getMetrics().get(metric).getValues() .get(value).getValue();
		 * this.traces.get(tempName).addPoint(timestampDouble, tempValue);
		 * this.legend.updateItem(tempName, tempValue); } } }
		 */
		for (String s : this.traces.keySet()) {
			System.out.println(s);
		}

		for (String metric : b.getMetrics().getNames()) {
			for (String dist : b.getMetrics().get(metric).getDistributions()
					.getNames()) {
				if (this.traces.containsKey(metric + "." + dist)) {
					Distribution tempDist = b.getMetrics().get(metric)
							.getDistributions().get(dist);
					String tempName = metric + "." + dist;

					if (tempDist instanceof DistributionDouble) {
						double[] tempValues = ((DistributionDouble) b
								.getMetrics().get(metric).getDistributions()
								.get(dist)).getDoubleValues();
						this.addPoints(tempName, tempValues);
					}
					if (tempDist instanceof DistributionInt) {
						int[] tempValues = ((DistributionInt) b.getMetrics()
								.get(metric).getDistributions().get(dist))
								.getIntValues();
						this.addPoints(tempName, tempValues);
					}
					if (tempDist instanceof DistributionLong) {
						long[] tempValues = ((DistributionLong) b.getMetrics()
								.get(metric).getDistributions().get(dist))
								.getLongValues();
						this.addPoints(tempName, tempValues);
					}

					this.legend.updateItem(tempName, 0.0);
				}
			}
			for (String nvl : b.getMetrics().get(metric).getNodeValues()
					.getNames()) {
				if (this.traces.containsKey(metric + "." + nvl))
					this.addPoints(metric + "." + nvl,
							b.getMetrics().get(metric).getNodeValues().get(nvl)
									.getValues());
			}
		}
		updateXTicks();
	}

	/** adds points for a given trace to the chart **/
	private void addPoints(String name, double[] values) {
		ITrace2D tempTrace = this.traces.get(name);
		System.out.println("addPoints: " + values.length);
		for (int i = 0; i < values.length; i++) {
			if (values[i] != Double.NaN)
				tempTrace.addPoint(i, values[i]);
		}
		if (values.length - 1 > this.maxShownTimestamp)
			this.maxShownTimestamp = values.length - 1;
		if (values.length - 1 > this.maxTimestamp)
			this.maxTimestamp = values.length - 1;
	}

	/** adds points for a given trace to the chart **/
	private void addPoints(String name, int[] values) {
		ITrace2D tempTrace = this.traces.get(name);
		System.out.println("addPoints: " + values.length);
		for (int i = 0; i < values.length; i++) {
			tempTrace.addPoint(i, values[i]);
		}
		if (values.length - 1 > this.maxShownTimestamp)
			this.maxShownTimestamp = values.length - 1;
		if (values.length - 1 > this.maxTimestamp)
			this.maxTimestamp = values.length - 1;
	}

	/** adds points for a given trace to the chart **/
	private void addPoints(String name, long[] values) {
		ITrace2D tempTrace = this.traces.get(name);
		System.out.println("addPoints: " + values.length);
		for (int i = 0; i < values.length; i++) {
			tempTrace.addPoint(i, values[i]);
		}
		if (values.length - 1 > this.maxShownTimestamp)
			this.maxShownTimestamp = values.length - 1;
		if (values.length - 1 > this.maxTimestamp)
			this.maxTimestamp = values.length - 1;
	}

	/** resets the metric visualizer **/
	public void clearPoints() {
		this.minShownTimestamp = 0;
		this.maxShownTimestamp = 10;
		for (String trace : this.traces.keySet()) {
			this.traces.get(trace).removeAllPoints();
		}
	}

	/** adds trace to the visualizer with default trace length **/
	public void addTrace(String name, Color color) {
		Trace2DSimple newTrace = new Trace2DSimple();
		newTrace.setColor(color);
		this.traces.put(name, newTrace);
		this.chart.addTrace(newTrace);

		if (Config.getBoolean("DEFAULT_PAINT_LINESPOINT"))
			newTrace.addTracePainter(new TracePainterDisc(Config
					.getInt("DEFAULT_LINESPOINT_SIZE")));
		if (Config.getBoolean("DEFAULT_PAINT_FILL"))
			newTrace.addTracePainter(new TracePainterFill(this.chart));

	}

	/** removes a trace from the chart and the traces-list **/
	public void removeTrace(String name) {
		if (this.traces.containsKey(name)) {
			this.chart.removeTrace(this.traces.get(name));
			this.traces.remove(name);
		}
		if (this.yRight.getTraces().size() < 1)
			this.yRight.setVisible(false);
	}

	/** gathers all plottable values from the batch **/
	public String[] gatherValues(BatchData b) {
		ArrayList<String> tempList = new ArrayList<String>();

		ArrayList<String> tempDists = new ArrayList<String>();
		ArrayList<String> tempNvls = new ArrayList<String>();

		for (MetricData m : b.getMetrics().getList()) {
			if (m.getValues().size() > 0) {
				for (Distribution d : m.getDistributions().getList()) {
					tempDists.add(m.getName() + "." + d.getName());
				}
				for (NodeValueList n : m.getNodeValues().getList()) {
					tempNvls.add(m.getName() + "." + n.getName());
				}
			}
		}

		tempList.add("--- Distributions");
		for (String s : tempDists) {
			tempList.add("----- " + s);
		}
		tempList.add("--- NodeValueLists");
		for (String s : tempNvls) {
			tempList.add("----- " + s);
		}

		String[] tempValues = tempList.toArray(new String[tempList.size()]);
		return tempValues;
	}

}
