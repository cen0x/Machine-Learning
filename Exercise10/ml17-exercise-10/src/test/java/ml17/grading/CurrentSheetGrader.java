package ml17.grading;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.Result;
import java.util.List;

public class CurrentSheetGrader extends Grader {

  public CurrentSheetGrader() {}

  /** One-based id of the exercise sheet */
  protected String getSheetId() {
    return "10";
  }

  /**
   * Returns the maximum number of points that can be
   * given by this agent automatically.
   */
  protected PointDistribution getPointDistribution() {
    double[][] res = {{3}, {1, 1, 1, 2, 1, 2, 2}, {7}};
    return new PointDistribution(res);
  }

  protected GradeDatabase getGradeDatabase() {
    return new PythonTableGradeDatabase();
  }

}