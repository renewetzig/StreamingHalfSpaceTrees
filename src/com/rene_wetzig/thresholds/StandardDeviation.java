package com.rene_wetzig.thresholds;

public class StandardDeviation extends Threshold {


    private double runningAverage;
    private int counter;
    private double squaredDistances;
    private double standardDeviation;
    private double sigma;
    private boolean started;




    public StandardDeviation(int windowSize, double sigma){
        super(windowSize);
        counter = 0;
        standardDeviation = 0;
        this.sigma = sigma;
        started = false;
    }



    @Override
    public boolean insertNewSample(int anomalyScore) {
        if(!referenceCreated()) return true;
        boolean prediction = predictSample(anomalyScore);

        if(!started){ // insert first scored value into running average.
            runningAverage = anomalyScore;
            counter++;
            started = true;
        } else {
            runningAverage = (counter * runningAverage + anomalyScore)/(counter+1);
            squaredDistances += Math.pow(anomalyScore-runningAverage, 2);
            standardDeviation = Math.sqrt((squaredDistances/counter));
            counter++;
        }
        setCurrentThreshold((int) (runningAverage - sigma*standardDeviation));

        return prediction;
    }

    public String toString(){
        return "StandardDeviation(windowsize="+getWindowSize()+", sigma="+sigma+")";
    }
}
