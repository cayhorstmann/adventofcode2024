import com.horstmann.adventofcode.*;

CharGrid grid;
long sum1 = 0;
long sum2 = 0;

void parse(Path p) throws IOException {
    grid = CharGrid.parse(p);
    sum1 = 0;
    sum2 = 0;
}

Set<Location> neighbors(Location l) {
    return grid.mainNeighbors(l).stream().filter(n -> grid.get(n) == grid.get(l)).collect(Collectors.toSet());
}

Object part1() {
    var zeroes = grid.locations().filter(l -> grid.get(l) == '0').toList();
    var nines = grid.locations().filter(l -> grid.get(l) == '9').toList();
    for (var start : zeroes)
        for (var end : nines) {
            int count = Graphs.simplePaths(start, end, this::neighbors).size();
            if (count >= 1) sum1 ++;
            sum2 += count;
        }
    return sum1;
}

Object part2() {
    return sum2;
}

void main() throws IOException {
    long start = System.nanoTime();
    parse(Util.inputPath("a"));
    IO.println(part1());
    IO.println(part2());
    parse(Util.inputPath("z"));    
    IO.println(part1());
    IO.println(part2());
    IO.println("%.3f sec".formatted((System.nanoTime() - start) / 1E9));
}