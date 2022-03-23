/*WorldState.java
 * CPSC 565 W2016: Project
 * Jason Schneider and Emil Emilov-Dulguerov
 * This class holds all the constants for the project, making it the goto file for quick tweaks
 * 
 */
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;



public class WorldState {
	
	//START CONSTANTS
	public static final int turns = 10000; //if we wish to limit the number of turns
	public static final boolean useTurns = true; //true to use above number, false to ignore
	public static final int startOrgNum = 100;
	public static final int sleepTime = 100;
	public static final boolean allowResourceExhaustion = false;
	
	//TESTING FLAGS
	public static final boolean isDebug = false;
	public static final boolean trueRandom = true;
	public static final int forcedReproductionEvent = 500;
	
	//SEEDS
	public static final int seed0 = 564421;
	public static final int seed1 = 4939;
	public static final int seed2 = 11477;
	public static final int seed3 = 7319551;
	public static final int seed4 = 3;
	public static final int seedM = 100000;
	
		
	//RANDOM NUMBER MAKERS
	public static final int streamNumber = 6;
	public static RandomNumberMaker [] rng0; //FOR PATCH-RESOURCE(spawn/replenish amount, spawn/replenish chance)-MUTATION(pickParent/litterSize, drift/radical chance, gene value)
	public static RandomNumberMaker [] rng1; //FOR ENTITY'S (Genes, x-coord, y-coord, name, woundStuff/mood, food-eat)
	public static RandomNumberMaker [] rng2; //BATTLE(speed tiebreaker)-TEAM1(target choice, run success, action choice, attack-crit, attack-dodge)
	public static RandomNumberMaker [] rng3; //ACTIONS(priority choice, harvest, action choice, direction choice
	//public static RandomNumberMaker [] rng4; //RESERVED
	public static RandomNumberMaker rngMove; //Movement
		
	//World Constants/Counters
	public static final double driftFactor = 0.10;
	public static final double driftAmount = 0.05;
	public static final double radicalMutation = 0.01;
	public static final int maxLitter = 20;
	private static int counterID = 1;
	
	//world/patch/window dimensions
	//World Size must be be divisible by patch size, or else it will BREAK
	public static final int wLength = 1200; 
	public static final int wWidth = 800; 
	public static final int bLength = 400;
	public static final int pLength = 20;
	public static final int pWidth = pLength;
	public static final int pLnum = wLength / pLength;
	public static final int pWnum = wWidth / pWidth;
	public static final int winLength = (int) (wLength + pLength * 3);
	public static final int winWidth = (int) (wWidth + pWidth * 4.25);
	
	//---------------------------------------------------------------------
	//TERRAIN CONSTANTS
	//{0 = Grassland, 1 = Water, 2 = Wetlands}
	public static final int [] terrainTypes = { 0, 1, 2 };
	public static final Color [] terrainColor = {Color.green, Color.blue, Color.cyan};
	public static final String [] terrainName = {"Grassland, Water, Wetland"};
	public static final double [] terrainProb = { 0.90, 0.05, 0.05}; //must add up to 1.0
	
	//---------------------------------------------------------------------
	//RESOURCE CONSTANTS
	public static final Color rExhaustionColor = Color.gray;
	public static final int resourceAmountMax = 501; //max that can spawn in a patch
	public static final String [] resourceName = {"Apple", "Peach"};
	public static final int [] resourceNum = {0, 1};
	//public static final int [] resourceNourishment = {} 
	public static final Color [] resourceColor = {Color.pink, Color.yellow};
	public static final int [] resourceShape = {0, 1};
	//0 = north-triangle, 1 = south triangle, 2 = square
	
	public static final int [] resourceType = {0};
	public static final String [] resourceTypeName = {"Food"};
	public static final String [] resourceTypeShape = {"Triangle"};
	//EXAMPLE: Food is type 0, and represented by triangle. A specific type of food, say Apple which is 0 would be red (and a triangle)
	
	public static final double [][] rSpawnChance = { {0.05, 0}, {0, 0.05}, {0.05, 0.05}};//X = terrain, Y = resourceNum, the value is the probability of spawning that resource
	public static final double [][] rReplenishChance = { {0.05, 0}, {0, 0.05}, {0.05, 0.05}};//X = terrain, Y = resourceNum, the value is the probability of respawning an existing resource
	public static final double respawnAmountMax = 0.5; //dependant on initial value
	
