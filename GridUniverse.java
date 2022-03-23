/* GridUniverse.java
 * CPSC 565 W2016: Project
 * Emil Emilov-Dulguerov and Jason Schneider
 * This class contains methods to draw and redraw our world window, and it's UI components
 * 
 */import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;


//first there was light, then there was a grid
@SuppressWarnings("serial")
public class GridUniverse extends JPanel{
	
	int formerCellX; //used to erase organism's former location (x,y) block color
	int formerCellY;
	int cellX; //current location (x,y) painting 
	int cellY;
	private List<Point> fillCells;
	private boolean isDebug = WorldState.isDebug;
	
	//Size Constants from WorldState
	private int length = WorldState.pLength;
	private int width = WorldState.pWidth;
	private int gridLength = WorldState.wLength;
	private int gridWidth = WorldState.wWidth;
	private int patchXnum = WorldState.pLnum;
	private int patchYnum = WorldState.pWnum;
	
	//Colors
	private Color orgColor = Color.red;
	private Color orgColor2 = Color.pink;
	private Color orgColor3 = Color.black;
	private Color orgColor4 = Color.white; 
	private Color orgColor5 = Color.GRAY;
	
	//Colors used to ID organisms "tribe" if you will (biologically tied to eye size); thus organisms
	//actually look at one another's eye sizes to determine similarity between one another
	private Color colorIDString = new Color(255, 0, 0); 
	//private Color colorPhenotypeID2 = new Color(0, 64, 0);
	//private Color colorPhenotypeID3 = new Color(0, 0, 64);
	//private Color colorPhenotypeID4 = new Color(64, 0, 64);
	private int numberPhenotypeID1 = 1;
	private int numberPhenotypeID2 = 2;
	private int numberPhenotypeID3 = 3;
	private int numberPhenotypeID4 = 4;

	private Color gridColor = Color.black;
	private Color textColor = Color.black;
	private Color borderColor = Color.blue;
	private Color resColor = Color.yellow;
	
	//Other Stuff
	private Patch [][] patchGrid;
	
	
	public GridUniverse(){
		fillCells = new ArrayList<>();
	}
	
	public GridUniverse(int x, int y){
		fillCells = new ArrayList<>();
		patchGrid = new Patch [x][y];
		
		for (int ix = 0; ix < x; ix++)
		{
			for (int jy = 0; jy < y; jy++){
				double tSeed = WorldState.rng0[0].rDouble();
				double cumulative = 0;
				int seed = 0;
				
				for (int i = 0; i < WorldState.terrainProb.length; i++)
				{
					if(tSeed < (WorldState.terrainProb[i] + cumulative))
					{
						seed = i;
						break;
					}else
					{
						cumulative += WorldState.terrainProb[i];
					}
				}
				
				patchGrid[ix][jy] = new Patch(ix*WorldState.pLength, jy*WorldState.pWidth, seed);
				patchGrid[ix][jy].spawnResource(WorldState.rng0[2].rDouble());
				//if(isDebug) System.out.println("Patch(" + ix + "," + jy + ") color: " + patchGrid[ix][jy].getType() + ", seed: " + seed);
			}
		}
		WorldState.addLogEvent("The World has been created.");
	}
	
	public void resetUniverse(int x, int y)
	{
		fillCells = new ArrayList<>();
		patchGrid = new Patch [x][y];
		
		for (int ix = 0; ix < x; ix++)
		{
			for (int jy = 0; jy < y; jy++){
				double tSeed = WorldState.rng0[0].rDouble();
				double cumulative = 0;
				int seed = 0;
				
				for (int i = 0; i < WorldState.terrainProb.length; i++)
				{
					if(tSeed < (WorldState.terrainProb[i] + cumulative))
					{
						seed = i;
						break;
					}else
					{
						cumulative += WorldState.terrainProb[i];
					}
				}
				
				patchGrid[ix][jy] = new Patch(ix*WorldState.pLength, jy*WorldState.pWidth, seed);
				patchGrid[ix][jy].spawnResource(WorldState.rng0[2].rDouble());
				//if(isDebug) System.out.println("Patch(" + ix + "," + jy + ") color: " + patchGrid[ix][jy].getType() + ", seed: " + seed);
			}
		}
		WorldState.addLogEvent("The World has been created.");
	}
		
