package ml17.exercise;
import ml17.teaching.Kernel;
import ml17.teaching.HomeworkTodo;
import java.util.function.*;
import static ml17.teaching.Kernel.*;

/**
 * Implement the static methods in this class using only
 * the assumptions <code>scalarProdfxfy</code>,
 * <code>sum</code>, <code>prod</code>, and <code>exp</code> provided in
 * <code>Kernel</code>.
 *
 * You are not supposed to use "<code>new Kernel</code>" anywhere
 * (whether hidden by a java-lambda or not),
 * the static methods of the class <code>Kernel</code> is
 * supposed to be the only trusted source of <code>Kernel</code>s, all the
 * more sophisticated kernels must be composed from those basic kernels.
 */
public class Kernels {

  /**
   * Constructs the kernel <code>(x, y) -> c</code>.
   *
   * @param c a nonnegative constant
   * @return the kernel <code>(x, y) -> c</code>.
   */
  public static <X> Kernel<X> constant(double c) {
    if (c < 0) {
      throw new IllegalArgumentException(
        "The constant function is a kernel only for non-negative constants!"
      );
    }
    Kernel<X> kernel = scalarProdfxfy(x -> new double[]{Math.sqrt(c)});
    return kernel;
  }

  /**
   * Constructs the kernel <code>(x, y) -> c * k(x, y)</code>
   * for nonnegative constants <code>c</code> and kernels <code>k</code>.
   *
   * @param nonNegConst a nonnegative constant
   * @param k a kernel
   * @return the kernel <code>(x, y) -> c * k(x, y)</code>
   */
  public static <X> Kernel<X> scale(double nonNegConst, Kernel<X> k) {
    if (nonNegConst < 0) {
      throw new IllegalArgumentException("The constant must be non-negative!");
    }
    return prod(constant(nonNegConst), k);
  }

  /**
   * Constructs the kernel <code>(x, y) -> ((k(x, y)) ^ exponent)</code>.
   *
   * @param k a kernel
   * @param exponent a nonnegative integer exponent
   * @return kernel <code>k</code> raised to the specified power pointwise.
   */
  public static <X> Kernel<X> power(Kernel<X> k, int exponent) {
    if (exponent < 0) {
      throw new IllegalArgumentException("The exponent must be positive");
    }
    if(exponent > 1) {
      return prod(k, power(k, exponent-1));
    }
    return (exponent == 0) ? constant(1) : k;
  }

  /**
   * Constructs the kernel
   * <code> c_0 + c_1 * k(x, y) + ... c_n * k(x,y)^n </code>.
   *
   * @param coeffs nonnegative polynomial coefficients
   * @param k a kernel
   * @return kernel plugged into the polynomial (which is guaranteed to be
   *   a kernel)
   */
  public static <X> Kernel<X> poly(double[] coeffs, Kernel<X> k) {
    for (double c: coeffs) {
      if (c < 0) {
        throw new IllegalArgumentException("negative coefficient!");
      }
    }
    Kernel<X> s = constant(coeffs[0]);
    for(int i=1; i<coeffs.length; i++)
      s = sum(s, scale(coeffs[i], power(k, i)));
    return s;
  }

  /**
   * Constructs the kernel <code> (x, y) -> f(x)k(x,y)f(y)</code>.
   *
   * @param k a kernel
   * @param f an arbitrary function
   * @return the kernel <code> (x, y) -> f(x)k(x,y)f(y)</code>
   */
  public static <X> Kernel<X> fxkxyfy(
    Kernel<X> k,
    Function<X, Double> f
  ) {
    Kernel<X> fxy = scalarProdfxfy(x -> new double[]{f.apply(x)});
    return prod(k, fxy);
  }

  // Computes square of the euclidean norm of a vector in R^n
  public static double normSq(double[] x) {
    double sum = 0.0;
    for (double a: x) {
      sum += a * a;
    }
    return sum;
  }

  /**
   * Constructively proves that
   * <code>(x,y) -> exp(-(norm(x - y)^2)/(2*sigma^2))</code>
   * is indeed a kernel.
   */
  public static Kernel<double[]> gaussianRbf(double sigma) {
    if (sigma <= 0) {
      throw new IllegalArgumentException("sigma must be positive");
    }
    double sq = Math.pow(sigma,2);
    Kernel<double[]> k = exp(scale(1/sq, scalarProdfxfy(x -> x)));
    return fxkxyfy(k, x -> Math.exp(-normSq(x)/(2*sq)));
  }

  /**
   * Provides constructive proof of the fact that the polynomial kernel
   * <code>(x, y) -> (scalarProduct(x, y) + 1)^k</code>
   * is indeed a kernel.
   */
  public static Kernel<double[]> polynomialKernel(int k) {
    return power(sum(scalarProdfxfy(x -> x), constant(1)), k);
  }
}