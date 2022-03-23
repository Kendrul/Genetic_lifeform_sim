import java.awt.AWTException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;


public class EventPack {

	
	//--------------------------------------------------------------------------------------------
	//reproduction events
	public synchronized static Organism reproductionS(Organism dad, Organism mom, double mutation, int energyD, int energyM) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	{
		int energy = energyD + energyM;
		if (energy == 0) energy = WorldState.baseEnergy;
		GeneticMutation gm = new GeneticMutation(dad, mom, mutation);
		gm.newGenomeS();
		Organism kid = gm.getChild();
		Starter.getLifeForms().add(kid);
		kid.setTheOrg(new OrganismGFX(Starter.getGrid(), dad.getPoint().x, dad.getPoint().y));
		WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + dad.getName() + " and " + mom.getName() + " had a child named " + kid.getName());
		Starter.getStats().incSexualReproductionEvents(1);
		WorldState.addGenerationVault(kid.getInfo());
		kid.getInfo().cycle += "b";
		kid.getInfo().parents = dad.getName() + "." + mom.getName();
		return kid;		
	}
	
	public synchronized static Organism reproductionA(Organism parent, double mutation, int energy) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	{
		if (energy == 0) energy = WorldState.baseEnergy;
		GeneticMutation gm2 = new GeneticMutation(parent, mutation);
		gm2.newGenomeA();
		Organism kid2 = gm2.getChild();
		Starter.getLifeForms().add(kid2);
		kid2.setTheOrg(new OrganismGFX(Starter.getGrid(), parent.getPoint().x, parent.getPoint().y));
		WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + parent.getName() + " had a child named " + kid2.getName());
		Starter.getStats().incAsexualReproductionEvents(1);
		WorldState.addGenerationVault(kid2.getInfo());
		kid2.getInfo().cycle += "b";
		kid2.getInfo().parents = parent.getName();
		return kid2;
	}
	
	public synchronized static void reprodDeath(Organism o)
	{
		if(WorldState.reproductionDeath){
			WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + o.getName() + " died from the reproductive process.");
			Starter.getStats().incReproductionDeath(1);
			Starter.entityDeath(o, "Reproduction");
		}
	}
	
	public static int litterSize()
	{
		return (WorldState.rng0[3].rInt(WorldState.maxLitter) + 1);
	}
	
	public static int litterSize(int p, double [] rule){
		//p is the number of parents, rule is the number of children
		int num;
		num = (int) Math.ceil(p * (1+ rule[1]));
		return num;
	}
	//-------------------------------------------------------------------
	//GENETIC STUFF
	public static reflectPack geneFields(GeneSequence genes) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		Class aClass = genes.getClass();
		Field[] geneSequenceFieldArray = aClass.getDeclaredFields(); //.getDeclaredFields(); 
		double[] genoArray = new double[geneSequenceFieldArray.length]; //automatic adjustment of array length based on number of field variables
		String[] genoArrayNames = new String[geneSequenceFieldArray.length]; //"------------"
		
		
		for (int i = 0; i < geneSequenceFieldArray.length; i++){
			Field f = geneSequenceFieldArray[i];
			genoArray[i] = (double)geneSequenceFieldArray[i].get(genes);
			genoArrayNames[i] = (String)geneSequenceFieldArray[i].getName();					
		}
		
		reflectPack r = new reflectPack(geneSequenceFieldArray, genoArray, genoArrayNames);
		
		return r;
	}
	
	public static reflectPack phenoFields(GeneToPhenotype pheno) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		    Class aClass1 = pheno.getClass();
			Field[] phenoStartFieldArray = aClass1.getDeclaredFields(); //.getDeclaredFields(); 
			
			
			int arrayLengthMinusArrayFields = 0; 
			for (int i = 0; i < phenoStartFieldArray.length; i++){
				Field f = phenoStartFieldArray[i];
				if(f.getType().equals(double.class)){
					arrayLengthMinusArrayFields++; 
				}
			}
			
			double[] phenoArray = new double[arrayLengthMinusArrayFields]; //automatic adjustment of array length based on number of field variables
			String[] phenoArrayNames = new String[arrayLengthMinusArrayFields]; //"------------"
			
			int phenoArrayIterator = 0;
			for (int i = 0; i < phenoStartFieldArray.length; i++){
				Field f = phenoStartFieldArray[i];
				
				//skip the array fields declared in this class
				if(f.getType().equals(double.class)){
					phenoArray[phenoArrayIterator] = (double)phenoStartFieldArray[i].get(pheno);
					phenoArrayNames[phenoArrayIterator] = (String)phenoStartFieldArray[i].getName();
					phenoArrayIterator++; 
				}		
			}
						
			reflectPack r = new reflectPack(phenoStartFieldArray, phenoArray, phenoArrayNames);
			return r;
		}
	
	public static reflectPack kinFields(PhenotypeToKinetics kin) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		    Class aClass1 = kin.getClass();
			Field[] kinStartFieldArray = aClass1.getDeclaredFields(); //.getDeclaredFields(); 
			
			
			int arrayLengthMinusArrayFields = 0; 
			for (int i = 0; i < kinStartFieldArray.length; i++){
				Field f = kinStartFieldArray[i];
				if(f.getType().equals(double.class)){
					arrayLengthMinusArrayFields++; 
				}
			}
			
			double[] kinArray = new double[arrayLengthMinusArrayFields]; //automatic adjustment of array length based on number of field variables
			String[] kinArrayNames = new String[arrayLengthMinusArrayFields]; //"------------"
			
			int phenoArrayIterator = 0;
			for (int i = 0; i < kinStartFieldArray.length; i++){
				Field f = kinStartFieldArray[i];
				
				//skip the array fields declared in this class
				if(f.getType().equals(double.class)){
					kinArray[phenoArrayIterator] = (double)kinStartFieldArray[i].get(kin);
					kinArrayNames[phenoArrayIterator] = (String)kinStartFieldArray[i].getName();
					phenoArrayIterator++; 
				}		
			}
						
			reflectPack r = new reflectPack(kinStartFieldArray, kinArray, kinArrayNames);
			return r;
		}
	
	public static reflectPack statFields(StatPack stat) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		    Class aClass1 = stat.getClass();
			Field[] statStartFieldArray = aClass1.getDeclaredFields(); //.getDeclaredFields(); 
			
			
			int arrayLengthMinusArrayFields = 0; 
			for (int i = 0; i < statStartFieldArray.length; i++){
				Field f = statStartFieldArray[i];
				if(f.getType().equals(double.class)){
					arrayLengthMinusArrayFields++; 
				}
			}
			
			double[] statArray = new double[arrayLengthMinusArrayFields]; //automatic adjustment of array length based on number of field variables
			String[] statArrayNames = new String[arrayLengthMinusArrayFields]; //"------------"
			
			int phenoArrayIterator = 0;
			for (int i = 0; i < statStartFieldArray.length; i++){
				Field f = statStartFieldArray[i];
				
				//skip the array fields declared in this class
				if(f.getType().equals(double.class)){
					statArray[phenoArrayIterator] = (double)statStartFieldArray[i].get(stat);
					statArrayNames[phenoArrayIterator] = (String)statStartFieldArray[i].getName();
					phenoArrayIterator++; 
				}		
			}
						
			reflectPack r = new reflectPack(statStartFieldArray, statArray, statArrayNames);
			return r;
		}
	
	public static reflectPack infoFields(OrgInfo o) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		    Class aClass1 = o.getClass();
			Field[] statStartFieldArray = aClass1.getDeclaredFields(); //.getDeclaredFields(); 
			
			
			int arrayLengthMinusArrayFields = 0; 
			for (int i = 0; i < statStartFieldArray.length; i++){
				Field f = statStartFieldArray[i];
				if(f.getType().equals(String.class)){
					arrayLengthMinusArrayFields++; 
				}
			}
			
			String[] statArray = new String[arrayLengthMinusArrayFields]; //automatic adjustment of array length based on number of field variables
			String[] statArrayNames = new String[arrayLengthMinusArrayFields]; //"------------"
			
			int phenoArrayIterator = 0;
			for (int i = 0; i < statStartFieldArray.length; i++){
				Field f = statStartFieldArray[i];
				
				//skip the array fields declared in this class
				if(f.getType().equals(String.class)){
					statArray[phenoArrayIterator] = (String)statStartFieldArray[i].get(o);
					statArrayNames[phenoArrayIterator] = (String)statStartFieldArray[i].getName();
					phenoArrayIterator++; 
				}		
			}
						
			reflectPack r = new reflectPack(statStartFieldArray, statArray, statArrayNames);
			return r;
		}
	
	public static GeneSequence randomGS() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException{
		//WorldState.addLogEvent("Begin GeneSequence Manipulation....");
		GeneSequence aGeneSequence = new GeneSequence(); 
        Class aClass = aGeneSequence.getClass();
		Field[] geneSequenceFieldArray = aClass.getDeclaredFields(); //.getDeclaredFields(); 
		double[] genoArray = new double[geneSequenceFieldArray.length]; //automatic adjustment of array length based on number of field variables
		String[] genoArrayNames = new String[geneSequenceFieldArray.length]; //"------------"
		
		
		for (int i = 0; i < geneSequenceFieldArray.length; i++){
			Field f = geneSequenceFieldArray[i];
			f.setAccessible(true);
			
			f.set(aGeneSequence, WorldState.rng0[5].rDouble());
			genoArray[i] = (double)geneSequenceFieldArray[i].get(aGeneSequence);
			genoArrayNames[i] = (String)geneSequenceFieldArray[i].getName();					
		}
		aGeneSequence.update();
		return aGeneSequence;
	}
	
	public static void shareResource(Organism giver, Organism receiver, Resource r, int amount, boolean isTheft)
	{//resources are transfered from one organism to another
		if (giver == null || receiver == null || r == null) return; //just in case of broken links
		if (giver.findResource(r)) {
			amount = Math.floorDiv(r.getAmount(), 2); //OVERRIDE, GIVE A MAX OF HALF
			giver.subResource(r, -amount);
			Resource r2 = new Resource(r, amount);
			receiver.addResource(r2, r2.getAmount());
			if (!isTheft){
				WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + giver.getName() + " shared " + amount + " units of " + r.getName() + " with " + receiver.getName());
				Starter.getStats().incShareEvents(1);
				Starter.getStats().incFoodShared(amount);
				Starter.getGrid().getPatch(giver.getPoint()).setLocalEvent("S");
				//coupling benefit
			} else
			{
				WorldState.addLogEvent("[Turn " + Starter.getTurn() + "] " + receiver.getName() + " took " + amount + " units of " + r.getName() + " from " + giver.getName());
				Starter.getStats().incTheftEvents(1);
				Starter.getStats().incFoodStolen(amount);
				//coupling penalty
			}
		} else {}//do nothing
		
		int n = WorldState.rng3[4].rInt() % WorldState.sitMax;
		giver.incSitCounter(n);
		receiver.incSitCounter(n);
	}
	
	public static void tradeResource(TradeRequest tr1, TradeRequest tr2)
	{
		tradeResource(tr1.getAsker(), tr2.getAsker(), tr1.getResource(), tr2.getResource(), tr1.getAmount(), tr2.getAmount());
	}
	
	public static void tradeResource(Organism org1, Organism org2, Resource r1, Resource r2, int amount1, int amount2)
	{//resources of different kinds are transfered between two organisms
		WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + org1.getName() + " traded with " + org2.getName());
		//Starter.getStats().incTradeEvents(1);
		shareResource(org1, org2, r1, amount1, false);
		shareResource(org2, org1, r2, amount2, false);
		Starter.getGrid().getPatch(org1.getPoint()).setLocalEvent("T");
	}
	
	//INTERACTION EVENTS
	//											//Trade 0: Decline,Trade, Share, Fight
	public static final int TRADE_SHARE = 0;
	public static final int TRADEBACK = 1;
	public static final int SHARE = 2;
	
	public static final int Decline = 0;
	public static final int TradeEvent = 1;
	public static final int ShareEvent = 2;
	public static final int FightEvent = 3;
	private static final int [][] eventGrid = {{Decline, TradeEvent, ShareEvent, FightEvent},{Decline, TradeEvent, -1, FightEvent}, {Decline, TradeEvent, ShareEvent, FightEvent}};
	
	/*
	public static void askExchange(Organism asker, Organism responder, Resource r, int amount, int thisEvent) throws AWTException
	{
		Organism winner = null;
		TradeRequest tr = new TradeRequest(r, asker, amount);
		int answer = responder.respond(tr, thisEvent);
		int result = eventGrid[TRADE_SHARE][answer];
		if (result == FightEvent) winner = battle(asker, responder, false);
			//organism declined to trade/share, might have attacked
			//should we attack?				 
		else if (result == Decline){
			if(asker.decideFight(responder, true) == FightEvent) battle(asker, responder, true);
			else return; //no further interaction
		}
			else if (result == TradeEvent) 
				{
					TradeRequest responseTR = responder.getTradeRequest();
					answer = asker.respondTrade(tr);
					result = eventGrid[TRADEBACK][answer];
					if (result == Decline) return; //no further interaction this turn
					else if (result == FightEvent) winner = battle(asker, responder, true);
					else if (result == TradeEvent) tradeResource(tr, responseTR);
				}
			else if (result == ShareEvent) shareResource(responder, asker, r, amount, false);
		if ((winner != null) && (winner.equals(asker))) {
			shareResource(responder, asker, r, amount, true);
			Starter.entityDeath(responder);
		} else if ((winner != null) && (winner.equals(responder))) Starter.entityDeath(asker);
	}
	*/
	public static Organism battle(Organism a, Organism b, boolean aStartedIt)
	{					
		if (aStartedIt) a.subActionPoint();
		else b.subActionPoint();
		
		Fight battle = new Fight(a.getFighter(), b.getFighter());
		Starter.getGrid().getPatch(a.getPoint()).setLocalEvent("F");
		BattleState winner = battle.fightSim();
		return winner.getOwner();
	}
	
	public static void battleWrapper(Organism a, Organism b, boolean aStartedIt, boolean aboutFood)
	{
		if (a == null || b == null) return; //something broke
		Organism winner = battle(a, b, true);
		if ((winner != null) && (winner.equals(a))) {
			if (aboutFood && b.hasFood()) EventPack.shareResource(b, a, b.getInventory().get(0), b.getInventory().get(0).getAmount(), true);
			Starter.entityDeath(b, "Fight");
			Starter.getStats().incFightDeath(1);
		}
		else if ((winner != null) && (winner.equals(b))) 
			{
				Starter.entityDeath(a, "Fight");
				Starter.getStats().incFightDeath(1);
			}
		
		winner.incSitCounter(1);
	}
	
	public static void askMate(Organism asker, Organism responder) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	{//one organism asks another to mate with them
		if (responder == null) return; //empty request
		boolean willAccept = responder.matingRequest(asker);
		if (willAccept)
			{		
				int roll = (WorldState.rng0[2].rInt() % WorldState.maxLitter) + 1;
				double m = Starter.getGrid().getPatch(asker.getPoint()).getMutation();
				int eA = asker.energyTransfer(roll);
				int eR = responder.energyTransfer(roll);
				if (eA == 0 || eR == 0) return; //no energy
			
				for(int i = 0; i < roll; i++)
				{
					reproductionS(asker, responder, m, eA, eR);				
				}
				reprodDeath(asker);	
				reprodDeath(responder);
				
				Starter.getGrid().getPatch(asker.getPoint()).setLocalEvent("R");
				for (int i=0; i < 2; i++){
					//no actions taken while reproducing
					asker.subActionPoint();
					responder.subActionPoint();
				}	
			}else {
				if(asker.decideFight(responder, true) == FightEvent) battleWrapper(asker, responder, true, false);
					else return; //do nothing				
			}
	}
	
	public synchronized static void askShare(TradeRequest tr, Organism a, Organism g){
		int answer = g.respondShare(tr, a);
		int result = eventGrid[SHARE][answer];
		if (result == FightEvent) battleWrapper(a, g, false, true);
			//organism declined to trade/share, might have attacked
			//should we attack?				 
		else if (result == Decline){
			if(a.decideFight(g, true) == FightEvent) battleWrapper(a, g, true, true);
			else return; //no further interaction
		}
			else if (result == ShareEvent) shareResource(g, a, tr.getResource(), tr.getAmount(), false);
	}
}
