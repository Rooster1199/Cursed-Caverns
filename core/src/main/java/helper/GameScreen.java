package helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static helper.Constants.HEIGHT;
import static helper.Constants.WIDTH;

public class GameScreen {

    private Texture background;
    private Sprite backgroundSprite;

    /**
     * Represents the game screen, including the background and overlay effects.
     * Initializes the background texture and sprite from a given file name.
     *
     * @param FileName The file name of the background texture.
     */
    public GameScreen(String FileName) {

        background = new Texture(FileName);
        backgroundSprite = new Sprite(background);
    }

    /**
     * Draws the background on the screen using the specified SpriteBatch.
     * The background is positioned and scaled appropriately.
     *
     * @param batch The SpriteBatch used to draw the background.
     */
    public void drawbg(SpriteBatch batch) {
        backgroundSprite.draw(batch);
        backgroundSprite.setPosition(-WIDTH / 2 - 100, -HEIGHT / 2);
        backgroundSprite.setSize((float) (WIDTH * 1.3), HEIGHT);

    }

    /**
     * Draws an overlay on the screen using the specified SpriteBatch.
     * The overlay uses a semi-transparent background to create an effect.
     *
     * @param batch The SpriteBatch used to draw the overlay.
     */
    public void drawOverlay(SpriteBatch batch) {
        backgroundSprite.draw(batch);
        backgroundSprite.setSize(WIDTH, HEIGHT);
        Color c = batch.getColor();
        backgroundSprite.setColor(c.r, c.g, c.b, .3f);
        backgroundSprite.setCenter(0,0);
    }

}
