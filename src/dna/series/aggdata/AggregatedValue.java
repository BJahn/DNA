package dna.series.aggdata;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import java.io.IOException;

import dna.io.Reader;
import dna.io.Writer;
import dna.io.filesystem.Files;
import dna.util.Config;

/**
 * An AggregatedValue object contains aggregated values.
=======
/**
 * AggregatedValue is a class containing the aggregated values of a list of values.
<<<<<<< HEAD
>>>>>>> Codeupdate 13-06-10.
=======
 * Array structure as follows: values = { avg, min, max, median, variance, variance-low, variance-up, confidence-low, confidence-up }
>>>>>>> Codeupdate 13-06-18
=======
import java.io.IOException;

import dna.io.Reader;
import dna.io.etc.Keywords;

/**
 * An AggregatedValue object contains aggregated values.
>>>>>>> Codeupdate 13-06-24
=======
/**
 * AggregatedValue is a class containing the aggregated values of a list of values.
<<<<<<< HEAD
>>>>>>> Codeupdate 13-06-10.
=======
 * Array structure as follows: values = { avg, min, max, median, variance, variance-low, variance-up, confidence-low, confidence-up }
>>>>>>> Codeupdate 13-06-18
=======
import java.io.IOException;

import dna.io.Reader;
import dna.io.etc.Keywords;

/**
 * An AggregatedValue object contains aggregated values.
>>>>>>> Codeupdate 13-06-24
 * 
 * @author Rwilmes
 * @date 10.06.2013
 */
public class AggregatedValue extends AggregatedData {

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	// member variables
	private double[] values;

	// AggregatedValue array structure: { avg, min, max, median, variance,
	// variance-low, variance-up, confidence-low, confidence-up }

	public double getAvg() {
		return this.values[0];
	}

	public double getMin() {
		return this.values[1];
	}

	public double getMax() {
		return this.values[2];
	}

	public double getMedian() {
		return this.values[3];
	}

	public double getVariance() {
		return this.values[4];
	}

	public double getVarianceLow() {
		return this.values[5];
	}

	public double getVarianceUp() {
		return this.values[6];
	}

	public double getConfidenceLow() {
		return this.values[7];
	}

	public double getConfidenceUp() {
		return this.values[8];
	}

	// constructors
	public AggregatedValue(String name) {
		super(name);
	}

	public AggregatedValue(String name, double[] values) {
		super(name);
		this.values = values;
	}

	public AggregatedValue(String name, double[] values, String dir)
			throws IOException {
		super(name);
		this.write(dir, Files.getValuesFilename(name));
	}

	// get methods
	public double[] getValues() {
		return this.values;
	}

	// IO methods
	public void write(String dir, String filename) throws IOException {
		Writer w = new Writer(dir, filename);
		String temp = "name";
		for (int i = 0; i < this.values.length; i++) {
			temp = temp + Config.get("AGGREGATED_DATA_DELIMITER")
					+ this.values[i];
		}

		w.writeln(temp);
		w.close();
	}

	/**
	 * @param dir
	 *            String which contains the path to the directory the
	 *            AggregatedValue will be read from.
	 * 
	 * @param filename
	 *            String representing the filename the Distribution will be read
	 *            from.
	 * 
	 * @param readValues
	 *            Boolean. True: values from the file will be read. False: empty
	 *            AggregatedValue will be created.
	 */
	public static AggregatedValue read(String dir, String filename,
			String name, boolean readValues) throws IOException {
		if (!readValues) {
			return new AggregatedValue(name, null);
		}
		Reader r = new Reader(dir, filename);

		String line = null;

		line = r.readString();
		String[] temp = line.split(Config.get("AGGREGATED_DATA_DELIMITER"));

		double[] tempDouble = new double[temp.length];
		for (int i = 0; i < tempDouble.length; i++) {
			tempDouble[i] = Double.parseDouble(temp[i]);
		}

		r.close();
		return new AggregatedValue(name, tempDouble);
	}

