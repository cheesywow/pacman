import java.awt.Color;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ghost extends Level{
	int xpos = 0; // location of ghosts
	int ypos = 0;
	int speed = 1; //speed
	String[][] map;
	int[] wall = new int[4];
	int[] walls = new int[4];
	JLabel sprite;
	int random = 4; 
	boolean hit;
	Random rand;
	
	/**
	 * When a new ghost created, location and icon is passed on
	 */
	public ghost(int x, int y, ImageIcon icon, String[][] colours){
		rand = new Random();
		xpos = x;
		ypos = y;
		sprite = new JLabel("");
		map = new String[colours.length][colours[0].length];
		for(int a =0; a < colours.length; a++){
			for(int b =0; b < colours[0].length; b++){
				map[a][b] = colours[a][b];
			}
		}
		sprite.setIcon(icon);
		sprite.setBackground(Color.red);
		sprite.setForeground(Color.white);
		sprite.setBounds(xpos, ypos, interval, interval);	
	}

	/**
	 * ghosts will move randomly 
	 */
	public void move(){
		hit = false;
		//when ghost goes through wall
		checkwarp();
		for(int x = 0; x < 4; x++){
			wall[x]= 0;
			}


		surroundWall();
		
		if(hit == false){
			if(random == 1){ //up
				ypos-=speed;
			}
			else if(random == 2){//down
				ypos+=speed;
			}
			else if(random == 3){//left
				xpos-=speed;
			}
			else if(random == 4){//right
				xpos+=speed;
			}
		}
		
		
		
		if(random == 1 || random == 2){
			if(ypos%interval == 0){
				int i = 0;
				for(int x = 0; x < 4; x++){
					if(wall[x]==0){
						walls[i] = x+1;
						i++;
					}
				}
				int p = 0;
				if(random==1){
					do{
						p = rand.nextInt(i);
					}while(walls[p]==2);
				}
				else{
					do{
						p = rand.nextInt(i);
					}while(walls[p]==1);
				}
				random = walls[p];
			}
		}
		
		else if(random == 3 || random == 4){
			if(xpos%interval == 0){
				int i = 0;
				for(int x = 0; x < 4; x++){
					if(wall[x]== 0){
						walls[i] = x+1;
						i++;
					}
				}
				int p = 0;
				if(random==3){
					do{
						p = rand.nextInt(i);
					}while(walls[p]==4);
				}
				else{
					do{
						p = rand.nextInt(i);
					}while(walls[p]==3);
				}
				//System.out.println(""+walls[p]);
				random = walls[p];
			}
		}
	}

	
	public void surroundWall(){
		int x;
		int y;
		
		x = (int) Math.ceil((xpos-interval)/(interval+0.00));
		y = (int) Math.ceil((ypos-interval)/(interval+0.00));
		
			if(map[x][y-1].equals("0,255,0")){
				wall[0] = 1;
			}
			else if(map[x-1][y].equals("0,255,0")){
				wall[2] = 1;
			}
			
		if(random == 1){
			if(map[x][y-1].equals("0,255,0")){
				hit = true;
			}
		}
		else if(random == 3){
			if(map[x-1][y].equals("0,255,0")){
				hit = true;
			}
		}
			
		x = (xpos-interval)/interval;
		y = (ypos-interval)/interval;
			if(map[x][y+1].equals("0,255,0")){
				wall[1] = 1;
			}
	
			else if(map[x+1][y].equals("0,255,0")){
				wall[3] = 1;
			}
		if(random == 2){
			if(map[x][y+1].equals("0,255,0")){
				hit = true;
			}
		}
		else if(random == 4){
			if(map[x+1][y].equals("0,255,0")){
				hit = true;
			}
		}
	}
	
	public void checkwarp(){
		if(ypos > (map[0].length*interval)-2){
			ypos = interval+2;
		}
		else if(xpos > (map.length*interval)-2){
			xpos = interval+2;
		}
		else if(ypos < interval+2){
			ypos = (map[0].length*interval)-2;
		}
		else if(xpos < interval+2){
			xpos = (map.length*interval)-2;
		}
		
	}
	
}