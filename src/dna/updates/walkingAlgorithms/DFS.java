package dna.updates.walkingAlgorithms;

import java.util.LinkedList;

import dna.graph.Graph;
import dna.graph.nodes.Node;
import dna.graph.startNodeSelection.StartNodeSelectionStrategy;
import dna.util.parameters.Parameter;

/**
 * A sampling algorithm based on deep first search
 * 
 * @author Benedict Jahn
 * 
 */
public class DFS extends WalkingAlgorithm {

	private Node currentNode;
	private LinkedList<Node> queue;

	/**
	 * Creates an instance of the depth first sampling algorithm
	 * 
	 * @param name
	 *            the name of this instance
	 * @param fullGraph
	 *            the graph the algorithm shall walk on
	 * @param startNodeStrat
	 *            the strategy how the algorithm will select the first node
	 * @param onlyVisitedNodesToGraph
	 *            if set to true the generator will only put visited nodes in
	 *            the batch
	 * @param costPerBatch
	 *            how many steps the algorithm shall perform for one batch
	 * @param ressouce
	 *            the maximum count of steps the algorithm shall perform, if
	 *            initialized with 0 or below the algorithm will walk until the
	 *            graph is fully visited
	 * @param parameters
	 *            the parameters which makes this algorithm unique and which
	 *            will be added to the name
	 */
	public DFS(String name, Graph fullGraph,
			StartNodeSelectionStrategy startNodeStrategy,
			boolean onlyVisitedNodesToGraph, int costPerBatch, int resource,
			Parameter[] parameters) {
		super(name, fullGraph, startNodeStrategy, onlyVisitedNodesToGraph,
				costPerBatch, resource, parameters);

		queue = new LinkedList<Node>();
		currentNode = null;
	}

	@Override
	protected Node findNextNode() {
		if (queue.isEmpty()) {
			noNodeFound();
			return null;
		}
		currentNode = queue.removeLast();
		queue.addAll(getUnvisitedNeighbors(currentNode));
		return currentNode;
	}

	@Override
	protected Node init(StartNodeSelectionStrategy startNode) {
		currentNode = startNode.getStartNode();
		queue.addAll(getUnvisitedNeighbors(currentNode));
		return currentNode;
	}

}
