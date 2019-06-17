package com.rene_wetzig;


// Note: Main class only used for quick start of the test environment.

/*
 This is an expanded implementation of the Streaming Half-Space Trees algorithm from "Fast Anomaly Detection for Streaming Data"
    by Swee Chuan Tan, Kai Ming Ting and Tony Fei Liu, 2011, in Proceedings of the Twenty-Second International
    Joint Conference on Artificial Intelligence

 Streaming Half-Space Trees is an Offline machine learning anomaly detection algorithm capable of identifying anomalies
 on large sets of streaming data, comprised of multidimensional continuous values.

 We have expanded Streaming Half-Space Trees to work Online, allowing it to predict whether a newly arrived data point is
  an anomaly immediately after its arrival, by introducing efficient adaptive thresholds.
  We have also introduced TestSampleGenerator, a data stream simulator that allows us to simulate high-frequency multidimensional
  data streams with concept drift and randomly injected anomalies.

  Classes:
  TreeOrchestrator - creates, keeps and manages a family of Half-Space Trees, which is used to learn a data stream's profile
    and score data points
  Node - Half-Space Trees are created as a series of linked Nodes
  thresholds - package holds different implementations of static or adaptive thresholds, used to make predictions based
    on anomaly scores generated using Streaming Half-Space Trees. Includes an implementation of Online Arima by CIT, TU-Berlin

  TestSampleGenerator - generates a synthetic high-frequency data stream based on a series of parameters, allows anomaly injection
  TestBed - creates and runs all necessary objects for a single test run, handles anomaly injection, records results
  TestBedRunner - runs a series of TestBeds in sequence, records results



 */


public class Main {




    public static void main(String[] args) {

        TestBedRunner.main();

    }
}
