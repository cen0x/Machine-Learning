package org.kramerlab.ml17.grading;

/**
 * Globally identifies position of a subexercise in a series of
 * exercise sheets.
 *
 * The identifiers are one-based (more natural, because sheets and
 * exercises are usually numbered starting from 1).
 *
 * The corresponding indices are zero-based (id - 1).
 */
class SubExPos {
  public final int exerciseId;
  public final int subExId;

  public SubExPos(int exercise, int subEx) {
    exerciseId = exercise;
    subExId = subEx;
  }

  public int getExerciseIndex() {
    return exerciseId - 1;
  }

  public int getSubExIndex() {
    return subExId - 1;
  }

  @Override
  public String toString() {
    return String.format(
      "(Ex %02d, Part %02d)",
      exerciseId,
      subExId
    );
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof SubExPos) {
      SubExPos other = (SubExPos)o;
      return
        other.exerciseId == this.exerciseId &&
        other.subExId == this.subExId;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return 99989 * exerciseId + 7477 * subExId;
  }
}