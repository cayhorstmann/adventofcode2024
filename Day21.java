import com.horstmann.adventofcode.*;

List<String> codes;

void parse(Path path) throws IOException {
    codes = Files.readAllLines(path);
}

class Keypad {
    CharGrid grid;
    Location aim;
    Map<Character, Location> keys = new TreeMap<>();
    Map<String, Set<String>> paths = new HashMap<>(); 
        // key is source + target, value is all <>^vA paths from location of source to location of target
        // e.g. 29 -> { >^^A, ^>^A,, ^^>A } 
    
    Keypad(String layout) {
        grid = CharGrid.parse(List.of(layout.split("\n")));
        aim = grid.findFirst('A');
        for (var l : grid.locations().toList()) {
            char key = grid.get(l);
            if (key != ' ') keys.put(key, l);
        }
        
        for (var from : keys.keySet()) {
            for (var to : keys.keySet()) {
                var ft = "" + from + to;
                var floc = keys.get(from);
                var tloc = keys.get(to);
                var distance = floc.taxicabDistance(tloc);
                if (distance == 0) paths.put(ft, Set.of("A")); 
                else {
                    var r = Graphs.simplePaths(floc, tloc, l -> Sets.intersection(grid.mainNeighbors(l), keys.values()), p -> p.size() - 1 > distance);                    
                    paths.put(ft, r.stream().map(this::moves).collect(Collectors.toSet()));
                }
            }
        }
    }
    
    String moves(List<Location> path) {
        var result = new StringBuilder();
        for (int i = 0; i < path.size() - 1; i++) {
            var d = path.get(i).to(path.get(i + 1));
            result.append(d.symbol());
        }
        result.append("A");  
        return result.toString();
    }
    
    Set<String> encode(char c) { 
        var loc = grid.findFirst(c);
        if (loc.equals(aim)) return Set.of("A");
        var r = paths.get("" + grid.get(aim) + c);
        aim = loc;
        return r;
    }
    
    Map<String, Set<String>> memo = new HashMap<>();
        
    Pattern partPattern = Pattern.compile("[^A]*A");
    Set<String> encode(String s) {
        aim = grid.findFirst('A');
        var r = memo.get(s);
        if (r != null) return r;
        var parts = partPattern.matcher(s).results().map(MatchResult::group).toList();
        var partLists = new ArrayList<Set<String>>();
        for (var part : parts) {            
            r = memo.get(part);
            if (r == null) r = doEncode(part);
            memo.put(part, r);
            partLists.add(r);
        }
        r = Sets.reduceAll(partLists, String::concat);
        memo.put(s, r);
        return r;
    }
    
    Set<String> doEncode(String s) {
        var first = encode(s.charAt(0));
        if (s.length() == 1) return first;
        var rest = doEncode(s.substring(1));
        var result = new TreeSet<String>();
        for (var f : first)
            for (var r : rest)
                result.add(f + r);
        return result;
    }
    
    Set<String> shortestEncodings(Set<String> strings) {
        var result = new HashSet<String>();
        for (var s : strings) {
            result.addAll(encode(s));
        }
        var minlength = result.stream().mapToInt(String::length).min().getAsInt();
        return result.stream().filter(s -> s.length() == minlength).collect(Collectors.toSet());
    }

    // For debugging
    String decode(String keys) {
        var result = new StringBuilder();
        var loc = grid.findFirst('A');
        for (int i = 0; i < keys.length(); i++) {
            char c = keys.charAt(i);
            if (c == 'A') result.append(grid.get(loc));
            else loc = loc.moved(Direction.ofSymbol(c));
        }
        return result.toString();
    }
}

Keypad pad1 = new Keypad("""
789
456
123
 0A
""");

Keypad pad2 = new Keypad("""
 ^A
<v>
""");        

int complexity(String s, int n) {
    var s1 = pad1.shortestEncodings(Set.of(s));
    Set<String> s2 = s1;
    for (int i = 0; i < n; i++) {
        s2 = pad2.shortestEncodings(s2);
    }
    return s2.iterator().next().length();    
}

Object part1() {
    int sum = 0;
    for (var s : codes) {
        sum += Integer.parseInt(s.substring(0, s.length() - 1)) * complexity(s, 2);
    }
    return sum;
}

/*

For each level, build up a table with the cost of moving from a pad2 key to another, using another pad2 keyboard

The first table is just the taxicab metric + 1 (for pushing A).
 
    \  to
from \ A < > ^ v
     A 1 4 2 2 3   
     < 4 1 3 3 2
     > 2 3 1 3 2
     ^ 2 3 3 1 2
     v 3 2 2 2 1

The next table: 

    \  to
from \ A  <  >  ^  v     
   A   1 10  6  8  9
   <   8  1  5  7  4
   >   4  9  1  9  8
   ^   4  9  7  1  6
   v   7  8  4  4  1
     
 */

List<Map<String, Long>> costs = new ArrayList<Map<String, Long>>();

long cost(String path, int level) {
    String apath = "A" + path;
    long cost = 0;
    for (int i = 0; i < apath.length() - 1; i++) {
        String ft = apath.substring(i, i + 2);
        cost += costs.get(level).get(ft);
    }
    return cost;
}

long minCost(Set<String> paths, int level) {
    return paths.stream().mapToLong(p -> cost(p, level)).min().getAsLong();
}

void calculateMoveCosts(int levels) {
    costs = new ArrayList<Map<String, Long>>();
    for (int level = 0; level < levels; level++) {
        costs.add(new TreeMap<String, Long>());
        for (var from : pad2.keys.entrySet()) {
            for (var to : pad2.keys.entrySet()) {
                String ft = "" + from.getKey() + to.getKey();
                if (from == to) 
                    costs.get(level).put(ft, 1L);
                else if (level == 0) 
                    costs.get(level).put(ft, from.getValue().taxicabDistance(to.getValue()) + 1L); // + 1 for pressing A
                else {
                    var mincost = minCost(pad2.paths.get(ft), level - 1); 
                    costs.get(level).put(ft, mincost);
                }
            }
        }
    }
}

Object part2(int levels) {
    calculateMoveCosts(levels);
    long sum = 0;
    for (var s : codes) {
        var encodings = pad1.shortestEncodings(Set.of(s));
        var cost = minCost(encodings, levels - 1);
        sum += cost * Integer.parseInt(s.substring(0, s.length() - 1));
    }
    return sum;
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        IO.println(part1());
        IO.println(part2(2));
        parse(Util.inputPath("z"));
        IO.println(part1());        
        IO.println(part2(25));
    });
}