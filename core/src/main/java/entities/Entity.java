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
import io.github.killtheinnocents.View;

import static helper.Constants.*;

public class Entity extends Actor {

    // Sprites + Textures
    private Animation<TextureRegion> currentAnimation;
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
    boolean animationFinished;
    float Time = 0;

    public Entity(World world, int health, int strength, int x, int y, boolean isPlayer,String facing, double xS, double yS, double sizeFactor)
    {
        super();

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
        xHit = new Hitbox(eX,xS/3);
        yHit = new Hitbox(eY,yS/2.75);
        xAttack = new Hitbox(eX,50*sizeFactor);
        yAttack = new Hitbox(eY,50*sizeFactor);

        entityHeight = 100 * sizeFactor;
        entityWidth = 100 * sizeFactor;
        lastAttackTime = System.currentTimeMillis();

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
        animationFinished = false;
    }


    public void updatePosition(float factorX, float factorY)
    {
        float speed = 160f;
        float time = Gdx.graphics.getDeltaTime();

        facX = factorX;
        facY = factorY;

        eX += factorX * speed * time;
        eX = MathUtils.clamp(eX,  Gdx.graphics.getWidth() / -2, 460);
        eY += factorY * speed * time;
        eY = MathUtils.clamp(eY,  -300, 200);

        changeAnimation();
    }

    public void modPos(float xMod, float yMod){

        facX = xMod;
        facY = yMod;

        eX = eX + xMod;
        xHit.updateMod(eX,xSize/3);
        eY = eY + yMod;
        yHit.updateMod(eY,ySize/2.75);

        changeAnimation();
    }

    public void initializeAllSprites() {
        String characterState = player ? "player" : "enemy";
        int index = 0;

        for (entityState state : entityState.values()) {
            String filename = characterState + "_" + state + ".png";

            if (characterState.equals("player") && state != entityState.DEATH) {
                allTextures[index] = new Texture(filename);
                allSprites[index] = new Sprite(allTextures[index]);

                index++;

            } else if (characterState.equals("enemy") && state != entityState.HEAL) {
                allTextures[index] = new Texture(filename);
                allSprites[index] = new Sprite(allTextures[index]);

                index++;
            }

        }

        currentAnimation =  new Animation<TextureRegion>(.25f, animationSplicer(allTextures[animationIndex],colsAndRows[animationIndex][0], colsAndRows[animationIndex][1]));

    }

    public boolean getAnimationFinished()
    {
        return animationFinished;
    }

    public void drawSprite(SpriteBatch batch, float stateTime)
    {
        if (!player && !living)
            specialChangeAnimation("Death");
        stateTime = specialAnimation ? Time : stateTime;
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        if (!animationFinished)
            batch.draw(currentFrame, (float) eX, (float) eY, (float) entityWidth, (float) entityHeight);

        if(specialAnimation)
            Time += animationSpeed[animationIndex] * .1f;


        if (Time > (animationSpeed[animationIndex] * (colsAndRows[animationIndex][0]) * (colsAndRows[animationIndex][1])))
        {
            if (!player && !living){
                batch.draw(animationSplicer(allTextures[7], colsAndRows[7][0], colsAndRows[7][1])[15], (float) eX, (float) eY, (float) entityWidth, (float) entityHeight);
                animationFinished = true;
            } else {
                Time = 0;
                animationIndex = 0;
                specialAnimation = false;
            }
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

            if (Math.abs(facX) < 0.01 && Math.abs(facY) < 0.01) {
                state = entityState.STANDING;
            } else if (Math.abs(facX) > Math.abs(facY)) {
                if (facX > 0) {
                    state = entityState.WALKE;
                } else {
                    state = entityState.WALKW;
                }

            } else if (Math.abs(facX) < Math.abs(facY)) {
                if (facY > 0) {
                    state = entityState.WALKN;
                } else {
                    state = entityState.WALKS;
                }
            }

            animationIndex = state.determineIndex(this);
            currentAnimation =  new Animation<TextureRegion>(animationSpeed[animationIndex], animationSplicer(allTextures[animationIndex],colsAndRows[animationIndex][0], colsAndRows[animationIndex][1]));

        }

    }

    public void specialChangeAnimation(String modifier) {

        if (!specialAnimation) {
            specialAnimation = true;
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
        yHit.updateMod(eY,xSize/3);
        xHit.updateMod(eX,ySize/2.75);
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

        //System.out.println(View.checkOverlap(xHit,enemy.getEAX()) + " L " + View.checkOverlap(enemy.getEAY(),yHit));
        if(View.checkOverlap(xHit,enemy.getEAX()) && View.checkOverlap(enemy.getEAY(),yHit)){
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
