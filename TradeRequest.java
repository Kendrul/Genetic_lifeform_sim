
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
