
public class Coupling {

	private Organism orgA;
	private Organism orgB;
	private Patch patch;
	
	private double coupleAmount;
	private boolean positiveCouple;
	private boolean isOrgPatch;
	
	public Coupling(Organism a, Organism b)
	{
		orgA = a;
		orgB = b;
		patch = null;
		coupleAmount = 0;
		positiveCouple = true;
		isOrgPatch = false;
	}
	
	public Coupling(Organism a, Patch p)
	{
		Organism orgA = a;
		Organism orgB = null;
		Patch patch = p;
		coupleAmount = 0;
		positiveCouple = true;
		isOrgPatch = true;
	}
	
	public Coupling(Organism a, Organism b, double amount, boolean pos)
	{
		orgA = a;
		orgB = b;
		coupleAmount = amount;
		positiveCouple = pos;
		isOrgPatch = false;
	}
	
	public Coupling(Organism a, Patch p, double amount, boolean pos)
	{
		Organism orgA = a;
		Organism orgB = null;
		Patch patch = p;
		coupleAmount = amount;
		positiveCouple = pos;
		isOrgPatch = true;
	}
	
	public Organism [] getPair(){
		Organism [] orgPair;
		if(!isOrgPatch) orgPair = new Organism []{orgA, orgB};
		else orgPair = new Organism []{orgA};
		return orgPair;
	}
	
	public Organism getOtherOrg(Organism o)
	{	if(!isOrgPatch){
		if (o.equals(orgA)) return orgB;
		else return orgA;
		} else return null;
	}

	public Patch getPatch() {
		return patch;
	}

	public boolean isOrgPatch() {
		return isOrgPatch;
	}

	public double getCoupleAmount() {
		return coupleAmount;
	}

	public void setCoupleAmount(double coupleAmount) {
		this.coupleAmount = coupleAmount;
	}

	public boolean isPositiveCouple() {
		return positiveCouple;
	}

	public void setPositiveCouple(boolean positiveCouple) {
		this.positiveCouple = positiveCouple;
	}
	
	
	
}
