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
     * Takes in a text file that contains triples and creates a hash map
     * @return hash map with predicate as the key, value is an ArrayList of Triples
     */
    public static HashMap<Integer, ArrayList<Triple>> parseHashMap(String dataFilePath, int numPs){
        HashMap<Integer, ArrayList<Triple>> dataMap = new HashMap<>();
        try{
            File f = new File(dataFilePath);
            Scanner scan = new Scanner(f);
            System.out.println("Adding " + scan.nextLine() + " triples to a new hash map from " + f.getName());                
            int pCount = 0;
            while(scan.hasNextLine()){
                String[] nums = scan.nextLine().split(" ");
                Triple t = new Triple(nums);
                if(pCount < numPs){
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
        catch (FileNotFoundException ex){
            System.out.println("Cannot find file in " + dataFilePath + ": " + ex);
        }

        for (ArrayList<Triple> list : dataMap.values()){
            Comparator<Triple> comp = new CompareTriple();
            list.sort(comp);
        }

        return dataMap;
    }

    /**
     * Takes in a text file that contains triples and creates a hash map
     * @return hash map with predicate as the key, value is an ArrayList of Triples
     */
    public static HashMap<Integer, ArrayList<Triple>> parseCombinedHashMap(String datasetFolder, int numP){
        HashMap<Integer, ArrayList<Triple>> dataMap = new HashMap<>();
        try{
            String[] filenames = {"/test2id.txt", "/train2id.txt", "/valid2id.txt"};
            for (String filename : filenames){
                File f = new File(datasetFolder + filename);
                Scanner scan = new Scanner(f);
                //int n = Integer.valueOf(scan.nextLine());
                System.out.println("Adding " + scan.nextLine() + " triples to a new hash map from " + f.getName());
                int pCount = 0;
                while(scan.hasNextLine()){
                    String[] nums = scan.nextLine().split(" ");
                    Triple t = new Triple(nums);
                    if(pCount < numP){
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
            System.out.println("Cannot find file in " + datasetFolder + ": " + ex);
        }

        for (ArrayList<Triple> list : dataMap.values()){
            Comparator<Triple> comp = new CompareTriple();
            list.sort(comp);
        }

        return dataMap;
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


    public static void main(String[] args) throws Exception {
        // test text parsing with WN18 test set
            // "C:\Users\lklec\AnomalyResearch\Datasets\WN18\test2id.txt"
        String currFolder = "C:/Users/lklec/AnomalyResearch/Datasets/FB15K237";
        File relationFile = new File(currFolder + "/relation2id.txt");
        Scanner scan = new Scanner(relationFile);
        int numPs = Integer.valueOf(scan.nextLine());
        scan.close();
        System.out.println("Number of relations P: " + numPs);

        System.out.println("Training set:");
        var trainMap = parseHashMap(currFolder + "/train2id.txt", numPs);
        System.out.println(testMap(trainMap, currFolder + "/train2id.txt")); 
        Anomaly.findNearSame(trainMap, numPs);
        Anomaly.findNearReverse(trainMap, numPs); 
        Anomaly.findCartesianProduct(trainMap);    
/*
        System.out.println("\nValidation set:");
        var validMap = parseHashMap(currFolder + "/valid2id.txt", numPs);
        System.out.println(testMap(validMap, currFolder + "/valid2id.txt")); 
        Anomaly.findNearSame(validMap, numPs);
        Anomaly.findNearReverse(validMap, numPs); 
        Anomaly.findCartesianProduct(validMap);    

        System.out.println("\nTest set:");
        var testMap = parseHashMap(currFolder + "/test2id.txt", numPs);
        System.out.println(testMap(testMap, currFolder + "/test2id.txt")); 
        Anomaly.findNearSame(testMap, numPs);
        Anomaly.findNearReverse(testMap, numPs); 
        Anomaly.findCartesianProduct(testMap);
        *//*
        System.out.println("\nCombined set:");
        var combinedMap = parseCombinedHashMap(currFolder, numPs);
        testMap(combinedMap, currFolder);
        Anomaly.findNearSame(combinedMap, numPs);
        Anomaly.findNearReverse(combinedMap, numPs); 
        Anomaly.findCartesianProduct(combinedMap);
        */
    }
}
