package StringSimilitud;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import utils.SparseBooleanVector;
import utils.SparseIntegerVector;

public class KShingling {
	
	

    protected int k;
    private final HashMap<String, Integer> shingles = new HashMap<String, Integer>();
    
    public KShingling() {
        k = 5;
    }
    
    public KShingling(int k) {
        if (k <= 0) {
            throw new InvalidParameterException("k should be positive!");
        }
        
        this.k = k;
    }
    
    public int getK() {
        return k;
    }
    
    private static final Pattern spaceReg = Pattern.compile("\\s+");
    
    protected int[] getArrayProfile(String s) {
        ArrayList<Integer> r = new ArrayList<Integer>(shingles.size());
        for (int i = 0; i < shingles.size(); i++) {
            r.add(0);
        }
        
        s = spaceReg.matcher(s).replaceAll(" ");
        String shingle;
        for (int i = 0; i < (s.length() - k + 1); i++) {
            shingle = s.substring(i, i+k);
            int position;
            
            if (shingles.containsKey(shingle)) {
                position = shingles.get(shingle);
                r.set(position, r.get(position) + 1);
                
            } else {
                shingles.put(shingle, shingles.size());
                r.add(1);
            }
            
        }
        
        return convertIntegers(r);  
    }

    private static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }
    
    public StringProfile getProfile(String s) {
        
        HashMap<Integer, Integer> hash_profile = getHashProfile(s);
        
        return new StringProfile(new SparseIntegerVector(hash_profile), this);
    }
    
    public StringSet getSet(String s) {
        HashMap<Integer, Integer> hash_profile = getHashProfile(s);
        
        return new StringSet(new SparseBooleanVector(hash_profile), this);
    }
    
    public int getDimension() {
        return this.shingles.size();
    }

    private HashMap<Integer, Integer> getHashProfile(String s) {
        HashMap<Integer, Integer> hash_profile = new HashMap<Integer, Integer>(s.length());
        
        s = spaceReg.matcher(s).replaceAll(" ");
        String shingle;
        for (int i = 0; i < (s.length() - k + 1); i++) {
            shingle = s.substring(i, i+k);
            int position;
            
            if (shingles.containsKey(shingle)) {
                position = shingles.get(shingle);
                
            } else {
                position = shingles.size();
                shingles.put(shingle, shingles.size());
                
            }
            
            if (hash_profile.containsKey(position)) {
                hash_profile.put(position, hash_profile.get(position) + 1);
                
            } else {
                hash_profile.put(position, 1);
            }
        }
        
        return hash_profile;
    }

    String getNGram(int key) {
        for (Map.Entry<String, Integer> entry : shingles.entrySet()) {
            if (entry.getValue().equals(key)) {
                return entry.getKey();
            }
        }
        
        throw new InvalidParameterException("No ngram coresponds to key " + key);
    }


}
