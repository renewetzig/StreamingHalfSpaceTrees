package com.rene_wetzig;

import java.util.concurrent.ThreadLocalRandom;

/*
A class that represents a node in an HS-Tree
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
        this.myMin = min.clone();
        this.myMax = max.clone();
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
            leftChild = new Node(myDepth+1, maxDepth, nrOfDims, myMin, tempMax, sizeLimit, this);

            //create right Child
            double[] tempMin = myMin.clone();
            tempMin[halvingDim] = halfPoint;
            rightChild = new Node(myDepth+1, maxDepth, nrOfDims, tempMin, myMax, sizeLimit, this);
        }


    }




    // TODO Mass only needs to be kept in Leaves if we ignore SizeLimit

    /*
     * inserts an instance into the tree and returns its anomaly score.
     */
    public int insertSample(Sample instance){
        latestMass++;
        if (!amLeaf &&  referenceMass > sizeLimit && instance.getMetrics()[halvingDim] < halfPoint) {
            return leftChild.insertSample(instance);
        } else if(!amLeaf && referenceMass > sizeLimit && instance.getMetrics()[halvingDim] >= halfPoint) {
            return rightChild.insertSample(instance);
        }

        return referenceMass * (int) Math.pow(2,myDepth);
    }

    public void updateReference(){
        // TODO the paper says to replace referenceMass with latestMass only if latestMass or referenceMass are non-zero.
        // figure out if that is important.

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
