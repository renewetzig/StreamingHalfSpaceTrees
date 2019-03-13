package com.rene_wetzig;

public class Threshold {

    private String thresholdType = "";
    private int windowSize;
    private int currentThreshold;



    public Threshold(String type, int windowSize){
        thresholdType = type;
        this.windowSize = windowSize;
    }

    public Threshold(int windowSize){
        thresholdType = "default";
        this.windowSize = windowSize;
    }


    // returns false if Sample is recognised as an anomaly, true if it's recognised as a normal sample.
    public boolean insertNewSample(Sample sample){

        //if()

        return true;
    }

/*    // AnomalyThreshold Method A (BAD): for calculating AnomalyThreshold - goes by average of normal points
    // calculates an averaged anomaly Threshold over second window.
    // IMPORTANT: This assumes the second window is clean.
    private void averageFirstWindowDividedBy(int divisor) {
        if (counter > windowSize && divisor <= windowSize) {
            averagedAnomalyThreshold = averagedAnomalyThreshold + thisSampleScore;
            divisor++;
        }
        if (divisor == windowSize && !averageCreated) {
            anomalyThreshold = (averagedAnomalyThreshold / divisor) / 4;
            averageCreated = true;
        }
    }*/

}
