import java.util.HashMap;

import com.horstmann.adventofcode.*;

List<String> patterns = new ArrayList<>();
List<String> towels;

void parse(Path path) throws IOException {
    List<String> lines = Files.readAllLines(path);
    patterns = List.of(lines.get(0).split(", "));
    towels = lines.subList(2, lines.size());
}

boolean possible(String s) {
    return Util.memoize(() -> { 
        if (s.length() == 0) return true;
        for (var p : patterns) {
            if (s.startsWith(p) && possible(s.substring(p.length()))) return true;
        }
        return false;
    }, "possible", s);
}

long countWays(String s) {
    return Util.memoize(() -> { 
        if (s.length() == 0) return 1L;
        long count = 0;
        for (var p : patterns) {
            if (s.startsWith(p)) count += countWays(s.substring(p.length()));
        }
        return count;
    }, "countWays", s);
}

Object part1() {
    return towels.stream().filter(this::possible).count();
}

Object part2() {
    return towels.stream().mapToLong(this::countWays).sum();
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));    
        IO.println(part1());
        IO.println(part2());
        parse(Util.inputPath("z"));
        Util.resetMemo();
        IO.println(part1());
        IO.println(part2());
    });
}