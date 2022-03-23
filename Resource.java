import java.awt.Color;


public class Resource {
	
	private int resourceType; //food, powerup, trap
	private int resourceNum; //specific resource
	private Color rColor;
	private Patch home;
	private int amount;
	private int rShape;



	public Resource(int type, int num, Patch location)
	{
		resourceType = type;
		resourceNum = num;
		home = location;
		rColor = WorldState.resourceColor[num];
		rShape = WorldState.resourceShape[num];
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
	
	
}
