package com.polar.lithos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    public Rectangle hitbox;
    public float playerVelocityX = 0;
    public float playerVelocityY = 0;
    public boolean onGround = false;
    public boolean facingRight = true;

    private final float SPEED = 200;
    private final float GRAVITY;
    private final float JUMP_SPEED = 250;


    Texture playerTexture;
    Sprite playerSprite;

    public Player() {

        GRAVITY = Config.GRAVITY;

        // Setup Hitbox
        this.hitbox = new Rectangle(8 * Config.BLOCK_SIZE, 100 * Config.BLOCK_SIZE, 12, 28);

        // Setup Sprite
        playerTexture = new Texture("character.png");
        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(hitbox.getWidth(), hitbox.getHeight());
        playerSprite.setOriginCenter();
    }

    public void update(float deltaTime) {
        playerVelocityX = 0;

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && onGround) {
            playerVelocityY = JUMP_SPEED;
            onGround = false;
        }

        // Apply Gravity
        playerVelocityY += GRAVITY * deltaTime;
    }

    public void draw(SpriteBatch batch) {
        // Sync the sprite to the hitbox before drawing
        playerSprite.setPosition(hitbox.x, hitbox.y);
        playerSprite.draw(batch);
    }
}
