

import java.lang.reflect.Field;


public class GeneToPhenotype {
	
	double[] genoArray; // = new double[27];
	String[] genoArrayNames;
	double weight, limbLength, boneDiameter, limbStructuralStrength;
	double muscleMass, muscleProteinSynthesis, muscleGenesisEfficiency;
	double muscleSpeed, muscleEndurance, muscleStrength;
	double neuralMass, perceptionRadius, hostility, moodPositive, fightResponse, colonialCognitionCapacity; 
	//double trade;
	boolean isDebug = WorldState.isDebug;
	 /**
	  * 
	  * @param genoArray
	  * @param genoArrayNames
	  * @throws IllegalArgumentException
	  * @throws IllegalAccessException
	  */
	public GeneToPhenotype(double[] genoArray, String[] genoArrayNames) throws IllegalArgumentException, IllegalAccessException
	{
		this.genoArray = genoArray; 
		this.genoArrayNames = genoArrayNames;
		limbLength = getLimbLength();
		limbStructuralStrength = getLimbStructuralStrength();
		muscleMass = getMuscleMass();
		muscleSpeed = getMuscleSpeed();
		muscleEndurance = getMuscleEndurance(); 
		muscleStrength = getMuscleStrength();
		neuralMass = getNeuralMass();
		perceptionRadius = getPerceptionRadius(); 
		hostility = getHostility(); 
		moodPositive = getMood();
		fightResponse = getFightResponse(); 
		colonialCognitionCapacity = getColonialCognitionCapacity(); 
		weight = weight();
		
		//if (isDebug) print();
		//phenoArrayGeneration(); 
	}
	
