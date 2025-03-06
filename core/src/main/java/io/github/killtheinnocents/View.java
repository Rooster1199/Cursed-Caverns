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
import entities.Entity;
import entities.Hitbox;
import helper.GameScreen;
import helper.Room;

import static helper.Constants.*;

public class View extends ScreenAdapter {

    private static final int FRAME_COLS = 2, FRAME_ROWS = 2;

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public World world;
    public Box2DDebugRenderer box2DDebugRenderer;

    //Font
    private BitmapFont font;

    private String[][] introDialouge = {

        {"O", "Oh", "Oh!", "Oh! ", "Oh! \n ",  "Oh! \n We", "Oh! \n Wel", "Oh! \n Welc", "Oh! \n Welco", "Oh! \n Welcom", "Oh! \n Welcome", "Oh! \n Welcome,", "Oh! \n Welcome, ", "Oh! \n Welcome, T", "Oh! \n Welcome, Tr", "Oh! \n Welcome, Tra", "Oh! \n Welcome, Trav", "Oh! \n Welcome, Trave", "Oh! \n Welcome, Travell", "Oh! \n Welcome, Traveller", "Oh! \n Welcome, Traveller.", "Oh! \n Welcome, Traveller..", "Oh! \n Welcome, Traveller...", "Oh! \n Welcome, Traveller...", "Oh! \n Welcome, Traveller...", "Oh! \n Welcome, Traveller...", "Oh! \n Welcome, Traveller...", "Oh! \n Welcome, Traveller...", "Oh! \n Welcome, Traveller...", "Oh! \n Welcome, Traveller...", "Oh! \n Welcome, Traveller...", "Oh! \n Welcome, Traveller..."},

        {"A",
            "A ",
            "A g",
            "A gr",
            "A gre",
            "A grea",
            "A great",
            "A great ",
            "A great e",
            "A great ev",
            "A great evi",
            "A great evil\n",
            "A great evil \n",
            "A great evil\n h",
            "A great evil\n ha",
            "A great evil\n has",
            "A great evil\n has ",
            "A great evil\n has b",
            "A great evil\n has be",
            "A great evil\n has bef",
            "A great evil\n has befa",
            "A great evil\n has befall",
            "A great evil\n has befallen",
            "A great evil\n has befallen ",
            "A great evil\n has befallen o",
            "A great evil\n has befallen ou",
            "A great evil\n has befallen our",
            "A great evil\n has befallen our ",
            "A great evil\n has befallen our l",
            "A great evil\n has befallen our la",
            "A great evil\n has befallen our lan",
            "A great evil\n has befallen our land",
            "A great evil\n has befallen our land",
            "A great evil\n has befallen our land",
            "A great evil\n has befallen our land",
            "A great evil\n has befallen our land",
            "A great evil\n has befallen our land",
            "A great evil\n has befallen our land",
            "A great evil\n has befallen our land",
            "A great evil\n has befallen our land",
            "A great evil\n has befallen our land",
            "A great evil\n has befallen our land"},

        {"T",
            "Th",
            "The",
            "They",
            "They ",
            "They h",
            "They ho",
            "They hoo",
            "They hoar",
            "They hoard",
            "They hoard ",
            "They hoard r",
            "They hoard ri",
            "They hoard ric",
            "They hoard rich",
            "They hoard riches\n",
            "They hoard riches \n",
            "They hoard riches\n a",
            "They hoard riches\n an",
            "They hoard riches\n and",
            "They hoard riches\n and ",
            "They hoard riches\n and s",
            "They hoard riches\n and st",
            "They hoard riches\n and ste",
            "They hoard riches\n and stea",
            "They hoard riches\n and steal",
            "They hoard riches\n and steal ",
            "They hoard riches\n and steal o",
            "They hoard riches\n and steal ou",
            "They hoard riches\n and steal our",
            "They hoard riches\n and steal our ",
            "They hoard riches\n and steal our f",
            "They hoard riches\n and steal our fi",
            "They hoard riches\n and steal our fir",
            "They hoard riches\n and steal our firs",
            "They hoard riches\n and steal our first",
            "They hoard riches\n and steal our firstb",
            "They hoard riches\n and steal our firstbo",
            "They hoard riches\n and steal our firstbor",
            "They hoard riches\n and steal our firstborn",
            "They hoard riches\n and steal our firstborns",
            "They hoard riches\n and steal our firstborns.",
            "They hoard riches\n and steal our firstborns.",
            "They hoard riches\n and steal our firstborns.",
            "They hoard riches\n and steal our firstborns.",
            "They hoard riches\n and steal our firstborns.",
            "They hoard riches\n and steal our firstborns.",
            "They hoard riches\n and steal our firstborns.",
            "They hoard riches\n and steal our firstborns.",
            "They hoard riches\n and steal our firstborns.",
            "They hoard riches\n and steal our firstborns.",
            "They hoard riches\n and steal our firstborns."},

        {"Y",
            "Yo",
            "You",
            "You,\n",
            "You,\n ",
            "You,\n O",
            "You,\n O ",
            "You,\n O d",
            "You,\n O dr",
            "You,\n O dra",
            "You,\n O drag",
            "You,\n O drago",
            "You,\n O dragon",
            "You,\n O dragon ",
            "You,\n O dragon h",
            "You,\n O dragon he",
            "You,\n O dragon hea",
            "You,\n O dragon hear",
            "You,\n O dragon heart",
            "You,\n O dragon hearte",
            "You,\n O dragon hearted",
            "You,\n O dragon hearted ",
            "You,\n O dragon hearted o",
            "You,\n O dragon hearted on",
            "You,\n O dragon hearted one",
            "You,\n O dragon hearted one,\n",
            "You,\n O dragon hearted one,\n ",
            "You,\n O dragon hearted one,\n a",
            "You,\n O dragon hearted one,\n ar",
            "You,\n O dragon hearted one,\n are",
            "You,\n O dragon hearted one,\n are ",
            "You,\n O dragon hearted one,\n are t",
            "You,\n O dragon hearted one,\n are th",
            "You,\n O dragon hearted one,\n are the",
            "You,\n O dragon hearted one,\n are the ",
            "You,\n O dragon hearted one,\n are the o",
            "You,\n O dragon hearted one,\n are the on",
            "You,\n O dragon hearted one,\n are the only",
            "You,\n O dragon hearted one,\n are the only ",
            "You,\n O dragon hearted one,\n are the only o",
            "You,\n O dragon hearted one,\n are the only on",
            "You,\n O dragon hearted one,\n are the only one",
            "You,\n O dragon hearted one,\n are the only one ",
            "You,\n O dragon hearted one,\n are the only one w",
            "You,\n O dragon hearted one,\n are the only one wh",
            "You,\n O dragon hearted one,\n are the only one who",
            "You,\n O dragon hearted one,\n are the only one who ",
            "You,\n O dragon hearted one,\n are the only one who c",
            "You,\n O dragon hearted one,\n are the only one who ca",
            "You,\n O dragon hearted one,\n are the only one who can\n",
            "You,\n O dragon hearted one,\n are the only one who can\n ",
            "You,\n O dragon hearted one,\n are the only one who can\n v",
            "You,\n O dragon hearted one,\n are the only one who can\n va",
            "You,\n O dragon hearted one,\n are the only one who can\n van",
            "You,\n O dragon hearted one,\n are the only one who can\n vanq",
            "You,\n O dragon hearted one,\n are the only one who can\n vanqu",
            "You,\n O dragon hearted one,\n are the only one who can\n vanqui",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquis",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish ",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish o",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish ou",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our ",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our e",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our en",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our ene",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our enem",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our enemy",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our enemy.",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our enemy.",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our enemy.",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our enemy.",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our enemy.",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our enemy.",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our enemy.",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our enemy.",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our enemy.",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our enemy.",
            "You,\n O dragon hearted one,\n are the only one who can\n vanquish our enemy."},

        {"V",
            "Ve",
            "Ven",
            "Vent",
            "Ventu",
            "Ventur",
            "Venture",
            "Venture ",
            "Venture y",
            "Venture yo",
            "Venture yon",
            "Venture yond",
            "Venture yonde",
            "Venture yonder\n",
            "Venture yonder ",
            "Venture yonder\n i",
            "Venture yonder\n i",
            "Venture yonder\n in",
            "Venture yonder\n int",
            "Venture yonder\n into",
            "Venture yonder\n into ",
            "Venture yonder\n into t",
            "Venture yonder\n into th",
            "Venture yonder\n into tha",
            "Venture yonder\n into that",
            "Venture yonder\n into that ",
            "Venture yonder\n into that c",
            "Venture yonder\n into that ca",
            "Venture yonder\n into that cav",
            "Venture yonder\n into that cave",
            "Venture yonder\n into that cavern",
            "Venture yonder\n into that cavern ",
            "Venture yonder\n into that cavern \ns",
            "Venture yonder\n into that cavern\n sa",
            "Venture yonder\n into that cavern\n sav",
            "Venture yonder\n into that cavern\n save",
            "Venture yonder\n into that cavern\n save ",
            "Venture yonder\n into that cavern\n save u",
            "Venture yonder\n into that cavern\n save us",
            "Venture yonder\n into that cavern\n save us.",
            "Venture yonder\n into that cavern\n save us.",
            "Venture yonder\n into that cavern\n save us.",
            "Venture yonder\n into that cavern\n save us.",
            "Venture yonder\n into that cavern\n save us.",
            "Venture yonder\n into that cavern\n save us.",
            "Venture yonder\n into that cavern\n save us.",
            "Venture yonder\n into that cavern\n save us.",
            "Venture yonder\n into that cavern\n save us.",
            "Venture yonder\n into that cavern\n save us.",
            "Venture yonder\n into that cavern\n save us."},

            {"P", "Pr", "Pre", "Pres", "Press", "Press ", "Press E", "Press ES", "Press ESC", "Press ESC ", "Press ESC t", "Press ESC to", "Press ESC to ", "Press ESC to v", "Press ESC to vi", "Press ESC to vie", "Press ESC to view", "Press ESC to view ", "Press ESC to view s", "Press ESC to view se", "Press ESC to view set", "Press ESC to view sett", "Press ESC to view setti", "Press ESC to view settin", "Press ESC to view setting", "Press ESC to view settings", "Press ESC to view settings", "Press ESC to view settings", "Press ESC to view settings", "Press ESC to view settings", "Press ESC to view settings", "Press ESC to view settings", "Press ESC to view settings", "Press ESC to view settings", "Press ESC to view settings", "Press ESC to view settings"},

            {"S", "Sp", " Spa", "Spac", "Space", "Space ", "Space t", "Space to", "Space to ", "Space to c", "Space to co", "Space to con", "Space to cont", "Space to contin", "Space to continu", "Space to continue","Space to continue", "Space to continue", "Space to continue", "Space to continue", "Space to continue", "Space to continue", "Space to continue", "Space to continue", "Space to continue", "Space to continue" }
        };

