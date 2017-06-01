package org.kramerlab.ml17.exercise;

import java.util.*;
import java.util.stream.*;
import org.junit.*;
import org.kramerlab.ml17.teaching.HomeworkTodo;
import org.kramerlab.ml17.grading.Points;
import static org.junit.Assert.*;
import static org.kramerlab.ml17.exercise.Exercise_05_02.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

/**
 * Tests Exercise_05_02
 */
public class Exercise_05_02_Test {

  /** Helper method for building text instances */
  private static TextInstance text(String label, String... words) {
    return new TextInstance(Arrays.asList(words), label);
  }

  private static TextInstance textRaw(String label, String rawText) {
    return new TextInstance(extractWords(rawText), label);
  }

  @Points(
    points = 0.5,
    exerciseId = 2,
    subExId = 4,
    errorMessage = "vocabulary is not constructed correctly (small example)"
  )
  @Test
  public void testVocab() throws Exception {
    Set<String> v = extractVocabulary(asList(
      text("A", "a", "b"),
      text("B", "a", "c"),
      text("C", "d", "e", "f"),
      text("A", "g", "h")
    ));
    Set<String> expected = IntStream
      .range(0, 8)
      .mapToObj(i -> "" + (char) ('a' + i))
      .collect(toSet());

    assertEquals(expected, v);
  }

  @Points(
    points = 0.5,
    exerciseId = 2,
    subExId = 4,
    errorMessage = "vocabulary is not constructed correctly (large example)"
  )
  @Test
  public void testVocabRandomized() throws Exception {
    Random rnd = new Random(0);
    for (int rep = 0; rep < 10; rep++) {
      ArrayList<String> vocabGen = new ArrayList<>();
      for (int i = 0; i < 50; i++) {
         vocabGen.add("word" + i);
      }
      Set<String> used = new HashSet<String>();
      List<TextInstance> insts = new LinkedList<TextInstance>();
      int mustBeUsed = vocabGen.size();
      int needInsts = rnd.nextInt(100) + 10;
      while (used.size() < mustBeUsed || insts.size() < needInsts) {
        int numWords = rnd.nextInt(100) + 20;
        String[] words = new String[numWords];
        for (int i = 0; i < numWords; i++) {
          String chosenWord = vocabGen.get(rnd.nextInt(vocabGen.size()));
          words[i] = chosenWord;
          used.add(chosenWord);
        }
        insts.add(text("Foobar", words));
      }
      Set<String> expected = new HashSet<>();
      expected.addAll(vocabGen);

      Set<String> is = extractVocabulary(insts);
      assertEquals(expected, is);
    }
  }

  @Test
  @Points(
    points = 1,
    exerciseId = 2,
    subExId = 4,
    errorMessage = "estimating class probabilities does not work"
  )
  public void testEstimateClassProbSmall() {
    List<TextInstance> insts = asList(
      text("A", /* meaningless from here on -> */ "A", "B", "C", "D"),
      text("C", /* meaningless from here on -> */ "A", "B", "C", "D"),
      text("B", /* meaningless from here on -> */ "A", "B", "C", "D"),
      text("A", /* meaningless from here on -> */ "A", "B", "C", "D"),
      text("A", /* meaningless from here on -> */ "A", "B", "C", "D"),
      text("B", /* meaningless from here on -> */ "A", "B", "C", "D"),
      text("A", /* meaningless from here on -> */ "A", "B", "C", "D"),
      text("D", /* meaningless from here on -> */ "A", "B", "C", "D")
    );
    Map<String, Double> est = estimateClassProbabilities(insts);
    assertEquals(est.get("A"), 0.5, 0.00001);
    assertEquals(est.get("B"), 0.25, 0.00001);
    assertEquals(est.get("C"), 0.125, 0.00001);
    assertEquals(est.get("D"), 0.125, 0.00001);
  }

  @Test
  @Points(
    points = 1,
    exerciseId = 2,
    subExId = 4,
    errorMessage = "estimClassProb does not produce a probability distribution"
  )
  public void testEstimateClassProbRandomized() {
    Random rnd = new Random(0);
    for (int rep = 0; rep < 20; rep++) {
      List<TextInstance> insts = new LinkedList<TextInstance>();
      int needInsts = rnd.nextInt(100) + 10;
      for (int i = 0; i < needInsts; i++) {
        insts.add(text(
          rnd.nextBoolean() ? "A" : rnd.nextBoolean() ? "B" : "C"
        ));
      }
      Map<String, Double> est = estimateClassProbabilities(insts);
      checkProbDistr(est);
    }
  }