	//speaks for itself
	public void setFormerPosition(int x, int y){
		formerCellX = length + (x * length);
		formerCellY = width + (y * width); 
	}
	
	//it's a living work of art
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		updateUni(g);
/*		g.setColor(gridColor); //blue grid lines
		
		//grid lines - horizontal
		for(int i = length; i <= gridLength; i += length){
			g.drawLine(i, length, i, gridWidth + length);
		}
		
		//grid lines - vertical
		for(int i = width; i <= gridWidth; i += width){
			g.drawLine(width, i, gridLength + width, i);
		}*/		
		
		g.setColor(borderColor); //blue grid lines
		g.drawRect(length, width, gridLength, gridWidth); //50 x 80 grid
	}
	
	//don't be jelly that I'm the chosen cell filler, bro
	public void fillCell(int x, int y, OrganismGFX o){
		//if (o != null) setFormerPosition(o.formerCellX, o.formerCellY); //jason
			fillCells.add(new Point(x, y));
		//display();
	}
	
	
	private void display(){
		System.out.print("New Points:");
		for(int i = 0; i < fillCells.size(); i++){
			System.out.print(" (" + fillCells.get(i).x + "," + fillCells.get(i).y + "),");
		}
		System.out.println();
	}
	
	private void empty(Graphics g)
	{
		Patch patch;
		Point emptyCell;
		
		for(int i =0; i < patchXnum; i++)
		{for (int j = 0; j < patchYnum; j++)
		{
			cellX = length + (i * length);
			cellY = width + (j * width); 
			patch = patchGrid[i][j];
			//System.out.println("Patch(" + i + "," + j + ") color: " + patchGrid[i][j].getColor());
			g.setColor(patch.getColor()); 
			g.fillRect(cellX, cellY, length, width);
		}
		}
	}
	
	private void fill(Graphics g)
	{
		Patch patch;
		Point fillCell;
		
		while (fillCells.size() > 0)
		{
			fillCell = fillCells.remove(0);
			cellX = length + (fillCell.x * length);
			cellY = width + (fillCell.y * width); 
			patch = patchGrid[fillCell.x / WorldState.pLnum][fillCell.y / WorldState.pWnum];
			g.setColor((orgColor)); //our little red riding hood
			//g.fillRect(cellX + (length/4), cellY + (width/4), length - (length/2), width - (width/2));
			g.fillOval(cellX + (length/4), cellY + (width/4), length - (length/2), width - (width/2));
		}
	}

	public Patch[][] getPatchGrid() {
		return patchGrid;
	}
	
	public void updateUni(Graphics g)
	{
		Patch patch;
		
		for(int i =0; i < patchXnum; i++)
		{for (int j = 0; j < patchYnum; j++)
		{	//DRAW PATCH
			cellX = length + (i * length);
			cellY = width + (j * width); 
			patch = patchGrid[i][j];
			
			//System.out.println("Patch(" + i + "," + j + ") color: " + patchGrid[i][j].getColor());
			g.setColor(patch.getColor()); 
			g.fillRect(cellX, cellY, length, width);
			
			//DRAW RESOURCE
			if (patch.isHasResource())
			{//draw the resources that belong on the map
				//if (patch.getTheR().getAmount() > 0) g.setColor((patch.getTheR().getrColor()));
				//else g.setColor(WorldState.rExhaustionColor);
				if (!patch.hasFood() || patch.getTheR().getAmount() <= 0) g.setColor(WorldState.rExhaustionColor);
				else g.setColor((patch.getTheR().getrColor()));
				//g.fillRect(cellX + (length/4), cellY + (width/4), length - (length/2), width - (width/2));
				//g.fillOval(cellX + (length/4), cellY + (width/4), length - (length/2), width - (width/2));
				
				//draw a triangle!
				if (patch.getTheR().getrShape() == 0){
					int [] x = {cellX + (length/2), cellX, cellX + length};
					int [] y = {cellY + (width/4), cellY + ((3 * width)/4), cellY + ((3 * width)/4)};
					g.fillPolygon(x, y, 3);
					//g.drawString(Integer.toString(patch.getTheR().getAmount()), x[0], y[0]);
				} else if (patch.getTheR().getrShape() == 1) { //upside down triangle
					int [] x = {cellX + (length/2), cellX, cellX + length};
					int [] y = {cellY + ((3 * width)/4), cellY + (width/4), cellY + (width/4)};
					g.fillPolygon(x, y, 3);
					//g.drawString(Integer.toString(patch.getTheR().getAmount()), x[0], y[0]);
				} else //default small square 
				{
					g.fillRect(cellX + (length/4), cellY + (width/4), length - (length/2), width - (width/2));
				}
			}//draw resource
			
			//DRAW ENTITY
			if (patch.isHasEntity()) {
				g.setColor((orgColor3)); //our little red riding hood
				//g.fillRect(cellX + (length/4), cellY + (width/4), length - (length/2), width - (width/2));
				//g.fillOval(cellX + (length/4), cellY + (width/4), length - (length/2), width - (width/2));
				
				Organism thisOrganism = patch.getTheE();
				GeneToPhenotype thisOrganismsPhenotype = thisOrganism.getPheno(); 
				double limbLength = thisOrganismsPhenotype.limbLength; 
				double limbStructuralStrength = thisOrganismsPhenotype.limbStructuralStrength;
				
				//get facial characteristics
				double eyeSize = thisOrganismsPhenotype.eyeSize;
				double facialLength = thisOrganismsPhenotype.facialLength;
				double facialWidth = thisOrganismsPhenotype.facialWidth; 
				double organismsColorID = thisOrganismsPhenotype.generalPhenotype; 
				
				//get patch's center
				int[] centerOfPatch = patch.findCenter();
				int centerX = centerOfPatch[0];
				int centerY = centerOfPatch[1];
				
				//patch.
				
				//get patch's upper left corner
				int patchX = patch.getX();
				int patchY = patch.getY(); 
				 
				//make sure phenotype shapes don't go out of patch bounds
				int deltaCenterXToBound = centerX - patchX; 
				int deltaCenterYToBound = centerY - patchY; 
				
				
				if(limbLength < 0.25){	
					if(limbStructuralStrength < 0.25){
						g.fillRect(cellX + width - width/7, cellY + width - length/4, width/7, length/4);
						g.fillRect(cellX, cellY + width - length/4, width/7, length/4);
					}
					if(limbStructuralStrength >= 0.25 && limbStructuralStrength < 0.5){
						g.fillRect(cellX + width - width/6, cellY + width - length/4, width/6, length/4);
						g.fillRect(cellX, cellY + width - length/4, width/6, length/4);
					}
					if(limbStructuralStrength >= 0.5 && limbStructuralStrength < 0.75){
						g.fillRect(cellX + width - width/5, cellY + width - length/4, width/5, length/4);
						g.fillRect(cellX, cellY + width - length/4, width/5, length/4);
					}
					//if(limbStructuralStrength < 0.25){
					else{
						g.fillRect(cellX + width - width/4, cellY + width - length/4, width/4, length/4);
						g.fillRect(cellX, cellY + width - length/4, width/4, length/4);
					}
					
				}
				else if(limbLength >= 0.25 && limbLength < 0.5){
					//g.fillRect(cellX + width - width/6, cellY, width/6, length/3);
					//g.fillRect(cellX, cellY, width/6, length/3);
					
					if(limbStructuralStrength < 0.25){
						g.fillRect(cellX + width - width/7, cellY + width - length/3, width/7, length/3);
						g.fillRect(cellX, cellY + width - length/3, width/7, length/3);
					}
					else if(limbStructuralStrength >= 0.25 && limbStructuralStrength < 0.5){
						g.fillRect(cellX + width - width/6, cellY + width - length/3, width/6, length/3);
						g.fillRect(cellX, cellY + width - length/3, width/6, length/3);
					}
					else if(limbStructuralStrength >= 0.5 && limbStructuralStrength < 0.75){
						g.fillRect(cellX + width - width/5, cellY + width - length/3, width/5, length/3);
						g.fillRect(cellX, cellY + width - length/3, width/5, length/3);
					}
					//if(limbStructuralStrength < 0.25){
					else{
						g.fillRect(cellX + width - width/4, cellY + width - length/3, width/4, length/3);
						g.fillRect(cellX, cellY + width - length/3, width/4, length/3);
					}
				} 
				else if(limbLength >= 0.5 && limbLength < 0.75){
					//g.fillRect(cellX + width - width/5, cellY, width/5, length/2);
					//g.fillRect(cellX, cellY, width/5, length/2);
					
					if(limbStructuralStrength < 0.25){
						g.fillRect(cellX + width - width/7, cellY + width - length/2, width/7, length/2);
						g.fillRect(cellX, cellY + width - length/2, width/7, length/2);
					}
					else if(limbStructuralStrength >= 0.25 && limbStructuralStrength < 0.5){
						g.fillRect(cellX + width - width/6, cellY + width - length/2, width/6, length/2);
						g.fillRect(cellX, cellY + width - length/2, width/6, length/2);
					}
					else if(limbStructuralStrength >= 0.5 && limbStructuralStrength < 0.75){
						g.fillRect(cellX + width - width/5, cellY + width - length/2, width/5, length/2);
						g.fillRect(cellX, cellY + width - length/2, width/5, length/2);
					}
					//if(limbStructuralStrength < 0.25){
					else{
						g.fillRect(cellX + width - width/4, cellY + width - length/2, width/4, length/2);
						g.fillRect(cellX, cellY + width - length/2, width/4, length/2);
					}
				}
				//if(limbLength >= 0.75){
				else{
					//g.fillRect(cellX + width - width/4, cellY, width/4, length);
					//g.fillRect(cellX, cellY, width/4, length);
					
					if(limbStructuralStrength < 0.25){
						g.fillRect(cellX + width - width/7, cellY + width - length, width/7, length);
						g.fillRect(cellX, cellY + width - length, width/7, length);
					}
					else if(limbStructuralStrength >= 0.25 && limbStructuralStrength < 0.5){
						g.fillRect(cellX + width - width/6, cellY + width - length/2, width/6, length);
						g.fillRect(cellX, cellY + width - length, width/6, length);
					}
					else if(limbStructuralStrength >= 0.5 && limbStructuralStrength < 0.75){
						g.fillRect(cellX + width - width/5, cellY + width - length, width/5, length);
						g.fillRect(cellX, cellY + width - length, width/5, length);
					}
					//if(limbStructuralStrength < 0.25){
					else{
						g.fillRect(cellX + width - width/4, cellY + width - length, width/4, length);
						g.fillRect(cellX, cellY + width - length, width/4, length);
					}
				}
				
				
				
				//g.fillOval(cellX + (length/5), cellY + (width/5), 2* length/5, 2* width/5);
				
				
				//drawing the face
				g.setColor(orgColor5);
				
				if(facialLength < 0.25){
					g.fillOval(cellX, cellY, width, length/7);
					if(facialWidth < 0.25){
						g.fillOval(cellX + width/2 - width/10, cellY, width/5, length/2);	
					}
					else if(facialWidth >= 0.25 && facialWidth < 0.5){
						g.fillOval(cellX + width/2 - width/8, cellY, width/4, length/2);
					}
					else if(facialWidth >= 0.5 && facialWidth < 0.75){
						g.fillOval(cellX + width/2 - width/6, cellY, width/3, length/2);
					}
					else{
						g.fillOval(cellX + width/2 - width/4, cellY, width/2, length/2);
					}
				}
				else if(facialLength >= 0.25 && facialLength < 0.5){
					//g.fillOval(cellX, cellY, width, length/4);
					g.fillOval(cellX, cellY, width, length/7);
					if(facialWidth < 0.25){
						g.fillOval(cellX + width/2 - width/10, cellY, width/5, length/4);						
					}
					else if(facialWidth >= 0.25 && facialWidth < 0.5){
						g.fillOval(cellX + width/2 - width/8, cellY, width/4, length/4);
					}
					else if(facialWidth >= 0.5 && facialWidth < 0.75){
						g.fillOval(cellX + width/2 - width/6, cellY, width/3, length/4);
					}
					else{
						g.fillOval(cellX + width/2 - width/4, cellY, width/2, length/4);
					}
				}
				else if(facialLength >= 0.5 && facialLength < 0.75){
					g.fillOval(cellX, cellY, width, length/7);
					if(facialWidth < 0.25){
						g.fillOval(cellX + width/2 - width/10, cellY, width/5, length/3);						
					}
					else if(facialWidth >= 0.25 && facialWidth < 0.5){
						g.fillOval(cellX + width/2 - width/8, cellY, width/4, length/3);
					}
					else if(facialWidth >= 0.5 && facialWidth < 0.75){
						g.fillOval(cellX + width/2 - width/6, cellY, width/3, length/3);
					}
					else{
						g.fillOval(cellX + width/2 - width/4, cellY, width/2, length/3);
					}
				}
				else{
					g.fillOval(cellX, cellY, width, length/7);
					if(facialWidth < 0.25){
						g.fillOval(cellX + width/2 - width/10, cellY, width/5, length/2);						
					}
					else if(facialWidth >= 0.25 && facialWidth < 0.5){
						g.fillOval(cellX + width/2 - width/8, cellY, width/4, length/2);
					}
					else if(facialWidth >= 0.5 && facialWidth < 0.75){
						g.fillOval(cellX + width/2 - width/6, cellY, width/3, length/2);
					}
					else{
						g.fillOval(cellX + width/2 - width/4, cellY, width/2, length/2);
					}
				}
				
				//drawing the eyes 
				g.setColor(orgColor4);
				
				if(eyeSize < 0.25){
					g.fillOval(cellX + width - width/5, cellY, width/5, length/5);
					g.fillOval(cellX, cellY, width/5, length/5);
					g.setColor(colorIDString);
					g.drawString(String.valueOf(numberPhenotypeID1), cellX + width/2 - 3, cellY + width);
					//g.fillRect(cellX, cellY + width/2 + 2, width, length/5);
				}
				else if(eyeSize >= 0.25 && eyeSize < 0.5){
					g.setColor(orgColor4);
					g.fillOval(cellX + width - width/4, cellY, width/4, length/4);
					g.fillOval(cellX, cellY, width/4, length/4);
					g.setColor(colorIDString);
					//g.fillRect(cellX, cellY + width/2 + 2, width, length/5);
					g.drawString(String.valueOf(numberPhenotypeID2), cellX + width/2 - 3, cellY + width);
				}
				else if(eyeSize >= 0.5 && eyeSize < 0.75){
					g.setColor(orgColor4);
					g.fillOval(cellX + width - width/3, cellY, width/3, length/3);
					g.fillOval(cellX, cellY, width/3, length/3);
					g.setColor(colorIDString);
					//g.fillRect(cellX, cellY + width/2 + 2, width, length/5);
					g.drawString(String.valueOf(numberPhenotypeID3), cellX + width/2 - 3, cellY + width);
				}
				else{
					g.setColor(orgColor4);
					g.fillOval(cellX + width - width/2, cellY, width/2, length/2);
					g.fillOval(cellX, cellY, width/2, length/2);
					g.setColor(colorIDString);
					//g.fillRect(cellX, cellY + width/2 + 2, width, length/5);
					g.drawString(String.valueOf(numberPhenotypeID4), cellX + width/2 - 3, cellY + width);
				}
				patch.setLocalEvent(null); //events last 1 turn?
			}//draw entity
		}
		}
	}
	
	public Patch getPatch(Point p)
	{
		return getPatch(p.x, p.y);
	}
	
	public Patch getPatch(int x, int y)
	{
		return patchGrid[x][y];
	}
}
