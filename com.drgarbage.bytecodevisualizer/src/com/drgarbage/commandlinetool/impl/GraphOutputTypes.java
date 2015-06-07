/**
 * Copyright (c) 2008-2015, Dr. Garbage Community
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
package com.drgarbage.commandlinetool.impl;

/**
 * An enum class to set the GraphOutputType while parsing the arguments.
 * 
 * @author Cihan Aydin
 * @version $Revision: 723 $
 * $Id: GraphOutputTypes.java 723 2015-05-12 08:40:35Z cihanaydin $
 */
//TODO verschieben interfaces
public enum GraphOutputTypes {
	ExportFormat_Dr_Garbage_Graph,
	ExportFormat_DOT_Graph_Language,
	ExportFormat_GraphXML_XML_Based,
	ExportFormat_GraphML_XML_Based,
	ExportFormat_PrintNodes,
	ExportFormat_ByteCode, 
	ExportFormat_SourceCodeGraph,
	ExportFormat_ByteCodeGraph
}
