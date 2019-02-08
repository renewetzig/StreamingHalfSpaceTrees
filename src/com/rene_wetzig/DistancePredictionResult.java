package com.rene_wetzig;

public class DistancePredictionResult {

    private final boolean isAnomaly;
    private final double error;
    private final double[] distance;
    private final double threshold;

    public DistancePredictionResult(boolean isAnomaly, double error, double[] distance, double threshold) {
        this.isAnomaly = isAnomaly;
        this.error = error;
        this.distance = distance;
        this.threshold = threshold;
    }

    public boolean isAnomaly() {
        return isAnomaly;
    }

    public double getError() {
        return error;
    }

    public double[] getDistance() {
        return distance;
    }

    public double getThreshold() {
        return threshold;
    }
}
