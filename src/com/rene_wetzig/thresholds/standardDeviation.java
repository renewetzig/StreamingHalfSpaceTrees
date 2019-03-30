package com.rene_wetzig.thresholds;

public class standardDeviation extends Threshold {


    private double runningAverage;
    private int counter;
    private double squaredDistances;
    private double standardDeviation;
    private double sigma;
    private boolean started;




    public standardDeviation(int windowSize, double sigma){
        super(windowSize);
        counter = 0;
        standardDeviation = 0;
        this.sigma = sigma;
        started = false;
    }



    @Override
    public boolean insertNewSample(int anomalyScore) {

        if(!referenceCreated()) return true;


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
        return (anomalyScore > getCurrentThreshold());
    }

    public String toString(){
        return "standardDeviation(windowsize="+getWindowSize()+", sigma="+sigma+")";
    }
}
