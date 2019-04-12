package com.rene_wetzig;

import com.rene_wetzig.externalClasses.Sample;
import com.rene_wetzig.thresholds.Threshold;
import com.rene_wetzig.thresholds.WeightedStandardDeviation;

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
    // sizeLimit, the minimum number of instances in the reference counter of a node, at which the anomalyScore is calculated.
    public static int sizeLimit = (int) Math.round(0.1 * windowSize);


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

        TestBedRunner.main();

       // runAsTest();


    }

    private static void runAsTest() {
        // print instance scores?
        boolean printScores = false;

        // set Test environment for the trees
        nrOfTrees = 25;
        maxDepth = 15;
        windowSize = 250;
        int nrOfSamples = 10000;
        int startInsertingAnomalies = 2*windowSize; // how many clean samples to send through before inserting anomalies

        // set Domain of the test environment
        int testDimensions = 30;
        double testMin = 0.0;
        double testMax = 1.0;

        // the percentage of anomalies to be inserted at random points.
        int percentageOfAnomalies = 2;

        // int anomalyDimensions = (int) (testDimensions*0.1);
        int anomalyDimensions = 2; // the number of dimensions affected by the anomaly

        // Minimum and Maximum drift step sizes (relative to entire size of domain in a given dimension)
        int minDriftStepSize = 50;
        int maxDriftStepSize = 10000;

        System.out.println("anomalydimensions = " + anomalyDimensions);

        // This is where the threshold method is chosen.
        // Threshold threshold = new staticThreshold(windowSize, 10000000);
        // Threshold threshold = new ExponentialMovingAverage(windowSize, 0.1, 0.7, false);
        // Threshold threshold = new StandardDeviation(windowSize, 1);
         Threshold threshold = new WeightedStandardDeviation(windowSize, 0.1, 1);

        boolean thresholdCreated; // has the averaged thresholed been created?
        int anomalyThreshold = 0; // threshold under which an anomalyScore is deemed an anomaly.

        anomalyThreshold = 100000000; // Fixed Threshold. Works surprisingly well.
        thresholdCreated = true;
        System.out.println("Starting Threshold = " + threshold.getCurrentThreshold());

        sizeLimit = (int) Math.round(0.1 * windowSize); // this is where the

        System.out.println("sizeLimit = " + sizeLimit);



        int randomiser = 100; // A random number between 1 and 100 that the percentageOfAnomalies is checked against.

        boolean insertAnomaly; // if this is true, an anomaly is inserted in this round.
        int thisSampleScore = 0; // saves the score of the most recent sample in order to create the averagedAnomalyThreshold

        Sample newSample; // the variable new Samples will be saved in.

        // Counters for statistics
        int anomalyCounter = 0; // counts the number of Anomalies inserted
        int anomaliesRecognised = 0; // counts the number of anomalies that were correctly recognised.
        int anomaliesNotRecognised = 0;
        int normalCounter = 0;
        int normalRecognised = 0;
        int normalAsAnomaly = 0; // counts the number of false positives (normal data recognised as anomalies)

        // allows us to calculate the average time it takes to insert and score a single sample.
        long sampleTimer = 0;


        // create workspace according to dimensions and limits given for this test.
        nrOfDimensions = testDimensions;
        min = new double[nrOfDimensions];
        max = new double[nrOfDimensions];

        for (int i = 0; i < nrOfDimensions; i++) {
            min[i] = testMin;
            max[i] = testMax;
        }

        System.out.println("Creating " + nrOfTrees + " Trees with a maximum Depth of " + maxDepth + " over " + nrOfDimensions + " Dimensions.");

        long treeBuildStart = System.currentTimeMillis();
        // this is where the family of Halfspace-Trees is created.
        family = new TreeOrchestrator(nrOfTrees, maxDepth, windowSize, nrOfDimensions, min, max, sizeLimit);
        long treeBuildEnd = System.currentTimeMillis();
        long treeBuildTime = treeBuildEnd - treeBuildStart;
        System.out.println("Done. Treebuilding took " + treeBuildTime + " Milliseconds.");

        TestSampleGenerator generator = new TestSampleGenerator(nrOfDimensions, min, max, anomalyDimensions, 0, minDriftStepSize, maxDriftStepSize, false, 0, 1);

        System.out.println("Inserting about " + nrOfSamples + " Samples.");

        // counts the number of samples that has been inserted.
        int counter = 0;
        long startDate = System.currentTimeMillis();


        // generating and inserting the samples
        while (counter < nrOfSamples) {
            insertAnomaly = false;
            if (counter >= startInsertingAnomalies) {
                randomiser = ThreadLocalRandom.current().nextInt(0, 101);
                // if our randomiser outputs a number greater than the percentageOfAnomalies, insert a normal Sample. Otherwise, insert an Anomaly.
                if (randomiser <= percentageOfAnomalies) insertAnomaly = true;
                if(counter == startInsertingAnomalies + 1 && printScores) System.out.println(
                        "\n----------------------- Starting Anomaly Insertion -----------------------\n");
            }

            if(insertAnomaly) {
                newSample = generator.getAnomaly();
            } else {
                newSample = generator.getNormalSampleWithDrift();
            }

            long timerStart = System.currentTimeMillis();

            // inserts the current Sample into the family of trees and records its score.
            thisSampleScore = family.insertSample(newSample);

            long timerStop = System.currentTimeMillis();
            long timeThisSample = timerStop - timerStart;
            sampleTimer += timeThisSample;

            /* Outputs the metrics of every single sample inserted. Useful for
            String output = "[ ";
            for(int i = 0; i < nrOfDimensions; i++){
                output = output + newSample.getMetrics()[i] + " ";
            }
            output = output + "  ]";
            System.out.println(output);
            */

            if(printScores) System.out.println("                            CurrentThreshold = " + threshold.getCurrentThreshold());

            if(insertAnomaly) {
                anomalyCounter++;
                if (!threshold.insertNewSample(thisSampleScore)) {
                    if(printScores) System.out.println("++++++++++AnomalyScore recognised = " + thisSampleScore);
                    anomaliesRecognised++;
                } else {
                   if(printScores) System.out.println("----------AnomalyScore NOT recognised = " + thisSampleScore);
                    anomaliesNotRecognised++;
                }
            } else {
                if (thresholdCreated && counter > startInsertingAnomalies ) normalCounter++;

                if (threshold.insertNewSample(thisSampleScore) && counter > startInsertingAnomalies) {
                    if (printScores) System.out.println("++++Normal Score recognised = " + thisSampleScore);
                    normalRecognised++;

                } else {
                    if (counter > startInsertingAnomalies) {
                        if (printScores) System.out.println("----Normal NOT recognised = " + thisSampleScore);
                        normalAsAnomaly++;
                    }
                }
            }

            counter++;
        }


        long endDate = System.currentTimeMillis();
        long seconds = (endDate - startDate)/1000;

        System.out.println("Done. \nInserting " + counter + " Samples took " + seconds + " Seconds.");

        double timePerSample = (double) sampleTimer / (double) (counter - anomalyCounter);
        System.out.println("Time per Sample Insertion = " + timePerSample + "ms");


        double percentageNormalRecognised = (double) Math.round(((double) normalRecognised / normalCounter)*1000)/10;
        double percentageNormalNotRecognised = (double) Math.round(((double) normalAsAnomaly / normalCounter)*1000)/10;
        double percentageAnomaliesRecognised = (double) Math.round(((double) anomaliesRecognised / anomalyCounter)*1000)/10;
        double percentageAnomaliesNotRecognised = (double) Math.round(((double) anomaliesNotRecognised / anomalyCounter)*1000)/10;

        System.out.println(
                "Final Threshold = " + threshold.getCurrentThreshold() + "\n\n" +
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
