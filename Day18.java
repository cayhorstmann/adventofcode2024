import com.horstmann.adventofcode.*;

List<Location> locs;
CharGrid grid;

void parse(Path path) throws IOException {
    locs = Files.lines(path).map(line -> Util.parseIntegers(line)).map(is -> new Location(is.get(1), is.get(0))).toList();
}

Object part1(int xmax, int bytes) {    
    grid = new CharGrid(xmax + 1, xmax + 1, '.');
    for (var loc : locs.subList(0, bytes)) grid.put(loc, '#');
    var p = Graphs.bfs(new Location(0, 0), grid::sameNeighbors);
    return Graphs.path(p, new Location(xmax, xmax)).size() - 1;
    // No need to use Dijkstra if all path lengths are 1
}

Object part2(int xmax, int bytes) {    
    // Could have used binary search if this had been too slow
    for (int i = bytes + 1; i < locs.size(); i++) {
        grid = new CharGrid(xmax + 1, xmax + 1, '.');
        for (var loc : locs.subList(0, i)) grid.put(loc, '#');        
        var r = Graphs.bfs(new Location(0, 0), grid::sameNeighbors);
        if (r.get(new Location(xmax, xmax)) == null) return locs.get(i - 1).flipped();
    }
    return null;
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        IO.println(part1(6, 12));
        IO.println(part2(6, 12));
        parse(Util.inputPath("z"));    
        IO.println(part1(70, 1024));
        IO.println(part2(70, 1024));
    });
}