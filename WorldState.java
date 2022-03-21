
public class WorldState {
	
	
	public final double driftFactor = 0.05;
	private int counterID = 0;
	
	public double getDrift()
	{
		return driftFactor;
	}
	
	public int getID(){
		counterID++;
		return counterID - 1;
	}
	
}
