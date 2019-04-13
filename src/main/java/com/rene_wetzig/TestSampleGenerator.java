package com.rene_wetzig;

import com.rene_wetzig.externalClasses.Header;
import com.rene_wetzig.externalClasses.Sample;

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
    private int nrOfAnomalyDims;
    private int sampleCounter = 0;
    private int[] anomalyDims; //dimensions where anomalies are inserted.
    private double minNormal;
    private double maxNormal;


    public TestSampleGenerator(int nrOfDimensions, double[] min, double[] max, int nrOfAnomalyDims, int firstAnomalyDim,
                               double minStepSize, double maxStepSize, // the minimum and maximum step size for concept drift as fractions of the domain.
                               boolean randomiseStepSizes,
                               double minNormal, // minimum and maximum of the working domain that Normal values should live in.
                               double maxNormal  // minNormal = 0 and maxNormal = 1 are the whole domain.
    ) {
        this.nrOfDimensions = nrOfDimensions;
        this.min = min.clone();
        this.max = max.clone();
        this.nrOfAnomalyDims = nrOfAnomalyDims;
        this.minNormal = minNormal;
        this.maxNormal = maxNormal;

        driftStepSize = new double[nrOfDimensions];
        latestMetrics = new double[nrOfDimensions];
        direction = new boolean[nrOfDimensions];

        anomalyDims = new int[nrOfAnomalyDims];

        for (int i = 0; i < nrOfAnomalyDims; i++){
            anomalyDims[i] = firstAnomalyDim++;
        }


        for (int i = 0; i < nrOfDimensions; i++) {
            latestMetrics[i] = min[i] + minNormal * (max[i]-min[i]);
            direction[i] = true;
            // System.out.println("Min/Max["+i+"]: " + min[i] + " / " + max[i]);
        }

        if(randomiseStepSizes) {
            // randomise the size of a step in any given direction of the drift
            for (int i = 0; i < nrOfDimensions; i++) {
                driftStepSize[i] = (max[i] - min[i]) * ThreadLocalRandom.current().nextDouble(minStepSize, maxStepSize);
                // System.out.println("driftStepSize["+i+"]: " + driftStepSize[i]);
            }
        } else {
            // StepSizes sink in equal proportions for each Dimension. Dimensions of lowest number have higher StepSize.
            double stepDifference = (maxStepSize - minStepSize)/nrOfDimensions;
            double currentStepSize = maxStepSize;
            for (int i = 0; i < nrOfDimensions; i++){
                driftStepSize[i] = (max[i] - min[i]) * currentStepSize;
                currentStepSize -= stepDifference;
            }
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
            if (latestMetrics[i] + driftStepSize[i] > min[i] + maxNormal * (max[i]-min[i])) {
                direction[i] = false;
            } else if (latestMetrics[i] - driftStepSize[i] < min[i] + minNormal * (max[i]-min[i])) {
                direction[i] = true;
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

        double[] latestMetricsSafe = new double[nrOfAnomalyDims];

        for (int i = 0; i < nrOfDimensions; i++) {
            for(int j = 0; j < nrOfAnomalyDims; j++){
                if(i == anomalyDims[j]){
                    latestMetricsSafe[j] = latestMetrics[i];
                }
            }

        }

        Sample newSample = getNormalSampleWithDrift();

        for (int i = 0; i < nrOfDimensions; i++) {
            for(int j = 0; j < nrOfAnomalyDims; j++){
                if(i == anomalyDims[j]){
                    latestMetrics[i] = latestMetricsSafe[j];
                    if(latestMetrics[i] > min[i] + (max[i]-min[i])*0.5 )
                        newSample.getMetrics()[i] = min[i];
                    else newSample.getMetrics()[i] = max[i];
                }
            }

        }

        return newSample;
    }

}
