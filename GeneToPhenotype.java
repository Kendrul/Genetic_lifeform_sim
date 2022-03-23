

import java.lang.reflect.Field;
import java.util.Arrays;

import old.CulturalPhenotype;


public class GeneToPhenotype {
	
	double[] genoArray; // = new double[27];
	String[] genoArrayNames;
	double weight, limbLength, boneDiameter, limbStructuralStrength, osteocyteGenesisEfficiency;
	double muscleMass, muscleProteinSynthesis, myocyteGenesisEfficiency;
	double muscleSpeed, muscleEndurance, muscleStrength;
	double neuralMass, perceptionRadius, hostility, moodPositive, fightResponse, colonialCognitionCapacity; 
	double neurocyteGenesisEfficiency; 
	double eyeSize, cranialSize, facialLength, facialWidth; 
	double generalPhenotype; 
	double agreeability;
	//double trade;
	boolean isDebug = WorldState.isDebug;
	 
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
		agreeability = getAgreeability();
		moodPositive = getMood();
		fightResponse = getFightResponse(); 
		colonialCognitionCapacity = getColonialCognitionCapacity(); 
		weight = weight();
		eyeSize = getEyeSize();
		facialLength = getFacialLength();
		facialWidth = getFacialWidth(); 
		cranialSize = getCranialSize(); 

		osteocyteGenesisEfficiency = getOsteocyteGenesisEfficiency();
		myocyteGenesisEfficiency = getMyocyteGenesisEfficiency();
		neurocyteGenesisEfficiency = getNeurocyteGenesisEfficiency();
				
		generalPhenotype = getGeneralPhenotype();
		//CulturalPhenotype Billy_Bob_Culture = new CulturalPhenotype(generalPhenotype);
		
		//below used for debug (will freeze)
		//use this mech for pause
		/*while(true){
			int red =(int) Math.floor(generalPhenotype*255);
			System.out.println(red);
			System.out.println("*****************************************************");
			System.out.println(generalPhenotype); //1.60752171E9
		}*/ 
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
				myocyteGenesisEfficiency = genoArray[i];
			}
			if(muscleProteinSynthesis > myocyteGenesisEfficiency){
				muscleMass = muscleProteinSynthesis;
			} else {muscleMass = myocyteGenesisEfficiency;} //if more protein synthesis than available receptors, 
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
		hostility = (hostility + (1 - getMood()))/2; //(1 - getMood) will give us negative mood
		return hostility; 
	}
	
	
	public double getAgreeability(){
		agreeability = 1 - getHostility();
		
		return agreeability; 
	}
	
	//*******changes made here as well********
	//inclination for positive mood
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
	
	
	/////facial type defined below (translates as general phenotype (organisms use this for id (in addition to color))/////
	public double getEyeSize(){
		eyeSize = (perceptionRadius + colonialCognitionCapacity)/2;
		return eyeSize; 
	}
	
    public double getFacialLength(){
		facialLength = (osteocyteGenesisEfficiency*1.5 + limbLength*0.5)/2; 
		return facialLength; 
	}
    
    public double getFacialWidth(){
    	facialWidth = (osteocyteGenesisEfficiency*1.5 + limbStructuralStrength*0.5)/2;
		return facialWidth; 
	}

    //******no longer used in facial phenotype*******
    public double getCranialSize(){
		cranialSize = (neurocyteGenesisEfficiency*1.5 + colonialCognitionCapacity*0.5)/2; 
    	
		return cranialSize; 
	}
	
	
	/////regeneration potential (not strictly phenotype stuff - but related nonetheless (diff class?)/////
	//bone tissue generation/regeneration and resistance to environmental stressors
	public double getOsteocyteGenesisEfficiency(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Osteo") && genoArrayNames[i].contains("Multiplier")){
				osteocyteGenesisEfficiency = genoArray[i]*2;
			} 
			if(genoArrayNames[i].contains("Osteo") && genoArrayNames[i].contains("Protein")){
				osteocyteGenesisEfficiency += genoArray[i]; // would capture this 3 times (cause 3 proteins)
			}
		}
		osteocyteGenesisEfficiency /= 5; 
		return osteocyteGenesisEfficiency; 
	}
	
	
	//muscle tissue generation/regeneration and resistance to environmental stressors
	public double getMyocyteGenesisEfficiency(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Myo") && genoArrayNames[i].contains("Multiplier")){
				myocyteGenesisEfficiency = genoArray[i]*2;
			} 
			if(genoArrayNames[i].contains("Myo") && genoArrayNames[i].contains("Protein")){
				myocyteGenesisEfficiency += genoArray[i]; // would capture this 3 times (cause 3 proteins)
			}
		}
		myocyteGenesisEfficiency /= 5;
		return myocyteGenesisEfficiency; 
	}
	
	
	public double getNeurocyteGenesisEfficiency(){
		for (int i = 0; i < genoArray.length; i++){
			if(genoArrayNames[i].contains("Neuro") && genoArrayNames[i].contains("Multiplier")){
				neurocyteGenesisEfficiency = genoArray[i];
			} 
			if(genoArrayNames[i].contains("Neuro") && genoArrayNames[i].contains("Protein")){
				myocyteGenesisEfficiency += genoArray[i]; // would capture this 8 times (cause 8 neuro proteins)
			}
		}
		neurocyteGenesisEfficiency /= 9; 
		return neurocyteGenesisEfficiency; 
	}

	
	//was going to use a hash function, but made my on visual-id-code-generator (aka generalPhenotype)
	//highest priority feature is eye size
	//lowest priority feature is cranial size (hence recognitionPriority getting smaller with each iteration)
	public double getGeneralPhenotype(){
		int colorValue;
		if(eyeSize < 0.25){
			colorValue = 1;
		}
		else if(eyeSize >= 0.25 && eyeSize < 0.5){
			colorValue = 2;
		}
		else if(eyeSize >= 0.5 && eyeSize < 0.75){
			colorValue = 3;
		}
		else{
			colorValue = 4;
		}
		
		/*double[] generalPhenotypeArray = {eyeSize, facialLength, facialWidth}; //*****got rid of cranialSize in this array
		for(double i = 0, recognitionPriority = 1; i < generalPhenotypeArray.length; i++, recognitionPriority -= 0.2){
			generalPhenotype += generalPhenotypeArray[(int)i]*recognitionPriority;
		}
		generalPhenotype /= (generalPhenotypeArray.length);
		//generalPhenotype = Arrays.hashCode(genoArray); 		*/
		generalPhenotype = colorValue; 
		return generalPhenotype;  //*********this name should be changed to colorPhenotype used by entities to identify kin
	}
}
