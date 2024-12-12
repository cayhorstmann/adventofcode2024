import com.horstmann.adventofcode.*;

String input;

void parse(Path path) throws IOException {
    input = Files.readString(path);
}

Object part1() {
    Pattern mul = Pattern.compile("mul\\(([0-9]+),([0-9]+)\\)");
    return mul.matcher(input).results().mapToInt(r -> Integer.parseInt(r.group(1)) * Integer.parseInt(r.group(2))).sum();
}

Object part2() {
    Pattern mul = Pattern.compile("mul\\(([0-9]+),([0-9]+)\\)|do\\(\\)|don't\\(\\)");
    var results = mul.matcher(input).results().toList();
    long s = 0;
    boolean enabled = true;
    for (var r : results) {
        if (r.group().equals("do()"))
            enabled = true;
        else if (r.group().equals("don't()"))
            enabled = false;
        else if (enabled)
            s += Integer.parseInt(r.group(1)) * Integer.parseInt(r.group(2));
    }
    
    return s;
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        IO.println(part1());
        parse(Util.inputPath("b"));
        IO.println(part2());
        parse(Util.inputPath("z"));
        IO.println(part1());
        IO.println(part2());
    });
}
