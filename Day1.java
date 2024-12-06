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

void main() throws IOException {
    long start = System.nanoTime();
    int day = Integer.parseInt(getClass().getName().replaceAll("\\D", ""));
    String path = "inputs/input" + day;
    parse(Path.of(path + "a"));
    IO.println(part1());
    IO.println(part2());
    parse(Path.of(path));
    IO.println(part1());
    IO.println(part2());
    IO.println("%.3f sec".formatted((System.nanoTime() - start) / 1E9));
}