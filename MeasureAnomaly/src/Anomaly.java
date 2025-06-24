import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Anomaly {

    public static void findNearSame(HashMap<Integer, ArrayList<Triple>> dataMap){
        // pattern to match: (s)-[p]->(o)<-[pp]-(s)

        // access values: nearSameCnt[smaller p][larger p]
        int[][] nearSameCnt = new int[dataMap.size()][dataMap.size()];
        Comparator<Triple> c = new CompareTriple();
        for (ArrayList<Triple> tList : dataMap.values()){
            for (ArrayList<Triple> compList: dataMap.values()){
                if (tList.get(0).p < compList.get(0).p){
                    int ptr = 0;
                    int cmpPtr = 0;
                    while (ptr < tList.size() && cmpPtr < compList.size()){
                        int compVal = c.compare(tList.get(ptr), compList.get(cmpPtr));
                        if (compVal == 0){
                            nearSameCnt[tList.get(ptr).p][compList.get(cmpPtr).p]++;
                            ptr++;
                            cmpPtr++;
                        } else if (compVal > 0){
                            cmpPtr++;
                        } else {
                            ptr++;
                        }
                    }
                }
            }
        }

        // printing results
        System.out.println("Counts for the Near-Same Relations: ");
        System.out.println("p  0  1  2 ");
        for (int j = 0; j < nearSameCnt.length; j++){
            System.out.print(j + " [");
            for (int i = 0; i < nearSameCnt[0].length; i++){
                if (i != 0){
                    System.out.print(", ");
                }
                if (j < i){
                    System.out.print(nearSameCnt[j][i]);
                } else { 
                    System.out.print("-");
                }
            }
            System.out.println("]");
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

        findNearSame(map);
    }

}
