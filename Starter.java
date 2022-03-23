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


public class Starter{

	/**
	 * @param args
	 */
	private static boolean isDebug = WorldState.isDebug; //change this to false to disable output messages
	private static ArrayList<Organism> lifeForms;
	private static GridUniverse grid;
	private static int turn;
	private static StatPack stats;
	private static int generation = 0;
	
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
     
    static boolean start2 = false;
    static boolean pause2 = false;
    private static Thread threadObject;
	
	
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
		
		stats = new StatPack();
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
        			runSimulation();
        			break; 
        		}
			}
		} catch (Exception e)
		{
			System.out.println("Exception Found: ");
			//System.out.println(e);
			e.printStackTrace();
		}

		WorldState.addLogEvent("Terminating Simulation at turn " + getTurn() + ".....");
		FileOutput out = new FileOutput();
		out.outputFiles();
		System.out.println("Terminating Program at turn " + getTurn() + ".....");
		System.out.println("Start Time: " + start + ", End Time: " + LocalTime.now());
		
	}
	
	
	
	public static void runSimulation()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	{
		
		entityGenesis();
		danceParty();
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
		//if (isDebug) WorldState.addLogEvent("Begin Movement Pattern Testing....");
		if (WorldState.useTurns){
			boolean forceReproduction = false;
			for (int i = 1; i <= WorldState.turns; i++)
			{//main loop
				if((lifeForms.size() == 0) /*|| (end == true)*/) break; //all organisms have died, end simulation
				if((WorldState.forcedReproductionEvent > 0) && ((i % WorldState.forcedReproductionEvent) == 0))
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
						Collections.sort(lifeForms);					
					}
				}
				
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
				
				for (int ix = 0; ix < WorldState.pLength; ix++)
				{	for (int jy =0; jy < WorldState.pWidth; jy++)
					{
						 Patch patch = grid.getPatch(ix, jy);
						if (patch.isHasResource() && patch.getTheR().getResourceType() == WorldState.resourceType[0])
						{
							double roll = WorldState.rng0[2].rDouble();
							if (roll < WorldState.rReplenishChance[patch.getType()][patch.getTheR().getResourceNum()])
								patch.getTheR().replenish();
						}
					}
				}
				
				for (int index = 0; index < lifeForms.size(); index++){
					if ((forceReproduction == true) && (index < ((int) (newSize * 0.40)))) lifeForms.get(index).forcedReproduction();
					else lifeForms.get(index).move(i);					
				}

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
	      
		frame.setVisible(true);			
		btnframe.setVisible(true);
		lifeForms = new ArrayList<Organism>();	
	}
	
    public static JPanel btnSetup(){
        JPanel btnPanel = new JPanel(new FlowLayout());
        ButtonHandler bh = new ButtonHandler();
        startButton = new JButton("Start");
        startButton.addActionListener(bh);
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
                startButton.setText("haha");
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
	
	public synchronized static void entityDeath(Organism o){
		lifeForms.remove(o);
		grid.getPatchGrid()[o.getPoint().x][o.getPoint().y].removeE(o);
	}
	
	private static void cull(int i)
{
	//System.out.println("DEATH COMES FOR US ALL");
	if (lifeForms.size() > 0) {
		WorldState.addLogEvent("[Turn: " + turn+"] " + lifeForms.get(i).getName() + " has been culled by an act of the Creator.");
		stats.incCullDeath(1);
		if (lifeForms.size() > 0) entityDeath(lifeForms.get(i));
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
	    
	    public static int getGeneration(){
	    	return generation;
	    }
	 
}
