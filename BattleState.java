import java.util.Random;


public class BattleState {
	
	Entity owner;
	boolean isDebug;
	boolean runner;
	Random rng;
	int teamNumber;
	
	public int getTeamNumber() {
		return teamNumber;
	}


	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}


	public BattleState(Entity theEntity, boolean debugModeOn)
	{
		owner = theEntity;
		isDebug = debugModeOn;
		runner = false;
		rng = new Random();
	}
	

	public void debugSet(boolean setDebugMode)
	{
		isDebug = setDebugMode;
	}
	
	public void plant(int seed)
	{
		rng = new Random(seed);
	}
	
	public boolean chooseAttack()
	{//this is where the entity decides if it will fight or flight
		//if the entity is too wounded, it does nothing
		if (owner.getWoundPenalty() < 0.75) {
		double roll = doubleDice();
		if (owner.getWoundPenalty() > 0.5) roll -= (owner.getWoundPenalty() * 2); //wounded animal more likely to run
		if (roll < owner.getFlightTendency()) {
			//chosen to run
			return false;
		}//fight chosen
		else return true;		
			} else return true; //too wounded to run
	}
	
	private double doubleDice()
	{
		return rng.nextDouble();
	}
	
	public int attackAction()
	{
		if (owner.getWoundPenalty() < 0.75) {
		double roll = doubleDice();
		
		if (roll < owner.getCrit()) {
			return owner.getAttack() * 2;
		}
		else return owner.getAttack();
		//too wounded to fight back
		} else {
			if (isDebug) System.out.println(owner.getName() + " is too wounded to react.");
			return 0;
		}
	}
	
	public boolean survivedAttack(int damage, BattleState attacker)
	{
		if (damage <= 0) return true;
		double roll = doubleDice();
		if (roll >= owner.getDodge()) {
		owner.setHp(owner.getHp() - damage);
		owner.setWoundPenalty(1.0 - ((double) owner.getHp() / (double) owner.getMaxHp()));
		//if (isDebug) System.out.println(name + " has a woundPenalty level of " + woundPenalty + ".");
		if (isDebug) System.out.println(attacker.getName() + " attacks " + owner.getName() + " for " + damage + " health, " + owner.getHp() + " remaining.");
		//true means entity survived, false means entity has died 
		if (owner.getHp() <= 0) {
			if (isDebug) System.out.println(owner.getName() + " was killed.");
			return false;
		}
		 else return true; }
		else {
			if (isDebug) System.out.println(owner.getName() + " avoided the attack by " + attacker.getName() + ".");
			return true;
		}
	}
	
	public int battleSpeed()
	{
		return (int) (owner.getSpeed() * (owner.getWoundPenalty() * 2));
	}
	
	//GETTERS AND SETTERS
	public String getName() {
		return owner.getName();
	}

	public void setName(String name) {
		owner.setName(name);
	}

	public int getHp() {
		return owner.getHp();
	}

	public void setHp(int hp) {
		owner.setHp(hp);
	}

	public int getAttack() {
		return owner.getAttack();
	}

	public void setAttack(int attack) {
		owner.setAttack(attack);
	}

	public int getSpeed() {
		return owner.getSpeed();
	}

	public void setSpeed(int speed) {
		owner.setSpeed(speed);
	}

	public double getCrit() {
		return owner.getCrit();
	}

	public void setCrit(double crit) {
		owner.setCrit(crit);
	}

	public double getDodge() {
		return owner.getDodge();
	}

	public void setDodge(double dodge) {
		owner.setDodge(dodge);
	}

	public double getWoundPenalty() {
		return owner.getWoundPenalty();
	}

	public void setWoundPenalty(double woundPenalty) {
		owner.setWoundPenalty(woundPenalty);
	}

	public double getFlightTendency() {
		return owner.getFlightTendency();
	}

	public void setFlightTendency(double flightTendency) {
		owner.setFlightTendency(flightTendency);
	}
	
	public void setRunner(boolean ran)
	{
		runner = ran;
	}
	
	public boolean getRunner()
	{
		return runner;
	}
	
	public boolean isAlive()
	{
		if (getHp() <= 0) return false; //entity is dead
		else return true; //entity is alive
	}
}
