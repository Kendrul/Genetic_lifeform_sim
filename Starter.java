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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import old.ButtonHandler;



public class Starter{

	/**
	 * @param args
	 */
	private static boolean isDebug = WorldState.isDebug; //change this to false to disable output messages
	private static ArrayList<Organism> lifeForms;
	private static GridUniverse grid;
	private static int turn;
	private static StatPack stats;
	private static StatPack turnStats;
	private static int generation = 0;
	private static int simRun = 0;
	
	private static boolean start = true;
	private static boolean reset = false;
	private static boolean pause = false;
	private static boolean end = false;
	
    //BUTTONS
    private static JButton startButton;
    private static JButton exitButton;
    private static JButton resetButton;
    private static JButton pauseButton;
    private static JButton continueButton;
    
    private static JFrame frame;
     
    static boolean start2 = false;
    static boolean pause2 = false;
    static boolean start3 = false;
    private static Thread threadObject;
    private static FileOutput combiner;
    
    /////////
    private static JFrame frame2;
    private static DefaultTableModel model;
    private static DefaultTableModel model2;
    private static Organism anOrganism;   
	//ArrayList<Organism> lifeForms = this.getLifeForms();
	private static Double[] visuallyExpressedAveragePhenotype = new Double[14];
	private static Double[] initialvisuallyExpressedAveragePhenotype = new Double[14];
	private static GeneToPhenotype thisOrganismsPhenotype;
	
	public static void main(String[] args) {
		LocalTime start = LocalTime.now();
		//1) Create any resources needed (RNG's for example)
		//Create the world:
		//	2) Terrain
		//	3) Resources, and their placement
		//	4) Entities, and their placement
		//	5) Load gfx
		// 	6) Start Simulation
	
		//5)
		//worldWindow = terraGenesis();
		combiner = new FileOutput();
		stats = new StatPack();
		turnStats = new StatPack();
		WorldState.resetLogs();
		WorldState.addLogEvent("Starting Program....");
		System.out.println("Starting Program...");
		
		WorldState.establishRNG();
		try{
			emilGenesis();		
			while(start2 == false){
        		//note what happens when this is commented out (if used, it focuses thread)
        		//this mechanism (using a print statement) may allow us to control threading 
        		//priority and thus actualize total program control 
        		System.out.print(""); 
        		if(start2 == true){ 			
        			runSimulationTrials();
        			//WorldState.addLogEvent("Terminating Simulation at turn " + getTurn() + ".....");
        			//FileOutput out = new FileOutput();
        			//out.outputFiles();
        			break; 
        		}else if(start3 == true){
        			runSimulationTrials();
        			break; 
        		}
			}
		} catch (Exception e)
		{
			System.out.println("Exception Found: ");
			//System.out.println(e);
			e.printStackTrace();
		}

		//WorldState.addLogEvent("Terminating Simulation at turn " + getTurn() + ".....");
		//FileOutput out = new FileOutput();
		//out.outputFiles();
		
		if (WorldState.trials > 1 ) 
			{
				System.out.println("------------------------------------------");	
				System.out.println("Writing Output for Combined Stat File......");
				combiner.setFinalRunFlag(true);
				combiner.outputFiles();
			}
		System.out.println("Terminating Program at turn " + getTurn() + ".....");
		System.out.println("Start Time: " + start + ", End Time: " + LocalTime.now());
	}
	
	public static void runSimulationTrials()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	{//for running multiple simulation runs in serial
		for(int i =0; i < WorldState.trials; i++){
			simRun = i;
			if(i!=0) entityGenesis();
			danceParty();
			WorldState.addLogEvent("Terminating Simulation " + i+ " at turn " + getTurn() + ".....");
			System.out.println("Terminating Simulation " + i+ " at turn " + getTurn() + ".....");
			stats.update(WorldState.nameVault.size());
			WorldState.resetTurn();
			FileOutput out = new FileOutput();
			String [] stF = WorldState.statFile.split(".");
			String [] logF = WorldState.logFile.split(".");
			String [] orgF = WorldState.orgFile.split(".");		
			String [] finF = WorldState.finalStatFile.split(".");
			
			if (stF.length ==2 && logF.length ==2 && orgF.length ==2 && finF.length == 2) out.outputFiles(stF[0] + i + "." + stF[1], logF[0] + i + "." + logF[1], orgF[0] + i + "." + orgF[1], finF[0] + i + "." + finF[1]);
			else out.outputFiles("colonialCognitionGenStats" + i + ".csv","colonialCognitionLog" + i + ".txt", "colonialCognitionOrganismData" + i +".csv", "colonialCognitionFinalStats" + i + ".csv");
			
			if (WorldState.trials > 1 ) 
			{//if running multiple back-to-back sims, save all the data
				combiner.setGenerationVault(WorldState.generationVault);
				combiner.setGeneVault(WorldState.geneVault);
				combiner.setTotalVault(stats);
				combiner.setTurnVault(WorldState.turnVault);
			}
			
			if (i + 1 < WorldState.trials) reset(i+1);
		}
	}
	
