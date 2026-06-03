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
        this.menuViewport = new FitViewport(1920, 1080);
    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        // Apply the menu's viewport, NOT Game's viewport
        menuViewport.apply();
        game.batch.setProjectionMatrix(menuViewport.getCamera().combined);

        game.batch.begin();
        game.font.draw(game.batch, "Main Menu - Click to Start", 890, 540);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
        System.out.println("viewport width: " + menuViewport.getWorldWidth() + " and height: " + menuViewport.getWorldHeight());
    }

    @Override
    public void resize(int width, int height) {
        if(width <= 0 || height <= 0) return;

        menuViewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
