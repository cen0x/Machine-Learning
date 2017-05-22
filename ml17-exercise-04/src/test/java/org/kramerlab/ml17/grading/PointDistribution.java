package org.kramerlab.ml17.grading;

/**
 * Describes the distribution of points for an exercise sheet.
 *
 * Wraps an array of arrays of doubles.
 * The entire array stands for the exercise sheet.
 * Each sub-array stands for an exercise.
 * An entry in the sub-array stands for a sub-exercise.
 * The value of the double is the maximum number of points this grader
 * can give for an exercise.
 * In particular, exercises not graded by this automated grader, are
 * represented by (non-empty!) arrays filled with zeroes.
 */
final class PointDistribution {

  private double[][] maxPoints;

  public double[][] getMaxPoints() {
    double[][] ret = new double[maxPoints.length][];
    for (int i = 0; i < maxPoints.length; i++) {
      double[] ex = new double[maxPoints[i].length];
      for (int j = 0; j < ex.length; j++) {
        ex[j] = maxPoints[i][j];
      }
      ret[i] = ex;
    }
    return ret;
  }

  public double[][] getZeroPoints() {
    double[][] ret = new double[maxPoints.length][];
    for (int i = 0; i < maxPoints.length; i++) {
      double[] ex = new double[maxPoints[i].length];
      for (int j = 0; j < ex.length; j++) {
        ex[j] = 0.0;
      }
      ret[i] = ex;
    }
    return ret;
  }

  public PointDistribution(double[][] maxPoints) {
    for (double[] exercise: maxPoints) {
      for (double subExercise: exercise) {
        if (subExercise < 0) {
          throw new IllegalArgumentException("Invalid maxPoints: negative.");
        }
      }
    }
    this.maxPoints = maxPoints;
  }
}