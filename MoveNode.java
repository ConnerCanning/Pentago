
public class MoveNode {

	int[][] I   = new int[3][3];
	int[][] II  = new int[3][3];
	int[][] III = new int[3][3];
	int[][] IV  = new int[3][3];
	
	int[][][] game = new int[4][][];
	int player;
	
	public MoveNode(int thePlayer) { 
		game[0] = I;
		game[1] = II;
		game[2] = III;
		game[3] = IV;
		player = thePlayer;
	}
	
	RotateNode[] children = new RotateNode[8];
	
	// Creates all possible rotations from this move, adds them as children
	public void expand() {
		for (int i = 0; i < 8; i++) {
			children[i] = new RotateNode(this, i/2, i%2);
		}
	}
}