    private int[] introIndicies = {33, 42, 52, 80, 50, 36, 25, 26};


    private String[][] dungeonMessages = {
        {"E", "En", "Ent", "Ente", "Enter", "Enteri", "Enterin", "Entering", "Entering ", "Entering D", "Entering Du", "Entering Dun", "Entering Dung", "Entering Dunge", "Entering Dungeo", "Entering Dungeon", "Entering Dungeon.", "Entering Dungeon..", "Entering Dungeon...", "Entering Dungeon...", "Entering Dungeon...", "Entering Dungeon...", "Entering Dungeon...", "Entering Dungeon...", "Entering Dungeon...", "Entering Dungeon...", "Entering Dungeon...", "Entering Dungeon...", "Entering Dungeon..."},
        {"L", "Le", "Lev", "Leve", "Level", "Level ", "Level C", "Level Cl", "Level Cle", "Level Clea", "Level Clear", "Level Cleare", "Level Cleared", "Level Cleared!", "Level Cleared!", "Level Cleared!", "Level Cleared!", "Level Cleared!", "Level Cleared!", "Level Cleared!", "Level Cleared!", "Level Cleared!", "Level Cleared!", "Level Cleared!"}
    };

    private int[] dungeonTextPosition = { -425, -330 };

    private int[] dungeonMessageIndicies = {29, 24};