	public static void reset(int run){

		stats = new StatPack();
		turnStats = new StatPack();
		WorldState.resetLogs();
		WorldState.addLogEvent("Starting Simulation " + run);
		System.out.println("Starting Simulation " + run);
		grid.resetUniverse(WorldState.pLnum, WorldState.pWnum);
		grid.repaint();
	}
	
	public static void runSimulation()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	{
		
		entityGenesis();
		danceParty();
	}
	
	private static void entityGenesis()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException{
		//creates lifeforms
		lifeForms = new ArrayList<Organism>();
		Organism e = new Organism();
		e.setGenes(EventPack.randomGS());
		lifeForms.add(e);
		WorldState.addGenerationVault(e.getInfo());
		lifeForms.get(0).setTheOrg(new OrganismGFX(grid, 0, 0));

		
		for (int i = 1; i < WorldState.startOrgNum; i++)
		{
			e = new Organism();
			e.setGenes(EventPack.randomGS());
			lifeForms.add(e);
			WorldState.addGenerationVault(e.getInfo());
			e.getInfo().cycle += "g";
			int x = WorldState.rng1[1].rInt(WorldState.pLnum);
			int y = WorldState.rng1[2].rInt(WorldState.pWnum);
			lifeForms.get(i).setTheOrg(new OrganismGFX(grid, x, y));	
		}
	}
	
