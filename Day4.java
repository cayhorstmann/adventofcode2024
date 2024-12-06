import com.horstmann.adventofcode.*;
import com.horstmann.adventofcode.CharGrid.*;
import static com.horstmann.adventofcode.CharGrid.Direction.*;

CharGrid grid;

void parse(Path path) throws IOException {
    grid = CharGrid.parse(path);
}

Object part1() {
    long sum = 0;
    for (var d : Direction.values())
        sum += grid.locations().filter(l -> grid.substring(l, d, 4).equals("XMAS")).count();         
    return sum;
}

boolean x_mas(Location l) {
    return List.of("MAS", "SAM").contains(grid.substring(l.next(NW), SE, 3))
        && List.of("MAS", "SAM").contains(grid.substring(l.next(NE), SW, 3));
}

Object part2() {
    return grid.locations().filter(this::x_mas).count();
}

void main() throws IOException {
    long start = System.nanoTime();
    int day = Integer.parseInt(getClass().getName().replaceAll("\\D", ""));
    String path = "inputs/input" + day;
    parse(Path.of(path + "a"));
    IO.println(part1());
    IO.println(part2());
    parse(Path.of(path));
    IO.println(part1());
    IO.println(part2());
    IO.println("%.3f sec".formatted((System.nanoTime() - start) / 1E9));
}