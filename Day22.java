import java.util.Arrays;

import com.horstmann.adventofcode.*;

List<Long> secretNumbers;
List<Buyer> buyers;

void parse(Path path) throws IOException {
    secretNumbers = Files.lines(path).map(Long::parseLong).toList();
    buyers = secretNumbers.stream().map(Buyer::new).toList();
}

long mix(long x, long y) { return x ^ y; }
long prune(long x) { return x % 16777216; }

long evolve(long x) {
    x = prune(mix(x * 64, x));
    x = prune(mix(x / 32, x));
    x = prune(mix(x * 2048, x));
    return x;
}

long evolve(long x, int n) {
    for (int i = 0; i < n; i++)
        x = evolve(x);
    return x;
}

class Buyer {
    Map<Sequence, Integer> bananas = new HashMap<>();
    Buyer(long seed) {
        List<Integer> prices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            prices.add((int) (seed % 10));
            seed = evolve(seed);
        }
        for (int i = 4; i <= 2000; i++) {
            prices.add((int) (seed % 10));
            seed = evolve(seed);
            
            Sequence s = new Sequence(prices.get(1) - prices.get(0),
                    prices.get(2) - prices.get(1),
                    prices.get(3) - prices.get(2),
                    prices.get(4) - prices.get(3));
            if (!bananas.containsKey(s))
                bananas.put(s, prices.get(4));
            prices.remove(0);
        }
    }
    int bananas(Sequence s) {
        Integer r = bananas.get(s);
        return r == null ? 0 : r;
    }
}

record Sequence(int d1, int d2, int d3, int d4) {
    long value(List<Buyer> buyers) {
        return buyers.stream().mapToInt(b -> b.bananas(this)).sum();
    }
}

Object part1() {
    long sum = 0;
    for (long s : secretNumbers) {
        long r = evolve(s, 2000);
        sum += r;
    }
    return sum;
}

Object part2() {
    Sequence best = null;
    long bestValue = 0;
    for (int a = -9; a <= 9; a++)
        for (int b = -9; b <= 9; b++)
            for (int c = -9; c <= 9; c++)
                for (int d = -9; d <= 9; d++) {
                    var s = new Sequence(a, b, c, d);
                    long value = s.value(buyers);
                    if (value > bestValue) {
                        best = s;
                        bestValue = value;
                    }
                }
    var finalBest = best;
    return buyers.stream().mapToInt(b -> b.bananas(finalBest)).sum();
}

void test() {
    var b = new Buyer(123);
    // var s = new Sequence(new int[] { -2,1,-1,3 });
    var s = new Sequence(-1, -1, 0, 2);
    Util.log(b.bananas(s));
    System.exit(0);
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        IO.println(part1());
        IO.println(part2());
        parse(Util.inputPath("b"));
        IO.println(part1());
        IO.println(part2());
        parse(Util.inputPath("z"));    
        IO.println(part1());
        IO.println(part2());
    });
}