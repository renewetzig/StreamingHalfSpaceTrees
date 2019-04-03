package com.rene_wetzig.thresholds;

public class Arima extends Threshold
{



    public Arima(int windowSize){
        super(windowSize);
    }

    @Override
    public boolean insertNewSample(int anomalyScore) {
        if(!referenceCreated()) return true;
        boolean prediction = predictSample(anomalyScore);

        return prediction;
    }

    @Override
    public String toString() {
        return null;
    }
}
