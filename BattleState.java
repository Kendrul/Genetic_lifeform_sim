/*BattleState.java
 * CPSC 565 W2016: Project
 * Jason Schneider and Emil Emilov-Dulguerov
 * This class acts as an interface between Organism and Fight
 * 
 */

import java.util.Random;

public class BattleState {
	
	private Organism owner;
	private boolean isDebug = WorldState.isDebug;
	private boolean runner;
	private boolean cornered;
	private double flightRisk;

	int teamNumber;
	
	public int getTeamNumber() {
		return teamNumber;
	}


	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}


	public BattleState(Organism theEntity)
	{
		owner = theEntity;
		runner = false;
	}
	

	public void debugSet(boolean setDebugMode)
	{
		isDebug = setDebugMode;
	}
	
	public boolean chooseAttack()
	{//this is where the entity decides if it will fight or flight
		//if the entity is too wounded, it does nothing
		if (owner.getBattlePenalty() < 0.75) {
		double roll = WorldState.rng2[3].rDouble();
		//if (owner.getBattlePenalty() > 0.5) roll -= (owner.getBattlePenalty() * 2); //wounded animal more likely to run
		if (roll < (flightRisk)) {
			//chosen to run
			return false;
		}//fight chosen
		else return true;		
			} else return true; //too wounded to run
	}
	
	public int attackAction()
	{
		if (owner.getBattlePenalty() < 0.75) {
		double roll = WorldState.rng2[4].rDouble();
		
		if (roll < owner.getCrit()) {
			if (!cornered) return (int) Math.ceil(owner.getAttack() * 2);
			else return (int) Math.ceil(owner.getAttack() * 2.25);
		}
		else if (!cornered) return owner.getAttack();
			else return (int) Math.ceil(owner.getAttack() * 1.25 );
		//too wounded to fight back
		} else {
			if (isDebug) System.out.println(owner.getName() + " is too wounded to react.");
			return 0;
		}
	}
	
	public boolean survivedAttack(int damage, BattleState attacker)
	{
		if (damage <= 0) return true;
		double roll = WorldState.rng2[5].rDouble();
		if (roll >= owner.getDodge()) {
			if (!cornered) owner.setHp(owner.getHp() - damage);
			else owner.setHp(owner.getHp() - (int) Math.floor(damage * 0.75));
		//owner.setWoundPenalty(1.0 - ((double) owner.getHp() / (double) owner.getMaxHp()));
		//if (isDebug) System.out.println(name + " has a woundPenalty level of " + woundPenalty + ".");
		if (isDebug) System.out.println(attacker.getName() + " attacks " + owner.getName() + " for " + damage + " health, " + owner.getHp() + " remaining.");
		//true means entity survived, false means entity has died 
		if (owner.getHp() <= 0) {
			if (isDebug) System.out.println(owner.getName() + " was killed.");
			WorldState.addLogEvent(owner.getName() + " was killed.");
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
		return (int) (owner.getSpeed() * (owner.getBattlePenalty() * 2));
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
		return owner.getBattlePenalty();
	}

	public void setWoundPenalty(double woundPenalty) {
		owner.setWoundPenalty(woundPenalty);
	}

	public double getFlightTendency() {
		return flightRisk;
	}

	public void setFlightTendency(double flightTendency) {
		flightRisk = flightTendency * WorldState.baseFlight;
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


	public Organism getOwner() {
		return owner;
	}


	public void setOwner(Organism owner) {
		this.owner = owner;
	}


	/**
	 * @return the cornered
	 */
	public boolean isCornered() {
		return cornered;
	}


	/**
	 * @param cornered the cornered to set
	 */
	public void setCornered(boolean cornered) {
		this.cornered = cornered;
	}
	
	
}
