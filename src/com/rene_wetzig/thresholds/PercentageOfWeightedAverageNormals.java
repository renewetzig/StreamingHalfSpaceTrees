package com.rene_wetzig.thresholds;


public class PercentageOfWeightedAverageNormals extends Threshold {

    private int counter; // counts the number of samples inserted in the current window
    private int resetCounter; // Resets the Threshold after resetCounter Samples were inserted. Does not reset if resetCounter is set to zero
    private boolean resetActive;
    private double weightedAverageNormal; // keeps the weighted average of Normal samples.
    private double weightMostRecent; // how strongly the most recent normal value is supposed to be weighted.
    private double percentage; // Percentage under normal average that we consider anomaly.


    public PercentageOfWeightedAverageNormals(int windowSize, double weightMostRecent, int percentage, int resetCounter){
        super(windowSize);
        this.weightMostRecent = weightMostRecent;
        this.percentage = percentage*0.01;
        this.resetCounter = resetCounter;
        if(resetCounter == 0) { resetActive = false; } else { resetActive = true; }

        currentThreshold = 0;
        weightedAverageNormal = 0;
        counter = 0;
    }


    public boolean insertNewSample(int anomalyScore){
        if(resetActive) counter++;
        if(resetActive && counter == resetCounter) {
            currentThreshold = 0;
            weightedAverageNormal = 0;
            counter = 0;
        }
        if(anomalyScore > currentThreshold){
            weightedAverageNormal = ((1-weightMostRecent)*weightedAverageNormal + weightMostRecent*anomalyScore);
            currentThreshold = (int) (percentage*weightedAverageNormal);
            return true;
        } else {
            return false;
        }
    }


}
