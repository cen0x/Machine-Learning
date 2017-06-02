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
	 * Removes all punctuation and weird characters, converts everything to
	 * lower case.
	 */
	public static List<String> extractWords(String text) {
		LinkedList<String> result = new LinkedList<>();
		String noWeirdChars = text.replaceAll("[^a-zA-Z ]+", " ");
		String[] words = noWeirdChars.split(" ");
		for (String w : words) {
			String trimmed = w.trim();
			if (trimmed.length() > 0) {
				result.add(trimmed.toLowerCase());
			}
		}
		return result;
	}

	/**
	 * A text instance is a list of preprocessed words, together with a label
	 * (e.g. name of newsgroup or "spam"/"ham").
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
			for (String w : words) {
				sample = sample + w + ",";
				if (i == 16)
					break;
				i++;
			}
			return String.format("[#words = %d, label = %s]{%s...}", words.size(), label, sample);
		}
	}

	/**
	 * Loads text instances from a directory in src/main/resources.
	 *
	 * Can be used to load the training set and the test set.
	 */
	public static List<TextInstance> loadTextInstances(String subdirectory) throws Exception {
		List<TextInstance> instances = new LinkedList<>();
		File dir = new File(Exercise_05_02.class.getClassLoader().getResource(subdirectory).getFile());
		for (File grp : dir.listFiles()) {
			String label = grp.getName();
			for (File textFile : grp.listFiles()) {
				String content = new String(Files.readAllBytes(textFile.toPath()));
				String[] lines = content.split("\n");
				String headerless = "";
				boolean skippingHeader = true;
				for (String line : lines) {
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
		 * @param preprocessedWords
		 *            idealized piece of text, without any punctuation and with
		 *            all words in lower case; The order of words is preserved,
		 *            there can be duplicates.
		 * @return class label (e.g. "sci.space")
		 */
		String classify(List<String> preprocessedWords);
	}

	/**
	 * Collects all words that occur in the training set.
	 *
	 * @param instances
	 *            training set of instances
	 * @return set of all words that occur in all texts
	 */
	public static Set<String> extractVocabulary(List<TextInstance> instances) {
		Set<String> s = new HashSet<>();
		instances.forEach(x -> x.getWords().forEach(y -> s.add(y)));
		return s;
	}

	/**
	 * For each possible class label <code>v</code>, estimates the probability
	 * <code>P(v)</code>.
	 *
	 * Simply count the proportion of each class label in the dataset.
	 *
	 * @param instances
	 *            training set of text instances
	 * @return map from labels to class probability estimates
	 */
	public static Map<String, Double> estimateClassProbabilities(List<TextInstance> instances) {
		Map<String, Integer> absoluts = new HashMap<>();

		int sum = 0;

		for (TextInstance inst : instances) {
			if (!absoluts.containsKey(inst.getLabel())) {
				absoluts.put(inst.getLabel(), 1);
			} else {
				absoluts.put(inst.getLabel(), absoluts.get(inst.getLabel()) + 1);
			}
			sum++;
		}

		Map<String, Double> res = new HashMap<>();

		for (Map.Entry<String, Integer> entry : absoluts.entrySet()) {
			String x = entry.getKey();
			double y = entry.getValue();
			res.put(x, y / sum);
		}

		return res;
	}

	/**
	 * For each class label <code>v</code>, and each word <code>w</code> from
	 * the vocabulary, estimates <code>P(w|v)</code>.
	 *
	 * Uses uniform distribution on the vocabulary as prior. Combines the actual
	 * count with a weighted prior, so that even words that never occur in texts
	 * of certain class get some non-zero chance to occur.
	 *
	 * @param instances
	 *            training set
	 * @param vocabulary
	 *            all words that occur in the training set
	 * @param numVirtualExamples
	 *            weight given to prior
	 * @return map that maps class labels <code>v</code> to a nested map that in
	 *         turn maps words <code>w</code> to estimated conditional
	 *         probability <code>P(w|v)</code>.
	 */
	// TODO: make vocabulary a Set<String> next time.
	public static Map<String, Map<String, Double>> estimateConditionalWordProbs(List<TextInstance> instances,
			Collection<String> vocabulary, int numVirtualExamples) {
		Map<String, Map<String, Integer>> absoluts = new HashMap<>();
		Map<String, Integer> sums = new HashMap<String, Integer>();

		// Counts Words
		for (TextInstance inst : instances) {
			Map<String, Integer> tmp;
			int sum;

			// Prepare sup Map if needed
			if (!absoluts.containsKey(inst.getLabel())) {
				tmp = new HashMap<>();
				for (String word : vocabulary) {
					tmp.put(word, 0);
				}
				sum = 0;
				absoluts.put(inst.getLabel(), tmp);
			} else {
				sum = sums.get(inst.getLabel());
				tmp = absoluts.get(inst.getLabel());
			}

			// Converts List of Words to Set of Words
			Set<String> words = new HashSet<>(inst.getWords());

			// add Words
			for (String word : words) {
				tmp.put(word, tmp.get(word) + 1);
				sum++;
			}
			sums.put(inst.getLabel(), sum);
		}

		Map<String, Map<String, Double>> result = new HashMap<>();
		double offset = (double) numVirtualExamples / vocabulary.size();

		absoluts.forEach((clazz, nested) -> {
			Map<String, Double> tmp = new HashMap<>();
			int sum = sums.get(clazz);
			nested.forEach((word, num) -> {
				tmp.put(word, (num + offset) / (sum + numVirtualExamples));
			});
			result.put(clazz, tmp);
		});

		return result;
	}

	/**
	 * Trains a naive Bayes classifier for text classification.
	 *
	 * The trained naive Bayes classifier ignores all words that do not occur in
	 * the vocabulary extracted from the training set.
	 *
	 * @param textInstances
	 *            training set with labeled preprocessed text instances
	 * @param numVirtualExamples
	 *            weight given to prior
	 * @return a trained naive Bayes text classifier
	 */
	public static NaiveBayesTextClassifier learnNaiveBayesText(List<TextInstance> textInstances,
			int numVirtualExamples) {
		return new NaiveBayesTextClassifier() {
			{
				Set<String> vocab = extractVocabulary(textInstances);
				classProbs = estimateClassProbabilities(textInstances);
				wordProbs = estimateConditionalWordProbs(textInstances, vocab, numVirtualExamples);
			}
			Map<String, Map<String, Double>> wordProbs;
			Map<String, Double> classProbs;

			public String classify(List<String> preprocessedText) {
				Set<String> words = new HashSet<>(preprocessedText);
				double max = Double.NEGATIVE_INFINITY;
				String maxClazz = null;
				for (String clazz : wordProbs.keySet()) {
					double value = 0.;
					double classProb = Math.log(classProbs.get(clazz));
					Map<String, Double> nested = wordProbs.get(clazz);
					for (String word : words) {
						Double tmp = nested.get(word);
						if (tmp != null) {
							value += Math.log(tmp);
						}
					}

					value -= classProb;
					if (value > max) {
						max = value;
						maxClazz = clazz;
					}
				}

				return maxClazz;
			}
		};
	}

	public static double evaluate(NaiveBayesTextClassifier classifier, List<TextInstance> test) {
		int trues = 0;
		for (TextInstance inst : test) {
			trues += classifier.classify(inst.getWords()).equals(inst.getLabel()) ? 1 : 0;
		}
		return (double) trues / test.size();
	}

	public static void main(String... args) throws Exception {
		long start;
		long end;
		System.out.println("[Naive Bayes] starts loading Train Set...");
		start = System.currentTimeMillis();
		List<TextInstance> train = loadTrainingSet();
		end = System.currentTimeMillis();
		System.out.println("[Naive Bayes] ends loading Test Set...\ntime: " + (end - start));

		System.out.println("[Naive Bayes] starts training Classifier...");
		start = System.currentTimeMillis();
		NaiveBayesTextClassifier classifier = learnNaiveBayesText(train, 100);
		end = System.currentTimeMillis();
		System.out.println("[Naive Bayes] ends training Classifier...\ntime: " + (end - start));

		System.out.println("[Naive Bayes] starts loading Test Set...");
		start = System.currentTimeMillis();
		List<TextInstance> test = loadTestSet();
		end = System.currentTimeMillis();
		System.out.println("[Naive Bayes] ends loading Test Set...\ntime: " + (end - start));

		System.out.println("[Naive Bayes] starts evaluating...");
		start = System.currentTimeMillis();
		double evaluation = evaluate(classifier, test);
		end = System.currentTimeMillis();
		System.out.println("[Naive Bayes] ends evaluating...\ntime: " + (end - start));

		System.out.printf("[Naive Bayes] result: %2.2f%n", evaluation * 100);

	}
}
