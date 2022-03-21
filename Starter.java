import java.util.Random;


public class Starter {

	/**
	 * @param args
	 */
	private static boolean isDebug = true; //change this to false to disable output messages
	private static final int bobSize = 2;
	private static final int robSize = 2;
	private static Random rng;
	
	public static void main(String[] args) {
		rng = new Random(123456789);
		WorldState world = new WorldState();
		//--------------------------------------------------------------
		//             Fight Test
		//------------------------------------------------------------
		//Entity(name, hp, damage, speed, crit, avoidance, flightChance
		BattleState bob = new BattleState(new Entity("Bob", 130, 20, 20, 0.1, 0.1, 0.2, world), isDebug);
		BattleState rob = new BattleState(new Entity("Rob", 100, 30, 15, 0.1, 0.1, 0.5, world), isDebug);
		bob.plant(1337);
		rob.plant(7331);
		TestFight fight = new TestFight(bob, rob, isDebug);
		fight.plant(56456);
		if (isDebug) System.out.println("Begin Fight Simulation.");
		fight.fightSim();
		
		//--------------------------------------------------------------
		//             Battle Test
		//------------------------------------------------------------
		BattleState [] teamBob = new BattleState[bobSize];
		BattleState [] teamRob = new BattleState[robSize];
		
		for (int i =0; i < bobSize; i++)
		{
			teamBob[i] = new BattleState(new Entity("Bob", 130, 20, 20, 0.1, 0.1, 0.2, world), isDebug);	
			teamBob[i].plant(rng.nextInt());
		}
		
		for (int i =0; i < robSize; i++)
		{
			teamRob[i] = new BattleState(new Entity("Rob", 100, 30, 15, 0.1, 1, 0.5, world), isDebug);
			teamRob[i].plant(rng.nextInt());
		}
		
		TestBattle testBattle = new TestBattle(teamBob, teamRob, isDebug);
		testBattle.plant(rng.nextInt());
		if (isDebug) {
			System.out.println("-------------------------------------------------------------");
			System.out.println("Begin Battle Simulation. (" + bobSize + " vs " + robSize + ")");
		}
		
		testBattle.battleSim();
		if (isDebug) {
			System.out.println("-------------------------------------------------------------");
			System.out.println("Simulation Complete, exiting.....");
		}
		
	}

}
