package org.kramerlab.ml17.grading;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.Result;
import java.util.List;

public class Sheet05Grader extends Grader {

  public Sheet05Grader() {}

  /** One-based id of the exercise sheet */
  protected String getSheetId() {
    return "05";
  }

  /**
   * Returns the maximum number of points that can be
   * given by this agent automatically.
   *
   * TODO: balance it out properly
   */
  protected PointDistribution getPointDistribution() {
    double[][] res =
      {{0.0}};
    return new PointDistribution(res);
  }

  protected GradeDatabase getGradeDatabase() {
    return new PythonTableGradeDatabase();
  }

}