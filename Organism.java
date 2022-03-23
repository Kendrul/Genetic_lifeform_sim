import java.awt.AWTException;
import java.awt.Point;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import old.Parameter;

public class Organism implements Comparable{
	
	private String name;
	private int idNumber;
	private int hp;
	private OrgInfo info;
	
	private int age = 0; //in simulation turns
	private int generation = 0;
	private int energy = WorldState.baseEnergy;
	private int lowEnergyThreshold = WorldState.lowEnergyThreshold;
	private int highEnergyThreshold = WorldState.highEnergyThreshold;
	private int regenThreshold = WorldState.regenThreshold;
	private int brainEnergyConsumption;
	private int actionPoint;
	private int goal; //0 food, 1 safety, 2 repro, 3 explore/none, 4 share
	private Patch target;
	private boolean aSexual = false;
	private int rFitness = 0;
	private int sitCounter = 0;
	
	//---------------------------------------------------------
	//COMBAT VALUES, determined from Kinetics
	private int maxHp = 100; //amount of harm an entity can take before dying
	private int attack; //amount of harm inflicted per attack
	private int speed; //determines which entity acts first, and the ability to flee (speedOfLocomotion)
	private double crit = 0.0; //chance to do double damage to another entity upon attack
	private double dodge = 0.0; //chance to avoid taking damage from an action
	private double flightTendency; //chance to decide to run away (1 - fightResponse)
	//------------------------------------------------------------------
	
	private final int ap = 2;
	private int resourceCarryAmount; //current amount of carried weight
	private ArrayList<Resource> inventory;
	
	//behaviour modifiers
	private double hostility;
	private int paranoiaLevel;
	
	private double woundPenalty;
	private double fatiguePenalty;

	private boolean isDebug = WorldState.isDebug;
	
