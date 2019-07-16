# Streaming Half-Space Trees with Anomaly-Thresholds
An Implementation of Streaming Half-Space Trees, a semi-supervised machine learning algorithm for offline anomaly detection on high speed data streams, with my own contribution Anomaly-Thresholds, which allows the algorithm to be used for online anomaly detection on high speed data streams, with classification of each new data point immediately after its arrival.

# The Original Algorithm

Streaming Half-Space Trees is based on the paper "Fast Anomaly Detection for Streaming Data" by Swee Chuan Tan, Kai Ming Ting and Tony Fei Liu, published in "Proceedings of the Twenty-Second International Joint Conference on Artificial Intelligence". 

The algorithm employs a diverse family of binary trees to partition the work-space of a multi-dimensional data stream (numerical, continuous dimensions only), allowing it to learn the composition of the data stream using only a short window of normal data points, and predict whether data points are normal or anomalous. According to the authors, it is robust under concept drift and learns new behaviour quickly.

It requires a starting period of a small number (dependent on parameters) of normal data points to learn the stream, after which the detection and continued learning are completely unsupervised. 

Streaming Half-Space Trees assigns scores to data points - the higher the score assigned to a data point, the higher the likelihood of it being normal. Anomalies are then predicted after the data streem has ended by keeping the scores for each data point, sorting the resulting log by the score and predicting the data points at the bottom of the list as anomalous. For this method to be useful, knowledge of the percentage of anomalies in the data stream is needed.

# Contribution: Anomaly-Thresholds
This implementation contains my contribution Anomaly-Thresholds, which turns Streaming Half-Space Trees into an online, real-time anomaly detection algorithm that works without any information about the expected anomaly quota in a data stream, and only a very limited number of parameters needed.

Anomaly-Thresholds are algorithms that employ statistical techniques to predict whether a data point is normal or anomalous at the time of its arrival, eliminating the need to log anomaly scores or wait for the stream to end to output results.

I've also included my simple data stream simulator. It allows the user to generate a synthetic data stream for testing purposes, based on AIOps data stream characteristics that can be controlled using a number of parameters.

To allow for the empirical study of the algorithm's performance, I've also added a testbed that runs a synthetic data stream through the algorithm, analyses and logs the results to disk, and a testbed-runner which allows the user to create, run and log any number of testbeds to find average performance data. 
