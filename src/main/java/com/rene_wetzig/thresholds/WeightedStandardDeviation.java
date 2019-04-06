package com.rene_wetzig.thresholds;

public class WeightedStandardDeviation extends Threshold {

    private double runningAverage;
    private double squaredDistances;
    private double standardDeviation;
    private double weightMostRecent;
    private double sigma;
    private boolean started;

public WeightedStandardDeviation(int windowSize, double weightMostRecent, double sigma){
        super(windowSize);
        standardDeviation = 0;
        this.weightMostRecent = weightMostRecent;
        this.sigma = sigma;
        started = false;

    }


    @Override
    public boolean insertNewSample(int anomalyScore) {

        if(!referenceCreated()) return true;


        if(!started){ // insert first scored value into running average.
            runningAverage = anomalyScore;
            started = true;
        }else{
            runningAverage = (1-weightMostRecent) * runningAverage + weightMostRecent*anomalyScore;

            squaredDistances = (1-weightMostRecent) * squaredDistances + weightMostRecent * Math.pow(anomalyScore-runningAverage, 2);
            standardDeviation = Math.sqrt((squaredDistances));

        }
      //  System.out.println("Current average = " + runningAverage + "   Current StdDev. = "+ StandardDeviation);
        setCurrentThreshold((int) Math.max(0, (runningAverage - sigma*standardDeviation)));
        return !(anomalyScore <= getCurrentThreshold());
    }

    public String toString(){
        return "WeightedStandardDeviation(windowSize="+getWindowSize()+",weightMostRecent="+weightMostRecent+",sigma="+sigma+")";
    }
}
