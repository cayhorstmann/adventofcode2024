import com.horstmann.adventofcode.*;
import static com.horstmann.adventofcode.Direction.*;

CharGrid grid;

void parse(Path p) throws IOException {
    grid = CharGrid.parse(p);
}

Set<Location> escape() {
    var locations = new HashSet<Location>();
    record Arrow(Location l, Direction d) {}
    var arrows = new HashSet<Arrow>();
    
    var p = grid.findFirst('^');
    var d = N;
    arrows.add(new Arrow(p, d));
    for (;;) {
        var next = p.moved(d);
        var c = grid.get(next); 
        if (c == null) {
            return locations;            
        } else {
            if (c != '.' && c != '^') {
                d = d.turn(2);
            } else {
                p = next;
                locations.add(p);
            }
            if (!arrows.add(new Arrow(p, d))) return null;
        }
    }    
}

Object part1() {
    return escape().size();
}

Object part2() {
    var locations = escape();
    int count = 0;
    for (var p : locations) {
        if (grid.get(p) == '.') {
            var old = grid.put(p, 'O');
            if (escape() == null) count++;
            grid.put(p, old);
        } 
    }
    return count;
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        IO.println(part1());
        IO.println(part2());
        parse(Util.inputPath("z"));
        IO.println(part1());
        IO.println(part2());
    });
}