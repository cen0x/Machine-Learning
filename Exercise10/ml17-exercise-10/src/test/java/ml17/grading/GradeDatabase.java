package ml17.grading;

import java.util.List;

interface GradeDatabase {
  void saveResults(
    String sheetId,
    double[][] points,
    List<String>[][] messages
  );

  static String toPythonArrayFormat(double[][] points) {
    StringBuilder bldr = new StringBuilder();
    bldr.append("[");
    int j = 0;
    for (double[] ex: points) {
      int len = ex.length;
      bldr.append("[");
      int i = 0;
      for (double p : ex) {
        bldr.append(String.format("%.1f", p));
        if (i < len - 1) {
          bldr.append(",");
        } else {
          bldr.append("]");
        }
        i++;
      }
      if (i == 0) {
        bldr.append("]");
      }
      if (j < points.length - 1) {
        bldr.append(",");
      } else {
        bldr.append("]");
      }
      j++;
    }
    if (j == 0) {
      bldr.append("]");
    }
    return bldr.toString();
  }

  static String toFormattedFeedback(List<String>[][] comments) {
    StringBuilder bldr = new StringBuilder();
    int exId = 1;
    for (List<String>[] exerciseComments: comments) {
      bldr.append("-----\n");
      int subExId = 1;
      for (List<String> subExComments: exerciseComments) {
        bldr.append(
          String.format("%4s: ",
            String.format("%d.%d", exId, subExId)
          )
        );
        boolean first = true;
        for (String comment: subExComments) {
          bldr.append((first ? "" : "      ") + " - " + comment + "\n");
          first = false;
        }
        if (first) {
          bldr.append("\n");
        }
        subExId++;
      }
      exId++;
    }
    bldr.append("-----\n");
    return bldr.toString();
  }

}