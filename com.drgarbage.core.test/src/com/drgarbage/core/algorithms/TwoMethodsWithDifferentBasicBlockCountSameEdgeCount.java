package com.drgarbage.core.algorithms;

public class TwoMethodsWithDifferentBasicBlockCountSameEdgeCount {
	
	public void threeBasicBlocksThreeEdges(){
		
		int a = 2;
		
		if(a == 2){
			a++;
		}

		a--;
	}
	
	public void fourBasicBlocksThreeEdges(){
		
		int a = 1;
		
		if(a == 1)
		try {
			a--;
		} catch (Exception e) {
			return;
		}

		
	}

}
