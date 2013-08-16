package com.drgarbage.core.algorithms;

public class TwoMethodsWithDifferentBasicBlockCount {
	
	public void threeBasicBlocks(){
		
		int a = 1;
		int b = 2;
		int c;
		
		if(a > b)
			c = a + b;
		
		c = 2 * a;
		
	}
	
	public void oneBasicBlock(){
		
		int a = 1;
		int b = 2;
		int c;
		
		c = a + b;
		
	}

}
