import com.horstmann.adventofcode.*;

int MAXX;
int MAXY;

record Robot(int sx, int sy, int vx, int vy) {}

Location move(Robot r, long t) {
    return new Location(Math.floorMod(r.sy + t * r.vy, MAXY), Math.floorMod(r.sx + t * r.vx, MAXX));    
}

List<Robot> robots;

int quadrant(Location l) {
    int x = l.col();
    int y = l.row();
    if (x < MAXX / 2) {
        if (y < MAXY / 2)
            return 1;
        else if (y > MAXY / 2)
            return 3;
    } else if (x > MAXX / 2) {
        if (y < MAXY / 2)
            return 2;
        else if (y > MAXY / 2)
            return 4;            
    }
    return 0;
}

void parse(Path path) throws IOException {
    robots = new ArrayList<>();
    var lines = Files.readAllLines(path);
    for (var line : lines) {
        var ns = Util.parseIntegers(line);
        robots.add(new Robot(ns.get(0), ns.get(1), ns.get(2), ns.get(3)));
    }
}


Object part1() {
    if (Util.logging) {
        var grid = new CharGrid(MAXY, MAXX, ' ');
        for (var r : robots) {
            var p = move(r, 100);
            Util.log(p);
            grid.put(p, grid.get(p) == ' ' ? '1' : (char)(grid.get(p) + 1));
        }
        Util.log(grid);
    }

    
    var cs = robots.stream().map(r -> move(r, 100)).map(this::quadrant).collect(Collectors.groupingBy(s -> s, Collectors.counting()));
    return cs.getOrDefault(1, 0L) * cs.getOrDefault(2, 0L) * cs.getOrDefault(3, 0L) * cs.getOrDefault(4, 0L);
}

boolean hasClump(CharGrid g, int cutoff) {
    var locs = g.findAll('*').toList();
    var components = Graphs.connectedComponents(locs, g::sameNeighbors);
    var largestClumpSize = components.stream().mapToInt(c -> c.size()).max().getAsInt();
    return largestClumpSize >= cutoff;
}

Object part2() {
    for (int t = 0; t < Util.lcm(MAXX, MAXY); t++) {
        var grid = new CharGrid(MAXX, MAXY, ' '); // TODO: Why not MAXY, MAXX?
        for (var r : robots) {
            var p = move(r, t);
            grid.put(p, '*');
        }
        if (hasClump(grid, 20)) {            
            IO.println(grid);
            return t;
        }        
    }
    return null;
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        MAXX = 11;
        MAXY = 7;
        IO.println(part1()); // TODO Why 12 in the example?
        IO.println(part2());
        Util.logging = false;
        parse(Util.inputPath("z"));    
        MAXX = 101;
        MAXY = 103;
        IO.println(part1());
        IO.println(part2());
    });
}