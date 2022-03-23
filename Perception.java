import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import java.util.ArrayList;

public class Perception{
	OrganismGFX organism;
	int locationX;
	int locationY;
	Patch PatchAtLocationPoint;
	int perceptionRadius = 1; 
	Patch[] perceptionRadiusBlocks;
	private boolean isDebug = WorldState.isDebug;
	
	
	Perception(OrganismGFX thisOrganism){
		this.organism = thisOrganism;			
	}
	
	//returns the Patch at current block (must change to Patchs around organism current location block)
	public void currentPointOfView() throws AWTException{
		Point locationPoint = organism.getPoint(); //gets the point location of the organism
	    locationX = locationPoint.x;
	    locationY = locationPoint.y;
		//PatchAtLocationPoint = getPixelPatch(locationX, locationY); 
	    
	    if(perceptionRadius == 0){
	    	Patch[] perceptionRadiusBlocks1 = new Patch[1];
	    	perceptionRadiusBlocks = perceptionSpace(perceptionRadiusBlocks1, 0, locationX, locationY);
	    }
	    if(perceptionRadius == 1){
	    	Patch[] perceptionRadiusBlocks2 = new Patch[9];
	    	perceptionRadiusBlocks = perceptionSpace(perceptionRadiusBlocks2, 1, locationX, locationY);
	    }
	   
	   if(isDebug){ 	    
	    for(int i = 0; i < perceptionRadiusBlocks.length; i++){
	    	if (perceptionRadiusBlocks[i] != null) System.out.println("(" + perceptionRadiusBlocks[i].getX() + ", " + perceptionRadiusBlocks[i].getY()+")");
	    	else System.out.println("null");
	    	System.out.println(i);
	    } 
	   }
	}
	
	//setting the organism's perception radius
	public void setPerceptionRadius(int setPerceptionRadius){
		perceptionRadius = setPerceptionRadius; 
	}
	
	//contains information all the blocks within the organism's perception range
	public Patch[] perceptionSpace(Patch[] perceptionRadiusBlocks, int perceptionRadius, int locationX, int locationY) throws AWTException{
		
		
		//sees only its own patch 
		if(perceptionRadius == 0){		
			PatchAtLocationPoint = getPixelPatch(locationX, locationY);
			perceptionRadiusBlocks[0] = PatchAtLocationPoint;
		}
		
		 
		//sees it's own patch and the 8 patches surrounding it
		int tempX, tempY;
		if(perceptionRadius == 1){
			PatchAtLocationPoint = getPixelPatch(locationX, locationY);	
			perceptionRadiusBlocks[0] = PatchAtLocationPoint;

			
			tempY = locationY - 1; 
			tempY = tempY - 1;
			PatchAtLocationPoint = getPixelPatch(locationX, tempY);		
			perceptionRadiusBlocks[1] = PatchAtLocationPoint;

			
			tempX = locationX; 
			tempY = locationY;
			tempX = tempX + 1; tempY = tempY - 1;
			PatchAtLocationPoint = getPixelPatch(tempX, tempY);
			perceptionRadiusBlocks[2] = PatchAtLocationPoint;

			
			tempX = locationX;
			tempX = tempX + 1;
			PatchAtLocationPoint = getPixelPatch(tempX, locationY);
			perceptionRadiusBlocks[3] = PatchAtLocationPoint;

			
			tempX = locationX; 
			tempY = locationY;
			tempX = tempX + 1; tempY = tempY + 1;
			PatchAtLocationPoint = getPixelPatch(tempX, tempY);
			perceptionRadiusBlocks[4] = PatchAtLocationPoint;

			
			tempY = locationY;
			tempY = tempY + 1;
			PatchAtLocationPoint = getPixelPatch(locationX, tempY);
			perceptionRadiusBlocks[5] = PatchAtLocationPoint;

			
			tempX = locationX; 
			tempY = locationY;
			tempX = tempX - 1; tempY = tempY + 1;
			PatchAtLocationPoint = getPixelPatch(tempX, tempY);
			perceptionRadiusBlocks[6] = PatchAtLocationPoint;

			
			tempX = locationX;
			tempX = tempX - 1;
			PatchAtLocationPoint = getPixelPatch(tempX, locationY);
			perceptionRadiusBlocks[7] = PatchAtLocationPoint;

			
			tempX = locationX; 
			tempY = locationY;
			tempX = tempX - 1; tempY = tempY - 1;
			PatchAtLocationPoint = getPixelPatch(tempX, tempY);
			perceptionRadiusBlocks[8] = PatchAtLocationPoint;
		
		}		
		return perceptionRadiusBlocks;		
	}

