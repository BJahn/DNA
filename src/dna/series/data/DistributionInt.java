package dna.series.data;

import java.io.IOException;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> Codeupdate 13-06-24
=======
>>>>>>> Codeupdate 13-06-24
import java.util.ArrayList;

import com.sun.media.sound.InvalidFormatException;

import dna.io.Reader;
import dna.io.Writer;
<<<<<<< HEAD
<<<<<<< HEAD
import dna.util.ArrayUtils;
import dna.util.Config;

/**
 * DistributionInt is an object which represents an distribution by whole
 * numbers and its denominator. Integer data-structures are used. For larger
 * numbers see DistributionLong. Additional values are used for compared
 * distributions.
=======
=======
>>>>>>> Codeupdate 13-06-24
import dna.io.etc.Keywords;

/**
 * DistributionInt is an object which represents an distribution by whole numbers and its denominator.
 * Integer data-structures are used. For larger numbers see DistributionLong.
<<<<<<< HEAD
>>>>>>> Codeupdate 13-06-24
=======
>>>>>>> Codeupdate 13-06-24
 * 
 * @author Rwilmes
 * @date 17.06.2013
 */
<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> Codeupdate 13-06-18
=======
>>>>>>> Codeupdate 13-06-24
=======

>>>>>>> Codeupdate 13-06-18
=======
>>>>>>> Codeupdate 13-06-24
public class DistributionInt extends Distribution {

	// class variables
	private int[] values;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	private int denominator;

	// values for comparison
	private int comparedSum;
	private int comparedMin;
	private int comparedMax;
	private int comparedMed;
	private double comparedAvg;

	// constructors
	public DistributionInt(String name, int[] values, int denominator) {
		super(name);
		this.values = values;
		this.denominator = denominator;
	}

	public DistributionInt(String name) {
		super(name);
		this.values = new int[0];
		this.denominator = 0;
	}

	public DistributionInt(String name, int[] values, int denominator, int sum,
			int min, int max, int med, double avg) {
		super(name);
		this.values = values;
		this.denominator = denominator;
		this.comparedSum = sum;
		this.comparedMin = min;
		this.comparedMax = max;
		this.comparedMed = med;
		this.comparedAvg = avg;
	}

	// class methods
	public String toString() {
		return "distributionInt(" + super.getName() + ")";
	}

	// get methods
=======
=======
	private int denominator;
>>>>>>> Codeupdate 13-06-24
	
	// constructors
	public DistributionInt(String name, int[] values, int denominator) {
		super(name);
		this.values = values;
		this.denominator = denominator;
	}
	
<<<<<<< HEAD
	// class methods
>>>>>>> Codeupdate 13-06-18
=======
=======
	private int denominator;
>>>>>>> Codeupdate 13-06-24
	
	// constructors
	public DistributionInt(String name, int[] values, int denominator) {
		super(name);
		this.values = values;
		this.denominator = denominator;
	}
	
<<<<<<< HEAD
	// class methods
>>>>>>> Codeupdate 13-06-18
	public int[] getIntValues() {
		return this.values;
	}

<<<<<<< HEAD
<<<<<<< HEAD
	public int getDenominator() {
		return this.denominator;
	}

	public void setDenominator(int denominator) {
		this.denominator = denominator;
	}

	public void incrDenominator() {
		this.incrDenominator(1);
	}

	public void incrDenominator(int count) {
		this.denominator += count;
	}

	public void decrDenominator() {
		this.decrDenominator(1);
	}

	public void decrDenominator(int count) {
		this.denominator -= count;
	}

	public int getMin() {
		int y = 0;
		while (values[y] < 0) {
			y++;

		}
		return y;
	}

	public int getMax() {
		return values.length - 1;
	}

	public int getComparedSum() {
		return this.comparedSum;
	}

	public int getComparedMin() {
		return this.comparedMin;
	}

	public int getComparedMax() {
		return this.comparedMax;
	}

	public int getComparedMed() {
		return this.comparedMed;
	}

	public double getComparedAvg() {
		return this.comparedAvg;
	}

	/**
	 * Recalculates the denominator value.
	 */
	public void updateDenominator() {
		this.denominator = ArrayUtils.sum(this.values);
	}

	/**
	 * Increments a value of the distribution. Note: Also increments the
	 * denominator!
	 * 
	 * @param index
	 *            Index of the value that will be incremented.
	 */
	public void incr(int index) {
		this.values = ArrayUtils.incr(this.values, index);
		this.denominator++;
	}

	/**
	 * Decrements a value of the distribution. Note: Also decrements the
	 * denominator!
	 * 
	 * @param index
	 *            Index of the value that will be decremented.
	 */
	public void decr(int index) {
		this.values = ArrayUtils.decr(this.values, index);
		this.denominator--;
	}

	/**
	 * Truncates the distribution array by erasing all 0 at the end of it's
	 * value array. Note: Not affecting denominator.
	 * 
	 * @param index
	 *            Index of the value that will be decremented.
	 */
	public void truncate() {
		this.values = ArrayUtils.truncate(this.values, 0);
	}

	/**
	 * Truncates the value with a chosen index. Note: The denominator is not
	 * updated when calling this function!
	 * 
	 * @param index
	 *            Index of the value that will be decremented.
	 * @param value
	 *            Value the integer will be set to.
	 */
	public void set(int index, int value) {
		this.values = ArrayUtils.set(this.values, index, value, 0);
	}

