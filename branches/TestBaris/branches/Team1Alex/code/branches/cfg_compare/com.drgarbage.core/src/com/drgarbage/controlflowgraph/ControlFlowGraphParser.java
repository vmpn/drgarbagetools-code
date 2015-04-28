/**
 * Copyright (c) 2008-2013, Dr. Garbage Community
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drgarbage.controlflowgraph;

import java.util.List;

import com.drgarbage.controlflowgraph.intf.IDirectedGraphExt;
import com.drgarbage.controlflowgraph.intf.IEdgeExt;
import com.drgarbage.controlflowgraph.intf.IEdgeListExt;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;

/**
 * Collection of methods for parsing control flow graphs into various formats
 *
 * @author Adam Kajrys, Andreas Karoly
 * @version $Revision$
 * $Id$
 */
public class ControlFlowGraphParser {
	
	public static String toGHS(IDirectedGraphExt graph) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("$GRAPH" + "method name dummy" + "\n");
		sb.append("$VERTICES ");
		
		int ctr = 0;
		
		INodeListExt nodeList = graph.getNodeList();
		
		for(int i = 0; i < nodeList.size(); i++) {
			INodeExt node = nodeList.getNodeExt(i);
			
			sb.append(node.getByteCodeOffset() + " ");
			
			ctr++;
			if(ctr%10 == 0)
				sb.append("\n          ");
		}
		
		sb.append("\n\n$EDGES\n");
		buildEdgesString(sb, graph);
		
		sb.append("$END");
		
		return sb.toString();
	}

	public static String toGHS(List<IDirectedGraphExt> graphList) {
		if(graphList.isEmpty())
			return "";
		
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("$VERTICES ");
		
		int ctr = 0;
		for(IDirectedGraphExt graph : graphList) {
			INodeListExt nodeList = graph.getNodeList();
			
			for(int i = 0; i < nodeList.size(); i++) {
				INodeExt node = nodeList.getNodeExt(i);
				
				sb.append(node.getByteCodeOffset() + " ");
				
				ctr++;
				if(ctr%10 == 0)
					sb.append("\n          ");
			}
		}
		
		sb.append("\n\n$EDGES\n");
		
		for(IDirectedGraphExt graph : graphList) {
			buildEdgesString(sb, graph);
		}
		
		sb.append("$END");
		
		return sb.toString();
	}
	
	public static String toGHSAppendMode(List<IDirectedGraphExt> graphList) {
		StringBuilder sb = new StringBuilder();
		
		for(IDirectedGraphExt graph : graphList) {
			sb.append(toGHS(graph));
			sb.append("\n\n");			
		}
		
		return sb.toString();
	}

	private static void buildEdgesString(StringBuilder sb, IDirectedGraphExt graph) {
		INodeListExt nodeList = graph.getNodeList();
		
		for(int i = 0; i < nodeList.size(); i++) {
			INodeExt node = nodeList.getNodeExt(i);
			IEdgeListExt edgeList = node.getOutgoingEdgeList();
			
			if(edgeList.size() > 0) {
				for(int j = 0; j < edgeList.size(); j++) {
					IEdgeExt edge = edgeList.getEdgeExt(j);
					INodeExt target = edge.getTarget();
					
					sb.append("_d*" + node.getByteCodeOffset() + "*" + target.getByteCodeOffset()
							+ "  " + node.getByteCodeOffset() + " " + target.getByteCodeOffset()
							+ "\t");
				}
				sb.append("\n");
			}
		}
	}
}
