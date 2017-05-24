package org.kramerlab.ml17.exercise;

import org.kramerlab.ml17.teaching.datasets.NominalAttribute;
import org.kramerlab.ml17.teaching.datasets.Attribute;
import org.kramerlab.ml17.teaching.datasets.NominalValue;
import org.kramerlab.ml17.teaching.datasets.Dataset;
import org.kramerlab.ml17.teaching.datasets.Instance;
import org.kramerlab.ml17.teaching.Classifier;

import java.util.*;
import java.util.function.BiFunction;

import javax.swing.event.ListSelectionEvent;

import org.kramerlab.ml17.teaching.HomeworkTodo;

/**
 * Stratified cross validation.
 */
public class Exercise_04_02 {

  /**
   * Extracts the training set from a dataset for cross
   * validation with specified number of folds.
   *
   * @param ds the entire dataset
   * @param currentFold number of current fold (zero based, 0 to (numFold - 1))
   * @param numFold number of folds used for cross validation
   * @return training set
   */
  public static Dataset trainCV(Dataset ds, int currentFold, int numFold) {
    throw new HomeworkTodo("implement trainCV");
  }

  /**
   * Extracts the test set from a dataset for cross
   * validation with specified number of folds.
   *
   * @param ds the entire dataset
   * @param currentFold number of current fold (zero based, 0 to (numFold - 1))
   * @param numFold number of folds used for cross validation
   * @return test set
   */
  public static Dataset testCV(Dataset ds, int currentFold, int numFold) {
    throw new HomeworkTodo("implement testCV");
  }

  /**
   * Shuffles instances in a dataset.
   *
   * @param ds a dataset
   * @return dataset with the same instances and attributes, but
   *   with instances shuffled uniformly.
   */
  public static Dataset shuffle(Dataset ds)
  {
    List<Instance> shuffled = ds.getInstances();
    Collections.shuffle(shuffled);

    // build a new dataset with the uniform shuffled instances
    // and same attributes before
    Dataset d = new Dataset();
    ds.getAttributes().forEach(a -> { d.addAttribute(a); });
    shuffled.forEach(i -> { d.addInstance(i); });

    return d;
  }

  /**
   * Groups instances of a dataset by the values of the target attribute.
   *
   * @param dataset entire dataset
   * @param classAttribute target attribute
   * @return list of datasets, such that the union of all datasets
   *   contains
   */
  public static List<Dataset> stratify(
    Dataset dataset,
    NominalAttribute classAttribute
  ) {
	  Map<NominalValue,Dataset> data = new HashMap<>();
	  for(NominalValue val: classAttribute.getValues()){
		  Dataset ds = new Dataset(val.getValue());
		  data.put(val, ds);
		  for(Attribute att:dataset.getAttributes()){
			  ds.addAttribute(att);
		  }
	  }
	  for(Instance inst:dataset.getInstances()){
		  data.get(inst.getValue(classAttribute)).addInstance(inst);
	  }
	return new ArrayList<>(data.values());
	  
  }

  /**
   * Evaluates a classifier using stratified cross validation.
   *
   * @param trainingMethod a function that takes a training set,
   *   a target attribute ("class attribute"), and outputs a trained classifier
   * @param dataset the entire dataset (which will be split into training
   *   and validation sets multiple times)
   * @param classAttribute the class attribute (by which the data has to
   *   be stratified)
   * @param numFolds number of folds
   * @return average accuracy
   */
  public static double stratifiedCrossValidation(
    BiFunction<Dataset, NominalAttribute, Classifier> trainingMethod,
    Dataset dataset,
    NominalAttribute classAttribute,
    int numFolds
  ) {
    throw new HomeworkTodo("implement cross validation");
  }
}