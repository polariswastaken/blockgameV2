package com.polar.lithos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Screen;

public class GameScreen implements Screen {

    SpriteBatch batch;
    Texture stoneTexture;
    Texture dirtTexture;
    Texture grassTexture;
    Texture grassFullTexture;

    Music backgroundMusic;
    OrthographicCamera camera;

    Player player;
    World world;

    private final int BLOCK_SIZE;
    private final int WORLD_WIDTH;
    private final int WORLD_HEIGHT;

    private final Game game;
    private final View view;

    private int pickedBlock;


    public GameScreen(final Game game) {
        // generate the world
        this.world = new World();
        this.player = new Player();
        this.view = new View();

        BLOCK_SIZE = Config.BLOCK_SIZE;
        WORLD_WIDTH = Config.WORLD_WIDTH;
        WORLD_HEIGHT = Config.WORLD_HEIGHT;

        this.game = game;
        this.batch = game.batch; //import spriteBatch
        this.camera = view.camera;


        stoneTexture = new Texture("stone.png");
        dirtTexture = new Texture("dirt.png");
        grassTexture = new Texture("grass.png");
        grassFullTexture = new Texture("grasstop.png");

        String bkgMusic1 = "crossing_the_divide.mp3";
        String bkgMusic2 = "calman.mp3";
        String music;

        if (Math.random() > 0.5) {
            music = bkgMusic1;
        } else {
            music = bkgMusic2;
        }
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(music));

        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.1f);
        backgroundMusic.play();

    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        world.update(delta);

        // Track the camera to the player's new position inside the world
        view.setCameraX(world.player.hitbox.x);
        view.setCameraY(world.player.hitbox.y + 100);
        view.clampScreen();

        view.clampScreen();

        // Checks if the screen has been touched (e.g., mouse left,right,middle clicked)
        if (Gdx.input.isTouched()) {

            //get the raw screen coordinates
            float screenX = Gdx.input.getX();
            float screenY = Gdx.input.getY();

            //translate to world coordinates
            //Vector3 because unproject wants 3 dimensions (x, y, z), (we only use
            Vector3 mousePos = new Vector3(screenX, screenY, 0);

            //converts the vector from screen to world coords
            view.translateCoordinateSystem(mousePos);
            // now mousePos.x and mousePos.y are in world coordinates

            // Convert world coordinates to grid blocks/tiles
            // (int) to drop the decimals
            int gridX = (int) (mousePos.x / BLOCK_SIZE);
            int gridY = (int) (mousePos.y / BLOCK_SIZE);

            // Safety check so if we click outside the world, the game won't crash
            if (gridX >= 0 && gridX < WORLD_WIDTH && gridY >= 0 && gridY < WORLD_HEIGHT) {
                if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)) {
                    pickedBlock = world.getBlock(gridX, gridY);
                }

                // Sets the array value to PICKED block on RIGHT click.
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    world.setBlock(gridX, gridY, pickedBlock);
                }
                // Sets array value to 0 (air) on LEFT click.
                else if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    world.setBlock(gridX, gridY, 0);
                }
            }
        }

        Gdx.gl.glClearColor(0.4f, 0.6f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render
        view.camera.update();
        batch.setProjectionMatrix(view.camera.combined);

        batch.begin();

        // Calculate which blocks are visible
        // "startX" is the camera's left edge converted to grid coordinates
        int startX = (int)(camera.position.x - camera.viewportWidth / 2) / BLOCK_SIZE;
        // "endX" is the camera's right edge
        int endX = (int)(camera.position.x + camera.viewportWidth / 2) / BLOCK_SIZE + 2; // +2 to prerender

        // Clamp these values so we don't look outside the array
        if (startX < 0) startX = 0;
        if (endX > WORLD_WIDTH) endX = WORLD_WIDTH;

        // Draws whats on screen with frustum culling.
        for (int x = startX; x < endX; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                if (world.getBlock(x, y) == 1) {
                    batch.draw(stoneTexture, x * BLOCK_SIZE, y * BLOCK_SIZE);
                } else if (world.getBlock(x, y) == 2) {
                    batch.draw(dirtTexture, x * BLOCK_SIZE, y * BLOCK_SIZE);
                } else if (world.getBlock(x, y) == 3) {
                    batch.draw(grassTexture, x * BLOCK_SIZE, y * BLOCK_SIZE);
                } else if (world.getBlock(x, y) == 4) {
                    batch.draw(grassFullTexture, x * BLOCK_SIZE, y * BLOCK_SIZE);
                }
            }
        }

        //draw player
        world.player.draw(batch);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new FirstScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

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

    public void draw() {

    }


    @Override
    public void dispose() {
        // cleanup memory
        batch.dispose();
        stoneTexture.dispose();
        dirtTexture.dispose();
        grassTexture.dispose();
        grassFullTexture.dispose();
        backgroundMusic.dispose();
    }
}
