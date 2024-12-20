package com.horstmann.adventofcode;

public record Location(int row, int col) implements Comparable<Location> {
    public Location moved(Direction d) {
        int[] drc = d.drc();
        return movedBy(drc[0], drc[1]);
    }
    
    public Location movedBy(int dr, int dc) {
        return new Location(row + dr, col + dc);
    }
    
    public Location flipped() { return new Location(col, row); }

    public Direction to(Location p) {
        for (Direction d : Direction.values())
            if (moved(d).equals(p))
                return d;
        return null;
    }
    
    public int taxicabDistance(Location other) {
        return Math.abs(row - other.row) + Math.abs(col - other.col);
    }

    public String toString() {
        return row + "," + col;
    }
    
    public int compareTo(Location other) {
        int dr = row - other.row();
        if (dr != 0) return dr;
        else return col - other.col();
    }
}