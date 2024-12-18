import javax.swing.JFrame;

public class App {
	
	public static void main(String[] args) {
		//define dimensions for the screen
		// define size of the each tile
		int rowCount = 21;
		int columnCount = 19;
		int tileSize = 32;
		int screenWidth = columnCount * tileSize;
		int screenHeight = rowCount * tileSize;
		
		//configure screen
		JFrame screen = new JFrame("Poject Pac Man");
		//screen.setVisible(true);
		screen.setSize(screenWidth, screenHeight);
		screen.setLocationRelativeTo(null);
		//disable resize
		screen.setResizable(false);
		//terminate game if the user quits the game
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//create instance of the game screen
		Game pacMan = new Game();
		screen.add(pacMan);
		screen.pack();
		pacMan.requestFocus();
		screen.setVisible(true);
	}
}