	public static void write(double[] x, AggregatedValue[] values, String dir,
			String filename) throws IOException {
		Writer w = new Writer(dir, filename);
		for (int i = 0; i < x.length; i++) {
			StringBuffer buff = new StringBuffer(x[i] + "");
			for (int j = 0; j < values[i].getValues().length; j++) {
				buff.append(Config.get("AGGREGATEDDATA_DELIMITER"));
				buff.append(values[i].getValues()[j]);
			}
			w.writeln(buff.toString());
		}
		w.close();
	}

	public AggregatedValue clone() {
		double[] values = new double[this.values.length];
		for (int i = 0; i < this.values.length; i++) {
			values[i] = this.values[i];
		}
		return new AggregatedValue(this.getName(), values);
	}

	public AggregatedValue clone(double factor) {
		double[] values = new double[this.values.length];
		for (int i = 0; i < this.values.length; i++) {
			values[i] = this.values[i] * factor;
		}
		return new AggregatedValue(this.getName(), values);
	}

	public static AggregatedValue getNaN() {
		return new AggregatedValue("NaN", new double[] { Double.NaN,
				Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN,
				Double.NaN, Double.NaN, Double.NaN });
	}

}
=======
=======
>>>>>>> Codeupdate 13-06-10.
	// class variables
	private String name;
	private String type = "AggregatedValue";
	private double value;
	private double[] values; // AggregatedValue array structure:  { 0, 0, avg, min, max, median, variance, variance-low, variance-up, confidence-low, confidence-up }
	
<<<<<<< HEAD
=======
>>>>>>> Codeupdate 13-06-18
=======
	// member variables
	private double[] values;
	
>>>>>>> Codeupdate 13-06-24
	// constructors
	public AggregatedValue(String name) {
		super(name);
	}
	
	public AggregatedValue(String name, double[] values) {
		super(name);
		this.values = values;
	}
	
	// get methods
=======
=======
>>>>>>> Codeupdate 13-06-18
=======
	// member variables
	private double[] values;
	
>>>>>>> Codeupdate 13-06-24
	// constructors
	public AggregatedValue(String name) {
		super(name);
	}
	
	public AggregatedValue(String name, double[] values) {
<<<<<<< HEAD
<<<<<<< HEAD
		this.name = name;
		this.values = values;
	}
	
	// class methods
	public void setValue(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return this.value;
	}
	
>>>>>>> Codeupdate 13-06-10.
=======
		super(name);
		this.values = values;
	}
	
	// get methods
>>>>>>> Codeupdate 13-06-24
	public double[] getValues() {
		return this.values;
	}
	
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> Codeupdate 13-06-24
	// IO methods
	/**
	 * @param dir String which contains the path to the directory the AggregatedValue will be read from.
	 * 
	 * @param filename String representing the filename the Distribution will be read from.
	 * 
	 * @param readValues Boolean. True:  values from the file will be read.
	 * 							  False: empty AggregatedValue will be created.	
	 */
	public static AggregatedValue read(String dir, String filename, String name,
			boolean readValues) throws IOException {
		if (!readValues) {
			return new AggregatedValue(name, null);
		}
		Reader r = new Reader(dir, filename);

		String line = null;
		
		line = r.readString();
		String[] temp = line.split(Keywords.aggregatedDataDelimiter);

		double[] tempDouble = new double[temp.length];
		for(int i = 0; i < tempDouble.length; i++) {
			tempDouble[i] = Double.parseDouble(temp[i]);
		}

		r.close();
		return new AggregatedValue(name, tempDouble);
<<<<<<< HEAD
	}

}
>>>>>>> Codeupdate 13-06-10.
=======
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
=======
		super(name, values);
>>>>>>> Codeupdate 13-06-18
=======
>>>>>>> Codeupdate 13-06-24
	}

}
>>>>>>> Codeupdate 13-06-10.
