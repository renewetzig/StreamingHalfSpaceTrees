package com.rene_wetzig;

import java.util.Date;

public class TestSampleGenerator {

private int nrOfDimensions;
private double[] min;
private double[] max;

/*
This class generates Sample class objects for test purposes.

Note: There are different versions of normal samples available.
IMPORTANT: Anomalies are always in the exact Center of the domain. Avoid this area for normal Sample creation.
 */

    public TestSampleGenerator(int nrOfDimensions, double[] min, double[] max) {
        this.nrOfDimensions = nrOfDimensions;
        this.min = min.clone();
        this.max = max.clone();

    }

    public Sample getNormalSample(){
        Sample newSample;
        Header newHeader = new Header(new String[0]);
        Date newDate = new Date();

        double[] newMetrics = new double[nrOfDimensions];

        for(int i=0; i < nrOfDimensions; i++) {
            newMetrics[i] = min[i] + ((max[i]-min[i])/4);
        }

        newSample = new Sample(newHeader, newMetrics, newDate);

        return newSample;
    }


    //TODO introduce drift. Idea: Counter that knows how many samples have been introduced
    public Sample getNormalSampleWithDrift(){
        Sample newSample;
        Header newHeader = new Header(new String[0]);
        Date newDate = new Date();

        double[] newMetrics = new double[nrOfDimensions];

        for(int i=0; i < nrOfDimensions; i++) {
            newMetrics[i] = min[i] + ((max[i]-min[i])/4);
        }

        newSample = new Sample(newHeader, newMetrics, newDate);

        return newSample;
    }

    // returns a Sample at the exact center of the domain. Avoid this area for normal values.
    public Sample getAnomaly(){
        Sample newSample;
        Header newHeader = new Header(new String[0]);
        Date newDate = new Date();

        double[] newMetrics = new double[nrOfDimensions];

        for(int i=0; i < nrOfDimensions; i++) {
            newMetrics[i] = (min[i] + max[i]) / 2;
        }

        newSample = new Sample(newHeader, newMetrics, newDate);

        return newSample;
    }

}
