import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class App {

     public static HashMap<Integer, ArrayList<ArrayList<Float>>> oldParseRanks(String filename) throws Exception {
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

    public static HashMap<Integer, ArrayList<Rank>> parseRanks (String filename) throws Exception{
        HashMap<Integer, ArrayList<Rank>> rankMap = new HashMap<>();
        try {
            File f = new File(filename);
            Scanner scan = new Scanner(f);
            scan.nextLine();
            int previousPredicate = -1;
            while (scan.hasNext()){
                String[] line = scan.nextLine().split("\t");
                String[] triple = line[0].split("-")[0].split("[,)()]");
                String position = line[0].split("-")[1];
                boolean head = false;
                if (position.equals("h")){
                    head = true;
                }
                Double rank = Double.valueOf(line[1]);
                int negatives = Integer.valueOf(line[2]);
                Rank r = new Rank(Integer.valueOf(triple[1]), Integer.valueOf(triple[2]), 
                    Integer.valueOf(triple[3]), rank, negatives, head);
                if (r.p != previousPredicate){
                    if (rankMap.get(r.p) == null){
                        rankMap.put(r.p, new ArrayList<Rank>());
                    }
                    previousPredicate = r.p;
                }
                rankMap.get(r.p).add(r);
            }
            scan.close();
        } catch (Exception e){
            System.out.println("Issue with file: " + filename + "\ne: " + e);
        }
        return rankMap;
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

    // public static float arithmaticMeanRank(ArrayList<Float> ranks){
    //     long sum = 0;
    //     for (Float rank : ranks){
    //         sum += rank;
    //     }

    //     return sum/ranks.size();
    // }

    public static float[] mr(ArrayList<Rank> ranks){
        float headResult = 0;
        float tailResult = 0;
        for (Rank r : ranks){
            if (r.isHead){
                headResult += r.rank/(ranks.size()/2);
            } else {
                tailResult += r.rank/(ranks.size()/2);
            }
        }
        float[] result = {headResult, tailResult};
        return result;
    }

    public static double[] mrr(ArrayList<Rank> ranks){
        double headResult = 0;
        double tailResult = 0;
        for (Rank r : ranks){
            if (r.isHead){
                headResult += 1/r.rank/(ranks.size()/2);
            } else {
                tailResult += 1/r.rank/(ranks.size()/2);
            }
        }
        double[] result = {headResult, tailResult};
        return result;
    }

     public static double[] arithmaticMeanRank(ArrayList<Rank> ranks){
        double headSum = 0;
        double tailSum = 0;
        for (Rank r : ranks){
            if (r.isHead){
                headSum += r.rank;
            } else {
                tailSum += r.rank;
            }
        }
        double[] result = {headSum/(ranks.size()/2), tailSum/(ranks.size()/2)};
        return result;
    }

    public static double[] barAMR(ArrayList<Rank> ranks){
        double numTestTriples = ranks.size()/2; //ranks holds 2 ranks for each triple
        double headSum = 0;
        double tailSum = 0;
        double headNegs = 0;
        double tailNegs = 0;
        for (Rank r : ranks){
            if (r.isHead){
                headNegs += r.numNegatives - 1;
                headSum += r.rank;
            } else {
                tailNegs += r.numNegatives - 1;
                tailSum += r.rank;
            }
        }
        //System.out.println(headNegs/numTestTriples + ", " + tailNegs/numTestTriples);
        // Expected = |N|-1 / 2
        double[] expected = {(headNegs/numTestTriples) / 2, (tailNegs/numTestTriples) / 2};
        //System.out.println("Expected: [" + expected[0] + ", " + expected[1] + "]");
        double[] amr = {headSum/numTestTriples, tailSum/numTestTriples};
        //System.out.println("AMR: [" + amr[0] + ", " + amr[1] + "]");

        // AMR bar = 1 - AMR / E(AMR)
        double[] result = {1 - amr[0]/expected[0], 1 - amr[1]/expected[1]};
        return result;
    }

    // public static double geometricMeanRank(ArrayList<Float> ranks){
    //     long sum = 0;
    //     for (Float rank : ranks){
    //         sum += Math.log(rank);
    //     }
    //     return Math.exp(sum/ranks.size());
    // }

     public static double[] geometricMeanRank(ArrayList<Rank> ranks){
        double headSum = 0;
        double tailSum = 0;
        for (Rank r : ranks){
            if (r.isHead){
                headSum += Math.log(r.rank);
            } else {
                tailSum += Math.log(r.rank);
            }
        }
        double[] result = {Math.exp(headSum/(ranks.size()/2)), Math.exp(tailSum/(ranks.size()/2))};
        return result;
    }

    public static double[] weightedMeanRank(ArrayList<Rank> ranks){
        double sum1H = 0;
        double sum2H = 0;
        double sum1T = 0;
        double sum2T = 0;
        for (Rank r : ranks){
            if (r.isHead){
                sum1H += r.numNegatives * Math.log(r.rank);
                sum2H += r.numNegatives;
            } else {
                sum1T += r.numNegatives * Math.log(r.rank);
                sum2T += r.numNegatives;
            }
        }
        double[] result = {Math.exp(sum1H/sum2H), Math.exp(sum1T/sum2T)};
        return result;
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

        int fb13 = 0;
        int fb15k = 1;
        int fb15k237 = 2;
        int nell995 = 3;
        String path = "/C:/Users/lklec/AnomalyResearch/Ranks/";
        HashMap<Integer, ArrayList<Rank>> fb13_boxe_ranks_new = parseRanks(path + fb13 + "/boxe__0_ranks.tsv");
        HashMap<Integer, ArrayList<ArrayList<Float>>> fb13_boxe_ranks_old = oldParseRanks( path + fb13+ "/boxe__0_ranks.tsv");
        HashMap<Integer, ArrayList<Rank>> fb15k_boxe_ranks = parseRanks(path + fb15k + "/boxe__0_ranks.tsv");
        HashMap<Integer, ArrayList<Rank>> fb15k237_boxe_ranks = parseRanks(path + fb15k237 + "/boxe__0_ranks.tsv");
        HashMap<Integer, ArrayList<Rank>> wn18_boxe_ranks = parseRanks(path + 5 + "/boxe__0_ranks.tsv");
        HashMap<Integer, ArrayList<Rank>> wn18rr_boxe_ranks = parseRanks(path + 6 + "/boxe__0_ranks.tsv");
        HashMap<Integer, ArrayList<Rank>> nell_boxe_ranks = parseRanks(path + nell995 + "/boxe_resplit__0_ranks.tsv");

        System.out.println("FB15k-237 BoxE Ranks: ");
        System.out.println("p, MR_head, MR_tail, MRR_head, MRR_tail, AMR_h, AMR_t, GMR_h, GMR_t, WMR_h, WMR_t");
        for (int p : fb15k237_boxe_ranks.keySet()){
            ArrayList<Rank> ranks = fb15k237_boxe_ranks.get(p);
            double[] amr = arithmaticMeanRank(ranks);
            double[] gmr = geometricMeanRank(ranks);
            double[] wmr = weightedMeanRank(ranks);
            double[] amrBar = barAMR(ranks);
            //p, MR_head, MR_tail, MRR_head, MRR_tail, AMR_h, AMR_t, GMR_h, GMR_t, WMR_h, WMR_t
            // System.out.println(p + ", " + mr(ranks)[0] + ", " + mr(ranks)[1] + ", " + mrr(ranks)[0] + ", " + mrr(ranks)[1] + ", "
            //                                 + amr[0] + ", " + amr[1] + ", " + gmr[0] + ", " + gmr[1] + ", " + wmr[0] + ", " + wmr[1]);
            //System.out.println("" + p + ", " + mr(ranks)[0] + ", " + mr(ranks)[1] + ", " + wmr[0] + ", " + wmr[1]);
            
            System.out.println(amrBar[0] + ", " + amrBar[1]);
            // ArrayList<Float> headList = fb13_boxe_ranks_old.get(p).get(0);
            // ArrayList<Float> tailList = fb13_boxe_ranks_old.get(p).get(1);
            // printFloatArr(fiveNumberSummary(headList), "5 Number Summary - Head");
            // printFloatArr(fiveNumberSummary(tailList), "5 Number Summary - Tail");
            // System.out.println();
        }
        

        // HashMap<Integer, ArrayList<ArrayList<Float>>> fb13_boxe_ranks = oldParseRanks( path + "boxe__0_ranks.tsv");

        // System.out.println("Analyzing prediction on FB13 using BoxE:");

        // for (int p : fb13_boxe_ranks.keySet()){
        //     System.out.println("p = " + p);
        //     ArrayList<Float> headList = fb13_boxe_ranks.get(p).get(0);
        //     ArrayList<Float> tailList = fb13_boxe_ranks.get(p).get(1);
        //     System.out.println("Size of p: " + headList.size());
            
        //     System.out.println("HEAD");
        //     allAnalysis(headList);
        //     System.out.println("AMR: " + arithmaticMeanRank(headList));
        //     System.out.println("GMR: " + geometricMeanRank(headList));
        //     printFloatArr(fiveNumberSummary(headList), "5 Number Summary - Head");
        //     System.out.println("TAIL");
        //     allAnalysis(tailList);
        //     System.out.println("AMR: " + arithmaticMeanRank(tailList));
        //     System.out.println("GMR: " + geometricMeanRank(tailList));
        //     printFloatArr(fiveNumberSummary(tailList), "5 Number Summary - Tail");
            
            // float[] means = meanRank(headList);
            // System.out.println("MR - Head: " + means[0] + "; MRR - Head: " + means[1]);
            // System.out.println("Hits at 10: " + hitsAtX(headList, 10));
            // means = meanRank(tailList);
            // System.out.println("MR - Tail: " + means[0] + "; MRR - Tail: " + means[1]);
            // System.out.println("Hits at 10: " + hitsAtX(tailList, 10));
        //}

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
