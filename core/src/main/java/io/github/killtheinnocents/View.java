package io.github.killtheinnocents;
import java.io.*;
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
    String[][] introDialouge = {
        {"*", "*[@", "*[@&", "*[@&!", "*[@&!^", "*[@&!^#", "*[@&!^#$]"},

        {"O", "Oh", "Oh!", "Oh! ", "Oh! \\n ",  "Oh! \\n We", "Oh! \\n Wel", "Oh! \\n Welc", "Oh! \\n Welco", "Oh! \\n Welcom", "Oh! \\n Welcome", "Oh! \\n Welcome,", "Oh! \\n Welcome, ", "Oh! \\n Welcome, T", "Oh! \\n Welcome, Tr", "Oh! \\n Welcome, Tra", "Oh! \\n Welcome, Trav", "Oh! \\n Welcome, Trave", "Oh! \\n Welcome, Travell", "Oh! \\n Welcome, Traveller", "Oh! \\n Welcome, Traveller.", "Oh! \\n Welcome, Traveller..", "Oh! \\n Welcome, Traveller..."},

        {"A", "A ", "A g", "A gr", "A gre", "A grea", "A great", "A great ", "A great e", "A great ev", "A great evi", "A great evil", "A great evil ", "A great evil h", "A great evil ha", "A great evil has", "A great evil has ", "A great evil has b", "A great evil has be", "A great evil has bef", "A great evil has befa", "A great evil has befall", "A great evil has befallen", "A great evil has befallen ", "A great evil has befallen o", "A great evil has befallen ou", "A great evil has befallen our", "A great evil has befallen our ", "A great evil has befallen our l", "A great evil has befallen our la", "A great evil has befallen our lan", "A great evil has befallen our land"},

        {"T", "Th", "The", "They", "They ", "They h", "They ho", "They hoo", "They hoar", "They hoard", "They hoard ", "They hoard r", "They hoard ri", "They hoard ric", "They hoard rich", "They hoard riches", "They hoard riches ", "They hoard riches a", "They hoard riches an", "They hoard riches and", "They hoard riches and ", "They hoard riches and s", "They hoard riches and st", "They hoard riches and ste", "They hoard riches and stea", "They hoard riches and steal", "They hoard riches and steal ", "They hoard riches and steal o", "They hoard riches and steal ou", "They hoard riches and steal our", "They hoard riches and steal our ", "They hoard riches and steal our f", "They hoard riches and steal our fi", "They hoard riches and steal our fir", "They hoard riches and steal our firs", "They hoard riches and steal our first", "They hoard riches and steal our firstb", "They hoard riches and steal our firstbo", "They hoard riches and steal our firstbor", "They hoard riches and steal our firstborn", "They hoard riches and steal our firstborns", "They hoard riches and steal our firstborns."},

        {"Y", "Yo", "You", "You,", "You, ", "You, O", "You, O ", "You, O d", "You, O dr", "You, O dra", "You, O drag", "You, O drago", "You, O dragon", "You, O dragon ", "You, O dragon h", "You, O dragon he", "You, O dragon hea", "You, O dragon hear", "You, O dragon heart", "You, O dragon hearte", "You, O dragon hearted", "You, O dragon hearted ", "You, O dragon hearted o", "You, O dragon hearted on", "You, O dragon hearted one", "You, O dragon hearted one,", "You, O dragon hearted one, ", "You, O dragon hearted one, a", "You, O dragon hearted one, ar", "You, O dragon hearted one, are", "You, O dragon hearted one, are ", "You, O dragon hearted one, are t", "You, O dragon hearted one, are th", "You, O dragon hearted one, are the", "You, O dragon hearted one, are the ", "You, O dragon hearted one, are the o", "You, O dragon hearted one, are the on", "You, O dragon hearted one, are the only", "You, O dragon hearted one, are the only ", "You, O dragon hearted one, are the only o", "You, O dragon hearted one, are the only on", "You, O dragon hearted one, are the only one", "You, O dragon hearted one, are the only one ", "You, O dragon hearted one, are the only one w", "You, O dragon hearted one, are the only one wh", "You, O dragon hearted one, are the only one who", "You, O dragon hearted one, are the only one who ", "You, O dragon hearted one, are the only one who c", "You, O dragon hearted one, are the only one who ca", "You, O dragon hearted one, are the only one who can", "You, O dragon hearted one, are the only one who can ", "You, O dragon hearted one, are the only one who can v", "You, O dragon hearted one, are the only one who can va", "You, O dragon hearted one, are the only one who can van", "You, O dragon hearted one, are the only one who can vanq", "You, O dragon hearted one, are the only one who can vanqu", "You, O dragon hearted one, are the only one who can vanqui", "You, O dragon hearted one, are the only one who can vanquis", "You, O dragon hearted one, are the only one who can vanquish", "You, O dragon hearted one, are the only one who can vanquish ", "You, O dragon hearted one, are the only one who can vanquish o", "You, O dragon hearted one, are the only one who can vanquish ou", "You, O dragon hearted one, are the only one who can vanquish our", "You, O dragon hearted one, are the only one who can vanquish our ", "You, O dragon hearted one, are the only one who can vanquish our e", "You, O dragon hearted one, are the only one who can vanquish our en", "You, O dragon hearted one, are the only one who can vanquish our ene", "You, O dragon hearted one, are the only one who can vanquish our enem", "You, O dragon hearted one, are the only one who can vanquish our enemy", "You, O dragon hearted one, are the only one who can vanquish our enemy."},

        {"V", "Ve", "Ven", "Vent", "Ventu", "Ventur", "Venture", "Venture ", "Venture y", "Venture yo", "Venture yon", "Venture yond", "Venture yonde", "Venture yonder", "Venture yonder ", "Venture yonder i", "Venture yonder in", "Venture yonder int", "Venture yonder into", "Venture yonder into ", "Venture yonder into t", "Venture yonder into th", "Venture yonder into tha", "Venture yonder into that", "Venture yonder into that ", "Venture yonder into that c", "Venture yonder into that ca", "Venture yonder into that cav", "Venture yonder into that cave", "Venture yonder into that cavern", "Venture yonder into that cavern ", "Venture yonder into that cavern s", "Venture yonder into that cavern sa", "Venture yonder into that cavern sav", "Venture yonder into that cavern save", "Venture yonder into that cavern save ", "Venture yonder into that cavern save u", "Venture yonder into that cavern save us", "Venture yonder into that cavern save us."},

        {"P", "Pr", "Pre", "Pres", "Press", "Press ", "Press E", "Press ES", "Press ESC", "Press ESC ", "Press ESC t", "Press ESC to", "Press ESC to ", "Press ESC to v", "Press ESC to vi", "Press ESC to vie", "Press ESC to view", "Press ESC to view ", "Press ESC to view s", "Press ESC to view se", "Press ESC to view set", "Press ESC to view sett", "Press ESC to view setti", "Press ESC to view settin", "Press ESC to view setting", "Press ESC to view settings"},

        {"S", "Sp", " Spa", "Spac", "Space", "Space ", "Space t", "Space to", "Space to ", "Space to c", "Space to co", "Space to con", "Space to cont", "Space to contin", "Space to continu", "Space to continue"}
        };

    int[] introIndicies = {7, 27, 31, 37, 53, 37, 27, 15};



    private int introIndex;


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

    private Texture title;
    private Sprite titleSprite;

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
        player = new Entity(this.world, 100, 5, STARTX, STARTY, true,"E",240,320, 1);
        this.body = player.getBody();
        healthIndex = 0;

        //font
        Texture font_texture = new Texture("game_font.png");
        font_texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font = new BitmapFont(Gdx.files.internal("game_font.fnt"), new TextureRegion(font_texture));
        font.getData().setScale(1f);

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
        // Assets

        // sfx Gdx.audio.newSound(Gdx.files.internal(name));
        // music Gdx.audio.newMusic(Gdx.files.internal(name));

        music = Gdx.audio.newMusic(Gdx.files.internal("MenuSong.wav"));
        music.setVolume(musicVolume);
        music.setLooping(true);
        music.play();

        title = new Texture("cursed_cavern.png");
        titleSprite = new Sprite(title);

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

        mainFont = new Font(new BitmapFont(Gdx.files.internal("game_font.fnt")));

        enemies1 = new Array<>();
        createEnemies();

        deathIndex = 0;
        mapIndex = 0;
        settingIndex = 7;
        introIndex = 0;

        map = false;
        keyTime = 0; ;
    }

    private void createEnemies() {

        Entity enemy1 = new Entity(world,15,2,410,20, false,"W",240,320, 1);
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

        if(currentScreen == Screen.INTRO && time > 500)
        {
            currentScreen = Screen.MAIN_GAME;
        } else if (currentScreen == Screen.INTRO)
        {
            time++;
        }
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

            batch.draw(title, -350, 0, (256 * 3),  (128 * 3));
            font.draw(batch, "Press Space to begin", -170, -350);

            batch.end();
        }

        // INTRO SCREEN
        else if(currentScreen == Screen.INTRO) {

            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();

            typingLabel = new TypingLabel(introDialouge[0][0], mainFont);
            typingLabel.draw(batch, 1);

            TextureRegion wizardFrame = wizardAnimation.getKeyFrame(stateTime, true);
            batch.draw(wizardFrame, -500, -300, 600, 600);

            batch.end();
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
            }

            if (mapIndex >= 7)
                mapIndex = 6;

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
            batch.draw(healthFrame[healthIndex], -580, -450, 300, 150);

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

            font.draw(batch, "Game Over", -80, -275);
            font.draw(batch, "Press R to restart", -160, -350);


            TextureRegion[] executionFrame = executionAnimation.getKeyFrames();
            batch.draw(executionFrame[deathIndex], -570, -200, 1100, 550);

            batch.end();

            if (elapsedTime > 10 && deathIndex < 26)
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
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            currentScreen = Screen.SETTINGS;
        }
        // MAP
        else if (currentScreen == Screen.MAP)
        {
            if (elapsedTime > 90 && Gdx.input.isKeyPressed(Input.Keys.SPACE))
                currentScreen = Screen.MAIN_GAME;
        }
        // MENU
        else if (currentScreen == Screen.MENU)
        {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
                currentScreen = Screen.INTRO;

        }
        // GAME OVER
        else if (currentScreen == Screen.GAME_OVER)
        {
            if (Gdx.input.isKeyPressed(Input.Keys.R))
                currentScreen = Screen.MENU;

        }
        // MAIN_GAME
        else if (currentScreen == Screen.MAIN_GAME) {

            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                currentScreen = Screen.MAP;
                elapsedTime = 0;
                map = false;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                for (Entity enemy : enemies1) {
                    enemy.takeDamage(player);
                    System.out.println(enemy.getCHealth());
                    break;
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
            {
                player.updatePosition(0, 1);
                player.getDirection("N");
                player.attackBox();
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                player.updatePosition(0, -1);
                player.getDirection("S");
                player.attackBox();
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                player.updatePosition(1, 0);
                player.getDirection("E");
                player.attackBox();
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)||Gdx.input.isKeyPressed(Input.Keys.A)) {
                player.updatePosition(-1, 0);
                player.getDirection("W");
                player.attackBox();
            } else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (checkExecute(10))
                    player.specialChangeAnimation("Attack");
            } else if (Gdx.input.isKeyPressed(Input.Keys.H)) {
                if (checkExecute(10)) {
                    player.specialChangeAnimation("Heal");
                    player.ouchies(-5);
                }
            } else {
                player.updatePosition(0, 0);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.I))
                player.ouchies(5);
            player.forceHUpdate(player.geteX(),player.geteY());

        }
        // SETTINGS
        else if (currentScreen == Screen.SETTINGS)
        {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
                if (checkExecute(5))
                    currentScreen = Screen.MAIN_GAME;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                if (checkExecute(15))
                    settingIndex = (settingIndex <= 0) ? 0 : settingIndex - 1;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                if (checkExecute(15))
                    settingIndex = (settingIndex >= 15) ? 15 : settingIndex + 1;
        }
        // FOR DEVELOPMENT PURPOSES
        else if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            currentScreen = Screen.MAIN_GAME;
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

            if(EPLD>=150 && EPLD <=800){
                xMod = player.geteX()-e.geteX();
                yMod = player.geteY()-e.geteY();
                xMod = xMod/EPLD;
                yMod = yMod/EPLD;
                if(Math.abs(xMod)>Math.abs(yMod)){
                    if(xMod>0){
                        e.getDirection("E");
                    }
                    else if(xMod<0){
                        e.getDirection("W");
                    }
                }
                else if(Math.abs(xMod)<Math.abs(yMod)){
                    if(yMod>0){
                        e.getDirection("N");
                    }
                    else if(yMod<0){
                        e.getDirection("S");
                    }
                }
                //enemy1.modPos(eVelocity*Math.cos(EPLA)*Math.signum(PlayerX-enemy1.getX()),eVelocity*Math.sin(EPLA)*Math.signum(PlayerY-enemy1.getY()));
                e.modPos((float) (xMod*eVelocity),(float) (yMod*eVelocity));

            }
            else if(EPLD >=800){
                if((!(e.geteX() >= e.getStartX()-10) || !(e.geteX() <= e.getStartX()+10)) || (!(e.geteY() >= e.getStartY()-10) || !(e.geteY() <= e.getStartY()+10))){
                xMod = e.getStartX() - e.geteX();
                yMod = e.getStartY() - e.geteY();
                xMod = xMod/eStartD;
                yMod = yMod/eStartD;
                    if(Math.abs(xMod)>Math.abs(yMod)){
                        if(xMod>0){
                            e.getDirection("E");
                        }
                        else if(xMod<0){
                            e.getDirection("W");
                        }
                    }
                    else if(Math.abs(xMod)<Math.abs(yMod)){
                        if(yMod>0){
                            e.getDirection("N");
                        }
                        else if(yMod<0){
                            e.getDirection("S");
                        }
                    }
                }

                else{
                    e.getDirection("W");
                }

                e.modPos((float) (xMod*eVelocity),(float) (yMod*eVelocity));

            }
            if (EPLD <=200){
                if (e.isAttackReady()){
                    e.updateAttackTime();
                    player.takeDamage(e);}
            }

        }
    }
    public float getDistance(float x1, float x2){
        return 0;
    }
    public static double getOverlap(Hitbox h1, Hitbox h2){
        return h2.max - h1.min;
    }
    public static boolean checkOverlap(Hitbox h1, Hitbox h2){
        return h1.max >= h2.min && h2.max >= h1.min;
    }
    public void clamp(Entity player, Entity enemy){
        xMin = (float) -(Gdx.graphics.getWidth()) /2;
        xMax = (float) (Gdx.graphics.getWidth()) /2;
        yMin = (float) -Gdx.graphics.getHeight() /2;
        yMax = (float) Gdx.graphics.getHeight()/2;
        if (checkOverlap(player.getxHit(),enemy.getxHit()) && !(checkOverlap(player.getyHit(),enemy.getyHit()))){
            if (player.geteY()>enemy.geteY()){
                yMin= (float) (enemy.geteY()+(enemy.getESY()/2));
            }
            if (player.geteY()<enemy.geteY()){
                yMax= (float) (enemy.geteY()-(enemy.getESY()/2));

            }
        }
        else if (checkOverlap(player.getyHit(),enemy.getyHit()) && !(checkOverlap(player.getxHit(),enemy.getxHit()))){
            if (player.geteX()>enemy.geteX()){
                xMin= (float) (enemy.geteX() +(enemy.getESX()/2));
            }
            if (player.geteX()<enemy.geteX()){
                xMax= (float) (enemy.geteX() -(enemy.getESX()/2));


            }
        }
        else if (checkOverlap(player.getyHit(),enemy.getyHit()) && checkOverlap(player.getxHit(),enemy.getxHit())){
            System.out.println("overlap");
            if(getOverlap(player.getxHit(),enemy.getxHit())>getOverlap(player.getyHit(),enemy.getyHit())){
                if (player.geteX()>enemy.geteX()){
                    xMin= (float) (enemy.geteX() +(enemy.getESX()/2));
                }
                if (player.geteX()<enemy.geteX()){
                    xMax= (float) (enemy.geteX() -(enemy.getESX()/2));

                }
            }
            if(getOverlap(player.getxHit(),enemy.getxHit()) < getOverlap(player.getyHit(),enemy.getyHit())){
                if (player.geteY()>enemy.geteY()){
                    yMin= (float) (enemy.geteY()+(enemy.getESY()/2));
                }
                if (player.geteY()<enemy.geteY()){
                    yMax= (float) (enemy.geteY()-(enemy.getESY()/2));

                }
            }
        }
        else {
            yMin = (float) -Gdx.graphics.getHeight() /2;
            yMax = (float) Gdx.graphics.getHeight()/2;
            xMin = (float) -(Gdx.graphics.getWidth()) /2;
            xMax = (float) (Gdx.graphics.getWidth()) /2;
        }
        player.setPos(MathUtils.clamp(player.geteX(), xMin, xMax),MathUtils.clamp(player.geteY(), yMin, yMax));

    }

}
