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
}
