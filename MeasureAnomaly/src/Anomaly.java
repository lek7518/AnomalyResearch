import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Anomaly {

    public static void nearSame(HashMap<Integer, ArrayList<Triple>> dataMap){
        // pattern to match: (s)-[p]->(o)<-[pp]-(s)
        // plan: 
        // for one specific triple with p
            // find a relation that has the same s and o and different pp
            // for each following triple in p, is there a nearsame in pp? keep count

        for (ArrayList<Triple> tList : dataMap.values()){
            if(tList.contains(new Triple(12, 13, 3))){
                System.out.println("contains 12,13,311111111111");
                // this does not work to identify triples
            }
            for (Triple t : tList){
                int[] nearSameCnt = new int[dataMap.size()]; //assumes that p will be 0-n
                // for each triple, compare to every other triple 
                for (ArrayList<Triple> compList : dataMap.values()){
                    // only compare if not the same p
                    if (compList.get(0).p != t.p){
                        for (Triple compT : compList){
                            if (t.s == compT.s && t.o == compT.o){
                                nearSameCnt[compT.p]++;
                            }
                        }
                    }
                }
                // nearSameCnt now represents how many matches this specific triple has
                System.out.print("Triple " + t + " has duplicates: [");
                for (int i = 0; i < nearSameCnt.length; i++){
                    if (i != 0){
                        System.out.print(", ");
                    }
                    System.out.print(nearSameCnt[i]);
                }
                System.out.print("]\n");
            }
        }
    }

    public static void nearReverse(){}

    public static void cartesianProduct(){}

    public static void main(String[] args){
        // testing near-same
        HashMap<Integer, ArrayList<Triple>> map = new HashMap<>();
        ArrayList<Triple> p0 = new ArrayList<>();
        Collections.addAll(p0, 
            new Triple(20,12,0), new Triple(72, 20, 0), 
            new Triple(12, 13, 0), new Triple(42, 34, 0));
        p0.sort(new CompareTriple());
        map.put(0, p0);
        ArrayList<Triple> p1 = new ArrayList<>();
        Collections.addAll(p1, 
            new Triple(72, 20, 1), new Triple(12,13,1), 
            new Triple(20,7,1), new Triple(20,12,1), 
            new Triple(16,32,1), new Triple(15,14,1));
        p1.sort(new CompareTriple());
        map.put(1, p1);
        ArrayList<Triple> p2 = new ArrayList<>();
        Collections.addAll(p2, 
            new Triple(15,14,2), new Triple( 16,17,2), 
            new Triple(12,13,2));
        p2.sort(new CompareTriple());
        map.put(2, p2);
        
        //System.out.println(map);

        nearSame(map);
    }

}
