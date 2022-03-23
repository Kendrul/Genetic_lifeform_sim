import java.awt.AWTException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;


public class EventPack {

	
	//--------------------------------------------------------------------------------------------
	//reproduction events
	public synchronized static Organism reproductionS(Organism dad, Organism mom, double mutation) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	{
		GeneticMutation gm = new GeneticMutation(dad, mom, mutation);
		gm.newGenomeS();
		Organism kid = gm.getChild();
		Starter.getLifeForms().add(kid);
		kid.setTheOrg(new OrganismGFX(Starter.getGrid(), dad.getPoint().x, dad.getPoint().y));
		WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + dad.getName() + " and " + mom.getName() + " had a child named " + kid.getName());
		return kid;		
	}
	
	public synchronized static Organism reproductionA(Organism parent, double mutation) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	{
		GeneticMutation gm2 = new GeneticMutation(parent, mutation);
		gm2.newGenomeA();
		Organism kid2 = gm2.getChild();
		Starter.getLifeForms().add(kid2);
		kid2.setTheOrg(new OrganismGFX(Starter.getGrid(), parent.getPoint().x, parent.getPoint().y));
		WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + parent.getName() + " had a child named " + kid2.getName());
		return kid2;
	}
	
	public synchronized static void reprodDeath(Organism o)
	{
		WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + o.getName() + " died from the reproductive process.");
		Starter.entityDeath(o);
	}
	
	public static int litterSize()
	{
		return (WorldState.rng0[3].rInt(WorldState.maxLitter) + 1);
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
		if (giver.findResource(r)) {
			giver.subResource(r, -amount);
			Resource r2 = new Resource(r, amount);
			receiver.addResource(r2, r2.getAmount());
			if (!isTheft){
				WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + giver.getName() + " shared " + amount + " units of " + r.getName() + " with " + receiver.getName());
				Starter.getGrid().getPatch(giver.getPoint()).setLocalEvent("S");
				//coupling benefit
			} else
			{
				WorldState.addLogEvent("[Turn " + Starter.getTurn() + "] " + receiver.getName() + " took " + amount + " units of " + r.getName() + " from " + giver.getName());
				//coupling penalty
			}
		} else {}//do nothing
	}
	
	public static void tradeResource(TradeRequest tr1, TradeRequest tr2)
	{
		tradeResource(tr1.getAsker(), tr2.getAsker(), tr1.getResource(), tr2.getResource(), tr1.getAmount(), tr2.getAmount());
	}
	
	public static void tradeResource(Organism org1, Organism org2, Resource r1, Resource r2, int amount1, int amount2)
	{//resources of different kinds are transfered between two organisms
		WorldState.addLogEvent("[Turn:" + Starter.getTurn() + "] " + org1.getName() + " traded with " + org2.getName());
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
	private static final int [][] eventGrid = {{Decline, TradeEvent, ShareEvent, FightEvent},{Decline, TradeEvent, -1, FightEvent}};
	
	
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
		if ((winner != null) && (winner.equals(asker))) shareResource(responder, asker, r, amount, true);
	}
	
	public static Organism battle(Organism a, Organism b, boolean aStartedIt)
	{					
		if (aStartedIt) a.subActionPoint();
		else b.subActionPoint();
		
		Fight battle = new Fight(a.getFighter(), b.getFighter());
		Starter.getGrid().getPatch(a.getPoint()).setLocalEvent("F");
		BattleState winner = battle.fightSim();
		return winner.getOwner();
	}
	
	public static void askMate(Organism asker, Organism responder) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, AWTException
	{//one organism asks another to mate with them
		if (responder == null) return; //empty request
		boolean willAccept = responder.matingRequest(asker);
		if (willAccept)
			{		
				int roll = (WorldState.rng0[2].rInt() % WorldState.maxLitter) + 1;
				double m = Starter.getGrid().getPatch(asker.getPoint()).getMutation();
			
				for(int i = 0; i < roll; i++)
				{
					reproductionS(asker, responder, m);
					
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
				if(asker.decideFight(responder, true) == FightEvent) 
					{
						battle(asker, responder, true);
					} else return; //do nothing				
			}
	}
	
}
