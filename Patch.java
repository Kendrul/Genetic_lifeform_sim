import java.awt.Color;


public class Patch {
	
	//patch origin (top-left corner coordinates)
	private int worldX;
	private int worldY;
	


	private int type; //See WorldState for possible values and their meaning
	private Color color = Color.black;
	
	private boolean hasResource = false;
	private boolean hasEntity = false;
	
	private Entity theE = null;
	private Resource theR = null;
	
	public Patch(int wx, int wy, int tType)
	{

		if (tType >= WorldState.terrainTypes.length) tType = tType % (WorldState.terrainTypes.length);
		type = tType; //terrain	
		color = WorldState.terrainColor[type];
		worldX = wx;
		worldY = wy;
	}
	
	public void spawnResource(double roll)
	{
		if (roll < 0) roll *= (-1);
		
		if (!hasResource) {
			double cumulative = 0;
			for (int i = 0; i < WorldState.resourceNum.length; i++)
			{
				if(roll < WorldState.rSpawnChance[type][i])
				{
					hasResource = true; 
					theR = new Resource(0, i, this); //TODO
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

	public Entity getTheE() {
		return theE;
	}

	public void setTheE(Entity theE) {
		if ((hasResource == false) && ((hasEntity == false) || (theE == null))) this.theE = theE; //"remove hasResource = false" to remove entity-resource collision
		if (this.theE != null) hasEntity = true;
		else hasEntity = false;
	}
	
	public boolean setHasE(boolean hasE)
	{
/*		if ((hasEntity == false) && (hasE)) {
			hasEntity = hasE;
			return true;}
		else if (hasE == false) hasEntity = hasE;
		return false;*/
		hasEntity = hasE;
		return hasE;
	}

	public Resource getTheR() {
		return theR;
	}

	public void setTheR(Resource theR) {
		if ((hasEntity == false) && ((hasResource == false) || (theR == null))) this.theR = theR; //"remove hasEntity = false" to remove entity-resource collision
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
	
}
