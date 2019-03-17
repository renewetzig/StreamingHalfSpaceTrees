package com.rene_wetzig;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;


/*
This class generates Sample class objects for test purposes.

Note: There are different versions of normal samples available.
IMPORTANT: Anomalies are always at (1, 0, 0, .... 0, 0) of any given domain. Avoid this area for normal Sample creation.
 */
public class TestSampleGenerator {

    private int nrOfDimensions;
    private double[] min;
    private double[] max;
    private double[] driftStepSize;
    private double[] latestMetrics;
    private boolean[] direction; // positive if true, negative if false
    private int anomalyDimensions;
    private int sampleCounter = 0;


    public TestSampleGenerator(int nrOfDimensions, double[] min, double[] max, int anomalyDimensions) {
        this.nrOfDimensions = nrOfDimensions;
        this.min = min.clone();
        this.max = max.clone();
        this.anomalyDimensions = anomalyDimensions;

        driftStepSize = new double[nrOfDimensions];
        latestMetrics = new double[nrOfDimensions];
        direction = new boolean[nrOfDimensions];


        for (int i = 0; i < nrOfDimensions; i++) {
            latestMetrics[i] = min[i];
            direction[i] = true;
            // System.out.println("Min/Max["+i+"]: " + min[i] + " / " + max[i]);
        }

        // randomise the size of a step in any given direction of the drift
        for (int i = 0; i < nrOfDimensions; i++) {
            driftStepSize[i] = (max[i] - min[i]) / (double) ThreadLocalRandom.current().nextInt(100, 10000);
            // System.out.println("driftStepSize["+i+"]: " + driftStepSize[i]);
        }


    }

    public Sample getNormalSample() {
        Sample newSample;
        Header newHeader = new Header(new String[0]);
        Date newDate = new Date();

        double[] newMetrics = latestMetrics.clone();

        newSample = new Sample(newHeader, newMetrics, newDate);

        sampleCounter++;
        return newSample;
    }


    //TODO introduce drift. Idea: Counter that knows how many samples have been introduced
    public Sample getNormalSampleWithDrift() {
        Sample newSample;
        Header newHeader = new Header(new String[0]);
        Date newDate = new Date();

        double[] newMetrics = new double[nrOfDimensions];

        for (int i = 0; i < nrOfDimensions; i++) {
            if (latestMetrics[i] + driftStepSize[i] > max[i]) {
                direction[i] = false;
            } else if (latestMetrics[i] - driftStepSize[i] < min[i]) {
                direction[i] = true;
            }
            if (i < anomalyDimensions && latestMetrics[i] + driftStepSize[i] - min[i] > (max[i] - min[i]) * 0.5) { // Don't get too close to the anomaly point [1, 0, 0, ...]
                direction[i] = false;
            }

            if (direction[i]) {
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
    public Sample getAnomaly() {
        Sample newSample;
        Header newHeader = new Header(new String[0]);
        Date newDate = new Date();

        double[] newMetrics = new double[nrOfDimensions];

    for(int i = 0; i < anomalyDimensions; i++){
        newMetrics[i] = max[i];
    }


        for (int i = anomalyDimensions; i < nrOfDimensions; i++) {
            if (latestMetrics[i] + driftStepSize[i] > max[i]) {
                direction[i] = false;
            } else if (latestMetrics[i] - driftStepSize[i] < min[i]) {
                direction[i] = true;
            }

            if (direction[i]) {
                newMetrics[i] = latestMetrics[i] + driftStepSize[i];
            } else {
                newMetrics[i] = latestMetrics[i] - driftStepSize[i];
            }
            latestMetrics[i] = newMetrics[i];
        }

        newSample = new Sample(newHeader, newMetrics, newDate);

        return newSample;
    }

}
