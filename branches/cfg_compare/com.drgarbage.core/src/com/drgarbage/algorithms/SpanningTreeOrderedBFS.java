package com.drgarbage.algorithms;

import java.util.HashMap;
import java.util.Map;

import com.drgarbage.controlflowgraph.ControlFlowGraphException;
import com.drgarbage.controlflowgraph.intf.GraphExtentionFactory;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;

public class SpanningTreeOrderedBFS extends BFSOrderedTraversal {
	
	/**
	 * Spanning tree graph.
	 */
	private IDirectedGraphExt spanningTree;
	
	private int traverseCount = 0;
	
	/**
	 * Map of nodes <b>old graph</b> <-> <b>new graph</b>
	 */
	private Map<INodeExt, INodeExt> mapNodeList;
	
	/**
	 * If the variable is true a new graph for the spanning tree is created.
	 * Otherwise the original graph is modified to the spanning tree.
	 */
	private boolean createNewGraph = true;
	
	/**
	 * Set true if a new graph for the spanning tree has to be created.
	 * Otherwise the original graph is modified to the spanning tree. 
	 * @param createNewGraph - true or false
	 */
	public void setCreateNewGraph(boolean createNewGraph) {
		this.createNewGraph = createNewGraph;
	}

	/**
	 * Returns the spanning tree graph.
	 * @return the spanning tree graph
	 */
	public IDirectedGraphExt getSpanningTree(){
		return spanningTree;
	}
	
	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#start(com.drgarbage.controlflowgraph.intf.IDirectedGraphExt)
	 */
	@Override
	public void start(IDirectedGraphExt graph) throws ControlFlowGraphException{
		
		traverseCount = 0;
		
		resetVisitedFlags(graph);

		if(createNewGraph){
			spanningTree = GraphExtentionFactory.createDirectedGraphExtention();
			mapNodeList = new HashMap<INodeExt, INodeExt>();

			INodeListExt oldNodeList = graph.getNodeList();
			INodeListExt newNodeList = spanningTree.getNodeList();

			/* create a map of the old nodes to the new nodes */
			for (int i = 0; i < oldNodeList.size(); i++){
				INodeExt oldNode = oldNodeList.getNodeExt(i);
				INodeExt newNode = GraphExtentionFactory.createNodeExtention(oldNode.getData());
				mapNodeList.put(oldNode,  newNode);

				/* copy node property */
				copyNodeProperties(oldNode, newNode);

				/* copy the list of the new nodes to the new graph */
				newNodeList.add(newNode);
			}
		}
		else{
			spanningTree = graph;
			mapNodeList = null;
		}
		
		/* start algorithm */
		super.start(graph, graph.getNodeList().getNodeExt(0));
	}

	private void resetVisitedFlags(IDirectedGraphExt graph) {
		for(int i = 0; i < graph.getEdgeList().size(); i++){
			graph.getEdgeList().getEdgeExt(i).setVisited(false);
		}
		
		for(int i = 0; i < graph.getNodeList().size(); i++){
			graph.getNodeList().getNodeExt(i).setVisited(false);
		}
	}

	/**
	 * Copies all properties of the old node to the new node
	 * @param oldNode - the old node
	 * @param newNode - the new node
	 */
	private void copyNodeProperties(INodeExt oldNode, INodeExt newNode){
		newNode.setByteCodeOffset(oldNode.getByteCodeOffset());
		newNode.setByteCodeString(oldNode.getByteCodeString());
		//TODO: copy all properties
	}

	/* (non-Javadoc)
	 * @see com.drgarbage.algorithms.BFSBase#visitedEdge(com.drgarbage.controlflowgraph.intf.IEdgeExt)
	 */
	@Override
	protected void visitedEdge(IEdgeExt edge) {
		System.out.println("visited edge: " +edge.getSource().getByteCodeOffset()+" -> "+ edge.getTarget().getByteCodeOffset());

		if(!edge.getTarget().isVisited()){
			if(createNewGraph){
				/* find the source and target node, create new edge */
				INodeExt source = mapNodeList.get(edge.getSource());
				INodeExt target = mapNodeList.get(edge.getTarget());
				IEdgeExt newedge = GraphExtentionFactory.createEdgeExtention(source, target);
				spanningTree.getEdgeList().add(newedge);
			}
		}
		else{
			System.out.println("target from edge: " +edge.getSource().getByteCodeOffset()+" -> "+ edge.getTarget().getByteCodeOffset() + " already visited");
			if(!createNewGraph){
				edge.getSource().getOutgoingEdgeList().remove(edge);
				edge.getTarget().getIncomingEdgeList().remove(edge);
				spanningTree.getEdgeList().remove(edge);
			}
		}
	}

	@Override
	protected void visitedNode(INodeExt node) {
		System.out.println("Visited Node: " +node.getByteCodeOffset());
		
		node.setCounter(traverseCount++);
		
		/* nothing to do */
	}

	@Override
	protected void enqueue(INodeExt node) {
		/* nothing to do */
	}

	@Override
	protected void dequeue(INodeExt node) {
		/* nothing to do */
	}

}
