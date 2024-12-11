import com.horstmann.adventofcode.*;

List<Long> stones;

void parse(Path p) throws IOException {
    stones = Util.parseLongs(Files.readString(p), "\\s+");
}

List<Long> blink(List<Long> stones) {
    var result = new ArrayList<Long>();
    for (var s : stones) {
        if (s == 0) result.add(1L);
        else {
            var digits = "" + s;
            int n = digits.length();
            if (n % 2 == 0) {
                result.add(Long.parseLong(digits.substring(0, n / 2)));
                result.add(Long.parseLong(digits.substring(n / 2)));
            }
            else
                result.add(s * 2024);
        }
    }
    return result;
}

Object part1() {
    var result = stones;
    for (int i = 0; i < 25; i++)
        result = blink(result);
    return result.size();
}

long b(long n, long s) {
    return Util.memoize(() -> {
        if (n == 0) return 1L;
        else if (s == 0) return b(n - 1, 1);
        else {
            var digits = "" + s;
            int d = digits.length();
            if (d % 2 == 0) {
                var s1 = Long.parseLong(digits.substring(0, d / 2));
                var s2 = Long.parseLong(digits.substring(d / 2));
                return b(n - 1, s1) + b(n - 1, s2);
            }
            else return b(n - 1, s * 2024);        
        }
    }, n, s);
}

Object part2() {
    return stones.stream().mapToLong(x -> x).map(s -> b(75, s)).sum();
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        IO.println(part1());
        IO.println(part2());
        parse(Util.inputPath("z"));    
        IO.println(part1());
        IO.println(part2());
    });
}