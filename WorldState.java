import java.awt.Color;

public class WorldState {
	
	//START CONSTANTS
	public static final int turns = 100000; //if we wish to limit the number of turns
	public static final boolean useTurns = true; //true to use above number, false to ignore
	public static final int startOrgNum = 100;
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
	public static final int wLength = 1600; 
	public static final int wWidth = 800; 
	public static final int pLength = 20;
	public static final int pWidth = 20;
	public static final int pLnum = wLength / pLength;
	public static final int pWnum = wWidth / pWidth;
	public static final int winLength = (int) ( wLength * 1.4);
	public static final int winWidth = (int) (wWidth * 1.6);
	
	//---------------------------------------------------------------------
	//TERRAIN CONSTANTS
	//{0 = Grassland}
	public static final int [] terrainTypes = { 0, 1, 2 };
	public static final Color [] terrainColor = {Color.green, Color.blue, Color.cyan};
	public static final String [] terrainName = {"Grassland, Water, Wetland"};
	public static final double [] terrainProb = { 0.90, 0.05, 0.05}; //must add up to 1.0
	
	//---------------------------------------------------------------------
	//RESOURCE CONSTANTS
	public static final String [] resourceName = {"Apple", "Peach"};
	public static final int [] resourceNum = {0, 1};
	public static final Color [] resourceColor = {Color.pink, Color.yellow};
	public static final int [] resourceShape = {0, 2};
	//0 = north-triangle, 1 = south triangle, 2 = square
	
	public static final int [] resourceType = {0};
	public static final String [] resourceTypeName = {"Food"};
	public static final String [] resourceTypeShape = {"Triangle"};
	//EXAMPLE: Food is type 0, and represented by triangle. A specific type of food, say Apple which is 0 would be red (and a triangle)
	
	public static final double [][] rSpawnChance = { {0.05, 0}, {0, 0.05}, {0.05, 0.05}};//X = terrain, Y = resourceNum, the value is the probability of spawning that resource
	
	
	public static double getDrift()
	{
		return driftFactor;
	}
	
	public static int getID(){
		counterID++;
		return counterID - 1;
	}
	
}
