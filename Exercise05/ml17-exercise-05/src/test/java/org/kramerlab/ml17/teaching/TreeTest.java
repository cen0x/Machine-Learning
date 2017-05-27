package org.kramerlab.ml17.teaching;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;

/**
 * Contains a few utility methods for enumerating all possible
 * assignments of boolean values to a set of variables.
 */
public class TreeTest {
  /**
   * Generates list with variable names X_1 ... X_{n}
   */
  protected List<String> variableNames(int n) {
    LinkedList<String> result = new LinkedList<>();
    for (int i = 1; i <= n; i++) {
      result.add("X_" + i);
    }
    return result;
  }

  /**
   * Generates list with all possible valuations of variables
   * in the list.
   */
  protected List<Map<String, Boolean>> allValuations(List<String> vars) {
    if (vars.isEmpty()) {
      LinkedList<Map<String, Boolean>> res = new LinkedList<>();
      res.add(new HashMap<String, Boolean>());
      return res;
    } else {
      List<String> tail = new LinkedList<String>();
      tail.addAll(vars);
      String currentVariable = tail.remove(0);
      List<Map<String, Boolean>> vs = allValuations(tail);
      LinkedList<Map<String, Boolean>> res = new LinkedList<>();
      for (Map<String, Boolean> v: vs) {
        for (boolean b: new boolean[]{true, false}) {
          // O_o .... -.-
          HashMap<String, Boolean> mutableCopy = new HashMap<String, Boolean>();
          mutableCopy.putAll(v);
          mutableCopy.put(currentVariable, b);
          res.add(mutableCopy);
        }
      }
      return res;
    }
  }

  protected List<Map<String, Boolean>> allValuations(String ... vararg) {
    LinkedList<String> arg = new LinkedList<>();
    for (String s: vararg) {
      arg.add(s);
    }
    return allValuations(arg);
  }

  /*
  @Test
  public void showSomeValuations() {
    System.out.println(allValuations("A", "B"));
    System.out.println(allValuations(variableNames(3)));
    for (int i = 0; i < 5; i++) {
      System.out.println(i + " -> " + allValuations(variableNames(i)).size());
    }
  }
  */
}