package com.rene_wetzig;

import com.rene_wetzig.thresholds.ExponentialStandardDeviation;
import com.rene_wetzig.thresholds.StaticThreshold;

import java.io.File;
import java.io.PrintWriter;

public class TestBedRunner {

    private static PrintWriter pw;
    private static StringBuilder sb;



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

        String header = "threshold;" +
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
        double minStepSize = 0.000001;
        double maxStepSize = 0.001;
        double minNormal = 0.3;
        double maxNormal = 0.7;
        boolean printEverything = false;
        boolean calculateSampleInsertionTime = false;

        for(int i = 1; i <= 10; i=i+2) {
            for (int j = 1; j <= 10; j++) {
                for (int k = 1; k <= 10; k++){
                    TestBed testBed = new TestBed(new ExponentialStandardDeviation(windowSize, i * 0.02, j * 0.2),
                            nrOfTrees, maxDepth, windowSize, sizeLimit,
                            nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
                            randomiseTestSampleGenerator, anomalyDimensions, firstAnomalyDim, anomalyLength, minStepSize, maxStepSize,
                            minNormal, maxNormal, testNumber, filePath + testNumber++, printEverything, calculateSampleInsertionTime);
                    pw.println(testBed.getStats());
                }
            }
        }

//        for (int k = 1; k <= 10; k++){
//            TestBed testBed = new TestBed(new ExponentialStandardDeviation(windowSize, 0.14, 1.4),
//                    nrOfTrees, maxDepth, windowSize, sizeLimit,
//                    nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
//                    randomiseTestSampleGenerator, anomalyDimensions, firstAnomalyDim, anomalyLength, minStepSize, maxStepSize,
//                    minNormal, maxNormal, testNumber, filePath + testNumber++, printEverything, calculateSampleInsertionTime);
//            pw.println(testBed.getStats());
//        }




        pw.flush();
        pw.close();
    }



}
