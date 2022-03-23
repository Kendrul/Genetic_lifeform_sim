import java.awt.Color;

public class CulturalPhenotype {
	int red, blue, green; 
	double generalPhenotype;
	Color culturalPhenotype;
	
	CulturalPhenotype(double generalPhenotype){	
		//red is dependent on generalPhenotype (facial features that function as id)
		//this color can be used on one of the two circles that represent each organism's body
		this.generalPhenotype = generalPhenotype; 
		red =(int) Math.floor(generalPhenotype*255); //will give some value between 0 - 255 for red color 
	}
	
	public void setCulturalPhenotype(int b, int g){
		//blue and green are based on abstract cultural affiliations (no need to define them for now)
		//what we need to note is that they simply affect hostility between organisms 
		blue = b;
		green = g; 	
		culturalPhenotype = new Color(red,green,blue);
	}
	
	public Color getCulturalPhenotype(){
		return culturalPhenotype; 
	}
}
