import com.horstmann.adventofcode.*;

long ra;
long rb;
long rc;
long a;
long b;
long c;
int ip = 0;
List<Integer> instructions;
long output;

void parse(Path path) throws IOException {
    var inputs = Files.lines(path).map(Util::parseIntegers).toList();
    ra = inputs.get(0).get(0);
    rb = inputs.get(1).get(0);
    rc = inputs.get(2).get(0);
    instructions = inputs.get(4);
}

long combo(int operand) {
    return switch (operand) {
        case 4 -> a;
        case 5 -> b;
        case 6 -> c;
        case 7 -> throw new IllegalArgumentException();
        default -> operand;
    };
}

void step() {
    var operand = instructions.get(ip + 1);
    switch (instructions.get(ip)) {
        case 0 -> { a = a >> combo(operand); } // adv
        case 1 -> { b = b ^ operand; } // bxl
        case 2 -> { b = combo(operand) % 8; } // bst
        case 3 -> { if (a != 0) ip = operand - 2; } // jnz
        case 4 -> { b = b ^ c; } // bxc
        case 5 -> { output = output << 3 | combo(operand) % 8; } // out
        case 6 -> { b = a >> combo(operand); } // bdv
        case 7 -> { c = a >> combo(operand); } // cdv
        default -> {}
    }
    ip += 2;
}

long run(long ra) {
    a = ra;
    b = rb;
    c = rc;
    ip = 0;
    output = 0;
    while (ip < instructions.size()) step();
    return output;
}

Object part1() {    
    return Long.toOctalString(run(ra)).chars().mapToObj(c -> "" + (char) c).collect(Collectors.joining(","));    
}

int topOctal(long n) {
    while (n >= 8) n >>= 3;
    return (int) n;
}

/*
 The samples and my problem instance all had the structure
 
 Setting b and c to shifts and xors involving a, b, and c (with b and c initially 0)
 Shifting a to the right
 One output
 Jump to the top if a not zero
 
 Consider a single iteration of the loop to be executed.
  
 out only reports the last 3 bits of some computation. If they are obtained from xor, only the last 3 bits matter.
 If they are obtained from right shift, find the largest possible shift s. (In my problem instance, s was 7.) 
 Only the last 3 + s bits matter. For each possible loop output from 0 ... 7, compute the inverse set, 
 i.e. all (s + 3) bit values that can be transformed into the desired output.    
 
 To get the initial target digit t_0, the solution must end in an element of inverse(t_0). 
 To get the next target digit t_1, an element of inverse(t_1) must precede it by 3 bits:
 
      XXXXXXX...     in inverse(t_0)
   ...YYYYYYY        in inverse(t_1)
      <- s ->
   
 The overlap of XXXXXXX and YYYYYYY must be compatible. Different tests are possible, see below for 
 one that works well.
 
 Keep adding matching inverses:
 
      XXXXXXX.........     previous
   ...YYYYYYY              in inverse(t_l)
             <- 3*l ->    

 */

boolean isCompatible(long oldBits, long newBits) {
    return (oldBits & 077) == (newBits & 077);
}

/*
 * @param s the max number of bits by which a is shifted to the right in one iteration of the loop   
 */
Object part2(int s) {
    int n = instructions.size(); 
    long lastSMask = Util.pow2(s) - 1;
    var inverses = LongStream.range(0, Util.pow2(s + 3)).boxed().collect(Collectors.groupingBy(x -> topOctal(run(x))));
    record Vertex(int level, long value) {} //level is index of last matched target digit
    var root = new Vertex(-1, 0);    

    var preds = Graphs.bfs(root, v -> {
        if (v == root) { 
            return inverses.get(instructions.get(0))
                    .stream()
                    .map(i -> new Vertex(0, i)) 
                    .collect(Collectors.toSet()); 
        }
        else if (v.level < instructions.size() - 1) { 
            var l = v.level + 1;
            return inverses.get(instructions.get(l))
                    .stream()
                    .filter(i -> isCompatible(v.value >> 3 * l, i & lastSMask))
                    .map(i -> new Vertex(l, (i << 3 * l) | v.value & (Util.pow2(3 * l) - 1)))
                    .collect(Collectors.toSet()); 
            }
        else
            return Collections.emptySet();
    });
    long target = Long.parseLong(instructions.toString().replaceAll("[^0-7]", ""), 8);
    return preds.keySet().stream().mapToLong(Vertex::value).filter(v -> run(v) == target).min().orElse(-1);
}

void main() throws IOException {
    Util.time(() -> {
        parse(Util.inputPath("a"));
        IO.println(part1());
        IO.println(part2(1));
        parse(Util.inputPath("b"));
        IO.println(part1());
        IO.println(part2(3));
        parse(Util.inputPath("z"));    
        IO.println(part1());
        IO.println(part2(7));
   });        
}