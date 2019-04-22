package com.rene_wetzig.thresholds;

import com.rene_wetzig.thresholds.arima.EfficientModelONS;

public class Arima extends Threshold
{

    double percentageBelow; // How much below arima is Anomaly?
    EfficientModelONS arima;

    public Arima(int windowSize, int arimaWindow, int differentiation, double percentageBelow){
        super(windowSize);
        this.percentageBelow = percentageBelow;
        arima = new EfficientModelONS(arimaWindow, differentiation); // alle benutzen für differentiation maximal 5. Typisch ist 3.

    }

    @Override
    public void updateModel(int anomalyScore) {
        arima.train(anomalyScore);
        setCurrentThreshold((int) Math.max((1-percentageBelow) * arima.predict(),0));
    }

    @Override
    public String toString() {
        return "Arima( windowSize = " + getWindowSize() + ", percentageBelow = "+ percentageBelow + ")";
    }
}
