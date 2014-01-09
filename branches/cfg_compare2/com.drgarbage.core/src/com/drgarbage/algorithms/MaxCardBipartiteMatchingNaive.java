package com.drgarbage.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.drgarbage.controlflowgraph.intf.GraphUtils;
import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;

public class MaxCardBipartiteMatchingNaive {
	
	private List<IEdgeExt> m = new ArrayList<IEdgeExt>();

	public void start(IDirectedGraphExt g, List<INodeExt> partition1,
			List<INodeExt> partition2) {
		
		/* start once with every node in left partition */
		for(INodeExt n : partition1) {
			List<INodeExt> g1 = new ArrayList<INodeExt>();
			
			GraphUtils.clearGraph(g);
			
			/* create subset of left partition without current node */
			g1.addAll(partition1);
			g1.remove(n);
			
			IEdgeListExt outgoingEdges = n.getOutgoingEdgeList();
			
			/* start once with every outgoing edge of current node in left partition */
			for(int i = 0; i < outgoingEdges.size(); i++) {
				
				IEdgeExt e = outgoingEdges.getEdgeExt(i);
				
				if(e.getTarget().isVisited()) {
					continue;
				}
				
				List<IEdgeExt> tmpM = new ArrayList<IEdgeExt>();
				
				tmpM.add(e);
				e.getTarget().setVisited(true);
				
				startRec(g1, tmpM);
				
				e.getTarget().setVisited(false);
				
				if(tmpM.size() > m.size()) {
					m = tmpM;
				}
			}
		}
	}

	private void startRec(List<INodeExt> g, List<IEdgeExt> tmpM) {
		
		for(INodeExt n : g) {
			List<INodeExt> g1 = new ArrayList<INodeExt>();
			
			/* create subset of current left partition without current node */
			g1.addAll(g);
			g1.remove(n);
			
			IEdgeListExt outgoingEdges = n.getOutgoingEdgeList();
			
			/* start once with every outgoing edge of current node in left partition */
			for(int i = 0; i < outgoingEdges.size(); i++) {
				
				IEdgeExt e = outgoingEdges.getEdgeExt(i);
				
				if(e.getTarget().isVisited()) {
					continue;
				}
				
				tmpM.add(e);
				e.getTarget().setVisited(true);
				
				startRec(g1, tmpM);
				
				e.getTarget().setVisited(false);
				
				if(tmpM.size() > m.size()) {
					m = tmpM;
				}	
			}
		}
	}

	public List<IEdgeExt> getMatchingEdgeList() {
		return m;
	}

}
