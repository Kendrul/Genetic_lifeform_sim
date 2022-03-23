import java.awt.AWTException;
import java.awt.Point;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import old.Parameter;

public class Organism {
	
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
	
	private int resourceCarryAmount;
	private ArrayList<Resource> inventory;
	
	private double woundPenalty;

	private boolean isDebug = WorldState.isDebug;
	
	//other class links
	private OrganismGFX theOrg;
	private BattleState fighter;
	private GeneSequence genes;
	private GeneToPhenotype pheno;
	private PhenotypeToKinetics kinetics;
	
	//TBI
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
	public Organism(String whoAreYou, int health, int dmg, int theSpeed, double criticalStrike, double avoid, double flightRisk)
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
		idNumber = WorldState.getID();
		inventory = new ArrayList<Resource>();
	}
	
	public Organism()
	{
		woundPenalty = 0;
		idNumber = WorldState.getID();
		name = WorldState.randName(WorldState.rng1[3].rInt(WorldState.names.length));
		fighter = new BattleState(this);
		inventory = new ArrayList<Resource>();
	}
	
	public OrganismGFX getTheOrg() {
		return theOrg;
	}

	public void setTheOrg(OrganismGFX theOrg) {
		this.theOrg = theOrg;
		theOrg.setOwner(this);
	}
	public ArrayList<Parameter> getParameterList()
	{
		return null;
	}

	public void debugSet(boolean setDebugMode)
	{
		isDebug = setDebugMode;
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
	
	public void move(int turn) throws AWTException
	{
		theOrg.move(turn);
	}
	
	public Point getPoint()
	{
		return theOrg.getPoint();
	}

	public BattleState getFighter() {
		return fighter;
	}

	public void setFighter(BattleState fighter) {
		this.fighter = fighter;
	}

	public GeneSequence getGenes() {
		return genes;
	}

	public void setGenes(GeneSequence genes)throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		this.genes = genes;
		
		//SET PHENO
		reflectPack r = EventPack.geneFields(genes);
		genes.update();
		pheno = new GeneToPhenotype(r.getFieldArrayValues(), r.getFieldArrayNames());
		
		//SET KINETICS
		r = EventPack.phenoFields(pheno);
		kinetics = new PhenotypeToKinetics(r.getFieldArrayValues(), r.getFieldArrayNames());
		kinetics = pheno.phenoArrayGeneration();
		WorldState.addName(getName());
		WorldState.addGenome(genes);
		
		updateStats();
	}

	public void updateStats() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException{
		//uses kinetics and phenotype to update the statistics of the organism
		reflectPack r = EventPack.kinFields(kinetics);
		
	}
	
	public int getIdNumber() {
		return idNumber;
	}

	public GeneToPhenotype getPheno() {
		return pheno;
	}

	public PhenotypeToKinetics getKinetics() {
		return kinetics;
	}
	
	public boolean findResource(Resource r){
		if(inventory.contains(r)) return true;
		else return false;
	}
	
	public void addResource(Resource r, int amount){
		if ((amount + resourceCarryAmount) <= kinetics.getResourceCarryingCapacity())
		{
			if (!inventory.contains(r)) inventory.add(r);
			else inventory.get(inventory.indexOf(r)).addAmount(amount);
			resourceCarryAmount += amount;
		}
	}
	
	public Resource dropResource(Resource r, int amount)
	{
		Resource res = inventory.get(inventory.indexOf(r));
		
		if (res.getAmount() <= amount)
		{
			resourceCarryAmount -= res.getAmount();
			inventory.remove(r);
		} else
		{
			resourceCarryAmount -= amount;
			res.removeAmount(amount);
			res = new Resource(res, amount);
		}	
		
		return res;
	}
	
	public void consumeFood(int amount){
		
		for(int i = 0; i < inventory.size(); i++){
			if(inventory.get(i).getResourceType() == WorldState.resourceType[0])
			{
				Resource res = inventory.get(i);
				if (res.getAmount() <= amount)
				{
					resourceCarryAmount -= res.getAmount();
					inventory.remove(i);
				} else
				{
					resourceCarryAmount -= amount;
					res.removeAmount(amount);
				}	
				//TODO what benefit does food provide?
				break;
			}
		}
		
	}

}
