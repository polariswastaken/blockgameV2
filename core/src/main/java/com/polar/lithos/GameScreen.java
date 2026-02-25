package com.polar.lithos;

import com.badlogic.gdx.ApplicationAdapter;
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
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class GameScreen implements Screen {

    SpriteBatch batch;
    Texture stoneTexture;
    Texture dirtTexture;
    Texture grassTexture;
    Texture grassFullTexture;
    OrthographicCamera camera;

    // 2D array [width][height].
    // world[x][y] will hold an ID (e.g. 1 for stone, 2 for dirt)
    int[][] world;
    final int WORLD_WIDTH = 2000;
    final int WORLD_HEIGHT = 100;
    final int BLOCK_SIZE = 16; // Each block: 16x16

    Music backgroundMusic;

    Rectangle player;
    Texture playerTexture;
    Sprite playerSprite;


    float playerVelocityX = 0;
    float playerVelocityY = 0;
    boolean onGround = false;
    boolean facingRight = false;


    final float SPEED = 200;
    final float GRAVITY = -500;
    final float JUMP_SPEED = 250;


    public GameScreen(final Game game) {
        batch = new SpriteBatch();

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
        backgroundMusic.setVolume(0.8f);
        backgroundMusic.play();

        // Rectangle(x_spawn, y_spawn, x_size, y_size)
        player = new Rectangle(8*BLOCK_SIZE, 100*BLOCK_SIZE, 13, 30);

        playerTexture = new Texture("character.png");

        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(player.getWidth(), player.getHeight());
        playerSprite.setOriginCenter();

        // setup camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        // generate the world
        world = new int[WORLD_WIDTH][WORLD_HEIGHT];

        // x (column), y (row)
        for (int x = 0; x < WORLD_WIDTH; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                if (y < 59) {
                    world[x][y] = 1; // STONE
                } else if (y < 61){
                    world[x][y] = 2; // DIRT
                } else if (y < 62) {
                    world[x][y] = 3; // GRASS
                } else if (y < 63) {
                    world[x][y] = 4; // GRASS BLOCK
                } else {
                    world[x][y] = 0;
                }
            }
        }
    }

    private void updatePlayer(float deltaTime) {
        // Horizontal movement
        // stop moving if no key is pressed
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
                    if (world[x][y] > 0) { //if is not air

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
                    if (world[x][y] > 0) { //if it is not air ID.
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

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        updatePlayer(delta);

        camera.position.x = player.x;
        camera.position.y = player.y+100;

        // Clamp screen to game world. (world screen boundary).
        float worldWidthInPixels = WORLD_WIDTH * BLOCK_SIZE;

        float minX = camera.viewportWidth / 2;
        float maxX = worldWidthInPixels - (camera.viewportWidth / 2);

        if (camera.position.x < minX) camera.position.x = minX;
        if (camera.position.x > maxX) camera.position.x = maxX;

        // Check if the left mouse button is clicked (or screen is touched)
        if (Gdx.input.isTouched()) {

            //get the raw screen coordinates
            float screenX = Gdx.input.getX();
            float screenY = Gdx.input.getY();

            //translate to world coordinates
            //Vector3 because unproject wants 3 dimensions (x, y, z), (we only use
            Vector3 mousePos = new Vector3(screenX, screenY, 0);

            //converts the vector from screen to world coords
            camera.unproject(mousePos);
            // now mousePos.x and mousePos.y are in world coordinates

            // Convert world coordinates to grid blocks/tiles
            // (int) to drop the decimals
            int gridX = (int) (mousePos.x / BLOCK_SIZE);
            int gridY = (int) (mousePos.y / BLOCK_SIZE);

            // Safety check so if we click outside the 50x50 world, the game won't crash
            if (gridX >= 0 && gridX < WORLD_WIDTH && gridY >= 0 && gridY < WORLD_HEIGHT) {

                // Sets array value to 0 (air) on LEFT click.
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    world[gridX][gridY] = 0;
                }

                // Sets the array value to 1 (dirt) on RIGHT click.
                else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    world[gridX][gridY] = 1;
                }
            }
        }

        Gdx.gl.glClearColor(0.4f, 0.6f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render
        camera.update();
        batch.setProjectionMatrix(camera.combined);

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
                if (world[x][y] == 1) {
                    batch.draw(stoneTexture, x * BLOCK_SIZE, y * BLOCK_SIZE);
                } else if (world[x][y] == 2) {
                    batch.draw(dirtTexture, x * BLOCK_SIZE, y * BLOCK_SIZE);
                } else if (world[x][y] == 3) {
                    batch.draw(grassTexture, x * BLOCK_SIZE, y * BLOCK_SIZE);
                } else if (world[x][y] == 4) {
                    batch.draw(grassFullTexture, x * BLOCK_SIZE, y * BLOCK_SIZE);
                }
            }
        }

        //draw player
        //batch.draw(playerTexture, player.x, player.y, player.width, player.height);
        playerSprite.setPosition(player.getX(), player.getY());
        playerSprite.draw(batch);

        batch.end();
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
