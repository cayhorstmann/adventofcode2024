import com.horstmann.adventofcode.*;

void parse(Path path) throws IOException {
    edges = Files.lines(path).map(l -> Set.of(l.split("-"))).map(Clique::new).collect(Collectors.toSet());
    vertices = new TreeSet<>();
    for (var c : edges) { vertices.addAll(c.vertices); }
}

static boolean adjacent(String v, String w) { return edges.contains(new Clique(Set.of(v, w))); }

record Clique(Set<String> vertices) {
    boolean hasChief() {
        return vertices.stream().anyMatch(s -> s.startsWith("t"));
    }
    
    boolean allAdjacentTo(String v) {
        return !vertices.contains(v) && vertices.stream().allMatch(w -> adjacent(v, w));
    }
    
    public String toString() { return new TreeSet<>(vertices).toString().replaceAll(" ", ""); }
}

static Set<Clique> edges;
Set<String> vertices;

Set<Clique> triangles;
Set<String> triangleVertices;

Object part1() {    
    triangles = new HashSet<>();
    triangleVertices = new TreeSet<>();
    for (var e : edges) {
        for (var w : vertices) {
            if (e.allAdjacentTo(w)) {
                var c = new Clique(Sets.union(e.vertices, Set.of(w)));
                if (c.hasChief()) {
                    triangles.add(c);
                    triangleVertices.addAll(c.vertices);
                }
            }
        }
    }
        
    return triangles.size();
}

Object part2() {
    var cliques = triangles;
    var cliqueVertices = triangleVertices;
    for (;;) {
        var biggerCliques = new HashSet<Clique>();
        var biggerCliqueVertices = new TreeSet<String>();
        for (var e : cliques) {
            for (var w : cliqueVertices) {
                if (e.allAdjacentTo(w)) {
                    var c = new Clique(Sets.union(e.vertices, Set.of(w)));
                    biggerCliques.add(c);
                    biggerCliqueVertices.addAll(c.vertices);
                }
            }
        }
        if (biggerCliques.isEmpty()) return cliques;
        else {
            cliques = biggerCliques;
            cliqueVertices = biggerCliqueVertices;
        }
    }
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