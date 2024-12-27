package com.horstmann.adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A bounded grid of char values, indexed by Location.
 * CAUTION: In (x, y) coordinates, x is the column and y the row, i.e. use new Location(y, x) 
 * TODO: Maybe make this into a grid of code points in 2025 to avoid (char) casts?
 */
public class CharGrid {
    private char[][] grid;

    private CharGrid() {}
    
    /**
     * Constructs a grid with given dimensions and initial char value.
     * @param rows the number of rows
     * @param cols the number of columns
     * @param c the initial value for all elements
     */
    public CharGrid(int rows, int cols, char c) {
        grid = new char[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grid[i][j] = c;
    }
    
    /**
     * Parses a CharGrid in the standard AoC format.
     * @param p the path to the file
     * @return the grid
     * @throws IOException when the file cannot be read
     */
    public static CharGrid parse(Path p) throws IOException {
        var result = new CharGrid();
        result.grid = Files.lines(p).map(String::toCharArray).toArray(char[][]::new);
        return result;
    }

    /**
     * Parses a CharGrid in the standard AoC format.
     * @param lines the lines containing the grid
     * @return the grid
     * @throws IOException when the file cannot be read
     */
    public static CharGrid parse(List<String> lines) {
        var result = new CharGrid();
        result.grid = lines.stream().map(String::toCharArray).toArray(char[][]::new);
        return result;
    }

    public int rows() {
        return grid.length;
    }

    public int cols() {
        return grid[0].length;
    }
    
    /**
     * Checks if the given location is a valid key in this grid.
     * @param p a (row, col) location
     * @return true if the location is valid
     */
    public boolean isValid(Location p) {
        return p.row() >= 0 && p.row() < grid.length && p.col() >= 0 && p.col() < grid[0].length;
    }

    public Stream<Location> locations() {
        return Stream.iterate(
            new Location(0, 0),
            this::isValid, 
            l -> l.col() < cols() - 1 ? new Location(l.row(), l.col() + 1) : new Location(l.row() + 1, 0));
    }
    
    /**
     * Gets the element at a location.
     * @param p the (row, col) location
     * @return the element or null if the location is not valid
     */
    public Character get(Location p) {
        if (isValid(p))
            return grid[p.row()][p.col()];
        else 
            return null;
    }
    
    /**
     * Sets the element at a location. 
     * @param p the (row, col) location
     * @param c the new element
     * @return the old element
     */
    public Character put(Location p, char c) {
        if (isValid(p)) {
            char old = grid[p.row()][p.col()];
            grid[p.row()][p.col()] = c;
            return old;
        } else
            return null;
    }
    
    /**
     * Gets a string of adjacent locations.
     * @param p the starting location
     * @param d the direction of travel
     * @param length the number of characters 
     * @return the string of elements at valid locations  
     */
    public String substring(Location p, Direction d, int length) {
        var result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (isValid(p)) result.append(grid[p.row()][p.col()]);
            p = p.moved(d);
        }
        
        return result.toString();
    }

    /**
     * Gets valid neighbor locations in the N E S W compass directions.
     * @param p a location (need not be valid)
     * @return the valid neighbor locations in the main compass directions
     */
    public Set<Location> mainNeighbors(Location p) {
        var r = new HashSet<Location>();
        for (var d : Direction.MAIN_DIRECTIONS) {
            Location n = p.moved(d);
            if (isValid(n))
                r.add(n);
        }
        return r;
    }

    /**
     * Gets the locations for neighbors in the N E S W compass directions with the same contents
     * as the element in the current location.
     * NOTE: This is useful for maze traversal and flood filling.
     * @param p a valid location
     * @return the valid neighbor locations in the main compass directions with the same contents. 
     */
    public Set<Location> sameNeighbors(Location p) {
        var r = new HashSet<Location>();
        for (var d : Direction.MAIN_DIRECTIONS) {
            Location n = p.moved(d);
            if (isValid(n) && get(p) == get(n))
                r.add(n);
        }
        return r;
    }

    /**
     * Gets valid neighbor locations in all eight compass directions.
     * @param p a location (need not be valid)
     * @return the valid neighbor locations in all compass directions
     */
    public Set<Location> allNeighbors(Location p) {
        var r = new HashSet<Location>();
        for (var d : Direction.values()) {
            Location n = p.moved(d);
            if (isValid(n))
                r.add(n);
        }
        return r;
    }

    /**
     * Gets the N E S W compass directions to valid neighbor locations.
     * @param p a location (need not be valid)
     * @return the main compass directions to valid neighbor locations
     */
    public Set<Direction> mainNeighborDirections(Location p) {
        var r = new HashSet<Direction>();
        for (var d : Direction.MAIN_DIRECTIONS) {
            Location n = p.moved(d);
            if (isValid(n))
                r.add(d);
        }
        return r;
    }

    /**
     * Gets the compass directions to valid neighbor locations.
     * @param p a location (need not be valid)
     * @return the compass directions to valid neighbor locations
     */
    public Set<Direction> allNeighborDirections(Location p) {
        var r = new HashSet<Direction>();
        for (var d : Direction.values()) {
            Location n = p.moved(d);
            if (isValid(n))
                r.add(d);
        }
        return r;
    }

    /**
     * Gets valid locations within a given distance from a given location.
     * @param p a location (need not be valid)
     * @param d the maximum taxicab distance from p
     * @return the valid locations that have distance at most d from p
     */
    public Set<Location> taxicabDisc(Location p, int d) {
        var r = new HashSet<Location>();
        for (int dr = -d; dr <= d; dr++) 
            for (int dc = -(d - Math.abs(dr)); dc <= d - Math.abs(dr); dc++) {
                Location n = p.movedBy(dr, dc);
                if (isValid(n)) 
                    r.add(n);
            }   
        return r;
    }                
    
    /**
     * Gets the location of the first occurrence of a given character, in row-major order.
     * @param c the character to find
     * @return the location of the first occurrence of c, or null if c does not occur
     */
    public Location findFirst(char c) {
        return locations().filter(p -> grid[p.row()][p.col()] == c).findFirst().orElse(null);
    }

    /**
     * Gets the locations of all occurrences of a given character, in row-major order.
     * @param c the character to find
     * @return the locations at which c occurs
     */
    public Stream<Location> findAll(char c) {
        return locations().filter(p -> grid[p.row()][p.col()] == c);        
    }

    /**
     * Yields the standard AoC string representation. 
     */
    public String toString() {
        var result = new StringBuilder();
        for (int i = 0; i < rows(); i++) {
            result.append(grid[i]);
            result.append('\n');
        }
        return result.toString();
    }
}
