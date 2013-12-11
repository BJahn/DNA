package dna.metrics.motifs.exceptions;

import dna.graph.edges.UndirectedEdge;
import dna.metrics.motifs.UndirectedMotif;

public class UndirectedMotifInvalidEdgeAdditionException extends
		UndirectedMotifException {

	private static final long serialVersionUID = 3486630928088060613L;

	public UndirectedMotifInvalidEdgeAdditionException(UndirectedMotif m,
			UndirectedEdge e) {
		super(m, e);
	}

	public String toString() {
		return "cennot add edge " + this.e + " to motif:\n" + this.m.toString();
	}

}
