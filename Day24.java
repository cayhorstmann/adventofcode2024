import com.horstmann.adventofcode.*;

void parse(Path path) throws IOException {
    values = new TreeMap<>();
    var out = new PrintWriter(path.toString().replaceAll("in", "out"));
    out.println("digraph {");
    int gates = 0;

    for (String line : Files.readAllLines(path)) {
        var parts = List.of(line.split("[:\\s]+"));
        if (parts.size() == 2) {
            values.put(parts.get(0), new Constant(parts.get(0), Integer.parseInt(parts.get(1))));
        } else if (parts.size() == 5) {
            values.put(parts.get(4), new Expression(parts.get(4), parts.get(0), parts.get(1), parts.get(2)));
            gates++;
            out.printf("\"%s\" -> \"%s\"%n", parts.get(0), parts.get(1) + gates);
            out.printf("\"%s\" -> \"%s\"%n", parts.get(2), parts.get(1) + gates);
            out.printf("\"%s\" -> \"%s\"%n", parts.get(1) + gates, parts.get(4));
        } else {
            inputSize = values.size() / 2;
        }
    }
    out.println("}");
    out.close();
    exprCache.clear();
}

static Map<String, Value> values; 
int inputSize;

// TODO Is this cache worthwhile?
static Map<String, Integer> exprCache = new TreeMap<>();

sealed interface Value {
    String output();
    int value();
}

record Constant(String output, int value) implements Value {
    public String toString() { return output; }
}

record Expression(String output, String left, String op, String right) implements Value {
    public int value() {
        var r = exprCache.get(output);
        if (r != null) return r;
        r = switch(op) {
            case "AND" -> values.get(left).value() & values.get(right).value();
            case "OR" -> values.get(left).value() | values.get(right).value();
            case "XOR" -> values.get(left).value() ^ values.get(right).value();
            default -> 0;
        };
        exprCache.put(output, r);
        return r;
    }
    public String toString() { return left + " " + op + " " + right + "→" + output; }
}

Object part1() {    
    return get("z");
}

void set(String prefix, long value) {
    exprCache.clear();
    for (int i = 0; i < inputSize; i++) {
        var key = "%s%02d".formatted(prefix, i);
        if (!values.containsKey(key)) return;
        values.put(key, new Constant(key, (int)(value & 1)));
        value >>= 1;
    }
}

long get(String prefix) {
    long result = 0;
    for (int i = inputSize; i >= 0; i--) {
        var key = "%s%02d".formatted(prefix, i);        
        var v = values.get(key);
        if (v != null)
            result = (result << 1) | v.value();            
    }
    return result;
}

int firstFailure() {
    for (int s = 0; s < inputSize - 3; s++) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                set("x", x << s);
                set("y", y << s);
                if (get("z") != get("x") + get("y")) {                    
                    int d = Numbers.lowestSetBit(get("z") ^ (get("x") + get("y")));
                    return d - 3; 
                }
            }
        }
    }
    return inputSize;
}

void swap(String output1, String output2) {
    exprCache.clear();
    var old1 = (Expression) values.remove(output1);
    var old2 = (Expression) values.remove(output2);
    values.put(output1, new Expression(output1, old2.left, old2.op, old2.right));
    values.put(output2, new Expression(output2, old1.left, old1.op, old1.right));
}

boolean trySwap(String output1, String output2, int s) {
    swap(output1, output2);
    var r = firstFailure() > s + 1; 
    swap(output2, output1);
    return r;
}

boolean okToSwap(String output1, String output2) {    
    if (output1.equals(output2)) return false;
    var o1 = values.get(output1);
    var o2 = values.get(output2);
    if (!(o1 instanceof Expression)) return false;
    if (!(o2 instanceof Expression)) return false;
    // TODO: Would it be worth caching these dependencies?
    var d1 = dependencies(output1);
    var d2 = dependencies(output2);    
    if (d1.contains(output2)) return false;
    if (d2.contains(output1)) return false;
    return true;
}

Set<String> trySwap(String output1, Set<String> outputs, int s) {
    var r = new TreeSet<String>();
    for (var output2 : outputs) {
        if (okToSwap(output1, output2) && trySwap(output1, output2, s)) r.add(output2);
    }
    return r;
}

Set<String> dependencies(String output) {
    var preds = Graphs.bfs(output, o -> {
       var v = values.get(o);
       return switch (v) {
           case Constant _ -> Set.of();
           case Expression e -> Set.of(e.left, e.right);
       };
    });
    return preds.keySet();
}

Object part2() throws IOException {
    /*
     * TODO: Automate
     * For now, comment out the swaps, observe the first failure and suggested repairs,
     * look at the dot output, find the fix that works, and add a swap instruction
     */
    swap("vcf", "z10");
    swap("fhg", "z17");
    swap("dvb", "fsq");
    swap("tnc", "z39");
    int s = firstFailure();
    if (s < inputSize) {
        var prevdeps = new TreeSet<String>();
        for (int i = 0; i < s; i++) {
            prevdeps.addAll(dependencies("z%02d".formatted(i)));        
        }
        var faildeps = Sets.difference(dependencies("z%02d".formatted(s + 3)), prevdeps);
        var rest = Sets.difference(values.keySet(), faildeps);
        rest = Sets.difference(rest, prevdeps);
        for (var o : faildeps) Util.log(s, o, trySwap(o, rest, s));
    }
    return null; // TODO return sorted list of swaps
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        IO.println(part1());
        parse(Util.inputPath("b"));
        IO.println(part1());
        parse(Util.inputPath("z"));    
        IO.println(part1());
        IO.println(part2());
    });
}