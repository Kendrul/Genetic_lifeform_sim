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
	
	//-----------------------------------------------------------------------------------------------------------------------
	//	Change these values to tweak the simulation
	//-------------------------------------------------------------------------------------------
	//START CONSTANTS
	public static final int trials = 1; //set to greater than 1 to allow back to back simulation runs with same ruleset //DEFAULT 1

	public static final int turns = 5000; //if we wish to limit the number of turns //DEFAULT 5000
	public static final int sleepTime = 33; //set this higher to slow down the simulation, set it lower to speed it u //DEFAULT 33 or 66	
	public static final boolean trueRandom = true; //DEFAULT true
	public static final int startOrgNum = 300; //DEFAULT 300
	public static final int forcedReproductionEvent = 500; //DEFAULT 500
	
	//Fitness Rules: FoodCarried, FoodEaten, FoodShared, FoodStolen, Fights, FightsWon, Agreeability
	//0 = not a factor, 1 = positive factor, 2 = negative factor
	//First 2 can only be 1 or 0, the rest can be 0, 1, 2
	public static final int [] useThisRuleSet = {0, 0, 2, 1, 1, 1, 2}; 

	//Reproduction Rule: {# of Previous Generation, # of offspring, # of randomly created}
	//DEFAULT {0.1, 0.7, 0.2} -> Rule 172
	public static final double [] reproductiveRuleSet = {0.1,0.7, 0.2};
	//Terrain probability, 2nd and 3rd values are ALWAYS food patches
	public static final double [] terrainProb = { 0.90, 0.05, 0.05}; //must add up to 1.0 //DEFAULT 0.96, 0.02, 0.02
	//resource distribution rule

	//0 = RANDOM, 1 = 2 CORNER CLUSTER, 2= 4 CORNER CLUSTER, 3 =One Corner CLUSTER  

	public static final int resourceRule = 0;
	
	
	//Mutation CONSTANTS	
	public static final double driftFactor = 0.10; //Drift Mutation chance //DEFAULT IS 0.10
	public static final double driftAmount = 0.05; //Drift Mutation range //DEFAULT is 0.05
	public static final double radicalMutation = 0.01; //radical mutation chance (anything can heppen) //DEFAULT is 0.01
	
	//Organism base multipliers
	public static int baseEnergy = 200; //DEFAULT 100
	public static int resourceCarryConstant = 200; //carryCapacity is multiplied by this constant to find the organism's ability //DEFAULT 100
	public static final int hpConstant = 400; //factors into starvation survival and //Default 400
	public static final int attackConstant = hpConstant; //fight capability //DEFAULT same as hpConstant
	public static final double baseFlight = 0.1; //base probability multiplier to flee a battle
	 
		//SEEDS
	public static final int seed0 = 564421;
	public static final int seed1 = 4939;
	public static final int seed2 = 11477;
	public static final int seed3 = 7319551;
	public static final int seed4 = 3;
	public static final int seedM = 73;
	
	
//-------------------------------------------------------------------------------------------------------------------------------
	//BACKGROUND SIMULATION CONSTANTS
	//THESE SHOULD NOT BE CHANGED
