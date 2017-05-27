package org.kramerlab.ml17.grading;

import java.util.List;
import java.util.Map;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * A simple flat-file database that saves the results to multiple files,
 * some of them in python format.
 *
 * The files are specified by environment variables.
 */
class PythonTableGradeDatabase implements GradeDatabase {

  private static final String ERRMSG_FILE = "GRADER_ERRMSG_FILE";
  private static final String TABLE_FILE = "GRADER_TABLE_FILE";
  private static final String GROUP = "GRADER_GROUP";
  private static final String AGENT = "GRADER_AGENT";

  public void saveResults(
    String sheetId,
    double[][] points,
    List<String>[][] comments
  ) {

    // write it to the screen too
    System.out.println("Results for sheet " + sheetId +
      " (preliminary and probably nonsensical):");
    System.out.println(GradeDatabase.toPythonArrayFormat(points));
    System.out.println(GradeDatabase.toFormattedFeedback(comments));

    // now read the environment variables in order to understand where
    // to store the results.
    Map<String, String> env = System.getenv();

    boolean keysSet = true;
    for (
      String key:
      new String[]{ERRMSG_FILE, TABLE_FILE, GROUP, AGENT}
    ) {
      if (!env.containsKey(key)) {
        System.out.println("Environment variable " + key + " is not set");
        keysSet = false;
      }
    }

    if (keysSet) {
      String errMsgFile = env.get(ERRMSG_FILE);
      String tableFile = env.get(TABLE_FILE);
      String groupId = env.get(GROUP);
      String agentId = env.get(AGENT);

      try(
        FileWriter fw = new FileWriter(tableFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)
      ) {
        out.println(
          String.format(
            "table['%s']['%s']['%s'] = %s",
            sheetId,
            groupId,
            agentId,
            GradeDatabase.toPythonArrayFormat(points)
          )
        );
      } catch (IOException e) {
        throw new Error(e);
      }

      try(
        FileWriter fw = new FileWriter(errMsgFile);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)
      ) {
        out.println(GradeDatabase.toFormattedFeedback(comments));
      } catch (IOException e) {
        throw new Error(e);
      }
    } else {
      System.out.println(
        "Some environment variables are missing, RESULTS NOT SAVED!"
      );
    }
  }
}