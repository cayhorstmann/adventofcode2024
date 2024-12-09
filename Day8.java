import com.horstmann.adventofcode.*;

CharGrid grid;
Map<Character, List<Location>> freqs;

void parse(Path p) throws IOException {
    grid = CharGrid.parse(p);
    freqs = grid.locations().filter(l -> grid.get(l).toString().matches("[0-9a-zA-Z]")).collect(Collectors.groupingBy(grid::get));
}

Set<Location> antiNodes(Location p, Location q) {
    int dr = q.row() - p.row();
    int dc = q.col() - p.col();
    return Set.of(p.movedBy(-dr, -dc), p.movedBy(dr, dc));
}

Set<Location> antiNodes2(Location p, Location q) {
    int dr = q.row() - p.row();
    int dc = q.col() - p.col();
    var result = new HashSet<Location>();    
    for (var r = q; grid.isValid(r); r = r.movedBy(dr, dc)) result.add(r);
    for (var r = p; grid.isValid(r); r = r.movedBy(-dr, -dc)) result.add(r);
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

void main() throws IOException {
    long start = System.nanoTime();
    parse(Util.inputPath("a"));
    IO.println(part1());
    IO.println(part2());
    parse(Util.inputPath("b"));
    IO.println(part1());
    IO.println(part2());
    parse(Util.inputPath("z"));
    IO.println(part1());
    IO.println(part2());
    IO.println("%.3f sec".formatted((System.nanoTime() - start) / 1E9));
}