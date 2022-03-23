

import java.awt.Color;



public class Resource {
	
	private int resourceType; //food, powerup, trap
	private int resourceNum; //specific resource
	private Color rColor;
	private Patch home;
	private Organism owner;
	private int amount;
	private int rShape;



	public Resource(int type, int num, Patch location)
	{
		resourceType = type;
		resourceNum = num;
		home = location;
		owner = null;
		rColor = WorldState.resourceColor[num];
		rShape = WorldState.resourceShape[num];
		amount = WorldState.rng0[1].rInt(WorldState.resourceAmountMax);
	}
	
	public Resource(Resource clone, int a)
	{
		resourceType = clone.getResourceType();
		resourceNum = clone.getResourceNum();
		home = null;
		owner = null;
		rColor = WorldState.resourceColor[clone.getResourceNum()];
		rShape = WorldState.resourceShape[clone.getResourceNum()];
		amount = a;
	}

	public Resource(int type, int num, Organism own)
	{
		resourceType = type;
		resourceNum = num;
		home = null;
		owner = own;
		rColor = WorldState.resourceColor[num];
		rShape = WorldState.resourceShape[num];
		amount = WorldState.rng0[1].rInt(WorldState.resourceAmountMax);
	}
	
		public Organism getOwner() {
		return owner;
	}

		public String getName()
		{
			return WorldState.resourceName[resourceNum];
		}
		
	public void setOwner(Organism owner) {
		this.owner = owner;
	}
	public int getrShape() {
		return rShape;
	}

	public void setrShape(int shape) {
		this.rShape = shape;
	}
	public int getResourceType() {
		return resourceType;
	}

	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}

	public int getResourceNum() {
		return resourceNum;
	}

	public void setResourceNum(int resourceNum) {
		this.resourceNum = resourceNum;
	}

	public Color getrColor() {
		return rColor;
	}

	public void setrColor(Color rColor) {
		this.rColor = rColor;
	}

	public Patch getHome() {
		return home;
	}

	public void setHome(Patch home) {
		this.home = home;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void removeAmount(int amount)
	{
		this.amount -= amount;
	}
	
	public void addAmount(int amount)
	{
		this.amount += amount;
	}
	
	
}