	//returns the Patch at a block
	public Patch getPixelPatch(int x, int y) throws AWTException {
	    //Robot robot = new Robot();   
	    //return robot.getPixelPatch(locationX, locationY);
		if ((x < 0) || (x >= WorldState.pLnum) || (y < 0) || (y >= WorldState.pWnum)) return null;
		return Starter.getGrid().getPatch(x,y);
	}
	
	public Patch findFoodPatch()
	{//checks for a nearby patch that has food and is not fully occupied
		Patch optimalSpot = null;
		for(int i = 1; i < perceptionRadiusBlocks.length; i++)
		{//skip first spot, we already checked it
			if (perceptionRadiusBlocks[i] == null) continue; //might have nulls because of boundary
			if ((perceptionRadiusBlocks[i].hasFood()) && (perceptionRadiusBlocks[i].population() < WorldState.densityAllowance))
			{
				if ((optimalSpot == null) || (optimalSpot.getTheR().getAmount() < perceptionRadiusBlocks[i].getTheR().getAmount())){
						optimalSpot = perceptionRadiusBlocks[i];
				}
			}
		}
		return optimalSpot;
	}
	
	public Patch findFoodOrg()
	{//checks for a nearby patch that has food and is not fully occupied
		Patch optimalSpot = null;
		for(int i = 1; i < perceptionRadiusBlocks.length; i++)
		{//skip first spot, we already checked it
			if (perceptionRadiusBlocks[i] == null) continue; //might have nulls because of boundary
			if ((perceptionRadiusBlocks[i].isHasEntity()) && (perceptionRadiusBlocks[i].orgHasFood()) && (perceptionRadiusBlocks[i].population() < WorldState.densityAllowance))
			{
				if ((optimalSpot == null) || (optimalSpot.orgWithMostFood().potentialEnergy() < perceptionRadiusBlocks[i].orgWithMostFood().potentialEnergy())){
						optimalSpot = perceptionRadiusBlocks[i];
				}
			}
		}
		return optimalSpot;
	}
	
	public Patch findMate()
	{//checks for a nearby patch that has food and is not fully occupied
		Patch optimalSpot = null;
		for(int i = 1; i < perceptionRadiusBlocks.length; i++)
		{//skip first spot, we already checked it
			if (perceptionRadiusBlocks[i] == null) continue; //might have nulls because of boundary
			if ((perceptionRadiusBlocks[i].isHasEntity())  && (perceptionRadiusBlocks[i].population() < WorldState.densityAllowance))
			{
				if ((optimalSpot == null) || (optimalSpot.isHasEntity()) && (optimalSpot.getTheE().getRpriority() > WorldState.NONE)){//add extra conditionals here about reproductive fitness
						optimalSpot = perceptionRadiusBlocks[i];
				}
			}
		}
		return optimalSpot;
	}
	
	public ArrayList<Organism> pack(){
		ArrayList<Organism> o = new ArrayList<Organism>();
		for(int i = 1; i < perceptionRadiusBlocks.length; i++)
		{//skip first spot, we already checked it
			if (perceptionRadiusBlocks[i] == null) continue; //might have nulls because of boundary
			if (perceptionRadiusBlocks[i].isHasEntity())
			{
				for(int j = 0; j < perceptionRadiusBlocks[i].theE.size(); j++)
				{
					if (perceptionRadiusBlocks[i].getTheE(j) != organism.getOwner())
						o.add(perceptionRadiusBlocks[i].getTheE(j));
				}
			}
		}
		return o;
	}
	
	public ArrayList<Integer> mobCount(){
		ArrayList<Integer> count = new ArrayList<Integer>();
		count.add(-1); //first spot is irrelevant
		for(int i = 1; i < perceptionRadiusBlocks.length; i++)
		{
			if (perceptionRadiusBlocks[i] == null) {
				count.add(-1);
				continue; //might have nulls because of boundary
			}
			if (perceptionRadiusBlocks[i].isHasEntity())
			{
				count.add(perceptionRadiusBlocks[i].theE.size());
			}
		}
		return count;
	}
	
	public Patch getTarget(int spot)
	{
		if (spot < 0 || spot > perceptionRadiusBlocks.length) return null; //error check redundancy
		else return perceptionRadiusBlocks[spot];
	}
}