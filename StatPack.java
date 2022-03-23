import java.lang.reflect.Field;
import java.util.Arrays;

public class StatPack {
	
	double orgTotal = 0;
	double startOrg = WorldState.startOrgNum + 1;
	double orgAlive = 0;
	double endTurn = Starter.getTurn();
	double forcedReproductionEvents = Starter.getTurn() / WorldState.forcedReproductionEvent;
	double unableToActTurns = 0;
	
	//deaths
	double starveDeath = 0;
	double woundDeath = 0;
	double fightDeath = 0;
	double cullDeath = 0;
	double reproductionDeath = 0;
	
	//food events
	double foodHarvest = 0;
	double foodReap = 0;
	double foodConsumed = 0;
	double foodTraded = 0;
	double foodShared = 0;
	double foodStolen = 0;
	double replenishedAmount = 0;
	
	//event stats
	double tradeEvents = 0;
	double asexualReproductionEvents = 0;
	double sexualReproductionEvents = 0;
	double shareEvents = 0;
	double theftEvents = 0;
	double fightEvents = 0;
	double harvestEvents = 0;
	double reapEvents = 0;
	double consumeEvents = 0;
	double replenishEvents = 0;
	


	//move stats
	double wanderMoves = 0;
	double moveMoves = 0;
	double huntMoves = 0;
	
	//other stats
	//double avgLitterSize = 0;
	//double avgMovesPerOrg = 0;
	
	public void update()
	{
		endTurn = Starter.getTurn() - 1; //have to -1 because it exits after the turn increments past the end point
		//avgLitterSize = (orgTotal - startOrg) / reproductionDeath;
		//avgMovesPerOrg = (wanderMoves + moveMoves + huntMoves)/ (orgTotal - startOrg);
		forcedReproductionEvents = Starter.getTurn() / WorldState.forcedReproductionEvent;
		orgAlive = Starter.getLifeForms().size();
		orgTotal = WorldState.nameVault.size();
	}
		/**
	 * @param replenishEvents the replenishEvents to set
	 */
	public synchronized void incReplenishEvents(double replenishEvents) {
		this.replenishEvents += replenishEvents;
	}
	
	
	/**
		 * @param replenishedAmount the replenishedAmount to set
		 */
		public synchronized void incReplenishedAmount(double replenishedAmount) {
			this.replenishedAmount += replenishedAmount;
		}
	/**
	 * @param asexualReproductionEvents the asexualReproductionEvents to inc
	 */
	public synchronized void incAsexualReproductionEvents(
			double asexualReproductionEvents) {
		this.asexualReproductionEvents += asexualReproductionEvents;
	}
	/**
	 * @param starveDeath the starveDeath to inc
	 */
	public synchronized void incStarveDeath(double starveDeath) {
		this.starveDeath += starveDeath;
	}

	/**
	 * @param woundDeath the woundDeath to inc
	 */
	public synchronized void incWoundDeath(double woundDeath) {
		this.woundDeath += woundDeath;
	}

	/**
	 * @param fightDeath the fightDeath to inc
	 */
	public synchronized void incFightDeath(double fightDeath) {
		this.fightDeath += fightDeath;
	}

	/**
	 * @param cullDeath the cullDeath to inc
	 */
	public synchronized void incCullDeath(double cullDeath) {
		this.cullDeath += cullDeath;
	}

	/**
	 * @param reproductionDeath the reproductionDeath to inc
	 */
	public synchronized void incReproductionDeath(double reproductionDeath) {
		this.reproductionDeath += reproductionDeath;
	}

	/**
	 * @param foodHarvest the foodHarvest to inc
	 */
	public synchronized void incFoodHarvest(double foodHarvest) {
		this.foodHarvest += foodHarvest;
	}

	/**
	 * @param foodReap the foodReap to inc
	 */
	public synchronized void incFoodReap(double foodReap) {
		this.foodReap += foodReap;
	}

	/**
	 * @param foodConsumed the foodConsumed to inc
	 */
	public synchronized void incFoodConsumed(double foodConsumed) {
		this.foodConsumed += foodConsumed;
	}

	/**
	 * @param foodTraded the foodTraded to inc
	 */
	public synchronized void incFoodTraded(double foodTraded) {
		this.foodTraded += foodTraded;
	}

	/**
	 * @param foodShared the foodShared to inc
	 */
	public synchronized void incFoodShared(double foodShared) {
		this.foodShared += foodShared;
	}

	/**
	 * @param foodStolen the foodStolen to inc
	 */
	public synchronized void incFoodStolen(double foodStolen) {
		this.foodStolen += foodStolen;
	}

	/**
	 * @param tradeEvents the tradeEvents to inc
	 */
	public synchronized void incTradeEvents(double tradeEvents) {
		this.tradeEvents += tradeEvents;
	}

	/**
	 * @param reproductionEvents the reproductionEvents to inc
	 */
	public synchronized void incSexualReproductionEvents(double sexualReproductionEvents) {
		this.sexualReproductionEvents += sexualReproductionEvents;
	}

	/**
	 * @param unableToActTurns the unableToActTurns to set
	 */
	public synchronized void incUnableToActTurns(double unableToActTurns) {
		this.unableToActTurns += unableToActTurns;
	}

	/**
	 * @param shareEvents the shareEvents to inc
	 */
	public synchronized void incShareEvents(double shareEvents) {
		this.shareEvents += shareEvents;
	}

	/**
	 * @param fightEvents the fightEvents to inc
	 */
	public synchronized void incFightEvents(double fightEvents) {
		this.fightEvents += fightEvents;
	}

	/**
	 * @param harvestEvents the harvestEvents to inc
	 */
	public synchronized void incHarvestEvents(double harvestEvents) {
		this.harvestEvents += harvestEvents;
	}

	/**
	 * @param reapEvents the reapEvents to inc
	 */
	public synchronized void incReapEvents(double reapEvents) {
		this.reapEvents += reapEvents;
	}

	/**
	 * @param consumeEvents the consumeEvents to inc
	 */
	public synchronized void incConsumeEvents(double consumeEvents) {
		this.consumeEvents += consumeEvents;
	}

	/**
	 * @param wanderMoves the wanderMoves to inc
	 */
	public synchronized void incWanderMoves(double wanderMoves) {
		this.wanderMoves += wanderMoves;
	}

	/**
	 * @param moveMoves the moveMoves to inc
	 */
	public synchronized void incMoveMoves(double moveMoves) {
		this.moveMoves += moveMoves;
	}

	/**
	 * @param huntMoves the huntMoves to inc
	 */
	public synchronized void incHuntMoves(double huntMoves) {
		this.huntMoves += huntMoves;
	}

	/**
	 * @param theftEvents the theftEvents to set
	 */
	public synchronized void incTheftEvents(double theftEvents) {
		this.theftEvents += theftEvents;
	}

	
	

}
