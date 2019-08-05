
public class RotateNode {

	// The actual rotation being tested by this possible rotation
	// Rest of the MoveNode can be kept intact, we just want to try this possible difference of rotation
	int[][] localRotation = new int[3][3];
	int gameStateValue;
	
	public RotateNode(MoveNode move, int block, int direction) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				localRotation[i][j] = move.game[block][i][j];
			}
		}
		Main.rotate(localRotation, direction);
		gameStateValue = Main.evaluate(move, block, localRotation, true);
	}
	

} 
// block is 0 based