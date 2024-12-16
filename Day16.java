import com.horstmann.adventofcode.*;

static CharGrid grid;

void parse(Path path) throws IOException {
    grid = CharGrid.parse(path);
}

record Position(Location loc, Direction dir) {
    int cost(Position q) {
        if (q.loc == loc) {
            int t = dir.turnsTo(q.dir);
            if (t == 2 || t == 6) return 1000;
        } else if (q.loc.equals(loc.moved(dir)) && q.dir == dir)
            return 1;
        throw new IllegalArgumentException("Should not need cost from " + this + " to " + q);
    }
    Set<Position> next() {
        var result = new HashSet<Position>();
        result.add(new Position(loc, dir.turn(2)));
        result.add(new Position(loc, dir.turn(6)));
        Location n = loc.moved(dir);
        if (grid.isValid(n) && grid.get(n) != '#') result.add(new Position(n, dir));
        return result;
    }
    Set<Position> previous() {
        var result = new HashSet<Position>();
        result.add(new Position(loc, dir.turn(2)));
        result.add(new Position(loc, dir.turn(6)));
        Location n = loc.moved(dir.turn(4));
        if (grid.isValid(n) && grid.get(n) != '#') result.add(new Position(n, dir));
        return result;
    }
}

Position startPos;
Map<Position, Integer> costs;
int mincost;
List<Position> mincostPos;

Object part1() {    
    var start = grid.findFirst('S');
    var end = grid.findFirst('E');
    startPos = new Position(start, Direction.E);
    costs = Graphs.dijkstra(startPos, Position::next, Position::cost);
    mincost = costs.entrySet().stream().filter(e -> e.getKey().loc().equals(end)).mapToInt(e -> e.getValue()).min().orElse(Integer.MAX_VALUE);
    mincostPos = costs.entrySet().stream().filter(e -> e.getKey().loc().equals(end)).filter(e -> e.getValue() == mincost).map(Map.Entry::getKey).toList();
    return mincost;
}

Object part2() {
    var locs = new TreeSet<Location>();
    for (var endPos : mincostPos) {
        var paths = Graphs.simplePaths(endPos, startPos, 
                p -> p.previous().stream()
                    .filter(n -> costs.get(p) == n.cost(p) + costs.get(n))
                    .collect(Collectors.toSet()));
        for (var p : paths)
            for (var pos : p)
                locs.add(pos.loc());
    }
    return locs.size();
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        IO.println(part1());
        IO.println(part2());
        parse(Util.inputPath("b"));
        IO.println(part1());
        IO.println(part2());
        Util.logging = false;
        parse(Util.inputPath("z"));    
        IO.println(part1());
        IO.println(part2());
    });
}