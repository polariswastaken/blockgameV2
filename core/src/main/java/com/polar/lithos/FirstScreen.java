package com.polar.lithos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FirstScreen implements Screen {

    final Game game;
    Viewport menuViewport;

    public FirstScreen(Game game) {
        this.game = game;
        this.menuViewport = new FitViewport(1280, 720);
    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        // Apply the menu's viewport, NOT the game's 8x5 viewport
        menuViewport.apply();
        game.batch.setProjectionMatrix(menuViewport.getCamera().combined);

        game.batch.begin();
        // Now you are drawing in a 1280x720 space.
        // X and Y are in pixels. (640, 360) is the middle of the screen.
        game.font.draw(game.batch, "Main Menu - Click to Start", 550, 360);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        if(width <= 0 || height <= 0) return;

        menuViewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}
