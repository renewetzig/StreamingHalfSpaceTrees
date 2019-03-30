package com.rene_wetzig.thresholds;

public class staticThreshold extends Threshold {

    public staticThreshold(int windowSize, int threshold) {
        super(windowSize);
        setCurrentThreshold(threshold);
    }

    @Override
    public boolean insertNewSample(int anomalyScore) {
        if(!referenceCreated()) return true;

        if(anomalyScore > getCurrentThreshold()) {
            return true;
        } else {
            return false;
        }
    }

    public String toString(){
        String string = "staticThreshold(threshold="+ getCurrentThreshold()+")";
        return string;
    }

}
