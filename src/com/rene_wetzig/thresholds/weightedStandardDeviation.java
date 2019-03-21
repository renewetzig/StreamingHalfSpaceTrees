package com.rene_wetzig.thresholds;

public class weightedStandardDeviation extends Threshold {

    private int firstWindowCounter;
    private double runningAverage;
    private int counter;
    private double squaredDistances;
    private double standardDeviation;
    private double weightMostRecent;
    private double sigma;
    private boolean weightStdDev;

public weightedStandardDeviation(int windowSize, double weightMostRecent, double sigma, boolean weightStdDev){
        super(windowSize);
        firstWindowCounter = 0;
        counter = 0;
        standardDeviation = 0;
        this.weightMostRecent = weightMostRecent;
        this.sigma = sigma;
        this.weightStdDev = weightStdDev;

    }


    @Override
    public boolean insertNewSample(int anomalyScore) {

        if(firstWindowCounter <= windowSize){
            firstWindowCounter++;
            return true;
        }


        if(firstWindowCounter == windowSize+1){ // insert first scored value into running average.
            firstWindowCounter++;
            runningAverage = anomalyScore;
            counter++;

        }else if(firstWindowCounter > windowSize+1){
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
        currentThreshold = (int) (runningAverage - sigma*standardDeviation);
        return !(anomalyScore < currentThreshold);
    }
}
