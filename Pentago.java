
/** 
 * Utility class with useful methods for adjusting and displaying Pentago gamestate.
 * 
 * @author Conner Canning
 */
public class Pentago {
	 
	
	// rotates a block left or right. 0 is left, 1 is right.
	public static void rotate(int[][] toRotate, int direction) {
		if (direction == 1) {
		    for (int i = 0; i < 3 / 2; i++) { 
		        for (int j = i; j < 3-i-1; j++) { 
		            int temp = toRotate[i][j]; 
		            toRotate[i][j] = toRotate[3-1-j][i]; 
		            toRotate[3-1-j][i] = toRotate[3-1-i][3-1-j]; 
		            toRotate[3-1-i][3-1-j] = toRotate[j][3-1-i]; 
		            toRotate[j][3-1-i] = temp; 
		        } 
		    } 
		} else {
		    for (int i = 0; i < 3 / 2; i++) { 
		        for (int j = i; j < 3-i-1; j++) { 
		            int temp = toRotate[i][j]; 
		            toRotate[i][j] = toRotate[j][3-1-i]; 
		            toRotate[j][3-1-i] = toRotate[3-1-i][3-1-j];  
		            toRotate[3-1-i][3-1-j] = toRotate[3-1-j][i]; 
		            toRotate[3-1-j][i] = temp; 
		        } 
		    } 
		}
	}

