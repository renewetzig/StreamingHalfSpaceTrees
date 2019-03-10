package com.rene_wetzig;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    /*
     + OVERALL PARAMETERS
     * Basic parameters that control the performance of the trees.
     * To be set (somewhat) independently of the problem set at hand.
     *
     * PLEASE NOTE: Tree depth is defined as the length of a path from the root to a leaf. ROOT HAS DEPTH 0 !!!
     */
    private static int maxDepth = 20; //max. depth of any tree in the family
    private static int nrOfTrees = 25; //Number of trees to be created
    public static int windowSize = 250; //Number of instances per window
    public static int sizeLimit = (int) Math.round(0.1 * windowSize); // sizeLimit, the minimum number of instances in the reference counter of a node, at which the anomalyScore is calculated.


    /*
     * GENERAL VARIABLES
     */
    private static int nrOfDimensions; //number of dimensions to be watched for the set at hand NOTE:
    private static double[] min; //minimum value expected for each dimension
    private static double[] max; //maximum value expected for each dimension
    private static TreeOrchestrator family; // the TreeOrchestrator class is where a family of trees is created and controlled

    /*
     * PROBLEM SET SPECIFIC PARAMETERS
     */
    private static int probDimensions;
    private static double[] probMin = {-10, -10};
    private static double[] probMax = {10, 10};


    public static void main(String[] args) {
        // write your code here
        boolean runTest = true;

        if (runTest) {
            runAsTest();
        } else {
            nrOfDimensions = probDimensions;
            min = probMin;
            max = probMax;
            // runNormal();
        }


    }

    private static void runAsTest() {
        // set Test environment for the trees
        nrOfTrees = 25;
        maxDepth = 15;
        windowSize = 250;
        int nrOfSamples = 100000;
        int startInsertingAnomalies = 3 * windowSize; // how many clean samples to send through before inserting anomalies

        sizeLimit = (int) Math.round(0.1 * windowSize); // this is where the

        System.out.println("sizeLimit = " + sizeLimit);

        // set Domain of the test environment
        int testDimensions = 50;
        double testMin = 0;
        double testMax = 1;

        int percentageOfAnomalies = 5;

        int randomiser = 100; // A random number between 1 and 100 that the percentageOfAnomalies is checked against.

        int anomalyThreshold = 0; // threshold under which an anomalyScore is deemed an anomaly.
        int thisSampleScore = 0; // saves the score of the most recent sample in order to create the averagedAnomalyThreshold
        int minScoreNormal = nrOfTrees * windowSize; // the minimal score of a normal Sample during the second window
        int averagedAnomalyThreshold = 0;
        int divisor = 0;
        boolean thresholdCreated = false; // has the averaged thresholed been created?

        int anomalyCounter = 0; // counts the number of Anomalies inserted
        int anomaliesRecognised = 0; // counts the number of anomalies that were correctly recognised.
        int anomaliesNotRecognised = 0;
        int normalCounter = 0;
        int normalRecognised = 0;
        int normalAsAnomaly = 0; // counts the number of false positives (normal data recognised as anomalies)


        nrOfDimensions = testDimensions;
        min = new double[nrOfDimensions];
        max = new double[nrOfDimensions];

        for (int i = 0; i < nrOfDimensions; i++) {
            min[i] = testMin;
            max[i] = testMax;
        }

        /*
        // AnomalyThreshold Method B Calculate Anomaly Threshold as a function of a normal distribution over all leaves
        // of a Tree. Threshold = Windowsize/#Leaves
        // NOTE: I don't think this is good.
        anomalyThreshold = windowSize / Math.pow(2, maxDepth);
        thresholdCreated = true;
        System.out.println("WindowSize = " + windowSize + ", numberOfLeaves = " + Math.pow(2,maxDepth));
        System.out.println(anomalyThreshold);
        */


        System.out.println(nrOfTrees + " Trees with a maximum Depth of " + maxDepth + " over " + nrOfDimensions + " Dimensions.");
        System.out.println("Inserting about " + nrOfSamples + " Samples.");

        family = new TreeOrchestrator(nrOfTrees, maxDepth, windowSize, nrOfDimensions, min, max, sizeLimit);

        TestSampleGenerator generator = new TestSampleGenerator(nrOfDimensions, min, max);

        int counter = 0;
        Date startDate = new Date();

        while (counter < nrOfSamples) {

            if (counter >= startInsertingAnomalies) {
                randomiser = ThreadLocalRandom.current().nextInt(0, 101);
            }
            if(counter == startInsertingAnomalies) System.out.println("\n Starting Anomaly Insertion \n");

            // if our randomiser outputs a number greater than the percentageOfAnomalies, insert a normal Sample. Otherwise, insert an Anomaly.
            if (randomiser >= percentageOfAnomalies) {
                Sample newSample = generator.getNormalSampleWithDrift();

/*                String output = "[ ";

                for(int i = 0; i < nrOfDimensions; i++){
                    output = output + newSample.getMetrics()[i] + " ";
                }

                output = output + "  ]";

                System.out.println(output);*/


                thisSampleScore = family.insertSample(newSample);
                if(thresholdCreated) normalCounter++;
                System.out.println("Normal SampleScore = " + thisSampleScore);
                if (thisSampleScore <= anomalyThreshold && thresholdCreated) {
                    normalAsAnomaly++;
                } else { if(thresholdCreated) normalRecognised++; }
            } else {
                anomalyCounter++;
                int anomalySampleScore = family.insertSample(generator.getAnomaly());
                System.out.println("------Anomaly SampleScore = " + anomalySampleScore);
                if (anomalySampleScore <= anomalyThreshold) {
                    anomaliesRecognised++;
                } else { anomaliesNotRecognised++; }
            }


            /*
            // AnomalyThreshold Method A (BAD): for calculating AnomalyThreshold - goes by average of normal points
            // calculates an averaged anomaly Threshold over second window.
            // IMPORTANT: This assumes the second window is clean.
            if(counter > windowSize && divisor <= windowSize){
                averagedAnomalyThreshold = averagedAnomalyThreshold + thisSampleScore;
                divisor++;
            }
            if(divisor == windowSize && !averageCreated) {
                anomalyThreshold = (averagedAnomalyThreshold / divisor) / 4;
                averageCreated = true;
            }
            */

            // AnomalyThreshold Method C (BAD): Minimum value of a normal Sample minus distance to average.
            // calculates an averaged anomaly Threshold over second window.
            // IMPORTANT: This assumes the second window is clean.
            if(counter > windowSize && counter <= 2*windowSize){
                if(thisSampleScore < minScoreNormal) minScoreNormal = thisSampleScore;
                averagedAnomalyThreshold = averagedAnomalyThreshold + thisSampleScore;
                divisor++;
            }
            if(divisor == windowSize && !thresholdCreated) {
                System.out.println("minScoreNormal " + minScoreNormal);
                averagedAnomalyThreshold = (averagedAnomalyThreshold / divisor);
                System.out.println("averagedAnomalyThreshold " + averagedAnomalyThreshold);
                anomalyThreshold = minScoreNormal - (averagedAnomalyThreshold - minScoreNormal);
                System.out.println("anomalyThreshold = " + anomalyThreshold);
                thresholdCreated = true;
                anomalyThreshold = 1500000; // Fixed Threshold. Works surprisingly well.
            }

            counter++;
        }

        Date endDate = new Date();
        int minutes = endDate.getMinutes() - startDate.getMinutes();
        int seconds = endDate.getSeconds() - startDate.getSeconds();
        if (seconds < 0) {
            minutes--;
            seconds = 60 + seconds;
        }

        System.out.println("Done. \nInserting " + counter + " Samples took " + minutes + " Minutes and " + seconds + " Seconds.");

        double percentageNormalRecognised = (double) Math.round(((double) normalRecognised / normalCounter)*1000)/10;
        double percentageNormalNotRecognised = (double) Math.round(((double) normalAsAnomaly / normalCounter)*1000)/10;
        double percentageAnomaliesRecognised = (double) Math.round(((double) anomaliesRecognised / anomalyCounter)*1000)/10;
        double percentageAnomaliesNotRecognised = (double) Math.round(((double) anomaliesNotRecognised / anomalyCounter)*1000)/10;

        System.out.println(
                "\n\naveragedAnomalyThreshold " + averagedAnomalyThreshold + "\n" +
                "anomalyThreshold = " + anomalyThreshold + "\n\n" +
                "Stats: \n" +
                        "Normal Samples inserted: " + normalCounter + "\n" +
                        "Normal recognised as Normal: " + normalRecognised + "\n" +
                        "Normal falsely recognised as Anomaly: " + normalAsAnomaly + "\n" +
                        "Anomalies inserted: " + anomalyCounter + "\n" +
                        "Anomalies recognised correctly: " + anomaliesRecognised + "\n" +
                        "Anomalies not recognised: " + anomaliesNotRecognised + "\n" +
                        "\n\n" +
                        "Percentage of Normals properly recognised: " + percentageNormalRecognised + "%" + "\n" +
                        "Percentage of Normals not recognised: " + percentageNormalNotRecognised + "%" + "\n" +
                        "Percentage of Anomalies properly recognised: " + percentageAnomaliesRecognised + "%" + "\n" +
                        "Percentage of Anomalies not recognised: " + percentageAnomaliesNotRecognised + "%"
        );
        // System.out.println(family.toString());

    }

/*    private static void runNormal() {
        // SET UP FOR SPECIFIC USECASE. This needs to be edited when switching to another usecase.

        // TODO insert maxs, mins and number of Dimensions from the instance here.

        family = new TreeOrchestrator(nrOfTrees, maxDepth, windowSize, nrOfDimensions, min, max, sizeLimit);

    }*/


    public double insertSample(Sample newSample) {
        double anomalyScore;
        anomalyScore = 0;
        family.insertSample(newSample);

        return anomalyScore;

    }
}