	private static void danceParty()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException{
		//moves the fake organisms around
		turn = 1;
		setFitnessRules();
		//if (isDebug) WorldState.addLogEvent("Begin Movement Pattern Testing....");
		if (WorldState.useTurns){
			boolean forceReproduction = false;
			for (int i = 1; i <= WorldState.turns; i++)
			{//main loop
				if((lifeForms.size() <= 0) /*|| (end == true)*/) break; //all organisms have died, end simulation
				if((WorldState.forcedReproductionEvent > 0) && ((i % WorldState.forcedReproductionEvent) == 0) && (i != WorldState.turns))
					{
						forceReproduction = true;
						generation++;
					}
				else forceReproduction = false;

				if(isDebug) System.out.println("Turn: " + i);
				//WorldState.addLogEvent("Turn: " + i);				
				
				for (int index = 0; index < lifeForms.size(); index++){
					//reset's the action points of the organisms
					lifeForms.get(index).resetAp();
					if (forceReproduction == true) {
						//lifeForms.get(index).getRFitness();
						Collections.sort(lifeForms);					
					}
				}
				/*
				//CULL THE WEAK, THE HARVEST OF SOULS HAS BEGUN
				//THE SOULS TASTE LIKE CHICKEN
				int newSize = 0;
				if ((forceReproduction == true) && (lifeForms.size() > 20)){
					newSize = (int) (lifeForms.size() * 0.50);
					System.out.println("Start Culling");
					while (lifeForms.size() != newSize)
					{
						cull(newSize); //cull every organism beyond the top 25%
					}
					System.out.println("End Culling");
				}
								for (int index = 0; index < lifeForms.size(); index++){
					if ((forceReproduction == true) && (index < ((int) (newSize * 0.40)))) lifeForms.get(index).forcedReproduction();
					else lifeForms.get(index).move(i);					
				}*/
				
				if(forceReproduction == true){
					forcedReproductionEvent();
				}//end forcedReproduction
				else{//actions do not occur on reproduction turn
					for (int index = 0; index < lifeForms.size(); index++){
						lifeForms.get(index).move(i);
					}
				}

				frame.setTitle("Planet Terra Nova: Turn " + turn + " / " + WorldState.turns);
				
				
				////////////
				//if(getTurn() > 0){
	        		//*****determine average values
	        		
	        		lifeForms = Starter.getLifeForms();
	        		//we express 5 phenotype features visually
	        		
	        		//for(int i1 = 0; i1 < 14; i1++){
	        		//	visuallyExpressedAveragePhenotype[i1] = 0.0; //initialize to zero
	        		//}
	        		
	        		for(int i1 = 0; i1 < lifeForms.size(); i1++){
	        		anOrganism = lifeForms.get(i1);
	        		//System.out.println(anOrganism);
	        		//Organism thisOrganism = patch.getTheE();
	        		thisOrganismsPhenotype = anOrganism.getPheno();
	        		if(visuallyExpressedAveragePhenotype[0] != null){
	        			visuallyExpressedAveragePhenotype[0] += thisOrganismsPhenotype.limbLength;
	        		} else {visuallyExpressedAveragePhenotype[0] = 0.0;}
	        		if(visuallyExpressedAveragePhenotype[1] != null){
	        			visuallyExpressedAveragePhenotype[1] += thisOrganismsPhenotype.limbStructuralStrength;
	        		} else {visuallyExpressedAveragePhenotype[1] = 0.0;}
	        		if(visuallyExpressedAveragePhenotype[2] != null){
	        			visuallyExpressedAveragePhenotype[2] += thisOrganismsPhenotype.muscleMass;
	        		} else {visuallyExpressedAveragePhenotype[2] = 0.0;}
	        		if(visuallyExpressedAveragePhenotype[3] != null){
	        			visuallyExpressedAveragePhenotype[3] += thisOrganismsPhenotype.muscleSpeed;
	        		} else {visuallyExpressedAveragePhenotype[3] = 0.0;}
	        		if(visuallyExpressedAveragePhenotype[4] != null){
	        			visuallyExpressedAveragePhenotype[4] += thisOrganismsPhenotype.muscleEndurance;
	        		} else {visuallyExpressedAveragePhenotype[4] = 0.0;}
	        		if(visuallyExpressedAveragePhenotype[5] != null){
	        			visuallyExpressedAveragePhenotype[5] += thisOrganismsPhenotype.muscleStrength;
	        		} else {visuallyExpressedAveragePhenotype[5] = 0.0;}
	        		if(visuallyExpressedAveragePhenotype[6] != null){
	        			visuallyExpressedAveragePhenotype[6] += thisOrganismsPhenotype.neuralMass;
	        		} else {visuallyExpressedAveragePhenotype[6] = 0.0;}
	        		if(visuallyExpressedAveragePhenotype[7] != null){
	        			visuallyExpressedAveragePhenotype[7] += thisOrganismsPhenotype.hostility;
	        		} else {visuallyExpressedAveragePhenotype[7] = 0.0;}
	        		if(visuallyExpressedAveragePhenotype[8] != null){
	        			visuallyExpressedAveragePhenotype[8] += thisOrganismsPhenotype.agreeability;
	        		} else {visuallyExpressedAveragePhenotype[8] = 0.0;}
	        		if(visuallyExpressedAveragePhenotype[9] != null){
	        			visuallyExpressedAveragePhenotype[9] += thisOrganismsPhenotype.moodPositive;
	        		} else {visuallyExpressedAveragePhenotype[9] = 0.0;}
	        		if(visuallyExpressedAveragePhenotype[10] != null){
	        			visuallyExpressedAveragePhenotype[10] += thisOrganismsPhenotype.weight;
	        		} else {visuallyExpressedAveragePhenotype[10] = 0.0;}
	        		
	        		//get facial characteristics
	        		if(visuallyExpressedAveragePhenotype[11] != null){
	        			visuallyExpressedAveragePhenotype[11] += thisOrganismsPhenotype.eyeSize;
	        		} else {visuallyExpressedAveragePhenotype[11] = 0.0;}
	        		if(visuallyExpressedAveragePhenotype[12] != null){
	        			visuallyExpressedAveragePhenotype[12] += thisOrganismsPhenotype.facialLength;
	        		} else {visuallyExpressedAveragePhenotype[12] = 0.0;}
	        		if(visuallyExpressedAveragePhenotype[13] != null){
	        			visuallyExpressedAveragePhenotype[13] += thisOrganismsPhenotype.facialWidth;
	        		} else {visuallyExpressedAveragePhenotype[13] = 0.0;}
	        		
	        		
	        		//visuallyExpressedAveragePhenotype[5] += thisOrganismsPhenotype.generalPhenotype;
	        		}
	        		
	        		
	        		//average out the phenotype values in each index
	        		for(int i1 = 0; i1 < visuallyExpressedAveragePhenotype.length; i1++){
	        			visuallyExpressedAveragePhenotype[i1] /= lifeForms.size();
	        		}
	        			if(turn == 1){
	        				initialvisuallyExpressedAveragePhenotype[0] = visuallyExpressedAveragePhenotype[0];
	        				initialvisuallyExpressedAveragePhenotype[1] = visuallyExpressedAveragePhenotype[1];
	        				initialvisuallyExpressedAveragePhenotype[2] = visuallyExpressedAveragePhenotype[2];
	        				initialvisuallyExpressedAveragePhenotype[3] = visuallyExpressedAveragePhenotype[3];
	        				initialvisuallyExpressedAveragePhenotype[4] = visuallyExpressedAveragePhenotype[4];
	        				initialvisuallyExpressedAveragePhenotype[5] = visuallyExpressedAveragePhenotype[5];
	        				initialvisuallyExpressedAveragePhenotype[6] = visuallyExpressedAveragePhenotype[6];
	            		    //initialperceptionRadius = visuallyExpressedAveragePhenotype[7]; 
	        				initialvisuallyExpressedAveragePhenotype[7] = visuallyExpressedAveragePhenotype[7]; 
	        				initialvisuallyExpressedAveragePhenotype[8] = visuallyExpressedAveragePhenotype[8]; 
	        				initialvisuallyExpressedAveragePhenotype[9] = visuallyExpressedAveragePhenotype[9];
	        				initialvisuallyExpressedAveragePhenotype[10] = visuallyExpressedAveragePhenotype[10];
	            		    //initialcolonialCognitionCapacity = visuallyExpressedAveragePhenotype[0];
	        				initialvisuallyExpressedAveragePhenotype[11] = visuallyExpressedAveragePhenotype[11];
	            			
	            			
	            			
	        				initialvisuallyExpressedAveragePhenotype[12] = visuallyExpressedAveragePhenotype[12];
	        				initialvisuallyExpressedAveragePhenotype[13] = visuallyExpressedAveragePhenotype[13];
	        				//initialvisuallyExpressedAveragePhenotype[14] = visuallyExpressedAveragePhenotype[14]; 
	            		    //initialcranialSize = visuallyExpressedAveragePhenotype[0]; 
	            			
	            		    //initialosteocyteGenesisEfficiency = visuallyExpressedAveragePhenotype[0];
	            		    //initialmyocyteGenesisEfficiency = visuallyExpressedAveragePhenotype[0];
	            		    //initialneurocyteGenesisEfficiency = visuallyExpressedAveragePhenotype[0];
	            		}
	        		
	        		//*****
	        		
	        		
	        		
	        		//dynamic setting of phenotype variable values
	        		
	        		if(getTurn() == 1){
	        		for(int j = 0; j < 14; j++){
	        			model2.setValueAt(initialvisuallyExpressedAveragePhenotype[j], j, 1);
	        			//model2.setValueAt("hahaha", j, 1);
	        		}
	        		}
	        		
	        		if(getTurn() > 0){
	            		for(int j1 = 0; j1 < 14; j1++){
	            			model2.setValueAt(visuallyExpressedAveragePhenotype[j1], j1, 3);
	            			
	            			
	            			if(initialvisuallyExpressedAveragePhenotype[j1] < visuallyExpressedAveragePhenotype[j1]){
	            				model2.setValueAt("<", j1, 2);
	            			}
	            			else if(initialvisuallyExpressedAveragePhenotype[j1] > visuallyExpressedAveragePhenotype[j1]){
	            				model2.setValueAt(">", j1, 2);
	            			}
	            			else{
	            				model2.setValueAt("=", j1, 2);
	            			}
	            			//model2.setValueAt("hahaha", j, 1);
	            		}
	        		
	        		}
	        			
	        		//}
				////////////
				
				grid.repaint();
				/*
				if (reset == true) {
					//allow a ui button to reset the simulation
					start = false;
					reset = false;
					runSimulation();
					break;
				}*
				
				while (pause == true)
				{//allow simulation to be paused
					if(end == true) break;
				}*/
				  try{
		               Thread.sleep(WorldState.sleepTime); //slowed the loop to approximately 30 fps
		                  }catch(Exception e) {}
				  turn++;
			}//end for loop
		}//end if-then
		
		for(int i = 0; i < lifeForms.size(); i++)
		{
			lifeForms.get(i).getInfo().age = Integer.toString(lifeForms.get(i).getAge());
			//WorldState.addGenerationVault(lifeForms.get(i).getInfo());
		}
}//end danceParty
	
