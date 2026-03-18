package com.polar.lithos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Game extends com.badlogic.gdx.Game {
    public static final String GAME_TITLE = "blockgameV2 | [fps: %s]";

    public SpriteBatch batch;
    public BitmapFont font;

    public ExtendViewport worldViewport;
    public FitViewport uiViewport;

    public OrthographicCamera worldCamera;
    public OrthographicCamera uiCamera;


    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        worldCamera = new OrthographicCamera();
        worldViewport = new ExtendViewport(800, 600, worldCamera);

        uiCamera = new OrthographicCamera();
        uiViewport = new FitViewport(1920, 1080, uiCamera);

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
