/* Using DFS for practice */

import com.horstmann.adventofcode.*;

CharGrid grid;
long sum1 = 0;
long sum2 = 0;

void parse(Path p) throws IOException {
    grid = CharGrid.parse(p);
    sum1 = 0;
    sum2 = 0;
}

Set<Location> neighbors(Location loc) {
    return grid.mainNeighbors(loc).stream().filter(n -> grid.get(n) == grid.get(loc) + 1).collect(Collectors.toSet());
}

Object part1() {
    var zeroes = grid.locations().filter(l -> grid.get(l) == '0').toList();
    for (var start : zeroes) {
        var nines = new HashSet<Location>();
        Graphs.bfs(start, this::neighbors, (l, _) -> {
            if (grid.get(l) == '9') {
                if (nines.add(l)) sum1++;
                sum2++;
                return false;
            } else return true;
            });
    }
    return sum1;
}

Object part2() {
    return sum2;
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        IO.println(part1());
        IO.println(part2());
    
        Files.writeString(Path.of("/tmp/foo.dot"), Graphs.dot(grid.findFirst('0'), this::neighbors, (_, _) -> null));
        
        parse(Util.inputPath("z"));    
        IO.println(part1());
        IO.println(part2());
    });
}