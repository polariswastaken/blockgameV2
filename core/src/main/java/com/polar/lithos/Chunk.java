package com.polar.lithos;

public class Chunk {
    int chunkX, chunkY;
    int[][] blocks;

    public Chunk() {
        blocks = new int[16][16];
    }

    public void setBlock(int localX, int localY, int blockID) {
        blocks[localX][localY] = blockID;
    }

    public int getBlock(int localX, int localY) {
        return blocks[localX][localY];
    }
}
