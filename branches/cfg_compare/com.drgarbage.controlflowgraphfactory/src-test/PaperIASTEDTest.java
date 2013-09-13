package test;

public class PaperIASTEDTest {
	
	public int m1(int a, int b){

		int i = 0;

		if(a < b){
			i = a;
		}
		
		if(a > b){
			i = b;
			
			if(a < b){
				i = a;
			}
		}
		else{
			if(a < b){
				i = a;
			}
			
			if(a < b){
				i = a;
			}
			
			return i;
		}

		return i;
	}

	public int m1a(int a, int b){

		int i = 0;

		if(a < b){
			i = a;
		}
		
		if(a > b){
			i = b;
			i++;
			if(a < b){
				i = a;
			}
		}
		else{
			if(a < b){
				i = a;
			}
			
			if(a < b){
				i = a;
			}
			
			return i;
		}

		return i;
	}
	
	public int m1b(int a, int b){

		int i = 0;

		if(a < b){
			i = a;
		}
		
		if(a > b){
			i = b;
			
			if(a < b){
				i = a;
			}
			
			if(a < b){
				i = a;
			}
		}
		else{
			if(a < b){
				i = a;
			}
			
			if(a < b){
				i = a;
			}
			
			return i;
		}

		return i;
	}
	
	public void m2(int d){
		int a = 0;

		switch(d){
		case 0: a = 2;
		break;
		case 1: d = 10;
			if(d > a){
				d++;
			}
			break;
		case 2: d = 11;
			break;
		case 3: a--;
		break;
		}
		
		a++;
	}
	
	public void m2b(int d){
		int a = 0;

		switch(d){
		case 0: a = 2;
		break;
		case 1: d = 10;
			if(d > a){
				d++;
			}
			break;
		case 2: d = 11;
			break;
		case 3: a--;
		break;
		case 4: a*=2;
		break;
		}
		
		a++;
	}
	
	public void bla(int a){
		if(a == 1){
			
		}
		else if(a == 2);
		else {
			a = 3;
		}
	}
	
	
}
