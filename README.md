# Supervised Learning: Guessing The Parties of Congress Members
**Supervised learning** is a category of machine learning tasks that train on a set of data with provided class labels and given unlabeled data input, approximates class labels for that data. 

The data must be in a readable format; here, each classifier accepts two csv files: a training set and a test set. Each contain lines of 42 bill votes(yea and nay, non-yea votes counted as nay for simplification) made by a member of congress. In the training set, each line is labeled with the party of the member.

## Naive Bayes Classifier
The conditional probabilites of a vote given a party are calculated for each vote from the training data and are used with **bayes theorem** to calculate the probability that a Congress member is either a **Democrat or a Republican** in the test set. These two probabilites are normalized and the party with higher probability is assigned.

## Decision Tree Classifier
We create a tree from the training set and use it to assign labels on the test set. The **entropy** of all 42 votes are calculated and the node with the lowest entropy is assigned as the root. The data is split into sets with that vote fixed as 'yea' and fixed as 'nay'. 

The tree continues to divide the data according to the best attribute (vote with the highest **info gain**, or lowest entropy) until sets cannot be divided further or all lines have the same party (pure value set).

## Instructions
Compille and run each classifier with the training csv file followed by the test csv file

Example: java CongressNBC congress_train.csv congress_test.csv

Classifier outputs class label for each line in test file and its probability
