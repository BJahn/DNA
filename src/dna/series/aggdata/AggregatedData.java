package dna.series.aggdata;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import dna.io.Writer;
import dna.series.lists.ListItem;
import dna.util.Config;
=======
import java.io.IOException;

import dna.io.Writer;
import dna.io.etc.Keywords;
import dna.series.lists.ListItem;
import dna.util.Log;
>>>>>>> Codeupdate 13-06-18

/**
 * AggregatedData is the super-class for all provided aggregation
 * data-structures.
=======
=======
import java.io.IOException;

import dna.io.Writer;
import dna.io.etc.Keywords;
>>>>>>> Codeupdate 13-06-18
import dna.series.lists.ListItem;

/**
<<<<<<< HEAD
 * AggregatedData is the super-class for all provided aggregated data-structures.
>>>>>>> Codeupdate 13-06-10.
=======
 * AggregatedData is the super-class for all provided aggregation data-structures.
>>>>>>> Codeupdate 13-06-24
=======
import dna.series.lists.ListItem;

/**
 * AggregatedData is the super-class for all provided aggregated data-structures.
>>>>>>> Codeupdate 13-06-10.
 * 
 * @author Rwilmes
 * @date 10.06.2013
 */
public class AggregatedData implements ListItem {
<<<<<<< HEAD
<<<<<<< HEAD

	// member variables
	private String name;

	// constructors
	public AggregatedData() {
	}

	public AggregatedData(String name) {
		this.name = name;
	}

	// get methods
	public String getName() {
		return this.name;
	}

	// IO methods
	/**
	 * Method to write the context of an Array of AggregatedValue objects to a
	 * specified location.
	 * 
	 * @param inputData
	 *            The Aggregated Data that is to be written on the filesystem.
	 * @param dir
	 *            String which contains the path / directory the Data will be
	 *            written to.
	 * @param filename
	 *            String representing the desired filename for the Data.
	 */
	public static void write(AggregatedValue[] inputData, String dir,
			String filename) throws IOException {
		Writer w = new Writer(dir, filename);

		for (AggregatedValue aggData : inputData) {
			String temp = "" + aggData.getName()
					+ Config.get("AGGREGATED_DATA_DELIMITER");
			for (int i = 0; i < aggData.getValues().length; i++) {
				if (i == aggData.getValues().length - 1)
					temp += aggData.getValues()[i];
				else
					temp += aggData.getValues()[i]
							+ Config.get("AGGREGATED_DATA_DELIMITER");
			}
			w.writeln(temp);
		}
		w.close();
	}

	public static void write(ArrayList<AggregatedValue> inputData, String dir,
			String filename) throws IOException {
		Writer w = new Writer(dir, filename);

		for (AggregatedValue aggData : inputData) {
			String temp = "" + aggData.getName()
					+ Config.get("AGGREGATED_DATA_DELIMITER");
			for (int i = 0; i < aggData.getValues().length; i++) {
				if (i == aggData.getValues().length - 1)
					temp += aggData.getValues()[i];
				else
					temp += aggData.getValues()[i]
							+ Config.get("AGGREGATED_DATA_DELIMITER");
			}
			w.writeln(temp);
		}
		w.close();
	}

	public static void write(AggregatedValue inputData, String dir,
			String filename) throws IOException {
		Writer w = new Writer(dir, filename);

		String temp = "" + inputData.getName()
				+ Config.get("AGGREGATED_DATA_DELIMITER");
		// String temp = "" + Config.get("AGGREGATED_DATA_DELIMITER");
		for (int i = 0; i < inputData.getValues().length; i++) {
			if (i == inputData.getValues().length - 1)
				temp += inputData.getValues()[i];
			else
				temp += inputData.getValues()[i]
						+ Config.get("AGGREGATED_DATA_DELIMITER");
		}
		w.writeln(temp);

		w.close();
	}

	public static void write(AggregatedNodeValueList inputData, String dir,
			String filename) throws IOException {
		Writer w = new Writer(dir, filename);
		AggregatedValue[] tempData = inputData.getValues();

		for (AggregatedValue aggData : tempData) {
			String temp = "" + (int) aggData.getValues()[0]
					+ Config.get("AGGREGATED_DATA_DELIMITER");
			for (int i = 1; i < aggData.getValues().length; i++) {
				if (i == aggData.getValues().length - 1)
					temp += aggData.getValues()[i];
				else
					temp += aggData.getValues()[i]
							+ Config.get("AGGREGATED_DATA_DELIMITER");
			}
			w.writeln(temp);
		}
		w.close();
	}

	public static void write(AggregatedDistribution inputData, String dir,
			String filename) throws IOException {
		Writer w = new Writer(dir, filename);
		AggregatedValue[] tempData = inputData.getValues();

		for (AggregatedValue aggData : tempData) {
			String temp = "" + (int) aggData.getValues()[0]
					+ Config.get("AGGREGATED_DATA_DELIMITER");
			for (int i = 1; i < aggData.getValues().length; i++) {
				if (i == aggData.getValues().length - 1)
					temp += aggData.getValues()[i];
				else
					temp += aggData.getValues()[i]
							+ Config.get("AGGREGATED_DATA_DELIMITER");
			}
			w.writeln(temp);
		}
		w.close();
	}

