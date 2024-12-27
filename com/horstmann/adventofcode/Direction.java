package com.horstmann.adventofcode;

import java.util.List;
import java.util.Map;

/**
 * A compass direction.
 */
public enum Direction {
    N(-1, 0, '^'), 
    NE(-1, 1, '\''),
    E(0, 1, '>'), 
    SE(1, 1, '\\'), 
    S(1, 0, 'v'), 
    SW(1, -1, '/'), 
    W(0, -1, '<'), 
    NW(-1, -1, '`');

    public static final List<Direction> MAIN_DIRECTIONS = List.of(Direction.N, Direction.W, Direction.S, Direction.E); 
    public static final List<Direction> DIAGONALS = List.of(Direction.NE, Direction.SE, Direction.SW, Direction.NW); 
    private static final Map<Integer, Direction> DIRECTION_SYMBOLS = Map.of(
        (int) '^', Direction.N, 
        (int) '\'', Direction.NE, 
        (int) '>', Direction.E, 
        (int) '\\', Direction.SE, 
        (int) 'v', Direction.S,
        (int) '/', Direction.SW,
        (int) '<', Direction.W, 
        (int) '`', Direction.NW); 

    private int[] drc;
    private char symbol;

    Direction(int dr, int dc, char symbol) {
        drc = new int[] { dr, dc };
        this.symbol = symbol;
    }
    
    public char symbol() { return symbol; }
    public static Direction ofSymbol(int c) { return DIRECTION_SYMBOLS.get(c); }
    
    int[] drc() {
        return drc;
    }
    
    /**
     * The direction obtained by making 45 degree clockwise turns from the given direction.
     * NOTE: If you only work with the main directions N E S W, multiply the argument by 2.
     * @param eightsClockwise the number of turns (may be of any size, also negative)
     * @return the turned direction
     */
    public Direction turn(int eightsClockwise) {
        return Direction.values()[Math.floorMod(ordinal() + eightsClockwise, 8)];
    }
    
    /**
     * The number of 45 degree clockwise turns to yield another direction 
     * @param other the other direction
     * @return the number of turns (between 0 and 7)
     */
    public int turnsTo(Direction other) {
        return Math.floorMod(other.ordinal() - ordinal(), 8);
    }
}

