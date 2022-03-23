

import java.lang.reflect.Field;


public class PhenotypeToKinetics {
	double[] phenoArray;
	String[] phenoArrayNames;
	double speedOfLocomotion; 
	double fightingEffectiveness; 
	double resourceCarryingCapacity; 
	boolean isDebug = WorldState.isDebug;
	
	/**
	 * 
	 * @param phenoArray
	 * @param phenoArrayNames
	 */
	PhenotypeToKinetics(double[] phenoArray, String[] phenoArrayNames){
		this.phenoArray = phenoArray; 
		this.phenoArrayNames = phenoArrayNames; 
		
		speedOfLocomotion = getSpeedOfLocomotion();
		fightingEffectiveness = getFightingEffectiveness();
		resourceCarryingCapacity = getResourceCarryingCapacity();
		//if (isDebug) print();
	}
	
	
	public void print(){
		//System.out.println(speedOfLocomotion); 
		//System.out.println(fightingEffectiveness);
		//System.out.println(resourceCarryingCapacity);
	}
	
	public double getSpeedOfLocomotion(){
		for (int i = 0; i < phenoArray.length; i++){
			if(phenoArrayNames[i].contains("limbLength")){
				//if (isDebug) System.out.println(phenoArray[i]);
				speedOfLocomotion += phenoArray[i]*2;
			}
			if(phenoArrayNames[i].contains("muscleSpeed")){
				//if (isDebug)System.out.println(phenoArray[i]);
				speedOfLocomotion += phenoArray[i]*4;
			}
			if(phenoArrayNames[i].contains("muscleStrength")){
				//if (isDebug)System.out.println(phenoArray[i]);
				speedOfLocomotion += phenoArray[i]*1.5;
			}
			if(phenoArrayNames[i].contains("weight")){
				speedOfLocomotion += phenoArray[i]*0.5;
			}
		}
		speedOfLocomotion /= 8.5; 
		return speedOfLocomotion;
	}
	
	public double getFightingEffectiveness(){
		for (int i = 0; i < phenoArray.length; i++){
			if(phenoArrayNames[i].contains("limbStructuralStrength")){
				fightingEffectiveness += phenoArray[i]*1.5;
			}
			if(phenoArrayNames[i].contains("muscleStrength")){
				fightingEffectiveness += phenoArray[i]*3;
			}	
			if(phenoArrayNames[i].contains("muscleSpeed")){
				fightingEffectiveness += phenoArray[i]*3;
			}
			if(phenoArrayNames[i].contains("weight")){
				fightingEffectiveness += phenoArray[i];
			}
		}
		fightingEffectiveness /= 8.5; 
		return fightingEffectiveness;
	}
	
	public double getResourceCarryingCapacity(){
		for (int i = 0; i < phenoArray.length; i++){
			if(phenoArrayNames[i].contains("muscleStrength")){
				//if (isDebug) System.out.println(phenoArray[i]);
				resourceCarryingCapacity += phenoArray[i]*2;
			}
			if(phenoArrayNames[i].contains("muscleEndurance")){
				//if (isDebug) System.out.println(phenoArray[i]);
				resourceCarryingCapacity += phenoArray[i]*3;
			}	
			if(phenoArrayNames[i].contains("limbLength")){
				//if (isDebug) System.out.println(phenoArray[i]);
				resourceCarryingCapacity -= phenoArray[i];
			}
		}
		resourceCarryingCapacity /= 4;
		return resourceCarryingCapacity; 
	}
}
