import java.awt.Color;


public class WorldState {
	
	//SEEDS
	public static final int patchSeed = 564421;
	
	//TESTING FLAGS
	public static final boolean isDebug = true;
	
	//World Constants/Counters
	public static final double driftFactor = 0.05;
	private static int counterID = 0;
	
	public static final int wLength = 1000; 
	public static final int wWidth = 1000; 
	public static final int pLength = 5;
	public static final int pWidth = 5;
	public static final int pLnum = wLength / pLength;
	public static final int pWnum = wWidth / pWidth;
	//public final int pLength = 20;
	//public final int pHeight = 20;
	
	//---------------------------------------------------------------------
	//TERRAIN CONSTANTS
	//{0 = Grassland}
	public static final int [] terrainTypes = { 0 };
	public static final Color [] terrainColor = {Color.green};
	public static final String [] terrainName = {"Grassland"};
	
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
	
	
	public double getDrift()
	{
		return driftFactor;
	}
	
	public int getID(){
		counterID++;
		return counterID - 1;
	}
	
}
