package com.polar.lithos;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class View {
    ExtendViewport worldViewport;
    FitViewport uiViewport;

    public View(ExtendViewport world, FitViewport ui) {
        // setup camera
        worldViewport = world;
        uiViewport = ui;

    }
    public void resize(int width, int height) {
        if(width <= 0 || height <= 0) return;
        worldViewport.update(width, height, false);
        uiViewport.update(width, height, true);
    }

    public OrthographicCamera getWorldCamera() {
        return (OrthographicCamera) worldViewport.getCamera();
    }

    public void setCameraX(float x) {
        getWorldCamera().position.x = x;
    }
    public void setCameraY(float y) {
        getWorldCamera().position.y = y;
    }

    public float getCameraX() {
        return getWorldCamera().position.x;
    }
    public float getCameraY() {
        return getWorldCamera().position.y;
    }

    public Vector3 translateCoordinateSystem(Vector3 pos) {
        return worldViewport.unproject(pos);
    }

    public void clampScreen() {
        // Clamp screen to game world. (world screen boundary).
        OrthographicCamera camera = getWorldCamera();
        float worldWidthInPixels = Config.WORLD_WIDTH * Config.BLOCK_SIZE;

        float minX = camera.viewportWidth / 2;
        float maxX = worldWidthInPixels - (camera.viewportWidth / 2);

        if (getCameraX() < minX) camera.position.x = minX;
        if (getCameraX() > maxX) camera.position.x = maxX;
    }
}
