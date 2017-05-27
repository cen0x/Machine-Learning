package org.kramerlab.ml17.teaching;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains a few utility methods for testing some basic properties of
 * codes.
 */
public class CodeTest {

  protected boolean checkPrefixProperty(boolean[] a, boolean[] b) {
    if (a.length > b.length) {
      return checkPrefixProperty(b, a);
    } else {
      for (int i = 0; i < a.length; i++) {
        if (a[i] != b[i]) return true;
      }
      return false;
    }
  }

  protected boolean checkPrefixProperty(Map<Character, boolean[]> code) {
    for (Character a: code.keySet()) {
      for (Character b: code.keySet()) {
        if (a != b) {
          if (!checkPrefixProperty(code.get(a), code.get(b))) return false;
        }
      }
    }
    return true;
  }

  @Test
  public void testerTest() {
    HashMap<Character, boolean[]> goodCode = new HashMap<>();
    goodCode.put('A', new boolean[]{true, false, true});
    goodCode.put('B', new boolean[]{true, true});
    goodCode.put('C', new boolean[]{false, true});
    assertTrue(checkPrefixProperty(goodCode));

    HashMap<Character, boolean[]> badCode = new HashMap<>();
    badCode.put('A', new boolean[]{true, false, true});
    badCode.put('B', new boolean[]{true, true});
    badCode.put('C', new boolean[]{true, false});
    assertTrue(!checkPrefixProperty(badCode));
  }
}