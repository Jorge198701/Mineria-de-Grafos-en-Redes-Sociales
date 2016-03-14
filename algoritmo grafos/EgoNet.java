
package edu.fit.brees.ego.jung;

import java.util.Collection;


public class EgoNet extends JungNetwork
{
	private static final long	serialVersionUID	= 1L;
	
	private	double 	density		=	0.0;
	private int		id			= 	0;
	private boolean	inCommunity	= 	false;
	private JungVertex	egoNode		= null;


	public EgoNet(JungVertex ego)
	{
		super();
		
		egoNode = ego;
		
		this.addVertex(egoNode);
	}
	
	protected EgoNet()
	{
		super();
	}
	
	public double getDensity()
	{
		return density;
	}
	
	public void setDensity(double density)
	{
		this.density = density;
	}
	
	
	public int getId()
	{
		return id;
	}
	
	
	public void setId(int id)
	{
		this.id = id;
	}
	


	
	public boolean isInCommunity()
	{
		return inCommunity;
	}

	public boolean isInCommunity(int communityId)
	{
		Collection<JungVertex> nodes = this.getVertices();
		
		for ( JungVertex n : nodes)
		{
			if ( n != egoNode)
					return true;
		}
		
		return false;
	}
	
	
	public void setInCommunity(boolean inCommunity)
	{
		this.inCommunity = inCommunity;
	}
	
	
	public JungVertex getEgoNode()
	{
		return egoNode;
	}
	
}
