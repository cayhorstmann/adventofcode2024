import java.nio.file.Path;

List<Integer> left = new ArrayList<>();
List<Integer> right = new ArrayList<>();

void parse(Path path) throws IOException {
    var in = new Scanner(path);
    while (in.hasNextInt()) {
        left.add(in.nextInt());
        right.add(in.nextInt());
    }
}

Object part1() {
    Collections.sort(left);
    Collections.sort(right);
    long diff = 0;
    for (int i = 0; i < left.size(); i++)
        diff += Math.abs(left.get(i) - right.get(i));
    return diff;
}

Object part2() {
    long sum = 0;
    for (var l : left) {
        var count = right.stream().filter(l::equals).count();
        sum += l * count;
    }
    return sum;
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