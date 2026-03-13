package com.polar.lithos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;

public class World {
    // 2D array [width][height].
    // world[x][y] will hold an ID (e.g. 1 for stone, 2 for dirt)
    public int[][] map;

    public BlockDefinition[] registry;
    public HashMap<Integer, DamagedBlock> damagedBlocks;

    private final int WORLD_WIDTH;
    private final int WORLD_HEIGHT;
    private final int BLOCK_SIZE;

    //public so game screen can access it to draw it
    public Player player;


    public World() {
        WORLD_WIDTH = Config.WORLD_WIDTH;
        WORLD_HEIGHT = Config.WORLD_HEIGHT;
        BLOCK_SIZE = Config.BLOCK_SIZE;

        // Lightweight map (just numbers)
        map = new int[WORLD_WIDTH][WORLD_HEIGHT];

        // The registry (the rules for the blocks)
        registry = new BlockDefinition[256];
        damagedBlocks = new HashMap<>();

        registry[1] = new BlockDefinition(1, 30, true); // Stone
        registry[2] = new BlockDefinition(2, 10, true); // Dirt
        registry[3] = new BlockDefinition(3, 10, true); // Grass
        registry[4] = new BlockDefinition(4, 10, true); // Grass-dirt transition


        // x (column), y (row)
        for (int x = 0; x < WORLD_WIDTH; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                if (y < 59) {
                    map[x][y] = 1; // STONE
                } else if (y < 61){
                    map[x][y] = 2; // DIRT
                } else if (y < 62) {
                    map[x][y] = 3; // GRASS
                } else if (y < 63) {
                    map[x][y] = 4; // GRASS BLOCK
                } else {
                    map[x][y] = 0;
                }
            }
        }

        this.player = new Player();
    }

    // Converts a 2D (x,y) grid coordinate into a single 1D memory index.
    // (y * width) skips down to the correct row, and (+ x) walks right to the exact column.
    private int getIndex(int x, int y) {
        return x + (y * WORLD_WIDTH);
    }

    public void hitBlock(int x, int y, int damageAmount) {
        if (x >= 0 && x < WORLD_WIDTH && y >= 0 && y < WORLD_HEIGHT) return;
        int blockID = map[x][y];
        if (blockID == 0) return;

        int index = getIndex(x, y);

        if (!damagedBlocks.containsKey(index)) {
            // Block is at full health. Get max health from registry, then damage it!
            int maxHealth = registry[blockID].maxHealth;
            damagedBlocks.put(index, new DamagedBlock(maxHealth - damageAmount));
        } else {
            damagedBlocks.get(index).currentHealth -= damageAmount;
        }

        if (damagedBlocks.get(index).currentHealth <= 0) {
            map[x][y] = 0;                   // Change map data to air
            damagedBlocks.remove(index);     // Clean up memory since we don't need it anymore.
        }
    }


    public int getBlock(int x, int y) {
        return map[x][y];
    }

    public void setBlock(int x, int y, int block) {
        map[x][y] = block;
    }



    public void update(float deltaTime) {
        // Player calculates its velocities based on input(left,right) and gravity
        player.update(deltaTime);

        // Apply horizontal movement, then check walls
        player.hitbox.x += player.playerVelocityX * deltaTime;
        checkCollisionX();

        // Apply vertical movement, then check floors/ceilings
        player.hitbox.y += player.playerVelocityY * deltaTime;
        checkCollisionY();
    }


    private void checkCollisionX() {
        // To check collision, we look at the corners of the player
        // if the player is moving Left, we check his left corners.
        // If moving Right, we check his right corners.

        // Get the grid coordinates of the player's corners
        int startX = (int) (player.hitbox.x / BLOCK_SIZE);
        int endX = (int) ((player.hitbox.x + player.hitbox.width) / BLOCK_SIZE);
        int startY = (int) (player.hitbox.y / BLOCK_SIZE);
        int endY = (int) ((player.hitbox.y + player.hitbox.height) / BLOCK_SIZE);

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x >= 0 && x < WORLD_WIDTH && y >= 0 && y < WORLD_HEIGHT) {
                    if (getBlock(x, y) > 0) { // If it is solid block
                        if (player.playerVelocityX > 0) {
                            // Snapping to the LEFT of the block
                            player.hitbox.x = x * BLOCK_SIZE - player.hitbox.width - 0.01f;
                        } else if (player.playerVelocityX < 0) {
                            // Snapping to the RIGHT of the block
                            player.hitbox.x = (x + 1) * BLOCK_SIZE + 0.01f;
                        }
                        player.playerVelocityX = 0; // Stop X velocity
                        return;
                    }
                }
            }
        }
    }

    private void checkCollisionY() {
        int startX = (int) (player.hitbox.x / BLOCK_SIZE);
        int endX = (int) ((player.hitbox.x + player.hitbox.width) / BLOCK_SIZE);
        int startY = (int) (player.hitbox.y / BLOCK_SIZE);
        int endY = (int) ((player.hitbox.y + player.hitbox.height) / BLOCK_SIZE);

        // Assume we are falling until we hit a floor
        player.onGround = false;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x >= 0 && x < WORLD_WIDTH && y >= 0 && y < WORLD_HEIGHT) {
                    if (getBlock(x, y) > 0) {
                        if (player.playerVelocityY > 0) {
                            // Hit ceiling
                            player.hitbox.y = y * BLOCK_SIZE - player.hitbox.height - 0.01f;
                            player.playerVelocityY = 0;
                        } else if (player.playerVelocityY < 0) {
                            // Hit floor
                            player.hitbox.y = (y + 1) * BLOCK_SIZE;
                            player.playerVelocityY = 0;
                            player.onGround = true; // Tell player it can jump again
                        }
                        return;
                    }
                }
            }
        }
    }
}
