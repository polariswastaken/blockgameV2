package com.polar.lithos;

public class World {
    // 2D array [width][height].
    // world[x][y] will hold an ID (e.g. 1 for stone, 2 for dirt)
    int[][] terrain;
    final int WORLD_WIDTH = 2000;
    final int WORLD_HEIGHT = 100;
    final int BLOCK_SIZE = 16; // Each block: 16x16

    public World() {
        // generate the world
        terrain = new int[WORLD_WIDTH][WORLD_HEIGHT];

        // x (column), y (row)
        for (int x = 0; x < WORLD_WIDTH; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                if (y < 59) {
                    terrain[x][y] = 1; // STONE
                } else if (y < 61){
                    terrain[x][y] = 2; // DIRT
                } else if (y < 62) {
                    terrain[x][y] = 3; // GRASS
                } else if (y < 63) {
                    terrain[x][y] = 4; // GRASS BLOCK
                } else {
                    terrain[x][y] = 0;
                }
            }
        }
    }

    public int getBlock(int x, int y) {
        return terrain[x][y];
    }

    public void setBlock(int x, int y, int block) {
        terrain[x][y] = block;
    }

}
