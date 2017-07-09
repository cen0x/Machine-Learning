package ml17.teaching;
import java.util.function.*;

/**
 * Marker interface that guarantees that a function from `X^2 -> Double` is
 * a kernel.
 *
 * The constructor is private.
 * The clas provides few static methods to create some basic kernels.
 * Nothing else is believed to be a kernel until explicitly expressed
 * in terms of <code>scalaProdfxfy</code>, <code>sum</code>, <code>prod</code>,
 * and <code>exp</code>.
 */
public abstract class Kernel<X> implements ToDoubleBiFunction<X, X> {

  /** prohibit instantiation anywhere else */
  private Kernel() {}

  /**
   * Encodes basic definition of a kernel for feature spaces with finite
   * dimension.
   *
   * Expresses the fact that whenever <code>f</code> is a function from
   * <code>X</code> to <code>Real^n</code>, we can build a kernel function
   * <code>k(x, y) = scalarProduct(f(x), f(y))</code>.
   *
   * @param f mapping from input space <code>X</code> to
   *   feature space <code>Real^n</code>
   * @return kernel function <code>k(x,y) = scalarProduct(f(x), f(y))</code>
   */
  public static <X> Kernel<X> scalarProdfxfy(Function<X, double[]> f) {
    return new Kernel<X>() {
      public double applyAsDouble(X x, X y) {
        double[] xFeatures = f.apply(x);
        double[] yFeatures = f.apply(y);
        if (xFeatures.length != yFeatures.length) {
          throw new IllegalArgumentException(
            "f produced feature vectors of two different lengths: " +
            xFeatures.length + " != " + yFeatures.length
          );
        }
        double sum = 0.0;
        for (int i = 0; i < xFeatures.length; i++) {
          sum += xFeatures[i] * yFeatures[i];
        }
        return sum;
      }
    };
  }

  /**
   * Encodes the assumption that the sum of two kernels is again a kernel.
   *
   * The actual proof relies on properties of an infinite family of Gram's
   * matrices, and cannot be encoded in Java type system, therefore we provide
   * it as an axiom.
   *
   * @param k1 first kernel
   * @param k2 second kernel
   * @return pointwise sum of the two kernels, which is guaranteed to be
   *   a kernel.
   */
  public static <X> Kernel<X> sum(Kernel<X> k1, Kernel<X> k2) {
    return new Kernel<X>() {
      public double applyAsDouble(X x, X y) {
        return k1.applyAsDouble(x,y) + k2.applyAsDouble(x,y);
      }
    };
  }

  /**
   * Encodes the assumption that the pointwise product of two kernels is
   * again a kernel.
   *
   * The actual proof relies on properties of an infinite family of Gram's
   * matrices, and cannot be encoded in Java type system, therefore we provide
   * it as an axiom.
   *
   * @param k1 first kernel
   * @param k2 second kernel
   * @return pointwise product of two kernels, which is guaranteed to be a
   *  kernel.
   */
  public static <X> Kernel<X> prod(Kernel<X> k1, Kernel<X> k2) {
    return new Kernel<X>() {
      public double applyAsDouble(X x, X y) {
        return k1.applyAsDouble(x, y) * k2.applyAsDouble(x, y);
      }
    };
  }

  /**
   * Pointwise exponential of a kernel is again a kernel.
   *
   * The actual proof relies on properties of an infinite family of Gram's
   * matrices and limits of series of such matrices, and therefore cannot
   * be encoded in Java type system.
   *
   * @param k a kernel function
   * @return the kernel <code>(x, y) -> exp(k(x,y))</code>
   */
  public static <X> Kernel<X> exp(Kernel<X> k) {
    return new Kernel<X>() {
      public double applyAsDouble(X x, X y) {
        return Math.exp(k.applyAsDouble(x, y));
      }
    };
  }
}