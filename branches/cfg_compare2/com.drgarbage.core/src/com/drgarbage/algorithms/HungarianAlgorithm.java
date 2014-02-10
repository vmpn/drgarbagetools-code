package com.drgarbage.algorithms;
import java.util.Arrays;

/**
* @author Artem Garishin
* @version $Revision:$
* $Id:$
*/
public class HungarianAlgorithm {
	private final int[][] costMatrix;
	private final int rows, cols, size;
	private final int[] labelByB, labelByA;
	private final int[] minSlackBtoA;
	private final int[] minSlackValueA;
	private final int[] matchAtoB, matchBtoA;
	private final int[] parentBByCommittedA;
	private final boolean[] committedB;

	/**
	 * Construct an instance of the algorithm.
	 * @param costMatrix
	 *           
	 */
	public HungarianAlgorithm(int[][] costMatrix) {
		
		this.size = Math.max(costMatrix.length, costMatrix[0].length);
		this.rows = costMatrix.length;
		this.cols = costMatrix[0].length;
		this.costMatrix = new int[this.size][this.size];
		for (int w = 0; w < this.size; w++) {
			if (w < costMatrix.length) {
				if (costMatrix[w].length != this.cols) {
					throw new IllegalArgumentException("Irregular cost matrix");
				}
				this.costMatrix[w] = Arrays.copyOf(costMatrix[w], this.size);
			} else {
				this.costMatrix[w] = new int[this.size];
			}
		}
		labelByB = new int[this.size];
		labelByA = new int[this.size];
		minSlackBtoA = new int[this.size];
		minSlackValueA = new int[this.size];
		committedB = new boolean[this.size];
		parentBByCommittedA = new int[this.size];
		matchAtoB = new int[this.size];
		Arrays.fill(matchAtoB, -1);
		matchBtoA = new int[this.size];
		Arrays.fill(matchBtoA, -1);
	}
		
		/**
		 * Execute the algorithm.
		 * start point
		 */
		public int[] execute() {
			
			/* initialization to get max weighted matching*/
			initMatrix();
			printMatrix();
			
			/* reduce matrix*/
			reduce();
			printMatrix();
			
			computeInitialFeasibleSolution();
			
			/*find initial possible matches*/
			getInitialMatch();
			
			System.out.println("A->B:");
			printLabel(matchAtoB);
			System.out.println("\nB->A:");
			printLabel(matchBtoA);
			System.out.println("\n");
			
			/*find position of the first of unmatched vertices from A*/
			int pos = fetchUnmatchedFromA();
			System.out.println("pos="+pos);
			
			/* if number of matched vertices less than dimension - not max matching */
			while (pos < size) {
				
				initializePhase(pos);
				executePhase();
				
				pos = fetchUnmatchedFromA();
				System.out.println("pos="+pos);
			}
			
			int[] result = Arrays.copyOf(matchAtoB, rows);
			for (pos = 0; pos < result.length; pos++) {
				if (result[pos] >= cols) {
					result[pos] = -1;
				}
			}
			return result;
		}

