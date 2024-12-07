record Equation(long result, List<Long> operands) {
    static Equation parse(String line) {
        List<Long> parts = Stream.of(line.split(":? ")).map(Long::parseLong).toList();
        return new Equation(parts.get(0), parts.subList(1, parts.size()));
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

Path path(String suffix) { return Path.of("inputs/input" + Integer.parseInt(getClass().getName().replaceAll("\\D", "")) + suffix); }

void main() throws IOException {
    long start = System.nanoTime();
    parse(path("a"));
    IO.println(part1());
    IO.println(part2());
    parse(path("z"));
    IO.println(part1());
    IO.println(part2());
    IO.println("%.3f sec".formatted((System.nanoTime() - start) / 1E9));
}