
package edu.fit.brees.ego.loader;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import edu.fit.brees.ego.graph.EgoVertex;
import edu.fit.brees.ego.graph.ProcessingStatus;


public class GmlDataReader
{	
	private 	BufferedReader 		buffRead			= null;
	private 	ProcessingStatus 	status 				= null;
	private 	float				overlapThreshold	= 0.0f;
	private		int					firstIndex			= 99999;
	
	private int edgeCount = 0;
	private int nodeCount = 0;
	
	public int getNumberOfNodes() {
		return nodeCount;
	}


	public int getNumberOfEdges() {
		return edgeCount;
	}
	
	public  GmlDataReader()
	{
		
	}

	
	public int read(String inputFile, Map<Integer, EgoVertex> vertexMap, 
			float overlap, ProcessingStatus status)
	{
		this.overlapThreshold = overlap;
		this.status 		= status;
		
		
		try
		{
			FileReader fileReader = new FileReader(inputFile);
			buffRead = new BufferedReader(fileReader);
			
			String line = buffRead.readLine().trim();
			
			while (line != null)
			{
				line = line.trim();
				
				switch (line)  {
				case "#":
					break;
				case "node":
				case "node [":
					getNode(vertexMap, line);
					nodeCount++;
					break;
				case "edge":
				case "edge [":
					getEdge(vertexMap, line);
					edgeCount++;
					break;
				default:
				}
				line = buffRead.readLine();
			}
		} 
		catch (IOException ioe)
		{
			System.err.println("ERROR: ");
			ioe.printStackTrace();
			System.exit(-1);
		}
		
		return this.firstIndex;
	}
	
	
	private boolean getNode(Map<Integer, EgoVertex> vertexMap, String lastLine) throws IOException
	{
		String nodeLine = buffRead.readLine().trim();
		
		if ( ! lastLine.contains("["))	{	
			if ( ! nodeLine.startsWith("[") ) {
				System.err.println("Last line: " + lastLine);
				System.err.println("this line: " + nodeLine);
				System.err.println("nodes procceded:" + this.nodeCount);
				throw new IOException("Node [ missing on line " + nodeLine);
			}
			nodeLine = buffRead.readLine().trim();
		}
		
		
		int id = Integer.parseInt(nodeLine.substring(3).trim());
		
		if ( id < firstIndex)
			firstIndex = id;
				
		nodeLine = buffRead.readLine().trim();
		
		String label = nodeLine.substring(6).trim();		
		
		EgoVertex agent = new EgoVertex(label, id, this.overlapThreshold, this.status);
		vertexMap.put(id, agent);
		
		
		return true;
	}
	
	
	protected boolean getEdge(Map<Integer, EgoVertex> vertexMap, String lastLine) throws IOException
	{
		String line = buffRead.readLine().trim();
		
		if ( ! lastLine.contains("["))	{	
			if ( ! line.startsWith("[") ) {
				System.err.println("Last line: " + lastLine);
				System.err.println("this line: " + line);
				System.err.println("edges procceded:" + this.edgeCount);
				System.exit(-1);
			}
			
			line = buffRead.readLine().trim();		
		}
		
		if ( line.contains("label") ) {
			line = buffRead.readLine().trim();		
		}
		
		int a = Integer.parseInt(line.substring(6).trim() );
	
		line = buffRead.readLine().trim();
		int b = Integer.parseInt(line.substring(6).trim() );
		

		EgoVertex agentA = vertexMap.get(a);
		EgoVertex agentB = vertexMap.get(b);
		
		agentA.addNeighbor(agentB);
		agentB.addNeighbor(agentA);
			
		return true;		
	}
}
