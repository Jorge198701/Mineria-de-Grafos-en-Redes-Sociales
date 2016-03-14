package StringSimilitud;




abstract class ShingleBased {
    protected final int k;
    
    /**
     * 
     * @param k 
     */
    public ShingleBased(int k) {
        this.k = k;
    }
    
    public ShingleBased() {
        this(3);
    }
    
    public int getK() {
        return k;
    }

}
