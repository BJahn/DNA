package dna.graph.datastructures;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;

import dna.graph.Graph;
import dna.graph.IElement;
import dna.graph.datastructures.DataStructure.AccessType;
import dna.graph.datastructures.DataStructure.ListType;
import dna.graph.edges.DirectedEdge;
import dna.graph.edges.Edge;
import dna.graph.edges.IWeightedEdge;
import dna.graph.edges.UndirectedEdge;
import dna.graph.nodes.IWeightedNode;
import dna.graph.nodes.Node;
import dna.graph.weights.IWeighted;
import dna.profiler.ProfilerMeasurementData;
import dna.profiler.ProfilerMeasurementData.ProfilerDataType;
import dna.profiler.complexity.Complexity;
import dna.profiler.complexity.ComplexityType.Base;
import dna.util.Config;

/**
 * Container for different types of storages for everything: this holds the
 * graph type (eg. dna.graph, ReadableGraph), the storages within a graph for
 * edges and nodes, and the node type and the resulting edge type
 * 
 * @author Nico
 * 
 */
public class GraphDataStructure {
	private Class<? extends Node> nodeType;
	private Class<? extends Edge> edgeType;
	private Constructor<?> lastWeightedEdgeConstructor = null;
	private Constructor<?> lastEdgeConstructor = null;
	private IEdgeListDatastructure emptyList = new DEmpty(null);

	private EnumMap<ListType, Class<? extends IDataStructure>> listTypes;
	private EnumMap<ListType, Integer> defaultListSizes;

	public GraphDataStructure(
			EnumMap<ListType, Class<? extends IDataStructure>> listTypes,
			Class<? extends Node> nodeType, Class<? extends Edge> edgeType) {

		this.listTypes = listTypes;

		this.nodeType = nodeType;
		this.edgeType = edgeType;
		init();
	}

	@SuppressWarnings("unchecked")
	public GraphDataStructure(String gdsString) {
		String splitted[] = gdsString.split(Config
				.get("DATASTRUCTURES_CLASS_DELIMITER"));
		listTypes = new EnumMap<ListType, Class<? extends IDataStructure>>(
				ListType.class);
		try {
			listTypes.put(ListType.GlobalNodeList,
					(Class<? extends INodeListDatastructure>) Class
							.forName(splitted[0]));
			listTypes.put(ListType.GlobalEdgeList,
					(Class<? extends IEdgeListDatastructure>) Class
							.forName(splitted[1]));
			listTypes.put(ListType.LocalEdgeList,
					(Class<? extends IEdgeListDatastructure>) Class
							.forName(splitted[2]));
			this.nodeType = (Class<? extends Node>) Class.forName(splitted[3]);
			this.edgeType = (Class<? extends Edge>) Class.forName(splitted[4]);
		} catch (ClassNotFoundException | ClassCastException e) {
			e.printStackTrace();
		}
		init();
	}

