package com.rene_wetzig;

import com.rene_wetzig.thresholds.ExponentialMovingAverage;
import com.rene_wetzig.thresholds.weightedStandardDeviation;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestBedRunner {

    private static PrintWriter pw;
    private static StringBuilder sb;



    public static void main(){

        String topLevelPath =
                "D:\\Dropbox\\Studium\\BACHELORARBEIT\\TestErgebnisse"  // Windows Version
                // ""   // Mac Version
                ;

        String folder = "Testrun";
        int runNumber = 0;
        String.format("%03d", runNumber);
        File f = new File(topLevelPath + "\\" + folder + String.format("%03d", runNumber));
        while(f.exists() && f.isDirectory()) {
            runNumber++;
            f = new File(topLevelPath + "\\" + folder + String.format("%03d", runNumber));
        }

        String filePath =  topLevelPath + "\\" + folder + String.format("%03d", runNumber) + "\\";

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


/*        TestBed testBed1 = new TestBed(new ExponentialMovingAverage(250,0.1 , 0.7, false),
                25,15,250,25,
                30,0,1,100000,2,
                false,2,1000,10000,
                filePath+testNumber++, true);
        pw.println(testBed1.getStats());

        TestBed testBed2 = new TestBed(new ExponentialMovingAverage(250,0.1 , 0.7, false),
                25,15,250,25,
                30,0,1,100000,2,
                false,2,100,1000,
                filePath+testNumber++, true);
        pw.println(testBed2.getStats());*/

        for (int i=0;i<5;i++){
            TestBed testBed3 = new TestBed(new weightedStandardDeviation(250, 0.1, 0.7, true),
                    25, 15, 250, 25,
                    30, 0, 1, 100000, 2,
                    false, 2, 100, 1000,
                    filePath + testNumber++, true);
            pw.println(testBed3.getStats());
        }


        pw.flush();
        pw.close();
    }



}
