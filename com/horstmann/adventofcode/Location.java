package com.horstmann.adventofcode;

public record Location(int row, int col) {
    public Location moved(Direction d) {
        int[] dxy = d.dxy();
        return movedBy(dxy[0], dxy[1]);
    }
    
    public Location movedBy(int dx, int dy) {
        return new Location(row + dx, col + dy);
    }

    public Direction to(Location p) {
        for (Direction d : Direction.values())
            if (moved(d).equals(p))
                return d;
        return null;
    }

    public String toString() {
        return row + "," + col;
    }
}