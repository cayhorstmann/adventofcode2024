import com.horstmann.adventofcode.*;

record Equation(long result, List<Long> operands) {
    static Equation parse(String line) {
        List<Long> parts = Util.parseLongs(line, ":? ");
        return new Equation(parts.get(0), Lists.withoutFirst(parts));
    }
    
    boolean hasSolution(List<LongBinaryOperator> operators) { return hasSolution(operands.get(0), 1, operators); }
    
    boolean hasSolution(long prefixValue, int k, List<LongBinaryOperator> operators) {
        if (prefixValue > result) return false;
        else if (k == operands.size()) return result == prefixValue;
        else return operators.stream().anyMatch(op -> hasSolution(op.applyAsLong(prefixValue, operands.get(k)), k + 1, operators));        
    }
}

List<Equation> equations;

void parse(Path p) throws IOException {
    equations = Files.lines(p).map(Equation::parse).toList();
}

LongBinaryOperator PLUS = Long::sum;
LongBinaryOperator TIMES = (x, y) -> x * y;
LongBinaryOperator CONCAT = (x, y) -> Long.parseLong("" + x + y);

Object part1() {
    return equations.stream().filter(e -> e.hasSolution(List.of(PLUS, TIMES))).mapToLong(Equation::result).sum();
}

Object part2() {
    return equations.stream().filter(e -> e.hasSolution(List.of(PLUS, TIMES, CONCAT))).mapToLong(Equation::result).sum();
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