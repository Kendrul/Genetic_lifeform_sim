import java.util.ArrayList;
import java.util.Random;

public class GeneticMutation {

private Entity mother;
private Entity father;
private Entity child;
private WorldState theWorld;
private double mutationFactor;
private boolean isDebug;
private Random rng;

public GeneticMutation(Entity dad, Entity mom, double mutation, boolean debugModeOn, WorldState state){

	father = dad;
	mother = mom;
	mutationFactor = mutation;
	isDebug = debugModeOn;
	rng = new Random();
	theWorld = state;
	
	//TODO stats?
	child = new Entity();

}//end constructor

public void plant(int seed)
{
    rng = new Random(seed);
}//end plant

public void buildGenomeS()
{
	ArrayList<Parameter> dadpList = null;
	ArrayList<Parameter> mompList = null;
	if (father != null) dadpList = father.getParameterList();
	if (mother != null) mompList = mother.getParameterList();
	ArrayList<Parameter> kidpList = new ArrayList<Parameter>();
	    
	int size;
	if (dadpList.size() > mompList.size()) size = dadpList.size();
	else size = mompList.size();
	
	for (int i = 0; i < size; i++)
	{
	        //check if mutation is allowed for this parameter
		if ((dadpList.get(i) != null) & (dadpList.get(i).getMutatable()))
		{
			if (mompList.get(i) != null) kidpList.add(mutate(dadpList.get(i), mompList.get(i)));
			else kidpList.add(mutate( dadpList.get(i), null));
		        
		}//end if-then
		else if ((mompList.get(i) != null) & (mompList.get(i).getMutatable()))
		{
		    kidpList.add(mutate( null, mompList.get(i)));
		}//end if-else
	}//end for loop        

}//end buildGenome

public void buildGenomeA(){
	ArrayList<Parameter> mompList = null;
	if (mother != null) mompList = mother.getParameterList();
	else mompList = father.getParameterList();
	ArrayList<Parameter> kidpList = new ArrayList<Parameter>();
	    
	int size = mompList.size();
	
	for (int i = 0; i < size; i++)
	{
	        //check if mutation is allowed for this parameter
		if(mompList.get(i).getMutatable())
		{
		    kidpList.add(mutate( null, mompList.get(i)));
		}//end if-else
	}//end for loop      
}//end buildGenomeA

private Parameter mutate(Parameter dpara, Parameter mpara)
{//establish whether mutation will occur: radical, drift, none
//pick a parent to take a gene from
//give that gene, or drift from that gene

	double roll = rng.nextDouble();
	Parameter temp = null;
	if (roll <= 0.01) {
		//radical mutation
		    boolean pickMom = pickParent(dpara, mpara);
		if (pickMom)
		{
		    temp = mpara.clone();
		} 
		else 
		{
			temp = dpara.clone();
		}
		temp.mutateRandom();
		return temp;
	} else if (roll <= (theWorld.getDrift() + mutationFactor)) {
		//genetic drift
		boolean pickMom = pickParent(dpara, mpara);
		roll = rng.nextDouble();
		roll = (roll *2) - 1;
		if (pickMom) temp = mpara.clone();
		else temp = dpara.clone();
		temp.mutateDrift(roll, mutationFactor);
		return temp;
	} else
	{//no mutation 
		boolean pickMom = pickParent(dpara, mpara);
		if (pickMom) return mpara; 
		else return dpara;
	}//end if-else-else
}//end mutate

private boolean pickParent(Parameter dad, Parameter mom)
{//Randomly chooses a parent, TRUE = Mom, FALSE = Dad
	if (dad == null) {
	        return true; //dad does not have this parameter, so pick mom
	} else if (mom == null)
	{
	    return false; //mom does not have this parameter, so pick dad
	}//end if-else
	else
	{
	    int choice = rng.nextInt() % 2;
	    if (choice == 0) return false; //picked dad
	    else return true; //picked mom
	}//end if-else-else
}//end pickParent


}//end class

