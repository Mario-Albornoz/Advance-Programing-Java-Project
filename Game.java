import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

//import java.util.Random;
import javax.swing.*;



public class Game extends JPanel implements ActionListener, KeyListener{
	
	
	//class for block which is the parent class for all our entities in the game 
	class Block{
		int x;
		int y;
		int width;
		int height;
		Image image;
		
		//save starting position coordinates
		int startX;
		int startY;
		//variables for movements to traves the string array
		char direction ='U';
		int velocityX = 0;
		int velocityY = 0;
		
		
		
		
		//constructor for block
		Block(Image image, int x, int y, int width, int height){
			this.image = image;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.startX = x;
			this.startY = y;
			
		}
		
		//Create movement by updating the direction and the velocity depending on the key pressed
		//
		void updateDirection(char direction) {
			char previousDirection = this.direction;
			this.direction = direction;
			updateVelocity();
			this.x += this.velocityX;
			this.y += this.velocityY;
			for (Block wall : walls) {
				if (collision(this, wall)) {
					this.x -= velocityX;
					this.y -= velocityY;
					this.direction = previousDirection;
					updateVelocity();	
				}
			//add change in the direction so that 
			}
			
		}
		
		void updateVelocity() {
			// picked this ration of velocity based on what worked best though trial an eror i found 0.125 to work best
			double velocity = 0.125 * tileSize;
			
			if(this.direction == 'U') {
				this.velocityX = 0;
				this.velocityY = (int) (-tileSize/velocity);
			}
			else if (this.direction == 'D') {
				this.velocityX = 0;
				this.velocityY = (int) (tileSize/velocity);
			}
			else if (this.direction == 'L') {
				this.velocityY=0;
				this.velocityX= (int) (-tileSize/velocity);
			}
			else if (this.direction == 'R') {
				this.velocityY=0;
				this.velocityX= (int) (tileSize/velocity);
			}
		}
	}
	
	class Pacman extends Block{
		//flag for power up 
		boolean is_powered = false;
		//timer for power up
		private Timer powerTime;
		//lives
		private int lives = 3;
		
		Pacman(Image image, int x, int y, int width, int height) {
			super(image, x, y, width, height);
			
		}
		
		//method for activating power up place the duration in seconds
		void PowerUp(int powerUpDuration) {
			if (is_powered) {
	     
	            return; // Prevent reactivating the power-up
	        }
			
			is_powered = true;
			//initialize timer for ppwer up duration
			powerTime = new Timer(powerUpDuration * 1000, new ActionListener() {
				@Override 
				public void actionPerformed(ActionEvent e) {

				is_powered = false;
				
				//stop timer
				powerTime.stop();
			}
			});
			
			powerTime.setRepeats(false);
			powerTime.start();
		}
		
	
	}
	
	
	//set game panel to the same dimensions as the screen
	//row count == number of elements in the array
	private int rowCount = 21;
	//column Count == length of each string element in the array
	private int columnCount = 19;
	
	private int tileSize = 32;
	private int screenWidth = columnCount * tileSize;
	private int screenHeight = rowCount * tileSize;
	
	//variables for activation of movement in the walls
	private Boolean moveWallsFlag = false;
	private Boolean GameOver = false;
	
	//declare images
	private Image wallImage;
	private Image cherryImage;
	private Image blueGhostImage;
	private Image orangeGhostImage;
	private Image pinkGhostImage;
	private Image redGhostImage;
	
	private Image pacmanUpImage;
	private Image pacmanDownImage;
	private Image pacmanLeftImage;
	private Image pacmanRightImage;
	
	
	//Using a hashset for the elements in the tile map
	HashSet<Block> walls;
	HashSet<Block> foods;
	HashSet<Block> ghosts;
	HashSet<Block> cherries;
	Pacman pacman;
	
	//Timer to set the game loop
	Timer gameLoop;
	
	// set up list and variables for ghost movements
	
	char[] directions = {'U','D','L','R'};
	
	//import random function to randomize the ghost's movements
	Random random = new Random();
	
	//Create a tile the different maps using an array of strings
	//X = wall, O = empty, P= pac man, ' '=food
	//b = blue ghost, o = orange ghost, p = purple ghost, p = pink ghost
	
