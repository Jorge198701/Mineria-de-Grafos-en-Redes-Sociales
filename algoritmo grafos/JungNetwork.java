
package edu.fit.brees.ego.jung;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;


import java.util.Set;

public class JungNetwork extends UndirectedSparseGraph<JungVertex, JungEdge>
{
	private static final long	serialVersionUID	= 190470075572556544L;
	private int		edgeId							= 0;
	
	public JungVertex findVertex(int id)
	{
		
		Set<JungVertex> keys = this.vertices.keySet();
		
		for ( JungVertex v : keys)
		{
			if (v.getId() == id )
				return v;
		}
		
		return null;
	}
	
	public boolean addEdge(String txt, JungVertex a, JungVertex b)
	{
		
		JungEdge e = new JungEdge(edgeId++, txt);
		return addEdge(e, a, b);
		
	}
	

	public JungVertex addVertex(int id, String name)
	{
		JungVertex node = new JungVertex(id, name);
		addVertex(node);
		return node;
	}

}
