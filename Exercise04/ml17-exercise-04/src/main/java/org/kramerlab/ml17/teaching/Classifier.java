package org.kramerlab.ml17.teaching;

import java.util.Map;
import org.kramerlab.ml17.teaching.datasets.NominalValue;
import org.kramerlab.ml17.teaching.datasets.NominalAttribute;
import org.kramerlab.ml17.teaching.datasets.Instance;

/**
 * A trained classifier: essentially just a function that takes an
 * Instance, and predicts a NominalValue for the class attribute.
 */
public interface Classifier {
  NominalValue classify(Instance inst);
}