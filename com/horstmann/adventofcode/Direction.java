package com.horstmann.adventofcode;

import java.util.List;
import java.util.Map;

public enum Direction {
    N(-1, 0), 
    NE(-1, 1),
    E(0, 1), 
    SE(1, 1), 
    S(1, 0), 
    SW(1, -1), 
    W(0, -1), 
    NW(-1, -1);

    public static final List<Direction> MAIN_DIRECTIONS = List.of(Direction.N, Direction.W, Direction.S, Direction.E); 
    public static final List<Direction> DIAGONALS = List.of(Direction.NE, Direction.SE, Direction.SW, Direction.NW); 
    private static final Map<Integer, Direction> DIRECTION_SYMBOLS = Map.of(
            (int) '<', Direction.W, 
            (int) '>', Direction.E, 
            (int) '^', Direction.N, 
            (int) 'v', Direction.S,
            (int) '`', Direction.NW, 
            (int) '\'', Direction.NE, 
            (int) '\\', Direction.SE, 
            (int) '/', Direction.SW);

    private int[] drc;

    Direction(int dr, int dc) {
        drc = new int[] { dr, dc };
    }
    
    public static Direction ofSymbol(int c) { return DIRECTION_SYMBOLS.get(c); }
    
    public int[] drc() {
        return drc;
    }
    
    public Direction turn(int eightsClockwise) {
        return Direction.values()[Math.floorMod(ordinal() + eightsClockwise, 8)];
    }
    
    public int turnsTo(Direction other) {
        return Math.floorMod(other.ordinal() - ordinal(), 8);
    }
}

