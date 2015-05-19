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

package com.drgarbage.visualgraphic.model;

import com.drgarbage.controlflowgraph.ControlFlowGraphGenerator;

public class StartVertex extends RoundedRectangularVertex {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8337450843776088576L;

	public StartVertex() {
		super();
		setLabel(ControlFlowGraphGenerator.VIRTUAL_START_NODE_TEXT);
		setToolTip(ControlFlowGraphGenerator.VIRTUAL_START_NODE_TOOLTIP_TEXT);
	}

}