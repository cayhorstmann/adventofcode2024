import com.horstmann.adventofcode.*;

record Machine(long ax, long ay, long bx, long by, long px, long py) {
    long cost() {
        long det = ax * by - ay * bx;
        long mx = by * px - bx * py;
        long my = -ay * px + ax * py;
        if (mx % det == 0 && my % det == 0) return (3 * mx + my) / det;
        else return 0;
    }
}

List<Machine> machines;
List<Machine> machines2;

void parse(Path path) throws IOException {
    machines = new ArrayList<>();
    machines2 = new ArrayList<>();
    List<String> lines = Files.readAllLines(path);
    for (int i = 0; i < lines.size(); i += 4) {        
        var ns = Util.parseIntegers(lines.get(i) + lines.get(i + 1) + lines.get(i + 2), "\\D+");
        machines.add(new Machine(ns.get(0), ns.get(1), ns.get(2), ns.get(3), ns.get(4), ns.get(5)));
        machines2.add(new Machine(ns.get(0), ns.get(1), ns.get(2), ns.get(3), ns.get(4) + 10000000000000L, ns.get(5) + 10000000000000L));
    }
}

Object part1() {    
    return machines.stream().mapToLong(Machine::cost).sum();
}

Object part2() {
    return machines2.stream().mapToLong(Machine::cost).sum();
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