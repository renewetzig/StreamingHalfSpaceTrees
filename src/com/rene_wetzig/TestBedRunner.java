package com.rene_wetzig;

import com.rene_wetzig.thresholds.ExponentialMovingAverage;

public class TestBedRunner {



    public static void main(){

        String header = "threshold;nrOfTrees;maxDepth;windowSize;sizeLimit;nrOfDimensions;min;max;nrOfSamples;percentageOfAnomalies;randomiseTestSampleGenerator;anomalyDimensions;minStepSize;maxStepSize;Normals inserted;Normals Recognised; Normals Not Recognised;Anomalies inserted; Anomalies Recognised; Anomalies Not Recognised; Percentage Normal As Normal; Percentage Normal As Anomaly; Percentage Anomaly As Anomaly; Percentage Anomaly As Normal;";
        TestBed testBed1 = new TestBed(new ExponentialMovingAverage(250,0.1 , 0.7, false),
                25,15,250,25,
                30,0,1,10000,2,
                false,2,1000,10000,
                "D:\\testfolder", true);
        System.out.println(header);
        System.out.println(testBed1.getStats());

    }



}
