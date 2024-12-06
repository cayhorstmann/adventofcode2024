package com.horstmann.adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class CharGrid {
	private char[][] grid;

	private CharGrid() {}
	
	public enum Direction {
		N, NE, E, SE, S, SW, W, NW;

		public int[] dxy() {
			return dxys[ordinal()];
		}
		
		public Direction turn(int eightsClockwise) {
		    return Direction.values()[Math.floorMod(ordinal() + eightsClockwise, 8)];
		}

		private static int[][] dxys = { { -1, 0 }, { -1, 1}, { 0, 1 }, {1 , 1}, { 1, 0 }, {1 , -1}, { 0, -1 }, {-1 , -1} };
	}
	
	public static final List<Direction> MAIN_DIRECTIONS = List.of(Direction.N, Direction.W, Direction.S, Direction.E); 
    public static final List<Direction> ALL_DIRECTIONS = List.of(Direction.values()); 

	public record Location(int row, int col) {
		public Location next(Direction d) {
			int[] dxy = d.dxy();
			return new Location(row + dxy[0], col + dxy[1]);
		}

		public Direction to(Location p) {
			for (Direction d : Direction.values())
				if (next(d).equals(p))
					return d;
			return null;
		}

		public String toString() {
			return row + "," + col;
		}
	}

	public static CharGrid parse(Path p) throws IOException {
	    var result = new CharGrid();
        result.grid = Files.lines(p).map(String::toCharArray).toArray(char[][]::new);
	    return result;
    }

	public char charAt(Location p) {
	    return grid[p.row][p.col];
	}

    public char charAt(Location p, char defaultValue) {
        if (isValid(p))
            return grid[p.row][p.col];
        else
            return defaultValue;
    }

    public Character get(Location p) {
        if (isValid(p))
            return grid[p.row][p.col];
        else 
            return null;
    }
    
    public Character put(Location p, char c) {
        if (isValid(p)) {
            char old = grid[p.row][p.col];
            grid[p.row][p.col] = c;
            return old;
        } else
            return null;
    }
    
    public String substring(Location p, Direction d, int length) {
        var result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (isValid(p)) result.append(charAt(p));
            p = p.next(d);
        }
        
        return result.toString();
    }
    
	public boolean isValid(Location p) {
		return p.row >= 0 && p.row < grid.length && p.col >= 0 && p.col < grid[0].length;
	}

    public Set<Location> mainNeighbors(Location p) {
        var r = new HashSet<Location>();
        for (var d : MAIN_DIRECTIONS) {
            Location n = p.next(d);
            if (isValid(n))
                r.add(n);
        }
        return r;
    }

    public Set<Location> allNeighbors(Location p) {
		var r = new HashSet<Location>();
		for (var d : Direction.values()) {
			Location n = p.next(d);
			if (isValid(n))
				r.add(n);
		}
		return r;
	}

    public Set<Direction> mainNeighborDirections(Location p) {
        var r = new HashSet<Direction>();
        for (var d : MAIN_DIRECTIONS) {
            Location n = p.next(d);
            if (isValid(n))
                r.add(d);
        }
        return r;
    }

    public Set<Direction> allNeighborDirections(Location p) {
		var r = new HashSet<Direction>();
		for (var d : Direction.values()) {
			Location n = p.next(d);
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
	
	public Location find(char c) {
	    return locations().filter(p -> charAt(p) == c).findFirst().orElse(null);
	    /*
	    for (int i = 0; i < rows(); i++)
	        for (int j = 0; j < cols(); j++) {
	            var l = new Location(i, j);
	            if (charAt(l) == c) return l;
	        }
	    return null;
	    */
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
