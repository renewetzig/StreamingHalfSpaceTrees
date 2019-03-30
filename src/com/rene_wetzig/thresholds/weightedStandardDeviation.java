package com.rene_wetzig.thresholds;

public class weightedStandardDeviation extends Threshold {

        private double runningAverage;
    private int counter;
    private double squaredDistances;
    private double standardDeviation;
    private double weightMostRecent;
    private double sigma;
    private boolean weightStdDev;
    private boolean started;

public weightedStandardDeviation(int windowSize, double weightMostRecent, double sigma, boolean weightStdDev){
        super(windowSize);
        counter = 0;
        standardDeviation = 0;
        this.weightMostRecent = weightMostRecent;
        this.sigma = sigma;
        this.weightStdDev = weightStdDev;
        started = false;

    }


    @Override
    public boolean insertNewSample(int anomalyScore) {

        if(!referenceCreated()) return true;


        if(!started){ // insert first scored value into running average.
            runningAverage = anomalyScore;
            counter++;
            started = true;
        }else{
            runningAverage = (1-weightMostRecent) * runningAverage + weightMostRecent*anomalyScore;
            if(weightStdDev){
                squaredDistances = (1-weightMostRecent) * squaredDistances + weightMostRecent * Math.pow(anomalyScore-runningAverage, 2);
                standardDeviation = Math.sqrt((squaredDistances));
            } else {
                squaredDistances += Math.pow(anomalyScore-runningAverage, 2);
                standardDeviation = Math.sqrt((squaredDistances/counter));
            }

            counter++;
        }
      //  System.out.println("Current average = " + runningAverage + "   Current StdDev. = "+ standardDeviation);
        setCurrentThreshold((int) Math.max(0, (runningAverage - sigma*standardDeviation)));
        return !(anomalyScore <= getCurrentThreshold());
    }

    public String toString(){
        return "weightedStandardDeviation(windowSize="+getWindowSize()+",weightMostRecent="+weightMostRecent+",sigma="+sigma+",weightStdDev="+weightStdDev+")";
    }
}
