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

package com.drgarbage.controlflowgraphfactory.compare.actions;

import java.awt.event.MouseAdapter;
import java.util.Map;


import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

import com.drgarbage.controlflowgraph.figures.RectangleFigure;
import com.drgarbage.controlflowgraph.intf.INodeExt;
import com.drgarbage.visualgraphic.model.VertexBase;

/**
 * Describes mouse events, to highlight mapped nodes
 * according isomorphism algorithms
 * 
 * @author Artem Garishin
 * @version $Revision$
 * $Id$
 */
public class CompareMouseActions extends MouseAdapter implements  MouseListener, MouseMotionListener{

	/* Color constants */
	final static Color RED      		= new Color(null, 224, 0, 0);
	final static Color GREEN      		= new Color(null, 0, 224, 0);
	final static Color BLUE      		= new Color(null, 50, 0, 232);
	final static Color YELLOW      		= new Color(null, 255, 255, 0);
	final static Color DEFAULT      	= new Color(null, 0, 224, 0);
	
	/*define mapped nodes and figure which needs to attach mouse events*/
	public Map<INodeExt, INodeExt> MapEntry;
	public IFigure myFigure;
	
	public CompareMouseActions(Map<INodeExt, INodeExt> MapEntry, IFigure myFigure){
		this.MapEntry = MapEntry;
		this.myFigure = myFigure;
	}
	
	public void addMouseListener(){
		myFigure.addMouseListener(this);
	}
	public void removeListener(){
		myFigure.removeMouseListener(this);
	}
	
	public void addMotionMouseListener(){
		myFigure.addMouseMotionListener(this);
	}
	public void removeMotionListener(){
		myFigure.removeMouseMotionListener(this);
	}
	
	/**
	 * Mouse Listeners methods 
	 */
	public void mouseClicked(MouseEvent arg0){
		System.out.println(arg0.getLocation());
	}
	
	public void mouseDoubleClicked(MouseEvent arg0) {
	  
		Point p = arg0.getLocation();
		IFigure foundFigure = myFigure.findFigureAt(p);
    	
		/*check if found figure is node*/
		if(foundFigure.getParent() instanceof RectangleFigure){
    	
		RectangleFigure rectangleFigure = (RectangleFigure) foundFigure.getParent();
		VertexBase foundVertexBase = identifyMappedVertexBase(rectangleFigure, MapEntry);
		
		if(rectangleFigure != null && foundVertexBase != null){
    		rectangleFigure.setBackgroundColor(DEFAULT);
    		foundVertexBase.setColor(DEFAULT);
			}
		}
	}

	public void mousePressed(MouseEvent arg0) {
		Point p = arg0.getLocation();
		IFigure foundFigure = myFigure.findFigureAt(p);
    	
		/*check if found figure is node*/
		if(foundFigure.getParent() instanceof RectangleFigure){
    	
		RectangleFigure rectangleFigure = (RectangleFigure) foundFigure.getParent();
		VertexBase foundVertexBase = identifyMappedVertexBase(rectangleFigure, MapEntry);
		
		if(rectangleFigure != null && foundVertexBase != null){
    		rectangleFigure.setBackgroundColor(BLUE);
    		foundVertexBase.setColor(BLUE);
			}
		}
	}

	public void mouseReleased(MouseEvent arg0) {	
	}
	
	/**
	 * Mouse Motion Listeners methods 
	 */
	public void mouseDragged(MouseEvent arg0) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		
		
	}

	public void mouseExited(MouseEvent arg0) {
		
		
	}

	public void mouseHover(MouseEvent arg0) {
		Point p = arg0.getLocation();
		IFigure foundFigure = myFigure.findFigureAt(p);
    	
		/*check if found figure is node*/
		if(foundFigure.getParent() instanceof RectangleFigure){
    	
		RectangleFigure rectangleFigure = (RectangleFigure) foundFigure.getParent();
		VertexBase foundVertexBase = identifyMappedVertexBase(rectangleFigure, MapEntry);
		
		if(rectangleFigure != null && foundVertexBase != null){
    		rectangleFigure.setBackgroundColor(YELLOW);
    		foundVertexBase.setColor(YELLOW);
			}
		}
	}

	public void mouseMoved(MouseEvent arg0) {
			
	}
	
	/**
	 * gets a found figure from left viewer, 
	 * returns a corresponding mapped node to be later highlighted 
	 * 
	 * return null if vertexBase is not found
	 * @param rectangleFigure
	 * @param MapEntry
	 */
	public VertexBase identifyMappedVertexBase(RectangleFigure rectangleFigure, Map<INodeExt, INodeExt> MapEntry){
		
		/*get figure locations */
		int figurex = rectangleFigure.getBounds().x;
		int figurey = rectangleFigure.getBounds().y;
		int figureHeight = rectangleFigure.getBounds().height; 
		int figureWidth = rectangleFigure.getBounds().width;
		
		/*find corresponding vertexBase node according figure location*/
		for (Map.Entry<INodeExt, INodeExt> entry : MapEntry.entrySet()) {
			
			/*get each location of mapped vertexBase*/
			VertexBase vb = ((VertexBase) entry.getKey().getData());
    		int vbx = vb.getLocation().x;
    		int vby = vb.getLocation().y;
    		int vbSizeHeight = vb.getSize().height;
    		int vbSizeWidth = vb.getSize().width;

    		/*identify vertexBase by location of figure*/
    		if(figurex == vbx && figurey == vby && figureHeight == vbSizeHeight && figureWidth == vbSizeWidth){
    			return (VertexBase) entry.getValue().getData();
    		}
		}
		return null;
	}

}
