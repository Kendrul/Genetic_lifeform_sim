
public class Parameter {
	
	private boolean isMutatable;

	public Parameter() {
	}
	
	public Parameter clone(){
		Parameter newPara = new Parameter();
		return newPara;
	}
	
	public boolean getMutatable()
	{
		return isMutatable;
	}
	
	public void mutateRandom()
	{
		
	}
	
	public void mutateDrift(double roll, double mutationFactor)
	{
		
	}
}
