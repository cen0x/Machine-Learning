package org.kramerlab.ml17.exercise;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import org.kramerlab.ml17.teaching.HomeworkTodo;
import static java.util.stream.Collectors.*;

/**
 * Naive Bayes
 */
public class Exercise_05_02 {

  /**
   * Converts piece of text to a (somewhat cleaned) list of words.
   *
   * Removes all punctuation and weird characters,
   * converts everything to lower case.
   */
  public static List<String> extractWords(String text) {
    LinkedList<String> result = new LinkedList<>();
    String noWeirdChars = text.replaceAll("[^a-zA-Z ]+", " ");
    String[] words = noWeirdChars.split(" ");
    for (String w: words) {
      String trimmed = w.trim();
      if (trimmed.length() > 0) {
        result.add(trimmed.toLowerCase());
      }
    }
    return result;
  }

  /**
   * A text instance is a list of preprocessed words, together with a
   * label (e.g. name of newsgroup or "spam"/"ham").
   */
  public static class TextInstance {
    private List<String> words;
    private String label;

    public TextInstance(List<String> words, String label) {
      this.words = words;
      this.label = label;
    }

    public List<String> getWords() {
      return words;
    }

    public String getLabel() {
      return label;
    }

    @Override
    public String toString() {
      String sample = "";
      int i = 0;
      for (String w: words) {
        sample = sample + w + ",";
        if (i == 16) break;
        i++;
      }
      return String.format(
        "[#words = %d, label = %s]{%s...}",
        words.size(),
        label,
        sample
      );
    }
  }

  /**
   * Loads text instances from a directory in src/main/resources.
   *
   * Can be used to load the training set and the test set.
   */
  public static List<TextInstance> loadTextInstances(String subdirectory)
  throws Exception {
    List<TextInstance> instances = new LinkedList<>();
    File dir = new File(
      Exercise_05_02.class.getClassLoader()
      .getResource(subdirectory).getFile()
    );
    for (File grp: dir.listFiles()) {
      String label = grp.getName();
      for (File textFile: grp.listFiles()) {
        String content = new String(Files.readAllBytes(textFile.toPath()));
        String[] lines = content.split("\n");
        String headerless = "";
        boolean skippingHeader = true;
        for (String line: lines) {
          if (!skippingHeader) {
            headerless = headerless + " " + line;
          } else {
            boolean isEmpty = line.trim().length() == 0;
            boolean isHeaderLine = line.matches("[a-zA-Z_-]+:.*");
            if (!isEmpty && !isHeaderLine) {
              headerless = headerless + " " + line;
              skippingHeader = false;
            }
          }
        }
        List<String> cleanedWords = extractWords(headerless);
        TextInstance ti = new TextInstance(cleanedWords, label);
        instances.add(ti);
      }
    }
    return instances;
  }

  /**
   * Loads training instances for 20newsgroups classifier.
   */
  public static List<TextInstance> loadTrainingSet() throws Exception {
    return loadTextInstances("20news-bydate-train");
  }

  /**
   * Loads test instances for 20newsgroups classifier.
   */
  public static List<TextInstance> loadTestSet() throws Exception {
    return loadTextInstances("20news-bydate-test");
  }

  /**
   * Interface for the naive Bayes classifier.
   */
  public static interface NaiveBayesTextClassifier {
    /**
     * Assigns a label to a preprocessed piece of text.
     *
     * @param preprocessedWords idealized piece of text, without any punctuation
     *   and with all words in lower case; The order of words is preserved,
     *   there can be duplicates.
     * @return class label (e.g. "sci.space")
     */
    String classify(List<String> preprocessedWords);
  }

  /**
   * Collects all words that occur in the training set.
   *
   * @param instances training set of instances
   * @return set of all words that occur in all texts
   */
  public static Set<String> extractVocabulary(List<TextInstance> instances) {
    throw new HomeworkTodo("implement extractVocabulary");
  }

  /**
   * For each possible class label <code>v</code>, estimates the
   * probability <code>P(v)</code>.
   *
   * Simply count the proportion of each class label in the dataset.
   *
   * @param instances training set of text instances
   * @return map from labels to class probability estimates
   */
  public static Map<String, Double> estimateClassProbabilities(
    List<TextInstance> instances
  ) {
    throw new HomeworkTodo("implement estimateClassProbabilities");
  }

  /**
   * For each class label <code>v</code>, and each word <code>w</code> from
   * the vocabulary, estimates <code>P(w|v)</code>.
   *
   * Uses uniform distribution on the vocabulary as prior.
   * Combines the actual count with a weighted prior, so that even words that
   * never occur in texts of certain class get some non-zero chance to occur.
   *
   * @param instances training set
   * @param vocabulary all words that occur in the training set
   * @param numVirtualExamples weight given to prior
   * @return map that maps class labels <code>v</code> to a nested map that
   *   in turn maps words <code>w</code> to estimated conditional probability
   *   <code>P(w|v)</code>.
   */
  // TODO: make vocabulary a Set<String> next time.
  public static Map<String, Map<String, Double>> estimateConditionalWordProbs(
    List<TextInstance> instances,
    List<String> vocabulary,
    int numVirtualExamples
  ) {
    throw new HomeworkTodo("implement estimateConditionalWordProbs");
  }

  /**
   * Trains a naive Bayes classifier for text classification.
   *
   * The trained naive Bayes classifier ignores all words that do not
   * occur in the vocabulary extracted from the training set.
   *
   * @param textInstances training set with labeled preprocessed text instances
   * @param numVirtualExamples weight given to prior
   * @return a trained naive Bayes text classifier
   */
  public static NaiveBayesTextClassifier learnNaiveBayesText(
    List<TextInstance> textInstances,
    int numVirtualExamples
  ) {
    return new NaiveBayesTextClassifier() {
      public String classify(List<String> preprocessedText) {
        throw new HomeworkTodo("implement learnNaiveBayesText");
      }
    };
  }

  public static void main(String... args) throws Exception {
    throw new HomeworkTodo("Try it out! Train, test, compute accuracy.");
  }
}
