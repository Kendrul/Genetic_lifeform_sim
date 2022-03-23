import java.awt.AWTException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;


public class EventPack {

	
	//--------------------------------------------------------------------------------------------
	//reproduction events
	public static Organism reproductionS(Organism dad, Organism mom, double mutation) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	{
		GeneticMutation gm = new GeneticMutation(dad, mom, mutation);
		gm.newGenomeS();
		Organism kid = gm.getChild();
		Starter.getLifeForms().add(kid);
		kid.setTheOrg(new OrganismGFX(Starter.getGrid(), WorldState.rng1[1].rInt(WorldState.pLnum), WorldState.rng1[2].rInt(WorldState.pWnum)));
		WorldState.addLogEvent("[Turn: " + Starter.getTurn() + "] " + dad.getName() + " and " + mom.getName() + " had a child named " + kid.getName());
		return kid;		
	}
	
	public static Organism reproductionA(Organism parent, double mutation) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	{
		GeneticMutation gm2 = new GeneticMutation(parent, mutation);
		gm2.newGenomeA();
		Organism kid2 = gm2.getChild();
		Starter.getLifeForms().add(kid2);
		kid2.setTheOrg(new OrganismGFX(Starter.getGrid(), WorldState.rng1[1].rInt(WorldState.pLnum), WorldState.rng1[2].rInt(WorldState.pWnum)));
		WorldState.addLogEvent("[Turn: " + Starter.getTurn() + "] " + parent.getName() + " had a child named " + kid2.getName());
		return kid2;
	}
	
	public static int litterSize()
	{
		return (WorldState.rng0[3].rInt(WorldState.maxLitter) + 1);
	}
	//-------------------------------------------------------------------
	//GENETIC STUFF
	public static reflectPack geneFields(GeneSequence genes) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		Class aClass = genes.getClass();
		Field[] geneSequenceFieldArray = aClass.getDeclaredFields(); //.getDeclaredFields(); 
		double[] genoArray = new double[geneSequenceFieldArray.length]; //automatic adjustment of array length based on number of field variables
		String[] genoArrayNames = new String[geneSequenceFieldArray.length]; //"------------"
		
		
		for (int i = 0; i < geneSequenceFieldArray.length; i++){
			Field f = geneSequenceFieldArray[i];
			genoArray[i] = (double)geneSequenceFieldArray[i].get(genes);
			genoArrayNames[i] = (String)geneSequenceFieldArray[i].getName();					
		}
		
		reflectPack r = new reflectPack(geneSequenceFieldArray, genoArray, genoArrayNames);
		
		return r;
	}
	
	public static reflectPack phenoFields(GeneToPhenotype pheno) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		    Class aClass1 = pheno.getClass();
			Field[] phenoStartFieldArray = aClass1.getDeclaredFields(); //.getDeclaredFields(); 
			
			
			int arrayLengthMinusArrayFields = 0; 
			for (int i = 0; i < phenoStartFieldArray.length; i++){
				Field f = phenoStartFieldArray[i];
				if(f.getType().equals(double.class)){
					arrayLengthMinusArrayFields++; 
				}
			}
			
			double[] phenoArray = new double[arrayLengthMinusArrayFields]; //automatic adjustment of array length based on number of field variables
			String[] phenoArrayNames = new String[arrayLengthMinusArrayFields]; //"------------"
			
			int phenoArrayIterator = 0;
			for (int i = 0; i < phenoStartFieldArray.length; i++){
				Field f = phenoStartFieldArray[i];
				
				//skip the array fields declared in this class
				if(f.getType().equals(double.class)){
					phenoArray[phenoArrayIterator] = (double)phenoStartFieldArray[i].get(pheno);
					phenoArrayNames[phenoArrayIterator] = (String)phenoStartFieldArray[i].getName();
					phenoArrayIterator++; 
				}		
			}
						
			reflectPack r = new reflectPack(phenoStartFieldArray, phenoArray, phenoArrayNames);
			return r;
		}
	
	public static reflectPack kinFields(PhenotypeToKinetics kin) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		    Class aClass1 = kin.getClass();
			Field[] kinStartFieldArray = aClass1.getDeclaredFields(); //.getDeclaredFields(); 
			
			
			int arrayLengthMinusArrayFields = 0; 
			for (int i = 0; i < kinStartFieldArray.length; i++){
				Field f = kinStartFieldArray[i];
				if(f.getType().equals(double.class)){
					arrayLengthMinusArrayFields++; 
				}
			}
			
			double[] kinArray = new double[arrayLengthMinusArrayFields]; //automatic adjustment of array length based on number of field variables
			String[] kinArrayNames = new String[arrayLengthMinusArrayFields]; //"------------"
			
			int phenoArrayIterator = 0;
			for (int i = 0; i < kinStartFieldArray.length; i++){
				Field f = kinStartFieldArray[i];
				
				//skip the array fields declared in this class
				if(f.getType().equals(double.class)){
					kinArray[phenoArrayIterator] = (double)kinStartFieldArray[i].get(kin);
					kinArrayNames[phenoArrayIterator] = (String)kinStartFieldArray[i].getName();
					phenoArrayIterator++; 
				}		
			}
						
			reflectPack r = new reflectPack(kinStartFieldArray, kinArray, kinArrayNames);
			return r;
		}
	
	public static GeneSequence randomGS() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException{
		WorldState.addLogEvent("Begin GeneSequence Manipulation....");
		GeneSequence aGeneSequence = new GeneSequence(); 
        Class aClass = aGeneSequence.getClass();
		Field[] geneSequenceFieldArray = aClass.getDeclaredFields(); //.getDeclaredFields(); 
		double[] genoArray = new double[geneSequenceFieldArray.length]; //automatic adjustment of array length based on number of field variables
		String[] genoArrayNames = new String[geneSequenceFieldArray.length]; //"------------"
		
		
		for (int i = 0; i < geneSequenceFieldArray.length; i++){
			Field f = geneSequenceFieldArray[i];
			f.setAccessible(true);
			
			f.set(aGeneSequence, WorldState.rng0[5].rDouble());
			genoArray[i] = (double)geneSequenceFieldArray[i].get(aGeneSequence);
			genoArrayNames[i] = (String)geneSequenceFieldArray[i].getName();					
		}
		aGeneSequence.update();
		return aGeneSequence;
	}
	
	public static void shareResource(Organism giver, Organism receiver, Resource r, int amount)
	{//resources are transfered from one organism to another
		if (giver.findResource(r)) {
			Resource r2 = giver.dropResource(r, amount);
			receiver.addResource(r2, r2.getAmount());
			WorldState.addLogEvent("[Turn: " + Starter.getTurn() + "] " + giver.getName() + " shared " + amount + " units of " + r.getName() + " with " + receiver.getName());
		} else {}//do nothing
	}
	
	public static void tradeResource(Organism org1, Organism org2, Resource r1, Resource r2, int amount1, int amount2)
	{//resources of different kinds are transfered between two organisms
		WorldState.addLogEvent("[Turn: " + Starter.getTurn() + "] " + org1.getName() + " traded with " + org2.getName());
		shareResource(org1, org2, r1, amount1);
		shareResource(org2, org1, r2, amount2);
	}
}
