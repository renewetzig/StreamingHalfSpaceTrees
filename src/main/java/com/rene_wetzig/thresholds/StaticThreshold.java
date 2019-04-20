package com.rene_wetzig.thresholds;

public class StaticThreshold extends Threshold {

    public StaticThreshold(int windowSize, int threshold) {
        super(windowSize);
        setCurrentThreshold(threshold);
    }


    public void updateModel(int anomalyScore) {

    }

    public String toString(){
        String string = "staticThreshold(threshold="+ getCurrentThreshold()+")";
        return string;
    }

}
