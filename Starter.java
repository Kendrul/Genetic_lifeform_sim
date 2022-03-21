import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
	
	private static JApplet worldWindow;
	
	public static void main(String[] args) {
		//1) Create any resources needed (RNG's for example)
		//Create the world:
		//	2) Terrain
		//	3) Resources, and their placement
		//	4) Entities, and their placement
		//	5) Load gfx
		// 	6) Start Simulation
	
		//5)
		worldWindow = terraGenesis();
		//ShapesDemo2D sd2 = new ShapesDemo2D();
		//sd2.mainG(null);
		
		/*
		rng = new Random(123456789);
		WorldState world = new WorldState();
		//--------------------------------------------------------------
		//             Fight Test
		//------------------------------------------------------------
		//Entity(name, hp, damage, speed, crit, avoidance, flightChance
		BattleState bob = new BattleState(new Entity("Bob", 130, 20, 20, 0.1, 0.1, 0.2, world), isDebug);
		BattleState rob = new BattleState(new Entity("Rob", 100, 30, 15, 0.1, 0.1, 0.5, world), isDebug);
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
			teamBob[i] = new BattleState(new Entity("Bob", 130, 20, 20, 0.1, 0.1, 0.2, world), isDebug);	
			teamBob[i].plant(rng.nextInt());
		}
		
		for (int i =0; i < robSize; i++)
		{
			teamRob[i] = new BattleState(new Entity("Rob", 100, 30, 15, 0.1, 1, 0.5, world), isDebug);
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
		*/
	}

	private static JApplet terraGenesis(){
	    JFrame f = new JFrame("Planet Terra Nova"); //change this to change frame window title
	    f.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {System.exit(0);}
	    });

        Random tgRNG = new Random(WorldState.patchSeed);
        JApplet applet = new WorldWindow(WorldState.pLnum, WorldState.pWnum, tgRNG); //TODO change to WorldWindow when it has implementation
       	f.getContentPane().add("Center", applet);
	    applet.init();//TODO implement in worldwindow class
	    f.pack();
	    f.setSize(new Dimension(1000,1000)); //change this to change the size of the window
	    f.setVisible(true);
	    return applet;
	}
	
}
