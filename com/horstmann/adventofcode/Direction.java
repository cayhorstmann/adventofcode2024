package com.horstmann.adventofcode;

public enum Direction {
    N(-1, 0), 
    NE(-1, 1),
    E(0, 1), 
    SE(1, 1), 
    S(1, 0), 
    SW(1, -1), 
    W(0, -1), 
    NW(1, -1);

    private int[] dxy;

    Direction(int dx, int dy) {
        dxy = new int[] { dx, dy };
    }
    
    public int[] dxy() {
        return dxy;
    }
    
    public Direction turn(int eightsClockwise) {
        return Direction.values()[Math.floorMod(ordinal() + eightsClockwise, 8)];
    }
}