	//other class links
	private OrganismGFX theOrg;
	private BattleState fighter;
	private GeneSequence genes;
	private GeneToPhenotype pheno;
	private PhenotypeToKinetics kinetics;
	private ArrayList<Coupling> pairs; 
	private TradeRequest tr = null;
	
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
		pairs = new ArrayList<Coupling>();
		generation = Starter.getGeneration();
		info = new OrgInfo(idNumber, getName(), generation, Starter.getTurn());
	}
	
	public Organism()
	{
		woundPenalty = 0;
		idNumber = WorldState.getID();
		name = WorldState.randName(WorldState.rng1[3].rInt(WorldState.names.length));
		fighter = new BattleState(this);
		inventory = new ArrayList<Resource>();
		hp = maxHp;
		generation = Starter.getGeneration();
		pairs = new ArrayList<Coupling>();
		info = new OrgInfo(idNumber, getName(), generation, Starter.getTurn());
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
		info.name = getName();
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		if (hp <= maxHp) this.hp = hp;
		else hp = maxHp;
		setWoundPenalty(1.0 - ((double) hp / (double) maxHp));
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
	
	public void setGeneration(int g)
	{
		generation = g;
		info.generation = Integer.toString(g);
	}
	
	public int getGeneration()
	{
		return generation;
	}

	public void setDodge(double dodge) {
		this.dodge = dodge;
	}

	public double getBattlePenalty() {
		double p = woundPenalty;
		if (energy < 0) p += WorldState.fatiguePenalty;
		return p;
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
	
	public void move(int turn)throws AWTException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		action(turn);
		//theOrg.rmove(turn);
	}
	
	public void resetAp()
	{
		actionPoint = ap;
		
		if (kinetics.getSpeedOfLocomotion() > 0.75) actionPoint += 3;
		else if (kinetics.getSpeedOfLocomotion() > 0.5) actionPoint += 2;
		else if (kinetics.getSpeedOfLocomotion() > 0.25) actionPoint += 1;
	}
	
	public void action(int turn) throws AWTException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{//This Organism's turn to take an action, updates all attributes for over time (age, energy, regeneration)
		//check health here
		//TODO placeMarker for easy finding
		//moodSet();
		age++;
		changeEnergyLevel(-brainEnergyConsumption);
		regeneration();
		int action;
		if (hp <= 0) return; //dead
		if ((sitCounter > 0) && (goal != 4))
		{//currently acting out a different event, do nothing
			sitCounter--;
			return;
		} else if (goal == 4){
			action = helpMoveOptions(theOrg.getSight().mobCount());
		}else{
			action = decideTurn();
		if (target == null) action = -2;//something broke, move random
		}
		switch (action) {
		case -1 : 
			break; //entity is too weak/injured to take any further action
		case 1 : theOrg.move(target);
				if(theOrg.getPatch().equals(target)) reap(); //eat food here
				break;
		case 2: theOrg.move(target);
				//ask for food
				if (target.findOtherOrg(this).hasFood()) {
					TradeRequest tr= null;
					tr = new TradeRequest(target.findOtherOrg(this).getInventory().get(0), this, calcCollect());
					EventPack.askShare(tr, this, target.findOtherOrg(this));
				}
				break;
		case 3: //give food (was trade which was not implemented)
				theOrg.move(target);
				EventPack.shareResource(this, target.findOtherOrg(this), inventory.get(0), (int) Math.floor(inventory.get(0).getAmount() * WorldState.rng3[1].rDouble()), false);
				//findCouple.(theOrg.getPatch().findOtherOrg(this)).
				break;
		case 4: theOrg.move(target);
				//fight
				if (goal == 0) EventPack.battleWrapper(this, target.findOtherOrg(this), true, true);
				else EventPack.battleWrapper(this, target.findOtherOrg(this), true, false);
				break;
		case 5:  theOrg.move(target);
				harvest(); //pick up food here
				break;
		case 6: theOrg.hunt();
				break;
		case 7 : 
			actionPoint =0; //nowhere to go
			break; 
		case 8: theOrg.move(target);
				EventPack.askMate(this, theOrg.getPatch().getTheE());
				break;
		case 9: theOrg.move(target); //safety move
			break;
		case 0: //randomMove
		default :
			theOrg.setHunt(false); //not hunting for anything
			theOrg.randomMove();
		break;
		}
	}
	
	/*private void moodSet()
	{
		double roll = WorldState.rng1[4].rDouble();
		if (roll >= moodChance) goodMood = false;
		else goodMood = true;
	}*/
	
	public void regeneration(){
		if (woundPenalty > 0.75){
			//organism is severely wounded, even with energy there is a chance it gets worse
			double roll = WorldState.rng1[4].rDouble();
			if (roll > 0.75) {//poor health worsened, organism may die
				int subHp = maxHp / 10;
				setHp(hp - maxHp);
				if (hp < 0) {
					WorldState.addLogEvent("[Turn:" + Starter.getTurn() +"] " + this.getName() + " has died due to it's poor health and/or wounds.");
					Starter.getStats().incWoundDeath(1);
					Starter.entityDeath(this, "Wounds");
				}
				return;
			}
		} 
		
		if ((hp < maxHp) && (energy > regenThreshold))
		{//organism has energy, it regains some health as it's body heals
			if (energy < (highEnergyThreshold/2)) setHp(hp + maxHp / 10);
			else setHp(hp + maxHp / 15);
		} else if (energy == 0)
		{//organism is beginning to starve, it's health declines
			int subHp = maxHp / 10;
			setHp(hp - maxHp);
			if (hp < 0) {
				WorldState.addLogEvent("[Turn:" + Starter.getTurn() +"] " + this.getName() + " has starved to death.");
				Starter.getStats().incStarveDeath(1);
				Starter.entityDeath(this, "Starvation");
			}
		}
	}
	
	public Point getPoint()
	{
		return theOrg.getPoint();
	}
	
	public ArrayList<Coupling> getPairs() {
		return pairs;
	}

	public void addPairs(Coupling c) {
		pairs.add(c);
	}
	
	public void addCoupling(Organism o, Patch p){
		if (o != null) pairs.add(new Coupling(this, o));
		else pairs.add(new Coupling(this, p));
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
		
		updateStats(r);
	}

	public void updateStats(reflectPack rp) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException{
		//uses kinetics and phenotype to update the statistics of the organism
		reflectPack rk = EventPack.kinFields(kinetics);
		
		double brain = 0;
		double vision= 0;
		double relational = 0;
		
		for (int i = 0; i < rp.getFieldArrayNames().length; i++){
			if(rp.getFieldArrayNames()[i].contains("fightResponse")){
				flightTendency = rp.getFieldArrayValues()[i];
			} 
			if(rp.getFieldArrayNames()[i].contains("neural")){
				brain = rp.getFieldArrayValues()[i];
			} 
			if(rp.getFieldArrayNames()[i].contains("radius")){
				vision = rp.getFieldArrayValues()[i];
			} 
			if(rp.getFieldArrayNames()[i].contains("colonial")){
				relational = rp.getFieldArrayValues()[i];
			} 			
			if(rp.getFieldArrayNames()[i].contains("hostility")){
				hostility = rp.getFieldArrayValues()[i];
			} 
		}
		//calculates how much energy consumed per turn just from existing
		brainEnergyConsumption = (int) (WorldState.lifeEnergyCost  * ((brain * 0.5) + (vision * 0.1) + (relational * 0.25)));
		
		//kinetics
		for (int i = 0; i < rk.getFieldArrayNames().length; i++){
			//update CarryingCapacity, moodChance, hostility
			if(rk.getFieldArrayNames()[i].contains("Carry")){
				resourceCarryAmount = (int) (rk.getFieldArrayValues()[i] * WorldState.resourceCarryConstant);
			} 
			if(rk.getFieldArrayNames()[i].contains("fight")){
				attack = (int) (rk.getFieldArrayValues()[i] * WorldState.attackConstant);
			} 
			if(rk.getFieldArrayNames()[i].contains("maxHp")){
				maxHp = (int) (rk.getFieldArrayValues()[i] * WorldState.hpConstant);
				hp = maxHp;
			} 
			if(rk.getFieldArrayNames()[i].contains("crit")){
				crit = (rk.getFieldArrayValues()[i] * 20);
			} 
			if(rk.getFieldArrayNames()[i].contains("dodge")){
				dodge = (rk.getFieldArrayValues()[i] * 20);
			} 
			
		}
		resetAp();
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
	
	public boolean findResource(int num)
	{
		for (int i = 0; i < inventory.size(); i++)
		{
			if(inventory.get(i).getResourceNum() == num) return true;
		}
		return false;
	}
	
	public boolean findResource(Resource r){
		if (r == null) return false;
		for (int i = 0; i < inventory.size(); i++)
		{
			if(inventory.get(i).getResourceNum() == r.getResourceNum()) return true;
		}
		
		return false;
	}
	
	
	public int getActionPoint() {
		return actionPoint;
	}

	public void subActionPoint() {
		this.actionPoint -= 1;
	}
	
	public boolean hasFood()
	{
		for(int i = 0; i < inventory.size(); i++){
			if(inventory.get(i).getResourceType() == WorldState.resourceType[0])
			{
				return true;
			}
		}
		return false;
	}
	
	public int potentialEnergy()
	{
		int sum = 0;
		for(int i = 0; i < inventory.size(); i++){
			if(inventory.get(i).getResourceType() == WorldState.resourceType[0])
			{
				sum += inventory.get(i).getAmount();
			}
		}
		return sum;
	}

	public int addResource(Resource r, int amount){
		if ((amount + resourceCarryAmount) < (kinetics.getResourceCarryingCapacity()*WorldState.resourceCarryConstant))
		{//has room for all of the amount
			for (int i = 0; i < inventory.size(); i++)
			{
				if(inventory.get(i).getResourceNum() == r.getResourceNum()) 
				{
					inventory.get(i).addAmount(amount);
					resourceCarryAmount += amount;
					return 0;
				}
			} 
			//was not found
			inventory.add(new Resource(r, amount));
			resourceCarryAmount += amount;		
			return 0;
		}else
		{//has room for only some
			int a = (amount + resourceCarryAmount) - (int) (kinetics.getResourceCarryingCapacity() * WorldState.resourceCarryConstant);
			for (int i = 0; i < inventory.size(); i++)
			{
				if(inventory.get(i).getResourceNum() == r.getResourceNum()) 
				{
					inventory.get(i).addAmount(amount-a);
					resourceCarryAmount += amount-a;
					return a;
				}
			} 
			//was not found
			inventory.add(new Resource(r, amount-a));
			resourceCarryAmount += amount-a;	
			return a; //return the leftover
		}
	}
	
	public int subResource(Resource r, int amount){
			for (int i = 0; i < inventory.size(); i++)
			{
				if(inventory.get(i).getResourceNum() == r.getResourceNum()) 
				{
					amount = inventory.get(i).removeAmount(amount);
					resourceCarryAmount -= amount;
					return 0;
				}
			} 
			//was not found		
			return 0;
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
	
	public void consumeFood(){
		if (actionPoint < 0) return;
		int amount = calcConsume();
		for(int i = 0; i < inventory.size(); i++){
			if(inventory.get(i).getResourceType() == WorldState.resourceType[0])
			{
				Resource res = inventory.get(i);
				if (res.getAmount() <= amount)
				{
					amount -= res.getAmount();
					resourceCarryAmount -= res.getAmount();
					justEat(inventory.get(i), amount);
					inventory.remove(i);
				} else
				{
					resourceCarryAmount -= amount;
					res.removeAmount(amount);
					justEat(inventory.get(i), amount);
				}	
				break;
			}
		}	
	}
	
	public void justEat(Resource r, int amount)
	{//resource is passed in case it modifies the energy gain
		changeEnergyLevel(amount * WorldState.calorieFactor);
		addRFit(amount);
		actionPoint--;
		WorldState.addLogEvent("[Turn:"+ Starter.getTurn() + "] " + getName() + " ate " + amount + " " + r.getName() + "s and regained " + (amount * WorldState.calorieFactor) + " energy.");
		Starter.getStats().incConsumeEvents(1);
		Starter.getStats().incFoodConsumed(amount);	
	}

	public void changeEnergyLevel(int amount)
	{//changes the energy level of the organism, cannot be below 0
		if (energy + amount < 0) energy = 0;
		else energy += amount;
	}
	
	//DECISION MAKER
	private int foodPriority; //default
	private int safetyPriority;
	private int reproPriority ;
	private int explorePriority;
	
	public int decideTurn() throws AWTException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{//this function helps the organism decided what action it should pursue
		//PRIORITY: FOOD, SAFETY, REPRODUCTION, EXPLORE
		//FACTORS: Age, ENERGY, HP, FightingEffectiveness, Hostility, MoodResponse, fightResponse
			
		if(actionPoint <= 0) return -1; //already acted twice
		if (getBattlePenalty() > 0.9) {
			consumeFood();
			WorldState.addLogEvent("[Turn:"+ Starter.getTurn() + "] " + getName() + " is in poor health, and is unable to complete any actions.");
			Starter.getStats().incUnableToActTurns(1);
			return -1; //can't act
		}
		
		
		theOrg.organismPerceives();
		int foodPriority = WorldState.NONE; //default
		int safetyPriority = WorldState.NONE;
		int reproPriority = WorldState.NONE;
		int explorePriority = WorldState.LOW;
		paranoia();
		int p = paranoiaLevel;
		
		
		if (energy == 0) foodPriority = WorldState.CRITICAL; //FIND FOOD NO MATTER WHAT
		else if (energy < lowEnergyThreshold) foodPriority = WorldState.HIGH; //FIND FOOD ASAP
		else if ((energy + potentialEnergy()) < (lowEnergyThreshold * 3)) foodPriority = WorldState.MEDIUM ; //safe for now, build up food store	
		else if ((energy + potentialEnergy()) < highEnergyThreshold) foodPriority = WorldState.LOW;
		
		if (woundPenalty > 0.75) safetyPriority =WorldState.CRITICAL;
		else if ((woundPenalty > 0.5) || ((p >3)  && (woundPenalty > 0))) safetyPriority =WorldState.HIGH; //injured and possibly paranoid
		else if (p >= 3) safetyPriority =WorldState.MEDIUM; //just paranoid
		else if ((woundPenalty > 0) || p == 2) safetyPriority =WorldState.LOW; //slightly wounded or slightly paranoid
		
		if (age >= WorldState.reproductiveMaturityAge * 3) reproPriority = WorldState.CRITICAL; 
		if ((energy > (highEnergyThreshold / 4)) && (woundPenalty < 0.25) && (age > WorldState.reproductiveMaturityAge)) reproPriority = WorldState.LOW;
		
		///double roll = WorldState.rng3[0].rDouble(); // for sleep check
		
		int pChoice = decidePriority(new int[]{foodPriority, safetyPriority, reproPriority, explorePriority});
		int dChoice;
		switch (pChoice) {
		case 0: dChoice = decideFood(foodPriority); //find food
			break;
		case 1: dChoice = decideSafety(); //seek safety
			break;
		case 2: //dChoice = decideRepro(); //make some babies!
			//break;
		case 3: dChoice = decideExplore(); //to seek out new life and new civilizations
			break;
		case 4: return 4; //sleep,
		case 5: return helpMoveOptions(theOrg.getSight().mobCount());
		default: dChoice = decideExplore(); //to boldly go where no man has gone before
			break;	
		}
		return dChoice;
	}
	
	private int decidePriority(int [] p)
	{//PRIORITY: FOOD 0, SAFETY 1, REPRODUCTION 2, EXPLORE 3
		//first level
		if (p[3] == WorldState.CRITICAL) return 2; //forced to seek reproduction?
		if (p[0] == WorldState.CRITICAL) return 0; //STARVATION IMMINENT
		
		
		//second, third and fourth level
		if ((p[0] == WorldState.HIGH) && (p[1] == WorldState.CRITICAL) || (p[0] == WorldState.MEDIUM) && (p[1] == WorldState.HIGH))
		{//food high prior, safety critical prior
			//pick one
			double roll = WorldState.rng3[0].rDouble();
			//paranoia takes into account coupling, health, number of nearby organisms, flightTendency, hostility and fighting Effectiveness
			double threshold = WorldState.baseSafety + (paranoiaLevel / (100)); //base value + paranoia level + hostility
			//if (goodMood) threshold -= WorldState.baseSafety / 2; //good mood, less vigilant
			//else threshold += WorldState.baseSafety / 2; //bad mood, more vigilant
			if (potentialEnergy() > lowEnergyThreshold * 2) threshold += lowEnergyThreshold / 100; //has food stores, not so dire
			if (roll > threshold) return 0; //focus on food
			else return 1; //safety focus
			
		} else if (p[0] == WorldState.HIGH) return 0;
		else if (p[1] == WorldState.CRITICAL) return 1;
		 else if (p[0] == WorldState.MEDIUM) return 0;
			else if (p[1] == WorldState.HIGH) return 1;
				else if (p[1] == WorldState.MEDIUM) return 1;
		
		//fifth level
		//low prior food and safety have same prior has repro (if allowed) and explore
		double roll = WorldState.rng3[0].rDouble();
		if (roll < 0.5) return goal; //stick with current goal
		double threshold = 0.5 + (WorldState.baseSafety + (paranoiaLevel / (100)))/2;
		if (roll < threshold){
			goal = 1;
			return 1; //focus on safety
		}
		if (potentialEnergy() + energy > highEnergyThreshold) threshold += 0.025;
		else threshold += 0.1;
		if (roll < threshold) 
			{
			goal = 0;
			return 0; //focus on food
			}
		if(p[2] != WorldState.NONE) {//make sure organism is allowed to reproduce
			threshold += ((1-threshold) / 2) + p[2]/200;
			if (roll < threshold) {
				goal = 2;
				return 2; //make some babies
			}
		}
		//default action
		if (goal != 4) goal = 3;
		else return 5;//share
		return 3; //explore!
	}
	
	public int decideFood(int desperationLevel) throws AWTException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException{
		//entity has decided to look for food
		//1) check own food inventory (critical/high)
		//2) look for food source nearby
		//3) look for coupled food source nearby (distance calculation)
		//4a) look for shared food from nearby organism
		//4b) look to take food from nearby organism (critical/high or high hostility/bad mood/high fight effectiveness)
		//*step 4 depends on coupling relations, mood, hostility, and desperation
		//5) any other coupled food source
		//6) explore -> max move
		if ((desperationLevel == WorldState.CRITICAL) || (desperationLevel == WorldState.HIGH))
		{
			consumeFood();
			if (energy >= lowEnergyThreshold) 
			{//was carrying sufficient food
				return decideTurn();
			}
		}
			
		//needs to locate food	
			if (theOrg.getPatch().hasFood() && theOrg.getPatch().getTheR().getAmount() > 0 && (resourceCarryAmount < (kinetics.getResourceCarryingCapacity()*WorldState.resourceCarryConstant)))
			{//food located here, consume some
				if ((desperationLevel == WorldState.CRITICAL) || (desperationLevel == WorldState.HIGH)) reap();
				else if (resourceCarryAmount < (kinetics.getResourceCarryingCapacity()*WorldState.resourceCarryConstant)) harvest();
				return decideTurn();
			} else 
			{//check nearby perceived patches for food
				Patch spot = theOrg.getSight().findFoodPatch();
				if (spot != null)
				{//found a spot
					target = spot; 
					if ((desperationLevel == WorldState.CRITICAL) || (desperationLevel == WorldState.HIGH)) return 1; //1 means move to target location and reap
					else if (resourceCarryAmount < kinetics.getResourceCarryingCapacity()) return 5; //if not desperate for food, pick it up instead
					else {
						//goal = 3; //no room to carry food
						return 0;
					}
				}//check for coupled source? 
				else {//check for nearby organism with food
					
					spot = theOrg.getSight().findFoodOrg();
					if (spot != null)
					{//found a spot
						target = spot; //1 means move to target location and reap
						int choice = chooseActionForFood(spot.getTheE());
						if (choice == EventPack.FightEvent) return 4;						
						else return 2;
						//else if(choice == EventPack.TradeEvent) return 3;
					}//check for coupled source?
					else
					{
						//no options remain?
					}
				}//end else check orgs
			}//end else check patchs

		return 6; //wander it is
	}
	
	public int chooseActionForFood(Organism target){
		//determines whether the organism should first try to ask for trade, share, or fight for food
		int d = decideFight(target, false);
		//if (goodMood == false) threshold += 0.1;
		//threshold += spot for coupling, and similarity
		if ((d == EventPack.FightEvent)) return EventPack.FightEvent;
		else return EventPack.ShareEvent;
	}

	public int decideSafety() throws AWTException{
	//ORGANISM HAS DECIDED TO FOCUS ON SAFETY
		//If outnumbered or can't win --> run
	//If has numbers or stronger --> fight (maybe?)
		//boolean willFight = decideFight(false);
	//cornered -> fight
		//if weaker vs neutral or slightly hostile, share food? (positive coupling)
		
		//first check if current location is shared with another organism
		goal = 1; //safety
		Organism o = theOrg.getPatch().findOtherOrg(this);
		int d = 0;
		boolean run = false;
	if (o != null){
		//find relationship, if one exists
		Coupling c = findCouple(o);
		if (!c.isPositiveCouple()) d = decideFight(o, false); //enemy
		
		if (d == EventPack.FightEvent) EventPack.battleWrapper(this, o, true,false);
		else 
			{//decided not to fight, establish a coupling
				establishPair(o);
				run = true;
			}
	}
	int p = personality();
	//decided to run, or current location is empty (aside from THIS organism)
	if (run == true || o == null){
		//evaluate the safest escape route
		d = safetyMoveOptions();
		if (d == 7) 
			{//nowhere to move
				if(hasFood()){//share food to encourage good will?
					o = theOrg.getPatch().findOtherOrg(this);
					double roll = WorldState.rng3[2].rDouble();
					Coupling c = findCouple(o);
					if (findCouple(o).isPositiveCouple()) return 7;
					else if (roll > c.getCoupleAmount()) {
						EventPack.shareResource(this, o, inventory.get(0), (int) Math.floor(inventory.get(0).getAmount() * WorldState.rng3[1].rDouble()), false);
						return 7; 
					}
				}else return 7;
			}else if (d == 9) return 9; //safe spot to move to
			else if (d ==2){//potential danger spot to move to
				if(hasFood()){//share food to encourage good will?
					o = theOrg.getPatch().findOtherOrg(this);
					double roll = WorldState.rng3[2].rDouble();
					Coupling c = findCouple(o);
					if(c.getCoupleAmount() ==0) {
						if (roll > 0.5) {
							establishPair(o);
							return 3;
						}
					}else if (findCouple(o).isPositiveCouple()) return 9;
					else if (roll > c.getCoupleAmount()) return 2;
				}else return 9;			
			}
	}

		return 6;
	}
	
	public int safetyMoveOptions(){
		//assess's the surrounding area
		ArrayList<Integer> count = theOrg.getSight().mobCount();
		int num = 0;
		
		if (count.contains(0)) {
			for(int i = 1; i < count.size(); i++)
			{//count number of empty spots
				if (count.get(i) == 0) num++;
			}
			
			if (num == 1){
				for(int i = 1; i < count.size(); i++)
				{//count number of empty spots
					if (count.get(i) == 0) {
						num = i;
						break;
					}
				}
				target = theOrg.getSight().getTarget(num);
				if (target == null) target = theOrg.getPatch();
			} else if (num > 1) //multiple empty spots, find the least crowded
			{
				for(int i = 1; i < count.size(); i++)
				{//count number of empty spots
					if (count.get(i) == 0) {
						int left, right, middle = 0;
						if (i == 1) left = count.size()-1;
						else left = i-1;
						if (i == count.size()-1) right = 1;
						else right = i+1;
						if(count.get(left) != -1) middle += count.get(left);
						if(count.get(right) != -1) middle += count.get(right);
						count.set(i, middle);//creates a new value
						//higher means more dangerous, means more neighbours to that spot
					}
				}
				
				for(int i = 1; i < count.size(); i++)
				{// find the safest spot to move too
					if (i == 1) num = 1;
					else if (count.get(num) > count.get(i)) num = i;
					else if (count.get(num) == count.get(i)){
						//randomly choose a target
						int roll = WorldState.rng3[3].rInt() % 2;
						if (roll == 1) num = i;
					}
				}
				target = theOrg.getSight().getTarget(num);
				return 9;
			} else if (count.contains(1)){//find spots nearby that are not full
				Organism targetOrg = null;
				double temp1 = -100.0, temp2;
				Patch p = null;
				ArrayList<Organism> pack = theOrg.getSight().pack();
				
				for (int i = 1; i < count.size(); i++)
				{//go through each patch with non-max entities
					if(count.get(i) == 1){
						p = theOrg.getSight().getTarget(i);
						
						for (int j=0; j < pack.size(); j++)
						{//examine nearby organisms, determine perceived strength compared to THIS or find FRIENDS
							Organism o = pack.get(j);
							if (!o.getTheOrg().getPatch().equals(theOrg.getSight().getTarget(i))) continue;
							if (findCouple(o).isPositiveCouple()) {
								//found a friend!
								target = theOrg.getSight().getTarget(i);
								return 9;
							}
							else if (o.getTheOrg().getPatch().equals(p)){
								//assess a fighting capability difference
								//find the perceived weakest neighbour
								 temp2 = weaknessPerception(o);
								 if (temp2 > temp1){
									 temp1 = temp2;
									 num = i;
									 targetOrg = o;
								 } else if(temp2 == temp1)
								 {
										int roll = WorldState.rng3[3].rInt() % 2;
										if (roll == 1) {//random to decide which to choose
											 temp1 = temp2;
											 num = i;
											 targetOrg = o;
										}//end if
								 }//end else-if
							}//end if
						}//end for loop (pack)
					}//end if			
				}//end for loop (count)
				target = theOrg.getSight().getTarget(num);
				num = decideFight(targetOrg, false);
				if (num == EventPack.FightEvent) return 4;
				else return 3;
			}else {//fully surrounded
				fighter.setCornered(true);
				return 7;
			}
		}	
		return 0;
	}
	
	public int decideRepro() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException{
		//ORGANISM HAS DECIDED TO REPRODUCE
		if(actionPoint < 1) return -1;
		if (aSexual) {
			int roll = (WorldState.rng0[2].rInt() % WorldState.maxLitter) + 1;
			double m = theOrg.getPatch().getMutation();
			int e = energyTransfer(roll);
			if (e == -1) return 6; //not enough energy
			
			for(int i = 0; i < roll; i++)
			{
				EventPack.reproductionA(this, m, e);
			}
			EventPack.reprodDeath(this);
			return -1;
		} else //choose a mate
		{
			Organism o = theOrg.getPatch().findOtherOrg(this);
			
			if(matingRequest(o)) {
				//found a mate on current location
				EventPack.askMate(this, o);
				return 7;
			}
			
			target = theOrg.getSight().findMate();
			if (target != null){
				goal = 2;
				return 8;
			}
			
			return 6; //no suitable mate
		}
	}
	
	public int decideExplore()
	{//ORGANISM HAS DECIDED TO EXPLORE:
		//Go new direction
		//meet friendly organism (and share/trade)
		//meet neutral organism
		//attack hostile/neutral organism
		ArrayList<Integer> count = theOrg.getSight().mobCount();
		int num = 0;
		int dice = WorldState.rng3[3].rInt() % 2;
		if (count.contains(0) && dice == 0) {
			//random choose to move to an empty spot
			while (count.get(num) != 0)
			{//randomly pick a place to go, make sure its empty
				dice = 1 + (WorldState.rng3[3].rInt() % count.size()-2);
			}//end while loop
			
		
				target = theOrg.getSight().getTarget(num);
				return 9;
			} else return helpMoveOptions(count);
	}
	
	public int helpMoveOptions(ArrayList<Integer> count){
		//assess's the surrounding area
		int num = 0;
		
		if(potentialEnergy() <= 1) {
			//has no food to share, wander
			goal = 3;
			return 0;
		}
		if (count.contains(1)){//find spots nearby that are not full
			Organism targetOrg = null;
			double temp1 = -100.0, temp2;
			Patch p = null;
			ArrayList<Organism> pack = theOrg.getSight().pack();

			for (int i = 1; i < count.size(); i++)
			{//go through each patch with non-max entities
				if(count.get(i) == 1){
					p = theOrg.getSight().getTarget(i);
					for (int j=0; j < pack.size(); j++)
					{//examine nearby organisms, determine perceived strength compared to THIS or find FRIENDS

						Organism o = pack.get(j);
						if (!o.getTheOrg().getPatch().equals(theOrg.getSight().getTarget(i))) continue;
						if (o.potentialEnergy() >= potentialEnergy()) continue; //same level of food, move along
						else {
							Coupling c = findCouple(o);
							double roll = (WorldState.rng3[3].rDouble() * 2) -1 ;
							if(c.isPositiveCouple()) if (roll < c.getCoupleAmount()*2) {
								if (targetOrg == null || targetOrg.potentialEnergy() > o.potentialEnergy()) {
									targetOrg = o;
									num = i;
								} else if (roll > c.getCoupleAmount()*-1){
									if (targetOrg == null || targetOrg.potentialEnergy() > o.potentialEnergy()) {
										targetOrg = o;
										num = i;
									}
								}
							}
						}
					}//end for loop (pack)
				}//end if			
			}//end for loop (count)
			if (num ==0 || targetOrg == null) {
				goal = 3;
				return 0; //no sharing, move
			}
			goal = 4;
			target = theOrg.getSight().getTarget(num);
			return 3;
			}else {//fully surrounded
				goal = 3;
				return 7;
			}
		}	

	
	public int paranoia()
	{//determines if an organism is skittish about nearby organisms
		//organisms that have higher flight risk, or are in a weaker state, will take safety more seriously
		double s = flightTendency + getBattlePenalty() - WorldState.baseSafety;
		ArrayList<Organism> pack = theOrg.getSight().pack();
		double friend = 0, foe = 0, neutral = 0;
		boolean found;
	
	Pack_Search: //a goto label
		for(int i = 0; i < pack.size(); i++)
		{
			found = false;
			for (int j=0; j< pairs.size(); j++){
				if (found == true) continue;
				if (pairs.get(j).isCoupled(pack.get(i))) {
					if (pairs.get(j).isPositiveCouple())  
					{
						friend += 1 + pairs.get(j).getCoupleAmount();	
						found = true;
						continue Pack_Search;
					} else 
						{
							foe += pairs.get(j).getCoupleAmount();
							found = true;
							continue Pack_Search;
						}
				}
			}
			if (found == false) neutral  += 1;
		}	
		
		//counts foes as double versus neutral, and modified by its health and flightTendency
		int p = (int) Math.ceil((foe * 2 * s + neutral * s - friend));
		if (safetyPriority > WorldState.MEDIUM || p > 3) fighter.setCornered(true);
		else fighter.setCornered(false);

		paranoiaLevel = p;
		return p;
	}
	
	public void reap()
	{
		if (actionPoint < 0) return;
		int amount = calcCollect();
		if (amount > theOrg.getPatch().getTheR().getAmount()) amount = theOrg.getPatch().getTheR().getAmount();
		amount -= addResource(theOrg.getPatch().getTheR(), amount);
		WorldState.addLogEvent("[Turn:"+ Starter.getTurn() + "] " + getName() + " reaped " + amount + " " + theOrg.getPatch().getTheR().getName() + "s.");
		Starter.getStats().incReapEvents(1);
		Starter.getStats().incFoodReap(amount);			
		addRFit(amount);
		consumeFood();
		theOrg.getPatch().getTheR().removeAmount(amount);
	}
	
	public void harvest()
	{
		if (actionPoint < 0) return;
		if (!theOrg.getPatch().hasFood() || theOrg.getPatch().getTheR().getAmount() <=0) return; //no food, or exhausted
		
		int amount = calcCollect();
		if (amount > theOrg.getPatch().getTheR().getAmount()) amount = theOrg.getPatch().getTheR().getAmount();
		amount -= addResource(theOrg.getPatch().getTheR(), amount);
		addRFit(amount);
		WorldState.addLogEvent("[Turn:"+ Starter.getTurn() + "] " + getName() + " harvested " + amount + " " + theOrg.getPatch().getTheR().getName() + "s.");
		Starter.getStats().incHarvestEvents(1);
		Starter.getStats().incFoodHarvest(amount);				
		theOrg.getPatch().getTheR().removeAmount(amount);
		actionPoint--;
	}
	
	private int calcCollect()
	{//calculates how much to harvest/reap/ask for
		if (resourceCarryAmount < (kinetics.getResourceCarryingCapacity()*WorldState.resourceCarryConstant)) {
			int amount = 1 + (int) (((kinetics.getResourceCarryingCapacity()*WorldState.resourceCarryConstant) - resourceCarryAmount) * WorldState.rng3[1].rDouble());
			//if (goodMood) amount = (int) (amount * 1.5);
			return amount;
		} else return 0; //no space
	}
	
	private int calcConsume()
	{//calculates how much to eat this action
		
		int amount = 1 + (int) (20 * (1 + pheno.weight()) * WorldState.rng1[5].rDouble());
		//if (!goodMood) amount = (int) (amount * 1.5);
		return amount;
	}
	
	public void dropResource()
	{//when an entity dies it drops any resources it has, with half lost
		Resource ro;

		if(findResource(theOrg.getPatch().getTheR())) 
			{
			for (int i = 0; i < inventory.size(); i++)
			{
				if(inventory.get(i).getResourceNum() == theOrg.getPatch().getTheR().getResourceNum()){
					ro = inventory.get(i);
					theOrg.getPatch().getTheR().addAmount(ro.getAmount()/2);
					//add half the amount to the patch total, the rest is lost
				}	
			}
			} else {
				if (inventory.size() > 1) {					
					int roll = WorldState.rng3[1].rInt() % inventory.size();
					ro = inventory.get(roll);
				} else ro = inventory.get(0);	
				//patch has no resource, add it with half the amount
				theOrg.getPatch().setTheR(new Resource(ro, ro.getAmount()/2));	
				theOrg.getPatch().getTheR().setHome(theOrg.getPatch());
			}	
	}
	
	/**
	 * 
	 * @param o
	 * @param askedHelp
	 * @return
	 */
	public int decideFight(Organism o, boolean askedHelp){
		//0 = decline
		//3 = fight
		//This is where an organism decides if it wants to attack another organism
		if (o == null) return 0;
		double roll = WorldState.rng3[3].rDouble();
		double threshold = pheno.getHostility();
		
		/**/ //Organism is starving, sick/wounded and thus desperate
			Coupling c = findCouple(o);
			if ((foodPriority == WorldState.CRITICAL) && ((woundPenalty > 0) && (woundPenalty < 0.5))) {			
				if ((c.isPositiveCouple())) threshold *= (1-c.getCoupleAmount());//good coupling reduces willingness to attack
				else threshold *= (1+c.getCoupleAmount());//placeholder
			}else {
				//determine fear VS Bully VS Aggressor
				int mState = 0; //0 = neutral, 1 = fear, 2 = Bully, 3 = Aggressor
				mState = personality();
				
				switch (mState) {
				case 0 : //neutral
					break;
				case 1 : //skittish (paranoia high)			
					threshold *= (1 + (paranoiaLevel/10));//willingness to fight magnified by paranoia
					break;
				case 2 : //bully (strength + paranoia low)
					threshold *= 1/((1 + ((paranoiaLevel)/20)) - weaknessPerception(o));
					break;
				case 3 : //aggressor (paranoia low)
					threshold *= 1/(1 + ((paranoiaLevel)/20));
					break;
				default : ;
				}
				
				//determine Anger
/*				if ((!goodMood) && ((mState == 2) || (mState == 3))) {//if in a bad mood and Bully/Aggressor
					if (askedHelp && c != null && c.isPositiveCouple()) threshold *= 2 - c.getCoupleAmount(); //slight anger
					else threshold *= 2; //bigger anger
				}*/
							
					if (c == null) 
						{//no prior relationship, create it!
							c = new Coupling(this, o);
							addPairs(c);
						}
						
				//determine Hate
				if ((c.isPositiveCouple())) threshold *= (1-c.getCoupleAmount());//good coupling reduces willingness to attack
				else  threshold *= (1+c.getCoupleAmount());//placeholder				
			}
			
		 //*/

		if (roll < threshold) return 0; //do not fight
		else return 3; //FIGHT FIGHT FIGHT
	}
	
	private int personality(){
		int m = 0;
		double h = pheno.getHostility(), f = flightTendency;
		
		if (h > 0.2) {
			if ((h >= (f * 0.8)) && (f >= (h * 0.8))) {//hostility and flightTendency are close
				//Bully persona
				m = 2;
			}
			else if (h > f) {
				// Aggressor Persona
				m = 3;
			}
			else // (f > h)
			{//skittish Persona
				//assess paranoia
				m = 1;			
			}
		} else if ( f > 0.2)
		{//also skittish
			m=1;
		} //unstated else = neutral = 0
		
		return m;
	}
	
	public double weaknessPerception(Organism o)
	{
		double owp = o.getBattlePenalty(), ofe = o.getKinetics().getFightingEffectiveness(), osp = o.getKinetics().getSpeedOfLocomotion();	
		owp = sensorRoll(owp) - getBattlePenalty();
		ofe =  kinetics.getFightingEffectiveness() - sensorRoll(ofe);
		osp =  kinetics.getSpeedOfLocomotion() - sensorRoll(osp);
		double result = (1-owp) * (ofe + osp/5); //inverse wound penalty * (fighting effectiveness + speed*0.2)
		return result;
	}
	
	private double sensorRoll(double d)
	{//to calculate a perceived attribute value of another organism based on this organism's perceptionRadius
		double p = (1-pheno.getPerceptionRadius())/2;
		double roll1 = WorldState.rng3[4].rDouble();
		int roll2 = WorldState.rng3[4].rInt() % 2;
		double result;
		if (roll2 == 0)result = d * (1 - p * roll1);
		else result = d * (1 + p * roll1);
		
		if (result < 0) result = 0;
		else if (result > 1) result = 1;
		
		return result;
	}
	
	

	public int respondShare(TradeRequest trAsk, Organism a){
		//this decided how an entity will respond to being asked to share
		if (potentialEnergy() <= 1) return EventPack.Decline; //not enough food
		double roll = WorldState.rng3[2].rDouble(); 
		Coupling c = findCouple(a);
		
		if (c.isPositiveCouple()) {//good relationship
			if(roll <= c.getCoupleAmount()) //the better the relationship, the more likely to share
				return EventPack.ShareEvent;
			else {
				int d = decideFight(a, false);
				if (d == 3) return EventPack.FightEvent;
				else return EventPack.Decline;
			}
		}
		else {
			if(roll > c.getCoupleAmount()) //the worse the relationship, the less likely to share
				return EventPack.ShareEvent;
			else {
				int d = decideFight(a, false);
				if (d == 3) return EventPack.FightEvent;
				else return EventPack.Decline;
			}
		}
	}//end respondShare
	
		//TRADE STUFF NOT IMPLEMENTED
	/*
	public int respond(TradeRequest trAsk, int eventType) throws AWTException
	{//redirects to the proper response method
		if (eventType == EventPack.TradeEvent) return respondTrade(trAsk);
		else if (eventType == EventPack.ShareEvent) return respondShare(trAsk);
		else return EventPack.Decline;
	}
	
	public int respondGift(TradeRequest trGive)
	{//decide how an organism responds to a gift
		return 0; 
	}
	
	public int respondTrade(TradeRequest trAsk) throws AWTException{
		//how an entity responds to a trade request
		//tr = new TradeRequest(r, this, amount);
		tr = null;
		if (trAsk.getAsker().findResource(WorldState.resourceNum[0])){
			tr = new TradeRequest( new Resource(WorldState.resourceType[0], WorldState.resourceNum[1], this), this, calcCollect());
			return EventPack.TradeEvent;
		}
		else if (trAsk.getAsker().findResource(WorldState.resourceNum[1])){
			tr = new TradeRequest( new Resource(WorldState.resourceType[0], WorldState.resourceNum[0], this), this, calcCollect());
			return EventPack.TradeEvent;
		} 
		
		if (goodMood == false) return EventPack.Decline;
		//decline, fight or share
		return 0;
	}
	
	public void askTrade() throws AWTException
	{//asks the target organism for some food
			if (target.getTheE().findResource(WorldState.resourceNum[0]))
				EventPack.askExchange(this, target.getTheE(), new Resource(WorldState.resourceType[0], WorldState.resourceNum[0], this), WorldState.rng3[1].rInt() % 20, EventPack.TradeEvent);
			else if (target.getTheE().findResource(WorldState.resourceNum[1]))
				EventPack.askExchange(this, target.getTheE(), new Resource(WorldState.resourceType[0], WorldState.resourceNum[1], this), WorldState.rng3[1].rInt() % 20, EventPack.TradeEvent);
	}
	
	public TradeRequest getTradeRequest(){
		return tr;
	}
	*/
	public boolean matingRequest(Organism asker)
	{//decide whether to accept the mating request
		double roll = WorldState.rng3[2].rDouble();
		
		Coupling c = findCouple(asker);

		if (reproPriority == WorldState.NONE || !c.isPositiveCouple()) return false; 
		else if (reproPriority == WorldState.CRITICAL) return true;
		else if (reproPriority == WorldState.HIGH)if (asker.getRFitness() >= rFitness / 2) 
			{
				if (roll <= (findCouple(asker).getCoupleAmount()*2)) return true;
			}
		else if (reproPriority == WorldState.MEDIUM)if (asker.getRFitness() >= rFitness / 1.5) 			
		{
			if (roll <= (findCouple(asker).getCoupleAmount()*1.5)) return true;
		}
		else if (reproPriority == WorldState.LOW) if (asker.getRFitness() >= rFitness) 			
		{
			if (roll <= findCouple(asker).getCoupleAmount()) return true;
		}
		return false; // in case priority is screwed, reject
	}
	
	public int getRpriority()
	{
		return reproPriority;
	}
	
	public void addRFit(int i)
	{
		rFitness += i;
	}
	
	public int getRFitness()
	{
		return rFitness;
	}
	
	public void forcedReproduction(int p, double[] doubles) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException{
		int roll = EventPack.litterSize(p, doubles);
		forcedReproduction(roll);
	}
	
	public void forcedReproduction(int roll) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	{//the creator forces spontaneous reproduction
			
			//if(WorldState.rule127) roll = 2;
			//else roll = (WorldState.rng0[2].rInt() % WorldState.maxLitter) + 1;
			double m = theOrg.getPatch().getMutation();
			int e = energyTransfer(roll);
			//e == WorldState.baseEnergy * roll + reproEnergyCost; //OVERRIDE, ensures entity has same amount of energy
			//hp = maxHp;
			//woundPenalty = 0;
			
			for(int i = 0; i < roll; i++)
			{
				EventPack.reproductionA(this, m, e);
			}
			info.cycle += "r";
			//EventPack.reprodDeath(this);
	}
	
	public Coupling findCouple(Organism o){
		for (int i = 0; i < pairs.size(); i++)
		{
			if (pairs.get(i).isCoupled(o)) return pairs.get(i);
		}
		
		return establishPair(o);
	}

	public Coupling establishPair(Organism o){
		Coupling c = new Coupling(this, o);
		pairs.add(c);
		o.addPairs(c);
		return c;
	}
	
	/**
	 * @param sitCounter the sitCounter to set
	 */
	public void incSitCounter(int sitCounter) {
		this.sitCounter += sitCounter;
	}

	@Override
	public int compareTo(Object o) {
		int rFit = ((Organism)o).getRFitness();
		return rFit - this.rFitness;
	}

	public ArrayList<Resource> getInventory() {
		return inventory;
	}
	
	public void toggleRepro(){
		if (aSexual) aSexual = false;
		else aSexual = true;
	}
	
	public int energyTransfer(int litter)
	{
		if (energy - WorldState.reproEnergyCost <= 0) return -1; //not enough energy
		energy -= WorldState.reproEnergyCost;
		energy /= litter;
		return energy;
	}
	
	public void rFitCalc(){
		//calculate rFitness based on specified values
		rFitness = 0;
	}
	
	public void resetFitness(){
		inventory.clear();
		rFitCalc();
	}
	
	public OrgInfo getInfo()
	{
		return info;
	}
}
