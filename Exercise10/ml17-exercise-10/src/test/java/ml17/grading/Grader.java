package ml17.grading;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.Result;
import java.util.List;

public abstract class Grader extends RunListener {

  /** One-based id of the exercise sheet */
  protected abstract String getSheetId();
  protected abstract PointDistribution getPointDistribution();
  protected abstract GradeDatabase getGradeDatabase();

  private GradesAccumulator accumulator;

  /**
   * Called before any tests have been run.
   *
   * Sets up the data structure that will hold the grading results.
   */
  @Override
  public void testRunStarted(Description description) {
    accumulator = new GradesAccumulator();
  }

  /**
   * Called when all tests have finished.
   */
  @Override
  public void testRunFinished(Result result) {
    // copy accumulator results into arrays of right shapes
    double[][] maxPoints = getPointDistribution().getMaxPoints();
    double[][] results = getPointDistribution().getZeroPoints();

    List<String>[][] messages =
      (List<String>[][])new List<?>[maxPoints.length][];

    for (int i = 0; i < maxPoints.length; i++) {
      messages[i] = (List<String>[])(new List<?>[maxPoints[i].length]);
      for (int j = 0; j < maxPoints[i].length; j++) {
        SubExPos pos = new SubExPos(i + 1, j + 1);
        messages[i][j] = accumulator.getMessages(pos);
        double p = accumulator.getPoints(pos);
        // if (results[i][j] + p <= maxPoints[i][j]) {
        results[i][j] += p;
        // }
      }
    }

    getGradeDatabase().saveResults(getSheetId(), results, messages);
  }

  @Override
  public void testFailure(Failure f) {
    Description descr = f.getDescription();
    accumulator.markAsFailed(descr.getClassName(), descr.getMethodName());
  }

  @Override
  public void testFinished(Description descr) {

    // find class and method name of the test
    String className = descr.getClassName();
    String methodName = descr.getMethodName();
    boolean isFailed = accumulator.isFailed(className, methodName);

    try {
      Class<?> c = Class.forName(descr.getClassName());
      Method m = c.getMethod(descr.getMethodName());
      Annotation[] annotations = m.getAnnotations();
      // find the grade part annotation
      for (Annotation a: annotations) {
        if (a instanceof Points) {
          Points gp = (Points)a;
          // depending on whether the test failed or succeeded,
          // add points or an error message.
          SubExPos subExPos = new SubExPos(gp.exerciseId(), gp.subExId());
          if (isFailed) {
            accumulator.addMessage(subExPos, gp.errorMessage());
          } else {
            accumulator.addPoints(subExPos, gp.points());
          }
        }
      }
    } catch (ClassNotFoundException cnfe) {
      throw new Error(cnfe);
    } catch (NoSuchMethodException nsme) {
      throw new Error(nsme);
    }
  }

}