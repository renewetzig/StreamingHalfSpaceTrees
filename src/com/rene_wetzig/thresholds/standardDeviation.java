package com.rene_wetzig.thresholds;

public class standardDeviation extends Threshold {


    private int firstWindowCounter;
    private double runningAverage;
    private int counter;
    private double squaredDistances;
    private double standardDeviation;
    private double sigma;




    public standardDeviation(int windowSize, double sigma){
        super(windowSize);
        firstWindowCounter = 0;
        counter = 0;
        standardDeviation = 0;
        this.sigma = sigma;
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
            runningAverage = (counter * runningAverage + anomalyScore)/(counter+1);
            squaredDistances += Math.pow(anomalyScore-runningAverage, 2);
            standardDeviation = Math.sqrt((squaredDistances/counter));
            counter++;
        }
        currentThreshold = (int) (runningAverage - sigma*standardDeviation);
        return !(anomalyScore < currentThreshold);
    }
}
