import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Aggregate {
    // output redundancy values
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
        int numPs = r.get("Cartesian").length;
        Double[] result = new Double[numPs];

        for (int p = 0; p < numPs; p++){
            Double pSum = 0.0;
            for (var e : r.entrySet()){
                // do not include cardinality in sum for now
                if (!e.getKey().equals("N:1") && !e.getKey().equals("1:N")){ 
                    pSum += e.getValue()[p];
                }
            }
            result[p] = pSum;
        }
        
        return result;
    }

    
    // find minimum value
    public static Double findMin(Double[] arr){
        Double min = arr[0];
        for(Double x : arr){
            if (x < min){
                min = x;
            }
        }
        return min;
    }


    // find maximum value
    public static Double findMax(Double[] arr){
        Double max = arr[0];
        for(Double x : arr){
            if (x > max){
                max = x;
            }
        }
        return max;
    }


    // sum integer values
    public static int sum(Integer[] values){
        int result = 0;
        for (int i : values){
            result += i;
        }
        return result;
    }


    // x => 1 / x+1
    public static Double[] normalize(Double[] arr){
        Double[] zs = new Double[arr.length];
        for (int i = 0; i < arr.length; i++){
            zs[i] = 1.0 / (1.0 + arr[i]);
        }

        return zs;
    }


    // x => max - x
    public static Double[] normalize2(Double[] arr){
        //another option: normalize between zero and one: zs[i] = (arr[i] - min) / (max - min);
        Double min = 0.0;
        Double max = 5.0;
        Double[] zs = new Double[arr.length];
        for (int i = 0; i < arr.length; i++){
            zs[i] = max - arr[i];
        }

        return zs;
    }


    public static Double[] contraryWeights(Double[] arr){
        Double max = 5.0;
        Double[] res = new Double[arr.length];
        for(int i = 0; i < arr.length; i++){
            res[i] = max - arr[i];
        }
        return res;
    }

    /**
     * Calculate weighted mean
     * @param weights delta(p) values
     * @param triples number of triples per p
     * @return the weighted mean of delta(p) values, weighted by number of triples
     */
    public static Double weightedMean(Double[] weights, Integer[] triples){
        double numerator = 0.0;
        double denominator = 0.0;
        
        for(int i = 0; i < weights.length; i++){
            numerator += weights[i] * (1.0 * triples[i]);
            denominator += triples[i];
        }

        return numerator / denominator;
    }

    
    /**
     * Calculate weighted mean for each redundancy type
     * @param r redundancy hashmap
     * @param triples number of triples for each predicate
     * @return array of weighted means for each type of redundancy
     */
    public static Double[] weightedMeanPerRedundancy(HashMap<String, Float[]> r, Integer[] triples){
        Double[] weightedR = new Double[5];
        for (int w = 0; w < weightedR.length; w++){
            weightedR[w] = 0.0;
        }
        int numPs = r.get("Cartesian").length;
        int totalTriples = sum(triples);
        int i = 0;
        for (var e : r.entrySet()){
            // do not include cardinality in sum for now
            if (!e.getKey().equals("N:1") && !e.getKey().equals("1:N")){ 
                for (int p = 0; p < numPs; p++){
                    weightedR[i] += Double.valueOf(e.getValue()[p]) * Double.valueOf(triples[p]);
                }
                weightedR[i] = weightedR[i] / Double.valueOf(totalTriples);
                i++;
            }
        }     
        
        return weightedR;
    }


    /**
     * Print results in json format
     * @param dataset which dataset
     * @param redundancies map of all types of redundancy for this dataset
     */
    public static void outputWeights(String dataset, HashMap<String, Float[]> redundancies){
        System.out.print("{\"dataset\": \"" + dataset + "\", \"summed_weights\": [");
        Double[] sums = sumRedundancy(redundancies);            
        for(int s = 0; s < sums.length; s++){
            if (s != 0){
                System.out.print(", ");
            }
            System.out.print(String.format("%.3f", sums[s]));
        }
        System.out.print("], \"normalized_weights\": [");
        Double[] normalized = normalize(sums);
        // contrary weights
        //Double[] normalized = contraryWeights(sums);
        for(int n = 0; n < normalized.length; n++){
            if (n != 0){
                System.out.print(", ");
            }
            System.out.print(String.format("%.7f", normalized[n]));
        }
        System.out.print("]},\n");
    }


    /**
     * Print python code for overall redundancy measurement (split by type) histogram
     * @param data histogram data (rows = dataset, col = redundancy type)
     */
    public static void outputHistogramData(Double[][] data){
        // parameter data: rows = dataset, col = redundancy
        // data = {'all the data': [[near-duplicate], [near-reverse], [cartesian], [symmetric], [transitive]]}

        System.out.println("data = {'all': [");
        for(int i = 0; i < 5; i++){
            if (i != 0){
                System.out.print(", ");
            }
            System.out.print("[");
            for(int j = 0; j < 10; j++){
                if (j != 0){
                    System.out.print(", ");
                }
                System.out.print(data[j][i]);
            }
            System.out.print("]");
        }
        System.out.println("]}");
    }


    /**
     * Parses file (given inside function) to get number of triples in each predicate in each dataset
     * @return HashMap where key is dataset name and value is Int[] with number of triples for each predicate in order
     */
    public static HashMap<String, Integer[]> parseNumTriples(){
        HashMap<String, Integer[]> values = new HashMap<>();
        String filepath = "C:/Users/lklec/AnomalyResearch/AnomalyResearchResults/NumTriples_Test.txt";
        File f = new File(filepath);
        try (Scanner scan = new Scanner(f)){
            while (scan.hasNextLine()){
                String datasetName = scan.nextLine().split(" ")[1];
                Integer numP = Integer.parseInt(scan.nextLine().split(" ")[4]);
                long numTriples = 0;
                Integer[] tripleCnts;
                while(true){
                    String line = scan.nextLine();
                    String[] splitLine = line.split(" ");
                    if (splitLine[0].equals("Adding")){
                        numTriples += Long.parseLong(splitLine[1]);
                    } else {
                        String[] tripleArr = line.substring(1, line.length()-1).split(", ");
                        tripleCnts = Arrays.stream(tripleArr).map(Integer::valueOf).toArray(Integer[]::new);
                        break;
                    }
                }
                if(numP == tripleCnts.length){
                    values.put(datasetName, tripleCnts);
                }
                if (scan.hasNextLine()){
                    scan.nextLine();
                }
            }
        } catch(Exception e){
            System.out.println("Exception in parseNumTriples: " + e.getMessage());
        }
        return values;
    }
    

    public static void main(String args[]){
        var tripleCnts = parseNumTriples();
        Double[][] histogramData = new Double[10][5];

        // map to add redundancies to
        HashMap<String, Float[]> redundancies = new HashMap<>();

        // for each dataset in the base folder
        String baseFolder = "C:/Users/lklec/AnomalyResearch/AnomalyResearchResults/Train/";
        File folder = new File(baseFolder);
        //String currFile = "WN18.out";
        //File f = new File(baseFolder + currFile);
        int d = 0;
        for(File f : folder.listFiles()){
            String datasetName = f.getName().split("[\\.]")[0];
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

                // for each type of redundancy
                String[] keys = {"Near-Duplicate", "Near-Reverse", "Transitive", "Symmetric", "N:1", "1:N"};
                for(int i = 0; i < 11; i++){
                    String line = scan.nextLine();
                    if (i % 2 == 0){
                        // get values for each predicate for this type of redundancy and add to map
                        Float[] values = Arrays.stream(line.split(", ")).map(Float::valueOf).toArray(Float[]::new);
                        redundancies.put(keys[i/2], values);
                    }
                }

                String line = scan.nextLine();
                while (!line.substring(0, 12).equals("Measurements")){
                    line = scan.nextLine();
                }
                line = scan.nextLine();
                // add cartesian product values to map
                Float[] values = Arrays.stream(line.split(", ")).map(Float::valueOf).toArray(Float[]::new);
                redundancies.put("Cartesian", values);

            } catch (Exception e) {
                System.out.println("Exception found: " + e.getMessage());
            }
            // outputWeights(datasetName, redundancies);

            // find delta(p) values for each predicate by summing each type
            Double[] weights = sumRedundancy(redundancies);
            // get number of triples in each predicate
            Integer[] triples = tripleCnts.get(datasetName);
            // find overall redundancy measurement: weighted mean of all delta(p) values weighted by number of triples per predicate
            Double wmean = weightedMean(weights, triples);
            System.out.println(datasetName + " Weighted Mean: " + wmean);
            // record weighted mean per redundancy to show the value split by types for the histogram
            histogramData[d] = weightedMeanPerRedundancy(redundancies, triples);
            
            d++;
        } //end for loop

        // output all histogram data
        outputHistogramData(histogramData);
    }
}
