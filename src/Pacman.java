
class Pacman{
	int xpos = 240; // position of pacman
	int ypos = 200;
	int speed = 1; //speed 
	String direction = "right"; // initial direction is set to right

	//The movement of pacman
	public void move(){
		if(direction.equals("up")){
			ypos-=speed;
		}
		else if(direction.equals("down")){
			ypos+=speed;
		}
		else if(direction.equals("left")){
			xpos-=speed;
		}
		else if(direction.equals("right")){
			xpos+=speed;
		}
		else{
			
		}
	}
	// set direction 
	public void setdirection(String dir){
		direction = dir;
	}
	
}