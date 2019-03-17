package com.rene_wetzig.thresholds;

public class staticThreshold extends Threshold {

    public staticThreshold(int windowSize, int threshold) {
        super(windowSize);
        currentThreshold = threshold;
    }

    @Override
    public boolean insertNewSample(int anomalyScore) {
        if(anomalyScore > currentThreshold) {
            return true;
        } else {
            return false;
        }

    }

}
