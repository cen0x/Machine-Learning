val rnd = new scala.util.Random
for (i <- 0 until 1000) 
  printf("a%d,c%d\n", rnd.nextInt(2), rnd.nextInt(2))
