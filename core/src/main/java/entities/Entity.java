package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import helper.BodyCreator;
import helper.Constants;

import static helper.Constants.*;

public class Entity extends Actor {

    // Sprites + Textures
    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> standingAnimation;
    private Animation<TextureRegion> walkNAnimation;
    private Animation<TextureRegion> walkEAnimation;
    private Animation<TextureRegion> walkWAnimation;
    private Animation<TextureRegion> walkSAnimation;
    private Animation<TextureRegion> attackEAnimation;
    private Animation<TextureRegion> attackWAnimation;
    /*
    private Sprite standingSprite;
    private Texture standingTexture;
    private Sprite walkNSprite;
    private Texture walkNTexture;
    private Sprite walkESprite;
    private Texture walkETexture;
    private Sprite walkWSprite;
    private Texture walkWTexture;
    private Sprite walkSSprite;
    private Texture walkSTexture;
    private Sprite attackESprite;
    private Texture attackETexture;
    private Sprite attackWSprite;
    private Texture attackWTexture; */
    Texture[] allTextures = new Texture[8];
    Sprite[] allSprites = new Sprite[8];
    int animationIndex = 0;

    private Body body;
    public float speed = 5 * TILE_SIZE;
    private Vector2 currentVelocity = new Vector2(0,0);
    public entityState state;
    boolean player;

    int maxHealth;
    int str;
    int cHealth;
    boolean living;
    double eX; double eY; double startX; double startY; // was double before, change back if issue!
    int facX; int facY;

    public Entity(Vector2 startPosition, World world, int health, int strength, String filename_prefix, boolean isPlayer)
    {
        super();
        player = isPlayer;
        initializeAllSprites();

        setBounds(startPosition.x, startPosition.y, allSprites[0].getWidth(), allSprites[0].getHeight());

        this.body = BodyCreator.createBody(STARTX + 70, STARTY + 50, 50, 50, false, world);
        this.body.setUserData(this);

        addListener(new FreeRoamingMovementListener(this));

        state = entityState.STANDING;

        str = strength;
        cHealth = health;
        maxHealth = health;
        living = true;
   }
    public Entity(World world, int health, int strength, String filename_prefix, int x, int y, boolean isPlayer)
    {
        super();
        player = isPlayer;
        initializeAllSprites();

        eX = x;
        startX = x;
        eY = y;
        startY = y;
        facX = 0;
        facY = 0;

        setBounds(x,y, allSprites[0].getWidth(), allSprites[0].getHeight());

        this.body = BodyCreator.createBody(STARTX + 70, STARTY + 50, 50, 50, false, world);
        this.body.setUserData(this);

        addListener(new FreeRoamingMovementListener(this));

        state = entityState.STANDING;

        str = strength;
        cHealth = health;
        maxHealth = health;
        living = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        trackMovement(delta);
    }

    public void updatePosition(int factorX, int factorY)
    {
        float speed = 300f;
        float time = Gdx.graphics.getDeltaTime();

        facX = factorX;
        facY = factorY;

        eX += factorX * speed * time;
        eX = MathUtils.clamp(eX,  Gdx.graphics.getWidth() / -2, 807);
        eY += factorY * speed * time;
        eY = MathUtils.clamp(eY,  -540, 350);

    }

    public void initializeAllSprites() {
        String characterState = player ? "player" : "enemy";
        System.out.println(characterState + " " + player);
        int index = 0;

        for (entityState state : entityState.values()) {
            String filename = characterState + "_" + state + ".png";

            if (characterState.equals("player") && state != entityState.DEATH) {
                allTextures[index] = new Texture(filename);
                allSprites[index] = new Sprite(allTextures[index]);

//                System.out.println(allTextures[index]);
//                System.out.println(allSprites[index]);
                index++;

            } else if (characterState.equals("enemy") && state != entityState.HEAL) {
                allTextures[index] = new Texture(filename);
                allSprites[index] = new Sprite(allTextures[index]);

//                System.out.println(allTextures[index]);
//                System.out.println(allSprites[index]);
                index++;
            }

        }

        currentAnimation =  new Animation<TextureRegion>(.25f, animationSplicer(allTextures[animationIndex],playerColsAndRows[animationIndex][0], playerColsAndRows[animationIndex][1]));

        System.out.println(characterState + " " + allTextures[animationIndex]);

    }

    public void drawSprite(SpriteBatch batch, float stateTime)
    {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, (float) eX, (float) eY, 170, 170);
    }


    public TextureRegion[] animationSplicer(Texture texture, int COLS, int ROWS)
    {
        TextureRegion[][] tmp = TextureRegion.split(texture,
                texture.getWidth() / COLS,
                texture.getHeight() / ROWS);
        TextureRegion[] walkFrames = new TextureRegion[COLS * ROWS];
        int index = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        return walkFrames;
    }

    public Body getBody() {
        return this.body;
    }

    private void trackMovement(float delta) {
        float movement = delta * speed;
        body.setLinearVelocity(currentVelocity.cpy().scl(movement));
        this.setPosition(body.getPosition().x - TILE_SIZE / 2 / Constants.PPM, body.getPosition().y - TILE_SIZE / 2 / Constants.PPM);
        // tile_size may not be the correct value here
    }

    void setStateAndVelocity(entityState newState, Vector2 newVelocity)
    {
        currentVelocity = newVelocity;
        body.setLinearVelocity(currentVelocity);

        state = newState;

        // update state --> change animation

    }

    public void changeAnimation() {
        String animation = state.determineAnimation(this);

        if (state != entityState.HEAL || state != entityState.ATTACKE || state != entityState.ATTACKW || state != entityState.DEATH) {
            if ( facX == 0 && facY == 0)
            {
                state = entityState.STANDING;
            } else if (facY == 0)
            {
                if (facX > 0)
                {
                    state = entityState.WALKE;
                } else {
                    state = entityState.WALKW;
                }

            } else if (facX == 0)
            {
                if (facY > 0)
                {
                    state = entityState.WALKN;
                } else {
                    state = entityState.WALKS;
                }
            }
        }

        animationIndex = state.determineIndex(this);

        //System.out.println(animation);

    }

    public int ouchies(int damage) {
        cHealth -= damage;
        if (cHealth > maxHealth) {
            cHealth = maxHealth;
        } else if (cHealth <= 0)
        {
            cHealth = 0;
            living = false;
        }
        return cHealth;
    }

    public int inflictWound() {
        return 5 + str; //add real math
    }

    public int getCHealth() {
        return cHealth;
    }

    public int getMaxHealth() { return maxHealth; }

    public boolean isLiving() { return living; }

    public double geteX(){
        return eX;
    }
    public double geteY(){
        return eY;
    }
    public double getStartX(){
        return startX;
    }
    public double getStartY(){
        return startY;
    }
    public void modPos(float xMod, float yMod){
        eX = eX + xMod;
        eY = eY + yMod;
    }

    public boolean isPlayer(){
        return player;

    }
}
