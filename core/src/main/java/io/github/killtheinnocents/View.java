package io.github.killtheinnocents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
//import com.github.tommyettinger.textratypist.FWSkin;
//import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.github.tommyettinger.textra.FWSkin;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.TypingLabel;
import entities.Entity;
import entities.Hitbox;
import helper.GameScreen;
import jdk.internal.org.jline.terminal.TerminalBuilder;
import entities.entityState.*;
import org.w3c.dom.Text;

import java.util.*;

import static com.badlogic.gdx.Gdx.audio;
import static helper.Constants.*;

public class View extends ScreenAdapter {

    private static final int FRAME_COLS = 2, FRAME_ROWS = 2;

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public World world;
    public Box2DDebugRenderer box2DDebugRenderer;

    //Font
    BitmapFont font;
    Font mainFont;
    Texture font_texture;
    FWSkin fontSkin;
    Skin skin;
    TypingLabel typingLabel;
    String[] introDialouge = {"[*@&!^#$]","Oh! \\n Welcome, Traveller...", "A great evil has befallen our land", "They hoard riches and steal our firstborns.", "You, O dragon hearted one, are the only one who can vanquish our enemy.", "Venture yonder into that cavern save us.", "Press ESC to view settings", "Space to continue" };

    // Assets
    private Animation<TextureRegion> executionAnimation;
    private Texture executionSheet;
    private Sprite executionSprite;
    private Animation<TextureRegion> wizardAnimation;
    private Texture wizardSheet;
    private Sprite wizardSprite;
    private Animation<TextureRegion> mapAnimation;
    private Texture settingSheet;
    private Sprite settingSprite;
    private Animation<TextureRegion> settingAnimation;
    private TextureRegion[] settingFrames;
    private Texture mapSheet;
    private Sprite mapSprite;
    private TextureRegion[] mapFrames;
    private Texture settingsSheet;
    private Sprite settingsSprite;

    // SOUND
    Music music;
    float musicVolume;
    float sfxVolume;

    // Logic Components
    float stateTime; // time for animation
    private Entity player;
    Body body;
    private float deltaTime;
    private float time;
    private float elapsedTime;
    private float keyTime;

    //Health Bar
    private Animation<TextureRegion> healthBarAnimation;
    private Texture healthSheet;
    private Sprite healthSprite;

    // Animation Indexes
    private int healthIndex;
    private int deathIndex;
    private int mapIndex;
    private int settingIndex;
    private int introIndex;

    // Screens
    enum Screen {
        MENU, INTRO, MAP, MAIN_GAME, GAME_OVER, SETTINGS;
    }
    public Screen currentScreen = Screen.MENU;
    private GameScreen overlay;
    private GameScreen homeScreen;
    private GameScreen dungeonScreen;
    private GameScreen gameOverScreen;

    // enemy
    public Array<Entity> enemies1;
    public Array<Sprite> enemies;
    double eVelocity = 5;
    double EPLD;
    double eStartD;
    //double EPLS;
    //double EPLA;
    double xMod;
    double yMod;


    float yMin;
    float yMax;
    float xMin;
    float xMax;
    boolean map;

