package com.drgarbage.core.algorithms;

public class TwoMethodsWithDifferentBasicBlockCountSameEdgeCount {
	
	public void fourBasicBlocksFiveEdges(){
		
		int a = 2;
		
		if(a == 2){
			a++;
		}

		a--;
	}
	
	public void fourBasicBlocksFourEdges(){
		
		int a = 1;
		
		if(a == 1)
			a--;
		else return;
		
	}

}
