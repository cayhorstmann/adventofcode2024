package com.horstmann.adventofcode;

/**
 * A location in a grid. 
 */
public record Location(int row, int col) implements Comparable<Location> {
    /**
     * Yields the adjacent location in the given direction from this location.
     * @param d a direction
     * @return the location that is next visited when traveling in direction d 
     */
    public Location moved(Direction d) {
        int[] drc = d.drc();
        return movedBy(drc[0], drc[1]);
    }
    
    /**
     * Yields the direction between two adjacent locations
     * @param p the other location
     * @return the direction between this location and p, or null if p is not adjacent
     */
    public Direction to(Location p) {
        for (Direction d : Direction.values())
            if (moved(d).equals(p))
                return d;
        return null;
    }
    
    /**
     * Yields the location that is obtained by traveling a given number of rows and columns
     * @param dr the number of rows to travel (negative for traveling north)
     * @param dc the number of columns to travel (negative for traveling west)
     * @return the location that is dr rows and dc columns away from this location
     */
    public Location movedBy(int dr, int dc) {
        return new Location(row + dr, col + dc);
    }
    
    /**
     * Computes the number of N E S W moves to reach another location from this location. 
     * @param other the other location
     * @return the number of moves
     */
    public int taxicabDistance(Location other) {
        return Math.abs(row - other.row) + Math.abs(col - other.col);
    }
    
    /**
     * Yields the location with row and column values flipped.
     * @return the flipped location
     */
    public Location flipped() { 
        return new Location(col, row); 
    }

    /**
     * Returns a compact representation for this location.
     */
    public String toString() {
        return row + "," + col;
    }

    /**
     * Compares two locations in row-major order.
     */
    public int compareTo(Location other) {
        int dr = row - other.row();
        if (dr != 0) return dr;
        else return col - other.col();
    }
}