import java.io.PrintWriter;
import java.util.ArrayList;

/*FileOutput.java
 * CPSC 565 W2016: Project
 * Jason Schneider and Emil Emilov-Dulguerov
 * This class creates and formats the output for log and stats files
 * 
 */
public class FileOutput {

	private boolean isDebug = WorldState.isDebug;
	String logName = WorldState.logFile;
	String statName = WorldState.statFile;
	String orgFileName = WorldState.orgFile;
	String finalStatName = WorldState.finalStatFile;
	private boolean finalRunFlag = false;
	private int simRun = 0;
	
	private StatPack stats;
	private ArrayList<GeneSequence> geneVault = new ArrayList<GeneSequence>();
	private ArrayList<OrgInfo> generationVault= new ArrayList<OrgInfo>();
	private ArrayList<StatPack> turnVault= new ArrayList<StatPack>();
	private ArrayList<StatPack> totalVault= new ArrayList<StatPack>();
	
	public synchronized void outputFiles()
	{
		simRun = Starter.getSimRun();
		stats = totalVault.get(0);
		//outFileLog();
		outFileFinal();
		outFileStat();	
		outFileOrg();		
	}
	
	/**
	 * 
	 * @param stat name of stat output file
	 * @param log name of event log output file
	 */
	public synchronized void outputFiles(String stat, String log, String org, String finals)
	{
		logName = log;
		statName = stat;
		orgFileName = org;
		finalStatName = finals;
		simRun = Starter.getSimRun();
		stats = Starter.getStats();
		totalVault.add(stats);
		geneVault = WorldState.geneVault;
		generationVault = WorldState.generationVault;
		turnVault = WorldState.turnVault;
		
		outFileLog();
		outFileFinal();
		outFileStat();	
		outFileOrg();
	}
	
	public synchronized void outFileLog()
	{//this method creates the .log file
		try {
				System.out.println("Begin writing to file: " + WorldState.logFile); //for testing
				PrintWriter out = new PrintWriter(logName);
				ArrayList<String> theList = WorldState.getLog();
				int size = theList.size();
				
				for(int i = 0; i < size; i++)
				{				
					out.println(theList.get(i));
					if(isDebug) System.out.println(theList.get(i));
				}//end for loop
				
				if(isDebug) System.out.println("Finished."); //for testing
				out.close();
		} catch (Exception e)
		{
			System.out.println("Log File writing failed...");
			System.out.println(e);
		}//end catch block
	}//end outFileL

	public synchronized void outFileOrg(){
		try {
			System.out.println("Begin writing to file: " + WorldState.orgFile); //for testing
			
			PrintWriter out = new PrintWriter(orgFileName);

			ArrayList<String> statLine = gatherOrgs();
			int size = statLine.size();
			
			for(int i = 0; i < size; i++)
			{				
				out.println(statLine.get(i));
				if(isDebug) System.out.println(statLine.get(i));
				//System.out.println(statLine.get(i));
			}//end for loop
			
			if(isDebug) System.out.println("Finished."); //for testing
			out.close();
	} catch (Exception e)
	{
		System.out.println("Org File writing failed...");
		System.out.println(e);
	}//end catch block
	}
	public synchronized void outFileFinal(){
		try {
			System.out.println("Begin writing to file: " + WorldState.finalStatFile); //for testing
			
			PrintWriter out = new PrintWriter(finalStatName);

			ArrayList<String> statLine = gatherFinalStats();
			int size = statLine.size();
			
			for(int i = 0; i < size; i++)
			{				
				out.println(statLine.get(i));
				if(isDebug) System.out.println(statLine.get(i));
				//System.out.println(statLine.get(i));
			}//end for loop
			
			if(isDebug) System.out.println("Finished."); //for testing
			out.close();
	} catch (Exception e)
	{
		System.out.println("Stat File writing failed...");
		System.out.println(e);
	}//end catch block
	}
	
	public synchronized void outFileStat(){
		try {
			System.out.println("Begin writing to file: " + WorldState.statFile); //for testing
			
			PrintWriter out = new PrintWriter(statName);

			ArrayList<String> statLine = gatherStats();
			int size = statLine.size();
			
			for(int i = 0; i < size; i++)
			{				
				out.println(statLine.get(i));
				if(isDebug) System.out.println(statLine.get(i));
				//System.out.println(statLine.get(i));
			}//end for loop
			
			if(isDebug) System.out.println("Finished."); //for testing
			out.close();
	} catch (Exception e)
	{
		System.out.println("Stat File writing failed...");
		System.out.println(e);
	}//end catch block
	}

	public ArrayList<String> gatherStats() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		
		ArrayList<String> statLine = new ArrayList<String>();
		//String newString = "Final Stats";
		//statLine.add(newString);
		//Add Statistical Information from StatPack Class
				//stats.update();
				reflectPack rs = EventPack.statFields(stats);
				String newString = "Simulation Run,Generation,";
				int n = (WorldState.turns / WorldState.forcedReproductionEvent), m =0, k=0;
				
				for(int i =0; i < rs.getFieldArrayNames().length; i++)
				{
					newString += rs.getFieldArrayNames()[i];
					newString += ",";
				}
				
				statLine.add(newString);
				
				//System.out.println("turnVault size: " + WorldState.turnVault.size());
				for (int i =0; i < turnVault.size(); i++) {
					//newString = WorldState.nameVault.get(i) + "," + WorldState.generationVault.get(i) + ",";
					rs = EventPack.statFields(turnVault.get(i));
					
					if (!finalRunFlag) newString = simRun + "," + k + ",";
					else 
						{						
							newString = m + "," + k +",";
						}
					for (int j =0; j < rs.getFieldArrayValues().length; j++)
					{
						newString += rs.getFieldArrayValues()[j];
						newString += ",";
					}
					//System.out.println(newString);
					statLine.add(newString);
					if (k >= n) {
						k = 0;
						m++;
					}else k++;
				}
				
