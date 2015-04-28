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

package com.drgarbage.dot;

public interface DotKeywords {

	public static final String strict = "strict";
	public static final String subgraph = "subgraph";
	public static final String digraph = "digraph";
	public static final String graph = "graph";
	public static final String edge = "edge";
	public static final String node = "node";
	
	public static final String[] ALL = new String[] {
		strict, subgraph, digraph, graph, edge, node
	};

}
