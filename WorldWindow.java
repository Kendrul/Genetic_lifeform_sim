import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

import java.util.Random;

public class WorldWindow extends JApplet{
	
	private Patch [][] worldGrid;
	private Random seeder;
	
	//----------------------------------------------------------
   final static Color fg = Color.black;
   final static Color bg = Color.white;

	//----------------------------------------------------------
    public WorldWindow(){}
    
	public WorldWindow(int x, int y, Random planter){
		seeder = planter;
		worldGrid = new Patch [x][y];
		
		for (int ix = 0; ix < x; ix++)
		{
			for (int jy = 0; jy < y; jy++){
				worldGrid[ix][jy] = new Patch(ix*WorldState.pLength, jy*WorldState.pWidth, seeder.nextInt(WorldState.terrainTypes.length));
			}
		}
		//terraGenesis();
	}

    public void init() {
        //Initialize drawing colors
        setBackground(bg);
        setForeground(fg);
    }
	
	public void update(){
		
	}
	
	private void terraGenesis(){
	    JFrame f = new JFrame("Planet Terra Nova"); //change this to change frame window title
	    f.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {System.exit(0);}
	    });
	    f.getContentPane().add("Center", this);
	    init();//TODO implement in worldwindow class
	    f.pack();
	    f.setSize(new Dimension(1000,1000)); //change this to change the size of the window
	    f.setVisible(true);
	}
}