  private static void checkProbDistr(Map<String, Double> distr) {
    double totalSum = 0.0;
    for (Map.Entry<String, Double> kv: distr.entrySet()) {
      double p = kv.getValue();
      assertTrue("probs must be non-negative", p >= 0);
      assertTrue("probs must be <= 1", p <= 1);
      totalSum += p;
    }
    assertEquals("probs must sum to 1", totalSum, 1.0, 0.00001);
  }

  @Test
  @Points(
    points = 1,
    exerciseId = 2,
    subExId = 4,
    errorMessage = "estimCondProb (small example) doesn't work"
  )
  public void testEstimateCondProb() {
    List<TextInstance> insts = asList(
      text("A", "a", "b", "c"),
      text("B", "a", "b"),
      text("A", "a", "c", "d")
    );
    List<String> vocab = asList("a", "b", "c", "d");
    int vocabSize = 4;
    double aA = 2;
    double bA = 1;
    double cA = 2;
    double dA = 1;
    double aB = 1;
    double bB = 1;
    double totalA = aA + bA + cA + dA;
    double totalB = aB + bB;

    assertEquals(aA + bA + cA + dA + aB + bB, 8, 0.0);

    double prior = 1.0 / vocabSize;
    for (int virtual = 0; virtual < 10; virtual++) {
      Map<String, Map<String, Double>> est =
        estimateConditionalWordProbs(insts, vocab, virtual);
      assertEquals(est.get("A").get("a"),
        (aA + virtual * prior) / (totalA + virtual), 0.0001);
      assertEquals(est.get("A").get("b"),
        (bA + virtual * prior) / (totalA + virtual), 0.0001);
      assertEquals(est.get("A").get("c"),
        (cA + virtual * prior) / (totalA + virtual), 0.0001);
      assertEquals(est.get("A").get("d"),
        (dA + virtual * prior) / (totalA + virtual), 0.0001);
      assertEquals(est.get("B").get("a"),
        (aB + virtual * prior) / (totalB + virtual), 0.0001);
      assertEquals(est.get("B").get("b"),
        (bB + virtual * prior) / (totalB + virtual), 0.0001);
      assertEquals(est.get("B").get("c"),
        virtual * prior / (totalB + virtual), 0.0001);
      assertEquals(est.get("B").get("d"),
        virtual * prior / (totalB + virtual), 0.0001);
    }
  }

  @Test
  @Points(
    points = 1,
    exerciseId = 2,
    subExId = 4,
    errorMessage = "estimCondProb doesn't produce probability distributions"
  )
  public void testEstimateCondProbRandomized() {
    Random rnd = new Random(0);
    for (int rep = 0; rep < 10; rep++) {

      // generate a random vocabulary
      ArrayList<String> vocabGen = new ArrayList<>();
      int vocabSize = rnd.nextInt(30) + 20;
      for (int i = 0; i < vocabSize; i++) {
         vocabGen.add("word" + i);
      }

      // ensure that all words are used in instances
      Set<String> used = new HashSet<String>();

      // generate instances (random words, random class labels)
      List<TextInstance> insts = new LinkedList<TextInstance>();
      int needInsts = rnd.nextInt(100) + 10;
      while (used.size() < vocabSize || insts.size() < needInsts) {
        int numWords = rnd.nextInt(100) + 20;
        String[] words = new String[numWords];
        for (int i = 0; i < numWords; i++) {
          String chosenWord = vocabGen.get(rnd.nextInt(vocabGen.size()));
          words[i] = chosenWord;
          used.add(chosenWord);
        }
        insts.add(text(
          rnd.nextBoolean() ? "A" : rnd.nextBoolean() ? "B" : "C",
          words
        ));
      }

      for (int virtual = 0; virtual < 10; virtual++) {
        // compute conditional probability estimations
        Map<String, Map<String, Double>> est =
          estimateConditionalWordProbs(insts, vocabGen, virtual);

        // check that they are indeed probability distributions
        for (Map.Entry<String, Map<String, Double>> kv: est.entrySet()) {
          Map<String, Double> condWordProbs = kv.getValue();
          checkProbDistr(condWordProbs);
        }
      }
    }
  }

