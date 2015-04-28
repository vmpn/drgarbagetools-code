package com.drgarbage.core.algorithms;

public class TwoMethodsWithSameBasicBlockCountDifferentEdgeCount {
	
	public void fourBasicBlocksFiveEdges(){
		
		int a = 2;
		
		if(a == 2){
			a++;
			if(a == 3)
				a--;
		}


	}
	
	public void fourBasicBlocksFourEdges(){
		
		int a = 1;
		
		if(a == 1)
			a--;
		else a++;
		
	}

}
