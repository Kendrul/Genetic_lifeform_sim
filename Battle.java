

import java.util.ArrayList;
import java.util.Random;



public class Battle {
	
	BattleState [] teamBob;
	int bobCount;
	int robCount;
	BattleState [] teamRob;
	ArrayList<BattleState> actionList;
	boolean isDebug;

	
	private final int BOB = 1;
	private final int ROB = 2;
	private final double retreatFail = 0.1;
	private final double retreatSuccess = 0.9;
	private final double retreatTie = 0.5;
	
	public Battle(BattleState [] teamA, BattleState [] teamB, boolean debugModeOn){ 
		teamBob = teamA;
		teamRob = teamB;
		isDebug = debugModeOn;
		bobCount = teamBob.length;
		robCount = teamRob.length;
	}
	
	public void battleSim()
	{
		battleInit();
		while ((bobCount > 0) && (robCount > 0))
		{
			turnList();
			
			for(int i = 0; i < actionList.size(); i++)
			{
				if ((bobCount == 0) || (robCount == 0)) break; //one team has been killed or fled
				//entity 0
				BattleState yourTurn = actionList.get(i);
				if (yourTurn.getRunner()) continue; //entity ran away, skip turn
				if (!yourTurn.isAlive()) continue; //entity died, skip
				//decide action
				boolean chooseAttack = yourTurn.chooseAttack();
				//decide target (if not fleeing)
				if (chooseAttack) {
					BattleState target = pickTarget(yourTurn, yourTurn.getTeamNumber());
					if (target == null) continue; //no valid targets
					//act
					int damage = yourTurn.attackAction();
					//consequence
					target.survivedAttack(damage, yourTurn);
					if (!target.isAlive())
					{
						if (target.getTeamNumber() == BOB) bobCount--;
						else robCount--;
					}
				}//end if-then
				else {
					//act & consequence
					boolean runSuccess = flight(yourTurn);
					if (runSuccess) escaped(yourTurn);
				}
			}//end for loop
		}//end WHILE loop
		
		//Victory Message
		if (robCount <= 0){
			//bob won
			if (isDebug) System.out.println("Team One wins the battle, with " + bobCount + " entities remaining.");
			WorldState.addLogEvent("Team One wins the battle, with " + bobCount + " entities remaining.");
			
		}
		else if (bobCount <= 0){
			//rob won
			if (isDebug) System.out.println("Team Two wins the battle, with " + robCount + " entities remaining.");
			WorldState.addLogEvent("Team Two wins the battle, with " + robCount + " entities remaining.");
		} else
		{//a draw
			if (isDebug) System.out.println("The fight was a draw, both sides are too weak to continue.");
			WorldState.addLogEvent("The fight was a draw, both sides are too weak to continue.");
		}
	}//end battleSim
	
	private void battleInit()
	{
		for (int i = 0; i < teamBob.length; i++)
		{
			teamBob[i].setTeamNumber(BOB);
			teamBob[i].setRunner(false);
		}
		for (int i = 0; i < teamRob.length; i++)
		{
			teamRob[i].setTeamNumber(ROB);
			teamRob[i].setRunner(false);
		}
	}
	
	private void turnList()
	{//entities need to be sorted by speed
		actionList = new ArrayList<BattleState>();
		int size = 0;
		
		for (int i = 0; i < teamBob.length; i++)
		{
			if (actionList.isEmpty()) 
			{
				if ((teamBob[i].getHp() > 0) && !teamBob[i].getRunner())
				{
					actionList.add(teamBob[i]);
				}
			} else 
				{ size = actionList.size();
					for (int j = 0; j <= size; j++) {
						if (j == actionList.size()) actionList.add(teamBob[i]);
						else if (actionList.get(j).getSpeed() < teamBob[i].getSpeed()) actionList.add(j, teamBob[i]);
				}//end for loop
			}//end if-else
		}//end teamBob for loop
		//TeamRob for loop
		for (int i = 0; i < teamRob.length; i++)
		{
			if (actionList.isEmpty()) 
			{
				if ((teamRob[i].getHp() > 0) && !teamRob[i].getRunner())
				{
					actionList.add(teamRob[i]);
				}
			} else 
				{ size = actionList.size();
					for (int j = 0; j <= size; j++) {
						if (j == actionList.size()) actionList.add(teamRob[i]);
						else if (actionList.get(j).getSpeed() < teamRob[i].getSpeed()) actionList.add(j, teamRob[i]);
				}//end for loop
			}//end if-else
		}//end teamBob for loop
	}//end turnList
	
	private BattleState pickTarget(BattleState isTurn, int team){
		BattleState [] targetList = null;
		if (team == BOB) targetList = teamRob;
		else targetList = teamBob;
		int targ, count = targetList.length;
		
		for (int i = 0; i < targetList.length; i++)
		{
			if ((targetList[i].getRunner()) || (!targetList[i].isAlive()))
				{
					count--;
				}
		}
		if (count <= 0) return null; //no valid targets
		do {
		//change this to modify targeting priorities
			targ = WorldState.rng2[1].rInt(targetList.length);
			if (targ < 0) targ = targ * -1;
		} while ((targetList[targ].getRunner()) || (!targetList[targ].isAlive()));
	
		return targetList[targ]; 
	}
	
	/**
	 * 
	 * @param runner
	 * @param chaser
	 * @return True = runner escaped, False = escape failed
	 */
	private boolean flight(BattleState runner)
	{//calculates whether an escape attempt was successful
		int speedRun = runner.battleSpeed();
		int speedChase = 0;
		
		if (runner.getTeamNumber() == BOB){
			int count = 0;
			for (int i = 0; i < teamRob.length; i++)
			{
				if (!teamRob[i].getRunner()) {
					count++;
					speedChase += teamRob[i].battleSpeed();
				}
			}
			speedChase = speedChase / count;
		} else {
			int count = 0;
			for (int i = 0; i < teamBob.length; i++)
			{
				if (!teamBob[i].getRunner()) {
					count++;
					speedChase += teamBob[i].battleSpeed();
				}
			}
			speedChase = speedChase / count;
		}//if-then-else
		
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
		} else {//Chaser is faster
			double runFactor = 1 - (speedRun / speedChase);
			if (roll > (retreatTie + runFactor)) return true;
			else return false;
		}
	}

	private void escaped(BattleState runner)
	{//updates flag (and debugMode output) for a successful escape
		if (isDebug) System.out.println(runner.getName() + " fled the fight.");
		WorldState.addLogEvent(runner.getName() + " fled the fight.");
		runner.setRunner(true);
		if (runner.getTeamNumber() == BOB) bobCount--;
		else robCount--;
	}
	
}