	private void init() {
		if (listTypes.get(ListType.GlobalEdgeList) == null
				&& listTypes.get(ListType.LocalEdgeList) == null) {
			throw new RuntimeException(
					"Either the global or local edge list must not be NULL");
		}
		
		if ( listTypes.get(ListType.GlobalNodeList) == null ) {
			throw new RuntimeException("The GraphDataStructure cannot be initialized without a global node list");
		}
		
		if ( listTypes.get(ListType.LocalNodeList) == null ) {
			listTypes.put(ListType.LocalNodeList, (Class<? extends IDataStructure>) listTypes.get(ListType.GlobalNodeList));
		}

		this.defaultListSizes = new EnumMap<DataStructure.ListType, Integer>(
				DataStructure.ListType.class);
		this.defaultListSizes.put(ListType.GlobalEdgeList, 10);
		this.defaultListSizes.put(ListType.GlobalNodeList, 10);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphDataStructure other = (GraphDataStructure) obj;
		if (edgeType == null) {
			if (other.edgeType != null)
				return false;
		} else if (!edgeType.equals(other.edgeType))
			return false;
		if (listTypes.get(ListType.GlobalEdgeList) == null) {
			if (other.listTypes.get(ListType.GlobalEdgeList) != null)
				return false;
		} else if (!listTypes.get(ListType.GlobalEdgeList).equals(
				other.listTypes.get(ListType.GlobalEdgeList)))
			return false;
		if (listTypes.get(ListType.LocalEdgeList) == null) {
			if (other.listTypes.get(ListType.LocalEdgeList) != null)
				return false;
		} else if (!listTypes.get(ListType.LocalEdgeList).equals(
				other.listTypes.get(ListType.LocalEdgeList)))
			return false;
		if (listTypes.get(ListType.GlobalNodeList) == null) {
			if (other.listTypes.get(ListType.GlobalNodeList) != null)
				return false;
		} else if (!listTypes.get(ListType.GlobalNodeList).equals(
				other.listTypes.get(ListType.GlobalNodeList)))
			return false;
		if (nodeType == null) {
			if (other.nodeType != null)
				return false;
		} else if (!nodeType.equals(other.nodeType))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends INodeListDatastructure> getGlobalNodeListType() {
		return (Class<? extends INodeListDatastructure>) listTypes.get(ListType.GlobalNodeList);
	}

	@SuppressWarnings("unchecked")
	public Class<? extends IEdgeListDatastructure> getGlobalEdgeListType() {
		return (Class<? extends IEdgeListDatastructure>) listTypes.get(ListType.GlobalEdgeList);
	}

	@SuppressWarnings("unchecked")
	public Class<? extends IEdgeListDatastructure> getLocalEdgeListType() {
		return (Class<? extends IEdgeListDatastructure>) listTypes.get(ListType.LocalEdgeList);
	}

	public Class<? extends Node> getNodeType() {
		return nodeType;
	}

	public Class<? extends Edge> getEdgeType() {
		return edgeType;
	}

	public void setNodeType(Class<? extends Node> newNodeType) {
		this.nodeType = newNodeType;
	}

	public void setEdgeType(Class<? extends Edge> edgeType) {
		this.edgeType = edgeType;
	}

	public Graph newGraphInstance(String name, long timestamp, int nodes,
			int edges) {
		this.defaultListSizes.put(ListType.GlobalNodeList, nodes);
		this.defaultListSizes.put(ListType.GlobalEdgeList, edges);
		return new Graph(name, timestamp, this, nodes, edges);
	}

	public IDataStructure newList(ListType listType) {
		Class<? extends IDataStructure> sourceClass = listTypes.get(listType);
		Class<? extends IElement> storedDataType = null;

		switch (listType) {
		case GlobalEdgeList:
			storedDataType = edgeType;
			break;
		case LocalEdgeList:
			storedDataType = edgeType;
			break;
		case GlobalNodeList:
			storedDataType = nodeType;
		case LocalNodeList:
			storedDataType = nodeType;
		}

		if (sourceClass == null) {
			return emptyList;
		}
		IDataStructure res = null;
		try {
			res = sourceClass.getConstructor(ListType.class,
					storedDataType.getClass()).newInstance(listType,
					storedDataType);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		if (this.defaultListSizes.containsKey(listType)) {
			res.reinitializeWithSize(this.defaultListSizes.get(listType));
		}
		return res;
	}

	public INodeListDatastructure newLocalNodeList() {
		return (INodeListDatastructure) newList(ListType.LocalNodeList);
	}

	public INodeListDatastructure newGlobalNodeList() {
		return (INodeListDatastructure) newList(ListType.GlobalNodeList);
	}

	public IEdgeListDatastructure newGlobalEdgeList() {
		return (IEdgeListDatastructure) this.newList(ListType.GlobalEdgeList);
	}

	public IEdgeListDatastructure newLocalEdgeList() {
		return (IEdgeListDatastructure) this.newList(ListType.LocalEdgeList);
	}

	public Node newNodeInstance(int index) {
		Constructor<? extends Node> c;
		try {
			c = nodeType.getConstructor(int.class, GraphDataStructure.class);
			return c.newInstance(index, this);
		} catch (NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			RuntimeException rt = new RuntimeException(
					"Could not generate new node instance: " + e.getMessage());
			rt.setStackTrace(e.getStackTrace());
			throw rt;
		}
	}

	public Node newNodeInstance(String str) {
		Constructor<? extends Node> c;
		try {
			c = nodeType.getConstructor(String.class, GraphDataStructure.class);
			return c.newInstance(str, this);
		} catch (NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			RuntimeException rt = new RuntimeException(
					"Could not generate new node instance");
			rt.setStackTrace(e.getStackTrace());
			throw rt;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IWeightedNode<?> newWeightedNode(int index, Object weight) {
		Constructor<? extends IWeighted> c;
		try {
			c = (Constructor<? extends IWeightedNode>) nodeType.getConstructor(
					int.class, weight.getClass(), GraphDataStructure.class);
			return (IWeightedNode<?>) c.newInstance(index, weight, this);
		} catch (NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			RuntimeException rt = new RuntimeException(
					"Could not generate new weighted node instance");
			rt.setStackTrace(e.getStackTrace());
			throw rt;
		}
	}

	public Edge newEdgeInstance(Node src, Node dst) {
		if (src.getClass() != dst.getClass()) {
			throw new RuntimeException(
					"Could not generate new edge instance for non-equal node classes "
							+ src.getClass() + " and " + dst.getClass());
		}

		if (this.lastEdgeConstructor != null) {
			// Try to use cached constructor, but throw it away if it is not the
			// correct one
			try {
				return edgeType.cast(this.lastEdgeConstructor.newInstance(src,
						dst));
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| ClassCastException e) {
				this.lastEdgeConstructor = null;
			}
		}

		Constructor<?>[] cList = edgeType.getConstructors();
		Constructor<?> cNeeded = null;

		// First: search matching constructor for src.getClass and dst.getClass
		Class<?>[] cRequired = new Class[] { src.getClass(), dst.getClass() };
		cNeeded = getConstructor(cList, cRequired);

		// Okay, check for super types if needed
		if (cNeeded == null) {
			Class<?> superType;
			superType = src.getClass().getSuperclass();
			while (cNeeded == null && Node.class.isAssignableFrom(superType)) {
				cRequired = new Class[] { superType, superType };
				for (Constructor<?> c : cList) {
					if (Arrays.equals(c.getParameterTypes(), cRequired)) {
						cNeeded = c;
					}
				}
				superType = superType.getSuperclass();
			}
		}

		if (cNeeded == null) {
			throw new RuntimeException("No edge constructor for nodetype "
					+ src.getClass() + " in edge type " + edgeType + " found");
		}

		try {
			this.lastEdgeConstructor = cNeeded;
			return edgeType.cast(cNeeded.newInstance(src, dst));
		} catch (SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			RuntimeException rt = new RuntimeException(
					"Could not generate new edge instance");
			rt.setStackTrace(e.getStackTrace());
			throw rt;
		}
	}

	public Edge newEdgeInstance(String str, Graph graph,
			HashMap<Integer, Node> addedNodes) {
		Constructor<? extends Edge> c;
		try {
			c = edgeType.getConstructor(String.class, Graph.class,
					HashMap.class);
			return c.newInstance(str, graph, addedNodes);
		} catch (NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			RuntimeException rt = new RuntimeException(
					"Could not generate new edge instance");
			rt.setStackTrace(e.getStackTrace());
			throw rt;
		}
	}

	public Edge newEdgeInstance(String str, Graph graph) {
		Constructor<? extends Edge> c;
		try {
			c = edgeType.getConstructor(String.class, Graph.class);
			return c.newInstance(str, graph);
		} catch (NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			RuntimeException rt = new RuntimeException(
					"Could not generate new edge instance");
			rt.setStackTrace(e.getStackTrace());
			throw rt;
		}
	}

	public Constructor<?> getConstructor(Constructor<?>[] list,
			Class<?>[] required) {
		Constructor<?> cNeeded = null;

		for (Constructor<?> c : list) {
			Class<?>[] pt = c.getParameterTypes();
			if (pt.length != required.length)
				continue;

			for (int i = 0; i < required.length; i++) {
				if (pt[i] != required[i])
					break;
				if (i == (required.length - 1))
					return c;
			}
		}
		return cNeeded;
	}

	public IWeightedEdge<?> newWeightedEdge(Node src, Node dst, Object weight) {
		if (src.getClass() != dst.getClass()) {
			throw new RuntimeException(
					"Could not generate new edge instance for non-equal node classes "
							+ src.getClass() + " and " + dst.getClass());
		}

		if (this.lastWeightedEdgeConstructor != null) {
			// Try to use cached constructor, but throw it away if it is not the
			// correct one
			try {
				return (IWeightedEdge<?>) edgeType
						.cast(this.lastWeightedEdgeConstructor.newInstance(src,
								dst, weight));
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| ClassCastException e) {
				this.lastWeightedEdgeConstructor = null;
			}
		}

		Constructor<?>[] cList = edgeType.getConstructors();
		Constructor<?> cNeeded = null;

		// First: search matching constructor for src.getClass and dst.getClass
		Class<?>[] cRequired = new Class[] { src.getClass(), dst.getClass(),
				weight.getClass() };
		cNeeded = getConstructor(cList, cRequired);

		// Okay, check for super types if needed
		if (cNeeded == null) {
			Class<?> superType;
			superType = src.getClass().getSuperclass();
			while (cNeeded == null && Node.class.isAssignableFrom(superType)) {
				cRequired = new Class[] { superType, superType,
						weight.getClass() };
				cNeeded = getConstructor(cList, cRequired);
				superType = superType.getSuperclass();
			}
		}

		if (cNeeded == null) {
			throw new RuntimeException("No edge constructor for nodetype "
					+ src.getClass() + " in edge type " + edgeType + " found");
		}

		try {
			this.lastWeightedEdgeConstructor = cNeeded;
			return (IWeightedEdge<?>) edgeType.cast(cNeeded.newInstance(src,
					dst, weight));
		} catch (SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			RuntimeException rt = new RuntimeException(
					"Could not generate new edge instance");
			rt.setStackTrace(e.getStackTrace());
			throw rt;
		}
	}

	private Class<?> getWeightType(Class<?> in, Class<?> superInterface) {
		Class<?> weightType = null;

		// Get the type of weight
		for (Type ptU : in.getGenericInterfaces()) {
			ParameterizedType pt = (ParameterizedType) ptU;
			if (pt.getRawType() != superInterface)
				continue;
			Type[] args = pt.getActualTypeArguments();
			weightType = (Class<?>) args[0];
			return weightType;
		}
		return null;
	}

	public Class<?> getNodeWeightType() {
		return this.getWeightType(nodeType, IWeightedNode.class);
	}

	public Class<?> getEdgeWeightType() {
		return this.getWeightType(edgeType, IWeightedEdge.class);
	}

	public boolean createsDirected() {
		return DirectedEdge.class.isAssignableFrom(edgeType);
	}

	public boolean createsUndirected() {
		return UndirectedEdge.class.isAssignableFrom(edgeType);
	}

	public String getStorageDataStructures(boolean getSimpleNames) {
		if (getSimpleNames) {
			return listTypes.get(ListType.GlobalNodeList).getSimpleName()
					+ Config.get("DATASTRUCTURES_CLASS_DELIMITER")
					+ (listTypes.get(ListType.GlobalEdgeList) == null ? "null"
							: listTypes.get(ListType.GlobalEdgeList)
									.getSimpleName())
					+ Config.get("DATASTRUCTURES_CLASS_DELIMITER")
					+ (listTypes.get(ListType.LocalEdgeList) == null ? "null"
							: listTypes.get(ListType.LocalEdgeList)
									.getSimpleName());
		} else {
			return listTypes.get(ListType.GlobalNodeList).getName()
					+ Config.get("DATASTRUCTURES_CLASS_DELIMITER")
					+ (listTypes.get(ListType.GlobalEdgeList) == null ? "null"
							: listTypes.get(ListType.GlobalEdgeList)
									.getName())
					+ Config.get("DATASTRUCTURES_CLASS_DELIMITER")
					+ (listTypes.get(ListType.LocalEdgeList) == null ? "null"
							: listTypes.get(ListType.LocalEdgeList)
									.getName());
		}
	}

	public String getDataStructures() {
		return getStorageDataStructures(false)
				+ Config.get("DATASTRUCTURES_CLASS_DELIMITER")
				+ nodeType.getName()
				+ Config.get("DATASTRUCTURES_CLASS_DELIMITER")
				+ edgeType.getName();
	}

	public boolean isReadable() {
		return IReadable.class
				.isAssignableFrom((Class<? extends IDataStructure>) listTypes.get(ListType.GlobalEdgeList))
				&& IReadable.class
						.isAssignableFrom((Class<? extends IDataStructure>) listTypes.get(ListType.GlobalNodeList));
	}

	public boolean isReadable(IDataStructure list) {
		return isReadable(list.getClass());
	}

	public boolean isReadable(Class<? extends IDataStructure> list) {
		return IReadable.class.isAssignableFrom(list);
	}

	/**
	 * Switch data structures from the current setting stored here to another
	 * combination. Use the graph g as an entry point into the graph. We could
	 * also store a pointer to the graph within this object, but this currently
	 * looks more suitable.
	 * 
	 * @param newGDS
	 * @param g
	 */
	public void switchDatastructures(GraphDataStructure newGDS, Graph g) {
		if (!this.isReadable(listTypes.get(ListType.GlobalEdgeList))) {
			System.err
					.println("Reject switching data structures, as graph edge list of type "
							+ this.listTypes.get(ListType.GlobalEdgeList)
							+ " cannot be converted");
			return;
		}
		if (!this.isReadable(listTypes.get(ListType.LocalEdgeList))) {
			System.err
					.println("Reject switching data structures, as node edge list of type "
							+ this.listTypes.get(ListType.LocalEdgeList)
							+ " cannot be converted");
			return;
		}
		if (!this.isReadable(listTypes.get(ListType.GlobalNodeList))) {
			System.err
					.println("Reject switching data structures, as node list of type "
							+ this.listTypes.get(ListType.GlobalNodeList)
							+ " cannot be converted");
			return;
		}

		if (this.listTypes.get(ListType.LocalEdgeList) != newGDS
				.getLocalEdgeListType()) {
			this.listTypes.put(ListType.LocalEdgeList,
					newGDS.getLocalEdgeListType());
			g.switchDataStructure(ListType.LocalEdgeList,
					this.newLocalEdgeList());
		}
		if (this.listTypes.get(ListType.GlobalEdgeList) != newGDS
				.getGlobalEdgeListType()) {
			this.listTypes.put(ListType.GlobalEdgeList,
					newGDS.getGlobalEdgeListType());
			g.switchDataStructure(ListType.GlobalEdgeList,
					this.newGlobalEdgeList());
		}
		if (this.listTypes.get(ListType.GlobalNodeList) != newGDS
				.getGlobalNodeListType()) {
			this.listTypes.put(ListType.GlobalNodeList,
					newGDS.getGlobalNodeListType());
			g.switchDataStructure(ListType.LocalNodeList,
					this.newList(ListType.LocalNodeList));
			g.switchDataStructure(ListType.GlobalNodeList,
					this.newList(ListType.GlobalNodeList));
		}
	}

	private Complexity getComplexityClass(Class<? extends IDataStructure> ds,
			Class<? extends IElement> dt, ProfilerDataType complexityType,
			AccessType at, Base b) {
		return ProfilerMeasurementData.get(complexityType, ds.getSimpleName(),
				at, dt.getSimpleName(), b);
	}

	public Complexity getComplexityClass(ListType lt, AccessType at,
			ProfilerDataType complexityType) {
		Class<? extends IDataStructure> listClass = listTypes.get(lt);
		Class<? extends IElement> storedElement = null;
		Base baseType = null;

		switch (lt) {
		case GlobalEdgeList:
			storedElement = Edge.class;
			baseType = Base.EdgeSize;
			break;
		case GlobalNodeList:
			storedElement = Node.class;
			baseType = Base.NodeSize;
			break;
		case LocalEdgeList:
			storedElement = Edge.class;
			baseType = Base.Degree;
			break;
		case LocalNodeList:
			storedElement = Node.class;
			baseType = Base.Degree;
			break;
		}

		return getComplexityClass(listClass, storedElement, complexityType, at,
				baseType);
	}

	public static EnumMap<ListType, Class<? extends IDataStructure>> getList(
			ListType l1, Class<? extends IDataStructure> c1) {
		EnumMap<ListType, Class<? extends IDataStructure>> res = new EnumMap<ListType, Class<? extends IDataStructure>>(
				ListType.class);
		res.put(l1, c1);
		return res;
	}

	public static EnumMap<ListType, Class<? extends IDataStructure>> getList(
			ListType l1, Class<? extends IDataStructure> c1, ListType l2,
			Class<? extends IDataStructure> c2) {
		EnumMap<ListType, Class<? extends IDataStructure>> res = getList(l1, c1);
		res.put(l2, c2);
		return res;
	}

	public static EnumMap<ListType, Class<? extends IDataStructure>> getList(
			ListType l1, Class<? extends IDataStructure> c1, ListType l2,
			Class<? extends IDataStructure> c2, ListType l3,
			Class<? extends IDataStructure> c3) {
		EnumMap<ListType, Class<? extends IDataStructure>> res = getList(l1,
				c1, l2, c2);
		res.put(l3, c3);
		return res;
	}

	public static EnumMap<ListType, Class<? extends IDataStructure>> getList(
			ListType l1, Class<? extends IDataStructure> c1, ListType l2,
			Class<? extends IDataStructure> c2, ListType l3,
			Class<? extends IDataStructure> c3, ListType l4,
			Class<? extends IDataStructure> c4) {
		EnumMap<ListType, Class<? extends IDataStructure>> res = getList(l1,
				c1, l2, c2, l3, c3);
		res.put(l4, c4);
		return res;
	}

	public static EnumMap<ListType, Class<? extends IDataStructure>> getList(
			ListType l1, Class<? extends IDataStructure> c1, ListType l2,
			Class<? extends IDataStructure> c2, ListType l3,
			Class<? extends IDataStructure> c3, ListType l4,
			Class<? extends IDataStructure> c4, ListType l5,
			Class<? extends IDataStructure> c5) {
		EnumMap<ListType, Class<? extends IDataStructure>> res = getList(l1,
				c1, l2, c2, l3, c3, l4, c4);
		res.put(l5, c5);
		return res;
	}
}
