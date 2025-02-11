package io.github.killtheinnocents;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public static Main INSTANCE;
    private int screenWidth, screenHeight;
    private OrthographicCamera orthographicCamera;

    private SpriteBatch batch;
    private Texture image;

    public Texture testTexture;
    public SpriteBatch spriteBatch;
    public Sprite textureSprite;

    public Main() {
        INSTANCE = this;
    }

    @Override
    public void create() {

        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.orthographicCamera = new OrthographicCamera();
        this.orthographicCamera.setToOrtho(false, screenWidth, screenHeight);
        setScreen(new View(orthographicCamera));

    }

}
