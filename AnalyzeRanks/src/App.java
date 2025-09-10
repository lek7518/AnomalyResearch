import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class App {

     public static HashMap<Integer, ArrayList<ArrayList<Float>>> parseRanks(String filename) throws Exception {
        HashMap<Integer, ArrayList<ArrayList<Float>>> ranks = new HashMap<>();
        try {
            File f = new File(filename);
            Scanner scan = new Scanner(f);
            scan.nextLine();
            Integer previousPredicate = -1;
            while (scan.hasNext()){
                String[] line = scan.nextLine().split("\t");
                String triple = line[0];
                String position = triple.split("-")[1];
                Integer predicate = Integer.valueOf(triple.split(",")[1]);
                Float rank = Float.valueOf(line[1]);
                if (predicate != previousPredicate){
                    if (ranks.get(predicate) == null){
                        ranks.put(predicate, new ArrayList<ArrayList<Float>>());
                        ranks.get(predicate).add(new ArrayList<Float>());
                        ranks.get(predicate).add(new ArrayList<Float>());
                    }
                    previousPredicate = predicate;
                }
                if (position.equals("h")){
                    ranks.get(predicate).get(0).add(rank);
                } else {
                    ranks.get(predicate).get(1).add(rank);
                }
            }
            scan.close();
        } catch (Exception e){
            System.out.println("Issue with file: " + filename + "\ne: " + e);
        }
        return ranks;
    }

    public static void printFloatArr(float[] arr, String title){
        System.out.print(title + ": [");
        for (int i = 0; i < arr.length; i++){
            if (i == 0) {
                System.out.print(arr[i]);
            } else {
                System.out.print(", " + arr[i]);
            }
        }
        System.out.println("]");
    }

    public static float[] fiveNumberSummary(ArrayList<Float> ranks){
        // results = [minimum, Q1, median, Q3, maximum]
        float[] results = new float[5];

        Collections.sort(ranks);

        results[0] = ranks.get(0);

        //Q1, Q2, Q3
        int numRanks = ranks.size();

        int k = (int)(0.25 * (numRanks + 1));
        float a = (float)(0.25 * (numRanks + 1) - k);
        results[1] = ranks.get(k-1) + a * (ranks.get(k) - ranks.get(k-1));

        k = (int)(0.5 * (numRanks + 1));
        a = (float)(0.5 * (numRanks + 1) - k);
        results[2] = ranks.get(k-1) + a * (ranks.get(k) - ranks.get(k-1));

        k = (int)(0.75 * (numRanks + 1));
        a = (float)(0.75 * (numRanks + 1) - k);
        results[3] = ranks.get(k-1) + a * (ranks.get(k) - ranks.get(k-1));
        
        results[4] = ranks.get(numRanks-1);

        return results;
    }

    public static float[] meanRank(ArrayList<Float> ranks){
        Float mr = (float) 0;
        Float mrr = (float) 0;
        int numRanks = ranks.size();
        for (Float rank : ranks){
            mr += rank/numRanks;
            mrr += 1/rank/numRanks;
        }
        //mr = mr / ranks.get(0).get(0).size();
        //mrr = mrr / ranks.get(0).get(0).size();

        var results = new float[2];
        results[0] = mr;
        results[1] = mrr;
        return results;
    }

    public static float arithmaticMeanRank(ArrayList<Float> ranks){
        long sum = 0;
        for (Float rank : ranks){
            sum += rank;
        }

        return sum/ranks.size();
    }

    public static double geometricMeanRank(ArrayList<Float> ranks){
        long sum = 0;
        for (Float rank : ranks){
            sum += Math.log(rank);
        }
        return Math.exp(sum/ranks.size());
    }

    public static double weightedMeanRank(ArrayList<Float> ranks){
        int cSO = 1; // TODO replace with |Cs/o|
        long sum1 = 0;
        long sum2 = 0;
        for (Float rank : ranks){
            sum1 += cSO * Math.log(rank);
            sum2 += cSO;
        }
        return Math.exp(sum1/sum2);
    }

    public static int hitsAtK(ArrayList<Float> ranks, int k){
        int total = 0;
        for (Float rank : ranks) {
            if (rank <= k) {
                total++;
            }
        }
        return total;
    }

    public static void allAnalysis(ArrayList<Float> ranks){
        Float mr = (float) 0;
        Float mrr = (float) 0;
        int hits1 = 0;
        int hits10 = 0;
        int numRanks = ranks.size();
        for (Float rank : ranks){
            mr += rank/numRanks;
            mrr += 1/rank/numRanks;
            if (rank <=  1){
                hits1++;
            }
            if (rank <= 10){
                hits10++;
            }
        }
        System.out.println("Mean Rank: " + mr);
        System.out.println("Mean Reciprocal Rank: " + mrr);
        System.out.println("Hits @ 1: " + hits1);
        System.out.println("Hits @ 10: " + hits10);

    }

    public static void main(String[] args) throws Exception {

        String path = "/C:/Users/lklec/AnomalyResearch/Ranks/0/";
        var fb13_boxe_ranks = parseRanks( path + "boxe__0_ranks.tsv");

        System.out.println("Analyzing prediction on FB13 using BoxE:");

        for (int p : fb13_boxe_ranks.keySet()){
            System.out.println("p = " + p);
            ArrayList<Float> headList = fb13_boxe_ranks.get(p).get(0);
            ArrayList<Float> tailList = fb13_boxe_ranks.get(p).get(1);
            System.out.println("Size of p: " + headList.size());
            
            System.out.println("HEAD");
            allAnalysis(headList);
            System.out.println("AMR: " + arithmaticMeanRank(headList));
            System.out.println("GMR: " + geometricMeanRank(headList));
            printFloatArr(fiveNumberSummary(headList), "5 Number Summary - Head");
            System.out.println("TAIL");
            allAnalysis(tailList);
            System.out.println("AMR: " + arithmaticMeanRank(tailList));
            System.out.println("GMR: " + geometricMeanRank(tailList));
            printFloatArr(fiveNumberSummary(tailList), "5 Number Summary - Tail");
            
            // float[] means = meanRank(headList);
            // System.out.println("MR - Head: " + means[0] + "; MRR - Head: " + means[1]);
            // System.out.println("Hits at 10: " + hitsAtX(headList, 10));
            // means = meanRank(tailList);
            // System.out.println("MR - Tail: " + means[0] + "; MRR - Tail: " + means[1]);
            // System.out.println("Hits at 10: " + hitsAtX(tailList, 10));
        }

        // var fb13_transe_ranks = parseRanks( path + "transe__0_ranks.tsv");
        // System.out.println("Analyzing prediction on FB13 using TransE:");

        // for (int p : fb13_transe_ranks.keySet()){
        //     System.out.println("p = " + p);
        //     ArrayList<Float> headList = fb13_transe_ranks.get(p).get(0);
        //     ArrayList<Float> tailList = fb13_transe_ranks.get(p).get(1);
            
        //     printFloatArr(fiveNumberSummary(headList), "5 Number Summary - Head");
        //     printFloatArr(fiveNumberSummary(tailList), "5 Number Summary - Tail");

        //     float[] means = meanRank(headList);
        //     System.out.println("MR - Head: " + means[0] + "; MRR - Head: " + means[1]);
        //     System.out.println("Hits at 10: " + hitsAtX(headList, 10));
        //     means = meanRank(tailList);
        //     System.out.println("MR - Tail: " + means[0] + "; MRR - Tail: " + means[1]);
        //     System.out.println("Hits at 10: " + hitsAtX(tailList, 10));
        // }
    }
}
