import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;


public class Entity {
	
	private String name;
	private int idNumber;
	private int hp;
	
	//---------------------------------------------------------
	//COMBAT VALUES, determined from Kinetics
	private int maxHp; //amount of harm an entity can take before dying
	private int attack; //amount of harm inflicted per attack
	private int speed; //determines which entity acts first, and the ability to flee (speedOfLocomotion)
	private double crit; //chance to do double damage to another entity upon attack
	private double dodge; //chance to avoid taking damage from an action
	private double flightTendency; //chance to decide to run away (1 - fightResponse)
	//------------------------------------------------------------------
	
	private double woundPenalty;
	private Random rng;
	private boolean isDebug;
	private Organism theOrg;
	private BattleState fighter;
	//TBI
	
	
	public Organism getTheOrg() {
		return theOrg;
	}

	public void setTheOrg(Organism theOrg) {
		this.theOrg = theOrg;
	}

	/**
	 * 
	 * @param whoAreYou
	 * @param health
	 * @param dmg
	 * @param theSpeed
	 * @param criticalStrike
	 * @param avoid
	 * @param flightRisk
	 */
	public Entity(String whoAreYou, int health, int dmg, int theSpeed, double criticalStrike, double avoid, double flightRisk)
	{
		name = whoAreYou;
		maxHp = health;
		hp = health;
		attack = dmg;
		speed = theSpeed;
		crit = criticalStrike;
		dodge = avoid;
		flightTendency = flightRisk;
		woundPenalty = 0;
		rng = new Random();
		idNumber = WorldState.getID();
	}
	
	public Entity()
	{
		woundPenalty = 0;
		rng = new Random();
	}
	
	public ArrayList<Parameter> getParameterList()
	{
		return null;
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
		if (woundPenalty < 0.75) {
		double roll = doubleDice();
		if (woundPenalty > 0.5) roll -= (woundPenalty * 2); //wounded animal more likely to run
		if (roll < flightTendency) {
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
		if (woundPenalty < 0.75) {
		double roll = doubleDice();
		
		if (roll < crit) {
			return attack * 2;
		}
		else return attack;
		//too wounded to fight back
		} else {
			if (isDebug) System.out.println(name + " is too wounded to react.");
			return 0;
		}
	}
	
	public boolean survivedAttack(int damage, Entity attacker)
	{
		if (damage <= 0) return true;
		double roll = doubleDice();
		if (roll >= dodge) {
		hp -= damage;
		woundPenalty = 1.0 - ((double) hp / (double) maxHp);
		//if (isDebug) System.out.println(name + " has a woundPenalty level of " + woundPenalty + ".");
		if (isDebug) System.out.println(attacker.getName() + " attacks " + name + " for " + damage + " health, " + hp + " remaining.");
		//true means entity survived, false means entity has died 
		if (hp <= 0) {
			if (isDebug) System.out.println(name + " was killed.");
			return false;
		}
		 else return true; }
		else {
			if (isDebug) System.out.println(name + " avoided the attack by " + attacker.getName() + ".");
			return true;
		}
	}
	
	public int battleSpeed()
	{
		return (int) (speed * (woundPenalty * 2));
	}
	
	//GETTERS AND SETTERS
	public String getName() {
		return name + idNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public double getCrit() {
		return crit;
	}

	public void setCrit(double crit) {
		this.crit = crit;
	}

	public double getDodge() {
		return dodge;
	}

	public void setDodge(double dodge) {
		this.dodge = dodge;
	}

	public double getWoundPenalty() {
		return woundPenalty;
	}

	public void setWoundPenalty(double woundPenalty) {
		this.woundPenalty = woundPenalty;
	}

	public double getFlightTendency() {
		return flightTendency;
	}

	public void setFlightTendency(double flightTendency) {
		this.flightTendency = flightTendency;
	}
	
	public int getMaxHp()
	{
		return maxHp;
	}
	
	public void move()
	{
		theOrg.move();
	}
	
	public Point getPoint()
	{
		return theOrg.getPoint();
	}

}
