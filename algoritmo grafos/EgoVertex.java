
package edu.fit.brees.ego.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Callable;

import edu.fit.brees.ego.community.EgoCommunity;
import edu.fit.brees.ego.jung.JungNetwork;
import edu.fit.brees.ego.jung.JungVertex;
import edu.fit.brees.ego.util.SetUtil;
import edu.fit.brees.ego.util.ExtractEgoCommunities;


@SuppressWarnings("unused")
public class EgoVertex implements Callable<Integer>
{

	private String	name		= "unset";

	private int		myId			= -99;		// a negative indicated unset
	
	private ArrayList<EgoCommunity> egoCommunities		= null;
	
	private Map<Integer, EgoCommunity> special		= null;

	private Map<Integer, EgoVertex> neighbors		= null;
	
	private ProcessingStatus	processingStatus			= null;
	
	private float	threshold		= 0.75f;
	
	private Hashtable<Float, HashSet<Integer>> communities;
	private Hashtable<Float, HashSet<Integer>> tmpCommunities;
	
	
	JungNetwork egoNet	=	null;
	JungVertex  egoNode	= 	null;		 
	
	
	protected boolean	idChanged						= false;			
	protected boolean	egoCommunitiesFoundCompleted	= false;			
	protected boolean	findSpecialCompleted			= false;			

	public static final int WAIT	= 1;
	public static final int RAN		= 2;
	
	
	
	
	public EgoVertex(String vertexName, int vertexID, float threshold, ProcessingStatus status)
	{
		this.name 				= vertexName;
		this.myId 				= vertexID;
		this.threshold			= threshold;
		this.processingStatus	= status;
		
		special				= new Hashtable<Integer, EgoCommunity> ();
		neighbors 			= new Hashtable<Integer, EgoVertex>();
		egoCommunities		= new ArrayList<EgoCommunity>();
		
		idChanged = false;
	}
	
	
	public int execute()
	{
		if (egoCommunitiesFoundCompleted == false)
		{
			extractEgoCommunities();
			egoCommunitiesFoundCompleted = true;
			idChanged = true;
		
			return EgoVertex.RAN;
		}
		
		
		if ( findSpecialCompleted == false)
		{
			if ( checkNeighborStatus() == false ) {
				return EgoVertex.WAIT;
			}
			
			determineSpecialNodes();
			findSpecialCompleted = true;
			
			return EgoVertex.RAN;
		}
	
		if ( idChanged )
		{
			pushIdChange();
			return EgoVertex.RAN;
		}
		processingStatus.incDoneCount();
		
		return EgoVertex.NOOP;		
	}
	
	
	