	private static void emilGenesis()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException{
		//Create the graphic window, and the world behind it

		grid = new GridUniverse(WorldState.pLnum, WorldState.pWnum);	
		frame = new JFrame("Planet Terra Nova: Turn " + 0 + " / " + WorldState.turns);
		//frame.setFont(new Font("CourierNew", Font.BOLD, 12));
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
	      
		frame.setVisible(true);			
		btnframe.setVisible(true);
		
		//////lifeForms = new ArrayList<Organism>();
        entityGenesis();    
        
        
		//////////////////////////////////////////////////////
		frame2 = new JFrame();
		
		frame2.setLayout(new BorderLayout());
		
		model = new DefaultTableModel(14, 4);
		final JTable table = new JTable(model);
		
		//JPanel btnPnl = new JPanel(new BorderLayout());
		//JPanel topBtnPnl = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		//JPanel bottombtnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		//topBtnPnl.add(new JButton("Select All"));
		//bottombtnPnl.add(new JButton("Cancel"));
		//bottombtnPnl.add(new JButton("Add Selected"));
		
		//btnPnl.add(topBtnPnl, BorderLayout.NORTH);
		//btnPnl.add(bottombtnPnl, BorderLayout.CENTER);
		
		table.getTableHeader().setReorderingAllowed(false);
		
		
		
		frame2.add(table.getTableHeader(), BorderLayout.NORTH);
		frame2.add(table, BorderLayout.CENTER);
		//frame2.add(btnPnl, BorderLayout.SOUTH);
		
		frame2.setTitle("Phenotype Feature Changes: (Column B = Initial, Column D = Current)");
		
		////
		//table.set
		model2 = (DefaultTableModel)table.getModel();
		
		/*limbLength = getLimbLength();
		limbStructuralStrength = getLimbStructuralStrength();
		muscleMass = getMuscleMass();
		muscleSpeed = getMuscleSpeed();
		muscleEndurance = getMuscleEndurance(); 
		muscleStrength = getMuscleStrength();
		neuralMass = getNeuralMass();
		perceptionRadius = getPerceptionRadius(); 
		hostility = getHostility(); 
		agreeability = getAgreeability(); 
		moodPositive = getMood();
		fightResponse = getFightResponse(); 
		colonialCognitionCapacity = getColonialCognitionCapacity(); 
		weight = weight();
		
		
		
		eyeSize = getEyeSize();
		facialLength = getFacialLength();
		facialWidth = getFacialWidth(); 
		cranialSize = getCranialSize(); 
		
		osteocyteGenesisEfficiency = getOsteocyteGenesisEfficiency();
		myocyteGenesisEfficiency = getMyocyteGenesisEfficiency();
		neurocyteGenesisEfficiency = getNeurocyteGenesisEfficiency();
		
		generalPhenotype = getGeneralPhenotype();*/
		//bone values
		model2.setValueAt("Limb Length",0,0);
		model2.setValueAt("Limb Structural Strength",1,0);
		
		//muscle values
		model2.setValueAt("Muscle Mass",2,0);
		model2.setValueAt("Muscle Speed",3,0);
		model2.setValueAt("Muscle Endurance",4,0);
		model2.setValueAt("Muscle Strength",5,0);
		
		//neural values
		model2.setValueAt("Neural Mass",6,0);
		model2.setValueAt("Hostility",7,0);
		model2.setValueAt("Agreeability",8,0);
		model2.setValueAt("Positive Mood",9,0);
		
		//organism's weight
		model2.setValueAt("Weight",10,0);
		
		//phenotype features used for identification
		model2.setValueAt("Eye Size",11,0);
		model2.setValueAt("Facial Length",12,0);
		model2.setValueAt("Facial Width",13,0);
		
		
		
		
		
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.pack();
		frame2.setVisible(true);
			
	}
	
