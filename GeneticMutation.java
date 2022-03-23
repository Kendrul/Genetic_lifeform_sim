


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;


public class GeneticMutation {

private Organism mother;
private Organism father;
private Organism child;
private double mutationFactor;
private boolean isDebug = WorldState.isDebug;


public GeneticMutation(Organism dad, Organism mom, double mutation){

	father = dad;
	mother = mom;
	mutationFactor = mutation;
	
	child = new Organism();

}//end constructor

public GeneticMutation(Organism mom, double mutation){

	mother = mom;
	father = mom;
	mutationFactor = mutation;
	
	child = new Organism();

}//end constructor


public void newGenomeS()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
{
  reflectPack rd = EventPack.geneFields(father.getGenes());
  reflectPack rm = EventPack.geneFields(mother.getGenes());
  GeneSequence kidGene = new GeneSequence();
  
  for (int index = 0; index < rd.getFieldArrayNames().length; index++){
	  if (!(rd.getFieldArrayNames()[index] == "gene_Osteo_Width_ECM") 
			  && !(rd.getFieldArrayNames()[index] == "gene_Myo_Slow_Twitch_ECM") 
			  && !(rd.getFieldArrayNames()[index] == "gene_Neuro_Morphogenetic_Protein_Neutral") 
			  && !(rd.getFieldArrayNames()[index] == "gene_Neuro_Morphogenetic_Protein_MoodPositive")
			  && !(rd.getFieldArrayNames()[index] == "gene_Neuro_Morphogenetic_Protein_Flight"))
	  {//all of the above are determined by (1 - someAttribute)
		  double d = rd.getFieldArrayValues()[index];
		  double m = rm.getFieldArrayValues()[index];		  
		  double k = mutatationS(d, m);
		  setGene(index, k, kidGene);
	  }//end if-then
  }//end for loop
  
  child.setGenes(kidGene);
}

public void newGenomeA()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
{
  reflectPack rm = EventPack.geneFields(mother.getGenes());
  GeneSequence kidGene = new GeneSequence();
  
  for (int index = 0; index < rm.getFieldArrayNames().length; index++){
	  if (!(rm.getFieldArrayNames()[index] == "gene_Osteo_Width_ECM") 
			  && !(rm.getFieldArrayNames()[index] == "gene_Myo_Slow_Twitch_ECM") 
			  && !(rm.getFieldArrayNames()[index] == "gene_Neuro_Morphogenetic_Protein_Neutral") 
			  && !(rm.getFieldArrayNames()[index] == "gene_Neuro_Morphogenetic_Protein_MoodPositive")
			  && !(rm.getFieldArrayNames()[index] == "gene_Neuro_Morphogenetic_Protein_Flight"))
	  {//all of the above are determined by (1 - someAttribute)
		  double m = rm.getFieldArrayValues()[index];		  
		  double k = mutatationA(m);
		  setGene(index, k, kidGene);
	  }//end if-then
  }//end for loop
  
  child.setGenes(kidGene);
}


private void setGene(int index,double val, GeneSequence kGene)throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
{
	Field[] geneSequenceFieldArray = kGene.getClass().getDeclaredFields();
	Field f = geneSequenceFieldArray[index];
	f.setAccessible(true);			
	f.set(kGene, val);
	f.setAccessible(false);
}

private double mutatationS(double dParam, double mParam){
	//establish whether mutation will occur: radical, drift, none
	//pick a parent to take a gene from
	//give that gene, or drift from that gene

		double roll = WorldState.rng0[4].rDouble();
		double temp = 0;
		if (roll < WorldState.radicalMutation) {
			//radical mutation
			temp = mutateRandom();
			return temp;
		} else if (roll <= (WorldState.getDrift() + mutationFactor)) {
			//genetic drift
			boolean pickMom = pickParent();
			roll = WorldState.rng0[5].rDouble();
			roll = (roll *2) - 1;
			if (pickMom) temp = mParam;
			else temp = dParam;
			temp = mutateDrift(roll, temp);
			return temp;
		} else
		{//no mutation 
			boolean pickMom = pickParent();
			if (pickMom) return mParam; 
			else return dParam;
		}//end if-else-else
}

private double mutatationA(double mParam){
	//establish whether mutation will occur: radical, drift, none
	//pick a parent to take a gene from
	//give that gene, or drift from that gene

		double roll = WorldState.rng0[4].rDouble();
		double temp = 0;
		
		if (roll <= WorldState.radicalMutation) {
			//radical mutation
			temp = mutateRandom();
			return temp;
		} else if (roll <= (WorldState.getDrift() + mutationFactor)) {
			//genetic drift
			roll = WorldState.rng0[5].rDouble();
			roll = (roll *2) - 1;
			temp = mutateDrift(roll, mParam);
			return temp;
		} else
		{//no mutation 
			return mParam; 		
		}//end if-else-else
}

private boolean pickParent()
{
    int choice = WorldState.rng0[3].rInt(2);
    if (choice == 0) return false; //picked dad
    else return true; //picked mom
}

private double mutateDrift(double roll, double startVal){
	//allows for mutation within a small range bounded by +- (driftFactor + mutationFactor)
	double finalVal = startVal + (roll * (startVal * (mutationFactor + WorldState.driftAmount)));
	if (finalVal > 1) finalVal = 1;
	return finalVal;
}

private double mutateRandom(){
	double roll = WorldState.rng0[5].rDouble();
	if (roll > 1) roll = 1;
	return roll;
}

public Organism getChild() {
	return child;
}

}//end class

