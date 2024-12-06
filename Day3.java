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
    long start = System.nanoTime();
    int day = Integer.parseInt(getClass().getName().replaceAll("\\D", ""));
    String path = "inputs/input" + day;
    parse(Path.of(path + "a"));
    IO.println(part1());
    parse(Path.of(path + "b"));
    IO.println(part2());
    parse(Path.of(path));
    IO.println(part1());
    IO.println(part2());
    IO.println("%.3f sec".formatted((System.nanoTime() - start) / 1E9));
}
