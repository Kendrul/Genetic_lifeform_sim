//For the storage of misc information details
public class OrgInfo {
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

