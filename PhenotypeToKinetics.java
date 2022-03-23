import java.lang.reflect.Field;


public class PhenotypeToKinetics {
	double[] phenoArray;
	String[] phenoArrayNames;
	double speedOfLocomotion; 
	double fightingEffectiveness; 	
	double resourceCarryingCapacity; 
	boolean isDebug = WorldState.isDebug;
	
	//fighting effectiveness sub categories
	double crit;
	double dodge;
	
	//health related psychological, physiological or physical dynamics/kinetics
	double maxHp; 
	
	PhenotypeToKinetics(double[] phenoArray, String[] phenoArrayNames){
		this.phenoArray = phenoArray; 
		this.phenoArrayNames = phenoArrayNames; 
		
		speedOfLocomotion = getSpeedOfLocomotion();
		fightingEffectiveness = getFightingEffectiveness();
		resourceCarryingCapacity = getResourceCarryingCapacity();
		crit = getCrit();
		dodge = getDodge();
		maxHp = getMaxHp();
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
	
	/////fighting sub categories/////
	public double getCrit(){
		for (int i = 0; i < phenoArray.length; i++){
			if(phenoArrayNames[i].contains("neuralMass")){
				//if (isDebug) System.out.println(phenoArray[i]);
				crit += phenoArray[i];
			}
			if(phenoArrayNames[i].contains("fightingEffectiveness")){
				//if (isDebug) System.out.println(phenoArray[i]);
				crit += phenoArray[i];
			}
		}
		crit /= 2; 
		return crit; 
	}
	
	
	public double getDodge(){
			dodge = (getSpeedOfLocomotion()*2 + getCrit())/3; 
		return dodge; 
	}
    /////----------------------/////
	
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
				resourceCarryingCapacity += phenoArray[i]*0.5;
			}
		}
		resourceCarryingCapacity /= 5.5;
		return resourceCarryingCapacity; 
	}
	
	//psychological, physical and physiological dynamics/kinetics that allow
	//an organism to resist environmental stress (i.e. capacity to withstand damage)
	public double getMaxHp(){
		for (int i = 0; i < phenoArray.length; i++){
			if(phenoArrayNames[i].contains("osteocyte")){
				//if (isDebug) System.out.println(phenoArray[i]);
				maxHp += phenoArray[i];
			}
			if(phenoArrayNames[i].contains("myocyte")){
				//if (isDebug) System.out.println(phenoArray[i]);
				maxHp += phenoArray[i];
			}	
			if(phenoArrayNames[i].contains("neurocyte")){
				//if (isDebug) System.out.println(phenoArray[i]);
				maxHp += phenoArray[i];
			}
		}
		maxHp /= 3; 
		return maxHp; 
	}
}