	private String[] layoutMap1 = {
			"XXXXXXXXXXXXXXXXXXX",
	        "Xc       X       cX",
	        "X XX XXX X XXX XX X",
	        "X                 X",
	        "X XX X XXXXX X XX X",
	        "X    X       X    X",
	        "XXXX XXXX XXXX XXXX",
	        "OOOX X       X XOOO",
	        "XXXX X XXrXX X XXXX",
	        "O       bpo       O",
	        "XXXX X XXXXX X XXXX",
	        "OOOX X       X XOOO",
	        "XXXX X XXXXX X XXXX",
	        "X        X        X",
	        "X XX XXX X XXX XX X",
	        "X  X     P     X  X",
	        "XX X X XXXXX X X XX",
	        "X    X   X   X    X",
	        "X XXXXXX X XXXXXX X",
	        "Xc               cX",
	        "XXXXXXXXXXXXXXXXXXX" 	
	};
	private String[] layoutMap2 = {
			"XXXXXXX     XXXXXXXX",
	        "X                  ",
	        "X             c    ",
	        "X                  ",
	        "X    c             ",
	        "X                  ",
	        "X                  ",
	        "X                  ",
	        "X     P       c    ",
	        "X                  ",
	        "X                  ",
	        "                   ",
	        "  c                ",
	        "                   ",
	        "                   ",
	        "X               c  ",
	        "X                  ",
	        "X                  ",
	        "X  c               ",
	        "X                  ",
	        "X                  "};
	
	//initialize variable to hold the map that will be outputed
		private String [] tileMap = layoutMap1;
	
	//constructor
	Game(){
		setPreferredSize(new Dimension(screenWidth,screenHeight));
		setBackground(Color.BLACK);
		addKeyListener(this);//listens for key presses
		setFocusable(true); //allows the Jpanel to listen to the key presses
		
		//load images for map
		wallImage = new ImageIcon(getClass().getResource("Images/wall.png")).getImage();
		cherryImage = new ImageIcon(getClass().getResource("Images/cherry2.png")).getImage();
		
		
		//load ghost images
		blueGhostImage = new ImageIcon(getClass().getResource("Images/blueGhost.png")).getImage();
		orangeGhostImage = new ImageIcon(getClass().getResource("Images/orangeGhost.png")).getImage();
		pinkGhostImage = new ImageIcon(getClass().getResource("Images/pinkGhost.png")).getImage();
		redGhostImage = new ImageIcon(getClass().getResource("Images/redGhost.png")).getImage();
		
		//load pacman images
		pacmanUpImage = new ImageIcon(getClass().getResource("Images/pacmanUp.png")).getImage();
		pacmanDownImage = new ImageIcon(getClass().getResource("Images/pacmanDown.png")).getImage();
		pacmanLeftImage = new ImageIcon(getClass().getResource("Images/pacmanLeft.png")).getImage();
		pacmanRightImage = new ImageIcon(getClass().getResource("Images/pacmanRight.png")).getImage();
		
		loadMap();
		
		//iterate through each ghost to check the direction
		for(Block ghost : ghosts) {
			//pick a random index for the directions, 1 = up, 2 = down, 3= left, 4 = right and update the initial direction of the ghost
			char newDirection = directions[random.nextInt(4)];
			ghost.updateDirection(newDirection);
		}
		
		
		//initialize timer for the game loop with parameter 50 ms (delay) and this object refering to the game
		gameLoop = new Timer(50, this);
		gameLoop.start();
		
	}
	
