/**
 * Copyright (c) 2008-2012, Dr. Garbage Community
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

import org.eclipse.draw2d.graph.NodeList;

import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.controlflowgraph.intf.INodeListExt;

/**
 * Node List extention structure.
 *
 * @author Sergej Alekseev 
 * @version $Revision$
 * $Id$
 */
public class NodeListExt extends NodeList implements INodeListExt{

	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.drgarbage.visualgraphic.controlflowgraph.algorithms.intf.INodeListExt#getNodeExt(int)
	 */
	public INodeExt getNodeExt(int index) {
		return (INodeExt)super.get(index);
	}

}
