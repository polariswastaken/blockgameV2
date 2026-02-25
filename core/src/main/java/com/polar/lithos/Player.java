package com.polar.lithos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    Rectangle player;
    Texture playerTexture;
    Sprite playerSprite;
    World world;

    int WORLD_WIDTH, WORLD_HEIGHT, BLOCK_SIZE;

    float playerVelocityX = 0;
    float playerVelocityY = 0;
    boolean onGround = false;
    boolean facingRight = false;

    final float SPEED = 200;
    final float GRAVITY = -500;
    final float JUMP_SPEED = 250;

    public Player(World world) {
        this.WORLD_WIDTH = world.WORLD_WIDTH;
        this.WORLD_HEIGHT = world.WORLD_HEIGHT;
        this.BLOCK_SIZE = world.BLOCK_SIZE;
        this.world = world;

    }

    private void update(float deltaTime) {
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

        player.x += playerVelocityX * deltaTime;

        // Check wall collision
        checkCollisionX();


        // Vertical movement

        // Apply Gravity to Velocity
        playerVelocityY += GRAVITY * deltaTime;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && onGround) {
            playerVelocityY = JUMP_SPEED;
            onGround = false;
        }

        player.y += playerVelocityY * deltaTime;

        // Check floor/ceiling collision
        checkCollisionY();
    }

    private void checkCollisionX() {
        // To check collision, we look at the corners of the player
        // if the player is moving Left, we check his left corners.
        // If moving Right, we check his right corners.

        // Get the grid coordinates of the player's corners
        int startX = (int) (player.x / BLOCK_SIZE);
        int endX = (int) ((player.x + player.width) / BLOCK_SIZE);
        int startY = (int) (player.y / BLOCK_SIZE);
        int endY = (int) ((player.y + player.height) / BLOCK_SIZE);

        // Loop through the blocks we are currently touching
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {

                // Check bounds to avoid crashing at edge of world
                if (x >= 0 && x < WORLD_WIDTH && y >= 0 && y < WORLD_HEIGHT) {

                    // If we hit a solid block
                    if (world.getBlock(x, y) > 0) { //if is not air

                        if (playerVelocityX > 0) {
                            // We were moving RIGHT. Snap us to the LEFT edge of the block.
                            player.x = x * BLOCK_SIZE - player.width - 0.01f;
                        } else if (playerVelocityX < 0) {
                            // We were moving LEFT. Snap us to the RIGHT edge of the block.
                            player.x = (x + 1) * BLOCK_SIZE + 0.01f;
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
        int startX = (int) (player.x / BLOCK_SIZE);
        int endX = (int) ((player.x + player.width) / BLOCK_SIZE);
        int startY = (int) (player.y / BLOCK_SIZE);
        int endY = (int) ((player.y + player.height) / BLOCK_SIZE);

        onGround = false;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x >= 0 && x < WORLD_WIDTH && y >= 0 && y < WORLD_HEIGHT) {
                    if (world.getBlock(x, y) > 0) { //if it is not air ID.
                        if (playerVelocityY > 0) {
                            // Moving UP (head hit ceiling)
                            player.y = y * BLOCK_SIZE - player.height - 0.01f; // extra margin
                            playerVelocityY = 0;
                        } else if (playerVelocityY < 0) {
                            // Moving DOWN (feet hit floor)
                            player.y = (y + 1) * BLOCK_SIZE;

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



