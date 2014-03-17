package dna.profiler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import dna.graph.datastructures.DataStructure.AccessType;
import dna.profiler.datatypes.ComparableEntry;
import dna.profiler.datatypes.ComparableEntryMap;
import dna.profiler.datatypes.complexity.ComplexityMap;
import dna.profiler.datatypes.complexity.ComplexityType;
import dna.profiler.datatypes.complexity.MemoryComplexity;
import dna.profiler.datatypes.complexity.RuntimeComplexity;
import dna.util.PropertiesHolder;

public abstract class ProfilerMeasurementData extends PropertiesHolder {
	public enum ProfilerDataType {
		RuntimeComplexity, MemoryComplexity
	}

	private static String folderName = "profilerData/";

	private static HashMap<String, ComparableEntry> measurementData;

	public static void init() throws IOException {
		measurementData = null;
		loadFromProperties(initFromFolder(folderName));
	}

	public static ComparableEntry get(ProfilerDataType complexityType,
			String classname, AccessType accessType, String storedDataClass,
			ComplexityType.Base base	) {

		String keyName = complexityType.toString().toUpperCase() + "_"
				+ classname.toUpperCase();

		ComparableEntry c = get(keyName + "_"
				+ accessType.toString().toUpperCase() + "_"
				+ storedDataClass.toUpperCase());
		if (c == null)
			c = get(keyName + "_" + accessType.toString().toUpperCase());
		if (c == null)
			c = get(keyName);
		if (c == null)
			throw new RuntimeException("Missing complexity entry " + keyName);
		return c;
	}

	public static ComparableEntry get(String key) {
		if (measurementData == null)
			try {
				init();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		ComparableEntry res = measurementData.get(key);
		if (res != null) {
			res = res.clone();
		}
		return res;
	}

	public static ComparableEntry parseString(String key, String val) {
		if (key.startsWith("RUNTIMECOMPLEXITY")) {
			return RuntimeComplexity.parseString(key, val);
		} else if (key.startsWith("MEMORYCOMPLEXITY")) {
			return MemoryComplexity.parseString(key, val);
		} else {
			throw new RuntimeException("Don't know how to parse " + key + "="
					+ val);
		}
	}

	public static ComparableEntryMap getMap(ProfilerDataType t) {
		switch (t) {
		case MemoryComplexity:
		case RuntimeComplexity:
			return new ComplexityMap();
		default:
			throw new RuntimeException("Cannot create ComparableEntryMap for "
					+ t);
		}
	}

	public static void loadFromProperties(Properties in) {
		if (measurementData == null) {
			measurementData = new HashMap<String, ComparableEntry>();
		}

		for (String key : in.stringPropertyNames()) {
			String val = in.getProperty(key);
			ComparableEntry c = parseString(key, val);
			if (c == null) {
				throw new RuntimeException(
						"Could not properly parse complexity entry " + val);
			} else {
				measurementData.put(key, c);
			}
		}
	}
}
