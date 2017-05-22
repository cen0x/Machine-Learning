package org.kramerlab.ml17.teaching;

import org.kramerlab.ml17.teaching.datasets.*;
import java.util.function.BiFunction;

/**
 * A "training method" that "trains" a classifier simply
 * by looking into the table with the right results,
 * and pretending to be insane with some specified frequency.
 *
 */
public class OracleMockTrainingMethod
implements BiFunction<Dataset, NominalAttribute, DecisionTree> {

  private final Dataset rightAnswers;
  private final int insanityRounds;

  public OracleMockTrainingMethod(Dataset rightAnswers, int insanityRounds) {
    this.rightAnswers = rightAnswers;
    this.insanityRounds = insanityRounds;
  }

  private int trainingSize = 0;
  public DecisionTree apply(Dataset ds, NominalAttribute attr) {
    trainingSize = ds.getNumberOfInstances();
    return new DecisionTree() {
      private int round = 0;
      public NominalValue classify(Instance i) {
        round++;
        if (insanityRounds > 0 && (round % insanityRounds == 0)) {
          return new NominalValue("I know it's wrong, but for now, I'm insane");
        } else {
          for (Instance inst: rightAnswers.getInstances()) {
            if (inst.equalsExcept(i, attr)) {
              return (NominalValue) inst.getValue(attr);
            }
          }
        }
        throw new Error("Right answers table incomplete!");
      }
    };
  }

  public int getTrainingSize() {
    return trainingSize;
  }
}