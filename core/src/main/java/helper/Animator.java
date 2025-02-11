package helper;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Animator implements ApplicationListener {

    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 3, FRAME_ROWS = 2;

    // Objects used
    private Animation<TextureRegion> idleAnimation; // Must declare frame type (TextureRegion)
    private Texture idleAnimationSheet;
    private SpriteBatch batch;

    // A variable for tracking elapsed time for the animation
    float stateTime;

    public void AnimationCreate() {

        // Load the sprite sheet as a Texture
        idleAnimationSheet = new Texture(Gdx.files.internal("idlePLayer_sheet.png"));

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(idleAnimationSheet,
            idleAnimationSheet.getWidth() / FRAME_COLS,
            idleAnimationSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        idleAnimation = new Animation<TextureRegion>(0.025f, walkFrames);

        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        batch = new SpriteBatch();
        stateTime = 0f;
    }

    public void animationRender() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        batch.begin();
        batch.draw(currentFrame, 500, 500); // Draw current frame at (50, 50)
        batch.end();
    }


    public void dispose() { // SpriteBatches and Textures must always be disposed
        batch.dispose();
        idleAnimationSheet.dispose();
    }


}

