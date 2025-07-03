import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Anomaly {

    /**
     * Prints type int[][] in matrix format. Both axis labeled as p.
     * @param array double int array to print
     */
    private static void printDoubleArray(int[][] array){
        System.out.print("p  ");
        for(int p = 0; p < array[0].length; p++){
            System.out.print(p + "  ");
        }
        System.out.println();
        for (int j = 0; j < array.length; j++){
            System.out.print(j + " [");
            for (int i = 0; i < array[0].length; i++){
                if (i != 0){
                    System.out.print(", ");
                }
                if (j < i){
                    System.out.print(array[j][i]);
                } else { 
                    // only half the matrix needed, since p on both axis
                    System.out.print("-");
                }
            }
            System.out.println("]");
        }
    }

    private static void printArray(double[] array){
        System.out.print("[");
        for (int i = 0; i < array.length; i++){
            if (i != 0){
                System.out.print(", ");
            }
            System.out.print(String.format("%.3f", array[i]));
        }
        System.out.println("]");
    }

    private static int maxP (int[][] matrix, int p){
        int result = matrix[p][p]; // this value is always zero
        for (int i = 0; i < matrix[p].length; i++){
            if (matrix[p][i] > result){
                result = matrix[p][i];
            }
        }
        for (int j = 0; j < matrix.length; j++){
            if (matrix[j][p] > result){
                result = matrix[j][p];
            }
        }
        return result;
    }

    /**
     * Identifies quantity of near-same anomalies between each set of p.
     * @param dataMap The map (representing a graph) to search in.
     */
    public static void findNearSame(HashMap<Integer, ArrayList<Triple>> dataMap, int numPs){
        // near-same anomalies follow the pattern: (s)-[p]->(o)<-[pp]-(s)

        // to access values: nearSameCnt[smaller p][larger p]
        int[][] nearSameCnt = new int[numPs][numPs];
        double[] nearSameMsmt = new double[numPs];
        Comparator<Triple> c = new CompareTriple();
        for (ArrayList<Triple> tList : dataMap.values()){
            nearSameMsmt[tList.get(0).p] = tList.size();
            for (ArrayList<Triple> compList: dataMap.values()){
                if (tList.get(0).p < compList.get(0).p){
                    int ptr = 0;
                    int cmpPtr = 0;
                    while (ptr < tList.size() && cmpPtr < compList.size()){
                        int compVal = c.compare(tList.get(ptr), compList.get(cmpPtr));
                        if (compVal == 0){
                            // near-same found, increment the count
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
        //System.out.println("\nCounts for the Near-Same Relations: ");
        //printDoubleArray(nearSameCnt);
        
        for (int i = 0; i < numPs; i++){
            if (nearSameMsmt[i] != 0){
                nearSameMsmt[i] = 1.0*maxP(nearSameCnt, i)/nearSameMsmt[i];
            }
        }
        //System.out.println("Measurements for Near-Same Anomalies: ");
        //printArray(nearSameMsmt);

        int anomalyCount = 0;
        double avg = 0;
        for (int a = 0; a < numPs; a++){
            avg += nearSameMsmt[a];
            if (nearSameMsmt[a] >= 0.99){
                anomalyCount++;
            }
        }
        // only consider the number of ps we actually have for the average, not numPs
        avg = avg / dataMap.size();
        System.out.println("Average Near-Same Value: " + avg);
        System.out.println("Number of near-same anomalies past .99 threshold: " + anomalyCount);
    }

    /**
     * Counts the number of near-reverse anomalies between each set of p.
     * @param dataMap The map (representing a graph) to search in.
     */
    public static void findNearReverse(HashMap<Integer, ArrayList<Triple>> dataMap, int numPs){
        // near-reverse anomalies follow the pattern: (s)-[p]->(o)-[pp]->(s)

        // The data map with arrays sorted by s-value first
        HashMap<Integer, ArrayList<Triple>> sSortedMap = dataMap;
        // The data map with arrays sorted by o-value first
        HashMap<Integer, ArrayList<Triple>> oSortedMap = new HashMap<>();
        Comparator<Triple> oComp = new CompareTripleByO();
        for (ArrayList<Triple> list : sSortedMap.values()){
            ArrayList<Triple> oList = new ArrayList<>(list);
            oList.sort(oComp);
            oSortedMap.put(list.get(0).p, oList);
        }

        // to access values: nearReverseCnt[smaller p][larger p]
        int[][] nearReverseCnt = new int[numPs][numPs];
        double[] nearRevMsmt = new double[numPs];
        for (ArrayList<Triple> tList : sSortedMap.values()){
            nearRevMsmt[tList.get(0).p] = tList.size();
            for (ArrayList<Triple> revList: oSortedMap.values()){
                if (tList.get(0).p < revList.get(0).p){ //can p = pp? if p == pp, then the result is doubled
                    int ptr = 0;
                    int revPtr = 0;
                    while (ptr < tList.size() && revPtr < revList.size()){
                        if (tList.get(ptr).s > revList.get(revPtr).o){
                            revPtr++;
                        } else if (tList.get(ptr).s < revList.get(revPtr).o){
                            ptr++;
                        } else {
                            // tList s and revList o are equal, compare lesser values
                            if (tList.get(ptr).o > revList.get(revPtr).s){
                                revPtr++;
                            } else if (tList.get(ptr).o < revList.get(revPtr).s){
                                ptr++;
                            } else {
                                nearReverseCnt[tList.get(ptr).p][revList.get(revPtr).p]++;
                                ptr++;
                                revPtr++;
                            }
                        }
                    }
                }
            }
        }
        // printing results
        //System.out.println("\nCounts for the Near-Reverse Relations: ");
        //printDoubleArray(nearReverseCnt);

        for (int i = 0; i < numPs; i++){
            if (nearRevMsmt[i] != 0){
                nearRevMsmt[i] = 1.0*maxP(nearReverseCnt, i)/nearRevMsmt[i];
            }
        }
        //System.out.println("Measurements for Near-Reverse Anomalies: ");
        //printArray(nearRevMsmt);

        int anomalyCount = 0;
        double avg = 0;
        for (int a = 0; a < numPs; a++){
            avg += nearRevMsmt[a];
            if (nearRevMsmt[a] >= 0.99){
                anomalyCount++;
            }
        }
        // even though there are numPs p values, we only want to take average for however many ps are in this map
        avg = avg / dataMap.size();
        System.out.println("Average Near-Reverse Value: " + avg);
        System.out.println("Number of near-reverse anomalies past .99 threshold: " + anomalyCount);
    }

    /**
     * Measures the amount of Cartesian product anomalies in a set of p.
     * @param dataMap The map (representing a graph) to search in.
     */
    public static void findCartesianProduct(HashMap<Integer, ArrayList<Triple>> dataMap){
        // WWW measurement for Cartesian product anomalies: 
            // total triples / (unique subjects * unqiue objects)
        // to access values: result[p]    
        double[] result = new double[dataMap.size()];
        int currIndex = 0;
        for (ArrayList<Triple> tList : dataMap.values()){
            // total number of triples with predicate p
            int total = tList.size();

            // number of unique subjects with p
            int subjects = 1;
            int prevS = tList.get(0).s;
            int currS;
            for (int i = 1; i < tList.size(); i++){
                currS = tList.get(i).s;
                if (prevS != currS) {
                    subjects++;
                    prevS = currS;
                }
            }

            // number of unique objects with p
            ArrayList<Triple> oList = new ArrayList<>(tList);
            oList.sort(new CompareTripleByO());
            int objects = 1;
            int prevO = oList.get(0).o;
            int currO;
            for (int j = 1; j < oList.size(); j++){
                currO = oList.get(j).o;
                if (prevO != currO) {
                    objects++;
                    prevO = currO;
                }
            }

            //System.out.println("p" + tList.get(0).p + ": " + subjects + "/" +
                //total + " unique subjects; " + objects + "/" + total + " unique objects");

            if (subjects*objects != 0){
                result[currIndex] = total*1.0/(subjects*objects);
            }
            currIndex++;
        }

        // printing results
        int anomalyCount = 0;
        double cpSum = 0;
        //System.out.println("\nCartesian Product factors for each p:");
        //System.out.print("[");
        for (int i = 0; i < result.length; i++){
            /**
            if (i != 0){
                System.out.print(", ");
            }
            System.out.print(String.format("%.3f", result[i]));
            */
            cpSum += result[i];
            if (result[i] >= 0.8){
                anomalyCount++;
            } 
        }
        //System.out.println("]");

        // avg of Cartesian products for all p
        System.out.println("Cartesian average is: " + cpSum/result.length);
        System.out.println("Number of Cartesian anomalies past .80 threshold: " + anomalyCount);
    }


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
        //findNearSame(map);

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
        //findNearReverse(revMap);

        //findCartesianProduct(map);
        HashMap<Integer, ArrayList<Triple>> cpMap = new HashMap<>();
        ArrayList<Triple> cp0 = new ArrayList<>();
        Collections.addAll(cp0, 
            new Triple(20,12,0), new Triple(20, 20, 0), 
            new Triple(20, 23, 0), new Triple(40, 12, 0),
            new Triple(40, 20, 0), new Triple(40, 23, 0));
        cpMap.put(0, cp0);
        //findCartesianProduct(cpMap);
    }
}
