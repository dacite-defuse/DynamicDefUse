package dacite.examples.benchmarks;

/*
The MIT License (MIT)

Copyright (c) 2016 JayHorn

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

/* 2021-11-14 Taken and adjusted from https://github.com/sosy-lab/sv-benchmarks/blob/master/java/jayhorn-recursive/SatGcd/Main.java
    : LT */

import org.junit.jupiter.api.Test;

public class SatGcd {

  // Compute the greatest common denominator using Euclid's algorithm
  static int gcd(int y1, int y2) {
    if (y1 <= 0 || y2 <= 0) {
      return 0;
    }
    if (y1 == y2) {
      return y1;
    }
    if (y1 > y2) {
      return gcd(y1 - y2, y2);
    }
    return gcd(y1, y2 - y1);
  }

  @Test
  public void testGCD() {
    int i = gcd(94, 530);
    System.out.println(i);
  }
}
