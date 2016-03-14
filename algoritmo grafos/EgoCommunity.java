
package edu.fit.brees.ego.community;

import java.io.PrintStream;
import java.util.HashSet;



public class EgoCommunity
{
	
	protected HashSet<Integer> members		=	null;
	
	float	id		= (float) -99.9;

	boolean changed		= false;
	
	public EgoCommunity()
	{
		members = new HashSet<>();
	}
	
	public HashSet<Integer> getMembers()
	{
		return members;
	}

	public float getId()
	{
		return id;
	}


	
	public void setId(float id)
	{
		this.id = id;
		this.changed = true;
	}


	public boolean isChanged()
	{
		return changed;
	}


	public void setChanged(boolean changed)
	{
		this.changed = changed;
	}


	public void addToList(int agentId)
	{
		this.members.add(agentId);
	}
	

	
	public boolean conatins(int idx)
	{
		return members.contains(idx);		
	}
	
	public int size()
	{
		return members.size();
	}
	
	public void dump(PrintStream out)
	{
		out.print("ID: " + id + " [");
		
		for (int x : members)
		{
			out.print(x + ", ");
		}
		
		out.println("]");
	}

}
