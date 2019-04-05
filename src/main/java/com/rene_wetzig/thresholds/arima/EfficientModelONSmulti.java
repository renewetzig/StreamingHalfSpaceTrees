package com.rene_wetzig.thresholds.arima;



import Jama.Matrix;

import java.util.ArrayList;

public class EfficientModelONSmulti {

    private int mk;
    private int d;
    private int dim;
    private Matrix A;
    private Matrix lastInv;
    private Matrix Lm;
    private ArrayList<double[][]> grads;
    private double rate;

    public EfficientModelONSmulti(int mk, int d, int n){
        this.mk = mk;
        this.d = d;
        this.dim = n;
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

        grads = new ArrayList<double[][]>();

    }

    public double[] predict(){
        //predict
        double[] X_t = new double[dim];
        for(int i=0; i < mk; i++){
            if(grads.size()-i-1 < 0){

                break;
            }
            for(int j=0; j<dim;j++){
                X_t[j] += Lm.get(i, 0)*grads.get(grads.size()-i-1)[d][j];
            }
        }
        for(int i=0; i<d; i++){
            if(grads.size()-1 <0 ){

                break;
            }
            else{
                for(int j=0; j<dim;j++){
                    X_t[j] += grads.get(grads.size()-1)[i][j];
                }
            }
        }
        return X_t;
    }

    public void train(double[] newVal){

        double[] prediction = predict();
        updateModel(prediction, newVal);
        addGrad(newVal);




    }

    public void updateModel(double[] prediction, double[] realData){
        //double loss = Math.pow(realData-prediction,2);

        double[][] grad_loss = new double[mk][1];
        for(int i=0; i<mk;i++){
            double x = 0;
            grad_loss[i][0] = 0;
            if(grads.size()-i-1 >= 0){
                for(int j=0; j<dim;j++){
                    x = grads.get(grads.size()-i-1)[d][j];
                    grad_loss[i][0] += -2*(realData[j]-prediction[j])* x;
                }
            }


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


    public void addGrad(double[] newVal){

        double[][] newGrad = new double[d + 1][dim];
        newGrad[0] = newVal;
        int currGradLength = grads.size();


        if (currGradLength > 0) {
            for (int g = 1; g < d + 1; g++) {
                for(int n = 0; n < dim; n++){
                    newGrad[g][n]= newGrad[g-1][n] - grads.get(currGradLength-1)[g - 1][n];
                }
            }
        }

        if (currGradLength == mk) {
            grads.remove(0);


        }
        grads.add(newGrad);
    }
}