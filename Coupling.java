
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

	
	public void addPositiveCoupleAmount(double ca){
		if (positiveCouple) coupleAmount += ca;
		else coupleAmount -= ca;
	}
	
	public void addNegativeCoupleAmount(double ca){
		if (!positiveCouple) coupleAmount += ca;
		else coupleAmount -= ca;
	}

	public boolean isPositiveCouple() {
		return positiveCouple;
	}

	public void setPositiveCouple(boolean positiveCouple) {
		this.positiveCouple = positiveCouple;
	}
	
	public boolean isCoupled(Organism a)
	{
		if (a.getName() == orgA.getName()) return true;
		else if (a.getName() == orgB.getName()) return true;
		else return false;
	}
	
	//compute coupleAmount (either [-1 to 0] or [0 to 1]) and 
		//set the positiveCouple to false or true respectively
		public double setCoupleValue(){
			coupleAmount = 0;
			//Get each of the organisms' phenotypes
			GeneToPhenotype orgAPhenotype = orgA.getPheno();
			GeneToPhenotype orgBPhenotype = orgB.getPheno();
			
			double orgAagreeability = orgAPhenotype.agreeability; 
			double orgBagreeability = orgBPhenotype.agreeability;
			double orgAgeneralPhenotype = orgAPhenotype.generalPhenotype; 
			double orgBgeneralPhenotype = orgBPhenotype.generalPhenotype;
			
			
			//multiplied by 1/3 given the 1/3 weight of agreeability
			coupleAmount = ((orgAagreeability + orgBagreeability) *  1/3)/2; 
			double deltaPhenoSimilarty = Math.abs(orgAgeneralPhenotype - orgBgeneralPhenotype);
			
			
			//for each condition, coupleAmount is a sum of a phenotype similarity value that is 
			//multiplied by 2/3; this is done because we assign a 2/3 weight of phenotype similarity
			//versus the 1/3 weight for agreeability 
			if(deltaPhenoSimilarty == 0){
				coupleAmount = (coupleAmount + (1*2/3))/2;  
			}
			else if(deltaPhenoSimilarty == 1){
				coupleAmount = (coupleAmount + (0.75*2/3))/2; 
			}
			else if(deltaPhenoSimilarty == 2){
				coupleAmount = (coupleAmount + (0.5*2/3))/2;
			}
			else{
				coupleAmount = (coupleAmount + (0.25*2/3))/2;
			}
			
			
			//positiveCouple is negative or positive
			if(coupleAmount < 0.5){
				coupleAmount = coupleAmount * -2; //value within 0 to 0.5 range translated to -1 to 0 range 
				positiveCouple = false;
			}
			else{
				coupleAmount = (coupleAmount - 0.5) * 2; //value within 0.5 to 1 range translated to 0 to 1 range
				positiveCouple = true;
			}
						
			return coupleAmount; 
		}
}
