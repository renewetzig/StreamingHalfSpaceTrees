package com.rene_wetzig;

import com.rene_wetzig.thresholds.Arima;

import java.io.File;
import java.io.PrintWriter;

public class TestBedRunner {

    private static PrintWriter pw;
    private static StringBuilder sb;



    public static void main(){

        boolean windows = false;


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
                "Percentage Normal As Normal;" +
                "Percentage Normal As Anomaly;" +
                "Percentage Anomaly As Anomaly;" +
                "Percentage Anomaly As Normal;"+
                "Normals inserted;" +
                "Normals Recognised;" +
                "Normals Not Recognised;" +
                "Anomalies inserted;" +
                "Anomalies Recognised;" +
                "Anomalies Not Recognised;" +"nrOfTrees;maxDepth;windowSize;sizeLimit;nrOfDimensions;min;max;nrOfSamples;" +
                "percentageOfAnomalies;randomiseTestSampleGenerator;anomalyDimensions;minStepSize;maxStepSize;";
        pw.println(header);

        int testNumber = 0;

        int windowSize = 100;
        int nrOfTrees = 50;
        int maxDepth = 10;
        int sizeLimit = 25;
        int nrOfDimensions = 30;
        double min = 0;
        double max = 1;
        int nrOfSamples = 100000;
        int percentageOfAnomalies = 2;
        boolean randomiseTestSampleGenerator = false;
        int anomalyDimensions = 2;
        int minStepSize = 100;
        int maxStepSize = 1000;
        boolean printEverything = false;


/*        for (int i=0;i<10;i++){
            TestBed testBed3 = new TestBed(new ExponentialMovingAverage(windowSize, 0.1, 0.7, false ),
                    nrOfTrees, maxDepth, windowSize, sizeLimit,
                    nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
                    randomiseTestSampleGenerator, anomalyDimensions, minStepSize, maxStepSize,
                    testNumber, filePath + testNumber++, printEverything);
            pw.println(testBed3.getStats());
        }

        for (int i=0;i<10;i++){
            TestBed testBed3 = new TestBed(new ExponentialMovingAverage(windowSize, 0.1, 0.7, true ),
                    nrOfTrees, maxDepth, windowSize, sizeLimit,
                    nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
                    randomiseTestSampleGenerator, anomalyDimensions, minStepSize, maxStepSize,
                    testNumber, filePath + testNumber++, printEverything);
            pw.println(testBed3.getStats());
        }

        for (int i=0;i<10;i++){
            TestBed testBed3 = new TestBed(new StandardDeviation(windowSize, 0.7),
                    nrOfTrees, maxDepth, windowSize, sizeLimit,
                    nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
                    randomiseTestSampleGenerator, anomalyDimensions, minStepSize, maxStepSize,
                    testNumber, filePath + testNumber++, printEverything);
            pw.println(testBed3.getStats());
        }

        for (int i=0;i<10;i++){
            TestBed testBed3 = new TestBed(new WeightedStandardDeviation(windowSize, 0.1, 0.7),
                    nrOfTrees, maxDepth, windowSize, sizeLimit,
                    nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
                    randomiseTestSampleGenerator, anomalyDimensions, minStepSize, maxStepSize,
                    testNumber, filePath + testNumber++, printEverything);
            pw.println(testBed3.getStats());
        }

        for (int i=0;i<10;i++){
            TestBed testBed3 = new TestBed(new ExponentialStandardDeviation(windowSize, 0.1, 0.7),
                    nrOfTrees, maxDepth, windowSize, sizeLimit,
                    nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
                    randomiseTestSampleGenerator, anomalyDimensions, minStepSize, maxStepSize,
                    testNumber, filePath + testNumber++, printEverything);
            pw.println(testBed3.getStats());
        }*/

/*        for(int i=0; i<20;i++) {
            for (int j = 0; j < 20; j++) {
                for(int k = 0; k<10; k++) {
                    TestBed testBed3 = new TestBed(new ExponentialMovingAverage(windowSize, i*0.01, j*0.05, false),
                            nrOfTrees, maxDepth, windowSize, sizeLimit,
                            nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
                            randomiseTestSampleGenerator, anomalyDimensions, minStepSize, maxStepSize,
                            testNumber, filePath + testNumber++, printEverything);
                    pw.println(testBed3.getStats());
                }
            }
        }

        for (int i=0;i<20;i++){
            for(int j = 0; j<20; j++) {
                for(int k = 0; k<10; k++) {
                    TestBed testBed = new TestBed(new ExponentialStandardDeviation(windowSize, i * 0.01, 0.2 + j * 0.1),
                            nrOfTrees, maxDepth, windowSize, sizeLimit,
                            nrOfDimensions, min, max, nrOfSamples, percentageOfAnomalies,
                            randomiseTestSampleGenerator, anomalyDimensions, minStepSize, maxStepSize,
                            testNumber, filePath + testNumber++, printEverything);
                    pw.println(testBed.getStats());
                }
            }
        }*/


        TestBed testBed3 = new TestBed(new Arima(windowSize, 5 , 3, 0.3),
                nrOfTrees, maxDepth, windowSize, sizeLimit,
                nrOfDimensions, min, max, 10000, percentageOfAnomalies,
                randomiseTestSampleGenerator, anomalyDimensions, minStepSize, maxStepSize,
                testNumber, filePath + testNumber++, true);
        pw.println(testBed3.getStats());


        pw.flush();
        pw.close();
    }



}
