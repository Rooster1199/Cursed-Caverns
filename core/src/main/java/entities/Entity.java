package entities;
import java.io.*;
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
import io.github.killtheinnocents.View;

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
    int[][] colsAndRows;
    float[] animationSpeed;

    private Body body;
    public float speed = 5 * TILE_SIZE;
    private Vector2 currentVelocity = new Vector2(0,0);
    public entityState state;
    boolean player;

    double lastAttackTime;

    int maxHealth;
    int str;
    int cHealth;
    boolean living;
    double eX; double eY; double startX; double startY; // was double before, change back if issue!
    float facX; float facY;
    enum Facing{
        NORTH,
        SOUTH,
        EAST,
        WEST
    }
    Facing eDirection;
    Hitbox xHit; Hitbox yHit;
    Hitbox xAttack; Hitbox yAttack;
    double xSize; double ySize;

    double entityHeight; double entityWidth;

    boolean specialAnimation;
    int Time = 0;

    public Entity(World world, int health, int strength, int x, int y, boolean isPlayer,String facing, double xS, double yS, double sizeFactor)
    {
        super();
        updateAttackTime();
        getDirection(facing);
        xSize = xS;
        ySize = yS;
        player = isPlayer;
        colsAndRows = player ? playerColsAndRows : enemyColsAndRows;
        animationSpeed = player ? playerAnimationSpeed : enemyAnimationSpeed;
        initializeAllSprites();

        eX = x;
        startX = x;
        eY = y;
        startY = y;
        facX = 0;
        facY = 0;
        xHit = new Hitbox(eX);
        yHit = new Hitbox(eY);
        xAttack = new Hitbox(eX);
        yAttack = new Hitbox(eY);

        entityHeight = 100 * sizeFactor;
        entityWidth = 100 * sizeFactor;

        setBounds(x,y, allSprites[0].getWidth(), allSprites[0].getHeight());

//        this.body = BodyCreator.createBody(STARTX + 70, STARTY + 50, 50, 50, false, world);
//        this.body.setUserData(this);

//        addListener(new FreeRoamingMovementListener(this));

        state = entityState.STANDING;

        str = strength;
        cHealth = health;
        maxHealth = health;
        living = true;
        specialAnimation = false;
    }


    public void updatePosition(float factorX, float factorY)
    {
        float speed = 300f;
        float time = Gdx.graphics.getDeltaTime();

        facX = factorX;
        facY = factorY;

        eX += factorX * speed * time;
        eX = MathUtils.clamp(eX,  Gdx.graphics.getWidth() / -2, 460);
        eY += factorY * speed * time;
        eY = MathUtils.clamp(eY,  -300, 200);

        changeAnimation();
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

                System.out.println(allTextures[index]);
                System.out.println(allSprites[index]);
                index++;
            }


        }

        currentAnimation =  new Animation<TextureRegion>(.25f, animationSplicer(allTextures[animationIndex],colsAndRows[animationIndex][0], colsAndRows[animationIndex][1]));

        System.out.println(characterState + " " + allTextures[animationIndex]);

    }

    public void drawSprite(SpriteBatch batch, float stateTime)
    {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, (float) eX, (float) eY, (float) entityWidth, (float) entityHeight);

        if(specialAnimation)
        {
            Time++;
        }

        if (Time > currentAnimation.getAnimationDuration())
        {
            Time = 0;
            specialAnimation = false;
        }
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

    public void changeAnimation() {
        //String animation = state.determineAnimation(this);

        if(!specialAnimation) {


            if (state != entityState.HEAL || state != entityState.ATTACKE || state != entityState.ATTACKW || state != entityState.DEATH) {
                if (facX == 0 && facY == 0) {
                    state = entityState.STANDING;
                } else if (facY == 0) {
                    if (facX > 0) {
                        state = entityState.WALKE;
                    } else {
                        state = entityState.WALKW;
                    }

                } else if (facX == 0) {
                    if (facY > 0) {
                        state = entityState.WALKN;
                    } else {
                        state = entityState.WALKS;
                    }
                }
            }
        }

        animationIndex = state.determineIndex(this);
        currentAnimation =  new Animation<TextureRegion>(animationSpeed[animationIndex], animationSplicer(allTextures[animationIndex],colsAndRows[animationIndex][0], colsAndRows[animationIndex][1]));

        //System.out.println(animation);

    }

    public void specialChangeAnimation(String modifier) {

        if (!specialAnimation) {
            if (modifier.equals("Attack")) {
                if (facX < 0) {
                    state = entityState.ATTACKW;
                } else {
                    state = entityState.ATTACKE;
                }
            } else if (player) {
                state = entityState.HEAL;
            } else {
                state = entityState.DEATH;
            }
            animationIndex = state.determineIndex(this);
            specialAnimation = true;
            currentAnimation = new Animation<TextureRegion>(animationSpeed[animationIndex], animationSplicer(allTextures[animationIndex], colsAndRows[animationIndex][0], colsAndRows[animationIndex][1]));
        }

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

        facX = xMod - (float) eX;
        facY = yMod - (float) eY;

        eX = eX + xMod;
        xHit.update(eX);
        eY = eY + yMod;
        yHit.update(eY);

        changeAnimation();
    }

    public boolean isPlayer(){
        return player;

    }
    public Hitbox getxHit(){
        return xHit;
    }
    public Hitbox getyHit(){
        return yHit;
    }
    public void forceHUpdate(double x, double y){
        yHit.update(y);
        xHit.update(x);
    }
    public void setPos(double x, double y){
        eX = x;
        eY = y;
    }
    public void getDirection(String direction){
        switch (direction){
            case "N":
                eDirection = Facing.NORTH;
                break;
            case "S":
                eDirection = Facing.SOUTH;
                break;
            case "E":
                eDirection = Facing.EAST;
                break;
            case  "W":
                eDirection = Facing.WEST;
                break;
        }
    }
    public Facing getFacing(){
        return eDirection;
    }
    public void attackBox(){
        switch (eDirection){
            case NORTH:
                xAttack.updateMod(eX,xSize/2);
                yAttack.updateMod(eY+ySize,ySize/4);
            case SOUTH:
                xAttack.updateMod(eX,xSize/2);
                yAttack.updateMod(eY-ySize,ySize/4);
            case WEST:
                xAttack.updateMod(eX-xSize,xSize/2);
                yAttack.updateMod(eY+ySize/2,ySize/4);

            case EAST:
                xAttack.updateMod(eX+xSize,xSize/2);
                yAttack.updateMod(eY+ySize/2,ySize/4);
        }
    }
    public Hitbox getEAX(){return xAttack;}
    public Hitbox getEAY(){return yAttack;}
    public int getStrength(){return str;}
    public void takeDamage(Entity enemy){
        if(View.checkOverlap(xHit,enemy.getEAX()) || View.checkOverlap(enemy.getEAY(),yHit)){

            if(cHealth+enemy.getStrength()>maxHealth){cHealth=maxHealth;}
            if(cHealth-enemy.getStrength()<=0){
                living = false;
                cHealth=0;}
            else{cHealth-=enemy.getStrength();}
            }
    }
    public double getESX(){return xSize;}
    public double getESY(){return ySize;}
    public boolean isAttackReady(){
        int attackCooldown = 3000;
        return (System.currentTimeMillis() - lastAttackTime) > attackCooldown;
    }
    public void updateAttackTime(){
        lastAttackTime = System.currentTimeMillis();
    }
}
