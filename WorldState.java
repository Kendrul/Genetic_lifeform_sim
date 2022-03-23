import java.awt.Color;

public class WorldState {
	
	//START CONSTANTS
	public static final int turns = 1000; //if we wish to limit the number of turns
	public static final boolean useTurns = true; //true to use above number, false to ignore
	public static final int startOrgNum = 15;
	public static final int sleepTime = 100;
	
	//SEEDS
	public static final int patchSeed = 564421;
	
	//TESTING FLAGS
	public static final boolean isDebug = true;
	
	//World Constants/Counters
	public static final double driftFactor = 0.05;
	private static int counterID = 0;
	
	//world/patch/window dimensions
	//World Size must be be divisible by patch size, or else it will BREAK
	public static final int wLength = 800; 
	public static final int wWidth = 500; 
	public static final int pLength = 20;
	public static final int pWidth = 20;
	public static final int pLnum = wLength / pLength;
	public static final int pWnum = wWidth / pWidth;
	public static final int winLength = (int) ( wLength * 1.4);
	public static final int winWidth = (int) (wWidth * 1.6);
	
	//---------------------------------------------------------------------
	//TERRAIN CONSTANTS
	//{0 = Grassland}
	public static final int [] terrainTypes = { 0, 1 };
	public static final Color [] terrainColor = {Color.green, Color.blue};
	public static final String [] terrainName = {"Grassland, Water"};
	
	//---------------------------------------------------------------------
	//RESOURCE CONSTANTS
	public static final String [] resourceName = {"Apple"};
	public static final int [] resourceNum = {0};
	public static final Color [] resourceColor = {Color.red};
	
	public static final int [] resourceType = {0};
	public static final String [] resourceTypeName = {"Food"};
	public static final String [] resourceTypeShape = {"Circle"};
	//EXAMPLE: Food is type 0, and represented by circles. A specific type of food, say Apple which is 0 would be red (and a circle)
	
	public static final double [][] rSpawnChance = { {0.05} };//X = terrain, Y = resourceNum, the value is the probability of spawning that resource
	
	
	public static double getDrift()
	{
		return driftFactor;
	}
	
	public static int getID(){
		counterID++;
		return counterID - 1;
	}
	
}
