import com.horstmann.adventofcode.*;
import static com.horstmann.adventofcode.Direction.*;

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
    return List.of("MAS", "SAM").contains(grid.substring(l.moved(NW), SE, 3))
        && List.of("MAS", "SAM").contains(grid.substring(l.moved(NE), SW, 3));
}

Object part2() {
    return grid.locations().filter(this::x_mas).count();
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