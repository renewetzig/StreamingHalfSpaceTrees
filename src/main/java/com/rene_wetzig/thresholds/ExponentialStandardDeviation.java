package com.rene_wetzig.thresholds;

public class ExponentialStandardDeviation extends Threshold {

    private double emAverage;
    private double squaredDistances;
    private double emVar;
    private double emStdDev;
    private double weightMostRecent;
    private double sigma;
    private boolean started;

public ExponentialStandardDeviation(int windowSize, double weightMostRecent, double sigma){
        super(windowSize);
        emStdDev = 0;
        this.weightMostRecent = weightMostRecent;
        this.sigma = sigma;
        started = false;

    }


    @Override
    public void updateModel(int anomalyScore) {

        if(!started){ // insert first scored value into running average.
            emAverage = anomalyScore;
            emVar = 0;
            started = true;
        }else{
            double delta = anomalyScore - emAverage; 
                    
            emAverage = emAverage + weightMostRecent*delta;
            
            emVar = (1-weightMostRecent) * (emVar + weightMostRecent * Math.pow(delta, 2));
            emStdDev = Math.sqrt(emVar);
            
        }
      //  System.out.println("Current average = " + emAverage + "   Current StdDev. = "+ emStdDev);
        setCurrentThreshold((int) Math.max(0, (emAverage - sigma*emStdDev)));
    }

    public String toString(){
        return "ExponentialStandardDeviation(windowSize="+getWindowSize()+",weightMostRecent="+weightMostRecent+",sigma="+sigma+")";
    }
}
