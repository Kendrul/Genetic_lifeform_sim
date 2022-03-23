import java.util.Random;

//live, my little one; live and let die
public class Organism{

	GridUniverse grid;
	int birthPlaceX; //the mother/fatherland in x, y 
	int birthPlaceY; 
	int locationX; //current location in x, y
	int locationY;
	int formerCellX; //used to erase organism's former location (x,y) block color
	int formerCellY;
	int direction; //8 possible directions (think 12, 1.5, 3, 4.5, 6, 7.5, 9, 10.5 on a clock)
	boolean canMove = true; //will be used in the future to determine organism's capacity to move (true by default)
	Random rand = new Random();
	
	//boundary constants
	private int patchXnum = WorldState.pLnum;
	private int patchYnum = WorldState.pWnum;
	
	//let the divine spirit be your divine spark
	Organism(GridUniverse grid, int x, int y){
		this.grid = grid;
		this.birthPlaceX = x;
		this.birthPlaceY = y; 
		genesis(); 
		//locomotion();
	}
	  
	//from the earth we come, to the heavens we go
	public void genesis(){
			
		grid.fillCell(birthPlaceX, birthPlaceY, null); //jason
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
			
			grid.fillCell(locationX, locationY, this); //jason
			grid.repaint();
			  try{
	               Thread.sleep(33); //slowed the loop to approximately 30 fps
	                  }catch(Exception e) {}
		}
	}
	
	/**
	 * Used to move an entity one time
	 */ //jason
	public void move(){	
	if(canMove) {
		
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
			else {
				break;
				}
		}
		
		//grid.getPatchGrid()[locationX / WorldState.pLnum][locationY / WorldState.pWnum].setHasE(true);
		//grid.getPatchGrid()[formerCellX / WorldState.pLnum][formerCellY / WorldState.pWnum].setHasE(false);
		
		grid.fillCell(locationX, locationY, this);
		//grid.repaint();
	}
}
	//jason
	public void setFormerPosition(int x, int y){
		formerCellX = x;
		formerCellY = y;
	}
	
	//Einstein would be upset if he knew how random things really are...
	//Sorry Alberto, God does play with dice... Or does he? 
	//What if things are always random, but God never plays with dice? 
	//Whoa, mind blown dude. <-- says Alberto Einstein, the Rabbi of Physics  
	public int random(){ 
		int value = rand.nextInt(8) + 1; //for randomly generated values from 1 to 8 
		return value; 
	}
}