	private void extractEgoCommunities()
	{
		try
		{
			egoCommunities = ExtractEgoCommunities.extract(this);
		} 
		catch (Exception e)
		{
			System.out.println("ERROR " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	private void determineSpecialNodes()
	{
		
		for ( EgoCommunity ecSet : egoCommunities)
		{
			HashSet<Integer> myView = ecSet.getMembers();		
			
			for ( Integer nodeId : myView)						
			{
				if ( nodeId != this.myId)	
				{
					EgoVertex agent = neighbors.get(nodeId.intValue());

					HashSet<Integer> theirViewOfEC = agent.getEgoCommunity(this.myId);

					boolean similar = SetUtil.egoSimalarity(myView, theirViewOfEC, threshold);

					if ( similar == false)
					{
						EgoCommunity spec = new EgoCommunity();
						spec.addToList(nodeId);
						spec.setId(ecSet.getId());

						special.put(nodeId.intValue(), spec);

					}
				}
			}
		}
	}
	
	
	private boolean checkNeighborStatus()
	{
		Collection<EgoVertex> agents = neighbors.values();
		
		for ( EgoVertex agent : agents )
		{
			if (agent.isBuildEcCompleted() == false)
				return false;
		}
		
		return true;
	}

	
	
	public void pushIdChange()
	{
		synchronized(this) {
			this.idChanged = false;
		}
		
		for ( EgoCommunity ec : egoCommunities)
		{
			if ( ec.isChanged() )
			{
				float index = ec.getId();								 
				
				HashSet<Integer> members = ec.getMembers();
					
				for ( int idx : members )
				{
					if ( idx != myId )	
					{
						EgoVertex agent  = this.neighbors.get( idx );

						if ( agent != null)
						{
							agent.notifyGroupIdChange( this.myId, index);
						}
						else
						{
							System.out.println("\tERROR: On Agent (" + this.myId + ")   the agent id " + idx + " is NULL");
						}
					}
				}
			
				ec.setChanged(false);
			}
		}
	}
	
	
	
	public void notifyGroupIdChange(int callersID, float newGroupId)
	{
		
		EgoCommunity specialSet = special.get(callersID);
		
		if ( specialSet != null )
		{			
			specialSet.setId(newGroupId);
			specialSet.setChanged(false);
		}
		else
		{
			EgoCommunity ec = findEgo(callersID);
			
			if ( ec != null ) {
				if ( newGroupId < ec.getId() )
				{
					ec.setId(newGroupId);
					ec.setChanged(true);
					this.idChanged = true;
					processingStatus.resetDonCount();
				}	
			}
			
			
		
		}
	}
	
	
	
	
	
	public boolean hasAnyEgoChanged()
	{
		return idChanged;
	}



	
	public void addNeighbor(EgoVertex n)
	{
		Integer idx = new Integer(n.getId());		
					
		EgoVertex old = neighbors.put(idx, n);		
	}
	
	
	public int getNeighborCount()
	{
		return neighbors.size();
	}
	
	
	public Collection<EgoVertex> getNeighbors()
	{
		return this.neighbors.values();
	}
	
	
	public HashSet<Integer> getEgoCommunity(Integer id)
	{
		for (EgoCommunity ec : egoCommunities)
		{
			if ( ec.conatins(id) )
				return ec.getMembers();
		}
		
		return null;
	}
	
	
	public EgoCommunity findEgo(Integer id)
	{
		for (EgoCommunity ec : egoCommunities)
		{
			if ( ec.conatins(id) )
				return ec;
		}
		
		return null;
	}
		
	
	
	
	public Collection<EgoCommunity> getAllEgoCommunity()
	{
		return egoCommunities;
	}

	public Collection<EgoCommunity> getSpecialGroups()
	{
		return special.values();
	}	

	public String getName()
	{
		return name;
	}


	public int getId()
	{
		return myId;
	}
	
	
	public void dumpFGs()
	{	
		System.out.println("FG of Swarm Agent: " + this.myId);
		
		for ( EgoCommunity ec : egoCommunities)
		{			
			System.out.println("\t" + ec.getId() + " - " + ec.getMembers() );
		}
		
		System.out.println(" ");
	}
	

	
	public void dumpFgCommunities()
	{	
		System.out.println("FG of Swarm Agent: " + this.myId);
		
		for ( EgoCommunity ec : egoCommunities)
		{	
			if ( ec.size() > 2)
				System.out.println("\t" + ec.getId() + " - " + ec.getMembers() );
		}
		
		Collection<EgoCommunity> specials = special.values();
		
		for ( EgoCommunity ec :  specials)
		{	
				System.out.println("\t" + ec.getId() + " - " + ec.getMembers() );
		}		
		
		System.out.println("");
	}


	public boolean isBuildEcCompleted()
	{
		return this.egoCommunitiesFoundCompleted;
	}
	


	public Integer call() throws Exception
	{
		return execute();
	}


	public void setIdChanged(boolean idChanged) {
		this.idChanged = idChanged;
		
		for ( EgoCommunity ec :  egoCommunities	)
			ec.setChanged(idChanged);
		
	}


	public void setCommunities(Hashtable<Float, HashSet<Integer>> communities) {
		this.communities = communities;
	}


	public void setTmpCommunities(Hashtable<Float, HashSet<Integer>> tmpCommunities) {
		this.tmpCommunities = tmpCommunities;
	}
	
}
