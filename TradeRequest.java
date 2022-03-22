/*TradeRequest.java
 * CPSC 565 W2016: Project
 * Jason Schneider and Emil Emilov-Dulguerov
 * This class contains the details to facilitate the exchange-of/request-for a resource between two organisms
 * 
 */
public class TradeRequest {
	Resource resource;
	Organism asker;
	int amount;
	
	public TradeRequest(Resource r, Organism o, int a)
	{
		resource =r;
		asker = o;
		amount = a;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Organism getAsker() {
		return asker;
	}

	public void setAsker(Organism asker) {
		this.asker = asker;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
}
