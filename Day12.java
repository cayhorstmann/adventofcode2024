import com.horstmann.adventofcode.*;

CharGrid grid;
List<Region> regions;

class Region { // Can't be a record because it needs the non-static grid from the enclosing scope
    Set<Location> plots;
    
    Region(Set<Location> plots) {
        this.plots = plots;
    }

    long price() {
        return plots.size() * perimeter();
    }
    
    long discountedPrice() {
        return plots.size() * corners();
    }
    
    long perimeter() {
        return plots.stream().mapToLong(p -> 4 - grid.sameNeighbors(p).size()).sum();
    }
    
    /*                y
     *            ↖   ↑
     *              ┌──
     *          x ← │ p
     *          
     *          p has a 90° corner in ↖ direction if p ≠ and p ≠ y
     */
    boolean hasExteriorCorner(Location p, Direction dd) {
        var c = grid.get(p);
        return c != grid.get(p.moved(dd.turn(-1))) && c != grid.get(p.moved(dd.turn(1)));        
    }
    
    /*          z    y
     *            ↖│ ↑
     *           ──┘  
     *          x  ← p
     *          
     *          p has a 270° corner in ↖ direction if p = x, and p = y, p ≠ z
     */
    boolean hasInteriorCorner(Location p, Direction dd) {
        var c = grid.get(p);
        return c == grid.get(p.moved(dd.turn(-1))) && c == grid.get(p.moved(dd.turn(1))) && c != grid.get(p.moved(dd));        
    }

    int corners(Location loc) {
        int sum = 0;
        for (var dd : Direction.DIAGONALS) {
            if (hasExteriorCorner(loc, dd)) sum++;
            if (hasInteriorCorner(loc, dd)) sum++;
        }
        return sum;
    }
    
    int corners() {
        return plots.stream().mapToInt(this::corners).sum();
    }
}

void parse(Path path) throws IOException {
    grid = CharGrid.parse(path);
    regions = Graphs.connectedComponents(grid.locations().toList(), grid::sameNeighbors).stream().map(Region::new).toList();
}

Object part1() {    
    return regions.stream().mapToLong(Region::price).sum();
}

Object part2() {
    return regions.stream().mapToLong(Region::discountedPrice).sum();
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