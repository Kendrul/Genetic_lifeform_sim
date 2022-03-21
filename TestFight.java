import java.util.Random;


public class TestFight {

	BattleState bob;
	BattleState rob;
	int runnerRunner; //True = bob, false = rob
	Random rng;
	boolean isDebug, ranAway;
	
	//CONSTANTS
	private final double retreatFail = 0.1;
	private final double retreatSuccess = 0.9;
	private final double retreatTie = 0.5;
	private final int BOB = 1;
	private final int ROB = 2;
	
	
public TestFight (BattleState a, BattleState b, boolean debugModeOn){
	bob = a;
	rob = b;
	isDebug = debugModeOn;
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
		if ((bob.getWoundPenalty() > 0.75) && (bob.getWoundPenalty() > 0.75))
		{
			break;
		}
		if (bob.battleSpeed() == rob.battleSpeed())
		{//they have the same speed, randomly pick
			choice = rng.nextInt() % 2;
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
	if ((rob.getHp() <= 0) || (runnerRunner == ROB)){
		//bob won
		if (isDebug) System.out.println(bob.getName() + " wins the fight, with " + bob.getHp() + " health remaining.");
		return bob;
	}
	else if ((bob.getHp() <= 0) || (runnerRunner == BOB)){
		//rob won
		if (isDebug) System.out.println(rob.getName() + " wins the fight, with " + rob.getHp() + " health remaining.");
		return rob;
	} else
	{//a draw
		if (isDebug) System.out.println("The fight was a draw, both fighters are too weak to continue.");
		return null;
	}
}
	
private boolean turn(BattleState isTurn, BattleState notTurn)
{
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
	return survival;
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
	
	double roll = rng.nextDouble() ;

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
	runner.setRunner(true);
	ranAway = true;
	if (runner.equals(bob)) runnerRunner = BOB;
	else runnerRunner = ROB;
}

public void plant(int seed){
	rng = new Random(seed);
}


}
