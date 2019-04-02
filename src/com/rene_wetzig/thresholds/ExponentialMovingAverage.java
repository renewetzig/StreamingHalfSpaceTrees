package com.rene_wetzig.thresholds;


public class ExponentialMovingAverage extends Threshold {

    private boolean normalsOnly; // only use normal scores for weighted average if true
    private double weightedAverageNormal; // keeps the weighted average of Normal samples.
    private double weightMostRecent; // how strongly the most recent normal value is supposed to be weighted.
    private double percentage; // Percentage under normal average that we consider anomaly.


    public ExponentialMovingAverage(int windowSize, double weightMostRecent, double percentage, boolean normalsOnly){
        super(windowSize);
        this.weightMostRecent = weightMostRecent;
        this.percentage = percentage;
        this.normalsOnly = normalsOnly;

        setCurrentThreshold(0);
        weightedAverageNormal = 0;
    }


    public boolean insertNewSample(int anomalyScore){
        if(!referenceCreated()) return true;
        boolean isNormal = anomalyScore > getCurrentThreshold();

        if(!normalsOnly || isNormal) {
            weightedAverageNormal = ((1 - weightMostRecent) * weightedAverageNormal + weightMostRecent * anomalyScore);
            setCurrentThreshold((int) (percentage * weightedAverageNormal));
        }
        return isNormal;
    }

    public String toString(){
        String string = "exponentialMovingAverage(windowsize="+getWindowSize()+", weightMostRecent="+weightMostRecent+",percentage="+percentage+",normalsOnly="+normalsOnly+")";
        return string;
    }

}
