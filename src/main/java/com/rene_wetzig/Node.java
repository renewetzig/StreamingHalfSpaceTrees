package com.rene_wetzig;

import com.rene_wetzig.externalClasses.Sample;

import java.util.concurrent.ThreadLocalRandom;

/*
    Represents a node in an HS-Tree

    Based on Streaming Half-Space Trees algorithm from "Fast Anomaly Detection for Streaming Data"
    by Swee Chuan Tan, Kai Ming Ting and Tony Fei Liu, 2011, in Proceedings of the Twenty-Second International
    Joint Conference on Artificial Intelligence

    Implemented and expanded by René Wetzig




 */
public class Node {

    private int myDepth; //depth of this node. See Node.k in paper
    private int maxDepth; //depth of a leaf in this tree
    public Node leftChild;  //left Child of this Node (instances smaller than halfPoint go here)
    public Node rightChild; //right Child of this Node (instances greater OR EQUAL than halfPoint go here)
    public Node parent;   //parent Node of this Node
    public double[] myMax; //array of maximum values in all dimensions this node contains
    public double[] myMin; //array of minimum values in all dims this node contains
    private int halvingDim; //dimension by which this node's domain will be halved by this Node
    private double halfPoint; //point that halves the domain. NOTE: < goes to left child, >= goes to right child
    public boolean amLeaf; // true if this node is a leaf
    public boolean amRoot; // true if this node is the root of a tree
    private int sizeLimit; // the minimum number of instances in the reference counter of a node, at which the anomalyScore is calculated.
    private int referenceMass; // saves mass of this node in the reference window
    private int latestMass; // saves mass of this node in the latest window


    public Node(int depth, int maxD, int nrOfDims, double[] min, double[] max, int sizeLimit, Node parent){
        this.myDepth = depth;
        this.parent = parent;
        this.myMin = min;
        this.myMax = max;
        this.maxDepth = maxD;
        this.sizeLimit = sizeLimit;

        referenceMass = 0;
        latestMass = 0;


        if(myDepth == 0){ amRoot = true; } else { amRoot = false; }

        if(myDepth == maxDepth){
            amLeaf = true;
        }else{ // create Children
            amLeaf = false;

            halvingDim = ThreadLocalRandom.current().nextInt(0, nrOfDims);
            halfPoint = (min[halvingDim]+max[halvingDim])/2;

            // create left Child
            double[] tempMax = myMax.clone();
            tempMax[halvingDim] = halfPoint;
            leftChild = new Node(myDepth+1, maxDepth, nrOfDims, myMin.clone(), tempMax, sizeLimit, this);

            //create right Child
            double[] tempMin = myMin.clone();
            tempMin[halvingDim] = halfPoint;
            rightChild = new Node(myDepth+1, maxDepth, nrOfDims, tempMin, myMax.clone(), sizeLimit, this);
        }


    }



    /*
     * inserts a data point into the tree and returns its anomaly score.
     */
    public int insertSample(Sample instance, boolean scoreCreated, int returnScore){
        latestMass++;
        if(!scoreCreated && referenceMass <= sizeLimit) {
            returnScore = referenceMass * (int) Math.pow(2,myDepth);
            scoreCreated = true;
        } else if(!scoreCreated && amLeaf) return referenceMass * (int) Math.pow(2,myDepth);

        if (!amLeaf &&  instance.getMetrics()[halvingDim] < halfPoint) {
            return leftChild.insertSample(instance, scoreCreated, returnScore);
        } else if(!amLeaf && instance.getMetrics()[halvingDim] >= halfPoint) {
            return rightChild.insertSample(instance, scoreCreated, returnScore);
        }

        return returnScore;
    }

    public void updateReference(){

        referenceMass = latestMass;
        latestMass = 0;

        if(!amLeaf){
            leftChild.updateReference();
            rightChild.updateReference();
        }
    }


    @Override
    public String toString() {
        String myValues = "";

        if(amRoot){
            myValues = "HalvingDim ="+halvingDim;
        } else {
            myValues = "Dim="+parent.halvingDim + " (" + myMin[parent.halvingDim] + ", " + myMax[parent.halvingDim] + ")";
        }

        return myValues;
    }
}
