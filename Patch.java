

import java.awt.Color;
import java.util.ArrayList;



public class Patch {
	
	//patch origin (top-left corner coordinates)
	private int worldX;
	private int worldY;

	private int type; //See WorldState for possible values and their meaning
	private Color color = Color.black;
	
	private boolean hasResource = false;
	private boolean hasEntity = false;
	private double mutationFactor = 0;
	
	public ArrayList<Organism> theE = null;
	private Resource theR = null;
	private String localEvent = null;
	
	public Patch(int wx, int wy, int tType)
	{

		if (tType >= WorldState.terrainTypes.length) tType = tType % (WorldState.terrainTypes.length);
		type = tType; //terrain	
		color = WorldState.terrainColor[type];
		worldX = wx;
		worldY = wy;
		theE = new ArrayList<Organism>();
	}
	
	public void spawnResource(double roll)
	{
		if (roll < 0) roll *= (-1);
		
		if (!hasResource) {
			double cumulative = 0;
			for (int i = 0; i < WorldState.resourceNum.length; i++)
			{
				if(roll < (WorldState.rSpawnChance[type][i] + cumulative))
				{
					hasResource = true; 
					theR = new Resource(0, i, this); 
					break;
				}else
				{
					cumulative += WorldState.rSpawnChance[type][i];
				}
			}
		}
	}
	
	public int [] findCenter()
	{
		int [] out = new int[2];
		//finds the midpoint of the patch
		out[0] = worldX + (WorldState.pLength / 2) + (WorldState.pLength % 2);
		out[1] = worldY + (WorldState.pWidth / 2) + (WorldState.pWidth % 2);
		return out;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isHasResource() {
		return hasResource;
	}

	public int getX() {
		return worldX;
	}

	public void setX(int x) {
		worldX = x;
	}

	public int getY() {
		return worldY;
	}

	public void setY(int y) {
		worldY = y;
	}
	
	public boolean isHasEntity() {
		return hasEntity;
	}

	public Organism getTheE() {
		return theE.get(0);
	}
	
	public Organism getTheE(int index){
		return theE.get(index);
	}
	
	public int population(){
		return theE.size();
	}

	public void setTheE(Organism theE) {
		this.theE.add(theE); //"remove hasResource = false" to remove entity-resource collision
		if (this.theE != null) hasEntity = true;
		else hasEntity = false;
	}
	
	public void setHasE(boolean hasE)
	{
		hasEntity = hasE;
	}

	public Resource getTheR() {
		return theR;
	}

	public void setTheR(Resource theR) {
		this.theR = theR; 
		if (this.theR != null) hasResource = true;
		else hasResource = false;
	}
	
	public void setColor(Color c)
	{//should be changed to update the type number and change the color from there.
		color = c;
	}
	
	public Color getColor()
	{
		return color;
	}

	public void removeE(Organism e)
	{
		theE.remove(e);
		hasEntity = false;
	}
	
	public synchronized boolean hasFood()
	{
		if (theR != null){
			//if((theR.getResourceType() == WorldState.resourceType[0]) && (theR.getAmount() > 0))
			if (theR.getAmount() > 0)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean orgHasFood()
	{
		if (theE.size() > 0){
			for(int i = 0; i < theE.size(); i++)
			{
				if(theE.get(i).hasFood()) return true;
			}
		}
		return false;
	}
	
	public Organism orgWithMostFood()
	{//find the organism on this patch that has the most food
		Organism o = null;
		if (theE.size() > 0){
			for(int i = 0; i < theE.size(); i++)
			{	
				if (o == null) o = theE.get(i);
				else if(theE.get(i).potentialEnergy() > o.potentialEnergy()) o = theE.get(i);
			}
		}
		return o;
	}

	public String getLocalEvent() {
		return localEvent;
	}

	public void setLocalEvent(String localEvent) {
		this.localEvent = localEvent;
	}
	
	public double getMutation()
	{
		return mutationFactor;
	}
	
	public Organism findOtherOrg(Organism o){
		for (int i = 0; i < theE.size(); i++){
			if (theE.get(i).equals(o)) continue;
			else return theE.get(i);
		}
		return null;
	}
	
}
