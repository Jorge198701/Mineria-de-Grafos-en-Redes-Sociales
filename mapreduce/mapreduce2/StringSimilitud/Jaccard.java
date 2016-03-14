package StringSimilitud;

import stringSimilitud.interfaces.MetricStringDistance;
import stringSimilitud.interfaces.NormalizedStringDistance;
import stringSimilitud.interfaces.NormalizedStringSimilarity;

public class Jaccard extends ShingleBased implements MetricStringDistance, NormalizedStringDistance, NormalizedStringSimilarity{
	

    public Jaccard(int k) {
        super(k);
    }
    
    public Jaccard() {
        super();
    }

    
    public double similarity(String s1, String s2) {
        KShingling ks = new KShingling(k);
        int[] profile1 = ks.getArrayProfile(s1);
        int[] profile2 = ks.getArrayProfile(s2);
    
        int length = Math.max(profile1.length, profile2.length);
        profile1 = java.util.Arrays.copyOf(profile1, length);
        profile2 = java.util.Arrays.copyOf(profile2, length);
        
        int inter = 0;
        int union = 0;
        
        for (int i = 0; i < length; i++) {
            if (profile1[i] > 0 || profile2[i] > 0) {
                union++;
                
                if (profile1[i] > 0 && profile2[i] > 0) {
                    inter++;
                }
            }
        }
    
        return (double) inter / union;
    }
    

    public double distance(String s1, String s2) {
        return 1.0 - similarity(s1, s2);
    }


}
