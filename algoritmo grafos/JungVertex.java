
package edu.fit.brees.ego.jung;



public class JungVertex 
{
	private int		id		= -1;
	private String	name	= null;
	private int		tag		= 0;

	public JungVertex (int id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public int getId()
	{
		return id;
	}

	
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return name;
	}

	public int getTag()
	{
		return tag;
	}

	public void setTag(int tag)
	{
		this.tag = tag;
	}
	
}
