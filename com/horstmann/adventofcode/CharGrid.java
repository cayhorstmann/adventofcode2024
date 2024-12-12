package com.horstmann.adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CharGrid {
	private char[][] grid;

	private CharGrid() {}
	
	public static CharGrid parse(Path p) throws IOException {
	    var result = new CharGrid();
        result.grid = Files.lines(p).map(String::toCharArray).toArray(char[][]::new);
	    return result;
    }

    public Character get(Location p) {
        if (isValid(p))
            return grid[p.row()][p.col()];
        else 
            return null;
    }
    
    public Character put(Location p, char c) {
        if (isValid(p)) {
            char old = grid[p.row()][p.col()];
            grid[p.row()][p.col()] = c;
            return old;
        } else
            return null;
    }
    
    public String substring(Location p, Direction d, int length) {
        var result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (isValid(p)) result.append(grid[p.row()][p.col()]);
            p = p.moved(d);
        }
        
        return result.toString();
    }
    
	public boolean isValid(Location p) {
		return p.row() >= 0 && p.row() < grid.length && p.col() >= 0 && p.col() < grid[0].length;
	}

    public Set<Location> mainNeighbors(Location p) {
        var r = new HashSet<Location>();
        for (var d : Direction.MAIN_DIRECTIONS) {
            Location n = p.moved(d);
            if (isValid(n))
                r.add(n);
        }
        return r;
    }

    public Set<Location> sameNeighbors(Location p) { // for floodfill
        var r = new HashSet<Location>();
        for (var d : Direction.MAIN_DIRECTIONS) {
            Location n = p.moved(d);
            if (isValid(n) && get(p) == get(n))
                r.add(n);
        }
        return r;
    }

    public Set<Location> allNeighbors(Location p) {
		var r = new HashSet<Location>();
		for (var d : Direction.values()) {
			Location n = p.moved(d);
			if (isValid(n))
				r.add(n);
		}
		return r;
	}

    public Set<Direction> mainNeighborDirections(Location p) {
        var r = new HashSet<Direction>();
        for (var d : Direction.MAIN_DIRECTIONS) {
            Location n = p.moved(d);
            if (isValid(n))
                r.add(d);
        }
        return r;
    }

    public Set<Direction> allNeighborDirections(Location p) {
		var r = new HashSet<Direction>();
		for (var d : Direction.values()) {
			Location n = p.moved(d);
			if (isValid(n))
				r.add(d);
		}
		return r;
	}

	public int rows() {
		return grid.length;
	}

	public int cols() {
		return grid[0].length;
	}
	
	public Stream<Location> locations() {
	    return Stream.iterate(
            new Location(0, 0),
            this::isValid, 
            l -> l.col() < cols() - 1 ? new Location(l.row(), l.col() + 1) : new Location(l.row() + 1, 0));
	}
	
	public Location findFirst(char c) {
	    return locations().filter(p -> grid[p.row()][p.col()] == c).findFirst().orElse(null);
	}

	public Stream<Location> findAll(char c) {
	    return locations().filter(p -> grid[p.row()][p.col()] == c);	    
    }
	    
	    

	public String toString() {
	    var result = new StringBuilder();
        for (int i = 0; i < rows(); i++) {
            result.append(grid[i]);
            result.append('\n');
        }
        return result.toString();
    }
}
