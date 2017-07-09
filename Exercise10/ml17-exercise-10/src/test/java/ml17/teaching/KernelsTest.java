package ml17.teaching;

import java.util.function.*;
import java.util.Random;
import ml17.grading.Points;
import ml17.teaching.HomeworkTodo;
import org.junit.*;


import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static ml17.exercise.Kernels.*;
import static ml17.teaching.Kernel.*;

/**
 * Checks that the kernels defined in <code>Kernels.java</code>
 * adhere to the specification.
 */
public class KernelsTest {

  private final static double EPS = 0.0001;

  @Points(
    points = 1.0,
    exerciseId = 2,
    subExId = 1,
    errorMessage = "constant kernel fails"
  )
  @Test
  public void testConstant() {
    for (int i = 0; i < 10; i++) {
      double c = Math.random() * 42000;
      Kernel<String> s = constant(c);
      assertEquals(c, s.applyAsDouble("hey" + i, "hou" + (i * i)), EPS);
    }
  }

  @Points(
    points = 1.0,
    exerciseId = 2,
    subExId = 2,
    errorMessage = "scaling kernels fails"
  )
  @Test
  public void testScale() {
    for (int i = 0; i < 10; i++) {
      Kernel<String> k = scalarProdfxfy((String x) -> new double[]{x.length()});
      double c = Math.random() * 42;
      Kernel<String> scaledKernel = scale(c, k);

      for (int j = 0; j < 20; j++) {
        String a = "a " + j;
        String b = "b " + (j * j * j * 5395);
        assertEquals(
          c * a.length() * b.length(),
          scaledKernel.applyAsDouble(a, b),
          EPS
        );
      }
    }
  }

  @Points(
    points = 1.0,
    exerciseId = 2,
    subExId = 3,
    errorMessage = "pow fails"
  )
  @Test
  public void testPower() {
    Random rnd = new Random(42);
    for (int i = 0; i < 10; i++) {
      Kernel<String> k = scalarProdfxfy((String x) -> new double[]{x.length()});
      int p = rnd.nextInt(5);
      Kernel<String> powerKernel = power(k, p);

      for (int j = 0; j < 20; j++) {
        String a = "a " + j + rnd.nextInt(1000);
        String b = "b " + (j * j * rnd.nextInt(100) * 7598);
        assertEquals(
          Math.pow(a.length() * b.length(), p),
          powerKernel.applyAsDouble(a, b),
          EPS
        );
      }
    }
  }

  @Points(
    points = 2.0,
    exerciseId = 2,
    subExId = 4,
    errorMessage = "problems with polynomials with positive coeffs"
  )
  @Test
  public void testPoly() {
    Random rnd = new Random(42);
    for (int i = 0; i < 10; i++) {
      Kernel<String> k = scalarProdfxfy((String x) -> new double[]{x.length()});
      int deg = rnd.nextInt(3);
      double[] coeffs = new double[deg + 1];
      for (int r = 0; r <= deg; r++) {
        coeffs[r] = Math.random();
      }
      Kernel<String> polyKernel = poly(coeffs, k);

      for (int j = 0; j < 20; j++) {
        String a = "a " + j + rnd.nextInt(1000);
        String b = "b " + (j * j * rnd.nextInt(100) * 7598);
        double x = a.length() * b.length();
        double y = 0.0;
        // horner!
        for (int r = deg; r >= 0; r--) {
          y *= x;
          y += coeffs[r];
        }
        assertEquals(y, polyKernel.applyAsDouble(a, b), EPS);
      }
    }
  }

  @Points(
    points = 1.0,
    exerciseId = 2,
    subExId = 5,
    errorMessage = "f(x)k(x,y)f(y) behaves not as expected"
  )
  @Test
  public void testFxkxyfy() {
    Random rnd = new Random(42);
    for (int i = 0; i < 10; i++) {
      Kernel<String> k = scalarProdfxfy((String x) -> new double[]{x.length()});
      final double fi = i % 3.1314159;
      Function<String, Double> f = s -> (s.hashCode() % 100) * fi;
      Kernel<String> fkf = fxkxyfy(k, f);

      for (int j = 0; j < 20; j++) {
        String a = "a " + j + rnd.nextInt(1000);
        String b = "b " + (j * j * rnd.nextInt(100) * 7598);
        assertEquals(
          f.apply(a) * k.applyAsDouble(a, b) * f.apply(b),
          fkf.applyAsDouble(a, b),
          EPS
        );
      }
    }
  }

  @Points(
    points = 2.0,
    exerciseId = 2,
    subExId = 6,
    errorMessage = "gaussianRbf wrong"
  )
  @Test
  public void testGaussianRbf() {
    Random rnd = new Random(14535);
    for (int t = 0; t < 100; t++) {
      double sigma = (t * t) % 3.12423534 + 0.1;
      Kernel<double[]> k = gaussianRbf(sigma);

      int dim = rnd.nextInt(10);
      double[] x = new double[dim];
      double[] y = new double[dim];
      for (int i = 0; i < dim; i++) {
        x[i] = Math.random();
        y[i] = Math.random();
      }

      double is = k.applyAsDouble(x, y);
      double nSq = 0.0;
      for (int i = 0; i < dim; i++) {
        nSq += (x[i] - y[i]) * (x[i] - y[i]);
      }
      double should = Math.exp(-nSq/(2 * sigma * sigma));
      assertEquals(should, is, EPS);
    }
  }

  @Points(
    points = 2.0,
    exerciseId = 2,
    subExId = 7,
    errorMessage = "polynomialKernel wrong"
  )
  @Test
  public void testPolynomialKernel() {
    Random rnd = new Random(14535);
    for (int t = 0; t < 100; t++) {
      int exponent = t % 5;
      Kernel<double[]> k = polynomialKernel(exponent);

      int dim = rnd.nextInt(10);
      double[] x = new double[dim];
      double[] y = new double[dim];
      for (int i = 0; i < dim; i++) {
        x[i] = Math.random();
        y[i] = Math.random();
      }

      double is = k.applyAsDouble(x, y);
      double scProd = 0.0;
      for (int i = 0; i < dim; i++) {
        scProd += x[i] * y[i];
      }
      double should = Math.pow(scProd + 1, exponent);
      assertEquals(should, is, EPS);
    }
  }
}