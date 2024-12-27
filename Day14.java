import com.horstmann.adventofcode.*;

int WIDTH;
int HEIGHT;

record Robot(int sx, int sy, int vx, int vy) {}

Location move(Robot r, long t) {
    return new Location(Math.floorMod(r.sy + t * r.vy, HEIGHT), Math.floorMod(r.sx + t * r.vx, WIDTH));    
}

List<Robot> robots;

int quadrant(Location l) {
    int x = l.col();
    int y = l.row();
    if (y < HEIGHT / 2) {
        if (x < WIDTH / 2) 
            return 1;
        else if (x > WIDTH / 2)
            return 2;
    } else if (y > HEIGHT / 2) {
        if (x < WIDTH / 2) 
            return 3;
        else if (x > WIDTH / 2)
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
    for (int t = 0; t < Numbers.lcm(HEIGHT, WIDTH); t++) {
        var grid = new CharGrid(HEIGHT, WIDTH, ' '); 
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
        WIDTH = 11;
        HEIGHT = 7;
        IO.println(part1());
        IO.println(part2());
        Util.logging = false;
        parse(Util.inputPath("z"));    
        WIDTH = 101;
        HEIGHT = 103;
        IO.println(part1());
        IO.println(part2());
    });
}