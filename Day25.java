import com.horstmann.adventofcode.*;

void parse(Path path) throws IOException {
    var lines = Files.readAllLines(path);
    keys = new ArrayList<>();
    locks = new ArrayList<>();
    int i = 0;
    while (i < lines.size()) {
        var line = lines.get(i);
        int j = i + 1;
        while (j < lines.size() && !lines.get(j).isBlank()) j++;
        var grid = CharGrid.parse(lines.subList(i, j));
        var heights = new ArrayList<Integer>();
        if (line.equals("#####")) {
            for (int c = 0; c < grid.cols(); c++) {
                int r = 1; 
                while (r < grid.rows() && grid.get(new Location(r, c)) == '#') r++;
                heights.add(r - 1);
            }
            locks.add(heights);
        } else {
            for (int c = 0; c < grid.cols(); c++) {
                int r = grid.rows() - 2; 
                while (r >= 0 && grid.get(new Location(r, c)) == '#') r--;
                heights.add(grid.rows() - r - 2);
            }
            keys.add(heights);
        }        
        i = j + 1;
    }    
}

List<List<Integer>> keys;
List<List<Integer>> locks;

boolean fit(List<Integer> key, List<Integer> lock) {
    for (int i = 0; i < key.size(); i++) {
        if (key.get(i) + lock.get(i) >= 6) return false;
    }
    return true;
}

Object part1() {    
    long count = 0;
    for (var k : keys)
        for (var l : locks)
            if (fit(k, l))
                count++;
    return count;
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