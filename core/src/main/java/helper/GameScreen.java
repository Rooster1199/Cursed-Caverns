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

    public GameScreen(String FileName) {

        background = new Texture(FileName);
        backgroundSprite = new Sprite(background);

        // load screen
    }

    public void drawbg(SpriteBatch batch) {
        backgroundSprite.draw(batch);
        backgroundSprite.setPosition(-WIDTH / 2 - 100, -HEIGHT / 2);
        backgroundSprite.setSize((float) (WIDTH * 1.3), HEIGHT);

    }

    public void drawOverlay(SpriteBatch batch) {
        backgroundSprite.draw(batch);
        backgroundSprite.setSize(WIDTH, HEIGHT);
        Color c = batch.getColor();
        backgroundSprite.setColor(c.r, c.g, c.b, .3f);
        backgroundSprite.setCenter(0,0);
    }

}