    public static JPanel btnSetup(){
        JPanel btnPanel = new JPanel(new FlowLayout());
        //ButtonHandler bh = new ButtonHandler();
        startButton = new JButton("Start");
        //startButton.addActionListener(bh);
        pauseButton = new JButton("Pause");
        continueButton = new JButton("Continue");
        resetButton = new JButton("Reset");
        exitButton = new JButton("Exit");
        btnPanel.add(startButton);
        btnPanel.add(pauseButton);
        btnPanel.add(continueButton);
        btnPanel.add(resetButton);
        btnPanel.add(exitButton);    
        
        //JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent ae) {
                startButton.setText("running");
                start2 = true; 
                //stopButton.setEnabled(true);
             }
           }
         );
        
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
               exitButton.setText("miau");
               System.exit(0); 
               //stopButton.setEnabled(true);
            }
          }
        );
        
        //pauses image for 5 seconds 
        //must use concurrency control to pause everything
        pauseButton.addActionListener(new ActionListener() {
            @SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent ae) {
            	Thread t = Thread.currentThread();
            	t.setName("Master Thread");
               try {
            	       t.setPriority(1);
            	       t.currentThread().sleep(5000); 	   	
               } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Thread.currentThread().interrupt();
				e.printStackTrace();
               }
            }
          }
        );
          
         
        return btnPanel;
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
	
	public static int getTurn(){
		return turn;
	}
	
	public synchronized static void entityDeath(Organism o, String reason)
	{
		o.getInfo().deathTurn = Integer.toString(getTurn());
		o.getInfo().deathReason = reason;
		o.getInfo().calcAge();
		//WorldState.addGenerationVault(o.getInfo());
		o.getInfo().cycle += "d";
		entityDeath(o);
	}
	
	public synchronized static void entityDeath(Organism o){
		
		lifeForms.remove(o);
		grid.getPatchGrid()[o.getPoint().x][o.getPoint().y].removeE(o);
		ArrayList<Coupling> p = o.getPairs();
		
		/*
		while (p.size() > 0){
			p.get(0).getOtherOrg(o).removeCouple(p.get(0));
			p.remove(0);
		}
		*/
		
	}
	
	private static void cull(int i)
{
	//System.out.println("DEATH COMES FOR US ALL");
	if (lifeForms.size() > 0) {
		WorldState.addLogEvent("[Turn: " + turn+"] " + lifeForms.get(i).getName() + " has been culled by an act of the Creator.");
		stats.incCullDeath(1);
		turnStats.incCullDeath(1);
		if (lifeForms.size() > 0) entityDeath(lifeForms.get(i), "Cull");
	}
}
	
	private static void cull(ArrayList<Organism> newList)
{
	//System.out.println("DEATH COMES FOR US ALL");
	if (newList.size() > 0) {
		WorldState.addLogEvent("[Turn: " + turn+"] " + newList.get(0).getName() + " has been culled by an act of the Creator.");
		stats.incCullDeath(1);
		turnStats.incCullDeath(1);
		if (newList.size() > 0) entityDeath(newList.get(0), "Cull");
		newList.remove(0);
	}
}
	
	public synchronized static StatPack getStats(){
		return stats;
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
	 
	 
	    public static JButton getPauseButton() {
	        return pauseButton;
	    }
	 
	    public static void setPauseButton(JButton pauseButton) {
	        Starter.pauseButton = pauseButton;
	    }
	
	    public static boolean isStart() {
	        return start;
	    }
	 
	    public static void setStart(boolean start) {
	        Starter.start = start;
	    }
	 
	 
	    public static boolean isPause() {
	        return pause;
	    }
	 
	    public static void setPause(boolean pause) {
	        Starter.pause = pause;
	    }
	    
	    public static int getGeneration(){
	    	return generation;
	    }
	    
	    
	    
	    /**
		 * @return the simRun
		 */
		public static int getSimRun() {
			return simRun;
		}

		/**
		 * @return the turnStats
		 */
		public static StatPack getTurnStats() {
			return turnStats;
		}

		/**
		 * @param turnStats the turnStats to set
		 */
		public static void setTurnStats(StatPack turnStats) {
			Starter.turnStats = turnStats;
		}

		public static synchronized void forcedReproductionEvent() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	    {//handles the forced Reproduction event segments
	    	int count = 0;
			for (int index = 0; index < lifeForms.size(); index++){
				//calculate average rFitness
				WorldState.avgFC += lifeForms.get(index).potentialEnergy();
				WorldState.avgFE += lifeForms.get(index).getFoodEaten();
				WorldState.avgFS += lifeForms.get(index).getFoodShared();
				WorldState.avgFT += lifeForms.get(index).getFoodStolen();
				WorldState.avgBN += lifeForms.get(index).getFightNum();
				WorldState.avgBW += lifeForms.get(index).getFightWon();
				WorldState.avgA += lifeForms.get(index).getPheno().getAgreeability();
				count++;
			}
			
			//get the averages
			WorldState.avgFC /= count;
			WorldState.avgFE /= count;
			WorldState.avgFS /= count;
			WorldState.avgFT /= count;
			WorldState.avgBN /= count;
			WorldState.avgBW /= count;
			WorldState.avgA /= count;
			
			//sort!
			for (int index = 0; index < lifeForms.size(); index++){
				//reset's the action points of the organisms
					Collections.sort(lifeForms);					
			}
	    	WorldState.resetTurn();
	    	double [] rules = reproRuleSelection();
			ArrayList<Organism> newList1 = new ArrayList<Organism>(), newList2 = new ArrayList<Organism>();
			int newSize = (int) (WorldState.startOrgNum * rules[0]);
			if (newSize > lifeForms.size()) newSize = lifeForms.size();
			
			
			for (int j =0; j < lifeForms.size(); j++)
			{
				if (j < newSize) newList1.add(lifeForms.get(j));
				else newList2.add(lifeForms.get(j));
			}
			
			while (newList2.size() > 0){
				//CULL THE WEAK, THE HARVEST OF SOULS HAS BEGUN
				//THE SOULS TASTE LIKE CHICKEN
				cull(newList2); 			
			}
			
			lifeForms.clear();
			
			for(int j =0; j < newList1.size(); j++)
			{
				newList1.get(j).forcedReproduction(newList1.size(),reproRuleSelection());
				lifeForms.add(newList1.get(j));
				newList1.get(j).resetFitness();
				//newList1.get(j).getInfo().cycle += "r";
			}
			
			newSize = WorldState.startOrgNum - lifeForms.size();
			for(int j =0; j < newSize; j++){
				Organism e = new Organism();
				e.setGenes(EventPack.randomGS());
				lifeForms.add(e);
				int x = WorldState.rng1[1].rInt(WorldState.pLnum);
				int y = WorldState.rng1[2].rInt(WorldState.pWnum);
				e.setTheOrg(new OrganismGFX(grid, x, y));
				WorldState.addGenerationVault(e.getInfo());
				e.getInfo().cycle += "g";
			}
			for (int ix = 0; ix < WorldState.pLnum; ix++)
			{	for (int jy =0; jy < WorldState.pWnum; jy++)
				{
					 Patch patch = grid.getPatch(ix, jy);
					if (patch.getTheR() != null)
					{//reset the resources
						patch.getTheR().replenish2();
					}	
				}
			}
			
	    }//end forcedReproductionEvent
	    
	    private static double [] reproRuleSelection(){
	    	//choose reproduction rule to apply
	    	//How do we create our nextGen pop
	    	//[0] = % reproduction candidates, [1] = % of born, [2] = % of random gen pops
	    	//double [] rule = {0.1, 0.7, 0.2}; //172 rule
	    	double [] rule = WorldState.reproductiveRuleSet;
	    	return rule;
	    }
	    
	    private static void setFitnessRules(){
	    	WorldState.rRuleSet = WorldState.useThisRuleSet;
	    	//WorldState.rRuleSet[0] = 0; //food carried
	    	//WorldState.rRuleSet[1] = 0; //food eaten
	    	//WorldState.rRuleSet[2] = 0; //food shared
	    	//WorldState.rRuleSet[3] = 0; //food stolen
	    	//WorldState.rRuleSet[4] = 2; //number of fights
	    	//WorldState.rRuleSet[5] = 0; //fights won
	    	//WorldState.rRuleSet[6] = 0; //agreeability
	    }
}