	/**
	 * Compute an initial feasible solution by assigning zero labels to the
	 * A nodes and by assigning to each B-node a label equal to the maximun cost
	 * among its incident edges.
	 */
	protected void computeInitialFeasibleSolution() {
		for (int i = 0; i < size; i++) {
			labelByA[i] = Integer.MAX_VALUE;
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (costMatrix[i][j] < labelByA[j]) {
					labelByA[j] = costMatrix[i][j];
				}
			}
		}
		
	}



	/**
	 * 
	 * The runtime of a single phase of the algorithm is O(n^2), where n is the
	 * dimension of the internal square cost matrix, since each edge is visited
	 * at most once and since increasing the labeling is accomplished in time
	 * O(n) by maintaining the minimum slack values among non-committed jobs.
	 * When a phase completes, the matching will have increased in size.
	 * 
	 * Execute a single phase of the algorithm. A phase of the Hungarian
	 * algorithm consists of building a set of committed A-vertices and a set of
	 * committed B-vertices from a root unmatched A by following alternating
	 * unmatched/matched zero-slack edges. If an unmatched B is encountered,
	 * then an augmenting path has been found and the matching is grown. If the
	 * connected zero-slack edges have been exhausted, the labels of committed
	 * A`s are increased by the minimum slack among committed A`s and
	 * non-committed B`s to create more zero-slack edges (the labels of
	 * committed jobs are simultaneously decreased by the same amount in order
	 * to maintain a feasible labeling).
	 */
	protected void executePhase() {
		while (true) {
			int minSlackB = -1, minSlackPosA = -1;
			int minSlackValue = Integer.MAX_VALUE;
			for (int j = 0; j < size; j++) {
				if (parentBByCommittedA[j] == -1) {
					if (minSlackValueA[j] < minSlackValue) {
						minSlackValue = minSlackValueA[j];
						minSlackB = minSlackBtoA[j];
						minSlackPosA = j;
					}
				}
			}
//			System.out.println("\nminSlackValue:" + minSlackValue);
			
			if (minSlackValue > 0) {
				updateLabeling(minSlackValue);
			}
//			System.out.print("\nupdate labeling");
//			
//			System.out.print("\nlabelByA: ");
//			printLabel(labelByA);
//			
//			System.out.print("\nlabelByB: ");
//			printLabel(labelByB);
//			
//			System.out.print("\nminSlackValueA: ");
//			printLabel(minSlackValueA);
			
			parentBByCommittedA[minSlackPosA] = minSlackB;
			if (matchBtoA[minSlackPosA] == -1) {
				/*
				 * An augmenting path has been found.
				 */
				int committedA = minSlackPosA;
				System.out.print("\ncommittedA:" + committedA);
				int parentB = parentBByCommittedA[committedA];
				while (true) {
					int temp = matchAtoB[parentB];
					match(parentB, committedA);
					committedA = temp;
					if (committedA == -1) {
						break;
					}
					parentB = parentBByCommittedA[committedA];
				}
				return;
			} 
			else {
				/*
				 * Update slack values since we increased the size of the
				 * committed B set.
				 */
				int Bnode = matchBtoA[minSlackPosA];
				committedB[Bnode] = true;
				for (int j = 0; j < size; j++) {
					if (parentBByCommittedA[j] == -1) {
						int slack = costMatrix[Bnode][j] - labelByB[Bnode] - labelByA[j];
						if (minSlackValueA[j] > slack) {
							minSlackValueA[j] = slack;
							minSlackBtoA[j] = Bnode;
						}
					}
				}
			}
		}
	}

	/**
	 * find the first position of unmatched node from A->B or {@link #size} if all matched.
	 * -1 show that node is unmatched
	 * a positive number indicates a match
	 * Example:
	 * TODO give example
	 * 
	 * @return int index
	 */
	protected int fetchUnmatchedFromA() {
		int index;
		for (index = 0; index < size; index++) {
			if (matchAtoB[index] == -1) {
				break;
			}
		}
		return index;
	}

	/**
	 * Find a valid matching edges where weight is 0, "striking out" useless edges  
	 * 
	 * 
	 * Example:
	 * Reduced matrix is
	 * 	  b0|b1| b2|b3
	 * a0| 6  0  8  7
	 * a1| 4  6  0  10
	 * a2| 5  5  0  5
	 * a3| 0  0  0  0
	 * Take the first zero-element (a0;b1), it is valid match because this match is not in  matchAtoB[] and matchBtoA[] (vertices a0 and a1 are free)
	 * Next zero is (a1; a2) can be also matched because vertices a1 and a2 are free to be matched
	 * Next zero is (a2, b2) cannot(!) be matched because it overlapped by  (a1; a2)
	 * 
	 * The idea of algorithm to find such zero combination in the matrix that covers all rows(A nodes) 
	 * and all columns (B nodes).
	 * 
	 */
	protected void getInitialMatch() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (matchAtoB[i] == -1
						&& matchBtoA[j] == -1
						&& costMatrix[i][j] - labelByB[i] - labelByA[j] == 0) {
					match(i, j);
				}
			}
		}
	}

	/**
	 * records into arrays matchAtoB and matchBtoA matched combinations between A and B
	 * Example
	 * Reduced matrix is
	 * 	  b0|b1| b2|b3
	 * a0| 6  0  8  7
	 * a1| 4  6  0  10
	 * a2| 5  5  0  5
	 * a3| 0  0  0  0
	 * 
	 * matchAtoB = [1, 2, -1, 0]
	 * matchBtoA = [3, 0, 1, -1]
	 * These 2 arrays keep info about matched vertices
	 * (-1) indicates unmatched vertices
	 * matchAtoB[0] = 1 indicates a0 and b1 are matched
	 * matchBtoA[0] = 3 indicates b0 and a3 are matched
	 */
	protected void match(int i, int j) {
		matchAtoB[i] = j;
		matchBtoA[j] = i;
	}
	
	/**
	 * Initialize the next phase of the algorithm by clearing the committed
	 * workers and jobs sets and by initializing the slack arrays to the values
	 * corresponding to the specified root worker.
	 * 
	 * @param pos position of B node
	 */
	protected void initializePhase(int w) {
		Arrays.fill(committedB, false);
		Arrays.fill(parentBByCommittedA, -1);
		committedB[w] = true;
		for (int i = 0; i < size; i++) {
			minSlackValueA[i] = costMatrix[w][i] - labelByB[w] - labelByA[i];
			minSlackBtoA[i] = w;
		}
		System.out.println("min slack A:");
		printLabel(minSlackValueA);
		
		System.out.println("min slack B to A:");
		printLabel(minSlackBtoA);
	}

	

	/**
	 * Reduce the cost matrix by subtracting the smallest element of each row
	 * from all elements of the row as well as the smallest element of each
	 * column from all elements of the column. Note that an optimal assignment
	 * for a reduced cost matrix is optimal for the original cost matrix.
	 */
	protected void reduce() {
		for (int i = 0; i < size; i++) {
			int min = Integer.MAX_VALUE;
			for (int j = 0; j < size; j++) {
				if (costMatrix[i][j] < min) {
					min = costMatrix[i][j];
				}
			}
			for (int j = 0; j < size; j++) {
				costMatrix[i][j] -= min;
			}
		}
		int[] min = new int[size];
		for (int j = 0; j < size; j++) {
			min[j] = Integer.MAX_VALUE;
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (costMatrix[i][j] < min[j]) {
					min[j] = costMatrix[i][j];
				}
			}
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				costMatrix[i][j] -= min[j];
			}
		}
	}

	/**
	 * Update labels with the specified slack by adding the slack value for
	 * committed workers and by subtracting the slack value for committed jobs.
	 * In addition, update the minimum slack values appropriately.
	 */
	protected void updateLabeling(int slack) {
		for (int w = 0; w < size; w++) {
			if (committedB[w]) {
				labelByB[w] += slack;
			}
		}
		for (int j = 0; j < size; j++) {
			if (parentBByCommittedA[j] != -1) {
				labelByA[j] -= slack;
			} else {
				minSlackValueA[j] -= slack;
			}
		}
	}
	
	/**
	 * Multiplies matrix by (-1) and add it to all elements
	 */
	protected void initMatrix(){
		int max = this.costMatrix[0][0];
		for(int i = 0; i < this.size; i++){
			for(int j = 0; j < this.size; j++){
				if(this.costMatrix[i][j] > max){
					max = this.costMatrix[i][j];
				}
			}
		}
		for(int i = 0; i < this.size; i++){
			for(int j = 0; j < this.size; j++){
				this.costMatrix[i][j] = - this.costMatrix[i][j] + max;  
			}
		}
		
	}
	
	/*--------------------------------------------------DEBUG--------------------------------------------------*/
	/**
	 * debug print matrix
	 */
	protected void printMatrix(){
		System.out.println("---matrix---");
		for(int i = 0; i < this.size; i++){
			for(int j = 0; j < this.size; j++){
				System.out.print(this.costMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	/**
	 * debug print array
	 * @param list
	 */
	protected void printLabel(int[] list){
		
		
		for(int i = 0; i < list.length; i++){
			System.out.print(list[i]+ " ");
		}
		//System.out.println("\n");
	}
}
