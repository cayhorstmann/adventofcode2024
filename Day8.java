import com.horstmann.adventofcode.*;
import com.horstmann.adventofcode.CharGrid.*;
import static java.util.stream.Collectors.*;

CharGrid grid;
Map<Character, List<Location>> freqs;

void parse(Path p) throws IOException {
    grid = CharGrid.parse(p);
    freqs = grid.locations().filter(l -> grid.get(l).toString().matches("[0-9a-zA-Z]")).collect(groupingBy(grid::get));
}

Set<Location> antiNodes(Location p, Location q) {
    int dr = q.row() - p.row();
    int dc = q.col() - p.col();
    return Set.of(new Location(p.row() - dr, p.col() - dc), new Location(q.row() + dr, q.col() + dc));
}

Set<Location> antiNodes2(Location p, Location q) {
    int dr = q.row() - p.row();
    int dc = q.col() - p.col();
    var result = new HashSet<Location>();
    var r = q;
    while (grid.isValid(r)) {
        result.add(r);
        r = new Location(r.row() + dr, r.col() + dc);
    }
    r = p;
    while (grid.isValid(r)) {
        result.add(r);
        r = new Location(r.row() - dr, r.col() - dc);
    }
    return result;
}

Object part1() {
    var as = new HashSet<Location>();
    for (var s: freqs.values()) 
        for (var p : s)
            for (var q : s) 
                if (p != q) 
                    as.addAll(antiNodes(p, q));
    return as.stream().filter(grid::isValid).count();
}

Object part2() {
    var as = new HashSet<Location>();
    for (var s: freqs.values()) 
        for (var p : s)
            for (var q : s) 
                if (p != q) 
                    as.addAll(antiNodes2(p, q));
    return as.size();
}

Path path(String suffix) { return Path.of("inputs/input" + Integer.parseInt(getClass().getName().replaceAll("\\D", "")) + suffix); }

void main() throws IOException {
    long start = System.nanoTime();
    parse(path("a"));
    IO.println(part1());
    IO.println(part2());
    parse(path("b"));
    IO.println(part1());
    IO.println(part2());
    parse(path("z"));
    IO.println(part1());
    IO.println(part2());
    IO.println("%.3f sec".formatted((System.nanoTime() - start) / 1E9));
}