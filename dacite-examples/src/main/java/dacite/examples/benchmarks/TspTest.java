package dacite.examples.benchmarks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TspTest {

  @Test
  public void testTsp() {
    int D[][] = new int[4][4];
    int k = 0;
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        D[i][j] = k;
        k++;
      }
    }

    TSP tspSolver = new TSP(4, D);
    int sln = tspSolver.solve();
    assertEquals(30, sln);
  }
}
