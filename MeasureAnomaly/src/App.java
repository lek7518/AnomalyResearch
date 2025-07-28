import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class App {

    public static HashMap<Integer, ArrayList<Triple>> createHashMap(){
        HashMap<Integer, ArrayList<Triple>> dataMap = new HashMap<>();
        Triple[] fakeData = {new Triple(1, 3, 2), new Triple(1, 4, 2)};
        for (Triple i : fakeData){
            if(dataMap.get(i.p) == null){
                dataMap.put(i.p, new ArrayList<Triple>());
                dataMap.get(i.p).add(i);
            } 
            else{
                dataMap.get(i.p).add(i);
            }
        }
        return dataMap;
    }

    /**
     * Takes in a folder that contains files with training, validation, 
     * and test triples and creates a hash map
     * @return hash map with predicate as the key, value is an ArrayList of Triples
     */
    public static HashMap<Integer, ArrayList<Triple>> parseHashMap(
        String dataFolder, int numPs, boolean train, boolean valid, boolean test){
        HashMap<Integer, ArrayList<Triple>> dataMap = new HashMap<>();
        try{
            ArrayList<String> filenames = new ArrayList<>();
            if (train){
                filenames.add("/train2id.txt");
            }
            if (valid){
                filenames.add("/valid2id.txt");
            }
            if (test){
                filenames.add("/test2id.txt");
            }
            for (String filename : filenames){
                File f = new File(dataFolder + filename);
                Scanner scan = new Scanner(f);
                //int n = Integer.valueOf(scan.nextLine());
                System.out.println("Adding " + scan.nextLine() + " triples to a new hash map from " + f.getName());
                int pCount = 0;
                while(scan.hasNextLine()){
                    String[] nums = scan.nextLine().split("[\t ]");
                    Triple t = new Triple(nums);
                    if(pCount <= numPs){
                        if (dataMap.get(t.p) == null){
                            dataMap.put(t.p, new ArrayList<Triple>());
                            dataMap.get(t.p).add(t);
                            pCount++;
                        } else {
                            dataMap.get(t.p).add(t);
                        }
                    } else {
                        dataMap.get(t.p).add(t);
                    }
                }
            
                scan.close();
            }
        }
        catch (FileNotFoundException ex){
            System.out.println("Cannot find file in " + dataFolder + ": " + ex);
        }

        for (ArrayList<Triple> list : dataMap.values()){
            Comparator<Triple> comp = new CompareTriple();
            list.sort(comp);
        }

        return dataMap;
    }


    public static HashMap<Integer, ArrayList<Triple>> parseHashMap(
        String dataFolder, int numPs, int train, int valid, int test){
            boolean trainBool = true;
            boolean validBool = true;
            boolean testBool = true;
            if (train == 0){
                trainBool = false;
            }
            if (valid == 0){
                validBool = false;
            }
            if (test == 0){
                testBool = false;
            }
            return parseHashMap(dataFolder, numPs, trainBool, validBool, testBool);
        }


    public static boolean testMap(HashMap<Integer, ArrayList<Triple>> map, String dataFile){
        boolean result = true;

        // Test 2: Correct number of p
        int p = 0;
        try{
            File f = new File(dataFile);
            File relationFile = new File(f + "/relation2id.txt");
            if (!relationFile.exists()){
                relationFile = new File(f.getParent() + "/relation2id.txt");
            }
            Scanner scan = new Scanner(relationFile);
            p = Integer.valueOf(scan.nextLine());
            scan.close();
        } catch (Exception e){
            System.out.println("Relation file not found in " + dataFile + " or parent file. " + e);
        }
        if (map.size() != p){
            System.out.println("Incorrect number of predicates: " + map.size() + ". Expected: " + p);
            result = false;
        } else {
            System.out.println("TEST2: p = " + p);
        }

        // Test 3: Correct number of triples
        int expectedNumTriples = 0;
        int numTriples = 0;
        try {
            // find expected number of triples
            File f = new File(dataFile + "/entity2id.txt");
            if (!f.exists()) {
                f = new File(dataFile);
                Scanner scan = new Scanner(f);
                expectedNumTriples = Integer.valueOf(scan.nextLine());
                scan.close();
            } else {
                String[] filenames = {"/test2id.txt", "/train2id.txt", "/valid2id.txt"};
                for (String filename : filenames) {
                    f = new File(dataFile + filename);
                    Scanner scan = new Scanner(f);
                    expectedNumTriples += Integer.valueOf(scan.nextLine());
                    scan.close();
                }
            }
        
            // find actual number of triples
            //int itr = 0;
            for (ArrayList<Triple> l : map.values()) {
                numTriples += l.size();
                //System.out.println("for p = " + itr + " list has " + l.size() + " values");
                //itr++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found in " + dataFile + " : " + e);
            result = false;
        }
        if (numTriples != expectedNumTriples){
            System.out.println("Incorrect number of triples: " + numTriples + " Expected: " + expectedNumTriples);
            result = false;
        } else {
            System.out.println("TEST3: n = " + expectedNumTriples);
        }

        return result;
    }

    /**
     * Sorts p values into "buckets" for display as a histogram.
     * Assumes all values are between 0 and 1.
     * @param anomalies the anomaly coefficient of each p to sort
     * @param numBuckets the number of buckets to divide values into
     * @return the number of p values in each bucket
     */
    public static int[] histogramValues (double[] anomalies, int numBuckets){
        int[] quantity = new int[numBuckets];
        double step = 1.0 / numBuckets;
        for (int i = 0; i < numBuckets; i++){
            double curMin = i * step;
            double curMax = (i * step) + step;
            for (int j = 0; j < anomalies.length; j++){
                if (anomalies[j] >= curMin && anomalies[j] < curMax){
                    quantity[i]++;
                }
                if (anomalies[j] == curMax && i == numBuckets-1){
                    quantity[i]++;
                }
            }
        }

        return quantity;
    }

    /**
     * Sorts each triple into a "bucket" based on p value for use in a histogram. 
     * Assumes all p values are between 0 and 1.
     * @param anomalies the anomaly coefficient of each p
     * @param numBuckets the number of buckets to divide values into
     * @return the number of triples in each bucket
     */
    public static int[] histogramMapValues (double[] anomalies, HashMap<Integer, ArrayList<Triple>> map, int numBuckets){
        int[] quantity = new int[numBuckets];
        double step = 1.0 / numBuckets;
        for (int i = 0; i < numBuckets; i++){
            double curMin = i * step;
            double curMax = (i * step) + step;
            for (int p = 0; p < anomalies.length; p++){
                if (anomalies[p] >= curMin && anomalies[p] < curMax){
                    if (map.get(p) != null){
                        quantity[i] += map.get(p).size();
                    }
                }
                if (anomalies[p] == curMax && i == numBuckets-1){
                    if (map.get(p) != null) {
                        quantity[i] += map.get(p).size();
                    }
                }
            }
        }

        return quantity;
    }

/*
    public static void printHistogramCounts(String currFolder, int numPs){
        var map = parseHashMap(currFolder, numPs, 1, 1, 1);
        var trainMap  = parseHashMap(currFolder, numPs, true, false, false);
        var testMap = parseHashMap(currFolder, numPs, false, false, true);
        var validMap = parseHashMap(currFolder, numPs, false, true, false);

        double[] anomalies = Anomaly.findOverallAnomaly(Anomaly.findNearSame(map, numPs), 
            Anomaly.findNearReverse(map, numPs), Anomaly.findCartesianProduct(map));

        System.out.print("p_counts = ");
        Anomaly.printArray(histogramValues(anomalies, 5));
        System.out.print("all_counts = ");
        Anomaly.printArray(histogramMapValues(anomalies, map, 5));
        System.out.print("train_counts = ");
        Anomaly.printArray(histogramMapValues(anomalies, trainMap, 5));
        System.out.print("valid_counts = ");
        Anomaly.printArray(histogramMapValues(anomalies, validMap, 5));
        System.out.print("test_counts = ");
        Anomaly.printArray(histogramMapValues(anomalies, testMap, 5));
    }
*/

    public static void main(String[] args) throws Exception {
        String currFolder = "C:/Users/lklec/AnomalyResearch/Datasets/FB15K237";
        File relationFile = new File(currFolder + "/relation2id.txt");
        Scanner scan = new Scanner(relationFile);
        int numPs = Integer.valueOf(scan.nextLine());
        scan.close();
        System.out.println("Number of relations P: " + numPs);

        
        System.out.println("\nTraining set:");
        var trainMap  = parseHashMap(currFolder, numPs, true, false, false);
        System.out.println(testMap(trainMap, currFolder + "/train2id.txt")); 

        var startTime = System.currentTimeMillis();
        Anomaly.findTransitive(trainMap, numPs);
        var stopTime = System.currentTimeMillis();
        System.out.println("Elapsed time to find transitive relations (old way): " + (stopTime-startTime) + " milliseconds");

        startTime = System.currentTimeMillis();
        Anomaly.newTransitive(trainMap, numPs);
        stopTime = System.currentTimeMillis();
        System.out.println("Elapsed time to find transitive relations (new way): " + (stopTime-startTime) + " milliseconds");

        /*
        Anomaly.findOverallAnomaly(
            Anomaly.findNearSame(trainMap, numPs),
            Anomaly.findNearReverse(trainMap, numPs), 
            Anomaly.findCartesianProduct(trainMap),
            Anomaly.findReflexive(trainMap, numPs),
            Anomaly.findTransitive(trainMap, numPs),
            Anomaly.findDuplicate(trainMap, numPs),
            Anomaly.findSymmetric(trainMap, numPs));
*/

        /*
        var cart = Anomaly.findCartesianProduct(trainMap);
        var otm = Anomaly.oneToMany(trainMap, numPs);
        var mto = Anomaly.manyToOne(trainMap, numPs);

        //System.out.println("Top 1:N and N:1 scores for comparison");
        //System.out.println("Top Cartesian Product scores for comparison:");
        int bigScoreCnt = 0;
        int cartCnt = 0;
        int zeroCnt = 0;
        for (int i = 0; i < trainMap.size(); i++){
            if (cart[i] > 0.7){
                //System.out.println("[p " + i + "; CP " + cart[i]+ ", 1:N " + otm[i] + ", N:1 " + mto[i] + "]");
                //String.format("%.3f", result[i])
            }
            if (otm[i] > 0.99 && mto[i] > 0.99){
                //System.out.println("[p " + String.format("%4d", i) + "; CP " + String.format("%.3f", cart[i]) + ", 1:N " + String.format("%.3f", otm[i])                 + ", N:1 " + String.format("%.3f", mto[i]) + "]");
            }
            if (cart[i] > 0.7 && otm[i] > 0.7 && mto[i] > 0.7){
                bigScoreCnt++;
            }
            if (cart[i] > 0.7){
                cartCnt++;
                if (otm[i] == 0.0 || mto[i] == 0.0){
                    zeroCnt++;
                }
            }
        }
        System.out.println("Ratio of predicates with CP > 0.7, 1:N > 0.7, N:1 > 0.7: " + bigScoreCnt + " / " + trainMap.size() + " = " + (bigScoreCnt*1.0/trainMap.size()));
        System.out.println("Number of predicates with CP > 0.7: " + cartCnt);
        System.out.println("Number of CP > 0.7 where N:1 or 1:N is 0.0: " + zeroCnt);
*/
        
        
/*
        System.out.println("\nValidation set:");
        var validMap = parseHashMap(currFolder, numPs, 0, 1, 0);
        System.out.println(testMap(validMap, currFolder + "/valid2id.txt")); 
        Anomaly.findNearSame(validMap, numPs);
        Anomaly.findNearReverse(validMap, numPs); 
        Anomaly.findCartesianProduct(validMap);    

        System.out.println("\nTest set:");
        var testMap = parseHashMap(currFolder, numPs, false, false, true);
        System.out.println(testMap(testMap, currFolder + "/test2id.txt")); 
        Anomaly.findNearSame(testMap, numPs);
        Anomaly.findNearReverse(testMap, numPs); 
        Anomaly.findCartesianProduct(testMap);
*/
        //printHistogramCounts(currFolder, numPs);
    }
}
