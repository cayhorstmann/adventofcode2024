import com.horstmann.adventofcode.*;

CharGrid grid;
Location start;
Location end;
int shortestLength;
Map<Location, Integer> costsFromStart;
Map<Location, Integer> costsFromEnd;

void parse(Path path) throws IOException {
    grid = CharGrid.parse(path);
    start = grid.findFirst('S');
    grid.put(start, '.');
    end = grid.findFirst('E');
    grid.put(end, '.');
    
    costsFromStart = Graphs.dijkstraCosts(start, grid::sameNeighbors, (_, _) -> 1);
    costsFromEnd = Graphs.dijkstraCosts(end, grid::sameNeighbors, (_, _) -> 1);
    shortestLength = costsFromStart.get(end);
}

Object part1(int savings, int maxdist) {
    long count = 0;
    for (var s : grid.findAll('.').toList()) {
        for (var e : grid.taxicabDisc(s, maxdist)) {
            if (grid.get(e) == '.' && costsFromStart.get(s) + costsFromEnd.get(e) + s.taxicabDistance(e) <= shortestLength - savings) 
                count++;
        }
    }
    return count;
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        IO.println(part1(1, 2));
        IO.println(part1(50, 20));
        parse(Util.inputPath("z"));    
        IO.println(part1(100, 2));
        IO.println(part1(100, 20));
    });
}