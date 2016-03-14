
package edu.fit.brees.ego.graph;

public class ProcessingStatus 
{

	protected int	doneCount	= 0;

	public ProcessingStatus()
	{
		doneCount = 0;
	}
	
	public int getDoneCount()
	{
		return doneCount;
	}

	public synchronized void incDoneCount()
	{
		++doneCount;
	}
	
	public synchronized void decDoneCount()
	{
		--doneCount;
	}
	
	
	public synchronized void resetDonCount()
	{
		doneCount = 0;
	}
	
		
}