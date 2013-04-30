package test;

/**
 * Test for type based analysis.
 * Use *m.class file to verify the stack.
 * @author Sergej Alekseev
 */
public class C5 {
	
	int test(int a, int b){
		return diff(a > b ? a: b, a < b ? a: b);
	}
	
	int diff(int a, int b){
		if( a > b){
			return a;
		}
		return b;
	}
	
	public static void main(String argv[]){
		C5 c5 = new C5();
		int a = c5.test(2, 3);
		System.out.println(a);
	}
}