    private int introIndex;
    private int introArrayIndex;


    // Assets
    private Animation<TextureRegion> executionAnimation;
    private Texture executionSheet;
    private Animation<TextureRegion> wizardAnimation;
    private Texture wizardSheet;
    private Texture settingSheet;
    private Animation<TextureRegion> settingAnimation;
    private TextureRegion[] settingFrames;
    private Texture mapSheet;

    private Animation<TextureRegion> mapAnimation;
    private TextureRegion[] mapFrames;
    private Texture settingsSheet;
    private Sprite settingsSprite;

    private Texture title;
    private Texture potion;

    // Inventory
    private Texture inventoryBoxes;
    private Texture[] inventory;

    // SOUND
    private Music music;
    private float musicVolume;
    private float sfxVolume;

    // Logic Components
    private float stateTime; // time for animation
    private Entity player;
    private Body body;
    private float deltaTime;
    private float time;
    private float elapsedTime;
    private float keyTime;

    //Health Bar
    private Animation<TextureRegion> healthBarAnimation;
    private Texture healthSheet;

    // Animation Indexes
    private int healthIndex;
    private int deathIndex;
    private int mapIndex;
    private int settingIndex;
    private int roomIndex = 0;
    private int transitionIndex;
    private int transitionArrayIndex;

