

import java.awt.Color;



public class Resource {
	
	private int resourceType; //food, powerup, trap
	private int resourceNum; //specific resource
	private Color rColor;
	private Patch home;
	private Organism owner;
	private int amount;
	private int startAmount; //patch resources only
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
		startAmount = amount;
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
		startAmount = a;
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
		startAmount = 0; //only applies to patch resources
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
	
	public int removeAmount(int a)
	{
		if (a >= amount){//not enough resource, it has been e
			a = amount;
			amount = 0;
			if(WorldState.allowResourceExhaustion) home.setTheR(null);
			return a;
		} else{
		this.amount -= a;
		return a;
		}
	}
	
	public void addAmount(int amount)
	{
		this.amount += amount;
	}
	
	public void replenish()
	{
		if ((amount == 0) || (amount < (startAmount * WorldState.respawnAmountMax))){
		
			double spawnRoll = WorldState.rng0[0].rDouble();
			if (spawnRoll <= WorldState.rReplenishChance[home.getType()][resourceNum]);
			{
				double amountRoll = WorldState.rng0[1].rDouble();
				amount += (startAmount * WorldState.respawnAmountMax) * amountRoll;
				//home.s
				WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + getName() + " regrew " + amount + " units on location (" + home.getX() +","+ home.getY() +").");
				Starter.getStats().incReplenishedAmount(amount);
				Starter.getStats().incReplenishEvents(1);
			}
		}
	}
	
	public void replenish2()
	{//reset's the resource value of the patch back to the start value
		amount = startAmount;
		WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + getName() + " regrew " + amount + " units on location (" + home.getX() +","+ home.getY() +").");
		Starter.getStats().incReplenishedAmount(amount);
		Starter.getStats().incReplenishEvents(1);
	}
	
}
