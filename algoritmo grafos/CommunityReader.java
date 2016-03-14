
package edu.fit.brees.ego.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import edu.fit.brees.ego.graph.EgoVertex;
import edu.fit.brees.ego.graph.ProcessingStatus;



@SuppressWarnings("unused")
public class CommunityReader 
{
	
	private boolean verbose			= false;

	
	
	
	
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}



	public CommunityReader()
	{
		;
	}
	

	
	public Hashtable<Integer, HashSet<Integer>> read(String inputFile)
	{
		String line = null;
		Hashtable<Integer, HashSet<Integer>> groupMap = new Hashtable<Integer, HashSet<Integer>>();
		
		
		if ( verbose) {
			System.out.println("------\nCommunityReader loading:  " + inputFile);
		}
		
		try
		{
			FileReader fileReader = new FileReader(inputFile);
			BufferedReader buffRead = new BufferedReader(fileReader);
			
			line = buffRead.readLine();
		
			while (line != null)
			{
				String [] data = line.split("\t");
				
				Integer vertex 		= new Integer(data[0].trim());
				Integer groupID 	= new Integer(data[1].trim());
				
				HashSet<Integer> group = groupMap.get(groupID);
				
				if ( group == null) {
					group = new HashSet<Integer>();
					groupMap.put(groupID, group);
				}
				
				group.add(vertex);
				
				// get the next line
				line = buffRead.readLine();
			}
			
			buffRead.close();
		}
		catch (IOException ioe)
		{
			System.err.println("ERROR: ");
			System.out.println("line " + line);
			ioe.printStackTrace();
			System.exit(-1);
		}
		
		return groupMap;
	}
		
}
