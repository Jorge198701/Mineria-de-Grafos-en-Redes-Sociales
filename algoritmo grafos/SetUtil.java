
 package edu.fit.brees.ego.util;



import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class SetUtil
{

	
	
	static public boolean egoSimalarity(Set<Integer> a, Set<Integer> b, float threshold)
	{
		int minSize = 999;
		
		int intersectSize = SetUtil.intersection(a, b).size();

		if ( a.size() < b.size())
			minSize = (int)( (float)a.size() * threshold);
		else
			minSize = (int)( (float)b.size() * threshold);
		
		if (intersectSize >= minSize)
			return true;
		else
			return false;
	}
	
	
	static public Set<Integer> intersection(Set<Integer> a, Set<Integer> b)
	{
		Set<Integer> s = new HashSet<Integer>(a);
		s.retainAll(b);		
		return s;
	}
	
	
	static public int intersectionSize(Set<Integer> me, Set<Integer> them)
	{
		return SetUtil.intersection(me, them).size();
	}
	
	
	static public Set<Integer> union(Set<Integer> a, Set<Integer> b)
	{
		Set<Integer> s = new HashSet<Integer>(a);
		s.addAll(b);
		return s;
	}
	
	
	static public float jacaardIndex(Set<Integer> me, Set<Integer> them)
	{
		float intersectSize = (float)SetUtil.intersection(me, them).size();
		float unionSize 	= (float)SetUtil.union(me, them).size();
		
		return (intersectSize / unionSize);
	}
	
	static public float overlapCoefficient(Set<Integer> a, Set<Integer> b)
	{
		float answer = 0;
		
		float intersectSize = (float)SetUtil.intersection(a, b).size();
		
		if ( a.size() < b.size())
			answer = intersectSize / a.size();
		else
			answer = intersectSize / b.size();
		
		return answer;
	}
	
	
	static public void removeSubsets(Map<Float, HashSet<Integer>> communities)
	{
		Set<Float> toDelete = new HashSet<>();
		
		
		Set<Float> keys = communities.keySet();
		int size = keys.size();
		
		Float[] keyArray = keys.toArray( new Float[size]);
		
		HashSet<Integer> X	= null;
		HashSet<Integer> Y	= null;
		float id = 0;
		
		for ( int x = 0; x < size -1; x++)
		{
			id = keyArray[x];
			X = communities.get(id);
			
			for (int y = x+1; y < size; y++)
			{
				id = keyArray[y];
				Y = communities.get(id);
				
				// only compare if X is smaller than Y
				if ( X.size() < Y.size())
				{
					if (SetUtil.overlapCoefficient(X, Y) == 1)
					{
						toDelete.add(id);
						continue;
					}
				}
			}
		}
		
		for ( Float f : toDelete)
			communities.remove(f);
	}
	
	
	static public int sizeOfSmaller(Set<Integer> a, Set<Integer> b)
	{
		if ( a.size() < b.size())
			return a.size(); 
		else
			return b.size();
	}
	
	
	static public boolean properSubset(Set<Integer> A, Set<Integer> B)
	{
		return B.containsAll(A);
	}
	
}
