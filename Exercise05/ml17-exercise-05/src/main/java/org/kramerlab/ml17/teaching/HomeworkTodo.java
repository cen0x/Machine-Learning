package org.kramerlab.ml17.teaching;

/**
 * Placeholder for a solution of an exercise.
 */
public class HomeworkTodo extends RuntimeException {
  public HomeworkTodo(String what) {
    super(what);
  }

  /**
   * Placeholder that can be inserted whenever an expression
   * is expected.
   *
   * Terminology: `throw new HomeworkTodo("...");` is a statement.
   * `HomeworkTodo.gap<X>("...")` is an expression of type `X`.
   */
  public static <X> X gap(String message) {
    throw new HomeworkTodo(message);
  }
}