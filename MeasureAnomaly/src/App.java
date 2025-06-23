import java.util.HashMap;
import java.util.ArrayList;
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
    public static HashMap<Integer, ArrayList<Triple>> parseHashMap(String txtFile){
        HashMap<Integer, ArrayList<Triple>> dataMap = new HashMap<>();
        try{
            File f = new File(txtFile);
            Scanner scan = new Scanner(f);
            Integer n = Integer.valueOf(scan.nextLine());
            System.out.println("Adding " + n + " triples to a new hash map.");
            while(scan.hasNextLine()){
                String[] nums = scan.nextLine().split(" ");
                Triple t = new Triple(nums);
                if (dataMap.get(t.p) == null){
                    dataMap.put(t.p, new ArrayList<Triple>());
                    dataMap.get(t.p).add(t);
                } else {
                    dataMap.get(t.p).add(t);
                }
            }
            scan.close();
        }
        catch (FileNotFoundException ex){
            System.out.println("Cannot find " + txtFile);
        }

        return dataMap;
    }

    public static boolean testMap(HashMap<Integer, ArrayList<Triple>> map){
        return map.get(2).subList(0, 10).toString().equals(
            "[[s: 24257, o: 24051, p: 2], [s: 24484, o: 3510, p: 2], [s: 38166, o: 8808, p: 2], [s: 14244, o: 17173, p: 2], [s: 23148, o: 1594, p: 2], [s: 1550, o: 4544, p: 2], [s: 22912, o: 4228, p: 2], [s: 12735, o: 35623, p: 2], [s: 40675, o: 36808, p: 2], [s: 394, o: 9360, p: 2]]");
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(createHashMap());
        Triple t = new Triple(1, 3, 2);
        System.out.println("t: "+t.s + " --"+t.p+"-->"+" " + t.o);
        System.out.println(t);

        // test text parsing with WN18 test set
            // "C:\Users\lklec\AnomalyResearch\Datasets\WN18\test2id.txt"
        var map = parseHashMap("C:/Users/lklec/AnomalyResearch/Datasets/WN18/test2id.txt");
        System.out.println("Hash map size: " + map.size());
        int numTriples = 0;
        for (ArrayList<Triple> l : map.values()) {
            numTriples += l.size();
        }
        System.out.println("Number of triples: " + numTriples);
        System.out.println(testMap(map));
    }
}
