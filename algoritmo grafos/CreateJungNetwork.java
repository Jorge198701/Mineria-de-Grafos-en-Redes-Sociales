
package edu.fit.brees.ego.util;

import java.util.Collection;

import edu.fit.brees.ego.graph.EgoVertex;
import edu.fit.brees.ego.jung.JungNetwork;
import edu.fit.brees.ego.jung.JungVertex;

public class CreateJungNetwork 
{

	
	public static JungNetwork create( Collection<EgoVertex> nodes )
	{
		
		JungNetwork graph = new JungNetwork();
		for ( EgoVertex n : nodes) {
			JungVertex v = new JungVertex(n.getId(), n.getName());
			graph.addVertex(v);
		}
		
		for ( EgoVertex n : nodes) {
			Collection<EgoVertex> friends = n.getNeighbors();
			
			JungVertex a = graph.findVertex(n.getId());
			
			for ( EgoVertex v2 : friends) {
				JungVertex b = graph.findVertex(v2.getId());
			
				graph.addEdge(" ", a, b);
			}
		}
		return graph;
		
	}
	
	
}
