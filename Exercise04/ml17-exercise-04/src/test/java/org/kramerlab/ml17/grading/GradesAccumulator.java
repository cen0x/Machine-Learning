package org.kramerlab.ml17.grading;

import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * State of a running graded test suite.
 *
 * Accumulates points and remarks for each sub-exercise,
 * notes which tests have failed.
 *
 * @author tyukiand
 */
class GradesAccumulator {

  private HashSet<String> failedTests = new HashSet<>();

  private HashMap<SubExPos, Double> subExPoints = new HashMap<>();
  private HashMap<SubExPos, List<String>> subExMessages = new HashMap<>();

  public synchronized void addPoints(SubExPos key, double points) {
    if (subExPoints.containsKey(key)) {
      subExPoints.put(key, subExPoints.get(key) + points);
    } else {
      subExPoints.put(key, points);
    }
  }

  public synchronized void addMessage(SubExPos key, String message) {
    if (subExMessages.containsKey(key)) {
      subExMessages.get(key).add(message);
    } else {
      LinkedList<String> msgs = new LinkedList<>();
      msgs.add(message);
      subExMessages.put(key, msgs);
    }
  }

  public synchronized void markAsFailed(String className, String methodName) {
    failedTests.add(className + "#" + methodName);
  }

  public synchronized boolean isFailed(String className, String methodName) {
    return failedTests.contains(className + "#" + methodName);
  }

  public synchronized List<String> getMessages(SubExPos key) {
    if (subExMessages.containsKey(key)) {
      return subExMessages.get(key);
    } else {
      return new LinkedList<String>();
    }
  }

  public synchronized double getPoints(SubExPos key) {
    if (subExPoints.containsKey(key)) {
      return subExPoints.get(key);
    } else {
      return 0.0;
    }
  }

}