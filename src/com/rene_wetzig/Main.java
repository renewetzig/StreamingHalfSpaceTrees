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
            runNormal();
        }


    }

    private static void runAsTest() {
        // set Test environment for the trees
        nrOfTrees = 25;
        maxDepth = 15;
        int nrOfSamples = 3000;
        int startInsertingAnomalies = 3 * windowSize; // how many clean samples to send through before inserting anomalies

        // set Domain of the test environment
        int testDimensions = 10;
        double testMin = -10;
        double testMax = 10;

        int percentageOfAnomalies = 15;

        int randomiser = 100; // A random number between 1 and 100 that the percentageOfAnomalies is checked against.

        double anomalyThreshold; // threshold under which an anomalyScore is deemed an anomaly.
        double averagedAnomalyThreshold = 0;
        int divisor = 0;
        boolean averageCreated = false; // has the averaged thresholed been created?

        int anomalyCounter = 0; // counts the number of Anomalies inserted
        int anomaliesFound = 0; // counts the number of anomalies that were correctly recognised.
        int falsePositivesFound = 0; // counts the number of false positives (normal data recognised as anomalies)


        nrOfDimensions = testDimensions;
        min = new double[nrOfDimensions];
        max = new double[nrOfDimensions];

        for (int i = 0; i < nrOfDimensions; i++) {
            min[i] = testMin;
            max[i] = testMax;
        }

        // AnomalyThreshold Method B Calculate Anomaly Threshold as a function of a normal distribution over all leaves
        // of a Tree. Threshold = Windowsize/#Leaves
        // NOTE: I don't think this is good.
        anomalyThreshold = windowSize / Math.pow(2, maxDepth);


        System.out.println(nrOfTrees + " Trees with a maximum Depth of " + maxDepth + " over " + nrOfDimensions + " Dimensions.");
        System.out.println("Inserting about " + nrOfSamples + " Samples.");

        family = new TreeOrchestrator(nrOfTrees, maxDepth, windowSize, nrOfDimensions, min, max);

        TestSampleGenerator generator = new TestSampleGenerator(nrOfDimensions, min, max);

        int counter = 0;
        Date startDate = new Date();

        while (counter < nrOfSamples) {

            if (counter >= startInsertingAnomalies) {
                randomiser = ThreadLocalRandom.current().nextInt(0, 101);
            }
            if (randomiser > percentageOfAnomalies) {
                if (family.insertSample(generator.getNormalSample()) <= anomalyThreshold && counter > windowSize) {
                    falsePositivesFound++;
                }
            } else {
                anomalyCounter++;
                if (family.insertSample(generator.getAnomaly()) <= anomalyThreshold) {
                    anomaliesFound++;
                }
            }
            counter++;

            /* AnomalyThreshold Method A (BAD): for calculating AnomalyThreshold - goes by average of normal points
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

        }

        Date endDate = new Date();
        int minutes = endDate.getMinutes() - startDate.getMinutes();
        int seconds = endDate.getSeconds() - startDate.getSeconds();
        if (seconds < 0) {
            minutes--;
            seconds = 60 + seconds;
        }

        System.out.println("Done. \nInserting " + counter + " Samples took " + minutes + " Minutes and " + seconds + " Seconds.");
        System.out.println("Of the " + anomalyCounter + " anomalies inserted, " + anomaliesFound + " were recognised correctly. There were " + falsePositivesFound + " false positives.");
        // System.out.println(family.toString());

    }

    private static void runNormal() {
        // SET UP FOR SPECIFIC USECASE. This needs to be edited when switching to another usecase.

        // TODO insert maxs, mins and number of Dimensions from the instance here.

        family = new TreeOrchestrator(nrOfTrees, maxDepth, windowSize, nrOfDimensions, min, max);

    }


    public double insertSample(Sample newSample) {
        double anomalyScore;
        anomalyScore = 0;
        family.insertSample(newSample);

        return anomalyScore;

    }
}
