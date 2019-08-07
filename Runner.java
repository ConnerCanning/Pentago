import java.util.ArrayList;
import java.util.Scanner;

public class Runner {

	public static int AIPlayer = -1;
	public static char AIPlayerChar = 'a';
	public static int HumanPlayer = -2;
	public static char HumanPlayerChar = 'c';
	public static String HumanPlayerName;
	private static MoveNode gameState = new MoveNode();
	private static Scanner scan = new Scanner(System.in);

	
	public static void main(String[] args) {
		printHello();
		System.out.println("What is your name? ");
		HumanPlayerName = scan.nextLine();
		System.out.println("Hello " + HumanPlayerName + ", would you like to be Player 1 or Player 2? [1 or 2]");
		while (HumanPlayer != 1 && HumanPlayer != 2)
			HumanPlayer = scan.nextInt();
		scan.nextLine();
		if (HumanPlayer == 1) {
			AIPlayer = 2;
			AIPlayerChar = 'b';
			HumanPlayerChar = 'w';
		}
		else {
			AIPlayer = 1;
			AIPlayerChar = 'w';
			HumanPlayerChar = 'b';
		}
		System.out.println("Alright player " + HumanPlayer + " (" + HumanPlayerChar + "), you are against Fred who will be Player " + AIPlayer + " (" + AIPlayerChar + ")");
		
		
		while(true) {
			humanMove();
			AIMove();
		}
		
		
		
		
		
		
		
		
		

	}

	
	private static void humanMove() {
		boolean valid = false;
		try {
			int block = -1;
			int position = -1;
			int rotateBlock = -1;
			char rotateDir = 'a';
			while(!valid) {
				System.out.println("Make a move!");
				String move = scan.nextLine();
				block = Integer.parseInt(move.substring(0,1));
				position = Integer.parseInt(move.substring(2,3));
				rotateBlock = Integer.parseInt(move.substring(4, 5));
				rotateDir = move.charAt(5);

				gameState.availableMoves();
				System.out.println("updated moves");
				if (gameState.myMoves.get(block).contains(position)) { // blocks are 1/2/3/4 in that map 
					valid = true;
//					System.out.print("approved move");
				}
				if (rotateBlock <= 4 && rotateBlock >= 1)
					valid = valid && true;
				if (rotateDir == 'l' || rotateDir == 'L' || rotateDir == 'r' || rotateDir == 'R') 
					valid = valid && true;
				if (!valid)
					System.out.println("invalid move.");
			}
			gameState.makeMove(block - 1, position, HumanPlayer);
			if (Pentago.evaluate(gameState, HumanPlayer, -1, new int[0][0], false) >= 5) {
//				System.out.println("declaring winner");
//				System.out.println(Pentago.evaluate(gameState, HumanPlayer, -1, new int[0][0], false));
				humanWinner();
			}
			int rotate;
			if (rotateDir == 'l' || rotateDir == 'L')
				rotate = 0;
			else
				rotate = 1;
			gameState.makeRotation(rotateBlock, rotate);
			if (Pentago.evaluate(gameState, HumanPlayer, -1, new int[0][0], false) >= 5) {
				System.out.println(Pentago.evaluate(gameState, HumanPlayer, -1, new int[0][0], false));
				humanWinner();

			}
				
			Pentago.print(gameState);


		} catch (Exception e){
			System.out.println("critically invalid user input, but please try again." + e.toString());
			System.out.println("[block]/[position] [block][Direction], like: 2/3 4L");
			humanMove();
		}
	}
	
	
	private static void AIMove() {
		
		gameState.availableMoves();
		
		ArrayList<int[]> fringe = new ArrayList<>();
//		System.out.println(gameState.myMoves.keySet());
		for (Integer key : gameState.myMoves.keySet()) {
			for (int value : gameState.myMoves.get(key)) {
				fringe.add(new int[] {key, value});
//				System.out.print(" val: " + value);
			}
		}
		for (int i = 0; i < fringe.size(); i++) {			// fringe of current gameState, all these are minNodes
			int block = fringe.get(i)[0];
			int position = fringe.get(i)[1];			//false
			MoveNode minNode = new MoveNode(gameState, true, block, position, AIPlayer);	// this min player is placing a AIPlayer marble.
			minNode.alpha = gameState.alpha;
			minNode.beta = gameState.beta;
			minNode.expand();
			minNode.availableMoves();
			///*
			ArrayList<int[]> minFringe = new ArrayList<>();
			for (Integer key : minNode.myMoves.keySet()) {
				for (int value : minNode.myMoves.get(key)) {
					minFringe.add(new int[] {key, value});
				}
			}
			
			
			for (int j = 0; j < minFringe.size(); j++) {	// fringe of this minNode, all these are maxNodes
				int maxBlock = minFringe.get(j)[0];
				int maxPosition = minFringe.get(j)[1];
				MoveNode maxNode = new MoveNode(gameState, true, maxBlock, maxPosition, HumanPlayer);
				maxNode.alpha = minNode.alpha;
				maxNode.beta = minNode.beta;
				maxNode.expand();
				
				// >
				if (minNode.favoriteChild == null || minNode.beta > maxNode.gameStateValue) {
					minNode.beta = maxNode.gameStateValue;
					minNode.favoriteChild = maxNode;
				}
				
				if (minNode.alpha >= minNode.beta) {
					// we prune.
					System.out.print("  prune!  ");
					minFringe.clear();
					break;
				}
			}
			///*
			// update true game state (a maximizer) with largest alpha it can get
			
			
			if (minNode.gameStateValue > gameState.alpha) {
				gameState.alpha = minNode.gameStateValue;
				System.out.println("It's happening that we choose a diff child");
				gameState.favoriteChild = minNode;
			}
			
			
//			if (minNode.beta > gameState.alpha) {
//				gameState.alpha = minNode.beta;
//				System.out.println("It's happening that we choose a diff child");
//				gameState.favoriteChild = minNode;
//			}
		}
		
		// At the end the gameState will go with whichever minimized value was highest!
		gameState = gameState.favoriteChild;
		gameState.maximizePlayer = true;
		
		System.out.println("GAME STATE VALUE HERE:" + Pentago.evaluate(gameState, AIPlayer, -1, new int[0][0], false));
		if (Pentago.evaluate(gameState, AIPlayer, -1, new int[0][0], false) >= 5) {
			aiWinner();
		}
		
		Pentago.print(gameState);

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static void aiWinner() {
		System.out.print("It looks like AI Fred wins!");
		Pentago.print(gameState);
		System.exit(0);
	}
	
	
	private static void humanWinner() {
		System.out.print("congrats for winning, " + HumanPlayerName + "!");
		Pentago.print(gameState);
		System.exit(0);
	}
	
	
	
	private static void printHello() {
		System.out.println("Hello! You will be playing Pentago today against AI Fred!");
		System.out.println("Player 1 will be in white. 'w'");
		System.out.println("Player 2 will be in black. 'b'");
		System.out.println("You may now select your name and player number!");
	}

}
