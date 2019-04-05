package com.rene_wetzig.thresholds.arima;



import Jama.Matrix;

import java.util.ArrayList;

public class EfficientModelONS {

    private int mk;
    private int d;
    private Matrix A;
    private Matrix lastInv;
    private Matrix Lm;
    private ArrayList<double[]> grads;
    private double rate;

    public EfficientModelONS(int mk, int d){
        this.mk = mk;
        this.d = d;
        double D = Math.sqrt(2*(mk));
        double G = 2 * Math.sqrt(mk) * D;
        rate = 0.5 * Math.min(1.0/(mk), 4*G*D);

        double epsilon = 1.0/(Math.pow(rate, 2) * Math.pow(D,2));
        A = Matrix.identity(mk, mk).times(epsilon);

        lastInv = A.inverse();

        double[][] L = new double[mk][1];
        for(int i = 0; i < mk; i++){
            L[i][0] = Math.random()-0.5;
        }
        Lm = Matrix.constructWithCopy(L);

        grads = new ArrayList<double[]>();

    }

    public double predict(){
        //predict
        double X_t = 0.0;
        for(int i=0; i < mk; i++){
            if(grads.size()-i-1 < 0){

                break;
            }
            X_t += Lm.get(i, 0)*grads.get(grads.size()-i-1)[d];
        }
        for(int i=0; i<d; i++){
            if(grads.size()-1 <0 ){

                break;
            }
            else{
                X_t += grads.get(grads.size()-1)[i];
            }
        }
        return X_t;
    }

    public void train(double newVal){

        double prediction = predict();
        updateModel(prediction, newVal);
        addGrad(newVal);




    }

    public void updateModel(double prediction, double realData){
        double loss = Math.pow(realData-prediction,2);

        double[][] grad_loss = new double[mk][1];
        for(int i=0; i<mk;i++){
            double x = 0;
            if(grads.size()-i-1 >= 0){
                x = grads.get(grads.size()-i-1)[d];

            }

            grad_loss[i][0] = -2*(realData-prediction)* x;
        }
        Matrix gl = Matrix.constructWithCopy(grad_loss);

        Matrix GL = gl.times(gl.transpose());
        //A = A.plus(GL);


        Matrix a = lastInv.times(GL).times(lastInv);
        double b = 1 + (gl.transpose()).times(lastInv).times(gl).get(0, 0);
        lastInv = lastInv.plus(a.times(-1/b));

        Matrix midres = lastInv.times(gl).times(-1.0/rate);
        Lm = Lm.plus(midres);

    }


    public void addGrad(double newVal){

        double[] newGrad = new double[d + 1];
        newGrad[0] = newVal;
        int currGradLength = grads.size();


        if (currGradLength > 0) {
            for (int g = 1; g < d + 1; g++) {
                newGrad[g]= newGrad[g-1] - grads.get(currGradLength-1)[g - 1];
            }
        }

        if (currGradLength == mk) {
            grads.remove(0);


        }
        grads.add(newGrad);
    }
}