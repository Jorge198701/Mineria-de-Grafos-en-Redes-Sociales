
package edu.fit.brees.ego.util;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Stack;
import java.util.ArrayList;

import edu.fit.brees.ego.community.EgoCommunity;
import edu.fit.brees.ego.graph.EgoVertex;
import edu.fit.brees.ego.jung.JungNetwork;
import edu.fit.brees.ego.jung.JungVertex;


public class ExtractEgoCommunities
{

	public static ArrayList<EgoCommunity> extract(EgoVertex baseVertex) throws Exception
	{
		Hashtable<Integer, EgoCommunity>	groups 	= new Hashtable<>();	// what will be returned
		
		Stack<JungVertex> allVertices 	= new Stack<>();
		Stack<JungVertex> vertexToProcess = new Stack<>();
		
		JungNetwork egoNet	= new JungNetwork();
		JungVertex	egoNode = new JungVertex(baseVertex.getId(), baseVertex.getName());
		egoNet.addVertex(egoNode);
		
		int 	baseID 	= baseVertex.getId();
		int		groupID	= 0;						// this will be incremented 
		
		Collection<EgoVertex> neighbors = baseVertex.getNeighbors();
		
		for ( EgoVertex friend : neighbors)
		{
			JungVertex node = new JungVertex(friend.getId(), friend.getName());
			egoNet.addEdge("edge", egoNode, node);		
			allVertices.push(node);
		}
			
		for ( EgoVertex friend : neighbors)
		{
			JungVertex freindNode = egoNet.findVertex(friend.getId() );
			
			Collection<EgoVertex> oneHop = friend.getNeighbors();
			
			for (EgoVertex f2 : oneHop)
			{
				JungVertex f2Node = egoNet.findVertex(f2.getId());

				if (f2Node != null)
				{
					egoNet.addEdge("edge", freindNode, f2Node);
				}
			}
		}
	
		egoNet.removeVertex(egoNode);
		
		while ( ! allVertices.isEmpty() ) {
			groupID++;
			
			EgoCommunity ec = new EgoCommunity();
			float id = Float.parseFloat(baseID + "." + groupID);			
			ec.setId(id);				
			ec.addToList(baseID);		
			ec.setChanged(true);		
			groups.put(groupID, ec);	
			
			JungVertex jVertex =  allVertices.pop();
			jVertex.setTag(groupID);
			vertexToProcess.push(jVertex);

			while (! vertexToProcess.isEmpty() ) {
				JungVertex v = vertexToProcess.pop();
				
				EgoCommunity group = groups.get(v.getTag());		
				group.addToList(v.getId());							

				Collection<JungVertex> egoNeighbors = egoNet.getNeighbors(v);

				if ( egoNeighbors != null)		
				{
					for ( JungVertex v2 : egoNeighbors) {
						v2.setTag(groupID);
						vertexToProcess.push(v2);
						allVertices.remove(v2);
					}
				}
				
				egoNet.removeVertex(v);
			}
		}
			
		ArrayList<EgoCommunity> answer = new ArrayList<>(groups.values());
		return answer;
	}	
}
