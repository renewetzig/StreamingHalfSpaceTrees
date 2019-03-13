package com.rene_wetzig;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class TestSampleGenerator {

private int nrOfDimensions;
private double[] min;
private double[] max;
private double[] driftStepSize;
private double[] latestMetrics;
private boolean[] direction; // positive if true, negative if false
private int sampleCounter = 0;

/*
This class generates Sample class objects for test purposes.

Note: There are different versions of normal samples available.
IMPORTANT: Anomalies are always at (1, 0, 0, .... 0, 0) of any given domain. Avoid this area for normal Sample creation.
 */

    public TestSampleGenerator(int nrOfDimensions, double[] min, double[] max) {
        this.nrOfDimensions = nrOfDimensions;
        this.min = min.clone();
        this.max = max.clone();

        driftStepSize = new double[nrOfDimensions];
        latestMetrics = new double[nrOfDimensions];
        direction = new boolean[nrOfDimensions];


        for(int i = 0; i<nrOfDimensions;i++){
            latestMetrics[i]=min[i];
            direction[i] = true;
            // System.out.println("Min/Max["+i+"]: " + min[i] + " / " + max[i]);
        }

        // randomise the size of a step in any given direction of the drift
        for (int i = 0; i < nrOfDimensions; i++){
            driftStepSize[i] = (max[i]-min[i])/ (double) ThreadLocalRandom.current().nextInt(1000, 10000);
            // System.out.println("driftStepSize["+i+"]: " + driftStepSize[i]);
        }


    }

    public Sample getNormalSample(){
        Sample newSample;
        Header newHeader = new Header(new String[0]);
        Date newDate = new Date();

        double[] newMetrics = latestMetrics.clone();

        newSample = new Sample(newHeader, newMetrics, newDate);
        
        sampleCounter++;
        return newSample;
    }


    //TODO introduce drift. Idea: Counter that knows how many samples have been introduced
    public Sample getNormalSampleWithDrift(){
        Sample newSample;
        Header newHeader = new Header(new String[0]);
        Date newDate = new Date();

        double[] newMetrics = new double[nrOfDimensions];

        for (int i = 0; i < nrOfDimensions; i++) {
            if(latestMetrics[i] + driftStepSize[i] > max[i]) {
                direction[i] = false;
            } else if(latestMetrics[i] - driftStepSize[i] < min[i]) {
                direction[i] = true;
            }
            if(i == 0 && latestMetrics[i] + driftStepSize[i] > max[i]*0.8){ // Don't get too close to the anomaly point [1, 0, 0, ...]
                direction[0] = false;
            }

            if (direction[i]){
                    newMetrics[i] = latestMetrics[i] + driftStepSize[i];
            } else {
                newMetrics[i] = latestMetrics[i] - driftStepSize[i];
            }
        }


        newSample = new Sample(newHeader, newMetrics, newDate);
        latestMetrics = newMetrics.clone();

        sampleCounter++;
        return newSample;
    }

    // returns a Sample at the exact center of the domain. Avoid this area for normal values.
    public Sample getAnomaly(){
        Sample newSample;
        Header newHeader = new Header(new String[0]);
        Date newDate = new Date();

        double[] newMetrics = new double[nrOfDimensions];

        newMetrics[0]=1;

        for(int i=1; i < nrOfDimensions; i++) {
            newMetrics[i] = 0;
        }

        newSample = new Sample(newHeader, newMetrics, newDate);

        return newSample;
    }

}
