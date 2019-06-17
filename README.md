# Streaming Half-Space Trees with Anomaly-Thresholds
An Implementation of Streaming Half-Space Trees, a semi-supervised machine learning algorithm for offline anomaly detection on high speed data streams, with my own contribution Anomaly-Thresholds, which allows the algorithm to be used for online anomaly detection on high speed data streams, with classification of each new data point immediately after its arrival.

Streaming Half-Space Trees is based on the paper "Fast Anomaly Detection for Streaming Data" by Swee Chuan Tan, Kai Ming Ting and Tony Fei Liu, published in "Proceedings of the Twenty-Second International Joint Conference on Artificial Intelligence". 

It employs a diverse family of binary trees to partition the work-space of a multi-dimensional data stream (continuous, numerical dimensions only), allowing it to learn the composition of the data stream and predict whether data points are normal or anomalous. It's robust under concept shift and learns new behaviour quickly.

It requires a starting period of a number (dependent on parameters) of normal data points to learn the stream, everything after is unsupervised. Streaming Half-Space scores data points, anomalies are determined by keeping those scores for every data point and sorting the resulting log by the score. Lowest scores are anomalous points.

This implementation contains my contribution Anomaly-Thresholds. Algorithms that employ statistical techniques to predict whether a data point is normal or anomalous at the time of its arrival, making Streaming Half-Space Trees a true online anomaly detection algorithm. 

A data stream simulator, Test Sample Generator, is also included. It allows you to generate a synthetic data stream for testing purposes, based on a number of parameters.

Also included are a testbed that automatically runs a synthetic data stream through the algorithm, analyses and logs the results to disk, and a testbed-runner that allows automated creation of testbeds for empirical study of the algorithm's performance. 
