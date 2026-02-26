package com.polar.lithos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    Rectangle hitbox;
    float PLAYER_WIDTH = 12, PLAYER_HEIGHT = 16;
    float spawnX = 1000, spawnY = 100;

    public Player() {
        this.hitbox = new Rectangle(PLAYER_WIDTH, PLAYER_HEIGHT, 1000, 1000);

    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}



