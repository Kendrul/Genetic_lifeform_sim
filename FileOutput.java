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
	
	StatPack stats;
	
	public synchronized void outputFiles()
	{
		stats = Starter.getStats();
		outFileLog();
		outFileStat();			
	}
	
	/**
	 * 
	 * @param stat name of stat output file
	 * @param log name of event log output file
	 */
	public synchronized void outputFiles(String stat, String log)
	{
		logName = log;
		statName = stat;
		stats = Starter.getStats();
		outFileLog();
		outFileStat();		
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
		String newString = "Final Stats";
		statLine.add(newString);
		//Add Statistical Information from StatPack Class
				//stats.update();
				reflectPack rs = EventPack.statFields(stats);
				newString = "Block,";
				
				for(int i =0; i < rs.getFieldArrayNames().length; i++)
				{
					newString += rs.getFieldArrayNames()[i];
					newString += ",";
				}
				
				statLine.add(newString);
				newString = "Final,";
				
				for (int j =0; j < rs.getFieldArrayValues().length; j++)
				{
					newString += Double.toString(rs.getFieldArrayValues()[j]);
					newString += ",";
				}
				statLine.add(newString);
				
				//System.out.println("turnVault size: " + WorldState.turnVault.size());
				for (int i =0; i < WorldState.turnVault.size(); i++) {
					//newString = WorldState.nameVault.get(i) + "," + WorldState.generationVault.get(i) + ",";
					rs = EventPack.statFields(WorldState.turnVault.get(i));
					
					newString = i + ",";
					for (int j =0; j < rs.getFieldArrayValues().length; j++)
					{
						newString += rs.getFieldArrayValues()[j];
						newString += ",";
					}
					//System.out.println(newString);
					statLine.add(newString);
				}
				
				
		newString = "";
		statLine.add(newString);
		//next table of genetic information
		//newString = "Number, Name, Age, Generation, Life-Cycle, Birth-Turn, Death-Turn, Death Reason, Parents";
		newString ="";
		reflectPack ri = EventPack.infoFields(WorldState.generationVault.get(0));		
		
		reflectPack rg = EventPack.geneFields(WorldState.geneVault.get(0));
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
		
		
		for (int i =0; i < WorldState.geneVault.size(); i++) {
			//newString = WorldState.nameVault.get(i) + "," + WorldState.generationVault.get(i) + ",";
			rg = EventPack.geneFields(WorldState.geneVault.get(i));
			rp = EventPack.phenoFields(new GeneToPhenotype(rg.getFieldArrayValues(),rg.getFieldArrayNames()));
			rk = EventPack.kinFields(new PhenotypeToKinetics(rp.getFieldArrayValues(),rp.getFieldArrayNames()));
			
			if(i < WorldState.generationVault.size()) ri = EventPack.infoFields(WorldState.generationVault.get(i));	
			else ri = EventPack.infoFields(WorldState.generationVault.get(0));
			newString = "";
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

	private int search(int i)
	{
		ArrayList<OrgInfo> oi = WorldState.generationVault;
		
		for(int j = 0; j < oi.size(); j++)
		{
			if (Integer.toString(i).equals(oi.get(j).number))
			{
				return j;
			}
		}
		return 0;
	}
	
}

