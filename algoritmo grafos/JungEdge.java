
package edu.fit.brees.ego.jung;


public class JungEdge 
{

	int		id		= -1;
	String	name	= null;
	

	public JungEdge (int id, String name)
	{
		this.id = id;
		this.name = name;
		
	}
	
	public String toString()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	
}
