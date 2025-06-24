import java.util.Comparator;

public class Triple {

    protected Integer s;
    protected Integer o;
    protected Integer p;

    public Triple(int s, int o, int p){
        this.s = Integer.valueOf(s);
        this.o = Integer.valueOf(o);
        this.p = Integer.valueOf(p);
    }

    public Triple(String[] t){
        this.s = Integer.valueOf(t[0]);
        this.o = Integer.valueOf(t[1]);
        this.p = Integer.valueOf(t[2]);
    }

    @Override
    public String toString(){
        return "[s: " + s + ", o: " + o + ", p: " + p + "]";
    }
}

class CompareTriple implements Comparator<Triple>{

    // Compares two Triples, disregarding the p value
    @Override
    public int compare(Triple t1, Triple t2) {
        if (t1.s > t2.s){
            return 1;
        } else if (t1.s < t2.s){
            return -1;
        } else {
            if (t1.o > t2.o){
                return 1;
            } else if (t1.o < t2.o){
                return -1;
            } else {
                return 0;
            }
        }
    }

    
}
