package io.github.killtheinnocents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import entities.Entity;

import static helper.Constants.*;

public class View extends ScreenAdapter {


    private static final int FRAME_COLS = 2, FRAME_ROWS = 2;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    // Assets
    private Texture mapOverlay;
    private Sprite mapOverlaySprite;
    private Animation<TextureRegion> idleAnimation;
    private Texture idleAnimationSheet;
    private Sprite idleAnimationSprite;
    float stateTime; // time for animation
    private Entity player;
    Body body;
    private float deltaTime;

    public View(OrthographicCamera camera)
    {
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0,0),false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        player = new Entity( new Vector2(0, 0), this.world);
        this.body = player.getBody();

        // Assets
        // sfx Gdx.audio.newSound(Gdx.files.internal(name));
        // music Gdx.audio.newMusic(Gdx.files.internal(name));
        mapOverlay = new Texture("mapOverlay.png");
        mapOverlaySprite = new Sprite(mapOverlay);

        idleAnimationSheet = new Texture(Gdx.files.internal("idlePlayer_sheet2.png"));
        idleAnimationSprite = new Sprite(idleAnimationSheet);

        // Split Animation
        TextureRegion[][] tmp = TextureRegion.split(idleAnimationSheet,
            idleAnimationSheet.getWidth() / FRAME_COLS,
            idleAnimationSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        idleAnimation = new Animation<TextureRegion>(.25f, walkFrames);
        batch = new SpriteBatch();
        stateTime = 0f;
        deltaTime = Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(float delta)
    {
        this.update();

        // clears screen
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        batch.begin();
        // render objects here

        mapOverlaySprite.draw(batch);
        mapOverlaySprite.setSize(WIDTH, HEIGHT);
        Color c = batch.getColor();
        mapOverlaySprite.setColor(c.r, c.g, c.b, .3f);
        mapOverlaySprite.setCenter(0,0);

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, PlayerX, PlayerY, 150, 150);

        batch.end();

        world.step( 1/60, 6, 2);
        box2DDebugRenderer.render(world, camera.combined.scl(PPM));
    }

    // updates screen, handles key movements
    private void update()
    {
        world.step(1 / 60f, 6, 2);
        cameraUpdate();

        batch.setProjectionMatrix(camera.combined);

//        if (player.state == entityState.WALKING_N) {
//            player.position.add(player.velocity.x * deltaTime, player.velocity.y * deltaTime);
//        }

        input();

    }

    // updates pos. of camera
    private void cameraUpdate()
    {
        camera.position.set(new Vector3(0,0,0));
        camera.update();
    }

    // key word
    private void input() {
        float speed = 100f;
        float time = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            Gdx.app.exit();
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            PlayerY +=  time * speed;
            body.applyForceToCenter(new Vector2(10,0), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            PlayerY -= time * speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
            PlayerY +=  time * speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S))
        {
            PlayerY -= time * speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            PlayerX +=  time * speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            PlayerX -= time * speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D))
        {
            PlayerX +=  time * speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A))
        {
            PlayerX -= time * speed;
        }

    }

    // logic
    private void logic() {

    }

}
