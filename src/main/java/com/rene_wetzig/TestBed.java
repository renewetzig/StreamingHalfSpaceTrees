package com.rene_wetzig;

import com.rene_wetzig.externalClasses.Sample;
import com.rene_wetzig.thresholds.Threshold;

import java.io.File;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;

public class TestBed extends Thread{

    private TreeOrchestrator family;
    private TestSampleGenerator testSampleGenerator;
    private int nrOfSamples;
    private Threshold threshold;
    private int nrOfDimensions;
    private int windowSize;

    private boolean printEverything;

    private PrintWriter pw;
    private StringBuilder sb;
    private PrintWriter pwDetailed;

    private int percentageOfAnomalies;
    private int anomalyLength;
    private int anomalyCounter;

    private String settings;
    private String results;

    private int counter;

    private int normalsInserted;
    private int anomaliesInserted;
    private int normalsRecognised;
    private int normalsNotRecognised;
    private int anomaliesRecognised;
    private int anomaliesNotRecognised;

    private int normalsWithASZero; //normal samples with anomalyScore = 0
    private final boolean calculateSampleInsertionTime;
    private long sampleInsertionTime;
    long sampleInsertionStart;
    long sampleInsertionEnd;

    public TestBed(Threshold threshold,
                   int nrOfTrees, int maxDepth, int windowSize, int sizeLimit,
                   int nrOfDimensions, double min, double max, int nrOfSamples, int percentageOfAnomalies,
                   boolean randomiseTestSampleGenerator, int anomalyDimensions, int firstAnomalyDim, int anomalyLength, double minStepSize, double maxStepSize,
                   double minNormal, double maxNormal, int testNumber, String filePath, boolean printEverything, boolean calculateSampleInsertionTime) {


        this.nrOfSamples = nrOfSamples;
        this.threshold = threshold;
        this.percentageOfAnomalies = percentageOfAnomalies;
        this.nrOfDimensions = nrOfDimensions;
        this.windowSize = windowSize;
        this.printEverything = printEverything;
        this.anomalyLength = anomalyLength;
        this.calculateSampleInsertionTime = calculateSampleInsertionTime;

        anomalyCounter = 0;
        counter = 0;

        normalsInserted = 0;
        anomaliesInserted = 0;
        normalsRecognised = 0;
        normalsNotRecognised = 0;
        anomaliesRecognised = 0;
        anomaliesNotRecognised = 0;

        normalsWithASZero = 1-windowSize;

        this.sampleInsertionTime = 0;
        
        try {
            pw = new PrintWriter(new File(filePath + ".csv"));
            sb = new StringBuilder();

            if(printEverything) pwDetailed = new PrintWriter(new File(filePath + " DETAILED.csv"));

        } catch(Exception e){
            System.out.println(e.toString());
        }

        double[] minArray = new double[nrOfDimensions];
        double[] maxArray = new double[nrOfDimensions];
        for(int i = 0; i < nrOfDimensions; i++) {
            minArray[i] = min;
            maxArray[i] = max;
        }

        family = new TreeOrchestrator(nrOfTrees, maxDepth, windowSize, nrOfDimensions, minArray, maxArray, sizeLimit);
        testSampleGenerator = new TestSampleGenerator(nrOfDimensions, minArray, maxArray, anomalyDimensions, firstAnomalyDim, minStepSize, maxStepSize, randomiseTestSampleGenerator, minNormal , maxNormal );

        String topHeaders = "threshold;" +
                "nrOfTrees;" +
                "maxDepth;" +
                "windowSize;" +
                "sizeLimit;" +
                "nrOfDimensions;" +
                "min;" +
                "max;" +
                "nrOfSamples;" +
                "percentageOfAnomalies;" +
                "randomiseTestSampleGenerator;" +
                "nrOfAnomalyDims;" +
                "firstAnomalyDim;" +
                "anomalyLength;" +
                "minStepSize;" +
                "maxStepSize;" +
                "minNormal;" +
                "maxNormal;";
        pw.println(topHeaders);
        if(printEverything) pwDetailed.println(topHeaders);


        settings= nrOfTrees+";"+maxDepth+";"+windowSize+";"+sizeLimit+";"+nrOfDimensions+";"+min+";"+max+";"+nrOfSamples+";"+
                percentageOfAnomalies+";"+randomiseTestSampleGenerator+";"+anomalyDimensions+";"+firstAnomalyDim+";"+anomalyLength+";"+
                minStepSize+";"+maxStepSize+";"+
                minNormal+";"+maxNormal+";";
        pw.println(threshold.toString()+";" + settings+"\n");
        if(printEverything) pwDetailed.println(threshold.toString()+";" + settings+"\n");


        // print column headers to detailed file
        if(printEverything) {
            sb.append("SampleNr;Normal/Anomaly;Recognised Correctly;AnomalyScore;currentThreshold;");
            for (int i = 0; i < nrOfDimensions; i++) {
                sb.append("Dimension " + i + ";");
            }
            pwDetailed.println(sb.toString());

            sb.delete(0, sb.length());
        }


        run();



        double percentageNormalRecognised = (double) Math.round(((double) normalsRecognised / normalsInserted)*1000)/10;
        double percentageNormalNotRecognised = (double) Math.round(((double) normalsNotRecognised / normalsInserted)*1000)/10;
        double percentageAnomaliesRecognised = (double) Math.round(((double) anomaliesRecognised / anomaliesInserted)*1000)/10;
        double percentageAnomaliesNotRecognised = (double) Math.round(((double) anomaliesNotRecognised / anomaliesInserted)*1000)/10;

        if(calculateSampleInsertionTime) this.sampleInsertionTime = (long) (this.sampleInsertionTime / nrOfSamples);

        pw.println("Results");
        pw.println("Normals with AS = 0;" +
                "Percentage Normal As Normal;" +
                "Percentage Normal As Anomaly;" +
                "Percentage Anomaly As Anomaly;" +
                "Percentage Anomaly As Normal;"+
                "Normals inserted;" +
                "Normals Recognised;" +
                "Normals Not Recognised;" +
                "Anomalies inserted;" +
                "Anomalies Recognised;" +
                "Anomalies Not Recognised;" +
                "Average Time Sample Insertion + Prediction;");
        results = normalsWithASZero+ ";" + percentageNormalRecognised + ";" + percentageNormalNotRecognised+";"+percentageAnomaliesRecognised+";"+percentageAnomaliesNotRecognised+";"+
                normalsInserted+";"+normalsRecognised+";"+normalsNotRecognised+";"+anomaliesInserted+";"+anomaliesRecognised+";"+anomaliesNotRecognised+";"+ this.sampleInsertionTime +";";
        pw.println(results);

        // close the files
        pw.flush();
        pw.close();
        if(printEverything){
            pwDetailed.flush();
            pwDetailed.close();
        }

        System.out.println("Test Number " + testNumber + " finished successfully. \n Average Sample Insertion Time: " + this.sampleInsertionTime + "ms \n Normals with AnomalyScore = 0 : " + normalsWithASZero);

    }


