import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JApplet;
import javax.swing.JFrame;


public class Starter {

	/**
	 * @param args
	 */
	private static boolean isDebug = WorldState.isDebug; //change this to false to disable output messages
	private static final int bobSize = 2;
	private static final int robSize = 2;
	private static Random rng;
	private static ArrayList<Entity> lifeForms;
	private static GridUniverse grid;
	
	
	public static void main(String[] args) {
		//1) Create any resources needed (RNG's for example)
		//Create the world:
		//	2) Terrain
		//	3) Resources, and their placement
		//	4) Entities, and their placement
		//	5) Load gfx
		// 	6) Start Simulation
	
		//5)
		//worldWindow = terraGenesis();
		rng = new Random(123456789);
		emilGenesis();
		
		//combatTest();
		danceParty();
	}
	
	public static void combatTest(){
		//--------------------------------------------------------------
		//             Fight Test
		//------------------------------------------------------------
		//Entity(name, hp, damage, speed, crit, avoidance, flightChance
		BattleState bob = new BattleState(new Entity("Bob", 130, 20, 20, 0.1, 0.1, 0.2), isDebug);
		BattleState rob = new BattleState(new Entity("Rob", 100, 30, 15, 0.1, 0.1, 0.5), isDebug);
		bob.plant(1337);
		rob.plant(7331);
		TestFight fight = new TestFight(bob, rob, isDebug);
		fight.plant(56456);
		if (isDebug) System.out.println("Begin Fight Simulation.");
		fight.fightSim();
		
		//--------------------------------------------------------------
		//             Battle Test
		//------------------------------------------------------------
		BattleState [] teamBob = new BattleState[bobSize];
		BattleState [] teamRob = new BattleState[robSize];
		
		for (int i =0; i < bobSize; i++)
		{
			teamBob[i] = new BattleState(new Entity("Bob", 130, 20, 20, 0.1, 0.1, 0.2), isDebug);	
			teamBob[i].plant(rng.nextInt());
		}
		
		for (int i =0; i < robSize; i++)
		{
			teamRob[i] = new BattleState(new Entity("Rob", 100, 30, 15, 0.1, 1, 0.5), isDebug);
			teamRob[i].plant(rng.nextInt());
		}
		
		TestBattle testBattle = new TestBattle(teamBob, teamRob, isDebug);
		testBattle.plant(rng.nextInt());
		if (isDebug) {
			System.out.println("-------------------------------------------------------------");
			System.out.println("Begin Battle Simulation. (" + bobSize + " vs " + robSize + ")");
		}
		
		testBattle.battleSim();
		if (isDebug) {
			System.out.println("-------------------------------------------------------------");
			System.out.println("Simulation Complete, exiting.....");
			
		}
	}

	private static void entityGenesis(){
		for (int i = 0; i < WorldState.startOrgNum; i++)
		{
			Entity e = new Entity();
			lifeForms.add(e);
		}
	}
	
	private static void danceParty(){
		if (WorldState.useTurns){
			for (int i = 0; i < WorldState.turns; i++)
			{
				for (int index = 0; index < lifeForms.size(); index++){
					lifeForms.get(index).move();
				}
				System.out.println("Turn: " + i);
				grid.repaint();
				  try{
		               Thread.sleep(WorldState.sleepTime); //slowed the loop to approximately 30 fps
		                  }catch(Exception e) {}
			}//end for loop
		}//end if-then
}//end danceParty
	
	private static void emilGenesis(){
		//Create the graphic window, and the world behind it
		Random tgRNG = new Random(WorldState.patchSeed);
		grid = new GridUniverse(WorldState.pLnum, WorldState.pWnum, tgRNG);	
		JFrame frame = new JFrame("Planet Terra Nova");
		//I've left some space on the left side of the grid for buttons
		//feel free to change the position of the grid within the frame
		frame.setSize(WorldState.winLength, WorldState.winWidth); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(grid); 
		frame.setVisible(true);
		int x;// = rng.nextInt(WorldState.pLnum);
		int y;// = rng.nextInt(WorldState.pWnum);
		
		lifeForms = new ArrayList<Entity>();
		entityGenesis();
		//Organism [] organism = new Organism[WorldState.startOrgNum];
		
		for (int index = 0; index < WorldState.startOrgNum; index++){
			x = rng.nextInt(WorldState.pLnum);
			y = rng.nextInt(WorldState.pWnum);
			//organism[index] = new Organism(grid, x, y);
			lifeForms.get(index).setTheOrg(new Organism(grid, x, y));			
		}		
	}
	
	
}
