

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
	int oldDirection;
	boolean canMove = true; //will be used in the future to determine organism's capacity to move (true by default)
	Perception sight;
	Color cSaw;
	Organism owner;
	boolean hunting;
	
	double totalRandomMoveChance = 0.2;
	int [] distanceO = {5, 6, 7, 8, 1, 2, 3, 4}; //complete opposite direction
	int [] distancePL = {7, 8, 1, 2, 3, 4, 5, 6}; //Perpendicular Left
	int [] distancePR = {3, 4, 5, 6, 7, 8, 1, 2}; //Perpendicular Right
	
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
	
	
	/**
	 * Used to move an entity towards a specific destination
	 */ 
	public void move(Patch target) throws AWTException {
		//TODO IMPLEMENT THIS
		if (owner.getBattlePenalty() > 0.9) canMove = false;
		else canMove = true;
	if(canMove) {
		
		int dX = target.getX() - locationX + 1, tX;;
		int dY = target.getY() - locationY + 1, tY;
		//this info will be used to erase former position block on the grid
		//grid.setFormerPosition(locationX, locationY);
		setFormerPosition(locationX, locationY);
		int [][] dPatch = {{8,1,2},{7,0,3},{6,5,4}};
		
		if (dX > 1)  tX = 2; //x coord is right
		else if (dX > 1) tX = 0; //x coord is left
		else tX = 1; //x coord is same row
		if (dY > 1)  tY = 2; //y coord is down
		else if (dY > 1) tY = 0; //y coord is up
		else tY = 1; //y coord is same column
		
		direction = dPatch[tX][tY];
	
		int attempt = 0, attemptMax = 8;			
		
		tryAnotherDirection: //a convenient goto label
		while(true){
			
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
				double roll = WorldState.rng3[3].rDouble();
				if (roll < 0.5) {//try to move beside
					if (direction == 1) direction = 8;
					else direction--;
				} else {
					if (direction == 8) direction = 1;
					else direction++;
				}
				
				continue tryAnotherDirection;
			}
			else {
				if (!WorldState.collisionDetection ||  grid.getPatchGrid()[locationX][locationY].isHasEntity() == false || grid.getPatchGrid()[locationX][locationY].population() < WorldState.densityAllowance) break;
				else {
					attempt++;			
					if (attempt < attemptMax) continue tryAnotherDirection;
				}
				}
		}
		
		if (!WorldState.collisionDetection ||  grid.getPatchGrid()[locationX][locationY].isHasEntity() == false|| grid.getPatchGrid()[locationX][locationY].population() < WorldState.densityAllowance){
			grid.getPatchGrid()[locationX][locationY].setTheE(owner);
			grid.getPatchGrid()[formerCellX][formerCellY].removeE(owner);
			if (WorldState.logMove) WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + owner.getName() + " has moved from [" + formerCellX + "," + formerCellY + "] to [" +locationX + "," + locationY + "]");
			Starter.getStats().incMoveMoves(1);
			//grid.fillCell(locationX, locationY, this);
		}
		//grid.repaint();
	}
}
	
	/**
	 * For moving an entity randomly
	 * @param turn
	 * @throws AWTException
	 */
public void randomMove() throws AWTException {
		
		if (owner.getBattlePenalty() > 0.9) canMove = false;
		else canMove = true;
	if(canMove) {
		
		//this info will be used to erase former position block on the grid
		//grid.setFormerPosition(locationX, locationY);
		setFormerPosition(locationX, locationY);
		int attempt = 0, attemptMax = 8;			
		
		tryAnotherDirectionR: //a convenient goto label
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
				continue tryAnotherDirectionR;
			}
			else {
				if (!WorldState.collisionDetection ||  grid.getPatchGrid()[locationX][locationY].isHasEntity() == false|| grid.getPatchGrid()[locationX][locationY].population() < WorldState.densityAllowance) break;
				else {
					attempt++;			
					if (attempt < attemptMax) continue tryAnotherDirectionR;
				}
				}
		}
		
		if (!WorldState.collisionDetection ||  grid.getPatchGrid()[locationX][locationY].isHasEntity() == false|| grid.getPatchGrid()[locationX][locationY].population() < WorldState.densityAllowance){
			grid.getPatchGrid()[locationX][locationY].setTheE(owner);
			grid.getPatchGrid()[formerCellX][formerCellY].removeE(owner);
			if (WorldState.logMove) WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + owner.getName() + " has wandered from [" + formerCellX + "," + formerCellY + "] to [" +locationX + "," + locationY + "]");
			Starter.getStats().incWanderMoves(1);
		}

	}
}

	public void hunt() throws AWTException
	{
		if (hunting) continueHunt();
		else {
			hunting = true;
			oldDirection = direction; //save the oldDirection
			beginHunt();
		}
	}

	public void continueHunt() throws AWTException{
		int path = WorldState.rng3[3].rInt() % 3;
		
		if (path == 2){//slight turn left
			if (direction == 1) direction = 8;
			else direction--;
		} else if (path == 1){//slight right turn
			if (direction == 8) direction = 1;
			else direction++;
		}
		beginHunt();
	}
	
	public void beginHunt() throws AWTException{
		//when an organism is looking for something specific, but can't see it (ex: food), tends to prefer moving far away
		
		if (owner.getBattlePenalty() > 0.9) canMove = false;
		else {
			canMove = true;
		
			double roll = WorldState.rng3[3].rDouble();
			if ( roll != totalRandomMoveChance) {
				//erratic movement pattern
				//TODO
				randomMove();
				return;
			}	

			direction = distanceO[oldDirection - 1]; //start by picking the opposite direction
			int attempts = 0;
			int attemptMax = 0;
			
			setFormerPosition(locationX, locationY);		
			
			tryAnotherDirectionH: //a convenient goto label
			while(true){
				
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
					
					roll = WorldState.rng3[3].rDouble();
					if (roll < 0.2) {
						if (direction == 1) direction = 8;
						else direction--;
					} else if (roll < 0.4) {
						if (direction == 8) direction = 1;
						else direction++;
					} else if (roll < 0.6) direction = distancePL[oldDirection - 1];
					else direction = distancePR[oldDirection - 1];
					
					if (attempts > attemptMax)
					{//resort to random
						randomMove();
						return;
					}
					
					continue tryAnotherDirectionH;
				}
				else {
					if (!WorldState.collisionDetection ||  grid.getPatchGrid()[locationX][locationY].isHasEntity() == false|| grid.getPatchGrid()[locationX][locationY].population() < WorldState.densityAllowance) break;
					else {
						attempts++;			
						if (attempts < attemptMax) continue tryAnotherDirectionH;
					}
					}
			}
			
			if (!WorldState.collisionDetection ||  grid.getPatchGrid()[locationX][locationY].isHasEntity() == false|| grid.getPatchGrid()[locationX][locationY].population() < WorldState.densityAllowance){
				grid.getPatchGrid()[locationX][locationY].setTheE(owner);
				grid.getPatchGrid()[formerCellX][formerCellY].removeE(owner);
				if (WorldState.logMove) WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + owner.getName() + " has gone on a search from [" + formerCellX + "," + formerCellY + "] to [" +locationX + "," + locationY + "]");
				Starter.getStats().incHuntMoves(1);
			}
		}//canMove
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

	public Perception getSight() {
		return sight;
	}
	
	public Patch getPatch()
	{
		return grid.getPatchGrid()[locationX][locationY];
	}
	
	public void setHunt(boolean h)
	{
		hunting = h;
	}
	
	
}
