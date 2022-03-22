/*Fight.java
 * CPSC 565 W2016: Project
 * Jason Schneider and Emil Emilov-Dulguerov
 * This class facilitates combat between two organisms
 * 
 */

import java.util.Random;



public class Fight {

	BattleState bob;
	BattleState rob;
	int runnerRunner; //True = bob, false = rob

	boolean isDebug = WorldState.isDebug, ranAway;
	
	//CONSTANTS
	private final double retreatFail = 0.01;
	private final double retreatSuccess = 0.99;
	private final double retreatTie = 0.75;
	private final int BOB = 1;
	private final int ROB = 2;
	
	
public Fight (BattleState a, BattleState b){
	bob = a;
	rob = b;
	bob.debugSet(isDebug);
	rob.debugSet(isDebug);
	ranAway = false;
	runnerRunner = 0;
}

public BattleState fightSim()
{
	int choice;
	
	//loop runs while both entities survive, and none have run away
	while (((bob.getHp() > 0) && (rob.getHp() > 0)) & !ranAway)
	{
		choice = 2;
		if ((bob.getWoundPenalty() > 0.75) && (rob.getWoundPenalty() > 0.75))
		{//noth organisms are too wounded to continue
			if (isDebug) System.out.println("The fight was a draw, both fighters are too weak to continue.");
			WorldState.addLogEvent("The fight was a draw, both fighters are too weak to continue.");
			return null;
		}
		if (bob.battleSpeed() == rob.battleSpeed())
		{//they have the same speed, randomly pick
			choice = WorldState.rng2[0].rInt(2);
			//0 = bob, 1 = rob
		}
		
	
		if ((bob.battleSpeed() > rob.battleSpeed()) || (choice == 0)) {
			//bob goes first
			if (turn(bob, rob) & !ranAway) 
			{//rob survived, his turn
				turn(rob, bob);
			}
				
		} else if ((rob.battleSpeed() > bob.battleSpeed()) || (choice == 1))
		{
			//rob goes first
			if (turn(rob, bob) & !ranAway) 
			{//bob survived, his turn
				turn(bob, rob);
			}
			
		}
	}//end while loop
	Starter.getStats().incFightEvents(1);
	Starter.getTurnStats().incFightEvents(1);
	if ((rob.getHp() <= 0) || (runnerRunner == ROB)){
		//bob won
		if (isDebug) System.out.println(bob.getName() + " wins the fight, with " + bob.getHp() + " health remaining.");
		WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + bob.getName() + " wins the fight, with " + bob.getHp() + " health remaining.");
		if (runnerRunner == ROB) 
			{
				bob.getOwner().incFightWon(1);
				Starter.getStats().incFledEvents(1);
				Starter.getTurnStats().incFledEvents(1);
				rob.setRunner(false); //reset value
				return null;
			}
		else return bob;
	}
	else if ((bob.getHp() <= 0) || (runnerRunner == BOB)){
		//rob won
		if (isDebug) System.out.println(rob.getName() + " wins the fight, with " + rob.getHp() + " health remaining.");
		WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + rob.getName() + " wins the fight, with " + rob.getHp() + " health remaining.");
		if (runnerRunner == BOB) 
		{
			rob.getOwner().incFightWon(1);
			Starter.getStats().incFledEvents(1);
			Starter.getTurnStats().incFledEvents(1);
			bob.setRunner(false); //reset value
			return null;
		}
		else return rob;
	} else
	{//a draw, should not reach this code
		if (isDebug) System.out.println("The fight was a draw, both fighters are too weak to continue.");
		WorldState.addLogEvent("The fight was a draw, both fighters are too weak to continue.");
		return null;
	}
}
	
private boolean turn(BattleState isTurn, BattleState notTurn)
{
	double roll = WorldState.rng2[3].rInt(2);
	if (roll > isTurn.getFlightTendency()) {
		//ran away
		escaped(isTurn);
		return true;
	}
	else {	//hit and kill
		if (isTurn.getAttack() >= notTurn.getAttack()) notTurn.setHp(0);
		else return true; //not strong enough to win
		return false;
	}
	/*
	boolean survival;
	if (isTurn.chooseAttack()) 
	{
		//rob chose to attack
		survival = attackTurn(isTurn, notTurn);
	} else 
		{
			if (flight(isTurn, notTurn)) 
			{
				//escape successful
				escaped(isTurn);				
			} else if (isDebug) System.out.println(isTurn.getName() + " tried to flee, it failed.");
			survival = true;
		}
	return survival; */
}

	/**
	 * 
	 * @param attacker
	 * @param defender
	 * @return False = defender died, True = defender lived
	 */
private boolean attackTurn(BattleState attacker, BattleState defender) {
	int amount = attacker.attackAction();
	return defender.survivedAttack(amount, attacker);
}

/**
 * 
 * @param runner
 * @param chaser
 * @return True = runner escaped, False = escape failed
 */
private boolean flight(BattleState runner, BattleState chaser)
{//calculates whether an escape attempt was successful
	int speedRun = runner.battleSpeed();
	int speedChase = chaser.battleSpeed();
	
	double roll = WorldState.rng2[2].rDouble() ;

	if (roll < retreatFail) return false; //escape auto-fail
	else if (roll >= retreatSuccess) return true; //escape success
	else if (speedRun == speedChase)
	{//they have same speed
		if (roll > retreatTie) return true; //escape succesful
		else return false; //escape failed
	} else if (speedRun > speedChase)
	{//Runner is faster
		double runFactor = 1 - (speedChase / speedRun);
		if (roll > (retreatTie - runFactor)) return true;
		else return false;
	} else {
		double runFactor = 1 - (speedRun / speedChase);
		if (roll > (retreatTie + runFactor)) return true;
		else return false;
	}
}

private void escaped(BattleState runner)
{//updates flag (and debugMode output) for a successful escape
	if (isDebug) System.out.println(runner.getName() + " fled the fight.");
	WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + runner.getName() + " fled the fight.");
	runner.setRunner(true);
	ranAway = true;
	if (runner.equals(bob)) runnerRunner = BOB;
	else runnerRunner = ROB;
}
}
