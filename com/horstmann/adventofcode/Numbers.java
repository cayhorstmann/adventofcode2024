package com.horstmann.adventofcode;

import java.util.BitSet;

// TODO https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm
// TODO https://en.wikipedia.org/wiki/Modular_multiplicative_inverse
// TODO https://en.wikipedia.org/wiki/Chinese_remainder_theorem

public class Numbers {

    public static int lowestSetBit(long n) {
        return BitSet.valueOf(new long[] { n }).nextSetBit(0);
    }
    
    // TODO highestSetBit

    public static int sign(long n) {
        return n == 0 ? 0 : n > 0 ? 1 : -1;
    }

    public static long pow2(int n) {
        long r = 1;
        for (int i = 1; i <= n; i++) r <<= 1;
        return r;
    }

    public static long lcm(long a, long b) {
        return a * b / gcd(a, b);
    }

    public static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            var temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
