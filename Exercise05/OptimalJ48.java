import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

import java.util.Random;

public class OptimalJ48 implements Classifier
{
    private float confidenceStart = 0.01f;
    private float confidenceEnd = 0.5f;
    private float confidenceStep = 0.01f;
    private double percentageTrainSet = 60;
    private Instances dataset;
    private Instances trainSet;
    private Instances testSet;
    private Instances validationSet;
    private J48 classifier;
    private double bestConfidenceFactor = 0.0;
    private double bestAccuracy = 0.0;
    private double validationAccuracy = 0.0;

    public float getConfidenceStart() {
        return confidenceStart;
    }

    public void setConfidenceStart(float confidenceStart) {
        this.confidenceStart = confidenceStart;
    }

    public float getConfidenceEnd() {
        return confidenceEnd;
    }

    public void setConfidenceEnd(float confidenceEnd) {
        this.confidenceEnd = confidenceEnd;
    }

    public float getConfidenceStep() {
        return confidenceStep;
    }

    public void setConfidenceStep(float confidenceStep) {
        this.confidenceStep = confidenceStep;
    }

    public double getBestConfidenceFactor() {
        return this.bestConfidenceFactor;
    }

    public double getBestAccuracy() {
        return this.bestAccuracy;
    }

    public double getValidationAccuracy() {
        return this.validationAccuracy;
    }

    @Override
    public void buildClassifier(Instances instances) throws Exception
    {
        this.dataset = instances;
        // randomize dataset
        instances.randomize(new Random());
        // split dataset into train, test and validation set
        RemovePercentage rp = new RemovePercentage();
        rp.setInputFormat(instances);
        rp.setPercentage(percentageTrainSet);
        rp.setInvertSelection(true);
        this.trainSet = Filter.useFilter(instances, rp);
        rp.setInputFormat(instances);
        rp.setPercentage(percentageTrainSet);
        rp.setInvertSelection(false);
        Instances remainderSet = Filter.useFilter(instances, rp);
        rp.setInputFormat(remainderSet);
        rp.setPercentage(50);
        rp.setInvertSelection(true);
        this.validationSet = Filter.useFilter(remainderSet, rp);
        rp.setInputFormat(remainderSet);
        rp.setPercentage(50);
        rp.setInvertSelection(false);
        this.testSet = Filter.useFilter(remainderSet, rp);
        this.findBestPruneParameter();
    }

    private void findBestPruneParameter() throws Exception
    {
        this.classifier = new J48();
        Evaluation evaluation = new Evaluation(this.trainSet);
        double confidence = this.confidenceStart;

        while(confidence <= this.confidenceEnd)
        {
            this.classifier.setConfidenceFactor((float)confidence);
            this.classifier.setUnpruned(false);
            this.classifier.buildClassifier(this.trainSet);
            evaluation.evaluateModel(this.classifier, this.testSet, new Object[0]);
            double accuracy = evaluation.pctCorrect();
            if(accuracy > this.bestAccuracy)
            {
                this.bestConfidenceFactor = confidence;
                this.bestAccuracy = accuracy;
            }
            confidence += this.confidenceStep;
        }

        // validate best factor
        this.classifier.setConfidenceFactor((float)this.bestConfidenceFactor);
        evaluation.evaluateModel(this.classifier, this.validationSet, new Object[0]);
        this.validationAccuracy = evaluation.pctCorrect();

    }

    @Override
    public double classifyInstance(Instance instance) throws Exception {
        return this.classifier.classifyInstance(instance);
    }

    @Override
    public double[] distributionForInstance(Instance instance) throws Exception {
        return new double[0];
    }

    @Override
    public Capabilities getCapabilities() {
        return null;
    }

}