	public PhenotypeToKinetics phenoArrayGeneration() throws IllegalArgumentException, IllegalAccessException{ 
	    Class aClass1 = this.getClass();
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
				phenoArray[phenoArrayIterator] = (double)phenoStartFieldArray[i].get(this);
				phenoArrayNames[phenoArrayIterator] = (String)phenoStartFieldArray[i].getName();
				phenoArrayIterator++; 
			}		
		}
		if (isDebug){
			for (int i = 0; i < phenoArray.length; i++){
			
				System.out.println(phenoArray[i]);
				System.out.println(phenoArrayNames[i]);
			}
		}
		
		PhenotypeToKinetics Billy_Bob_Fuckface_Pheno = new PhenotypeToKinetics(phenoArray, phenoArrayNames);
		return Billy_Bob_Fuckface_Pheno;
	}
	
	
	public void print(){	
		System.out.printf("limbL: %s\nboneD: %s\nlimbSS: %s\nmuscleM: %s\n", limbLength, boneDiameter, limbStructuralStrength, muscleMass);
		System.out.printf("muscleSp: %s\nmuscleE: %s\nmuscleSt: %s\n", muscleSpeed, muscleEndurance, muscleStrength);
		System.out.printf("neuralMass: %s\nperceptionRadius: %s\nhostility: %s\n", neuralMass, perceptionRadius, hostility);
		System.out.printf("mood: %s\ncolonialCognitionCapacity: %s\n", moodPositive, colonialCognitionCapacity);
		//System.out.println(muscleStrength);	
		//System.out.println(muscleEndurance);
	}	
	
	 
	public double getLimbLength(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Osteo") && genoArrayNames[i].contains("Length")){
				limbLength = genoArray[i];
			}
			
			//if(retval = str1.contains(cs1)){
			//limbLength = 	
		}	
		return limbLength;
	}
	
	
	public double getLimbStructuralStrength(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Osteo") && genoArrayNames[i].contains("Width")){
				boneDiameter = genoArray[i];
			}
			limbStructuralStrength = limbLength/boneDiameter; //ratio between length and width determines
			//if(retval = str1.contains(cs1)){                //structural strength
			//limbLength = 	
		}	
		return limbStructuralStrength;
	}
	
		
	public double getMuscleMass(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Myo") && genoArrayNames[i].contains("Protein")){
				muscleProteinSynthesis = genoArray[i];
			}
			if(genoArrayNames[i].contains("Myo") && genoArrayNames[i].contains("Receptor")){
				muscleGenesisEfficiency = genoArray[i];
			}
			if(muscleProteinSynthesis > muscleGenesisEfficiency){
				muscleMass = muscleProteinSynthesis;
			} else {muscleMass = muscleGenesisEfficiency;} //if more protein synthesis than available receptors, 
			                                               //then protein gets wasted; in the opposing
														   //case, the receptors don't get used
														   //thus the lower number determines ultimate muscle gowth and mass
		}	
		return muscleMass;
	}
	
		 
	public double getMuscleSpeed(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Myo") && genoArrayNames[i].contains("Fast_Twitch")){
				muscleSpeed = genoArray[i];
			}
			 	
		}
		muscleSpeed /= muscleMass; //the bigger the ratio, the greater the portion of the 
		//if(retval = str1.contains(cs1)){    //muscle that has fast twitch fibers; thus the faster it will be
		//limbLength =
		return muscleSpeed;
	}
	
	
	public double getMuscleEndurance(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Myo") && genoArrayNames[i].contains("Slow_Twitch")){
				//System.out.println(genoArray[i]);
				muscleEndurance = genoArray[i];				
			}
		}
		muscleEndurance += muscleMass * 0.5; //a little muscle mass is good, but too much adds weight and reduces endurance
		muscleEndurance /= 1.5;
		return muscleEndurance; 
	}
	
		
	public double getMuscleStrength(){	
		//System.out.println(muscleEndurance);
		muscleStrength = (muscleSpeed * 2 + muscleEndurance) / 3; //muscleSpeed * 2 because fast twitch are stronger (generalized to twice as strong) 
		//System.out.println(muscleStrength);                       //above divided by 3, since 3 can be the maximal value 
			//if(retval = str1.contains(cs1)){    
			//limbLength = 		
		return muscleStrength;
	}
	
	
	public double getNeuralMass(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Neuro") && genoArrayNames[i].contains("3d")){
				neuralMass = genoArray[i];
			}
			 	
		}
		return neuralMass;
	}
	
	
    //the weightGrowthMultiplier may (in the future) be used to ascertain the weight of an organism
	public double weight()
	{ 
		weight = (limbLength + boneDiameter*2 + muscleMass*3 + neuralMass)/7;		
		return weight;	
	}
	
	public double getPerceptionRadius(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Neuro") && genoArrayNames[i].contains("Sensory")){
				perceptionRadius = genoArray[i];
			} 
		}
		perceptionRadius = (neuralMass + perceptionRadius)/2;
		return perceptionRadius; 
	}
	
	
	//emotional phenotype and cognitive phenotype characterisitcs below
	public double getHostility(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Neuro") && genoArrayNames[i].contains("Hostile")){
				hostility = genoArray[i];
			} 
		}
		hostility = (neuralMass + hostility)/2;
		return hostility; 
	}
	
	
	public double getMood(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Neuro") && genoArrayNames[i].contains("MoodPositive")){
				moodPositive = genoArray[i];
			} 
		}
		moodPositive = (neuralMass + moodPositive)/2;
		return moodPositive; 
	}
	

	//fight or flight
	public double getFightResponse(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Neuro") && genoArrayNames[i].contains("Fight")){
				fightResponse = genoArray[i];
			} 
		}
		fightResponse = (hostility + (1-moodPositive) + (fightResponse * 2))/4;
		return fightResponse; 
	}
	
	
	public double getColonialCognitionCapacity(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Neuro") && genoArrayNames[i].contains("SelfProjection")){
				colonialCognitionCapacity = genoArray[i];
			} 
		}
		colonialCognitionCapacity = (neuralMass + colonialCognitionCapacity)/2;
		return colonialCognitionCapacity; 
	}
	
}


