val rnd = new scala.util.Random
for (i <- 0 until 100)
  printf("a%d,%d\n", rnd.nextInt(2), i)
