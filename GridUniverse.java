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
	private List<Point> emptyCells;
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
						seed = i;//TODO
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
		
		//empty(g);
		//fill(g);
		testUpdate(g);
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
	
	public void testUpdate(Graphics g)
	{
		Patch patch;
		Point emptyCell,fillCell;
		
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
				if (patch.getTheR().getAmount() > 0) g.setColor((patch.getTheR().getrColor()));
				else g.setColor(WorldState.rExhaustionColor);
				//g.fillRect(cellX + (length/4), cellY + (width/4), length - (length/2), width - (width/2));
				//g.fillOval(cellX + (length/4), cellY + (width/4), length - (length/2), width - (width/2));
				
				//draw a triangle!
				if (patch.getTheR().getrShape() == 0){
					int [] x = {cellX + (length/2), cellX, cellX + length};
					int [] y = {cellY + (width/4), cellY + ((3 * width)/4), cellY + ((3 * width)/4)};
					g.fillPolygon(x, y, 3);
				} else if (patch.getTheR().getrShape() == 1) { //upside down triangle
					int [] x = {cellX + (length/2), cellX, cellX + length};
					int [] y = {cellY + ((3 * width)/4), cellY + (width/4), cellY + (width/4)};
					g.fillPolygon(x, y, 3);
				} else //default small square 
				{
					g.fillRect(cellX + (length/4), cellY + (width/4), length - (length/2), width - (width/2));
				}
			}//draw resource
			
			//DRAW ENTITY
			if (patch.isHasEntity()) {
				if ((patch.population() > 1 ) && (patch.getLocalEvent() != null))
				{
					g.setColor((textColor));
					g.drawString(patch.getLocalEvent(), cellX, cellY);
				}else {
					g.setColor((orgColor)); //our little red riding hood
					//g.fillRect(cellX + (length/4), cellY + (width/4), length - (length/2), width - (width/2));
					//g.fillOval(cellX + (length/4), cellY + (width/4), length - (length/2), width - (width/2));
					g.fillOval(cellX + (length/5), cellY + (width/5), 2* length/5, 2* width/5);
					//g.fillOval(cellX + (length*(4/5)), cellY + (width * (4/5)), 2* length/5, 2* width/5);
					g.setColor(orgColor2);
					g.fillOval(cellX + (length/2) - 1, cellY + (width/2) -1, 2* length/5, 2* width/5);
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
