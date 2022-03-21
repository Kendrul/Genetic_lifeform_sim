
public class Patch {
	
	//patch origin (top-left corner coordinates)
	private int worldX;
	private int worldY;
	
	private int type; //See WorldState for possible values and their meaning
	
	private boolean hasResource = false;
	private boolean hasEntity = false;
	
	private Entity theE = null;
	private Resource theR = null;
	
	public Patch(int wx, int wy, int tType)
	{
		worldX = wx;
		worldY = wy;
		type = tType; //terrain
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

	public Object getTheR() {
		return theR;
	}

	public void setTheR(Object theR) {
		if ((hasEntity == false) && ((hasResource == false) || (theR == null))) this.theR = theR; //"remove hasEntity = false" to remove entity-resource collision
		if (this.theR != null) hasResource = true;
		else hasResource = false;
	}
	
	

}
