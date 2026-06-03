package com.polar.lithos;

// Should be a Simple, Dumb container of numbers.
public class Chunk {
    int chunkX, chunkY; // 0 -> 15
    int[][] blocks;
    public final int CHUNK_SIZE = 16;
    public final ChunkCoord coord;

    public Chunk(ChunkCoord coord) {
        blocks = new int[CHUNK_SIZE][CHUNK_SIZE];
        this.coord = coord;
    }

    public void setBlock(int localX, int localY, int blockID) {
        blocks[localX][localY] = blockID;
    }

    public int getBlock(int localX, int localY) {
        return blocks[localX][localY]; // Returns Block ID (e.g., 1, 2, 3...)
    }
}