	//method to place the elements on the map
	public void loadMap() {
		// initialize hash sets to hold the entities 
		walls = new HashSet<Block>();
		foods = new HashSet<Block>();
		ghosts = new HashSet<Block>();
		cherries = new HashSet<Block>();
		
		if (moveWallsFlag) {
			tileMap = layoutMap2;
		}
		
		// iterate through the map (array of strings)
		// the first loop iterates through the elements
		//the second loop iterates through the index of the string element
		//This way we can traverse through the map with two loops
		//It has a time complexity of O(n), let n = rowCount + columnCount
		//but for the amount of objects we have it is efficient
		for(int r = 0; r < rowCount; r++) {
			for(int c = 0; c < columnCount; c++) {
				String row = tileMap[r];
				char tileMapChar = row.charAt(c);
				
				//convert the coordinates to pixels using our tileSize variable
				int x = c*tileSize;
				int y = r*tileSize;
				
				if(tileMapChar == 'X') {
					Block wall = new Block(wallImage, x, y, tileSize, tileSize);
					walls.add(wall);
					
				}
				else if (tileMapChar == 'b') {
					Block ghost = new Block(blueGhostImage, x,y, tileSize,tileSize);
					ghosts.add(ghost);
				}
				else if (tileMapChar == 'o') {
					Block ghost = new Block(orangeGhostImage, x,y, tileSize,tileSize);
					ghosts.add(ghost);
				}
				else if (tileMapChar == 'p') {
					Block ghost = new Block(pinkGhostImage, x,y, tileSize,tileSize);
					ghosts.add(ghost);
				}
				else if (tileMapChar == 'r') {
					Block ghost = new Block(redGhostImage, x,y, tileSize,tileSize);
					ghosts.add(ghost);
				}
				else if (tileMapChar == 'P') {
					pacman = new Pacman(pacmanRightImage, x,y, tileSize,tileSize);
					
				}
				else if (tileMapChar == ' ') {
					//draw rectangle for food
					//add off sets of 14
					Block food = new Block(null, x+14,y+14, 4,4);
					foods.add(food);					
				}
				else if (tileMapChar == 'c') {
					Block cherry = new Block(cherryImage,x,y,tileSize,tileSize);
					cherries.add(cherry);				
				}
			}
		}		
	}
	
