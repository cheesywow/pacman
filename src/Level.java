/**
 * Tom Liu
 * ICS4U1
 * June 5th, 2016
 * 
 * The main class of pacman, loads the map file and controls all the movement of pacman and ghosts, including wrapping, collision with
 * wall or food and turns. Also check win or loss.
 */

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Level implements KeyListener{
	public JFrame frame;
	JPanel panel = new JPanel();
	JLabel [][] foodArray; // stores food and walls pixels 
	String [][] colours; // For reading map
	JLabel pacmansprite = new JLabel();
	int key; // arrow key pressed 
	int interval = 25; // interval to make the next pixel 
	String food = "255,0,255"; // color of food for readmap
	String wall = "0,255,0"; // color of wall for readmap
	boolean mouthOpen = true; // inital mouth position 
	boolean lost = false; 
	Pacman pacman; 
	// 3 ghosts created 
	ghost g1;
	ghost g2;
	ghost g3;
	//initial turn
	String nextTurn = "right";
	// how many food left 
	int foodCounter = 0;
	boolean enableSound = true; 
	//sound clip
	URL url = getClass().getResource("misc/food.wav");
	AudioClip clip = Applet.newAudioClip(url);
	
	public void mainloop(){
		// main loop will make pacman keep going until game over (win/lose)
			while(!lost){
				//Pacman will only turn at closest opening
				if(nextTurn.equals("up")){
					Turn("up");
				}
				else if(nextTurn.equals("down")){
					Turn("down");
				}
				else if(nextTurn.equals("right")){
					Turn("right");
				}
				else if(nextTurn.equals("left")){
					Turn("left");
				}
				//if no walls in front of pacman
				if(!checkWallCollision()){
					pacman.move();
					checkGame();
					changeIcon();
				}
				
				checkGame();
				g1.move();
				g2.move();
				g3.move();
				g1.sprite.setLocation(g1.xpos, g1.ypos);
				g2.sprite.setLocation(g2.xpos, g2.ypos);
				g3.sprite.setLocation(g3.xpos, g3.ypos);
				
				checkFoodCollision();
				pacmansprite.setLocation(pacman.xpos, pacman.ypos);
				
				//System.out.println("x:"+pacman.xpos+"   y:"+pacman.ypos);
				//pause the game for 0.01 seconds so pacman moves smoothly 
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				checkwarp();
			}
	}
	
	/**
	 * Initializes the onscreen components
	*/
	public void initialize(String level) throws IOException {
		//Reads image
		BufferedImage image;
		String lvl = "src/maps/"+level+".png";
		File file = new File(lvl);
		image = ImageIO.read(file);
		//each index of the 2D array is made into food/wall
		foodArray = new JLabel[image.getWidth()][image.getHeight()];
		//initialize the frame and panel
		frame = new JFrame();
		frame.addKeyListener(this);
		frame.setBounds(100, 100, foodArray.length *interval + 50,foodArray[0].length *interval + 80);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setForeground(Color.BLACK);
		panel.setBackground(Color.BLACK);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		//add pacman to the panel
		pacman = new Pacman();
		pacmansprite.setIcon(new ImageIcon("src/pacman_img/RightOpened.png"));
		pacmansprite.setBounds(250, 200, 25, 25);
		panel.add(pacmansprite);
		
		
		//read map from pixel image
		drawmap(readmap(image));
		// add ghosts 
		g1 = new ghost(50,50,new ImageIcon("src/ghost_img/Ghost1.png"),colours);
		g2 = new ghost(325,50,new ImageIcon("src/ghost_img/Ghost2.png"),colours);
		g3 = new ghost(50,325,new ImageIcon("src/ghost_img/Ghost3.png"),colours);
		panel.add(g1.sprite);
		panel.add(g2.sprite);
		panel.add(g3.sprite);
		
		frame.setVisible(true);
	}
	
	public void checkwarp(){
		//if pacman goes out of bonds on one side of the map, it will reappear at the other side 
		if(pacman.ypos > (colours[0].length*interval)-2){
			pacman.ypos = interval+2;
		}
		else if(pacman.xpos > (colours.length*interval)-2){
			pacman.xpos = interval+2;
		}
		else if(pacman.ypos < interval+2){
			pacman.ypos = (colours[0].length*interval)-2;
		}
		else if(pacman.xpos < interval+2){
			pacman.xpos = (colours.length*interval)-2;
		}
		
	}
	
	/**
	 * Turns Pacman in the direction the last key was pressed in
	*/
	public void Turn(String string){
		// if up or down is pressed, pacman will only turn if x position is divisibe by interval of blocks
		if(string.equals("up")||string.equals("down")){
			while(pacman.xpos % interval != 0 || checkWallCollision(string)){
				if(!checkWallCollision()){
					pacman.move();
					changeIcon();
					checkGame();
				}
				else{
					break;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//keep ghosts moving when an arrow key is pressed 
				pacmansprite.setLocation(pacman.xpos, pacman.ypos);
				checkFoodCollision();
				checkGame();
				g1.move();
				g2.move();
				g3.move();
				g1.sprite.setLocation(g1.xpos, g1.ypos);
				g2.sprite.setLocation(g2.xpos, g2.ypos);
				g3.sprite.setLocation(g3.xpos, g3.ypos);
			}
			pacman.setdirection(string);
		}
		// if left or right is pressed, pacman will only turn if y position is divisibe by interval of blocks
		else if(string.equals("right")||string.equals("left")){
			while(pacman.ypos % interval != 0 || checkWallCollision(string)){
				if(!checkWallCollision()){
					pacman.move();
					changeIcon();
				}
				else{
					break;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pacmansprite.setLocation(pacman.xpos, pacman.ypos);
				checkFoodCollision();
				checkGame();
				g1.move();
				g2.move();
				g3.move();
				g1.sprite.setLocation(g1.xpos, g1.ypos);
				g2.sprite.setLocation(g2.xpos, g2.ypos);
				g3.sprite.setLocation(g3.xpos, g3.ypos);
			}
			pacman.setdirection(string);
		}
	}
	
	/**
	 * Reads the map from a png file in the source and stores the pixel values in an array
	*/
	public String[][] readmap(BufferedImage image) throws IOException{
		int  red;
		int  green;
		int  blue;
		String colour = "";
		
			colours = new String[image.getWidth()][image.getHeight()];
			for(int x =0;x < image.getWidth();x++){
			  for(int y =0;y < image.getHeight();y++){
			  //gets color from specified pixel
			  int col =  image.getRGB(x,y);
			  //retrieves RGB values from color string by performing bit shifts right
			  red   = (col & 0x00ff0000) >> 16;
			  green = (col & 0x0000ff00) >> 8;
			  blue  =  col & 0x000000ff;
			  //converts to recognized notation
			  colour = ""+red+","+blue+","+green+"";
			  //saved in array
			  colours[x][y] = colour;
			  }
		  }
		return colours;
	}
	
	/**
	 * reads the pixel values of the png map and draws the components in their appropriate places
	*/
	public void drawmap(String[][] colours){
		int startingX = interval;
		int startingY = interval;
			//number of rows
			for(int y =0;y < colours[0].length;y++){
				//number of columns 
				for(int x =0;x < colours.length;x++){
				if(colours[x][y].equals(food)){
					// if colour of the pixel is yellow, convert it into food and put into the 2D array
					foodArray[x][y] = new JLabel(" * ");
					foodArray[x][y].setBounds(startingX,startingY, interval,interval);
					foodArray[x][y].setForeground(Color.yellow);
					startingX += interval;
					panel.add(foodArray[x][y]);
					foodCounter ++;
				}
				else if(colours[x][y].equals(wall)){
					// if colour of the pixel is blue, convert it into wall and put into the 2D array
					foodArray[x][y] =  new JLabel(new ImageIcon("src/misc/wall.png"));
					foodArray[x][y].setBounds(startingX,startingY, 15,15);
					foodArray[x][y].setForeground(Color.pink);
					startingX += interval;
					panel.add(foodArray[x][y]);
				}
				else{ 
					foodArray[x][y] = new JLabel("");
					foodArray[x][y].setBounds(startingX,startingY, interval,interval);
					startingX += interval;
				}
			}
				// add the interval for the next wall/food 
			startingY += interval;
			startingX = interval;
		}
	}
	
	/**
	 * checks if two labels intersect
	*/
	public boolean collision(JLabel label1, JLabel label2){
		//top
		if (label1.getY() >= label2.getY()){
			//bottom
			if (label1.getY() <= label2.getY()){
				//left
				if(label1.getX() >= label2.getX()){
					//right
					if (label1.getX() <= label2.getX()){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * checks if two labels intersect
	*/
	public boolean collision2(JLabel label1, JLabel label2){
	//Another way of doing collision detection, the first methond doesnt work sometimes 
		if (label1.getHeight()+label1.getY() > label2.getY()){
			if (label1.getY() < label2.getY() + label2.getHeight()){
				if(label1.getWidth()+label1.getX() > label2.getX()){
					if (label1.getX() < label2.getX() + label2.getWidth()){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Is responsible for detecting when arrow keys are hit
	*/
	@Override
	public void keyPressed(KeyEvent e) {
		key = e.getKeyCode();
		
		//up arrow
		if (key == KeyEvent.VK_UP) {
			pacman.speed=1;
				nextTurn = ("up");
		}
		//down arrow
		if (key == KeyEvent.VK_DOWN) {
			pacman.speed=1;
				nextTurn =("down");
		}
		//left arrow
		if (key == KeyEvent.VK_LEFT) {
			pacman.speed=1;
				nextTurn =("left");
		}
		//right arrow
		if (key == KeyEvent.VK_RIGHT) {
			pacman.speed=1;
				nextTurn =("right");
			
		}
	}
	
	/**
	 * Checks if Pacman is over or next to food, eats food if going over
	*/
	public void checkFoodCollision(){
		int a = (int) Math.ceil((pacman.xpos-interval)/(interval+0.00));// converts it to double with 0.00
		int b = (int) Math.ceil((pacman.ypos-interval)/(interval+0.00));
		for (int x = a - 1; x < a + 1;x++){
			for(int y = b - 1; y < b + 1; y++){
				if(x < foodArray.length && x > 0){
					if(y < foodArray[0].length && y > 0){
						if (collision(foodArray[x][y],pacmansprite)){
							if (!foodArray[x][y].getText().equals("")){//if there is food, "eats" it
								foodArray[x][y].setText("");
								foodCounter --;
								foodSound();
							}
						}
					}
					else{
						checkwarp();
					}
				}
				else{
					checkwarp();
				}
			}
		}
	}

	/**
	 * checks if there is a wall in front of Pacman when he is making a corner turn, return true if wall is in front of pacman
	*/
	public boolean checkWallCollision(String dir){
		int x;
		int y;
		// detects the color of the map, return true if it is blue(wall)
		if(dir.equals("up")){//check wall on top if pacman is moving up
			x = (int) Math.ceil((pacman.xpos-interval)/(interval+0.00));
			y = (int) Math.ceil((pacman.ypos-interval)/(interval+0.00));
			//System.out.println("x:"+x+"   y:"+y);
			if(colours[x][y-1].equals("0,255,0")){
				return true;
			}
		}
		else if(dir.equals("down")){//check wall down if pacman is moving down
			x = (pacman.xpos-interval)/interval;
			y = (pacman.ypos-interval)/interval;
			//System.out.println("x:"+x+"   y:"+y);
			if(colours[x][y+1].equals("0,255,0")){
				return true;
			}
		}
		else if(dir.equals("left")){//check wall left if pacman is moving left
			x = (int) Math.ceil((pacman.xpos-interval)/(interval+0.00));
			y = (int) Math.ceil((pacman.ypos-interval)/(interval+0.00));
			//System.out.println("x:"+x+"   y:"+y);
			if(colours[x-1][y].equals("0,255,0")){
				return true;
			}
		}
		else if(dir.equals("right")){//check wall right if pacman is moving right
			x = (pacman.xpos-interval)/interval;
			y = (pacman.ypos-interval)/interval;
			//System.out.println("x:"+x+"   y:"+y);
			if(colours[x+1][y].equals("0,255,0")){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * checks if there is a wall in front of Pacman in general, returns true if there is wall around pacman
	*/
	public boolean checkWallCollision(){
		int x;
		int y;
		
		if(pacman.direction.equals("up")){
			x = (int) Math.ceil((pacman.xpos-interval)/(interval+0.00));
			y = (int) Math.ceil((pacman.ypos-interval)/(interval+0.00));
			//System.out.println("x:"+x+"   y:"+y);
			if(y > 0){
				if(colours[x][y-1].equals("0,255,0")){
					return true;
				}
			}
		}
		else if(pacman.direction.equals("down")){
			x = (pacman.xpos-interval)/interval;
			y = (pacman.ypos-interval)/interval;
			//System.out.println("x:"+x+"   y:"+y);
			if(y < colours[0].length-1){
				if(colours[x][y+1].equals("0,255,0")){
					return true;
				}
			}
		}
		else if(pacman.direction.equals("left")){
			x = (int) Math.ceil((pacman.xpos-interval)/(interval+0.00));
			y = (int) Math.ceil((pacman.ypos-interval)/(interval+0.00));
			//System.out.println("x:"+x+"   y:"+y);
			if(x > 0){
				if(colours[x-1][y].equals("0,255,0")){
					return true;
				}
			}
		}
		else if(pacman.direction.equals("right")){
			x = (pacman.xpos-interval)/interval;
			y = (pacman.ypos-interval)/interval;
			//System.out.println("x:"+x+"   y:"+y);
			if(x < colours.length-1){
				if(colours[x+1][y].equals("0,255,0")){
					return true;
				}	
			}
		}
		return false;
	}
	
	/**
	 * make mouth close if opened, make it open if it closed
	 */
	public void changeIcon(){
		if (pacman.direction.equals("up")){
			if (mouthOpen)
				pacmansprite.setIcon(new ImageIcon("src/pacman_img/UpClosed.png"));
			else
				pacmansprite.setIcon(new ImageIcon("src/pacman_img/UpOpened.png"));
		}
		if (pacman.direction.equals("down")){
			if (mouthOpen)
				pacmansprite.setIcon(new ImageIcon("src/pacman_img/DownClosed.png"));
			else
				pacmansprite.setIcon(new ImageIcon("src/pacman_img/DownOpened.png"));
		}
		if (pacman.direction.equals("left")){
			if (mouthOpen)
				pacmansprite.setIcon(new ImageIcon("src/pacman_img/LeftClosed.png"));
			else
				pacmansprite.setIcon(new ImageIcon("src/pacman_img/LeftOpened.png"));
		}
		if (pacman.direction.equals("right")){
			if (mouthOpen)
				pacmansprite.setIcon(new ImageIcon("src/pacman_img/RightClosed.png"));
			else
				pacmansprite.setIcon(new ImageIcon("src/pacman_img/RightOpened.png"));
		}
		
		mouthOpen = !mouthOpen;
	}
	
	public void foodSound(){//play eating food sound
        if (enableSound){
	        clip.play();
        }
		
	}
	
	/**
	 * returns true if win/lose happened
	 */
	public void checkGame(){
		if (foodCounter == 0){ // all food is gone, win
			System.out.println("You won!");
			//stops ghosts and pacman
			pacman.speed = 0;
			g1.speed = 0;
			g2.speed = 0;
			g3.speed = 0;
			lost = true; // return to menu 
		}
		// if pacman collide with ghost, lose
		if (collision2(pacmansprite,g1.sprite) || collision2(pacmansprite,g2.sprite) || collision2(pacmansprite,g3.sprite)){
			System.out.println("You lost");
			lost = true;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}