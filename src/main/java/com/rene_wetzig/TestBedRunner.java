package com.rene_wetzig;

import com.rene_wetzig.thresholds.*;

import java.io.File;
import java.io.PrintWriter;

public class TestBedRunner {

    private static PrintWriter pw;
    private static StringBuilder sb;
    private static long startTime;




    public static void main(){

        boolean windows = true;


        String topLevelPath;
        String slash;

        if(windows) {
            // Windows Version
            topLevelPath = "D:\\Dropbox\\Studium\\BACHELORARBEIT\\TestErgebnisse";
            slash = "\\";
        } else {
            topLevelPath = "/Users/renewetzig/Dropbox/Studium/BACHELORARBEIT/TestErgebnisse";
            slash = "/";
        }

        String folder = "Testrun";
        int runNumber = 0;
        String.format("%03d", runNumber);
        File f = new File(topLevelPath + slash + folder + String.format("%03d", runNumber));
        while(f.exists() && f.isDirectory()) {
            runNumber++;
            f = new File(topLevelPath + slash + folder + String.format("%03d", runNumber));
        }

        String filePath =  topLevelPath + slash + folder + String.format("%03d", runNumber) + slash;

        f.mkdirs();

        try {
            pw = new PrintWriter(new File(filePath, "000 Results Testrun" + String.format("%03d", runNumber) + ".csv"));
        } catch(Exception e){
            System.out.println("TestBedRunner Printwriter Exception: " + e.toString());
        }
        sb = new StringBuilder();

        String header = "TestNr;"+
                "threshold;" +
                "Normals with AS = 0;" +
                "Percentage Normal As Normal;" +
                "Percentage Normal As Anomaly;" +
                "Percentage Anomaly As Anomaly;" +
                "Percentage Anomaly As Normal;"+
                "Normals inserted;" +
                "Normals Recognised;" +
                "Normals Not Recognised;" +
                "Anomalies inserted;" +
                "Anomalies Recognised;" +
                "Anomalies Not Recognised;" +
                "Average Time of Sample Insertion;" +
                "nrOfTrees;maxDepth;windowSize;sizeLimit;nrOfDimensions;min;max;nrOfSamples;" +
                "percentageOfAnomalies;randomiseTestSampleGenerator;nrOfAnomalyDimensions;firstAnomalyDimension;anomalyLength;minStepSize;maxStepSize;minNormal;maxNormal;";
        pw.println(header);

        int testNumber = 0;

        int windowSize = 250;
        int sizeLimit = (int) (windowSize/10);
        int nrOfTrees = 25;
        int maxDepth = 15;
        int nrOfDimensions = 30;
        double min = 0;
        double max = 1;
        int nrOfSamples = 50000;
        int percentageOfAnomalies = 2;
        boolean randomiseTestSampleGenerator = false;
        int anomalyDimensions = 2;
        int firstAnomalyDim = 15;
        int anomalyLength = 1;
        double minStepSize = 0.0000000001;
        double maxStepSize = 0.01;
        double minNormal = 0.3;
        double maxNormal = 0.7;
        boolean printEverything = false;
        boolean calculateSampleInsertionTime = true;

        startTime = System.currentTimeMillis();

        TestBed testBed;


//        for(int i = 0; i <= 10; i=i+2) {
//            for (int j = 0; j <= 10; j=j+2) {
//                for(int k = 0; k <10; k++) {
//                    testBed = new TestBed(new ExponentialStandardDeviation(windowSize, 0.02 * (i+1), 0.2 * (j+1)),
//                        nrOfTrees, maxDepth, windowSize, sizeLimit,
//                        nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
//                        randomiseTestSampleGenerator, anomalyDimensions, firstAnomalyDim, anomalyLength, minStepSize, maxStepSize,
//                        minNormal, maxNormal, testNumber, filePath + testNumber, printEverything, calculateSampleInsertionTime);
//                    pw.println(testNumber++ + ";" + testBed.getStats());
//                    System.out.println("Time Elapsed: " + getTimeElapsed() + "minutes");
//                }
//
//            }
//
//        }

                for(int k = 0; k <10; k++) {
                    testBed = new TestBed(new StaticThreshold(windowSize,2000000),
                        nrOfTrees, maxDepth, windowSize, sizeLimit,
                        nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
                        randomiseTestSampleGenerator, anomalyDimensions, firstAnomalyDim, anomalyLength, minStepSize, maxStepSize,
                        minNormal, maxNormal, testNumber, filePath + testNumber, printEverything, calculateSampleInsertionTime);
                    pw.println(testNumber++ + ";" + testBed.getStats());
                    System.out.println("Time Elapsed: " + getTimeElapsed() + "minutes");
                }


        pw.flush();
        pw.close();
    }

private static double getTimeElapsed(){
        return ( (long) ( (System.currentTimeMillis() - startTime) / 6000.0))/10.0;
}

}
