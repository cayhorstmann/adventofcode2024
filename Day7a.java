/* 
 * An alternate implementation that uses depth-first search instead of explicit recursion.
 * For better pruning, the inverse operations are applied in reverse when they can be applied. 
 * For example, if the expected result is 1500 and the last operand is 42, then one can rule out the * and || operator.
 * A graph is constructed so that (r, [o_1, ..., o_n]) has as neighbors (r - o_n, [o_1,...,o_(n-1)])
 * (r / o_n, [o_1,...,o_(n-1)]) if r is divisible by o_n, and (s, [o_1,...,o_(n-1)]) if s || o_n = r.
 * If a node (r, [r]) is reached, there is a solution. One could speed this up further by terminating 
 * the search as soon as the first solution is found, but it's not worth the effort. 
 * 
 *  I don't think this solution is any better than the straighforward recursion, but I thought it is good to 
 *  practice dfs which will surely be necessary later.
 */

import java.io.IO;

import com.horstmann.adventofcode.*;

record Equation(long result, List<Long> operands) {
    static Equation parse(String line) {
        List<Long> parts = Util.parseLongs(line, ":? ");
        return new Equation(parts.get(0), Lists.withoutFirst(parts));
    }

    Set<Equation> neighbors(List<BiFunction<Long, Long, Optional<Long>>> operators) {
        if (operands.size() <= 1) return Collections.emptySet();
        else return operators.stream()
            .map(op -> op.apply(result, operands.getLast()))
            .filter(Optional::isPresent)
            .map(r -> new Equation(r.get(), Lists.withoutLast(operands)))
            .collect(Collectors.toSet());
    }
    
    boolean hasSolution(List<BiFunction<Long, Long, Optional<Long>>> operators) {
        return Graphs.dfs(this, e -> e.neighbors(operators))
                .keySet().stream().anyMatch(e -> e.operands.size() == 1 && e.result == e.operands().getFirst());
    }
}

List<Equation> equations;

void parse(Path p) throws IOException {
    equations = Files.lines(p).map(Equation::parse).toList();
}

BiFunction<Long, Long, Optional<Long>> MINUS = (x, y) -> x >= y ? Optional.of(x - y) : Optional.empty();
BiFunction<Long, Long, Optional<Long>> DIV = (x, y) -> x % y == 0 ? Optional.of(x / y) : Optional.empty();
BiFunction<Long, Long, Optional<Long>> DECAT = (x, y) -> {
    long p = (long) Math.pow(10, Math.floor(Math.log10(y)) + 1);
    return x % p == y ? Optional.of(x / p) : Optional.empty(); 
};

Object part1() {
    return equations.stream().filter(e -> e.hasSolution(List.of(MINUS, DIV))).mapToLong(Equation::result).sum();
}

Object part2() {
    return equations.stream().filter(e -> e.hasSolution(List.of(MINUS, DIV, DECAT))).mapToLong(Equation::result).sum();
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