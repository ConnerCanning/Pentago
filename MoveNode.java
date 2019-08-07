import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoveNode {
	
	// [block#][row][col]
	public int[][][] game = new int[4][3][3];
	public RotateNode[] rotationChildren = new RotateNode[8];
	public MoveNode favoriteChild;
	public double alpha = -10.0;
	public double beta = 10.0;
	double gameStateValue;
	public boolean maximizePlayer;
	public Map<Integer, List<Integer>> myMoves;

	// First node to be made. Only make one of these
	// Also this player should be the non-human player number. 
	// Human players never make MoveNodes, they just become minimizers in AI player's minimax
	public MoveNode() { 
		game[0] = new int[3][3];
		game[1] = new int[3][3];
		game[2] = new int[3][3];
		game[3] = new int[3][3];
		maximizePlayer = true;
	}
	
	// All other nodes are children of the current game state, so pass that state in here.
	// Makes a move as well! Uses game state indices, so blocks 1/2/3/4 positions 1/2/3/4/5/6/7/8/9 from diagrams of Pentago.
	// For use of AI when looking ahead
	public MoveNode(MoveNode theOrigin, boolean maximizePlayer, int blockMove, int positionMove, int player) {
		for (int block = 0; block < 4; block++) {
			for (int row = 0; row < 3; row++) {
				game[block][row] = Arrays.copyOf(theOrigin.game[block][row], 3); 
			}
		}
		makeMove(blockMove - 1, positionMove, player);//not maximize player means when I'm minimizing I'm placing marbles as a human
		this.maximizePlayer = maximizePlayer;
	}
		
	
	// Makes a single move! For use by human and AI
	// Uses real index of block for the move
	// Uses position of piece to place, not real index
	public void makeMove(int block, int position, int player) { 
		int row = (position - 1) / 3;
		int col = (position - 1) % 3;
//		System.out.print("position" + position + "block : " + block + "row : " + row + "col: "+ col);
		game[block][row][col] = player;
	}
	
	// Rotates the one block. Block uses position indexes from diagrams, not real indices
	// Direction 0 = left, 1 = right. For use of human player
	public void makeRotation(int block, int direction) {
		Pentago.rotate(game[block-1], direction);
	}
	
	// Returns a map which links blocks to available moves in the block
	// Links based on position, not real index (blocks are 1/2/3/4, not 0/1/2/3)
	public void availableMoves() {
		HashMap<Integer, List<Integer>> map = new HashMap<>();
		for (int i = 1; i < 5; i++) { 
			ArrayList<Integer> available = new ArrayList<>();
			int count = 1;
			for (int[] row : game[i-1]) {
				for (int col : row) {
					if (col == 0) {
						available.add(count);
					}
					count++;
				}
			}
			map.put(i, available);
		}
		myMoves = map;
	}
	
	
	// Creates all possible rotations from this move, adds them as children
	// Then chooses best child and only keep this one.
	// Best determined by whether this is a minimize or maximize move
	public void expand() {
		for (int i = 0; i < 8; i++) {
			rotationChildren[i] = new RotateNode(this, i/2, i%2);
		}
		chooseBestChild();
	}
	
	// Choose the best possible rotation for this game piece placement
	// Then associate this move with that rotation
	private void chooseBestChild() {
		int bestChild = -1;

		if (maximizePlayer) {	// If I'm maximizing, choose highest valued child
			gameStateValue = 0.0;
			for (RotateNode n : rotationChildren) {
				if (n.gameStateValue > gameStateValue)
					gameStateValue = n.gameStateValue;
			}
		} else {				// If I'm minimizing, choose lowest valued child
			gameStateValue = 10.0;
			for (RotateNode n : rotationChildren) {
				if (n.gameStateValue < gameStateValue)
					gameStateValue = n.gameStateValue;
			}		
		}						// Find index of that chosen child
		for (int i = 0; i < rotationChildren.length; i++) {
			if (rotationChildren[i].gameStateValue == gameStateValue) {
				bestChild = i;
				break;
			}
		}						// Keep chosen child, lose the others
		rotationChildren[bestChild].chooseYou();
		System.out.print(this.gameStateValue + " ");
		rotationChildren = null;	
	}
}
