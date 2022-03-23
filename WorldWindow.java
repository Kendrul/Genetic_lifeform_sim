import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

import java.util.Random;

public class WorldWindow extends JApplet{
	
	private Patch [][] worldGrid;
	private Random seeder;
	
	//----------------------------------------------------------

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

}