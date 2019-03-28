package com.rene_wetzig.thresholds;

public abstract class Threshold {

    protected final int windowSize;
    protected int currentThreshold;

    // initialise this threshold.
    public Threshold(int windowSize){this.windowSize = windowSize;};

    // returns false if Sample is recognised as an anomaly, true if it's recognised as a normal sample.
    public abstract boolean insertNewSample(int anomalyScore);

    public int getThreshold(){
     return currentThreshold;
    }

}