	// IO Methods
	/**
	 * @param dir
	 *            String which contains the path / directory the Distribution
	 *            will be written to.
	 * 
	 * @param filename
	 *            String representing the desired filename for the Distribution.
	 */
	public void write(String dir, String filename) throws IOException {
		if (this.values == null) {
			throw new NullPointerException("no values for distribution \""
					+ this.getName() + "\" set to be written to " + dir);
		}
		Writer w = new Writer(dir, filename);

		w.writeln(this.denominator); // write denominator in first line

		for (int i = 0; i < this.values.length; i++) {
			w.writeln(i + Config.get("DISTRIBUTION_DELIMITER") + this.values[i]);
		}
		w.close();
	}

	/**
	 * @param dir
	 *            String which contains the path to the directory the
	 *            Distribution will be read from.
	 * 
	 * @param filename
	 *            String representing the filename the Distribution will be read
	 *            from.
	 * 
	 * @param readValues
	 *            Boolean. True: values from the file will be read. False: empty
	 *            Distribution will be created.
	 */
	public static DistributionInt read(String dir, String filename,
			String name, boolean readValues) throws IOException {
		if (!readValues) {
			return new DistributionInt(name, null, 0);
		}
		Reader r = new Reader(dir, filename);
		ArrayList<Integer> list = new ArrayList<Integer>();
		String line = null;
		int index = 0;

		line = r.readString();
		int denominator = Integer.parseInt(line);

		while ((line = r.readString()) != null) {
			String[] temp = line.split(Config.get("DISTRIBUTION_DELIMITER"));
			if (Integer.parseInt(temp[0]) != index) {
				throw new InvalidFormatException("expected index " + index
						+ " but found " + temp[0] + " @ \"" + line + "\"");
			}
			list.add(Integer.parseInt(temp[1]));
			index++;
		}
		int[] values = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			values[i] = list.get(i);
		}
		r.close();
		return new DistributionInt(name, values, denominator);
	}

	/**
	 * @param d1
	 *            distribution with integer datastructures
	 * @param d2
	 *            distribution with integer datastructures to compare equality
	 * @return true if both distributions have the same denominator, amount of
	 *         values and all values are equal
	 */
	public static boolean equals(DistributionInt d1, DistributionInt d2) {
		if (d1.getDenominator() != d2.getDenominator())
			return false;
		return ArrayUtils.equals(d1.getIntValues(), d2.getIntValues());
	}

=======
	// IO methods
	public void write(String dir, String filename) throws IOException {
		super.write(dir, filename);
=======
	// get methods
	public int[] getIntValues() {
		return this.values;
	}
	
	public int getDenominator() {
		return this.denominator;
>>>>>>> Codeupdate 13-06-24
	}

=======
	// IO methods
	public void write(String dir, String filename) throws IOException {
		super.write(dir, filename);
	}

	public static Distribution read(String dir, String filename, String name,
			boolean readValues) throws IOException {
		return Distribution.read(dir, filename, name, readValues);
	}
	
>>>>>>> Codeupdate 13-06-18
=======
	// get methods
	public int[] getIntValues() {
		return this.values;
	}
	
	public int getDenominator() {
		return this.denominator;
	}

>>>>>>> Codeupdate 13-06-24
	public int getMin() {
		int y = 0;
		while(values[y] < 0) {
			y++;
			
		}
		return y;
	}
	
	public int getMax() {
		return values.length-1;
	}
	
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> Codeupdate 13-06-18
=======
=======
>>>>>>> Codeupdate 13-06-24
	// IO Methods
	/**
	 * @param dir String which contains the path / directory the Distribution will be written to.
	 * 
	 * @param filename String representing the desired filename for the Distribution.
	 */
	public void write(String dir, String filename) throws IOException {
		if (this.values == null) {
			throw new NullPointerException("no values for distribution \""
					+ this.getName() + "\" set to be written to " + dir);
		}
		Writer w = new Writer(dir, filename);

		w.writeln(this.denominator);	// write denominator in first line
		
		for (int i = 0; i < this.values.length; i++) {
			w.writeln(i + Keywords.distributionDelimiter + this.values[i]);
		}
		w.close();
	}

	/**
	 * @param dir String which contains the path to the directory the Distribution will be read from.
	 * 
	 * @param filename String representing the filename the Distribution will be read from.
	 * 
	 * @param readValues Boolean. True:  values from the file will be read.
	 * 							  False: empty Distribution will be created.	
	 */
	public static DistributionInt read(String dir, String filename, String name,
			boolean readValues) throws IOException {
		if (!readValues) {
			return new DistributionInt(name, null,0);
		}
		Reader r = new Reader(dir, filename);
		ArrayList<Integer> list = new ArrayList<Integer>();
		String line = null;
		int index = 0;
		
		line = r.readString();
		int denominator = Integer.parseInt(line);
		
		while ((line = r.readString()) != null) {
			String[] temp = line.split(Keywords.distributionDelimiter);
			if (Integer.parseInt(temp[0]) != index) {
				throw new InvalidFormatException("expected index " + index
						+ " but found " + temp[0] + " @ \"" + line + "\"");
			}
			list.add(Integer.parseInt(temp[1]));
			index++;
		}
		int[] values = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			values[i] = list.get(i);
		}
		r.close();
		return new DistributionInt(name, values, denominator);
	}
	

	
<<<<<<< HEAD
>>>>>>> Codeupdate 13-06-24
=======
>>>>>>> Codeupdate 13-06-18
=======
>>>>>>> Codeupdate 13-06-24
}
