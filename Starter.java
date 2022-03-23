/*Starter.java
 * CPSC 565 W2016: Project
 * Jason Schneider and Emil Emilov-Dulguerov
 * This class contains the methods to initialize and run the simulation
 * 
 */

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Starter{

	/**
	 * @param args
	 */
	private static boolean isDebug = WorldState.isDebug; //change this to false to disable output messages
	private static final int bobSize = 2;
	private static final int robSize = 2;
	private static ArrayList<Organism> lifeForms;
	private static GridUniverse grid;
	private static int turn;
	
	private static boolean start = true;
	private static boolean reset = false;
	private static boolean pause = false;
	private static boolean end = false;
	
	//BUTTONS
	private static JButton startButton;
	private static JButton exitButton;
	private static JButton resetButton;
	private static JButton pauseButton;
	
	
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
		
		WorldState.addLogEvent("Starting Program....");
		System.out.println("Starting Program...");
		WorldState.establishRNG();
		try{		
			runSimulation();
		} catch (Exception e)
		{
			System.out.println("Exception Found: ");
			//System.out.println(e);
			e.printStackTrace();
		}

		WorldState.addLogEvent("Terminating Simulation......");
		FileOutput out = new FileOutput();
		out.outputFiles();
		System.out.println("Terminating Program...");
	}
	
	public static void geneMutationTest()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException{
        //tests gene creation and sequencing
		Organism dad = new Organism();
		dad.setName("Dad");
		GeneSequence dg = EventPack.randomGS();
		dad.setGenes(dg);
		lifeForms.add(dad);
		dad.setTheOrg(new OrganismGFX(grid, WorldState.rng1[1].rInt(WorldState.pLnum), WorldState.rng1[2].rInt(WorldState.pWnum)));
		
		Organism mom = new Organism();
		mom.setName("Mom");
		GeneSequence mg = EventPack.randomGS();
		mom.setGenes(mg);
		lifeForms.add(mom);
		mom.setTheOrg(new OrganismGFX(grid, WorldState.rng1[1].rInt(WorldState.pLnum), WorldState.rng1[2].rInt(WorldState.pWnum)));
			
		Organism parent = new Organism();
		parent.setName("Parent");
		GeneSequence pg = EventPack.randomGS();
		parent.setGenes(pg);
		lifeForms.add(parent);
		parent.setTheOrg(new OrganismGFX(grid, WorldState.rng1[1].rInt(WorldState.pLnum), WorldState.rng1[2].rInt(WorldState.pWnum)));
		
		double mutation = 0;
		EventPack.reproductionS(dad, mom, mutation);
		EventPack.reproductionA(parent, mutation);		
	}
	
	public static void runSimulation()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	{
		emilGenesis();
		//tester();
		while (start == false)
		{
			//do nothing
		}
		danceParty();
	}
	
	public static void tester()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException{
		genoStart();
		geneMutationTest();
		combatTest();
	}
	
	public static ArrayList<Organism> getLifeForms() {
		return lifeForms;
	}

	public static void setLifeForms(ArrayList<Organism> lifeForms) {
		Starter.lifeForms = lifeForms;
	}

	public static GridUniverse getGrid() {
		return grid;
	}

	public static void setGrid(GridUniverse grid) {
		Starter.grid = grid;
	}

	public static void genoStart() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException{
        //tests gene creation and sequencing
		WorldState.addLogEvent("Begin GeneSequence Manipulation Test....");
		GeneSequence aGeneSequence = new GeneSequence(); 
        Class aClass = aGeneSequence.getClass();
		Field[] geneSequenceFieldArray = aClass.getDeclaredFields(); //.getDeclaredFields(); 
		double[] genoArray = new double[geneSequenceFieldArray.length]; //automatic adjustment of array length based on number of field variables
		String[] genoArrayNames = new String[geneSequenceFieldArray.length]; //"------------"
		
		
		for (int i = 0; i < geneSequenceFieldArray.length; i++){
			Field f = geneSequenceFieldArray[i];
			//f.setAccessible(true);
			
			//f.set(aGeneSequence, 1.0);
			genoArray[i] = (double)geneSequenceFieldArray[i].get(aGeneSequence);
			genoArrayNames[i] = (String)geneSequenceFieldArray[i].getName();					
		}
		

		GeneToPhenotype Billy_Bob_Fuckface = new GeneToPhenotype(genoArray, genoArrayNames);	
	}
	
	public static void combatTest(){
		//tests combat parameters
		//--------------------------------------------------------------
		//             Fight Test
		//------------------------------------------------------------
		//Entity(name, hp, damage, speed, crit, avoidance, flightChance
		WorldState.addLogEvent("Begin Combat Test Simulation");
		BattleState bob = new BattleState(new Organism("Bob", 130, 20, 20, 0.1, 0.1, 0.2));
		BattleState rob = new BattleState(new Organism("Rob", 100, 30, 15, 0.1, 0.1, 0.5));
		Fight fight = new Fight(bob, rob, isDebug);
		if (isDebug) System.out.println("Begin Fight Simulation.");
		fight.fightSim();
		
		//--------------------------------------------------------------
		//             Battle Test
		//------------------------------------------------------------
		BattleState [] teamBob = new BattleState[bobSize];
		BattleState [] teamRob = new BattleState[robSize];
		
		for (int i =0; i < bobSize; i++)
		{
			teamBob[i] = new BattleState(new Organism("Bob", 130, 20, 20, 0.1, 0.1, 0.2));	
		}
		
		for (int i =0; i < robSize; i++)
		{
			teamRob[i] = new BattleState(new Organism("Rob", 100, 30, 15, 0.1, 1, 0.5));
		}
		
		Battle testBattle = new Battle(teamBob, teamRob, isDebug);
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

	private static void entityGenesis()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException{
		//creates lifeforms
		Organism e = new Organism();
		e.setGenes(EventPack.randomGS());
		lifeForms.add(e);
		lifeForms.get(0).setTheOrg(new OrganismGFX(grid, 0, 0));	
		
		for (int i = 1; i < WorldState.startOrgNum; i++)
		{
			e = new Organism();
			e.setGenes(EventPack.randomGS());
			lifeForms.add(e);
			int x = WorldState.rng1[1].rInt(WorldState.pLnum);
			int y = WorldState.rng1[2].rInt(WorldState.pWnum);
			lifeForms.get(i).setTheOrg(new OrganismGFX(grid, x, y));	
		}
	}
	
	private static void danceParty()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException{
		//moves the fake organisms around
		turn = 1;
		if (isDebug) WorldState.addLogEvent("Begin Movement Pattern Testing....");
		if (WorldState.useTurns){
			for (int i = 1; i <= WorldState.turns; i++)
			{//main loop
				if(end == true) break; //exit button pressed
				if(isDebug) System.out.println("Turn: " + i);
				//WorldState.addLogEvent("Turn: " + i);				
				
				for (int index = 0; index < lifeForms.size(); index++){
					lifeForms.get(index).move(i);
				}

				grid.repaint();
				
				if (reset == true) {
					//allow a ui button to reset the simulation
					start = false;
					reset = false;
					runSimulation();
					break;
				}
				
				while (pause == true)
				{//allow simulation to be paused
					if(end == true) break;
				}
				  try{
		               Thread.sleep(WorldState.sleepTime); //slowed the loop to approximately 30 fps
		                  }catch(Exception e) {}
				  turn++;
			}//end for loop
		}//end if-then
}//end danceParty
	
	private static void emilGenesis()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException{
		//Create the graphic window, and the world behind it

		grid = new GridUniverse(WorldState.pLnum, WorldState.pWnum);	
		JFrame frame = new JFrame("Planet Terra Nova");
		JFrame btnframe = new JFrame("Control Interface");
		//I've left some space on the left side of the grid for buttons
		JPanel btnPanel = btnSetup();
				
		//feel free to change the position of the grid within the frame
		frame.setSize(WorldState.winLength, WorldState.winWidth); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(grid); 
		//button panel
		btnframe.setSize(WorldState.bLength, WorldState.winWidth); 
		btnframe.setLocation(WorldState.winLength, 0);
		btnframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		btnframe.add(btnPanel); 
	      
		//frame.add(grid.getPauseButton());
		frame.setVisible(true);	
		btnframe.setVisible(true);
		int x;// = rng.nextInt(WorldState.pLnum);
		int y;// = rng.nextInt(WorldState.pWnum);
		
		lifeForms = new ArrayList<Organism>();
		entityGenesis();	
	}
	
	public static JPanel btnSetup(){
		JPanel btnPanel = new JPanel(new FlowLayout());
		ButtonHandler bh = new ButtonHandler();
		startButton = new JButton("Start");
		startButton.addActionListener(bh);
		pauseButton = new JButton("Pause");
		resetButton = new JButton("Reset");
		exitButton = new JButton("Exit");
		btnPanel.add(startButton);
		btnPanel.add(pauseButton);
		btnPanel.add(resetButton);
		btnPanel.add(exitButton);		
		
		return btnPanel;
	}
	
	public static int getTurn(){
		return turn;
	}

	public static boolean isStart() {
		return start;
	}

	public static void setStart(boolean start) {
		Starter.start = start;
	}

	public static boolean isReset() {
		return reset;
	}

	public static void setReset(boolean reset) {
		Starter.reset = reset;
	}

	public static boolean isPause() {
		return pause;
	}

	public static void setPause(boolean pause) {
		Starter.pause = pause;
	}

	public static void setTurn(int turn) {
		Starter.turn = turn;
	}

	public static JButton getStartButton() {
		return startButton;
	}

	public static void setStartButton(JButton startButton) {
		Starter.startButton = startButton;
	}

	public static JButton getExitButton() {
		return exitButton;
	}

	public static void setExitButton(JButton exitButton) {
		Starter.exitButton = exitButton;
	}

	public static JButton getResetButton() {
		return resetButton;
	}

	public static void setResetButton(JButton resetButton) {
		Starter.resetButton = resetButton;
	}

	public static JButton getPauseButton() {
		return pauseButton;
	}

	public static void setPauseButton(JButton pauseButton) {
		Starter.pauseButton = pauseButton;
	}
	
}