	//outputStuff
	public static ArrayList<String> EventLog= new ArrayList<String>();
	public static String logFile = "colonialCognitionLog.txt";
	public static String statFile = "colonialCognitionStats.csv";
	public static ArrayList<String> nameVault = new ArrayList<String>();
	public static ArrayList<GeneSequence> geneVault = new ArrayList<GeneSequence>();
	
	//LOG FLAGS
	public static boolean logMove = true;
	
	//ENTITY Constants
	public static String [] names = {"Eddard", "Caitlyn", "Robb", "Jon", "Sansa", "Bran", "Arya", "Rickon", "Brandon", "Rickard", "Ned", "Hoster", "Edmure", "Lysa", "Cersei", "Jaime", "Tywin", "Joanna", "Tyrion", "Joffrey", "Myrcella", "Tommen", "Robert", "Renly", "Stannis", "Melisandra"}; 
	public static boolean collisionDetection = false;
	public static int densityAllowance = 2; //max number of organisms per patch
	public static int resourceCarryConstant = 100; //carryCapacity is multiplied by this constant to find the organism's ability
	public static int reproductiveMaturityAge = 13;
	
	//ORGANISM ENERGY CONSTANTS
	public static final int regenThreshold = 20;
	public static final int lowEnergyThreshold = 40;
	public static final int highEnergyThreshold = 10 * lowEnergyThreshold;
	public static final double fatiguePenalty = 0.10;
	public static final int fightEnergyCost = 0;
	public static final int moveEnergyCost = 0;
	public static final int lifeEnergyCost = 10; //energy consumed per turn just for living, multiplied by neuro
	public static final int calorieFactor = lowEnergyThreshold / 4; //energy generated per food unit
	
	//DESPERATION LEVELS
	public static final int CRITICAL = 4;
	public static final int HIGH = 3;
	public static final int MEDIUM = 2;
	public static final int LOW = 1;
	public static final int NONE = 0;
	public static final double baseSafety = 0.10; //for decision calculations
	
	
	public static void addLogEvent(String newEvent)
	{
		EventLog.add(newEvent);
	}
	
	public static ArrayList<String> getLog()
	{
		return EventLog;
	}

	public static ArrayList<String> getStats()
	{//TODO Output an ArrayList of statistical information, which needs to be generated first
		return EventLog;
	}
	
	public static double getDrift()
	{
		return driftFactor;
	}
	
	public static synchronized int getID(){
		counterID++;
		return counterID - 1;
	}
	
	public static String randName(int seed){
		if (seed > names.length) seed = seed % names.length;
		return names[seed];
	}
	
	public static void addGenome(GeneSequence gene){
		geneVault.add(gene);
	}
	
	public static void addName(String name)
	{
		nameVault.add(name);
	}
	
	public static void establishRNG()
	{
		
		int r0 = seed0;
		int r1 = seed1;
		int r2 = seed2;
		int r3 = seed3;
		int r4 = seed4;	
		
		if (trueRandom) {
			
			RandomNumberMaker trng = new RandomNumberMaker(System.currentTimeMillis(),0);
			r0 = (int) trng.lcg(seed0 * trng.rInt());
			r1 = (int) trng.lcg(seed1 * trng.rInt());
			r2 = (int) trng.lcg(seed2 * trng.rInt());
			r3 = (int) trng.lcg(seed3 * trng.rInt());
			r4 = (int) trng.lcg(seed4 * trng.rInt());	
		}
		
		rng0 = new RandomNumberMaker[streamNumber];
		rng1 = new RandomNumberMaker[streamNumber];
		rng2 = new RandomNumberMaker[streamNumber];
		rng3 = new RandomNumberMaker[streamNumber];
		//rng4 = new RandomNumberMaker[streamNumber];
		rngMove = new RandomNumberMaker(seedM, 0);
		
		for (int i = 0; i < streamNumber; i++)
		{
			//establish the stream start
			rng0[i] = new RandomNumberMaker(r0, i);
			rng1[i] = new RandomNumberMaker(r1, i);
			rng2[i] = new RandomNumberMaker(r2, i);
			rng3[i] = new RandomNumberMaker(r3, i);
			//rng4[i] = new RandomNumberMaker(r4, i);
			
			//set the seed to reduce calculation time for the next stream
			r0 = (int) rng0[i].getLast();
			r1 = (int) rng1[i].getLast();
			r2 = (int) rng2[i].getLast();
			r3 = (int) rng3[i].getLast();
			//r4 = (int) rng4[i].getLast();				
		}
	}
	
}
