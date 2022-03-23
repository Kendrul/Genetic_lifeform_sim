

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.util.Random;

//live, my little one; live and let die
public class OrganismGFX{

	GridUniverse grid;
	int birthPlaceX; //the mother/fatherland in x, y 
	int birthPlaceY; 
	int locationX; //current location in x, y
	int locationY;
	int formerCellX; //used to erase organism's former location (x,y) block color
	int formerCellY;
	int direction; //8 possible directions (think 12, 1.5, 3, 4.5, 6, 7.5, 9, 10.5 on a clock)
	boolean canMove = true; //will be used in the future to determine organism's capacity to move (true by default)
	Perception sight;
	Color cSaw;
	Organism owner;

	
	//boundary constants
	private int patchXnum = WorldState.pLnum;
	private int patchYnum = WorldState.pWnum;
	
	//let the divine spirit be your divine spark
	OrganismGFX(GridUniverse grid, int x, int y)throws AWTException{
		this.grid = grid;
		this.birthPlaceX = x;
		this.birthPlaceY = y; 
		genesis(); 
		sight = new Perception(this);
		//locomotion();
	}
	  
	//from the earth we come, to the heavens we go
	public void genesis(){
			
		grid.fillCell(birthPlaceX, birthPlaceY, null); 
		locationX = birthPlaceX; 
		locationY = birthPlaceY; 	
	}
	
	//dance my child, dance
	public void locomotion(){
		while(canMove) {
		
			//this info will be used to erase former position block on the grid
			//grid.setFormerPosition(locationX, locationY);
			setFormerPosition(locationX, locationY);
				
			
			tryAnotherDirection: //a convenient goto label
			while(true){
				direction = random();
				
				//I decided to model locomotion using the 8 surrounding blocks/patches 
				switch(direction){
				case 1: locationY = locationY - 1; //1 is up toward 12 o'clock
					break;
				case 2: locationX = locationX + 1; locationY = locationY - 1; //2 is up-right toward 1.5 o'clock
					break;
				case 3: locationX = locationX + 1; //2 is right toward 3 o'clock
					break;
				case 4: locationX = locationX + 1; locationY = locationY + 1; //4 is down-right toward 4.5 o'clock
					break;
				case 5: locationY = locationY + 1; //5 is down toward 6 o'clock
					break;
				case 6: locationX = locationX - 1; locationY = locationY + 1; //6 is down-left toward 7.5 o'clock
					break;
				case 7: locationX = locationX - 1; //7 is left toward 9 o'clock
					break;
				case 8: locationX = locationX - 1; locationY = locationY - 1; //2 is up-left toward 10.5 o'clock
					break;
				}
				
				//created some boundary rules
				if (locationX < 0 || locationX > patchXnum - 1 || locationY < 0 || locationY > patchYnum - 1){
					
					//undoing the operation from the previous switch statement (neutralizing worm hole phenomena at boundary layer)
					switch(direction) {
					case 1: locationY = locationY + 1; //1 is up toward 12 o'clock
						break;
					case 2: locationX = locationX - 1; locationY = locationY + 1; //2 is up-right toward 1.5 o'clock
						break;
					case 3: locationX = locationX - 1; //2 is right toward 3 o'clock
						break;
					case 4: locationX = locationX - 1; locationY = locationY - 1; //4 is down-right toward 4.5 o'clock
						break;
					case 5: locationY = locationY - 1; //5 is down toward 6 o'clock
						break;
					case 6: locationX = locationX + 1; locationY = locationY - 1; //6 is down-left toward 7.5 o'clock
						break;
					case 7: locationX = locationX + 1; //7 is left toward 9 o'clock
						break;
					case 8: locationX = locationX + 1; locationY = locationY + 1; //2 is up-left toward 10.5 o'clock
						break;
					}
					continue tryAnotherDirection;
				}
				else { break; }
			}
			
			grid.fillCell(locationX, locationY, this); 
			grid.repaint();
			  try{
	               Thread.sleep(33); //slowed the loop to approximately 30 fps
	                  }catch(Exception e) {}
		}
	}
	
