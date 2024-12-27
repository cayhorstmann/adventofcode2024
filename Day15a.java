/* Using bfs instead of recursion */

import com.horstmann.adventofcode.*;

CharGrid grid1;
CharGrid grid2;
String moves;
Location robot;

String widen(String s) {
    return s.chars().mapToObj(c -> switch(c) {
        case '@' -> "@.";
        case 'O' -> "[]";
        default -> "" + ((char) c) + ((char) c);
    }).collect(Collectors.joining());
}

void parse(Path path) throws IOException {
    grid1 = null;
    moves = "";
    var lines = Files.readAllLines(path);
    for (int i = 0; i < lines.size(); i++) {
        if (lines.get(i).strip().length() == 0) {
            grid1 = CharGrid.parse(lines.subList(0, i));
            grid2 = CharGrid.parse(lines.subList(0, i).stream().map(this::widen).toList());
        }
        else if (grid1 != null) moves += lines.get(i);
    }
}

void move(Direction d) {
    var toMove = Graphs.<Location>bfs(robot, l -> { // Weird--can't infer type
            var n = l.moved(d); 
            var c = grid1.get(n); 
            return c == 'O' ? Set.of(n) : Set.of(); 
        });
    doMove(grid1, toMove.sequencedKeySet().reversed(), d);
}

void move2(Direction d) {
    var toMove = Graphs.<Location>bfs(robot, l -> { 
        var n = l.moved(d); 
        var c = grid2.get(n);
        return switch (d) {
            case E, W -> switch(c) {
                case '[', ']' -> Set.of(n);
                default -> Set.of();
            };
            case N, S -> switch(c) {
                case '[' -> Set.of(n, n.moved(Direction.E));
                case ']' -> Set.of(n, n.moved(Direction.W));
                default -> Set.of();
            };
            default -> null;
        };
    });
    
    doMove(grid2, toMove.sequencedKeySet().reversed(), d);
}

void doMove(CharGrid grid, SequencedSet<Location> locations, Direction d) {
    var newLocations = locations.stream().map(l -> l.moved(d)).collect(Collectors.toSet());
    var freeLocations = grid.findAll('.').collect(Collectors.toSet());
    if (Sets.union(freeLocations, locations).containsAll(newLocations)) {
        for (var l : locations) {
            var n = l.moved(d);
            grid.put(n,  grid.get(l));
            grid.put(l, '.');
        }
        robot = robot.moved(d);
    }
}

int gps(Location loc) { return 100 * loc.row() + loc.col(); }

Object part1() {    
    robot = grid1.findFirst('@');
    moves.chars().boxed().map(Direction::ofSymbol).forEach(this::move);
    return grid1.findAll('O').mapToInt(this::gps).sum();
}

Object part2() {
    robot = grid2.findFirst('@');
    moves.chars().boxed().map(Direction::ofSymbol).forEach(this::move2);
    return grid2.findAll('[').mapToInt(this::gps).sum();
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        IO.println(part1());
        IO.println(part2());
        parse(Util.inputPath("b"));
        IO.println(part1());
        IO.println(part2());
        parse(Util.inputPath("z"));    
        IO.println(part1());
        IO.println(part2());
    });
}