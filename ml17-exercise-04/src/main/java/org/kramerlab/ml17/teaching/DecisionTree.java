package org.kramerlab.ml17.teaching;

import java.util.Map;
import org.kramerlab.ml17.teaching.datasets.NominalValue;
import org.kramerlab.ml17.teaching.datasets.NominalAttribute;
import org.kramerlab.ml17.teaching.datasets.Instance;

/**
 * Decision tree that consists of inner nodes and leafs.
 *
 * Inner nodes are labeled by a <code>NominalAttribute</code>s.
 * Leafs are labeled by <code>NominalValue</code>s of the class attribute.
 */
public interface DecisionTree extends Classifier {
}