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