	/**
	 * Used to move an entity one time
	 */ 
	public void move(int turn) throws AWTException {	
	if(canMove) {
		
		//this info will be used to erase former position block on the grid
		//grid.setFormerPosition(locationX, locationY);
		setFormerPosition(locationX, locationY);
		int attempt = 0, attemptMax = 8;			
		
		tryAnotherDirection: //a convenient goto label
		while(true){
			organismPerceives();
			direction = random();
			
			//I decided to model locomotion using the 8 surrounding blocks/patches 
			switch(direction){
			case 1: locationY = locationY - 1; //1 is up toward 12 o'clock
				break;
			case 2: locationX = locationX + 1; locationY = locationY - 1; //2 is up-right toward 1.5 o'clock
				break;
			case 3: locationX = locationX + 1; //2 is right toward 3 o'clock
				break;
			case 4: locationX = locationX + 1; locationY = locationY + 1; //4 is down-right toward 4.5 o'clock
				break;
			case 5: locationY = locationY + 1; //5 is down toward 6 o'clock
				break;
			case 6: locationX = locationX - 1; locationY = locationY + 1; //6 is down-left toward 7.5 o'clock
				break;
			case 7: locationX = locationX - 1; //7 is left toward 9 o'clock
				break;
			case 8: locationX = locationX - 1; locationY = locationY - 1; //2 is up-left toward 10.5 o'clock
				break;
			}
			
			//created some boundary rules
			if (locationX < 0 || locationX > patchXnum - 1 || locationY < 0 || locationY > patchYnum - 1){
				
				//undoing the operation from the previous switch statement (neutralizing worm hole phenomena at boundary layer)
				switch(direction) {
				case 1: locationY = locationY + 1; //1 is up toward 12 o'clock
					break;
				case 2: locationX = locationX - 1; locationY = locationY + 1; //2 is up-right toward 1.5 o'clock
					break;
				case 3: locationX = locationX - 1; //2 is right toward 3 o'clock
					break;
				case 4: locationX = locationX - 1; locationY = locationY - 1; //4 is down-right toward 4.5 o'clock
					break;
				case 5: locationY = locationY - 1; //5 is down toward 6 o'clock
					break;
				case 6: locationX = locationX + 1; locationY = locationY - 1; //6 is down-left toward 7.5 o'clock
					break;
				case 7: locationX = locationX + 1; //7 is left toward 9 o'clock
					break;
				case 8: locationX = locationX + 1; locationY = locationY + 1; //2 is up-left toward 10.5 o'clock
					break;
				}
				continue tryAnotherDirection;
			}
			else {
				if (!WorldState.collisionDetection ||  grid.getPatchGrid()[locationX][locationY].isHasEntity() == false) break;
				else {
					attempt++;			
					if (attempt < attemptMax) continue tryAnotherDirection;
				}
				}
		}
		
		if (!WorldState.collisionDetection ||  grid.getPatchGrid()[locationX][locationY].isHasEntity() == false){
			grid.getPatchGrid()[locationX][locationY].setTheE(owner);
			grid.getPatchGrid()[formerCellX][formerCellY].removeE(owner);
			if (WorldState.logMove) WorldState.addLogEvent("[Turn:" + turn + "] " + owner.getName() + " has moved from [" + formerCellX + "," + formerCellY + "] to [" +locationX + "," + locationY + "]");
			//grid.fillCell(locationX, locationY, this);
		}
		//grid.repaint();
	}
}
	
	public void setFormerPosition(int x, int y){
		formerCellX = x;
		formerCellY = y;
	}
	
	//Einstein would be upset if he knew how random things really are...
	//Sorry Alberto, God does play with dice... Or does he? 
	//What if things are always random, but God never plays with dice? 
	//Whoa, mind blown dude. <-- says Alberto Einstein, the Rabbi of Physics  
	public int random(){ 
		int value = WorldState.rngMove.rInt(8) + 1; //for randomly generated values from 1 to 8 
		return value; 
	}
	
	public void organismPerceives() throws AWTException {
		sight.currentPointOfView();
	}
	
	public Point getPoint(){
		return new Point(locationX, locationY);
	}

	public Organism getOwner() {
		return owner;
	}

	public void setOwner(Organism owner) {
		this.owner = owner;
	}
	
	
}
