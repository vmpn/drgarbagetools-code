/**
 * 
 */
package com.drgarbage.algorithms;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.GraphExtentionFactory;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;

/**
 * Bipartite algorithm based on BFS.
 * 
 * @author Sergej Alekseev
 * 
 * @version $Revision$
 * 			$Id$
 */
public class BipartBFS extends BFSBase {

	/* Two sets for storing the vertices of the partitions */
	private INodeListExt partition[];
	
	/* 
	 * Step counter: we start with the value 1
	 * all nodes counter are initialized with 
	 * the value 0.
	 */
	private int stepCounter = 1;

	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#start(com.drgarbage.controlflowgraph.intf.IDirectedGraphExt)
	 */
	public void start(IDirectedGraphExt graph) throws ControlFlowGraphException{
	
	partition = new INodeListExt[2];
		partition[0] = GraphExtentionFactory.createNodeListExtention();
		partition[1] = GraphExtentionFactory.createNodeListExtention();
		super.start(graph);
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#start(com.drgarbage.controlflowgraph.intf.IDirectedGraphExt, com.drgarbage.controlflowgraph.intf.INodeExt)
	 */
	public void start(IDirectedGraphExt graph, INodeExt start) throws ControlFlowGraphException{
		partition = new INodeListExt[2];
		partition[0] = GraphExtentionFactory.createNodeListExtention();
		partition[1] = GraphExtentionFactory.createNodeListExtention();
		super.start(graph, start);
	}
	
	/**
	 * Returns the partitions of vertices if the graph is
	 * bipartite, otherwise <code>null</code>.
	 * @return the set of partition
	 */
	public INodeListExt[] getPartition() {
		return partition;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#visitedNode(com.drgarbage.controlflowgraph.intf.INodeExt)
	 */
	@Override
	protected void visitedNode(INodeExt node) {
		node.setCounter(stepCounter);
		
		if((stepCounter % 2) == 0){
			partition[0].add(node);
		}
		else{
			partition[1].add(node);
		}
	}

	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#visitedEdge(com.drgarbage.controlflowgraph.intf.IEdgeExt)
	 */
	@Override
	protected void visitedEdge(IEdgeExt edge) {
		int source = edge.getTarget().getCounter();
		if( source != 0){
			int target = edge.getSource().getCounter();
		
	if(source %2 != target %2){
				/* not bipartite */
				//TODO: implement stop method in the BFSBase
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#enqueue(com.drgarbage.controlflowgraph.intf.INodeExt)
	 */
	@Override

	protected void enqueue(INodeExt node) {
		stepCounter = node.getCounter() + 1;
	}

	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#dequeue(com.drgarbage.controlflowgraph.intf.INodeExt)
	 */
	@Override
	protected void dequeue(INodeExt node) {
		/* nothing to to */

	}

}