import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Aggregate {
    public static void printRedundancies(HashMap<String, Float[]> r){
        System.out.println("redundancies = {");
        for(var e : r.entrySet()){
            String spacing = "\t";
            if (e.getKey().equals("N:1") || e.getKey().equals("1:N")){
                spacing = "\t\t";
            } else if (e.getKey().equals("Near-Duplicate")){
                spacing = "";
            }
            System.out.print("\t" + e.getKey() + ": "+spacing+"[");
            for(Float f : e.getValue()){
                System.out.print(String.format("%.3f", f) + ", ");
            }
            System.out.println("],");
        }
        System.out.println("}");
    }


    // absolute error aggregation
    public static Double[] sumRedundancy(HashMap<String, Float[]> r){
        int numPs = r.get("N:1").length;
        Double[] result = new Double[numPs];

        for (int p = 0; p < numPs; p++){
            Double pSum = 0.0;
            for (var v : r.values()){
                pSum += v[p];
            }
            result[p] = pSum;
        }
        
        return result;
    }
    

    public static void main(String args[]){
        HashMap<String, Float[]> redundancies = new HashMap<>();

        String baseFolder = "C:/Users/lklec/AnomalyResearch/AnomalyResearchResults/All/";
        String currFile = "Anomalies_21049081_13.out";
        File f = new File(baseFolder + currFile);
        try (Scanner scan = new Scanner(f)){
            String line1 = scan.nextLine();
            String line2 = scan.nextLine();
            Integer numPs = Integer.parseInt(line2.substring(24));;

            long numTriples = 0;
            while(true){
                String line = scan.nextLine();
                String[] splitLine = line.split(" ");
                if (splitLine[0].equals("Adding")){
                    numTriples += Long.parseLong(splitLine[1]);
                } else {
                    break;
                }
            }

            String[] keys = {"Near-Duplicate", "Near-Reverse", "Transitive", "Symmetric", "N:1", "1:N"};
            for(int i = 0; i < 11; i++){
                String line = scan.nextLine();
                if (i % 2 == 0){
                    Float[] values = Arrays.stream(line.split(", ")).map(Float::valueOf).toArray(Float[]::new);
                    redundancies.put(keys[i/2], values);
                }
            }

            String line = scan.nextLine();
            while (!line.substring(0, 12).equals("Measurements")){
                line = scan.nextLine();
            }
            line = scan.nextLine();
            Float[] values = Arrays.stream(line.split(", ")).map(Float::valueOf).toArray(Float[]::new);
            redundancies.put("Cartesian", values);

        } catch (Exception e) {
            System.out.println("Exception found: " + e.getMessage());
        }

        //printRedundancies(redundancies);

        Double[] sums = sumRedundancy(redundancies);
        System.out.print("[");
        for(Double s : sums){
            System.out.print(String.format("%.3f", s)+ ", ");
        }
        System.out.println("]");
    }
}
