package com.rene_wetzig;

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
        int nrOfSamples = 25000;
        int percentageOfAnomalies = 2;
        boolean randomiseTestSampleGenerator = false;
        int anomalyDimensions = 1;
        int firstAnomalyDim = 0;
        int anomalyLength = 10;
        double minStepSize = 0.0001;
        double maxStepSize = 0.1;
        double minNormal = 0.15;
        double maxNormal = 0.85;
        boolean printEverything = false;


/*        for(int i = 0; i<28; i=i+2) {
            for (int j = 0; j < 10; j++) {
                TestBed testBed3 = new TestBed(new ExponentialStandardDeviation(windowSize, 0.1, 0.7),
                        nrOfTrees, maxDepth, windowSize, sizeLimit,
                        nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
                        randomiseTestSampleGenerator, anomalyDimensions, i, minStepSize, maxStepSize,
                        minNormal, maxNormal, testNumber, filePath + testNumber++, true);
                pw.println(testBed3.getStats());
            }
        }*/

        /*for(int i=1; i<=20; i++) {
            windowSize = i*25;
            sizeLimit = (int) (windowSize/10);
            System.out.println("WindowSize = "+windowSize+", sizeLimit = "+sizeLimit);
            for(int j=0;j<10;j++) {
                TestBed testBed3 = new TestBed(new StaticThreshold(windowSize,1),
                        nrOfTrees, maxDepth, windowSize, sizeLimit,
                        nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
                        randomiseTestSampleGenerator, anomalyDimensions, firstAnomalyDim, 1, minStepSize, maxStepSize,
                        minNormal, maxNormal, testNumber, filePath + testNumber++, true);
                pw.println(testBed3.getStats());
            }
        }*/

        TestBed testBed3 = new TestBed(new StaticThreshold(windowSize,1),
                nrOfTrees, maxDepth, windowSize, sizeLimit,
                nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
                randomiseTestSampleGenerator, anomalyDimensions, firstAnomalyDim, anomalyLength, minStepSize, maxStepSize,
                minNormal, maxNormal, testNumber, filePath + testNumber++, true);
        pw.println(testBed3.getStats());




        pw.flush();
        pw.close();
    }



}