		return statLine;
	}
	public ArrayList<String> gatherFinalStats() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		
		ArrayList<String> statLine = new ArrayList<String>();
		//String newString = "Final Stats";
		//statLine.add(newString);
		//Add Statistical Information from StatPack Class
				//stats.update();
				reflectPack rs = EventPack.statFields(totalVault.get(0));
				String newString = "Simulation Run,Generation,";
				
				for(int i =0; i < rs.getFieldArrayNames().length; i++)
				{
					newString += rs.getFieldArrayNames()[i];
					newString += ",";
				}
				
				statLine.add(newString);
				
				for(int i =0; i < totalVault.size(); i++)
				{
					if (!finalRunFlag) newString = simRun + ",Final,";
					else newString = i + ",Final,";
					rs = EventPack.statFields(totalVault.get(i));
					
					for (int j =0; j < rs.getFieldArrayValues().length; j++)
					{
						newString += Double.toString(rs.getFieldArrayValues()[j]);
						newString += ",";
					}
					statLine.add(newString);
				}
				
		return statLine;
	}

	private ArrayList<String> gatherOrgs()throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException{
		ArrayList<String> statLine = new ArrayList<String>();
		
		//next table of genetic information
		//newString = "Number, Name, Age, Generation, Life-Cycle, Birth-Turn, Death-Turn, Death Reason, Parents";
		String newString ="";
		reflectPack ri = EventPack.infoFields(generationVault.get(0));		
		
		reflectPack rg = EventPack.geneFields(geneVault.get(0));
		reflectPack rp = EventPack.phenoFields(new GeneToPhenotype(rg.getFieldArrayValues(),rg.getFieldArrayNames()));
		reflectPack rk = EventPack.kinFields(new PhenotypeToKinetics(rp.getFieldArrayValues(),rp.getFieldArrayNames()));
		
		for(int i =0; i < ri.getFieldArrayNames().length; i++)
		{
			//if(i==0) newString = r.getFieldArrayNames()[0];
			newString += ri.getFieldArrayNames()[i];
			newString += ",";
		}
		for(int i =0; i < rg.getFieldArrayNames().length; i++)
		{
			//if(i==0) newString = r.getFieldArrayNames()[0];
			newString += rg.getFieldArrayNames()[i];
			newString += ",";
		}
		for(int i =0; i < rp.getFieldArrayNames().length; i++)
		{
			//if(i==0) newString = r.getFieldArrayNames()[0];
			newString += rp.getFieldArrayNames()[i];
			newString += ",";
		}
		for(int i =0; i < rk.getFieldArrayNames().length; i++)
		{
			//if(i==0) newString = r.getFieldArrayNames()[0];
			newString += rk.getFieldArrayNames()[i];
			newString += ",";
		}
		statLine.add(newString);
		
		
		for (int i =0; i < geneVault.size(); i++) {
			//newString = WorldState.nameVault.get(i) + "," + WorldState.generationVault.get(i) + ",";
			newString = "";
			rg = EventPack.geneFields(geneVault.get(i));
			rp = EventPack.phenoFields(new GeneToPhenotype(rg.getFieldArrayValues(),rg.getFieldArrayNames()));
			rk = EventPack.kinFields(new PhenotypeToKinetics(rp.getFieldArrayValues(),rp.getFieldArrayNames()));
			
			if(i < generationVault.size()) ri = EventPack.infoFields(generationVault.get(i));	
			else ri = EventPack.infoFields(generationVault.get(0));
			//newString = simRun + ",";

			for (int j =0; j < ri.getFaValues().length; j++)
			{
				newString += ri.getFaValues()[j];
				newString += ",";
			}
			
			for (int j =0; j < rg.getFieldArrayValues().length; j++)
				{
					newString += Double.toString(rg.getFieldArrayValues()[j]);
					newString += ",";
				}
			for (int j =0; j < rp.getFieldArrayValues().length; j++)
			{
				newString += Double.toString(rp.getFieldArrayValues()[j]);
				newString += ",";
			}
			for (int j =0; j < rk.getFieldArrayValues().length; j++)
			{
				newString += Double.toString(rk.getFieldArrayValues()[j]);
				newString += ",";
			}
			statLine.add(newString);
		}
			
	
		return statLine;
	}

	/**
	 * @param totalVault the totalVault to set
	 */
	public void setTotalVault(StatPack totalVault) {
		this.totalVault.add(totalVault);
	}

	/**
	 * @param geneVault the geneVault to set
	 */
	public void setGeneVault(ArrayList<GeneSequence> geneVault) {
		for(int i = 0; i < geneVault.size(); i ++){
			this.geneVault.add(geneVault.get(i));
		}
	}

	/**
	 * @param generationVault the generationVault to set
	 */
	public void setGenerationVault(ArrayList<OrgInfo> generationVault) {
		for(int i = 0; i < generationVault.size(); i ++){
			this.generationVault.add(generationVault.get(i));
		}
	}

	/**
	 * @param turnVault the turnVault to set
	 */
	public void setTurnVault(ArrayList<StatPack> turnVault) {
		for(int i = 0; i < turnVault.size(); i ++){
			this.turnVault.add(turnVault.get(i));
		}
	}

	/**
	 * @param finalRunFlag the finalRunFlag to set
	 */
	public void setFinalRunFlag(boolean finalRunFlag) {
		this.finalRunFlag = finalRunFlag;
	}
	
	
	
}

