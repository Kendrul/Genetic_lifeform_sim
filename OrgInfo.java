/*OrgInfo.java
 * CPSC 565 W2016: Project
 * Jason Schneider and Emil Emilov-Dulguerov
 *	For the storage of Organism identifying information details 
 */

//
public class OrgInfo {
	public String simulationRun;
	public String number;
	public String name;
	public String age = "";
	public String generation;
	//for showing their cycle through generations
	//g = random creation, b = repro birth, r = reproduced, d = died
	public String cycle = ""; 
	public String birthTurn;
	public String deathTurn = "Alive";
	public String deathReason = "Survived";
	public String parents = "Simulator";
	

	
	public OrgInfo(int num, String n, int g, int bt)
	{
		//simulationRun = Integer.toString(Starter.getSimRun());
		number = Integer.toString(num);
		name = n;
		generation = Integer.toString(g);
		birthTurn = Integer.toString(bt);
	}

	public void calcAge(){
		int now, start = Integer.parseInt(birthTurn);
		if (deathTurn.equals("Alive")) now = Starter.getTurn();
		else now = Integer.parseInt(deathTurn);
		age = Integer.toString(now - start);
	}


}

