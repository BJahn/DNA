package dna.visualization.config.components;

import java.awt.Dimension;

import dna.visualization.GuiOptions;
import dna.visualization.config.VisualizerListConfig;
import dna.visualization.config.JSON.JSONObject;

/**
 * Configuration object to configure metric visualizer windows.
 * 
 * @author Rwilmes
 */
public class MetricVisualizerConfig {

	// constructor
	public MetricVisualizerConfig() {

	}

	public MetricVisualizerConfig(String name, int traceLength,
			Dimension chartSize, double xAxisOffset, String xAxisTitle,
			String xAxisType, String xAxisFormat, String y1AxisTitle,
			String y2AxisTitle, Dimension legendSize,
			VisualizerListConfig listConfig, MenuBarConfig menuBarConfig) {
		this.name = name;
		this.traceLength = traceLength;
		this.chartSize = chartSize;
		this.xAxisOffset = xAxisOffset;
		this.xAxisTitle = xAxisTitle;
		this.xAxisType = xAxisType;
		this.xAxisFormat = xAxisFormat;
		this.y1AxisTitle = y1AxisTitle;
		this.y2AxisTitle = y2AxisTitle;
		this.legendSize = legendSize;
		this.listConfig = listConfig;
		this.menuBarConfig = menuBarConfig;
	}

	// general options
	private String name;
	private int traceLength;

	// chart options
	private Dimension chartSize;
	private double xAxisOffset;

	// x axis
	private String xAxisTitle;
	private String xAxisType;
	private String xAxisFormat;

	// y axis
	private String y1AxisTitle;
	private String y2AxisTitle;

	// legend options
	private Dimension legendSize;
	private VisualizerListConfig listConfig;

	// menu bar options
	private MenuBarConfig menuBarConfig;

	// get methods
	public String getName() {
		return this.name;
	}

	public int getTraceLength() {
		return this.traceLength;
	}

	public Dimension getChartSize() {
		return this.chartSize;
	}

	public double getxAxisOffset() {
		return this.xAxisOffset;
	}

	public String getxAxisTitle() {
		return this.xAxisTitle;
	}

	public String getxAxisType() {
		return this.xAxisType;
	}

	public String getxAxisFormat() {
		return this.xAxisFormat;
	}

	public String getY1AxisTitle() {
		return this.y1AxisTitle;
	}

	public String getY2AxisTitle() {
		return this.y2AxisTitle;
	}

	public Dimension getLegendSize() {
		return this.legendSize;
	}

	public VisualizerListConfig getListConfig() {
		return this.listConfig;
	}

	public MenuBarConfig getMenuBarConfig() {
		return this.menuBarConfig;
	}

	/** creates a metric visualizer config object from a given json object **/
	public static MetricVisualizerConfig createMetricVisualizerConfigFromJSONObject(
			JSONObject o) {
		String name = GuiOptions.metricVisualizerDefaultTitle;
		int traceLength = GuiOptions.visualizerDefaultTraceLength;
		Dimension chartSize = GuiOptions.visualizerDefaultChartSize;

		double xAxisOffset = GuiOptions.metricVisualizerXAxisOffset;

		String xAxisTitle = GuiOptions.visualizerDefaultX1AxisTitle;
		String xAxisType = GuiOptions.metricVisualizerXAxisType;
		String xAxisFormat = GuiOptions.metricVisualizerXAxisFormat;

		String y1AxisTitle = GuiOptions.visualizerDefaultY1AxisTitle;
		String y2AxisTitle = GuiOptions.visualizerDefaultY2AxisTitle;

		Dimension legendSize = GuiOptions.visualizerDefaultLegendSize;

		try {
			name = o.getString("Name");
		} catch (Exception e) {
		}

		try {
			traceLength = o.getInt("TraceLength");
		} catch (Exception e) {
		}

		try {
			JSONObject chart = o.getJSONObject("Chart");
			try {
				chartSize = new Dimension(chart.getInt("Width"),
						chart.getInt("Height"));
			} catch (Exception e) {
			}

			for (String s : JSONObject.getNames(chart)) {
				switch (s) {
				case "xAxisTitle":
					xAxisTitle = chart.getString(s);
					break;
				case "xAxisType":
					xAxisType = chart.getString(s);
					break;
				case "xAxisFormat":
					xAxisFormat = chart.getString(s);
					break;
				case "xAxisOffset":
					xAxisOffset = chart.getDouble(s);
					break;
				case "y1AxisTitle":
					y1AxisTitle = chart.getString(s);
					break;
				case "y2AxisTitle":
					y2AxisTitle = chart.getString(s);
					break;
				}
			}
		} catch (Exception e) {
		}

		// legend
		try {
			JSONObject legend = o.getJSONObject("Legend");
			legendSize = new Dimension(legend.getInt("Width"),
					legend.getInt("Height"));
		} catch (Exception e) {
		}

		// parse list config
		VisualizerListConfig listConfig = VisualizerListConfig
				.createConfigFromJSONObject(o);

		// calculate menu bar size
		MenuBarConfig menuBarConfig = MenuBarConfig
				.createMenuBarConfigFromJSONObject(o.getJSONObject("MenuBar"));

		return new MetricVisualizerConfig(name, traceLength, chartSize,
				xAxisOffset, xAxisTitle, xAxisType, xAxisFormat, y1AxisTitle,
				y2AxisTitle, legendSize, listConfig, menuBarConfig);
	}
}
