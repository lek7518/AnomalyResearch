import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class App {

    /***
     * Create a small test HashMap from data provided inside the function
     * @return the test HashMap
     */
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

    /***
     * Takes in a folder that contains files with training, validation, 
     * and test triples and creates a hash map
     * @param dataFolder folder containing training, validation, and test text files
     * @param numPs number of predicates
     * @param train true to include training data, false otherwise
     * @param valid true to include validation data, false otherwise
     * @param test true to include test data, false otherwise
     * @return hash map with predicate as the key, value is an ArrayList of Triples
     */
    public static HashMap<Integer, ArrayList<Triple>> parseHashMap(
        String dataFolder, int numPs, boolean train, boolean valid, boolean test){
        HashMap<Integer, ArrayList<Triple>> dataMap = new HashMap<>();
        boolean isNell = false;
        if (dataFolder.contains("NELL")){
            isNell = true;
        }
        try{
            ArrayList<String> filenames = new ArrayList<>();
            if (train){
                filenames.add("train2id.txt");
            }
            if (valid){
                filenames.add("valid2id.txt");
            }
            if (test){
                filenames.add("test2id.txt");
            }
            for (String filename : filenames){
                String additional = "/";
                if (isNell) {
                    additional += "resplit_";
                }
                File f = new File(dataFolder + additional + filename);
                Scanner scan = new Scanner(f);
                System.out.println("Adding " + scan.nextLine() + " triples to a new hash map from " + f.getName());
                int pCount = 0;
                while(scan.hasNextLine()){
                    String[] nums = scan.nextLine().split("[\t ]");
                    // for files with (s, o, p) format
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


    /***
     * parseHashMap function for Experimental data.
     * Takes in a folder that contains files with training and test triples in (s, p, o) format
     * and creates a hash map for other experimental data.
     * @param dataFolder folder containing training and test text files (ex: YAGO3-10/experiments/epsilon_0.1_alpha_0.05)
     * @param numPs number of predicates
     * @param train true to include training data, false otherwise
     * @param valid true to include validation data, false otherwise
     * @param test true to include test data, false otherwise
     * @return hash map with predicate as the key, value is an ArrayList of Triples
     */
    public static HashMap<Integer, ArrayList<Triple>> parseExperimentalHashMap(
        String dataFolder, int numPs, boolean train, boolean valid, boolean test){
        HashMap<Integer, ArrayList<Triple>> dataMap = new HashMap<>();
        try{
            ArrayList<String> filenames = new ArrayList<>();
            if (train){
                //filenames.add("/train2id.txt");
                filenames.add("/train.txt");
            }
            if (valid){
                filenames.add("/valid2id.txt");
            }
            if (test){
                //filenames.add("/test2id.txt");
                filenames.add("/test.txt");
            }
            for (String filename : filenames){
                File f = new File(dataFolder + filename);
                Scanner scan = new Scanner(f);
                int pCount = 0;
                while(scan.hasNextLine()){
                    String[] nums = scan.nextLine().split("[\t ]");
                    // for files with (s, o, p) format
                    // Triple t = new Triple(nums);
                    // for files with (s, p, o) format
                    Triple t = new Triple(Integer.valueOf(nums[0]), Integer.valueOf(nums[2]), Integer.valueOf(nums[1]));
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


    /***
     * Alias for parseHashMap with integers instead of booleans to dictate which splits are included in the hashmap
     * @param dataFolder folder containing training, validation, and test text files
     * @param numPs number of predicates
     * @param train one to include training data, zero otherwise
     * @param valid one to include validation data, zero otherwise
     * @param test one to include test data, zero otherwise
     * @return hash map with predicate as the key, value is an ArrayList of Triples
     */
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


    /***
     * Test a hashmap to ensure it has been correctly created
     * Tests include: correct number of predicates, correct number of triples
     * @param map the hashmap to test
     * @param dataFile the file or folder the hashmap was created from
     * @return true if it passes all tests, false otherwise
     */
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


    /***
     * Finds all redundancy values for all splits of the KG. 
     * Prints the histogram counts for use in python plots.
     * @param currFolder folder of the dataset you wish to analyze
     * @param numPs number of predicates
     * @param numBuckets number of buckets in the histogram
     */
    public static void printHistogramCounts(String currFolder, int numPs, int numBuckets){
        var map = parseHashMap(currFolder, numPs, 1, 1, 1);
        var trainMap  = parseHashMap(currFolder, numPs, true, false, false);
        var testMap = parseHashMap(currFolder, numPs, false, false, true);
        var validMap = parseHashMap(currFolder, numPs, false, true, false);

        double[] anomalies = Anomaly.findOverallAnomaly(Anomaly.findNearSame(map, numPs), 
            Anomaly.findNearReverse(map, numPs), Anomaly.findCartesianProduct(map, numPs), Anomaly.findReflexive(map, numPs),
            Anomaly.findTransitive(map, numPs), Anomaly.findDuplicate(map, numPs), Anomaly.findSymmetric(map, numPs));

        System.out.print("p_counts = ");
        Anomaly.printArray(histogramValues(anomalies, numBuckets));
        System.out.print("all_counts = ");
        Anomaly.printArray(histogramMapValues(anomalies, map, numBuckets));
        System.out.print("train_counts = ");
        Anomaly.printArray(histogramMapValues(anomalies, trainMap, numBuckets));
        System.out.print("valid_counts = ");
        Anomaly.printArray(histogramMapValues(anomalies, validMap, numBuckets));
        System.out.print("test_counts = ");
        Anomaly.printArray(histogramMapValues(anomalies, testMap, numBuckets));
    }

    
    /***
     * Print the percent of the data with each anomaly for use in histograms.
     * @param anomalies anomaly values to use
     * @param map hashmap of triples to use
     * @param numPs number of predicates
     * @param label label of the split in the histogram
     */
    public static void percentPerAnomaly(double[][] anomalies, HashMap<Integer, ArrayList<Triple>> map, int numPs, String label){
        int numAnomalies = 6;
        
        // FORMATTING INFO:
        // anomalyValues = [anomaly type][bucket]
        // anomaly types: [near-duplicate, near-reverse, cartesian, transitive, symmetric, reflexive]
        // buckets: [0-.25, .25-.5, .5-.75, .75-1] 
        double[][] anomalyValues = new double[numAnomalies][4];

        //var map = parseHashMap(currFolder, numPs, 1, 0, 0);
        int[] pSizes = new int[numPs];
        double totalSize = 0.0;
        for (Map.Entry<Integer, ArrayList<Triple>> entry : map.entrySet()){
            int p = entry.getKey();
            if (label.equals("p")){
                pSizes[p] = 1;
            } else {
                pSizes[p] = entry.getValue().size();
            }            
            totalSize += pSizes[p];
        }
        
        for (Map.Entry<Integer, ArrayList<Triple>> entry : map.entrySet()){
            int i = entry.getKey();
            int type = (int)anomalies[i][0];
            double value = anomalies[i][1];
            if (value < 0.25){
                anomalyValues[type][0] += (pSizes[i]/totalSize*100);
            } else if (value >= 0.25 && value < 0.5){
                anomalyValues[type][1] += (pSizes[i]/totalSize*100);
            } else if (value >= 0.5 && value < 0.75){ 
                anomalyValues[type][2] += (pSizes[i]/totalSize*100);
            } else {
                anomalyValues[type][3] += (pSizes[i]/totalSize*100);
            }
        }

        System.out.print("'" + label + "': [");
        for (int j = 0; j < numAnomalies; j++){
            if (j != 0){ System.out.print(", "); }
            System.out.print("[" + anomalyValues[j][0] + ", " + anomalyValues[j][1] + ", " + anomalyValues[j][2] + ", " + anomalyValues[j][3] + "]" );
        }
        System.out.println("],");
    }


    /***
     * Print the python code for histograms measuring the percent of the data in each anomaly. 
     * Currently set up for experimental data (ex: YAGO/experiments/epsilon_1_alpha_0.95). Code for regular splits inside.
     * @param currDataset the dataset to use
     */
    public static void printExperimentGraphData(String currDataset) {
        String baseFolder = "C:/Users/lklec/AnomalyResearch/Datasets/";
        String currFolder = baseFolder + currDataset;
        int numPs = 0;
        try {
            File relationFile = new File(currFolder + "/relation2id.txt");
            Scanner scan = new Scanner(relationFile);
            numPs = Integer.valueOf(scan.nextLine());
            scan.close();
        } catch (Exception e) {
            System.out.println("Relation file not found. Ex: " + e);
        }
        System.out.println("Number of relations P: " + numPs);
        
        /* 
        // PERCENT HISTOGRAMS FOR REGULAR SPLITS HERE
        // Data for original splits
        var map  = parseHashMap(currFolder, numPs, true, true, true);
        
        double[][] anomalies = Anomaly.findOverallTypedAnomaly(Anomaly.findNearSame(map, numPs), 
            Anomaly.findNearReverse(map, numPs), Anomaly.findCartesianProduct(map, numPs), Anomaly.findReflexive(map, numPs),
            Anomaly.findTransitive(map, numPs), Anomaly.findDuplicate(map, numPs), Anomaly.findSymmetric(map, numPs));

        var trainMap  = parseHashMap(currFolder, numPs, true, false, false); 
        var testMap = parseHashMap(currFolder, numPs, false, false, true);
        var validMap = parseHashMap(currFolder, numPs, false, true, false);
        var trainValidMap = parseHashMap(currFolder, numPs, true, true, false);
        
        System.out.println("dataset_name = \"" + currDataset + " Original\"");
        System.out.println("percents = {");
        percentPerAnomaly(anomalies, map, numPs, "p");
        percentPerAnomaly(anomalies, map, numPs, "All");
        percentPerAnomaly(anomalies, trainValidMap, numPs, "Train");
        percentPerAnomaly(anomalies, testMap, numPs, "Test");
        System.out.println("}\n");

        System.out.println("dataset_name = \"" + currDataset + " Original\"");
        System.out.println("percents = {");
        percentPerAnomaly(anomalies, map, numPs, "p");
        percentPerAnomaly(anomalies, map, numPs, "All");
        percentPerAnomaly(anomalies, trainMap, numPs, "Train");
        percentPerAnomaly(anomalies, validMap, numPs, "Valid");
        percentPerAnomaly(anomalies, testMap, numPs, "Test");
        System.out.println("}\n");
        */

        // PERCENT HISTOGRAMS FOR EXPERIMENTAL SPLITS HERE
        // need to run epsilon_0_alpha_0.95, epsilon_1_alpha_0.05, epsilon_0_alpha_0.95
        baseFolder = currFolder + "/experiments/";
        try {
            //File base = new File(baseFolder);
            String[] toRun = {"epsilon_1_alpha_0.95"};
            //for (String currExperiment : base.list()){
            for (String currExperiment : toRun){
                System.out.println("dataset_name = \"" + currDataset + " " + currExperiment + "\"");
                currFolder = baseFolder + currExperiment;
                
                var map  = parseExperimentalHashMap(currFolder, numPs, true, false, true);
                var anomalies = Anomaly.findOverallTypedAnomaly(Anomaly.findNearSame(map, numPs), 
                    Anomaly.findNearReverse(map, numPs), Anomaly.findCartesianProduct(map, numPs), Anomaly.findReflexive(map, numPs),
                    Anomaly.findTransitive(map, numPs), Anomaly.findDuplicate(map, numPs), Anomaly.findSymmetric(map, numPs));
                var trainMap  = parseExperimentalHashMap(currFolder, numPs, true, false, false);
                var testMap = parseExperimentalHashMap(currFolder, numPs, false, false, true);
                
                System.out.println("percents = {");
                percentPerAnomaly(anomalies, map, numPs, "p");
                percentPerAnomaly(anomalies, map, numPs, "All");
                percentPerAnomaly(anomalies, trainMap, numPs, "Train");
                percentPerAnomaly(anomalies, testMap, numPs, "Test");
                System.out.println("}\n");
            }
        } catch (Exception e) {
            System.out.println("Issue with file " + baseFolder + "; Ex: " + e);
        }
    }


    /***
     * Return the number of triples in each predicate p
     * @param map triples to analyze
     * @param numPs number of predicates, total
     * @return integer array containing the number of triples for each p (p 0 at index 0, etc)
     */
    public static int[] getTriplesPerP (HashMap<Integer, ArrayList<Triple>> map, int numPs){
        int[] triples = new int[numPs];
        int i = 0;
        for (var x : map.entrySet()){
            while (i < x.getKey()){
                triples[i] = 0;
                i++;
            }
            if (x.getKey() == i){
                triples[i] =  x.getValue().size();
            }
            i++;
        }
        while (i < numPs){
                triples[i] = 0;
                i++;
            }
        return triples;
    }


    public static void main(String[] args) throws Exception {
        String baseFolder = "C:/Users/lklec/AnomalyResearch/Datasets/";
        String[] datasets = {"BioKG", "FB13", "FB15K", "FB15K237", "Hetionet", "NELL-995", "WN11", "WN18", "WN18RR", "YAGO3-10"};
        // For each dataset
        for (String dataset : datasets) {
            System.out.println("Analyzing " + dataset + " for redundancies in training & validation.");
            String currFolder = baseFolder + dataset;
            File relationFile = new File(currFolder + "/relation2id.txt");
            Scanner scan = new Scanner(relationFile);
            int numPs = Integer.valueOf(scan.nextLine());
            scan.close();
            System.out.println("Number of predicates P: " + numPs);

            // Create a hashmap of triples contained in the current dataset, accross all splits
            var map = parseHashMap(currFolder, numPs, true, true, true);

            // Find and print all redundancy measurements for the current hashmap
            // Anomaly.findNearSame(map, numPs);
            // Anomaly.findNearReverse(map, numPs); 
            // Anomaly.findTransitive(map, numPs); 
            // Anomaly.findSymmetric(map, numPs);
            // Anomaly.manyToOne(map, numPs);
            // Anomaly.oneToMany(map, numPs);
            // Anomaly.findCartesianConfidence(map);

            // Print the number of triples in each predicate
            Anomaly.printArray(getTriplesPerP(map, numPs));
            System.out.println();
        }

        // Print python code for histogram with percent of data in each anomaly type
        // Note: currently set up for experimental data, uncomment the code in printExperimentGraphData to use for regular datasets
        //printExperimentGraphData("YAGO3-10");
        
        // Print python code for histogram with number of triples in each anomaly type
        //printHistogramCounts(currFolder, numPs, 4);
       
        // Find the type and value of the maximum anomaly of each predicate
        /*
        Anomaly.findOverallAnomaly(
            Anomaly.findNearSame(map, numPs),
            Anomaly.findNearReverse(map, numPs), 
            Anomaly.findCartesianProduct(map, numPs),
            Anomaly.findReflexive(map, numPs),
            Anomaly.findTransitive(map, numPs),
            Anomaly.findDuplicate(map, numPs),
            Anomaly.findSymmetric(map, numPs));
        */
    
        // Find predicates with large Cartesian product, N:1, and 1:N scores for comparison    
        /*
        var cart = Anomaly.findCartesianProduct(trainMap, numPs);
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
    }
}
