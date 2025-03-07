package io.github.killtheinnocents;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main class of the game, extending {@link com.badlogic.gdx.Game} and implementing the {@link com.badlogic.gdx.ApplicationListener}.
 * It initializes and manages the main game settings, including camera and sprite batch.
 */public class Main extends Game {

    public static Main INSTANCE;
    private int screenWidth, screenHeight;
    private OrthographicCamera orthographicCamera;

    private SpriteBatch batch;
    private Texture image;

    public Texture testTexture;
    public SpriteBatch spriteBatch;
    public Sprite textureSprite;

    /**
     * Constructor for the Main class, initializing the singleton instance.
     */
    public Main() {
        INSTANCE = this;
    }

    /**
     * {@inheritDoc}
     * Initializes the game by setting the screen dimensions, camera, and the initial game screen.
     */
    @Override
    public void create() {

        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.orthographicCamera = new OrthographicCamera();
        this.orthographicCamera.setToOrtho(false, screenWidth, screenHeight);
        setScreen(new View(orthographicCamera));

    }

}
