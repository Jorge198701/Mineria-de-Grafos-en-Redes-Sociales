
package edu.fit.brees.ego.util;

import java.util.Collection;

import edu.fit.brees.ego.jung.JungEdge;
import edu.fit.brees.ego.jung.JungVertex;
import edu.uci.ics.jung.graph.Graph;


public class Metrics 
{

	public static int order(Graph<JungVertex, JungEdge> graph)
	{
		return graph.getVertexCount();
	}
	
	
	public static int size(Graph<JungVertex, JungEdge> graph)
	{
		return graph.getEdgeCount();
	}
	
	public static double density(Graph<JungVertex, JungEdge> graph)
	{
		float numberOfNodes = graph.getVertexCount();
		float numberOfEdges = graph.getEdgeCount();
		
		double density = ( 2.0 * numberOfEdges) / (numberOfNodes * (numberOfNodes-1));
		return density;
	}
	
	public static double averageDegree(Graph<JungVertex, JungEdge> graph)
	{
		float numberOfNodes = graph.getVertexCount();
		float numberOfEdges = graph.getEdgeCount();
		
		double avgDegree = ( 2.0 * numberOfEdges) / (numberOfNodes);
		return avgDegree;
	}
	
	public static int maxDegree(Graph<JungVertex, JungEdge> graph)
	{
		int degree = 0;
		
		Collection<JungVertex> nodes = graph.getVertices();
		
		for ( JungVertex v : nodes) {
			int d = graph.degree(v);
			
			if ( d > degree)
				degree = d;
		}
		
		return degree;
	}

	
	
	
	
	
	
	public static int diameter(Graph<JungVertex, JungEdge> graph)
	{
		
		return 1;
	}
	
	
	
}
