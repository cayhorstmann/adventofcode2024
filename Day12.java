import java.io.IO;

import com.horstmann.adventofcode.*;

void parse(Path p) throws IOException {
}

Object part1() {
    return null;
}

Object part2() {
    return null;
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