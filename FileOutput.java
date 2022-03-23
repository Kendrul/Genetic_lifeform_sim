import java.io.PrintWriter;
import java.util.ArrayList;

/*FileOutput.java
 * CPSC 565 W2016: Project
 * Jason Schneider and Emil Emilov-Dulguerov
 * This class creates and formats the output for log and stats files
 * 
 */
public class FileOutput {
//TODO
	private boolean isDebug = WorldState.isDebug;
	
	public void outputFiles()
	{
		outFileLog();
		outFileStat();
	}
	
	public void outFileLog()
	{//this method creates the .log file
		try {
				System.out.println("Begin writing to file: " + WorldState.logFile); //for testing
				PrintWriter out = new PrintWriter(WorldState.logFile);
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

	public void outFileStat(){
		try {
			System.out.println("Begin writing to file: " + WorldState.statFile); //for testing
			
			PrintWriter out = new PrintWriter(WorldState.statFile);

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
		String newString = "Name,";
		
		reflectPack r = EventPack.geneFields(WorldState.geneVault.get(0));
		
		for(int i =0; i < r.getFieldArrayNames().length; i++)
		{
			//if(i==0) newString = r.getFieldArrayNames()[0];
			newString += r.getFieldArrayNames()[i];
			newString += ",";
		}
		statLine.add(newString);
		
		for (int i =0; i < WorldState.geneVault.size(); i++) {
			newString = WorldState.nameVault.get(i) + ",";
			r = EventPack.geneFields(WorldState.geneVault.get(i));
			
			for (int j =0; j < r.getFieldArrayValues().length; j++)
				{
					newString += Double.toString(r.getFieldArrayValues()[j]);
					newString += ",";
				}
			statLine.add(newString);
			}
		
		return statLine;
	}

}

