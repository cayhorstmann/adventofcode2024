package com.horstmann.adventofcode;

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