	//Create function to paint the graphics
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	//create a function to draw the graphic using draw image to set the pngs accordingly with the size, leaving the observer param as null
	public void draw(Graphics g) {
		g.drawImage(pacman.image,pacman.x, pacman.y, pacman.width,pacman.height, null);
		
		for(Block ghost : ghosts) {
			g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
		}
		
		for (Block wall : walls) {
			g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
		}
		
		g.setColor(Color.white);
		for(Block food: foods) {
			g.fillOval( food.x, food.y, food.width, food.height);
		}
		
		for (Block cherry: cherries) {
			g.drawImage(cherry.image, cherry.x, cherry.y, cherry.width, cherry.height, null);
		}
		
		//display the number of foods left to collect
		int foods_display = foods.size();
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD,18));
		g.drawString("Food Left: " + foods_display, 15, screenHeight-10);
		
		//display time left in power up state
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD,18));
		
		// create variable to handle the state of the power up text
		if(pacman.is_powered && moveWallsFlag == false) {
			g.drawString("YOU ARE POWERED UP!!!", (screenWidth/2) - 98, screenHeight-10);
		}
		
		//display number of live left 
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD,18));
		g.drawString("LIVES LEFT: " + pacman.lives, 15, screenHeight - (screenHeight - 20));
		
		//dsiplay game over message if game over
		if (pacman.lives == 0) {
			int gameOverX = (screenWidth/2) - 98;
			int gameOverY = (screenHeight/2)-(tileSize*5)-15;
			
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD,30));
			g.drawString("GAME OVER!!!", gameOverX, gameOverY);
			GameOver = true;
		}
		
		if (moveWallsFlag && cherries.size() == 0 && tileMap == layoutMap2) {
			
			int gameWinX = (screenWidth/2) - 98;
			int gameWinY = (screenHeight/2)-(tileSize*5)-15;
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD,30));
			g.drawString("YOU WIN!!!", gameWinX, gameWinY);
			GameOver = true;
		}
	}
	
	//Function so that the changes in velocity and direction are reflected in game, by updating the x and y cordinates
	public void move() {

		//create condition to check lives, if lives are zero stop the game
		
		if (pacman.lives != 0) {
		
			pacman.x += pacman.velocityX;
			pacman.y += pacman.velocityY;
			
			//add tunnel feature
			if (pacman.x > screenWidth) {
				pacman.x = 0;
				//pacman.y = pacman.startY;
			}
			else if (pacman.x < 0) {
				pacman.x = screenWidth;
			}
			
			
			//use the collision function by iterating through each wall
			//if a collision is found, we'll nullify the movement by subtracting the velocity in the opposite direction
			for (Block wall: walls) {
				if (collision(pacman, wall)) {
					pacman.x -= pacman.velocityX;
					pacman.y -= pacman.velocityY;
					//break once a collision is found
					break;
				}
			}
			//add collision with foods so that as the pacman passes through the food it eats it
			// TO DO: we could add a counter to showcase how many foods are left on the screen
			for (Block food: foods) {
				if (collision(pacman, food)) {
					foods.remove(food);
					break;
				}
			}
			
			//add collision with cherries and initialized power up
			for (Block cherry: cherries) {
				if (collision(pacman, cherry)) {
					cherries.remove(cherry);
					pacman.PowerUp(10);
					break;
				}
			}
			
			
			for (Block ghost: ghosts) {
				//Keep ghost from staying in a straight line after turning them around in the middle row
				if (ghost.y == tileSize * 9 && ghost.direction != 'U' && ghost.direction != 'D') {
					ghost.updateDirection('U');
				}
				
				ghost.x += ghost.velocityX;
				ghost.y += ghost.velocityY;
				//keep the ghost inside the map
				if (ghost.x >= screenWidth) {
					ghost.updateDirection('L');
					
				}
				else if (ghost.x <= 0) {
					ghost.updateDirection('R');
				}
				
				// loop through walls to check for collisions
				for (Block wall: walls) {
					if (collision(ghost, wall)) {
						ghost.x -= ghost.velocityX;
						ghost.y -= ghost.velocityY;
						char newDirection = directions[random.nextInt(4)];
						ghost.updateDirection(newDirection);
					}
				}
				
				
				//check if pacman is powered up or not
				if(pacman.is_powered == false) {
					if (collision(pacman,ghost)) {
						pacman.x = pacman.startX;
						pacman.y = pacman.startY;
						pacman.y -= pacman.velocityY;
						pacman.x -= pacman.velocityY;
						pacman.lives --;
						
						break;
					}
				}
				else {
					if (collision(pacman, ghost)) {
						ghost.x = ghost.startX;
						ghost.y = ghost.startY;
					}
				}
			}
		}
	}
	
	
	
	
	//Create function for collisions
	//This function works by checking the edges of the two blocks
	
	public boolean collision(Block a, Block b) {
		final int gap = 0;
				//checks if the left edge of block a is colliding with the right edge of block b
		return a.x < b.x + b.width + gap &&
				//checks if the right edge of block a is colliding with the left edge of block b
				a.x +a.width > b.x + gap &&
				//checks if the top of block a is colliding with the bottom of block b
				a.y < b.y + b.height + gap&&
				//checks if the bottom of block a is colliding with the top of block b
				a.y +a.height >b.y + gap;
	}
	
	
	
	//use this method to do the game loop and repaint the jpanel updating the map
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (GameOver == false) {
			move();
			if (foods.size() == 0) {
				tileMap = layoutMap2;
				moveWallsFlag = true;
				loadMap();
			}
			checkIndicatorAndMoveWalls();
			repaint();
			}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			pacman.updateDirection('U');
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			pacman.updateDirection('D');
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			pacman.updateDirection('L');
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			pacman.updateDirection('R');
		}
		
		//check direction to update the pacman image ot the direction is facing
		if (pacman.direction == 'U') {
			pacman.image = pacmanUpImage;
		}
		else if (pacman.direction == 'D') {
			pacman.image = pacmanDownImage;
		}
		else if (pacman.direction == 'R') {
			pacman.image = pacmanRightImage;
		}
		else if (pacman.direction == 'L') {
			pacman.image = pacmanLeftImage;
		}
	}
	
	//function to check if the player has eaten all the foods, completed the first phase
	public void checkIndicatorAndMoveWalls() {

	    if (moveWallsFlag == true) {
	    	foods.clear();
		    //move the walls 
		    moveWalls(7);
		    //moveWallsCollumns();
	    }
	    
	    
	    
	}
	
	//decrease the number to increase speed, int direction -1 to move to the left, 1 to move to the right
	 private void moveWalls(int speed) {
	        if (moveWallsFlag) {
	            for (Block wall : walls) {
	                wall.y += (tileSize / speed);
	                wall.x += (tileSize / speed);
	                if (wall.y > screenHeight) {
	                    wall.y = 0;  
	                }
	                if (wall.x > screenWidth ) {
	                	wall.x = 0;
	                }
	                if (wall.y < 0) {
	                    wall.y = 0;  
	                }
	                if (wall.x < 0 ) {
	                	wall.x = 0;
	                }
	            }
	        }
	        for (Block wall: walls) {
	        	if (collision(pacman, wall)) {
	        		pacman.lives = 0;
	        	}
	        	
	        }
	        
	        for (Block cherry : cherries) {
	        	if (collision(pacman,cherry)) {
	        		cherries.remove(cherry);
	        	}
	        }
	    }
	 
	 
}