  @Test
  @Points(
    points = 2,
    exerciseId = 2,
    subExId = 4,
    errorMessage = "final test doesn't work as expected"
  )
  public void integrationTest() throws Exception {
    NaiveBayesTextClassifier c = learnNaiveBayesText(loadTrainingSet(), 1000);
    List<TextInstance> examples = Arrays.asList(
      textRaw("sci.space",
      "  The first step in embarking on a long and"+
      " challenging journey involves laying solid groundwork for a successful"+
      " endeavor. The ISS serves as a national"+
      " laboratory for human health, biological, and materials research, as a"+
      " technology test-bed, and as a stepping stone for going further into"+
      " the solar system.  will improve"+
      " and learn new ways to ensure astronauts are safe, healthy and"+
      " productive while exploring, and we will continue expand our knowledge"+
      " about how materials and biological systems behave outside of the"+
      " influence of gravity. NASA will continue its unprecedented work with"+
      " the commercial industry and expand an entire industry as private"+
      " companies develop and operate safe, reliable and affordable commercial"+
      " systems to transport crew and cargo to and from the International"+
      " Space Station and low Earth orbit."+
      " "+
      " Why Translunar Space?"+
      " "+
      " Translunar space is vast expanse surrounding the Earth-moon system,"+
      " extending far beyond the moon’s orbit and dominated by the two bodies’"+
      " gravity fields. Exploring in translunar space, beyond the protection"+
      " of the Earth’s geomagnetic field, will provide unprecedented"+
      " experience in deep-space operations. Operating in translunar space,"+
      " NASA can research galactic cosmic radiation–potentially the most"+
      " threatening element to humans exploring deep space–and develop"+
      " mitigation strategies that may also lead to medical advancements on"+
      " Earth. The Lagrange points–places in cislunar space where the"+
      " gravitational influences of the Earth and moon cancel each other"+
      " out–are advantageous areas for exploration and research in which"+
      " almost no propulsion is required to keep an object or spacecraft"+
      " stationary. The Lagrange point on the far side of the Earth-Moon"+
      " system, called L2, also provides a “radio silence” zone for"+
      " astronomical observations. Missions to translunar space will give NASA"+
      " and its partners the opportunity to develop tools and operational"+
      " techniques to support decades of future exploration, while remaining"+
      " in relative proximity to Earth.       ),"
      ),


      textRaw("comp.sys.mac.hardware",
      "Early Macs used the built-in serial ports for LocalTalk, which set" +
      " up a fast (at the time) network between two machines." +
      " Later, a modified AUI port was added named Apple Attachment Unit " +
      " Interface to provide a more user friendly version of 10BASE2 cabling" +
      " and adapters, with Apple's version known as FriendlyNet. As this was" +
      " an implementation of an Ethernet physical layer it was indifferent to" +
      " the protocols used - and allowed connection of Macintosh machines " +
      " using LocalTalk, TCP/IP, or other protocols assuming it was " +
      " supported by programs. Eventually as Ethernet over twisted pair "+
      " emerged as the dominate method for connecting computers, " +
      " all Macs adopted the now familiar modular 8 pin modular jack as " +
      " standard. Fibre Channel adapters are also available for the Mac Pro " +
      " and the discontinued Xserve, generally for connection to large " +
      " storage subsystems and/or high bandwidth multimedia applications."
      ),

      textRaw("comp.graphics",
      " Primitive assembly is the process of" +
      " collecting a run of vertex data output from the prior stages and" +
      " composing it into a sequence of primitives. The type of primitive the" +
      " user rendered with determines how this process works." +
      " " +
      " The output of this process is an ordered sequence of simple primitives" +
      " (lines, points, or triangles). If the input is a triangle strip" +
      " primitive containing 12 vertices, for example, the output of this" +
      " process will be 10 triangles." +
      " " +
      " If tessellation or geometry shaders are active, then a limited form of" +
      " primitive assembly is executed before these Vertex Processing stages." +
      " This is used to feed those particular shader stages with individual" +
      " primitives, rather than a sequence of vertices." +
      " " +
      " The rendering pipeline can also be aborted at this stage. This allows" +
      " the use of Transform Feedback operations, without having to actually" +
      " render something. Face culling Main article: Face Culling" +
      " " +
      " Triangle primitives can be culled (ie: discarded without rendering)" +
      " based on the triangle's facing in window space. This allows you to" +
      " avoid rendering triangles facing away from the viewer. For closed" +
      " surfaces, such triangles would naturally be covered up by triangles" +
      " facing the user, so there is never any need to render them. Face" +
      " culling is a way to avoid rendering such primitives. Rasterization" +
      " Main article: Rasterization" +
      " " +
      " Primitives that reach this stage are then rasterized in the order in" +
      " which they were given. The result of rasterizing a primitive is a" +
      " sequence of Fragments." +
      " " +
      " A fragment is a set of state that is used to compute the final data" +
      " for a pixel (or sample if multisampling is enabled) in the output" +
      " framebuffer. The state for a fragment includes its position in screen-" +
      " space, the sample coverage if multisampling is enabled, and a list of" +
      " arbitrary data that was output from the previous vertex or geometry" +
      " shader." +
      " " +
      " This last set of data is computed by interpolating between the data" +
      " values in the vertices for the fragment. The style of interpolation is" +
      " defined by the shader that outputed those values."
      ),

      textRaw("rec.sport.baseball",
      " comes out of the bullpen to" +
      " relieve Marcus Stroman after a tremendous start and faces off "+
      " against Rougned Odor. Many would argue that the righty reliever "+
      " should not have been allowed to face such a tough lefty, but "+
      " John Gibbons wanted to live or die with his best pitchers and "+
      " you can't blame the man for that. Regardless, the LHH second "+
      " baseman smacked a 2-1 fastball the other way to get the inning "+
      " going. "+
      "  "+
      " 6:17: After a Chris Gimenez sac bunt moved Odor to second, Delino "+
      " DeShields makes minimal contact on a Sanchez sinker resulting in a "+
      " squibber that requires an MVP-esque play from Josh Donaldson: "+
      "  "+
      " 6:20: A moment that could have haunted Blue Jays fans for as long as "+
      " they lived unfolds with Shin-Soo Choo at the plate. With a 1-2 count, "+
      " Aaron Sanchez throws a fastball with cutting action that ends up high "+
      " for a ball. On the throw back to the mound, Russell Martin "+
      " miraculously tosses the ball off Choo's bat and down the third base "+
      " line. The fans and players remain largely unfazed by this occurrence, "+
      " but Odor sees the opportunity and takes it by running home "+
      " uncontested. What unfolds after he touches the plate is memorable, "+
      " infuriating, and embarrassing for the city of Toronto. "
      ),

      textRaw("talk.politics.guns",
      " Holding aloft one of the banned weapons--an Israeli-made Galil" +
      " rifle--Higgins pointed out some of the accouterments that led to" +
      " its prohibition: a folding stock, a night sight, a flash" +
      " suppressor on the end that could serve as a grenade launcher," +
      " its ability to accept a bayonet lug, a pistol grip and a large" +
      " magazine." +
      " In the future, he said, weapons with several of these features" +
      " probably also would be subject to an import ban." +
      " Higgins estimated that the Customs Service has seized about 20,000 of" +
      " the weapons and that those will not be allowed into the country unless" +
      " purchased by law enforcement agencies. Asked whether the government" +
      " will pay damages to importers, Higgins said, 'That's something the" +
      " court would decide.'" +
      " Three importers already have sued the government over the suspension," +
      " and although a U.S. District Court has upheld the government in one of" +
      " the cases, Higgins said the ATF would accept comments on the permanent" +
      " ban from any importers for 30 days. The final regulations would be" +
      " issued"
      ),

      textRaw("soc.religion.christian",
      " you have it in your secret windows and you're understanding to" +
      " understand it and to bring it forth it takes minute detail it takes a" +
      " holy life it takes emotions it takes dedication it takes dedication it" +
      " takes a death and only God can allow it, and you couldn't do it if" +
      " you're not the seed of God and so the path through the great corridors" +
      " these are corridors unto his perfection that is which the prophet and" +
      " the Urim and Thummim has penetrated that through this great sea of" +
      " blackness that I penetrated through these corridors and I went through" +
      " that last segment where I went through these dark serpentines I passed" +
      " through that corridor where they sat, where they are and when you" +
      " penetrate to the most high God you will believe you are mad you will" +
      " believe you've gone insane but I tell you if you follow the secret" +
      " window and you die to the ego nature you will penetrate this darkness" +
      " oh yes there's many a man or woman that's been put in the insane" +
      " asylum when this has happened to them and they're sitting there today," +
      " people think they're insane but they saw something that's real and" +
      " they see it when they're on drugs the only thing is they see it not" +
      " through the light of God, and the way I show you I show you to see it" +
      " through the light of God and the understanding of God because when you" +
      " see the face of God, you will die and there will be nothing left of" +
      " you except the God-man, the God-woman the heavenly man, the heavenly" +
      " woman the heavenly child there'll be prayer on your lips day and night" +
      " there'll be a song of jubilee waiting for your king there will be" +
      " nothing you will not be be looking for in this world except in for" +
      " your god this is all a dream a dream in death" +
      " and so I went through that window and the tower of hell and the great" +
      " serpentines of the highest order and I went through that when I showed" +
      " you chart number three"
      ),

      textRaw("rec.sport.hockey",
      " Give credit to the Spitfires for pushing back each time they" +
      " trailed. A Graham Knott power-play goal tied the score at three" +
      " heading into the second intermission. Each team potentially had" +
      " 20 minutes to win the Memorial Cup. It setup a memorable finish." +
      " Spitfires’ Aaron Luchak scored early in the third period to take a 4-3" +
      " lead. Then Windsor played defense the rest of the night hoping to keep" +
      " the Otters from tying the game. The drama in those last few minutes" +
      " had to be cut with a knife. It made for great theater, especially when" +
      " the Otters put immense pressure on the Spitfires late in the game." +
      " And then, the defining moment of the game came off the stick of" +
      " 65-goal sniper Alex DeBrincat. He beat Michael Dipietro with his shot," +
      " only to have the puck hit the inside of the far post. The Otters were" +
      " that close to tying the game. It didn’t happen. The Spitfires survived" +
      " the last onslaught and skated off their home ice as Mastercard" +
      " Memorial Cup champions."
      ),

      textRaw("talk.politics.mideast",
      "       According to Iraqis, Syrians and analysts who study the group," +
      " almost all of ISIL's leaders—including the members of its military and" +
      " security committees and the majority of its emirs and princes—are" +
      " former Iraqi military and intelligence officers, specifically former" +
      " members of Saddam Hussein's Ba'ath government who lost their jobs and" +
      " pensions in the de-Ba'athification process after that regime was" +
      " overthrown.[141][142][143] The former Chief Strategist in the Office" +
      " of the Coordinator for Counterterrorism of the US State Department," +
      " David Kilcullen, has said that 'There undeniably would be no Isis if" +
      " we had not invaded Iraq.'[144] It has been reported that Iraqis and" +
      " Syrians have been given greater precedence over other nationalities" +
      " within ISIL because the group needs the loyalties of the local Sunni" +
      " populations in both Syria and Iraq in order to be" +
      " sustainable.[145][146] Other reports, however, have indicated that" +
      " Syrians are at a disadvantage to foreign members, with some native" +
      " Syrian fighters resenting 'favouritism' allegedly shown towards" +
      " foreigners over pay and accommodation.[147][148]     "
      ),

      textRaw("sci.med",
      " said this led to the development of monoclonal antibodies that" +
      " were used in the UK to treat infected health workers returning" +
      " from Africa." +
      " 'The down side is that monoclonal antibodies require considerable" +
      " investment for scale-up and manufacture, and are expensive,'" +
      " Professor Khromykh said." +
      " Equine antibodies are a considerably cheaper alternative, with" +
      " manufacturing capacity already in place in Africa." +
      " Antibodies from vaccinated horses provide a low-cost alternative, and" +
      " are already in use for rabies, botulism and diphtheria.'" +
      " UQ School of Chemistry and Molecular Biosciences Head Professor Paul" +
      " Young, who was part of the research team, said the finding offered" +
      " great hope as a rapid treatment option for Ebola patients" +
      " 'It’s a significant advance on the way we think about responding to" +
      " urgent disease threats, and could be applied to the treatment of" +
      " other infectious diseases,' he said."
      )
    );

    HashMap<String, String> answers = new HashMap<>();

    for (TextInstance example: examples) {
      String predicted = c.classify(example.getWords());
      System.out.println(example);
      System.out.println(
        "PREDICTED: " + predicted + " ACTUAL: " + example.getLabel()
      );
      answers.put(example.getLabel(), predicted);
    }

    for (Map.Entry<String, String> kv: answers.entrySet()) {
      assertEquals(kv.getKey(), kv.getValue());
    }
  }
}