    public void run() {
        while (counter < nrOfSamples){
            boolean insertNormal = true;
            if (counter > windowSize) {
                if(0 < anomalyCounter && anomalyCounter < anomalyLength){
                    insertNormal = false;
                    anomalyCounter++;
                }
                else {
                    int randomiser = ThreadLocalRandom.current().nextInt(1, 101);
                    // if our randomiser outputs a number greater than the percentageOfAnomalies, insert a normal Sample. Otherwise, insert an Anomaly.
                    if (randomiser <= percentageOfAnomalies) {
                        insertNormal = false;
                        anomalyCounter = 1;
                    }
                }
            }

            insertSample(insertNormal);
        }



    }


    // Insert a Sample and print details to the file
    public void insertSample(boolean normal){
        counter++;
        sb.append(counter + ";");
        Sample newSample;
        if(normal){
            newSample = testSampleGenerator.getNormalSampleWithDrift();
            normalsInserted++;
            if(printEverything) sb.append("Normal;");
        } else {
            newSample = testSampleGenerator.getAnomaly();
            anomaliesInserted++;
            if(printEverything) sb.append("Anomaly;");
        }

        if(calculateSampleInsertionTime) sampleInsertionStart = System.currentTimeMillis();
        int sampleScore = family.insertSample(newSample);
        if(calculateSampleInsertionTime) sampleInsertionEnd = System.currentTimeMillis();

        if(calculateSampleInsertionTime) sampleInsertionTime += (sampleInsertionEnd - sampleInsertionStart);



        if(normal && sampleScore == 0) normalsWithASZero++;

        if(threshold.insertNewSample(sampleScore)){
            if(normal){
                normalsRecognised++;
                if(printEverything) sb.append("true;");
            }else{
                anomaliesNotRecognised++;
                if(printEverything) sb.append("false;");
            }
        } else {
            if(normal){
                normalsNotRecognised++;
                if(printEverything) sb.append("false;");
            }else{
                anomaliesRecognised++;
                if(printEverything) sb.append("true;");
            }
        }

        if(printEverything) {
            sb.append(sampleScore + ";" + threshold.getCurrentThreshold() + ";");
            for (int i = 0; i < nrOfDimensions; i++) {
                sb.append(newSample.getMetrics()[i] + ";");
            }
            pwDetailed.println(sb.toString());
            sb.delete(0, sb.length());
        }

    }



    public String getStats(){ return threshold.toString()+";" + results + settings; }


}