    // TODO: enemy death animation + new inventory items

    // Screens
    private enum Screen {
        MENU, INTRO, TRANSITION_SCREEN, MAP, MAIN_GAME, GAME_OVER, SETTINGS;
    }
    public Screen currentScreen = Screen.MENU;
    private GameScreen overlay;
    private GameScreen homeScreen;
    private GameScreen dungeonScreen;
    private GameScreen gameOverScreen;
    private GameScreen introOverlay;
    private Room[] gameRooms;

    // enemy
    public Array<Entity> enemies1;
    public Array<Sprite> enemies;
    private double eVelocity = 5;
    private double EPLD;
    private double eStartD;
    private double xMod;
    private double yMod;


    private float yMin;
    private float yMax;
    private float xMin;
    private float xMax;
    private boolean map;
    private boolean chestOpened;
    private boolean canHeal;
    private int potionDrawn;

    public View(OrthographicCamera camera)
    {
        this.camera = camera;
        this.world = new World(new Vector2(0,0),false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.batch = new SpriteBatch();

        // Player + Health
        player = new Entity(this.world, 100, 5, STARTX, STARTY, true,"E",120,140, 1);
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
        introOverlay = new GameScreen("black_overlay.png");

        // Volume
        musicVolume = 0.5f;
        sfxVolume = 0.5f;

        create();
    }

    public void create() {
        // Assets
        // sfx Gdx.audio.newSound(Gdx.files.internal(name));

        music = Gdx.audio.newMusic(Gdx.files.internal("MenuSong.wav"));
        music.setVolume(musicVolume);
        music.setLooping(true);
        music.play();

        potion = new Texture("potion.png");

        title = new Texture("cursed_cavern.png");

        settingsSheet = new Texture("settings_cog.png");
        settingsSprite = new Sprite(settingsSheet);

        settingSheet = new Texture("settingsBG.png");
        settingAnimation = new Animation<TextureRegion>(2f, player.animationSplicer(settingSheet, 4, 4));
        settingFrames = settingAnimation.getKeyFrames();

        mapSheet = new Texture("mapBG.png");
        mapAnimation = new Animation<TextureRegion>(2f, player.animationSplicer(mapSheet, 2, 4));
        mapFrames = mapAnimation.getKeyFrames();

        executionSheet = new Texture("execution.png");
        executionAnimation = new Animation<TextureRegion>(2f, player.animationSplicer(executionSheet, 4, 7));

        wizardSheet = new Texture(Gdx.files.internal("wizardSheet.png"));
        wizardAnimation = new Animation<TextureRegion>(.7f, player.animationSplicer(wizardSheet,2, 2));

        healthSheet = new Texture(Gdx.files.internal("HealthBar.png"));
        healthBarAnimation = new Animation<TextureRegion>(.25f, player.animationSplicer(healthSheet,3, 6));

        inventoryBoxes = new Texture(Gdx.files.internal("inventory_box.png"));

        // Logic Components
        gameRooms = new Room[6];
        for (int i = 0; i < 5; i++)
        {
            if (i == 3)
                gameRooms[i] = new Room(player, 0, "Chest", 1, 0, 0, world);
        else
                gameRooms[i] = new Room(player, 1, "Normal", 1, 40* (i - 1) + 10, 4 * i, world);

        }

        enemies1 = new Array<>();
        if (roomIndex != 3)
        {
            createEnemies();
        }


        deathIndex = 0;
        mapIndex = 0;
        settingIndex = 7;
        introIndex = 0;

        chestOpened = false;
        canHeal = false;
        potionDrawn = 0;
        map = false;
        keyTime = 0;

    }

    private void createEnemies() {

        Entity enemy1 = new Entity(world,15,2,410,20, false,"W",120,140, 1);
        enemies1.add(enemy1);

    }

    @Override
    public void render(float delta)
    {
        if (roomIndex != 3)
        {
            checkDistance();
            clamp(player,enemies1.get(0));
        }

        this.update();
        super.render(delta);

        logic();
        draw();

        if(currentScreen == Screen.INTRO && introIndex == 6)
        {
            currentScreen = Screen.TRANSITION_SCREEN;
            time = 0;
            font.getData().setScale(2.5f);
        } else if (currentScreen == Screen.INTRO)
        {
            time++;
            if (time % 4 == 0)
                introArrayIndex = introArrayIndex > introIndicies[introIndex] ? introIndicies[introIndex] : introArrayIndex + 1;

        }

        if (introArrayIndex >= introIndicies[introIndex] - 1 && introIndex <= 5) {
            introIndex = introIndex >= 7 ? 6 : introIndex + 1;
            introArrayIndex = 0;
        }

        if(currentScreen == Screen.TRANSITION_SCREEN && transitionIndex == dungeonMessageIndicies[transitionArrayIndex] - 1)
        {
            if (transitionArrayIndex == 0) {
                currentScreen = Screen.MAIN_GAME;
                transitionArrayIndex++;
            }
            else
                currentScreen = Screen.MAP;
            transitionIndex = 0;

            time = 0;
            font.getData().setScale(1f);

        } else if (currentScreen == Screen.TRANSITION_SCREEN)
        {
            time++;
            if (time % 4 == 0)
                transitionIndex = transitionIndex > dungeonMessageIndicies[transitionArrayIndex] ? dungeonMessageIndicies[transitionArrayIndex] : transitionIndex + 1;

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

            homeScreen.drawbg(this.batch);
            introOverlay.drawOverlay(this.batch);

            font.draw(batch, introDialouge[introIndex][introArrayIndex], 40, 40 );

            TextureRegion wizardFrame = wizardAnimation.getKeyFrame(stateTime, true);
            batch.draw(wizardFrame, -550, -300, 600, 600);

            batch.end();
        }

        // TRANISITION SCREEN
        else if(currentScreen == Screen.TRANSITION_SCREEN) {

            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();

            font.draw(batch, dungeonMessages[transitionArrayIndex][transitionIndex], dungeonTextPosition[transitionArrayIndex], 0 );

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

            font.draw(batch, "Press Enter to continue", -190, -350);

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

            if (roomIndex == 3) {
                if (!chestOpened)
                    gameRooms[roomIndex].drawClosedChest(batch);
                else
                    gameRooms[roomIndex].drawOpenChest(batch);
                font.getData().setScale(.5f);
                font.draw(batch, "Press O to open chest", -70, -10);
                font.getData().setScale(1f);
            }
            else {
                for(Entity enemy: enemies1)
                {
                    enemy.drawSprite(batch, stateTime);
                }
            }

            batch.draw(inventoryBoxes, -330, -393, 180, 38);
            if (chestOpened)
            {
                batch.draw(potion, -334, -394, 40, 40);
                potionDrawn++;
            }

            font.draw(batch, "Player", -520, -430);


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

            if (elapsedTime > 10 && deathIndex <= 26)
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

        input();

    }

    private void resetGameForNewRoom()
    {
        player.ouchies(- (player.getMaxHealth() - player.getCHealth()));
        player.modPos((float) (-player.geteX() + STARTX), (float) (-player.geteY() + STARTY));
        enemies1 = gameRooms[roomIndex].getEnemyArray();
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
        else if (currentScreen == Screen.MAIN_GAME && Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            currentScreen = Screen.SETTINGS;
        }
        // MAP
        else if (currentScreen == Screen.MAP)
        {
            if (elapsedTime > 90 && Gdx.input.isKeyPressed(Input.Keys.ENTER))
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
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                elapsedTime = 0;
                currentScreen = Screen.MENU;
                System.out.println("R registered. Regretably, the re-start Game Feature has not yet been implemented. Please re-load the program to replay.");
            }

        }
        // MAIN_GAME
        else if (currentScreen == Screen.MAIN_GAME) {

             if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if (checkExecute(10)) {
                    player.specialChangeAnimation("Attack");

                    if (roomIndex != 3) {
                        for (Entity enemy : enemies1) {
                            enemy.takeDamage(player);
                            break;
                        }
                    }
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
            } else if (canHeal && Gdx.input.isKeyPressed(Input.Keys.H)) {
                if (checkExecute(10)) {
                    player.specialChangeAnimation("Heal");
                    player.ouchies(-50);
                    canHeal = false;
                }
            } else if (roomIndex == 3 && Gdx.input.isKeyPressed(Input.Keys.O))
            {
                chestOpened = true;
                canHeal = true;
            }
            else {
                player.updatePosition(0, 0);
            }

            //if (Gdx.input.isKeyPressed(Input.Keys.I))
            //    player.ouchies(5);
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
        /*
        else if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            currentScreen = Screen.MAIN_GAME;
        } */


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
        if (roomIndex != 3) {
            clamp(player, enemies1.get(0));

            for (Entity e : enemies1) {
                e.setX((float) MathUtils.clamp(e.geteX(), (double) -Gdx.graphics.getWidth() / 2, (double) Gdx.graphics.getWidth() / 2));
                e.setY((float) MathUtils.clamp(e.geteY(), (double) -Gdx.graphics.getHeight() / 2, (double) Gdx.graphics.getHeight() / 2));
            }
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

        // next Room
        if (isRoomBeaten()) {
            currentScreen = Screen.TRANSITION_SCREEN;
            font.getData().setScale(2.5f);
            elapsedTime = 0;
            map = false;
            roomIndex = roomIndex > 5 ? 5 : roomIndex + 1;
            resetGameForNewRoom();
        }


    }

    private boolean isRoomBeaten()
    {
        if (roomIndex != 3) {
            for (Entity e : enemies1) {
                if (!e.getAnimationFinished())
                    return false;
            }
            return true;
        } else if (chestOpened && potionDrawn > 15)
        {
            return true;
        }
        return false;
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
                if((!(e.geteX() >= e.getStartX()-20) || !(e.geteX() <= e.getStartX()+20)) || (!(e.geteY() >= e.getStartY()-20) || !(e.geteY() <= e.getStartY()+20))){
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
                    xMod = 0;
                    yMod= 0;
                }

                e.modPos((float) (xMod*eVelocity),(float) (yMod*eVelocity));

            }
            if (EPLD <=200){
                if (e.isAttackReady()){

                    e.updateAttackTime();
                    e.specialChangeAnimation("Attack");
                    player.ouchies(e.getStrength());
                    player.takeDamage(e);
                }
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

            if((getOverlap(player.getxHit(),enemy.getxHit()))>(getOverlap(player.getyHit(),enemy.getyHit()))){

                if (player.geteX()>enemy.geteX()){
                    xMin= (float) (enemy.geteX() +(enemy.getESX()/2));
                }
                if (player.geteX()<enemy.geteX()){
                    xMax= (float) (enemy.geteX() -(enemy.getESX()/2));

                }
            }
            if((getOverlap(player.getxHit(),enemy.getxHit()))<(getOverlap(player.getyHit(),enemy.getyHit()))){
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
