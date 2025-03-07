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

/**
 * Represents an entity (such as a player or enemy) in the game, including attributes like health, strength,
 * position, animation, and interaction with other entities. The entity can be controlled, move, attack, and
 * take damage, with animations and hitboxes to reflect its actions and state.
 */
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

    /**
     * Constructs an entity with the specified parameters, initializing its attributes, hitboxes, and animation.
     *
     * @param world The physics world where the entity resides.
     * @param health The maximum health of the entity.
     * @param strength The strength of the entity, used for dealing damage.
     * @param x The initial X position of the entity.
     * @param y The initial Y position of the entity.
     * @param isPlayer Flag indicating if the entity is a player.
     * @param facing The initial facing direction of the entity (N, S, E, W).
     * @param xS The width of the entity.
     * @param yS The height of the entity.
     * @param sizeFactor The scaling factor for the entity's sprite.
     */
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
        xHit = new Hitbox(eX,xS/2.5);
        yHit = new Hitbox(eY,yS/2.75);
        xAttack = new Hitbox(eX,50*sizeFactor);
        yAttack = new Hitbox(eY,50*sizeFactor);

        entityHeight = 100 * sizeFactor;
        entityWidth = 100 * sizeFactor;
        lastAttackTime = System.currentTimeMillis();

        setBounds(x,y, allSprites[0].getWidth(), allSprites[0].getHeight());

        state = entityState.STANDING;

        str = strength;
        cHealth = health;
        maxHealth = health;
        living = true;
        specialAnimation = false;
        animationFinished = false;
    }

    /**
     * Updates the position of the entity based on the movement factors.
     *
     * @param factorX The horizontal movement factor.
     * @param factorY The vertical movement factor.
     */
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

    /**
     * Modifies the entity's position by the specified amounts in the X and Y axes.
     *
     * @param xMod The amount to modify the X position.
     * @param yMod The amount to modify the Y position.
     */
    public void modPos(float xMod, float yMod){

        facX = xMod;
        facY = yMod;

        eX = eX + xMod;
        xHit.updateMod(eX,xSize/2.5);
        eY = eY + yMod;
        yHit.updateMod(eY,ySize/2.75);

        changeAnimation();
    }

    /**
     * Initializes all the sprites for the entity based on its state (e.g., walking, standing).
     */
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

    /**
     * Checks if the current animation has finished playing.
     *
     * @return True if the animation has finished, false otherwise.
     */
    public boolean getAnimationFinished()
    {
        return animationFinished;
    }

    /**
     * Draws the entity's current animation frame to the screen.
     *
     * @param batch The SpriteBatch used for drawing the entity.
     * @param stateTime The elapsed time used for animation timing.
     */
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


    /**
     * Splits the given texture into an array of texture regions for animation.
     *
     * @param texture The texture to split.
     * @param COLS The number of columns in the sprite sheet.
     * @param ROWS The number of rows in the sprite sheet.
     * @return An array of texture regions representing the animation frames.
     */
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

    /**
     * Retrieves the body associated with the entity for collision detection.
     *
     * @return The entity's body.
     */
    public Body getBody() {
        return this.body;
    }

    /**
     * Changes the current animation based on the entity's movement or state.
     */
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

    /**
     * Changes the entity's animation based on a special modifier (e.g., attack, death).
     *
     * @param modifier The modifier determining the animation change (e.g., "Attack", "Death").
     */
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

    /**
     * Modifies the entity's health by the specified damage amount.
     *
     * @param damage The amount of damage to deal to the entity.
     * @return The entity's current health after taking damage.
     */
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

    /**
     * Retrieves the entity's current health.
     *
     * @return The entity's current health.
     */
    public int getCHealth() {
        return cHealth;
    }

    /**
     * Retrieves the entity's maximum health.
     *
     * @return The entity's maximum health.
     */
    public int getMaxHealth() { return maxHealth; }

    /**
     * Checks if the entity is still alive.
     *
     * @return True if the entity is alive, false otherwise.
     */
    public boolean isLiving() { return living; }

    /**
     * Retrieves the entity's X position.
     *
     * @return The entity's X position.
     */
    public double geteX(){
        return eX;
    }

    /**
     * Retrieves the entity's Y position.
     *
     * @return The entity's Y position.
     */
    public double geteY(){
        return eY;
    }

    /**
     * Retrieves the entity's initial X position.
     *
     * @return The entity's initial X position.
     */
    public double getStartX(){
        return startX;
    }

    /**
     * Retrieves the entity's initial Y position.
     *
     * @return The entity's initial Y position.
     */
    public double getStartY(){
        return startY;
    }

    /**
     * Checks if the entity is a player.
     *
     * @return True if the entity is a player, false otherwise.
     */
    public boolean isPlayer(){
        return player;

    }

    /**
     * Retrieves the entity's X hitbox.
     *
     * @return The entity's X hitbox.
     */
    public Hitbox getxHit(){
        return xHit;
    }

    /**
     * Retrieves the entity's Y hitbox.
     *
     * @return The entity's Y hitbox.
     */
    public Hitbox getyHit(){
        return yHit;
    }

    /**
     * Forces an update to the entity's hitboxes with new X and Y positions.
     *
     * @param x The new X position.
     * @param y The new Y position.
     */
    public void forceHUpdate(double x, double y){
        yHit.updateMod(eY,ySize/2.75);
        xHit.updateMod(eX,xSize/2.5);
    }

    /**
     * Sets the entity's position to the specified X and Y coordinates.
     *
     * @param x The new X position.
     * @param y The new Y position.
     */
    public void setPos(double x, double y){
        eX = x;
        eY = y;
    }

    /**
     * Determines the entity's facing direction based on the provided string.
     *
     * @param direction The facing direction as a string (N, S, E, W).
     */
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

    /**
     * Retrieves the entity's current facing direction.
     *
     * @return The entity's facing direction.
     */
    public Facing getFacing(){
        return eDirection;
    }

    /**
     * Updates the entity's attack box based on its facing direction.
     */
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

    /**
     * Retrieves the entity's attack hitbox along the X-axis.
     *
     * @return The entity's X attack hitbox.
     */
    public Hitbox getEAX(){return xAttack;}

    /**
     * Retrieves the entity's attack hitbox along the Y-axis.
     *
     * @return The entity's Y attack hitbox.
     */
    public Hitbox getEAY(){return yAttack;}

    /**
     * Retrieves the entity's strength.
     *
     * @return The entity's strength.
     */
    public int getStrength(){return str;}

    /**
     * Handles the entity taking damage from another entity.
     *
     * @param enemy The entity dealing damage.
     */
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

    /**
     * Retrieves the entity's width along the X-axis.
     *
     * @return The entity's width.
     */
    public double getESX(){return xSize;}

    /**
     * Retrieves the entity's height along the Y-axis.
     *
     * @return The entity's height.
     */
    public double getESY(){return ySize;}

    /**
     * Checks if the entity's attack is ready to be performed based on cooldown.
     *
     * @return True if the attack is ready, false otherwise.
     */
    public boolean isAttackReady(){
        int attackCooldown = 3000;
        return (System.currentTimeMillis() - lastAttackTime) > attackCooldown;
    }

    /**
     * Updates the timestamp of the last attack performed by the entity.
     */
    public void updateAttackTime(){
        lastAttackTime = System.currentTimeMillis();
    }
}
