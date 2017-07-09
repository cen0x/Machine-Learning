package ml17.grading;

import java.util.List;

/**
 * A "fake" grade database that simply prints results to
 * the screen.
 *
 * You can copy this information using pencil and paper, and consider this
 * your database.
 */
class PrintoutGradeDatabase implements GradeDatabase {
  public void saveResults(
    String sheetId,
    double[][] points,
    List<String>[][] comments
  ) {
    System.out.println("Results for sheet " + sheetId);
    System.out.println(GradeDatabase.toPythonArrayFormat(points));
    System.out.println(GradeDatabase.toFormattedFeedback(comments));
  }
}