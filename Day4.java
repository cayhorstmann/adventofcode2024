import com.horstmann.adventofcode.*;
import com.horstmann.adventofcode.CharGrid.*;
import static com.horstmann.adventofcode.CharGrid.Direction.*;

import java.io.IO;
import java.io.IOException;
import java.nio.file.Path;

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

Path path(String suffix) { return Path.of("inputs/input" + Integer.parseInt(getClass().getName().replaceAll("\\D", "")) + suffix); }

void main() throws IOException {
    long start = System.nanoTime();
    parse(path("a"));
    IO.println(part1());
    IO.println(part2());
    parse(path("z"));
    IO.println(part1());
    IO.println(part2());
    IO.println("%.3f sec".formatted((System.nanoTime() - start) / 1E9));
}