	public static double evaluate(MoveNode move, int player, int block, int[][] rotation, boolean rotate) {
		
		// Combine the game state blocks into a large block for evaluation
		int[][] game = new int[6][6];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				game[i][j] = move.game[0][i][j];
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				game[i+3][j] = move.game[2][i][j];
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				game[i][j+3] = move.game[1][i][j];
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				game[i+3][j+3] = move.game[3][i][j];
			}
		}
		// Replace the rotated section if necessary
		if (rotate) {
			switch (block) {
				case 0:
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							game[i][j] = rotation[i][j];
						}
					}
					break;
				case 1:
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							game[i+3][j] = rotation[i][j];
						}
					}
					break;
				case 2:
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							game[i][j+3] = rotation[i][j];
						}
					}
					break;
				case 3:
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							game[i+3][j+3] = rotation[i][j];
						}
					}
					break;
			}
		}
		// Return the evaluation of this game state
		
		return evaluate(game, player);
	}
	
	// Returns the max number of beads you have in a row + 0.1 x the number of them that you have.
	// players 1 and 2 (0 spaces are empty)
	private static double evaluate(int[][] game, int player) {
		
		// Row Counting
		int maxRowCount = 0;
		int amountOfMaxRowCount = 0;

		for (int i = 0; i < 6; i++) {		// for each row
			boolean countedThisRowInMaxRowCount = false;
			int rowCount = 0;				// count number of marbles in a row
			for (int j = 0; j < 6; j++) {
				if (game[i][j] == player) {		// starting a new row count
					rowCount++;
				} else {						// ending a row count
					rowCount = 0;
				}
				if (rowCount == maxRowCount && !countedThisRowInMaxRowCount) {	// updating number of rows which all share the max row count
					amountOfMaxRowCount++;
					countedThisRowInMaxRowCount = true;
				}
				if (rowCount > maxRowCount)	{	// updating max row count
					maxRowCount = rowCount;
					amountOfMaxRowCount = 1;
				}
			}
		}
//		System.out.println(maxRowCount + "maxRowCount");
//		System.out.println(amountOfMaxRowCount);
		// Column Counting
		int maxColCount = 0;
		int amountOfMaxColCount = 0;
		
		for (int j = 0; j < 6; j++) {		// for each col
			boolean countedThisColInMaxColCount = false;
			int colCount = 0;				// count number of marbles in a col
			for (int i = 0; i < 6; i++) {
				if (game[i][j] == player) {		// starting a new col count
					colCount++;
				} else {						// ending a col count
					colCount = 0;
				}
				if (colCount == maxColCount && !countedThisColInMaxColCount) {	// updating number of rows which all share the max col count
					amountOfMaxColCount++;
					countedThisColInMaxColCount = true;
				}
				if (colCount > maxColCount)	{	// updating max col count
					maxColCount = colCount;
					amountOfMaxColCount = 1;
				}
			}
		}
//		System.out.println(maxColCount + "maxcolCount");
//		System.out.println(amountOfMaxColCount);


		// Only considering diagonals which could win the game, 3 possible diagonals for each pair of corners
		
		// Diagonal Counting Top Left -> Bottom Right
		int diag1ACount = 0;
		int diag1CCount = 0;
		for (int i = 0; i < 5; i++) {
			if (game[i][i+1] == player) {
				diag1ACount++;
			} else {
				diag1ACount = 0;
			}
			if (game[i+1][i] == player) {
				diag1CCount++;
			} else {
				diag1CCount = 0;
			}
		}
		int diag1BCount = 0;
		for (int i = 0; i < 6; i++) {
			if (game[i][i] == player) {
				diag1BCount++;
			} else {
				diag1BCount = 0;
			}
		}
		int maxDiag1Count = Math.max(diag1ACount, Math.max(diag1BCount, diag1CCount));
		int amountOfMaxDiag1Count = 0;
		if (diag1ACount == maxDiag1Count)
			amountOfMaxDiag1Count++;
		if (diag1BCount == maxDiag1Count)
			amountOfMaxDiag1Count++;
		if (diag1CCount == maxDiag1Count)
			amountOfMaxDiag1Count++;
		
		// Diagonal Counting Top Left -> Bottom Right
		int diag2ACount = 0;
		int diag2CCount = 0;
		for (int i = 0; i < 5; i++) {
			if (game[5-i][i+1] == player) {
				diag2ACount++;
			} else {
				diag2ACount = 0;
			}
			if (game[4-i][i] == player) {
				diag2CCount++;
			} else {
				diag2CCount = 0;
			}
		}
		int diag2BCount = 0;
		for (int i = 0; i < 6; i++) {
			if (game[5-i][i] == player) {
				diag2BCount++;
			} else {
				diag2BCount = 0;
			}
		}
		int maxDiag2Count = Math.max(diag2ACount, Math.max(diag2BCount, diag2CCount));
		int amountOfMaxDiag2Count = 0;
		if (diag2ACount == maxDiag2Count)
			amountOfMaxDiag2Count++;
		if (diag2BCount == maxDiag2Count)
			amountOfMaxDiag2Count++;
		if (diag2CCount == maxDiag2Count)
			amountOfMaxDiag2Count++;
		
		int[] counts = new int[4];
		int[] countsNum = new int[4];
		counts[0] = maxRowCount;
		countsNum[0] = amountOfMaxRowCount;
		counts[1] = maxColCount;
		countsNum[1] = amountOfMaxColCount;
		counts[2] = maxDiag1Count;
		countsNum[2] = amountOfMaxDiag1Count;
		counts[3] = maxDiag2Count;
		countsNum[3] = amountOfMaxDiag2Count;
		
		
//		System.out.println();
//		System.out.println("new game:");
//		for (int i = 0; i < 6; i++) {
//			for (int j = 0; j < 6; j++) {
//				System.out.print(game[i][j]);
//			}
//			System.out.println();
//		}
//		
//		System.out.println();
//		System.out.println("Player:" + player  +" ");
//		for (int i : counts)
//			System.out.print(i + " ");
//		System.out.println();
//		for (int j : countsNum)
//			System.out.print(j + " ");

		int max = counts[0];
		for (int c : counts) {
			if (c > max)
				max = c;
		}
		int numMaxCount = 0;
		for (int i = 0; i < counts.length; i++) {
			if (counts[i] == max) {
				numMaxCount += countsNum[i];
			}
		}
		double finalValue = max + (numMaxCount * 0.1);
		return finalValue;
	}
	
	//Prints entire game state
	public static void print(MoveNode game) {
		System.out.println();
		System.out.println("----------");
		for (int i = 0; i < 3; i++) {
			print(game, i, 0);
			System.out.println();
		}
		System.out.println();
		for (int i = 0; i < 3; i++) {
			print(game, i, 2);
			System.out.println();
		}
		System.out.println("----------");
	}
	
	// Prints specific portion of game state, single row left to right
	private static void print(MoveNode game, int row, int blockStart) {
		for (int i = blockStart; i < blockStart + 2; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(game.game[i][row][j]);
			}
			System.out.print(" ");
		}
	}
	
	
	
	
	
	
	


}