//--------------------------------------------------------------------
	

	public static final boolean allowResourceExhaustion = false;
	public static final boolean useTurns = true; //true to use above number, false to ignore
	
	//TESTING FLAGS
	public static final boolean isDebug = false;
	public static final boolean reproductionDeath = false; //true = die when reproduce, false = keep living
	
	//R-FITNESS RULES
	public static final int rRuleNum = 7;
	public static int [] rRuleSet = new int[rRuleNum];
	public static final boolean useOldRule = false;
	//0 = no factor, 1 = positive factor, 2 = negative factor
  /*public static final int [] rRuleFc = { 0, 1};
	public static final int [] rRuleFe = { 0, 1};
	public static final int [] rRuleFst = { 0, 1, 2};
	public static final int [] rRuleFsh = { 0, 1, 2};
	public static final int [] rRuleBw = { 0, 1, 2};
	public static final int [] rRuleBn = { 0, 1, 2};
	public static final int [] rRuleA = { 0, 1, 2};*/
	
		//ORGANISM ENERGY CONSTANTS 
	public static final int fightEnergyCost = 0; //DEFAULT 0
	public static final int moveEnergyCost = 0; //DEFAULT 0
	public static int reproEnergyCost = 0; //DEFAULT 0
	public static final int lifeEnergyCost = 10; //energy consumed per turn just for living, multiplied by neuro
	
	public static final int regenThreshold = 20; //DEFAULT 20
	public static final int lowEnergyThreshold = 100; //determine caloric value, and high energy //DEFAULT 40
	public static final int highEnergyThreshold = 10 * lowEnergyThreshold;
	public static final double fatiguePenalty = 0.10;
	public static final int calorieFactor = lowEnergyThreshold / 5; //energy generated per food unit
	
	public static int avgFC = 0;
	public static int avgFE = 0;
	public static int avgFS = 0;
	public static int avgFT = 0;
	public static int avgBN = 0;
	public static int avgBW = 0;
	public static double avgA = 0.0;
		
	//RANDOM NUMBER MAKERS
	public static final int streamNumber = 6;
	public static RandomNumberMaker [] rng0; //FOR PATCH-RESOURCE(spawn/replenish amount, spawn/replenish chance)-MUTATION(pickParent/litterSize, drift/radical chance, gene value)
	public static RandomNumberMaker [] rng1; //FOR ENTITY'S (Genes, x-coord, y-coord, name/fight decision, woundStuff/mood, food-eat)
	public static RandomNumberMaker [] rng2; //BATTLE(speed tiebreaker)-(target choice, run success, action choice, attack-crit, attack-dodge)
	public static RandomNumberMaker [] rng3; //ACTIONS(priority choice, harvest, action choice, direction choice, persona chance
	//public static RandomNumberMaker [] rng4; //RESERVED
	public static RandomNumberMaker rngMove; //Movement
		
	//World Constants/Counters

	public static final int maxLitter = 20;
	private static int counterID = 1;
	public static int sitMax = 10;
	

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
	
	
	//---------------------------------------------------------------------
	//RESOURCE CONSTANTS
	public static final boolean randomFood = false;
	public static final Color rExhaustionColor = Color.gray;
	public static final int resourceAmountMax = 1001; //max that can spawn in a patch
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
	
	public static final double [][] rSpawnChance = { {0.00, 0}, {0, 1.0}, {1.0, 0.0}};//X = terrain, Y = resourceNum, the value is the probability of spawning that resource
	public static final double [][] rReplenishChance = { {0.10, 0}, {0, 0.10}, {0.10, 0.10}};//X = terrain, Y = resourceNum, the value is the probability of respawning an existing resource
	public static final double respawnAmountMax = 0.5; //dependant on initial value //NOT USED
	
	//outputStuff
	public static ArrayList<String> EventLog;
	public static String logFile = "colonialCognitionLog.txt";
	public static String statFile = "colonialCognitionGenStats.csv";
	public static String orgFile = "colonialCognitionOrganismData.csv";
	public static String finalStatFile = "colonialCognitionFinalStats.csv";
	public static ArrayList<String> nameVault = new ArrayList<String>();
	public static ArrayList<GeneSequence> geneVault = new ArrayList<GeneSequence>();
	public static ArrayList<OrgInfo> generationVault = new ArrayList<OrgInfo>();
	public static ArrayList<StatPack> turnVault = new ArrayList<StatPack>();
	
	//LOG FLAGS
	public static boolean logMove = true;
	
	//ENTITY Constants
	public static String [] names = {"Eddard", "Caitlyn", "Robb", "Jon", "Sansa", "Bran", "Arya", "Rickon", "Brandon", "Rickard", "Ned", "Hoster", "Edmure", "Lysa", "Cersei", "Jaime", "Tywin", "Joanna", "Tyrion", "Joffrey", "Myrcella", "Tommen", "Robert", "Renly", "Stannis", "Melisandra"}; 
	public static boolean collisionDetection = false;
	public static int densityAllowance = 2; //max number of organisms per patch
	public static int reproductiveMaturityAge = 13;
	
	//DESPERATION LEVELS
	public static final int CRITICAL = 4;
	public static final int HIGH = 3;
	public static final int MEDIUM = 2;
	public static final int LOW = 1;
	public static final int NONE = 0;
	public static final double baseSafety = 0.10; //for decision calculations
	

	/**
	 * Add a log event string, for log output
	 * @param newEvent
	 */
	public static void addLogEvent(String newEvent)
	{
		EventLog.add(newEvent);
	}
	
	public static ArrayList<String> getLog()
	{
		addLogEvent("");
		return EventLog;
	}
	
	public static void resetLogs(){
		EventLog = new ArrayList<String>();
		nameVault = new ArrayList<String>();
		geneVault = new ArrayList<GeneSequence>();
		generationVault = new ArrayList<OrgInfo>();
		turnVault = new ArrayList<StatPack>();
	}

	public static ArrayList<String> getStats()
	{//Output an ArrayList of statistical information, which needs to be generated first
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
	
	public synchronized static void addGenome(GeneSequence gene){
		geneVault.add(gene);
	}
	
	public synchronized static void addName(String name)
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

	/**
	 * @param generationVault add OrgInfo to the GenerationVault
	 */
	public static synchronized void addGenerationVault(
			OrgInfo g) {
		WorldState.generationVault.add(g);
	}
	
	public static synchronized void resetTurn(){
		resetAverageFitness();
		if (WorldState.turnVault.size() > 0) Starter.getTurnStats().update(WorldState.nameVault.size() - (int) (WorldState.turnVault.get(WorldState.turnVault.size() -1).orgTotal));
		else Starter.getTurnStats().update(WorldState.nameVault.size());
		Starter.getTurnStats().update(WorldState.nameVault.size());
		turnVault.add(Starter.getTurnStats());
		Starter.setTurnStats(new StatPack());
	}
	
	public static synchronized void resetAverageFitness()
	{
		avgFC = 0;
		avgFE = 0;
		avgFS = 0;
		avgFT = 0;
		avgBN = 0;
		avgBW = 0;
		avgA = 0.0;
	}
	
	
}
