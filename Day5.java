record Rule(int before, int after) {}

Set<Rule> rules;
List<List<Integer>> updates;

void parse(Path path) throws IOException {
    rules = new HashSet<Rule>();
    updates = new ArrayList<List<Integer>>();
    boolean first= true;
    for (var line : Files.readAllLines(path)) {
        if (line.length() == 0) first = false;
        else {
            List<Integer> parts = Stream.of(line.split("[|,]")).map(Integer::parseInt).toList();
            if (first) rules.add(new Rule(parts.get(0), parts.get(1)));
            else updates.add(parts);
        }
    }
}

boolean inOrder(int a, int b) { return a == b || !rules.contains(new Rule(b, a)); }

boolean inOrder(List<Integer> pages) {
    for (int i = 0; i < pages.size() - 1; i++) {
        int p = pages.get(i);
        // Because inOrder is transitive for all inputs, the following  loop could 
        // be replaced by if (!inOrder(p, pages.get(i + 1))) return false;
        for (int j = i + 1; j < pages.size(); j++) {
            int q = pages.get(j);
            if (!inOrder(p, q)) return false;
        }
    }
    return true;
}

int middle(List<Integer> is) { return is.get(is.size() / 2); }

boolean checkTransitive(List<Integer> is) {
    for (var a : is)
        for (var b : is)
            for (var c : is)
                if (inOrder(a, b) && inOrder(b, c) && !inOrder(a, c)) return false;
    return true;
}

Object part1() throws IOException {
    return updates.stream().filter(this::inOrder).mapToInt(this::middle).sum();
}

Object part2() throws IOException {
    long sum = 0;
    for (var u : updates) {
        // I was worried whether the sorting could throw an exception due to a bad comparator
        // https://horstmann.com/unblog/2022-07-25/index.html
        if (!checkTransitive(u)) IO.println(u); // Never happened for my input
        if (!inOrder(u)) {
            u = u.stream().sorted((p, q) -> p == q ?  0 : inOrder(p, q) ? -1 : 1).toList();
            sum += middle(u); 
        }
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