    public View(OrthographicCamera camera)
    {
        this.camera = camera;
        this.world = new World(new Vector2(0,0),false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.batch = new SpriteBatch();

        // Player + Health
        player = new Entity(this.world, 100, 100, STARTX, STARTY, true);
        this.body = player.getBody();
        healthIndex = 0;

        //font
        Texture font_texture = new Texture("game_font.png");
        font_texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font = new BitmapFont(Gdx.files.internal("game_font.fnt"), new TextureRegion(font_texture));
        font.getData().setScale(3f);

        stateTime = 0f;
        deltaTime = Gdx.graphics.getDeltaTime();
        time = 0;

        // Screens
        overlay = new GameScreen("mapOverlay.png");
        homeScreen = new GameScreen("home_screen.png");
        dungeonScreen = new GameScreen("dungeon_background.png");
        gameOverScreen = new GameScreen("deathBg.png");

        // Volume
        musicVolume = 0.5f;
        sfxVolume = 0.5f;

        create();
    }

    public void create() {
        // Loading Assets
        // sfx Gdx.audio.newSound(Gdx.files.internal(name));
        music = Gdx.audio.newMusic(Gdx.files.internal("MenuSong.wav"));
        music.setVolume(musicVolume);
        music.setLooping(true);
        music.play();

        settingsSheet = new Texture("settings_cog.png");
        settingsSprite = new Sprite(settingsSheet);

        settingSheet = new Texture("settingsBG.png");
        settingSprite = new Sprite(settingSheet);
        settingAnimation = new Animation<TextureRegion>(2f, player.animationSplicer(settingSheet, 4, 4));
        settingFrames = settingAnimation.getKeyFrames();

        mapSheet = new Texture("mapBG.png");
        mapSprite = new Sprite(mapSheet);
        mapAnimation = new Animation<TextureRegion>(2f, player.animationSplicer(mapSheet, 2, 4));
        mapFrames = mapAnimation.getKeyFrames();

        executionSheet = new Texture("execution.png");
        executionSprite = new Sprite(executionSheet);
        executionAnimation = new Animation<TextureRegion>(2f, player.animationSplicer(executionSheet, 4, 7));

        wizardSheet = new Texture(Gdx.files.internal("wizardSheet.png"));
        wizardSprite = new Sprite(wizardSheet);
        wizardAnimation = new Animation<TextureRegion>(.5f, player.animationSplicer(wizardSheet,2, 2));

        healthSheet = new Texture(Gdx.files.internal("HealthBar.png"));
        healthSprite = new Sprite(wizardSheet);
        healthBarAnimation = new Animation<TextureRegion>(.25f, player.animationSplicer(healthSheet,3, 6));

        enemies1 = new Array<>();
        createEnemies();

        // Font
        //skin = new Skin(Gdx.files.internal("game_font.fnt"));
        mainFont = new Font(new BitmapFont(Gdx.files.internal("game_font.fnt")));


        // All these indexes!
        deathIndex = 0;
        mapIndex = 0;
        settingIndex = 7;
        introIndex = 0;

        map = false;
        keyTime = 0; ;
    }

    private void createEnemies() {

        Entity enemy1 = new Entity(world,15,2,600,20, false);
        enemies1.add(enemy1);

    }

    @Override
    public void render(float delta)
    {
        checkDistance();
        this.update();
        super.render(delta);
        clamp(player,enemies1.get(0));

        logic();
        draw();

        player.changeAnimation();

        if(currentScreen == Screen.INTRO && time > 500)
        {
            currentScreen = Screen.MAIN_GAME;
        } else if (currentScreen == Screen.INTRO)
        {
            time++;
        }
    }

    @Override
    public void show()
    {

    }

    public void draw() {

        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // MENU SCREEN

        if(currentScreen == Screen.MENU)
        {
            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();

            homeScreen.drawbg(this.batch);

            font.draw(batch, "CURSED CAVERN", -200, 100);
            font.draw(batch, "Press Space to play", -525, -100);

            batch.end();
        }

        // INTRO SCREEN
        else if(currentScreen == Screen.INTRO) {

            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
//            font.draw(batch, "[*@&!^#$]", 200, 100);
//            font.draw(batch, "Oh! \n Welcome, Traveller...", 200, 100);
//            font.draw(batch, "A great evil has befallen our land", 200, 100);
//            font.draw(batch, "They hoard riches and steal our firstborns.", 200, 100);
//            font.draw(batch, "You, O dragon hearted one, are the only one who can vanquish our enemy.", 200, 100);
//            font.draw(batch, "Venture yonder into that cavern save us.", 200, 100);
//
//            font.draw(batch, "Press ESC to view settings", 200, 100);
//            font.draw(batch, "Space to continue", 200, 100);

            typingLabel = new TypingLabel(introDialouge[0], mainFont);
            typingLabel.draw(batch, 0);

            TextureRegion wizardFrame = wizardAnimation.getKeyFrame(stateTime, true);
            batch.draw(wizardFrame, -1000, -600, 1200, 1200);

            batch.end();

            introIndex++;
        }

        //MAP
        else if (currentScreen == Screen.MAP) {
            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();

            batch.draw(mapFrames[mapIndex], -WIDTH/2, -HEIGHT/2, WIDTH, HEIGHT);

            if (elapsedTime > 45 && mapIndex < 7 && !map) {
                mapIndex++;
                map = true;
            } else if (elapsedTime > 90) {
                elapsedTime = 0;
                map = false;
                Gdx.gl.glClearColor(0,0,0,1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                currentScreen = Screen.MAIN_GAME;
            }

            elapsedTime++;

            batch.end();
        }

        // MAIN GAME
        else if(currentScreen == Screen.MAIN_GAME) {

            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();

            dungeonScreen.drawbg(this.batch);

            overlay.drawOverlay(this.batch);

            // Get current frame of player animation for the current stateTime
            player.drawSprite(batch, stateTime);

            TextureRegion[] healthFrame = healthBarAnimation.getKeyFrames();
            batch.draw(healthFrame[healthIndex], -1170, -905, 600, 300);

            for(Entity enemy: enemies1)
            {
                enemy.drawSprite(batch, stateTime);
            }

            batch.end();

        }
        // GAME OVER
        else if (currentScreen == Screen.GAME_OVER) {
            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();

            gameOverScreen.drawbg(this.batch);

            font.draw(batch, "Game Over", -300, -300);

            TextureRegion[] executionFrame = executionAnimation.getKeyFrames();
            batch.draw(executionFrame[deathIndex], -600, -200, 1200, 600);

            batch.end();

            if (deathIndex >= 25)
            {
                System.out.println("FIX THIS");
            }

            if (elapsedTime > 15 && deathIndex < 27)
            {
                elapsedTime = 0;
                deathIndex++;
            }

            elapsedTime++;

        }
        // SETTINGS
        else if (currentScreen == Screen.SETTINGS) {
            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();

            batch.draw(settingFrames[settingIndex], -WIDTH/2, -HEIGHT/2, WIDTH, HEIGHT);
            music.setVolume(((float) settingIndex / 15 ));
            System.out.println("Setting INdex" + settingIndex);
            System.out.println("Volume" + ((float) settingIndex / 15 ));

            batch.draw(settingsSprite, 830, -830, 300, 300);

            batch.end();
        }

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
//      }

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

        keyTime++;

        if (Gdx.input.isKeyPressed(Input.Keys.Q))
        {
            Gdx.app.exit();
        } else if (currentScreen == Screen.MENU && Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            currentScreen = Screen.INTRO;
        } else if (currentScreen == Screen.GAME_OVER && Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            currentScreen = Screen.MENU;
        } else if (currentScreen == Screen.MAIN_GAME && Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            currentScreen = Screen.SETTINGS;
        }
        else if (currentScreen == Screen.MAIN_GAME && Gdx.input.isKeyPressed(Input.Keys.L))
        {
            currentScreen = Screen.MAP;
        }
        else if (currentScreen == Screen.SETTINGS && Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            currentScreen = Screen.MAIN_GAME;
        } else if (Gdx.input.isKeyPressed(Input.Keys.U))
        {
            player.ouchies(1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            currentScreen = Screen.MAIN_GAME;
        } else if (currentScreen == Screen.SETTINGS && Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            if (checkExecute(15))
                settingIndex = (settingIndex <= 0) ? 0 : settingIndex - 1;
        } else if (currentScreen == Screen.SETTINGS && Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            if (checkExecute(15))
                settingIndex = (settingIndex >= 15) ? 15 : settingIndex + 1;
        } else if (currentScreen == Screen.MAIN_GAME){
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
            {
                player.updatePosition(0, 1);
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                player.updatePosition(0, -1);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                player.updatePosition(1, 0);
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)||Gdx.input.isKeyPressed(Input.Keys.A)) {
                player.updatePosition(-1, 0);
            } else if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                player.specialChangeAnimation("Attack");
            } else if (Gdx.input.isKeyPressed(Input.Keys.H)) {
                player.specialChangeAnimation("Heal");
            }else {
                player.updatePosition(0, 0);
            }
            player.forceHUpdate(player.geteX(),player.geteY());
        }

    }

    private boolean checkExecute(int factor)
    {

        if (keyTime > factor)
        {
            keyTime = 0;
            return true;
        }
        return false;
    }

    private void logic() {
        clamp(player,enemies1.get(0));

        for(Entity e : enemies1){
            e.setX((float)MathUtils.clamp(e.geteX(), (double) -Gdx.graphics.getWidth() /2, (double) Gdx.graphics.getWidth() /2));
            e.setY((float)MathUtils.clamp(e.geteY(), (double) -Gdx.graphics.getHeight() /2, (double) Gdx.graphics.getHeight() /2));
        }

        // Health Bar
        // Calculation: 17 - Math.floor(health lost / approximation)
        double approximation = 1.0 / 17;
        int percentHealthLost = (int) Math.floor((player.getCHealth() * 1.0 / player.getMaxHealth()) / approximation);
        healthIndex = 17 - percentHealthLost;
        if (!player.isLiving())
        {
            currentScreen = Screen.GAME_OVER;
        }
    }

    @Override
    public void dispose() {
        font.dispose();
    }


    public void checkDistance(){
        eVelocity = 1.5;
        for (Entity e: enemies1){
            EPLD = Math.sqrt(((player.geteX() - e.geteX())*(player.geteX() - e.geteX())) + ((player.geteY() - e.geteY())*(player.geteY() - e.geteY())));
            eStartD = Math.sqrt(((e.getStartX()-e.geteX())*(e.getStartX()-e.geteX())) + ((e.getStartY()-e.geteY())*(e.getStartY()-e.geteY())));

            if(EPLD<=800){
                xMod = player.geteX()-e.geteX();
                yMod = player.geteY()-e.geteY();
                xMod = xMod/EPLD;
                yMod = yMod/EPLD;
                //enemy1.modPos(eVelocity*Math.cos(EPLA)*Math.signum(PlayerX-enemy1.getX()),eVelocity*Math.sin(EPLA)*Math.signum(PlayerY-enemy1.getY()));
                e.modPos((float) (xMod*eVelocity),(float) (yMod*eVelocity));
            }
            else {
                if((!(e.geteX() >= e.getStartX()-10) || !(e.geteX() <= e.getStartX()+10)) || (!(e.geteY() >= e.getStartY()-10) || !(e.geteY() <= e.getStartY()+10))){
                xMod = e.getStartX() - e.geteX();
                yMod = e.getStartY() - e.geteY();
                xMod = xMod/eStartD;
                yMod = yMod/eStartD;
                e.modPos((float) (xMod*eVelocity),(float) (yMod*eVelocity));
                }
            }

        }
    }
    public boolean checkOverlap(Hitbox h1, Hitbox h2){
//        System.out.println(h1.max >= h2.min && h2.max >= h1.min);
//        System.out.println(": ("+h1.min+", "+h1.max+")");
//        System.out.println(": ("+h2.min+", "+h2.max+")");
        return h1.max >= h2.min && h2.max >= h1.min;
    }
    public void clamp(Entity player, Entity enemy){
        float bufferDistance = 50;
        xMin = (float) -(Gdx.graphics.getWidth()) /2;
        xMax = (float) (Gdx.graphics.getWidth()) /2;
        yMin = (float) -Gdx.graphics.getHeight() /2;
        yMax = (float) Gdx.graphics.getHeight()/2;
        if (checkOverlap(player.getxHit(),enemy.getxHit())){
            if (player.geteY()>enemy.geteY()){
                yMin= (float) (enemy.geteY()+bufferDistance);
                player.updatePosition(0, (int) bufferDistance);
            }
            else if (player.geteY() <enemy.geteY()){
                yMax= (float) (enemy.geteY()-bufferDistance);
                player.updatePosition(0, (int) bufferDistance);
            }
        }
        else {
            yMin = (float) -Gdx.graphics.getHeight() /2;
            yMax = (float) Gdx.graphics.getHeight()/2;
        }


        if (checkOverlap(player.getyHit(),enemy.getyHit())){
            if (player.geteX()>enemy.geteX()){
                xMin= (float) (enemy.geteX() +bufferDistance);
                player.updatePosition((int) bufferDistance, 0);
            }
            else if (player.geteX()<enemy.geteX()){
                xMax= (float) (enemy.geteX() -bufferDistance);
                player.updatePosition((int) bufferDistance, 0);

            }
        }
        else {
            xMin = (float) -(Gdx.graphics.getWidth()) /2;
            xMax = (float) (Gdx.graphics.getWidth()) /2;
        }

//        System.out.println("X: ("+xMin+", "+xMax+")");
//        System.out.println("Y: ("+yMin+", "+yMax+")");
//        System.out.println("e: ("+enemy.geteX()+", "+enemy.geteY()+")");
//        System.out.println("p: ("+player.geteX()+", "+player.geteY()+")");
//        System.out.println();
    }

}
