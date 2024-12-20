import com.horstmann.adventofcode.*;

static CharGrid grid;
Location start;
Location end;
int shortestLength;
Map<Location, Integer> costsFromStart;
Map<Location, Integer> costsFromEnd;

void parse(Path path) throws IOException {
    grid = CharGrid.parse(path);
    start =  grid.findFirst('S');
    grid.put(start, '.');
    end = grid.findFirst('E');
    grid.put(end, '.');
    
    var preds = Graphs.bfs(start, grid::sameNeighbors);
    shortestLength = Graphs.path(preds, end).size() - 1;
    costsFromStart = Graphs.dijkstraCosts(start, grid::sameNeighbors, (_, _) -> 1);
    costsFromEnd = Graphs.dijkstraCosts(end, grid::sameNeighbors, (_, _) -> 1);
}

Object part1(int savings) {
    long count = 0;
    for (var s : grid.findAll('.').toList()) {
        for (var e : grid.mainNeighbors(s).stream().flatMap(n -> grid.mainNeighbors(n).stream()).filter(l -> grid.get(l) == '.').toList()) {
            if (costsFromStart.get(s) + costsFromEnd.get(e) + 2 <= shortestLength - savings) count++;
        }
    }
    return count;
}

Object part2(int savings) {
    long count = 0;
    int maxdist = 20;
    for (var s : grid.findAll('.').toList()) {
        for (int r = -maxdist; r <= maxdist; r++) {
            for (int c = -maxdist; c <= maxdist; c++) {
                var e = s.movedBy(r, c);            
                if (s.taxicabDistance(e) <= maxdist && grid.isValid(e) && grid.get(e) == '.' && !s.equals(e)) {
                    if (costsFromStart.get(s) + costsFromEnd.get(e) + s.taxicabDistance(e) <= shortestLength - savings) count++;
                }
            }
        }
    }
    return count;
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        IO.println(part1(1));
        IO.println(part2(50));
        parse(Util.inputPath("z"));    
        IO.println(part1(100));
        IO.println(part2(100));
    });
}