import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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

    public static void printArray(double[] array){
        System.out.print("[");
        for (int i = 0; i < array.length; i++){
            if (i != 0){
                System.out.print(", ");
            }
            System.out.print(String.format("%.3f", array[i]));
        }
        System.out.println("]");
    }

    public static void printArray(int[] array){
        System.out.print("[");
        for (int i = 0; i < array.length; i++){
            if (i != 0){
                System.out.print(", ");
            }
            System.out.print(array[i]);
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
     * @param numPs number of p's in the complete dataset
     * @return the final measurement for each p
     */
    public static double[] findNearSame(HashMap<Integer, ArrayList<Triple>> dataMap, int numPs){
        // near-same anomalies follow the pattern: (s)-[p]->(o)<-[pp]-(s)
        // (s, p', o) => (s, p, o)

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
        //System.out.println("\nCounts (Support) for the Near-Same Relations: ");
        //printDoubleArray(nearSameCnt);
        
        for (int i = 0; i < numPs; i++){
            if (nearSameMsmt[i] != 0){
                nearSameMsmt[i] = 1.0*maxP(nearSameCnt, i)/nearSameMsmt[i];
            }
        }
        System.out.println("Measurements (Head Coverage) for Near-Same Anomalies: ");
        printArray(nearSameMsmt);

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
        //System.out.println("Average Near-Same Value: " + avg);
        //System.out.println("Number of near-same anomalies past .99 threshold: " + anomalyCount);
        return nearSameMsmt;
    }


     /**
     * Identifies quantity of reflexive anomalies in a set of p.
     * @param dataMap The map (representing a graph) to search in.
     * @param numPs number of p's in the complete dataset
     * @return the final measurement for each p
     */
    public static double[] findDuplicate(HashMap<Integer, ArrayList<Triple>> dataMap, int numPs){
        // duplicate anomalies are when there are multiple occurances of the same: (s)-[p]->(o)
        int[] duplicateCnt = new int[numPs];
        double[] duplicateMsmt = new double[numPs];
        Comparator<Triple> c = new CompareTriple();
        for (ArrayList<Triple> tList : dataMap.values()){
            int ptr = 0;
            int p = tList.get(0).p;
            duplicateMsmt[p] = tList.size();
            while (ptr < tList.size()){
                Triple curr = tList.get(ptr);
                boolean dupe = false;
                if (ptr != 0){
                    if (c.compare(curr, tList.get(ptr-1)) == 0){
                        duplicateCnt[p]++;
                        dupe = true;
                    }
                }
                if (ptr != tList.size()-1 && !dupe){
                    if (c.compare(curr, tList.get(ptr+1)) == 0){
                        duplicateCnt[p]++;
                    }
                }
                ptr++;
            }
        }
        // printing results
        // every triple that is part of a duplicate is counted
        //System.out.println("\nCounts for the Duplicate Relations: ");
        //printArray(duplicateCnt);
        
        for (int i = 0; i < numPs; i++){
            if (duplicateMsmt[i] != 0){
                duplicateMsmt[i] = 1.0*duplicateCnt[i]/duplicateMsmt[i];
            }
        }
        // System.out.println("Measurements for Duplicate Anomalies: ");
        // printArray(duplicateMsmt);

        int anomalyCount = 0;
        double avg = 0;
        for (int a = 0; a < numPs; a++){
            avg += duplicateMsmt[a];
            if (duplicateMsmt[a] >= 0.99){
                anomalyCount++;
            }
        }
        // only consider the number of ps we actually have for the average, not numPs
        avg = avg / dataMap.size();
        //System.out.println("Average Duplicate Value: " + avg);
        //System.out.println("Number of duplicate anomalies past .99 threshold: " + anomalyCount + " out of " + numPs);
        return duplicateMsmt;
    }


    /**
     * Identifies quantity of reflexive anomalies in a set of p.
     * @param dataMap The map (representing a graph) to search in.
     * @param numPs number of p's in the complete dataset
     * @return the final measurement for each p
     */
    public static double[] findReflexive(HashMap<Integer, ArrayList<Triple>> dataMap, int numPs){
        // reflexive anomalies follow the pattern: (s)-[p]->(s)

        int[] reflexiveCnt = new int[numPs];
        double[] reflexiveMsmt = new double[numPs];
        for (ArrayList<Triple> tList : dataMap.values()){
            int currP = tList.get(0).p;
            reflexiveMsmt[currP] = tList.size();
            for (Triple t : tList){
                if (t.s.equals(t.o)){
                    reflexiveCnt[currP]++;
                }
            }
        }
        // printing results
        //System.out.println("\nCounts for the Reflexive Relations: ");
        //printArray(reflexiveCnt);
        
        for (int i = 0; i < numPs; i++){
            if (reflexiveMsmt[i] != 0){
                reflexiveMsmt[i] = 1.0*reflexiveCnt[i]/reflexiveMsmt[i];
            }
        }
        // System.out.println("Measurements for Reflexive Anomalies: ");
        // printArray(reflexiveMsmt);

        int anomalyCount = 0;
        double avg = 0;
        for (int a = 0; a < numPs; a++){
            avg += reflexiveMsmt[a];
            if (reflexiveMsmt[a] >= 0.99){
                anomalyCount++;
            }
        }
        // only consider the number of ps we actually have for the average, not numPs
        avg = avg / dataMap.size();
        //System.out.println("Average Reflexive Value: " + avg);
        //System.out.println("Number of reflexive anomalies past .99 threshold: " + anomalyCount + " out of " + numPs);
        return reflexiveMsmt;
    }


    /**
     * Counts the number of near-reverse anomalies between each set of p.
     * @param dataMap The map (representing a graph) to search in.
     * @param numPs number of p's in the complete dataset
     * @return the final measurement for each p
     */
    public static double[] findNearReverse(HashMap<Integer, ArrayList<Triple>> dataMap, int numPs){
        // near-reverse anomalies follow the pattern: (s)-[p]->(o)-[pp]->(s)
        // (o, p', s) => (s, p, o)

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
                if (tList.get(0).p < revList.get(0).p){ 
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
        //System.out.println("\nCounts (Support) for the Near-Reverse Relations: ");
        //printDoubleArray(nearReverseCnt);

        for (int i = 0; i < numPs; i++){
            if (nearRevMsmt[i] != 0){
                nearRevMsmt[i] = 1.0*maxP(nearReverseCnt, i)/nearRevMsmt[i];
            }
        }
        System.out.println("Measurements (Head Coverage) for Near-Reverse Anomalies: ");
        printArray(nearRevMsmt);

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
        //System.out.println("Average Near-Reverse Value: " + avg);
        //System.out.println("Number of near-reverse anomalies past .99 threshold: " + anomalyCount);
        return nearRevMsmt;
    }

    
    /**
     * Identifies quantity of symmetric anomalies in a set of p.
     * @param dataMap The map (representing a graph) to search in.
     * @param numPs number of p's in the complete dataset
     * @return the final measurement for each p
     */
    public static double[] findSymmetric(HashMap<Integer, ArrayList<Triple>> dataMap, int numPs){
        // symmetric anomalies follow the pattern: (s)-[p]->(o)-[p]->(s)
        // (o, p, s) => (s, p, o)

        int[] symmetricCnt = new int[numPs];
        double[] symmetricMsmt = new double[numPs];

        for (ArrayList<Triple> tList : dataMap.values()){
            symmetricMsmt[tList.get(0).p] = tList.size();
            // tList is sorted forwards (by s)
            // revList is sorted backwards (by o)
            ArrayList<Triple> revList = new ArrayList<>(tList);
            revList.sort(new CompareTripleByO());

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
                    } else if (tList.get(ptr).o.equals(revList.get(revPtr).s) && tList.get(ptr).s.equals(revList.get(revPtr).o)
                                && !tList.get(ptr).o.equals(tList.get(ptr).s)){
                        symmetricCnt[tList.get(ptr).p]++;
                        ptr++;
                        revPtr++;
                    } else {
                        //System.out.println("Something's wrong. t: " + tList.get(ptr).toString() + " rev: " + revList.get(revPtr).toString());
                        //break;
                        //they are totally equal, do nothing
                        revPtr++;
                    }
                }                
            }
        }

        // printing results
        //System.out.println("\nCounts (Support) for the Symmetric Relations: ");
        //printArray(symmetricCnt);

        for (int i = 0; i < numPs; i++){
            if (symmetricMsmt[i] != 0){
                symmetricMsmt[i] = 1.0*symmetricCnt[i]/symmetricMsmt[i];
            }
        }
        System.out.println("Measurements (Head Coverage) for Symmetric Anomalies: ");
        printArray(symmetricMsmt);

        int anomalyCount = 0;
        double avg = 0;
        for (int a = 0; a < numPs; a++){
            avg += symmetricMsmt[a];
            if (symmetricMsmt[a] >= 0.99){
                anomalyCount++;
            }
        }
        // only consider the number of ps we actually have for the average, not numPs
        avg = avg / dataMap.size();
        //System.out.println("Average Symmetric Value: " + avg);
        //System.out.println("Number of symmetric anomalies past .99 threshold: " + anomalyCount + " out of " + numPs);
        return symmetricMsmt;
    }


    /**
     * Measures the amount of Cartesian product anomalies in a set of p.
     * @param dataMap The map (representing a graph) to search in.
     * @return the final measurement for each p
     */
    public static double[] findCartesianProduct(HashMap<Integer, ArrayList<Triple>> dataMap, int numPs){
        // WWW measurement for Cartesian product anomalies: 
            // total triples / (unique subjects * unique objects)
        double[] result = new double[numPs];
        for (ArrayList<Triple> tList : dataMap.values()){
            int currIndex = tList.get(0).p;

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
        }

        // printing results
        /*
        int anomalyCount = 0;
        double cpSum = 0;
        System.out.println("\nCartesian Product factors for each p:");
        System.out.print("[");
        for (int i = 0; i < result.length; i++){
            
            if (i != 0){
                System.out.print(", ");
            }
            System.out.print(String.format("%.3f", result[i]));
            
            cpSum += result[i];
            if (result[i] >= 0.8){
                anomalyCount++;
            } 
        }
        System.out.println("]");
        */
        // avg of Cartesian products for all p
        //System.out.println("Cartesian average is: " + cpSum/result.length);
        //System.out.println("Number of Cartesian anomalies past .80 threshold: " + anomalyCount);
        return result;
    }

    
    public static double[] findCartesianConfidence(HashMap<Integer, ArrayList<Triple>> dataMap){
        // calculate the confidence of the following rule
        // s->o' ^ s'->o => s->o  where o<>o' and s<>s'

        // idea: put together all the possible combinations of s->o' and s'->o and see what s->o we get out of it (denominator)
        // then check how many of the s->o exist (numerator)

        int[] support = new int[dataMap.size()];
        int[] denominator = new int[dataMap.size()];

        // for each p
        for (ArrayList<Triple> tList : dataMap.values()){
            int currP = tList.get(0).p;
            //System.out.println("p: " + currP);
            HashMap<Integer, ArrayList<Integer>> denomValues = new HashMap<>();

            for (Triple t : tList){
                for (Triple comp: tList){
                    // if o<>o' and s<>s'
                    if (!t.s.equals(comp.s) && !t.o.equals(comp.o)){
                        // if s not seen before, add s->o
                        if(denomValues.get(t.s) == null){
                            denomValues.put(t.s, new ArrayList<Integer>());
                            denomValues.get(t.s).add(comp.o);
                            denominator[currP]++;
                        // if o not seen before, add s->o
                        } else if (!denomValues.get(t.s).contains(comp.o)){
                            denomValues.get(t.s).add(comp.o);
                            denominator[currP]++;
                        }
                    }
                }
            }

            // see which s->o from the denominator exist in the data
            /*
            int ptr = 0;
            for (var entry : denomValues.entrySet()){
                Integer currS = entry.getKey();
                ArrayList<Integer> oList = entry.getValue();
                Collections.sort(oList);
                while (ptr < tList.size()){
                    Triple currTriple = tList.get(ptr);
                    if (currTriple.s.equals(currS)){
                        // look for samesies
                        for (Integer currO : oList){
                            if (currTriple.o.equals(currO)){
                                support[currP]++;
                            } else if (currTriple.o < currO){
                                ptr++;
                            } else {
                                break;
                            }
                        }
                    } else if (currTriple.s < currS){
                        ptr++;
                    } else {
                        break;
                    }
                }
            }
            */
            for (Triple t : tList){
                if (denomValues.get(t.s) != null){
                    if (denomValues.get(t.s).contains(t.o)){
                        support[currP]++;
                    }
                }
            }
            //System.out.print("Support: " + support[currP]);
            //System.out.print("; Denominator: " + denominator[currP]);
            //System.out.println("; Confidence: " + 1.0*support[currP]/denominator[currP]);
        }

        double[] confidence = new double[dataMap.size()];
        for (int i = 0; i < support.length; i++){
            confidence[i] = support[i]*1.0 / denominator[i];
        }

        // System.out.println(" Support: ");
        // printArray(support);
        // System.out.println("Denominator: ");
        // printArray(denominator);
        System.out.println("Measurements (Confidence) for Cartesian Product Anomalies:");
        printArray(confidence);

        return confidence;
    }


    public static double[] oneToMany(HashMap<Integer, ArrayList<Triple>> dataMap, int numPs){
        // (s, p, o') => (s, p, o) such that o' <> o
        int[] counts = new int[numPs];
        double[] measurements = new double[numPs];

        for (ArrayList<Triple> tList : dataMap.values()){
            // count how many times the same subject appears in a different triple
            int p = tList.get(0).p;
            // if (p == 55){
            //     System.out.println("hey");
            // }
            measurements[p] = tList.size();
            int curr = 0;
            while (curr < tList.size()){
                boolean sFound = false;
                if (curr != 0){
                    if (tList.get(curr).s.equals(tList.get(curr-1).s)){
                        if (!tList.get(curr).o.equals(tList.get(curr-1).o)){
                            counts[p]++;
                            sFound = true;
                        } else {
                            int i = curr-1;
                            while (i >=0 && tList.get(curr).s.equals(tList.get(i).s)){
                                if (!tList.get(curr).o.equals(tList.get(i).o)){
                                    counts[p]++;
                                    sFound = true;
                                    break;
                                }
                                i--;
                            }
                        }
                    }
                } 
                if (curr != tList.size()-1 && !sFound) {
                    if (tList.get(curr).s.equals(tList.get(curr+1).s)){
                        if (!tList.get(curr).o.equals(tList.get(curr+1).o)){
                            counts[p]++;
                        } else {
                            int i = curr+1;
                            while (i < tList.size() && tList.get(curr).s.equals(tList.get(i).s)){
                                if (!tList.get(curr).o.equals(tList.get(i).o)){
                                    counts[p]++;
                                    break;
                                }
                                i++;
                            }
                        }
                    }
                }
                curr++;
            }
        }

        // printing results
        // System.out.println("\nCounts (Support) for the 1:N Relations: ");
        // printArray(counts);

        for (int i = 0; i < numPs; i++){
            if (measurements[i] != 0){
                measurements[i] = 1.0*counts[i]/measurements[i];
            }
        }
        
        System.out.println("Measurements (Head Coverage) for 1:N Relations: ");
        printArray(measurements);

        int anomalyCount = 0;
        double avg = 0;
        for (int a = 0; a < numPs; a++){
            avg += measurements[a];
            if (measurements[a] >= 0.99){
                anomalyCount++;
            }
        }
        // only consider the number of ps we actually have for the average, not numPs
        avg = avg / dataMap.size();
        //System.out.println("Average 1:N Value: " + avg);
        //System.out.println("Number of 1:N anomalies past .99 threshold: " + anomalyCount + " out of " + numPs);
        return measurements;
    }


    public static double[] manyToOne(HashMap<Integer, ArrayList<Triple>> dataMap, int numPs){
        //(s', p, o) => (s, p, o) such that s' <> s
        int[] counts = new int[numPs];
        double[] measurements = new double[numPs];

        for (ArrayList<Triple> forwardList : dataMap.values()){
            // count how many times the same object appears in a different triple
            ArrayList<Triple> tList = new ArrayList<Triple>(forwardList);
            tList.sort(new CompareTripleByO());
            int p = tList.get(0).p;
            measurements[p] = tList.size();
            int curr = 0;
            while (curr < tList.size()){
                boolean oFound = false;
                if (curr != 0){
                    if (tList.get(curr).o.equals(tList.get(curr-1).o)){
                        if (!tList.get(curr).s.equals(tList.get(curr-1).s)){
                            counts[p]++;
                            oFound = true;
                        } else {
                            int i = curr-1;
                            while (i >= 0 && tList.get(curr).o.equals(tList.get(i).o)){
                                if (!tList.get(curr).s.equals(tList.get(i).s)){
                                    counts[p]++;
                                    oFound = true;
                                    break;
                                }
                                i--;
                            }
                        }
                    }
                } 
                if (curr != tList.size()-1 && !oFound) {
                    if (tList.get(curr).o.equals(tList.get(curr+1).o)){
                        if (!tList.get(curr).s.equals(tList.get(curr+1).s)){
                            counts[p]++;
                        } else {
                            int i = curr+1;
                            while (i < tList.size() && tList.get(curr).o.equals(tList.get(i).o)){
                                if (!tList.get(curr).s.equals(tList.get(i).s)){
                                    counts[p]++;
                                    break;
                                }
                                i++;
                            }
                        }
                    }
                }
                curr++;
            }
        }

        // printing results
        // System.out.println("\nCounts (Support) for the N:1 Relations: ");
        // printArray(counts);

        for (int i = 0; i < numPs; i++){
            if (measurements[i] != 0){
                measurements[i] = 1.0*counts[i]/measurements[i];
            }
        }
        
        System.out.println("Measurements (Head Coverage) for N:1 Relations: ");
        printArray(measurements);

        int anomalyCount = 0;
        double avg = 0;
        for (int a = 0; a < numPs; a++){
            avg += measurements[a];
            if (measurements[a] >= 0.99){
                anomalyCount++;
            }
        }
        // only consider the number of ps we actually have for the average, not numPs
        avg = avg / dataMap.size();
        //System.out.println("Average N:1 Value: " + avg);
        //System.out.println("Number of N:1 anomalies past .99 threshold: " + anomalyCount + " out of " + numPs);
        return measurements;
    }


    /**
     * Measures the amount of Transitive relations in a set of p.
     * @param dataMap The map (representing a graph) to search in.
     * @param numPs number of p's in the complete dataset
     * @return an array of the final Transitive measurement for each p.
     */
    public static double[] findTransitive(HashMap<Integer, ArrayList<Triple>> dataMap, int numPs){
        // transitive anomalies follow the pattern: (s)-[p]->(x)-[p]->(o), (s)-[p]->(o)
        // (s, p, x) ^ (x, p, o) => (s, p, o)

        int[] headCnt = new int[numPs];
        double[] transitiveMsmt = new double[numPs];

        // for each p value
        for (ArrayList<Triple> tList : dataMap.values()){
            transitiveMsmt[tList.get(0).p] = tList.size();

            // find relations with same s value, and store their o values
            Map<Integer, ArrayList<Integer>> map = new HashMap<>();
            for (Triple t : tList){
                if (map.get(t.s) == null){
                    map.put(t.s, new ArrayList<Integer>());
                }
                map.get(t.s).add(t.o);
            }

            int hCount = 0;
            // for keeping track of what relations have been counted already
            Map<Integer, ArrayList<Integer>> countingMap = new HashMap<>();

            for (Map.Entry<Integer, ArrayList<Integer>> entry : map.entrySet()){
                Integer currS = entry.getKey();
                ArrayList<Integer> xList = entry.getValue();
                if (xList.size() >= 2){
                    // look an xs to match this xo value
                    // when you find a matching xs, see it has an o that is in the xList
                    int xPtr = 0;
                    int tPtr = 0;
                    while (xPtr < xList.size() && tPtr < tList.size()){
                        Integer xItem = xList.get(xPtr);
                        Triple tItem = tList.get(tPtr);
                        if (xItem.equals(tItem.s)){
                            // found an xs that matches this xo, now does it's o also exist in x list
                            if(xList.contains(tItem.o)){
                                //add item to counting map to keep track
                                if (countingMap.get(currS) == null){
                                    countingMap.put(currS, new ArrayList<Integer>());
                                }
                                // only add head to counting map
                                if (!countingMap.get(currS).contains(tItem.o)){
                                    countingMap.get(currS).add(tItem.o);
                                    hCount++;
                                }
                                tPtr++;
                            } else {
                                tPtr++;
                            }
                        } else if (xItem < tItem.s){
                            xPtr++;
                        } else {
                            tPtr++;
                        }
                    }
                }
            }
            headCnt[tList.get(0).p] = hCount;
        }

        //System.out.println("Support for Transitive Relations");
        //printArray(headCnt);

        for (int i = 0; i < numPs; i++){
            if (transitiveMsmt[i] != 0){
                transitiveMsmt[i] = headCnt[i]*1.0 / transitiveMsmt[i];
            }
        }
        System.out.println("Transitive Measurements (Head Coverage): ");
        printArray(transitiveMsmt);

        int anomalyCount = 0;
        double avg = 0;
        for (int a = 0; a < numPs; a++){
            avg += transitiveMsmt[a];
            if (transitiveMsmt[a] >= 0.99){
                anomalyCount++;
            }
        }
        // only consider the number of ps we actually have for the average, not numPs
        avg = avg / dataMap.size();
        //System.out.println("Average Transitive Value: " + avg);

        return transitiveMsmt;
    }

    /**
     * Measures the amount of Transitive relations in a set of p.
     * @param dataMap The map (representing a graph) to search in.
     * @param numPs number of p's in the complete dataset
     * @return an array of the final Transitive measurement for each p.
     */
    public static double[] newTransitive(HashMap<Integer, ArrayList<Triple>> dataMap, int numPs){
        // transitive anomalies follow the pattern: (s)-[p]->(x)-[p]->(o), (s)-[p]->(o)
        // (s, p, x) ^ (x, p, o) => (s, p, o)
        // Very slow compared to findTransitive() function

        int[] headCnt = new int[numPs];
        double[] transitiveMsmt = new double[numPs];

        // for each p value
        for (ArrayList<Triple> tList : dataMap.values()){   

            transitiveMsmt[tList.get(0).p] = tList.size();

            // find relations with same s value, and store their o values
            Map<Integer, ArrayList<Integer>> map = new HashMap<>();
            for (Triple t : tList){
                if (map.get(t.s) == null){
                    map.put(t.s, new ArrayList<Integer>());
                }
                map.get(t.s).add(t.o);
            }

            // how many relations have been involved in a transitive relationship
            int hCount = 0;
            for (Map.Entry<Integer, ArrayList<Integer>> entry : map.entrySet()){
                Integer currS = entry.getKey();
                ArrayList<Integer> xList = entry.getValue();
                if (xList.size() >= 2){
                    ArrayList<Triple> sortedTList = new ArrayList<Triple>(tList);
                    sortedTList.sort(new CompareTripleByO());
                    Collections.sort(xList);
                    // look an o value to match the [s->o] o value
                    // when you find a matching o, see if it has an x that is in the xList to get [x->o]
                    int xPtr = 0;
                    int tPtr = 0;
                    while (xPtr < xList.size() && tPtr < sortedTList.size()){
                        Integer xItem = xList.get(xPtr);
                        Triple tItem = sortedTList.get(tPtr);
                        if (xItem.equals(tItem.o)){
                            // found matching o, does it's x/s also exist in x list
                            if(xList.contains(tItem.s)){
                                hCount++;
                                tPtr++;
                                xPtr++;
                            } else {
                                tPtr++;
                            }
                        } else if (xItem < tItem.o){
                            xPtr++;
                        } else {
                            tPtr++;
                        }
                    }
                }
            }
            headCnt[tList.get(0).p] = hCount;
        }

        //System.out.println("Support for Transitive Relations");
        //printArray(headCnt);

        for (int i = 0; i < numPs; i++){
            if (transitiveMsmt[i] != 0){
                transitiveMsmt[i] = headCnt[i]*1.0 / transitiveMsmt[i];
            }
        }
        // System.out.println("Transitive Measurements (Head Coverage): ");
        // printArray(transitiveMsmt);

        return transitiveMsmt;
    }


    /**
     * Finds the overall anomaly coefficient for a list of p values.
     * @param nearSame near-same anomaly values for each p
     * @param nearReverse near-reverse anomaly values for each p
     * @param cartesian Cartesian product anomaly values for each p
     * @return list of overall anomaly coefficient for each p value
     */
    public static double[] findOverallAnomaly(double[] nearSame, double[] nearReverse, double[] cartesian,
        double[] reflexive, double[] transitive, double[] duplicate, double[] symmetric){

        int numPs = nearSame.length;
        double[] result = new double[numPs];
        ArrayList<String> resultDetails = new ArrayList<>();
        
        // for each p, find greatest value and type
        for (int i = 0; i < numPs; i++){
            String maxType = "None";
            double maxVal = 0.0;
            if (nearSame[i] > maxVal){
                maxVal = nearSame[i];
                maxType = "N-Dup";
            }
            if (nearReverse[i] > maxVal){
                maxVal = nearReverse[i];
                maxType = "N-Rev";
            }
            if (cartesian[i] > maxVal){
                maxVal = cartesian[i];
                maxType = "Cart";
            }
            if (transitive[i] > maxVal){
                maxVal = transitive[i];
                maxType = "Tr";
            }
            if (symmetric[i] > maxVal){
                maxVal = symmetric[i];
                maxType = "Sym";
            }
            if (reflexive[i] > maxVal){
                maxVal = reflexive[i];
                maxType = "Ref";
            }
            if (duplicate[i] > maxVal){
                maxVal = duplicate[i];
                maxType = "Dup";
            }
            
            result[i] = maxVal;
            String details = maxType + " : " + String.format("%.3f", maxVal);
            resultDetails.add(details);

        }
        // System.out.println("Overall Anomaly Information: ");
        // System.out.println(resultDetails);

        return result;
    }

        /**
     * Finds the overall anomaly coefficient for a list of p values.
     * @param nearSame near-same anomaly values for each p
     * @param nearReverse near-reverse anomaly values for each p
     * @param cartesian Cartesian product anomaly values for each p
     * @return list of overal anomaly coefficient for each p value
     */
    public static double[][] findOverallTypedAnomaly(double[] nearSame, double[] nearReverse, double[] cartesian,
        double[] reflexive, double[] transitive, double[] duplicate, double[] symmetric){

        int numPs = nearSame.length;
        // result with type: [[Type, Value], ...]
        double[][] resultWithType = new double[numPs][2];
        ArrayList<String> resultDetails = new ArrayList<>();
        
        // for each p, find greatest value and type
        for (int i = 0; i < numPs; i++){
            String maxType = "None";
            double maxVal = 0.0;
            int maxTypeVal = 0;
            if (nearSame[i] > maxVal){
                maxVal = nearSame[i];
                maxType = "N-Dup";
                maxTypeVal = 0;
            }
            if (nearReverse[i] > maxVal){
                maxVal = nearReverse[i];
                maxType = "N-Rev";
                maxTypeVal = 1;
            }
            if (cartesian[i] > maxVal){
                maxVal = cartesian[i];
                maxType = "Cart";
                maxTypeVal = 2;
            }
            if (transitive[i] > maxVal){
                maxVal = transitive[i];
                maxType = "Tr";
                maxTypeVal = 3;
            }
            if (symmetric[i] > maxVal){
                maxVal = symmetric[i];
                maxType = "Sym";
                maxTypeVal = 4;
            }
            if (reflexive[i] > maxVal){
                maxVal = reflexive[i];
                maxType = "Ref";
                maxTypeVal = 5;
            }
            /*
            if (duplicate[i] > maxVal){
                maxVal = duplicate[i];
                maxType = "Dup";
                maxTypeVal = 6;
            }
            */
            resultWithType[i][0] = maxTypeVal;
            resultWithType[i][1] = maxVal;
            String details = maxType + " : " + String.format("%.3f", maxVal);
            resultDetails.add(details);

        }
        // System.out.println("Overall Anomaly Information: ");
        // System.out.println(resultDetails);

        return resultWithType;
    }

    public static double[] originalOverallAnomaly(double[] nearSame, double[] nearReverse, double[] cartesian){

        int numPs = nearSame.length;
        double[] result = new double[numPs];
        ArrayList<String> resultDetails = new ArrayList<>();
        
        // for each p, find greatest value and type
        for (int i = 0; i < numPs; i++){
            String maxType = "None";
            double maxVal = 0.0;
            if (nearSame[i] > maxVal){
                maxVal = nearSame[i];
                maxType = "N-Dup";
            }
            if (nearReverse[i] > maxVal){
                maxVal = nearReverse[i];
                maxType = "N-Rev";
            }
            if (cartesian[i] > maxVal){
                maxVal = cartesian[i];
                maxType = "Cart";
            }
            
            result[i] = maxVal;
            String details = maxType + " : " + String.format("%.3f", maxVal);
            resultDetails.add(details);

        }
        // System.out.println("Original Overall Anomaly Information: ");
        // System.out.println(resultDetails);

        return result;
    }



    public static void main(String[] args){
        Comparator<Triple> c = new CompareTriple();

        /*
        // testing near-same
        HashMap<Integer, ArrayList<Triple>> map = new HashMap<>();
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
        */
        // testing reflexive
        HashMap<Integer, ArrayList<Triple>> reflexiveMap = new HashMap<>();
        Comparator<Triple> rc = new CompareTriple();
        ArrayList<Triple> rp0 = new ArrayList<>();
        // 6 transitive
        Collections.addAll(rp0, 
            new Triple(103,3513,0), new Triple( 103,6835,0), 
            new Triple(3513, 103,0), new Triple( 3513,6835,0), 
            new Triple(6835,103,0), new Triple(6835, 3513, 0));
        rp0.sort(rc);
        reflexiveMap.put(2, rp0);
        ArrayList<Triple> rp1 = new ArrayList<>();
        // 3 transitive
        Collections.addAll(rp1, 
            new Triple(12, 13, 1), new Triple(13,14,1), 
            new Triple(20,40,1), new Triple(20,12,1), 
            new Triple(40, 20, 1), new Triple(12, 40,1),
            new Triple(12,14,1), new Triple(20,14,1));
        rp1.sort(c);
        reflexiveMap.put(1, rp1);
        ArrayList<Triple> rp2 = new ArrayList<>();
        // 1 transitive
        Collections.addAll(rp2,
            new Triple(20,72,2), new Triple(72, 10, 2), 
            new Triple(20, 10, 2), new Triple(42, 34, 2));
        rp2.sort(c);
        reflexiveMap.put(0, rp2);
        ArrayList<Triple> rp3 = new ArrayList<>();
        // 10 transitive
        Collections.addAll(rp3,
            new Triple(14, 86, 3), new Triple(14, 25, 3), new Triple(86, 14, 3),
            new Triple(320, 14, 3), new Triple(25, 14, 3), new Triple(14, 320, 3),
            new Triple(86, 320, 3), new Triple(86, 25, 3), new Triple(25, 86, 3),
            new Triple(320, 86, 3));
        rp3.sort(c);
        reflexiveMap.put(3, rp3);
        ArrayList<Triple> rp4 = new ArrayList<>();
        // 0 transitive
        Collections.addAll(rp4, 
            new Triple(1, 2, 4), new Triple(1, 2, 4), 
            new Triple(1, 4, 4), new Triple(1, 4, 4));
        rp4.sort(c);
        reflexiveMap.put(4, rp4);
        ArrayList<Triple> rp5 = new ArrayList<>();
        // 3 transitive
        Collections.addAll(rp5,
            new Triple(2, 1, 5), new Triple(2, 3, 5), new Triple(2, 4, 5),
            new Triple(2, 5, 5), new Triple(4, 2, 5), new Triple(4, 3, 5),
            new Triple(3, 5, 5));
        rp5.sort(c);
        reflexiveMap.put(5, rp5);
        
        
        //System.out.println(reflexiveMap);
        //findTransitive(reflexiveMap, 6);
        //newTransitive(reflexiveMap, 6);
        findCartesianConfidence(reflexiveMap);
        //oneToMany(reflexiveMap, 6);
        //manyToOne(reflexiveMap, 6);
        //findCartesianProduct(reflexiveMap, 6);
    }
}