	public static void write(HashMap<String, double[]> inputData, String dir,
			String filename) throws IOException {
		Writer w = new Writer(dir, filename);

		for (String value : inputData.keySet()) {
			double[] tempValues = inputData.get(value);

			String temp = value + Config.get("AGGREGATED_DATA_DELIMITER")
					+ tempValues[0]
					+ Config.get("AGGREGATED_DATA_DELIMITER");
			for (int i = 1; i < tempValues.length; i++) {
				if (i == tempValues.length - 1)
					temp += tempValues[i];
				else
					temp += tempValues[i]
							+ Config.get("AGGREGATED_DATA_DELIMITER");
			}
			w.writeln(temp);
		}
		w.close();
	}

=======
	
	// member variables
	private String name;
	
	// constructors
=======
	
	// class variables
	private String name;
	private double[] values;
	
	// class methods
>>>>>>> Codeupdate 13-06-10.
	public AggregatedData() { }
	
	public AggregatedData(String name) {
		this.name = name;
	}
	
<<<<<<< HEAD
<<<<<<< HEAD
	// get methods
	public String getName() {
		return this.name;
	}
	
	// IO methods
	/**
	 * Method to write the context of an Array of AggregatedValue objects to a specified location.
	 * 
	 * @param inputData The Aggregated Data that is to be written on the filesystem.
	 * @param dir String which contains the path / directory the Data will be written to.
	 * @param filename String representing the desired filename for the Data.
	 */
	public static void write(AggregatedValue[] inputData, String dir, String filename) throws IOException {
		Writer w = new Writer(dir, filename);
		
		for(AggregatedValue aggData : inputData) {			
			String temp = "" + (int) aggData.getValues()[0] + Keywords.aggregatedDataDelimiter;
			for (int i = 1; i < aggData.getValues().length; i++) {
				if(i == aggData.getValues().length-1)
					temp += aggData.getValues()[i];
				else
					temp += aggData.getValues()[i] + Keywords.aggregatedDataDelimiter;
			}
			w.writeln(temp);
		}
		w.close();
	}
<<<<<<< HEAD
>>>>>>> Codeupdate 13-06-10.
=======
	
	public static void write(AggregatedNodeValueList inputData, String dir, String filename) throws IOException {
		write(inputData.getValues(), dir, filename);
	}
	
	public static void write(AggregatedDistribution inputData, String dir, String filename) throws IOException {
		write(inputData.getValues(), dir, filename);
	}
>>>>>>> Codeupdate 13-06-24
=======
	public AggregatedData(String name, double value) {
		this.name = name;
		this.value = value;
	}
	
=======
>>>>>>> Codeupdate 13-06-18
	public AggregatedData(String name, double[] values) {
		this.name = name;
		this.values = values;
	}
	
	public String getName() {
		return this.name;
	}
	
	public double[] getValues() {
		return this.values;
	}
	
	public double getValue(int index) {
		try{
			return this.values[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.error("AggregatedNodeValueList IndexOutOfBoundsException");
		}
		return 0;
	}
	
	// IO methods
	/**
	 * Method to write the context of a single AggregatedData object to a specified location.
	 * 
	 * @param dir String which contains the path / directory the Data will be written to.
	 * 
	 * @param filename String representing the desired filename for the Data.
	 */
	public void write(String dir, String filename) throws IOException {
		if (this.values == null) {
			throw new NullPointerException("no values for aggregateddata \""
					+ this.name + "\" set to be written to " + dir);
		}
		Writer w = new Writer(dir, filename);
		String temp = "";
		for (int i = 0; i < this.values.length; i++) {
			if(i == this.values.length-1)
				temp += this.values[i];
			else
				temp += this.values[i] + "\t";
		}
		w.writeln(temp);
		w.close();
	}
	
	/**
	 * Method to write the context of a single AggregatedData object to a specified location.
	 * 
	 * @param inputData The Aggregated Data that is to be written on the filesystem.
	 * @param dir String which contains the path / directory the Data will be written to.
	 * @param filename String representing the desired filename for the Data.
	 */
	public static void write(AggregatedData[] inputData, String dir, String filename) throws IOException {
		Writer w = new Writer(dir, filename);
		
		for(AggregatedData aggData : inputData) {
			String temp = "";
			for (int i = 0; i < aggData.getValues().length; i++) {
				if(i == aggData.getValues().length-1)
					temp += aggData.getValues()[i];
				else
					temp += aggData.getValues()[i] + "\t";
			}
			w.writeln(temp);
		}
		w.close();
	}
>>>>>>> Codeupdate 13-06-10.
}
