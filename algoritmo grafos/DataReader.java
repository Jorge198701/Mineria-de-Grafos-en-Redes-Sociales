
package edu.fit.brees.ego.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

import edu.fit.brees.ego.graph.EgoVertex;
import edu.fit.brees.ego.graph.ProcessingStatus;



@SuppressWarnings("unused")
public class DataReader
{
	private final static int	VERTEX	= 0;
	private final static int 	EDGE		= 1;
	
	private int startValue	= 1;

	private int	processing = -1;

	private float threshold	= 0.75f;
	
	private ProcessingStatus	status = null;
	
	private int numberOfNodes		= 0;
	private int numberOfEdges		= 0;
	private boolean verbose			= false;
	
	
	public int getNumberOfNodes() {
		return numberOfNodes;
	}


	public int getNumberOfEdges() {
		return numberOfEdges;
	}


	public DataReader()
	{
		;
	}
	
	public DataReader(boolean verbose)
	{
		this.verbose = verbose;
	}
	
	public void read(String inputFile, Map<Integer, EgoVertex> vertexMap, int startIndex, float threshold, 
			ProcessingStatus status)
	{
		startValue = startIndex;
		this.threshold = threshold;
		this.status = status;
		doRead(inputFile, vertexMap);
	}

	

	
	
	private void doRead(String inputFile, Map<Integer, EgoVertex> vertexMap)
	{
		int edgeCount	= 0;
		int loopCounter	= 0;
		int lineCounter	= 0;
		String line = null;
		
		if ( verbose) {
			System.out.println("------\nDataReader READING:  " + inputFile);
		}
		
		
		try
		{
			// INPUT
			FileReader fileReader = new FileReader(inputFile);
			BufferedReader buffRead = new BufferedReader(fileReader);
			
			line = buffRead.readLine();

			while (line != null)
			{
				++loopCounter;
				
				if ( loopCounter > 1000)
				{
					loopCounter = 0;
					
					++lineCounter;
					
					if ( lineCounter == 50)
					{
						lineCounter = 0;
					}
				}
				
				if ( line.startsWith("#") )
				{
				}
				else if ( line.startsWith("*") )
				{
					if (line.contains("Vertices"))
					{
						processing = VERTEX;
						
						int idx = line.indexOf("Vertices");
						idx += 9;
						
						int nodeCount = Integer.parseInt(line.substring(idx).trim());
						
						createAgents(vertexMap, nodeCount);
					}
					
					else if(line.contains("Edges"))
					{
						processing = EDGE;
					}	
					else if(line.contains("Arcs"))
					{
						processing = EDGE;
					}	
					else
						processing = -1;
				}
				else
				{
					StringTokenizer strTok = new StringTokenizer(line);
					
					switch(processing)
					{
						case VERTEX:
							int id = Integer.parseInt(strTok.nextToken());
							String label = strTok.nextToken();
							break;			
							
						case EDGE:
							int wt = 1;
							Integer a = new Integer(strTok.nextToken());
							Integer b = new Integer(strTok.nextToken());
							
							
							String lb = new String( a + "-" + b);
							
							
							EgoVertex agentA = vertexMap.get(a);
							EgoVertex agentB = vertexMap.get(b);
							
							if ( agentA == null)
							{
								agentA = createAgent(vertexMap, a);
							}

							if ( agentB == null)
							{
								agentB = createAgent(vertexMap, b);
							}
							
							agentA.addNeighbor(agentB);
							agentB.addNeighbor(agentA);
							edgeCount++;

							break;
							
						default:
							System.exit(-1);
					}
				}
				
				line = buffRead.readLine();
			}
			
			buffRead.close();
			fileReader.close();
		} 
		catch (IOException ioe)
		{
			System.err.println("ERROR: ");
			System.out.println("line " + line);
			ioe.printStackTrace();
			System.exit(-1);
		}
		
		if ( verbose ) {
			System.out.println("\n\tDONE Reading  ");
			System.out.println("\tCreated " + (edgeCount) + "  edges");
			System.out.println("\tCreated " + vertexMap.size() + " Vertices\n");
		}
		
		this.numberOfEdges = edgeCount;

	}

	
	protected void createAgents(Map<Integer, EgoVertex> vertexMap, int count)
	{
		int id = this.startValue;

		for ( int n = 0; n <  count; n++)
		{
			String name = new String("ID: " + n);
			EgoVertex agent = new EgoVertex(name, id, threshold, status);

			vertexMap.put(id, agent);
			++id;
		}
		
		this.numberOfNodes = count;
	}
	
	
	
	/**
	 * 
	 * @param swarm
	 * @param count
	 */
	protected EgoVertex createAgent(Map<Integer, EgoVertex> vertexMap, int id)
	{
		String name = new String("ID: " + id	);
		EgoVertex agent = new EgoVertex(name, id, threshold, status);

		vertexMap.put(id, agent);
		
		++numberOfNodes;
		return agent;
	}
	
}
