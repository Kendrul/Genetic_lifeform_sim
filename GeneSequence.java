
/* GeneSequence.java
 * CPSC 565 W2016: Project
 * Emil Emilov-Dulguerov and Jason Schneider
 * This class contains information on the genetics of an organism
 */
public class GeneSequence {
	
	//Osteogenesis development and regulation
    double gene_Osteo_Pathway_Multiplier = 0.5;
	double gene_Osteo_Length_ECM = 0.4;
	double gene_Osteo_Width_ECM = 1.0- gene_Osteo_Length_ECM;
	
	double gene_Osteo_Morphogenetic_Protein1 = 1; 
	double gene_Osteo_Morphogenetic_Protein2 = 0;
	double gene_Osteo_Morphogenetic_Protein3 = 0.8; 
	
/*	double gene_Osteo_Morphogenetic_ReceptorA = 0.75; 
	double gene_Osteo_Morphogenetic_ReceptorB = 0.75;
	double gene_Osteo_Morphogenetic_ReceptorC = 0.2; */
		
	////////////////////////////////////////
	
	//Myogenesis development and regulation
	double gene_Myo_Pathway_Multiplier = 0.9;
	double gene_Myo_Fast_Twitch_ECM = 0.3;
	double gene_Myo_Slow_Twitch_ECM = 1.0- gene_Myo_Fast_Twitch_ECM; //total is 1, so the more there are fast twitch the less there
	                                                                //are slow twitch (and vice versa) 
	double gene_Myo_Morphogenetic_Protein1 = 0;
	double gene_Myo_Morphogenetic_Protein2 = 0;
	double gene_Myo_Morphogenetic_Protein3 = 0.5;
	
	/*double gene_Myo_Morphogenetic_ReceptorA = 0.5; 
	double gene_Myo_Morphogenetic_ReceptorB = 0.5;
	double gene_Myo_Morphogenetic_ReceptorC = 0.5; */
	////////////////////////////////////////
	
	//Neurogenesis development and regulation
	double gene_Neuro_Pathway_Multiplier = 1;
	double gene_Neuro_3d_ECM = 0.8; //potential for brain growth 
	//double gene_Neuro_Width_ECM = 0.86;
	
    double gene_Neuro_Morphogenetic_Protein_SensoryResolution = 0.3; //if this up then perception radius up
    
    //hereditary influence for neutral or hostile response with respect to phenotypically dissimilar dynamic entities
    //put another way, phenotypically dissimilar entities are instinctively perceived as hostile and therefore the
    //response toward them is a hostile one 
    double gene_Neuro_Morphogenetic_Protein_Hostile = 0.6;
    double gene_Neuro_Morphogenetic_Protein_Neutral = 1.0- gene_Neuro_Morphogenetic_Protein_Hostile;
    double gene_Neuro_Morphogenetic_Protein_SelfProjectionPotential = 0.3;
    double gene_Neuro_Morphogenetic_Protein_MoodNegative = 0.3; 
    double gene_Neuro_Morphogenetic_Protein_MoodPositive = 1.0- gene_Neuro_Morphogenetic_Protein_MoodNegative;
    
    //hereditary influence for fight or flight response toward those dynamic entities that are perceived as hostile
    double gene_Neuro_Morphogenetic_Protein_Fight = 0.3;
    double gene_Neuro_Morphogenetic_Protein_Flight = 1.0- gene_Neuro_Morphogenetic_Protein_Fight;
    
    
    //dermal development and regulation
    //***********************put some value for color*****************************//
    //!!!!!!!!!!                                                        !!!!!!!!!!//
    
		
   /* double gene_Neuro_Morphogenetic_Receptor_SensoryResolution = 0.7; 
	double gene_Neuro_Morphogenetic_ReceptorB = 0.7;
	double gene_Neuro_Morphogenetic_ReceptorC = 0.7;
	double gene_Neuro_Morphogenetic_ReceptorA = 0.7; 
	double gene_Neuro_Morphogenetic_CatalystA1 = 0.7;
	double gene_Neuro_Morphogenetic_CatalystB1 = 0.7; */
	///////////////////////////////////////
    
   /* public void set_geneSequence(){
    	if(genoArrayNames[i].contains("Osteo") && genoArrayNames[i].contains("Width")){
    		gene_Osteo_Pathway_Multiplier = genoArray[i];
		}
    	
    }*/
    
    public void update()
    {
    	gene_Osteo_Width_ECM = 1.0- gene_Osteo_Length_ECM;
    	gene_Myo_Slow_Twitch_ECM = 1.0- gene_Myo_Fast_Twitch_ECM;
    	gene_Neuro_Morphogenetic_Protein_Neutral = 1.0- gene_Neuro_Morphogenetic_Protein_Hostile;
    	gene_Neuro_Morphogenetic_Protein_MoodPositive = 1.0- gene_Neuro_Morphogenetic_Protein_MoodNegative;
    	gene_Neuro_Morphogenetic_Protein_Flight = 1.0- gene_Neuro_Morphogenetic_Protein_Fight;
    }
}
