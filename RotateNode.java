
public class RotateNode {

	// The actual rotation being tested by this possible rotation
	// Rest of the MoveNode can be kept intact, we just want to try this possible difference of rotation
	public int[][] localRotation = new int[3][3];
	public double gameStateValue;
	public MoveNode myMove;
	public int myBlock;
	public int myDirection;
	
	// Real index of block
	public RotateNode(MoveNode move, int block, int direction) {
		myMove = move;
		myBlock = block;
		myDirection = direction;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				localRotation[i][j] = move.game[block][i][j];
			}
		}
		Pentago.rotate(localRotation, direction);
		gameStateValue = Pentago.evaluate(move, Runner.AIPlayer, block, localRotation, true);
	}
	
	public void chooseYou() {
		myMove.game[myBlock] = localRotation;
	}

} 
// block is 0 based