// Look at Day15a for a prettier, but slower, solution without recursion.
// It uses bfs to identify the set of locations to be moved

import com.horstmann.adventofcode.*;

CharGrid grid;
CharGrid grid2;
String moves;

String widen(String s) {
    return s.chars().mapToObj(c -> switch(c) {
        case '@' -> "@.";
        case 'O' -> "[]";
        default -> "" + ((char) c) + ((char) c);
    }).collect(Collectors.joining());
}

void parse(Path path) throws IOException {
    grid = null;
    moves = "";
    var lines = Files.readAllLines(path);
    for (int i = 0; i < lines.size(); i++) {
        if (lines.get(i).strip().length() == 0) {
            grid = CharGrid.parse(lines.subList(0, i));
            grid2 = CharGrid.parse(lines.subList(0, i).stream().map(this::widen).toList());
        }
        else if (grid != null) moves += lines.get(i);
    }
}

Location move(Location p, Direction d) {
    Location n = p.moved(d);
    if (!grid.isValid(n)) return p;
    char c = grid.get(n);
    if (c == 'O') move(n, d);
    if (c == '.') {
        grid.put(n, grid.get(p));
        grid.put(p, '.');
        return n;
    } else {
        return p;
    }
}

Location move2(Location p, Direction d) {
    if (canMove(p, d)) {
        doMove(p, d);
        return p.moved(d);
    } else return p;
}

boolean canMove(Location p, Direction d) {
    Location n = p.moved(d);
    if (!grid2.isValid(n)) return false;
    char c = grid2.get(n); 
    if (d == Direction.W || d == Direction.E) {
        if (c == '[' || c == ']') return canMove(n, d);
        else return grid2.get(n) == '.';
    }
    else {
        if (c == '[') return canMove(n, d) && canMove(n.moved(Direction.E), d);
        else if (c == ']') return canMove(n, d) && canMove(n.moved(Direction.W), d);
        else return c == '.';
    }
}

void doMove(Location p, Direction d) {
    Location n = p.moved(d);
    char c = grid2.get(n); 
    if (d == Direction.W || d == Direction.E) {
        if (c == '[' || c == ']') doMove(n, d);
    }
    else {
        if (c == '[') {
            doMove(n, d); doMove(n.moved(Direction.E), d);
        } else if (c == ']') {
            doMove(n, d); doMove(n.moved(Direction.W), d);
        } 
    }
    grid2.put(n, grid2.get(p));
    grid2.put(p, '.');    
}

int gps(Location loc) { return 100 * loc.row() + loc.col(); }

Location robot; // Otherwise can't assign in forEach--don't code like that at home!

Object part1() {    
    robot = grid.findFirst('@');
    moves.chars().forEach(c -> { robot = move(robot, Direction.ofSymbol(c)); }); 
    return grid.findAll('O').mapToInt(this::gps).sum();
}

Object part2() {
    robot = grid2.findFirst('@');
    moves.chars().forEach(c -> { robot = move2(robot, Direction.ofSymbol(c)); });
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