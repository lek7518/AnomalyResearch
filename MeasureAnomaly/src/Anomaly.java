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

    public static void findNearReverse(HashMap<Integer, ArrayList<Triple>> dataMap){
        // have two copies of map, one with the values sorted by O and one sorted by S
        // then copy algorithm from near-same

        HashMap<Integer, ArrayList<Triple>> sSortedMap = dataMap;
        HashMap<Integer, ArrayList<Triple>> oSortedMap = new HashMap<>();
        Comparator<Triple> oComp = new CompareTripleByO();
        for (ArrayList<Triple> list : sSortedMap.values()){
            ArrayList<Triple> oList = new ArrayList<>(list);
            oList.sort(oComp);
            oSortedMap.put(list.get(0).p, oList);
        }

        // access values: nearReverseCnt[smaller p][larger p]
        int[][] nearReverseCnt = new int[dataMap.size()][dataMap.size()];
        for (ArrayList<Triple> tList : sSortedMap.values()){
            for (ArrayList<Triple> revList: oSortedMap.values()){
                if (tList.get(0).p < revList.get(0).p){
                    int ptr = 0;
                    int revPtr = 0;
                    while (ptr < tList.size() && revPtr < revList.size()){
                        if (tList.get(ptr).s == revList.get(revPtr).o 
                         && tList.get(ptr).o == revList.get(revPtr).s){
                            nearReverseCnt[tList.get(ptr).p][revList.get(revPtr).p]++;
                            ptr++;
                            revPtr++;
                        } else if (tList.get(ptr).s > revList.get(revPtr).o){
                            revPtr++;
                        } else if (tList.get(ptr).s < revList.get(revPtr).o){
                            ptr++;
                        } else {
                            // tList s and revList o are equal
                            if (tList.get(ptr).o > tList.get(ptr).s){
                                revPtr++;
                            } else {
                                ptr++;
                            }
                        }
                    }
                }
            }
        }
        // printing results
        System.out.println("Counts for the Near-Reverse Relations: ");
        System.out.println("p  0  1  2 ");
        for (int j = 0; j < nearReverseCnt.length; j++){
            System.out.print(j + " [");
            for (int i = 0; i < nearReverseCnt[0].length; i++){
                if (i != 0){
                    System.out.print(", ");
                }
                if (j < i){
                    System.out.print(nearReverseCnt[j][i]);
                } else { 
                    System.out.print("-");
                }
            }
            System.out.println("]");
        }
    }

    public static void cartesianProduct(){}

    public static void main(String[] args){
        // testing near-same
        HashMap<Integer, ArrayList<Triple>> map = new HashMap<>();
        Comparator<Triple> c = new CompareTriple();
        ArrayList<Triple> p0 = new ArrayList<>();
        Collections.addAll(p0, 
            new Triple(20,12,0), new Triple(72, 20, 0), 
            new Triple(12, 13, 0), new Triple(42, 34, 0));
        p0.sort(c);
        map.put(0, p0);
        ArrayList<Triple> p1 = new ArrayList<>();
        Collections.addAll(p1, 
            new Triple(72, 20, 1), new Triple(12,13,1), 
            new Triple(20,7,1), new Triple(20,12,1), 
            new Triple(16,32,1), new Triple(15,14,1));
        p1.sort(c);
        map.put(1, p1);
        ArrayList<Triple> p2 = new ArrayList<>();
        Collections.addAll(p2, 
            new Triple(15,14,2), new Triple( 16,17,2), 
            new Triple(12,13,2));
        p2.sort(c);
        map.put(2, p2);
        
        //System.out.println(map);
        findNearSame(map);

        HashMap<Integer, ArrayList<Triple>> revMap = new HashMap<>(map);
        revMap.remove(2);
        ArrayList<Triple> r2 = new ArrayList<>();
        Collections.addAll(r2, new Triple(13, 12, 2), new Triple(14, 15, 2), 
        new Triple(16, 17, 2));
        revMap.put(2, r2);
        revMap.get(0).remove(2);
        revMap.get(0).add(new Triple(20, 72, 0));
        revMap.get(0).sort(c);

        //System.out.println(revMap);
        findNearReverse(revMap);

    }

}
