package com.polar.lithos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Game extends com.badlogic.gdx.Game {
    public static final String GAME_TITLE = "blockgameV2 | [fps: %s]";

    public SpriteBatch batch;
    public BitmapFont font;
    public FitViewport viewport;


    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        viewport = new FitViewport(8,5);

        this.setScreen(new FirstScreen(this));
    }

    @Override
    public void render() {
        Gdx.graphics.setTitle(String.format(GAME_TITLE, Gdx.graphics.getFramesPerSecond()));
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
