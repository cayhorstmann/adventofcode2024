import com.horstmann.adventofcode.*;

record Level(List<Integer> values) {
    static Level parse(String line) {
        return new Level(Util.parseIntegers(line, "\\s+"));
    }

    boolean diffOk(int i, int j, int direction /* ±1 */) {
        int diff = (values.get(i) - values.get(j)) * direction;
        return diff > 0 && diff < 4;
    }
    
    boolean isSafe(int direction /* ±1 */) {
        for (int i = 1; i < values.size(); i++) {
            if (!diffOk(i, i - 1, direction)) return false; 
        }
        return true;
    }

    boolean isAlmostSafe(int direction /* ±1 */) {
        boolean skipped = false;
        for (int i = 1; i < values.size(); i++) {
            if (!diffOk(i, i - 1, direction)) { 
                if (skipped) return false;
                if (i == values.size() - 1) return true; // skip last one
                // try skipping i
                skipped = diffOk(i + 1, i - 1, direction); 
                if (!skipped) {
                    // try skipping i - 1
                    skipped = diffOk(i + 1, i, direction) && (i == 1 || diffOk(i, i - 2, direction)); 
                    if (!skipped) return false;
                }
                i++;
            }
        }
        return true;
    }
    
    boolean isSafe() { return isSafe(1) || isSafe(-1); }
    boolean isAlmostSafe() { return isAlmostSafe(1) || isAlmostSafe(-1); }
}

List<Level> levels;

void parse(Path path) throws IOException {
    levels = Files.lines(path).map(Level::parse).toList();
}

Object part1() {
    return levels.stream().filter(Level::isSafe).count();
}

Object part2() {
    return levels.stream().filter(Level::isAlmostSafe).count();
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