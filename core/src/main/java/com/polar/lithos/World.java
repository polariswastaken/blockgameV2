package com.polar.lithos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class World {
    // 2D array [width][height].
    // world[x][y] will hold an ID (e.g. 1 for stone, 2 for dirt)
    int[][] terrain;
    final int WORLD_WIDTH = 2000;
    final int WORLD_HEIGHT = 100;
    final int BLOCK_SIZE = 16; // Each block: 16x16

    float playerVelocityX = 0;
    float playerVelocityY = 0;
    boolean onGround = false;
    boolean facingRight = false;

    final float SPEED = 200;
    final float GRAVITY = -500;
    final float JUMP_SPEED = 250;

    Texture playerTexture;
    Sprite playerSprite;

    Player player;
    Rectangle hitbox;

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

        this.player = new Player();
        this.hitbox = player.getHitbox();
    }


    public int getBlock(int x, int y) {
        return terrain[x][y];
    }

    public void setBlock(int x, int y, int block) {
        terrain[x][y] = block;
    }



    public void update(float deltaTime) {
        // Horizontal movement
        // stop moving if no key is pressed

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerVelocityX = -SPEED; // move left

            if (facingRight) {
                playerSprite.setScale(1, 1);
                facingRight = false;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerVelocityX = SPEED;  // move right
            if (!facingRight) {
                playerSprite.setScale(-1, 1);
                facingRight = true;
            }
        }
        System.out.println("numah 1 ran");


        hitbox.x += playerVelocityX * deltaTime;
        System.out.println("numah 2 ran");
        // Check wall collision
        checkCollisionX();
        System.out.println("numah 3 ran");

        // Vertical movement

        // Apply Gravity to Velocity
        playerVelocityY += GRAVITY * deltaTime;

        System.out.println("numah 4 ran");

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && onGround) {
            playerVelocityY = JUMP_SPEED;
            onGround = false;
        }
        System.out.println("numah 5 ran");

        hitbox.y += playerVelocityY * deltaTime;

        // Check floor/ceiling collision
        checkCollisionY();
    }


    private void checkCollisionX() {
        // To check collision, we look at the corners of the player
        // if the player is moving Left, we check his left corners.
        // If moving Right, we check his right corners.

        // Get the grid coordinates of the player's corners
        int startX = (int) (hitbox.x / BLOCK_SIZE);
        int endX = (int) ((hitbox.x + hitbox.width) / BLOCK_SIZE);
        int startY = (int) (hitbox.y / BLOCK_SIZE);
        int endY = (int) ((hitbox.y + hitbox.height) / BLOCK_SIZE);

        // Loop through the blocks we are currently touching
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {

                // Check bounds to avoid crashing at edge of world
                if (x >= 0 && x < WORLD_WIDTH && y >= 0 && y < WORLD_HEIGHT) {

                    // If we hit a solid block
                    if (getBlock(x, y) > 0) { //if is not air

                        if (playerVelocityX > 0) {
                            // We were moving RIGHT. Snap us to the LEFT edge of the block.
                            hitbox.x = x * BLOCK_SIZE - hitbox.width - 0.01f;
                        } else if (playerVelocityX < 0) {
                            // We were moving LEFT. Snap us to the RIGHT edge of the block.
                            hitbox.x = (x + 1) * BLOCK_SIZE + 0.01f;
                        }
                        playerVelocityX = 0; // Stop moving
                        return; // Collision handled, stop checking
                    }
                }
            }
        }
    }

    private void checkCollisionY() {
        // Same logic, but for Up/Down
        int startX = (int) (hitbox.x / BLOCK_SIZE);
        int endX = (int) ((hitbox.x + hitbox.width) / BLOCK_SIZE);
        int startY = (int) (hitbox.y / BLOCK_SIZE);
        int endY = (int) ((hitbox.y + hitbox.height) / BLOCK_SIZE);

        onGround = false;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x >= 0 && x < WORLD_WIDTH && y >= 0 && y < WORLD_HEIGHT) {
                    if (getBlock(x, y) > 0) { //if it is not air ID.
                        if (playerVelocityY > 0) {
                            // Moving UP (head hit ceiling)
                            hitbox.y = y * BLOCK_SIZE - hitbox.height - 0.01f; // extra margin
                            playerVelocityY = 0;
                        } else if (playerVelocityY < 0) {
                            // Moving DOWN (feet hit floor)
                            hitbox.y = (y + 1) * BLOCK_SIZE;

                            // if we hit the floor gravity stops pulling down
                            playerVelocityY = 0;
                            onGround = true;
                        }
                        return;
                    }
                }
            }
        }
    }

}
