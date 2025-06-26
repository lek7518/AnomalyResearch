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
    public static HashMap<Integer, ArrayList<Triple>> parseHashMap(String datasetFolder){
        HashMap<Integer, ArrayList<Triple>> dataMap = new HashMap<>();
        try{
            File relationFile = new File(datasetFolder + "/relation2id.txt");
            Scanner scan = new Scanner(relationFile);
            int numP = Integer.valueOf(scan.nextLine());
            scan.close();

            String[] filenames = {"/test2id.txt", "/train2id.txt", "/valid2id.txt"};
            for (String filename : filenames){
                File f = new File(datasetFolder + filename);
                scan = new Scanner(f);
                //int n = Integer.valueOf(scan.nextLine());
                System.out.println("Adding " + scan.nextLine() + " triples to a new hash map.");
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

    public static boolean testMap(HashMap<Integer, ArrayList<Triple>> map, String datasetFolder){
        boolean result = true;

        // Test 1: Check the first 10 triples in the p = 2 ArrayList against the correct answer for WN18
        // Arbitrary and no longer useful
        /**
        String twoString = null;
        try{
            twoString = map.get(2).subList(0, 10).toString();
        } catch (IndexOutOfBoundsException ex){
            System.out.println("Insufficient length for p = 2: " + ex);
        }
        if(!twoString.equals("[[s: 24257, o: 24051, p: 2], [s: 24484, o: 3510, p: 2], [s: 38166, o: 8808, p: 2], [s: 14244, o: 17173, p: 2], [s: 23148, o: 1594, p: 2], [s: 1550, o: 4544, p: 2], [s: 22912, o: 4228, p: 2], [s: 12735, o: 35623, p: 2], [s: 40675, o: 36808, p: 2], [s: 394, o: 9360, p: 2]]")){
            System.out.println("Incorrect values for p = 2: \n" + twoString);
            result = false;
        }
        */

        // Test 2: Correct number of p
        int p = 0;
        try{
            File f = new File(datasetFolder + "/relation2id.txt");
            Scanner scan = new Scanner(f);
            p = Integer.valueOf(scan.nextLine());
            scan.close();
        } catch (Exception e){
            System.out.println("Relation file not found in " + datasetFolder + ": " + e);
        }
        System.out.println("TEST2: P = " + p);
        if (map.size() != p){
            System.out.println("Incorrect number of predicates: " + map.size());
            result = false;
        }

        // Test 3: Correct number of triples
        int expectedNumTriples = 0;
        int numTriples = 0;
        try {
            // find expected number of triples
            String[] filenames = {"/test2id.txt", "/train2id.txt", "/valid2id.txt"};
            for (String filename : filenames){
                File f = new File(datasetFolder + filename);
                Scanner scan = new Scanner(f);
                expectedNumTriples += Integer.valueOf(scan.nextLine());
                scan.close();
            }

            // find actual number of triples
            //int itr = 0;
            for (ArrayList<Triple> l : map.values()) {
                numTriples += l.size();
                //System.out.println("for p = " + itr + " list has " + l.size() + " values");
                //itr++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found in " + datasetFolder + " : " + e);
            result = false;
        }
        System.out.println("TEST3: N = " + expectedNumTriples);
        if (numTriples != expectedNumTriples){
            System.out.println("Incorrect number of triples: " + numTriples);
            result = false;
        }

        return result;
    }


    public static void main(String[] args) throws Exception {
        //System.out.println(createHashMap());
        //Triple t = new Triple(1, 3, 2);
        //System.out.println("t: "+t.s + " --"+t.p+"-->"+" " + t.o);
        //System.out.println(t);

        // test text parsing with WN18 test set
            // "C:\Users\lklec\AnomalyResearch\Datasets\WN18\test2id.txt"
        String currFolder = "C:/Users/lklec/AnomalyResearch/Datasets/WN18";
        var map = parseHashMap(currFolder);
        System.out.println(testMap(map, currFolder));  
        //Anomaly.findCartesianProduct(map);     
        //Anomaly.findNearSame(map);
        Anomaly.findNearReverse(map); 
    }
}
