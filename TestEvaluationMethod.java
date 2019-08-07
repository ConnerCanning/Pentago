
public class TestEvaluationMethod {

	public static void main(String[ ]args) {
		int[] one = {1, 0, 0, 0, 0, 0};
		int[] two = {1, 2, 0, 0, 0, 0};
		int[] three = {0, 0, 0, 0, 0, 0};
		int[] four = {0, 0, 0, 0, 0, 0};
		int[] five = {0, 0, 0, 0, 0, 0};
		int[] six = {0, 0, 0, 0, 0, 0};

		int[][] game = new int[6][6];
		game[0] = one;
		game[1] = two;
		game[2] = three;
		game[3] = four;
		game[4] = five;
		game[5] = six;
		
		double eval = evaluate(game, 1);
		System.out.println(eval);
		
	}
	
	
	
	
	
	
	
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
		
		
		System.out.println();
		System.out.println("new game:");
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				System.out.print(game[i][j]);
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println("Player:" + player  +" ");
		System.out.print("Counts: ");
		for (int i : counts)
			System.out.print(i + " ");
		System.out.println();
		System.out.print("Amounts: ");
		for (int j : countsNum)
			System.out.print(j + " ");

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
}
