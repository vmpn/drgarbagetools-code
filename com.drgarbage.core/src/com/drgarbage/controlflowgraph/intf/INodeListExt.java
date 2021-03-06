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

package com.drgarbage.controlflowgraph.intf;

/**
 * Node List extention structure.
 *
 * @author Sergej Alekseev  
 * @version $Revision$
 * $Id$
 */
public interface INodeListExt {

	/**
	 * Returns the Node extention object at the given index.
	 * @param index the index
	 * @return the node at a given index
	 */
	public INodeExt getNodeExt(int index);

	/**
	 * Adds a new element to the list
	 * @param obj new element of the list
	 */
	public boolean add(Object obj);
	
	/**
	 * Returns the size of the list.
	 * @return the list size
	 */
	public int size();

	/**
	 * Checks if the list contains given object.
	 * @return true if the list contains given object 
	 */
	public boolean contains(Object obj);

	/**
	 * Removes the object from the list
	 * @param obj - the object to be removed
	 * @return true if the object has been removed
	 */
	public boolean remove(Object obj);
	
	/**
	 * Removes the list element for the given index.
	 * @param i - index of the element to be removed
	 * @return the removed object 
	 */
	public Object remove(int i);
}
