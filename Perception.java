import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;

public class Perception{
	OrganismGFX organism;
	int locationX;
	int locationY;
	Color colorAtLocationPoint;
	int perceptionRadius = 1; 
	Color[] perceptionRadiusBlocks;
	
	
	Perception(OrganismGFX thisOrganism){
		this.organism = thisOrganism;			
	}
	
	//returns the color at current block (must change to colors around organism current location block)
	public void currentPointOfView() throws AWTException{
		Point locationPoint = organism.getPoint(); //gets the point location of the organism
	    locationX = locationPoint.x;
	    locationY = locationPoint.y;
		//colorAtLocationPoint = getPixelColor(locationX, locationY); 
	    
	    if(perceptionRadius == 0){
	    	Color[] perceptionRadiusBlocks1 = new Color[1];
	    	perceptionRadiusBlocks = perceptionSpace(perceptionRadiusBlocks1, 0, locationX, locationY);
	    }
	    if(perceptionRadius == 1){
	    	Color[] perceptionRadiusBlocks2 = new Color[9];
	    	perceptionRadiusBlocks = perceptionSpace(perceptionRadiusBlocks2, 1, locationX, locationY);
	    }
	   
	    	    
	    for(int i = 0; i < perceptionRadiusBlocks.length; i++){
	    	System.out.println(perceptionRadiusBlocks[i]);	
	    	System.out.println(i);
	    }  
	}
	
	//setting the organism's perception radius
	public void setPerceptionRadius(int setPerceptionRadius){
		perceptionRadius = setPerceptionRadius; 
	}
	
	//contains information all the blocks within the organism's perception range
	public Color[] perceptionSpace(Color[] perceptionRadiusBlocks, int perceptionRadius, int locationX, int locationY) throws AWTException{
		
		
		//sees only its own patch 
		if(perceptionRadius == 0){		
			colorAtLocationPoint = getPixelColor(locationX, locationY);
			perceptionRadiusBlocks[0] = colorAtLocationPoint;
		}
		
		 
		//sees it's own patch and the 8 patches surrounding it
		int tempX, tempY;
		if(perceptionRadius == 1){
			colorAtLocationPoint = getPixelColor(locationX, locationY);	
			perceptionRadiusBlocks[0] = colorAtLocationPoint;

			
			tempY = locationY - 1; 
			tempY = tempY - 1;
			colorAtLocationPoint = getPixelColor(locationX, tempY);		
			perceptionRadiusBlocks[1] = colorAtLocationPoint;

			
			tempX = locationX; 
			tempY = locationY;
			tempX = tempX + 1; tempY = tempY - 1;
			colorAtLocationPoint = getPixelColor(tempX, tempY);
			perceptionRadiusBlocks[2] = colorAtLocationPoint;

			
			tempX = locationX;
			tempX = tempX + 1;
			colorAtLocationPoint = getPixelColor(tempX, locationY);
			perceptionRadiusBlocks[3] = colorAtLocationPoint;

			
			tempX = locationX; 
			tempY = locationY;
			tempX = tempX + 1; tempY = tempY + 1;
			colorAtLocationPoint = getPixelColor(tempX, tempY);
			perceptionRadiusBlocks[4] = colorAtLocationPoint;

			
			tempY = locationY;
			tempY = tempY + 1;
			colorAtLocationPoint = getPixelColor(locationX, tempY);
			perceptionRadiusBlocks[5] = colorAtLocationPoint;

			
			tempX = locationX; 
			tempY = locationY;
			tempX = tempX - 1; tempY = tempY + 1;
			colorAtLocationPoint = getPixelColor(tempX, tempY);
			perceptionRadiusBlocks[6] = colorAtLocationPoint;

			
			tempX = locationX;
			tempX = tempX - 1;
			colorAtLocationPoint = getPixelColor(tempX, locationY);
			perceptionRadiusBlocks[7] = colorAtLocationPoint;

			
			tempX = locationX; 
			tempY = locationY;
			tempX = tempX - 1; tempY = tempY - 1;
			colorAtLocationPoint = getPixelColor(tempX, tempY);
			perceptionRadiusBlocks[8] = colorAtLocationPoint;
		
		}		
		return perceptionRadiusBlocks;		
	}

	//returns the color at a block
	public Color getPixelColor(int x, int y) throws AWTException {
	    Robot robot = new Robot();   
	    return robot.getPixelColor(locationX, locationY);
	}
}