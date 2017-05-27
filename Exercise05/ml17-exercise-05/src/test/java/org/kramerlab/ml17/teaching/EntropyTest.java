package org.kramerlab.ml17.teaching;

import org.junit.Test;
import org.kramerlab.ml17.teaching.datasets.Dataset;
import static org.junit.Assert.*;

public class EntropyTest {

  protected Dataset loadWeatherDataset() throws Exception {
    Dataset ds = new Dataset();
    ds.load(EntropyTest.class.getClassLoader().getResourceAsStream(
      "weather.nominal.arff"));
    return ds;
  }

  protected Dataset loadDataset(String name) throws Exception {
    Dataset ds = new Dataset();
    ds.load(EntropyTest.class.getClassLoader().getResourceAsStream(name));
    return ds;
  }

  public void getDatasetFromResourcesTest() throws Exception {
    Dataset ds = loadWeatherDataset();
    System.out.println("Here is a test dataset from test/resources:");
    System.out.println(ds);
  }

  public void assertApproxEquals(double expected, double is, double eps) {
    assertTrue(expected + " <-> " + is, Math.abs(expected - is) < eps);
  }

  protected double log2(double x) {
    return Math.log(x) / Math.log(2);
  }
}