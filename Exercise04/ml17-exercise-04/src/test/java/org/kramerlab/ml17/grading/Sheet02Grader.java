package org.kramerlab.ml17.grading;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.Result;
import java.util.List;

public class Sheet02Grader extends Grader {

  public Sheet02Grader() {}

  /** One-based id of the exercise sheet */
  protected String getSheetId() {
    return "02";
  }

  /**
   * Returns the maximum number of points that can be
   * given by this agent automatically.
   *
   */
  protected PointDistribution getPointDistribution() {
    double[][] res =
      {{2, 0.5, 2, 0.5, 2.5}, {0}, {0,0,0,0,1,0.5,0}};
    return new PointDistribution(res);
  }

  protected GradeDatabase getGradeDatabase() {
    return new PythonTableGradeDatabase();
  }

}