public class Rank {
    
    protected int s;
    protected int p;
    protected int o;
    protected double rank;
    protected int numNegatives;
    protected boolean isHead;

    public Rank(int s, int p, int o, double rank, int negatives, boolean isHead){
        this.s = s;
        this.p = p;
        this.o = o;
        this.rank = rank;
        this.numNegatives = negatives;
        this.isHead = isHead;
    }

    public String toString() {
        String result = "(" + this.s + ", " + this.p +"," + this.o + ")-";
        if (isHead){
            result += "h; ";
        } else {
            result += "t; ";
        }
        result += "Rank: " + this.rank + "; #Negatives: " + numNegatives;
        return result;
    }
}
