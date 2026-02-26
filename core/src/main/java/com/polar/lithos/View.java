package com.polar.lithos;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class View {
    OrthographicCamera camera;

    public View() {
        // setup camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
    }
    public void setCameraX(float x) {
        camera.position.x = x;
    }
    public void setCameraY(float y) {
        camera.position.y = y;
    }

    public float getCameraX() {
        return camera.position.x;
    }
    public float getCameraY() {
        return camera.position.y;
    }

    public Vector3 translateCoordinateSystem(Vector3 pos) {
        return camera.unproject(pos);
    }

    public void clampScreen() {
        // Clamp screen to game world. (world screen boundary).
        float worldWidthInPixels = Config.WORLD_WIDTH * Config.BLOCK_SIZE;

        float minX = camera.viewportWidth / 2;
        float maxX = worldWidthInPixels - (camera.viewportWidth / 2);

        if (getCameraX() < minX) camera.position.x = minX;
        if (getCameraX() > maxX) camera.position.x = maxX;
    }
}
