package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import helper.Constants;
import helper.PlayerAnimation;

import static helper.Constants.*;

public class Entity extends Actor {

    private Body body;
    private Sprite entitysprite;
    private Texture spriteTexture;
    public float speed = 5 * TILE_SIZE;
    private Vector2 currentVelocity = new Vector2(0,0);
    private entityState state;
    boolean player;

    int maxHealth;
    int str;
    int cHealth;
    boolean living;
    double eX; double eY; double startX; double startY;
    Hitbox xHit; Hitbox yHit;
    public Entity(Vector2 startPosition, World world, int health, int strength)
    {
        super();
        spriteTexture = new Texture("idlePlayer_sheet.png");
        entitysprite = new Sprite(spriteTexture);

        setBounds(startPosition.x, startPosition.y, entitysprite.getWidth(), entitysprite.getHeight());

        this.body = PlayerAnimation.createBody(PlayerX + 70, PlayerY + 50, 50, 50, false, world);
        this.body.setUserData(this);

        addListener(new FreeRoamingMovementListener(this));

        state = entityState.STANDING;

        str = strength;
        cHealth = health;
        maxHealth = health;
        living = true;
        eX = PlayerX;
        eY = PlayerY;
        xHit = new Hitbox(eX);
        yHit = new Hitbox(eY);
   }
    public Entity(World world, int health, int strength,int x, int y)
    {
        super();
        spriteTexture = new Texture("idlePlayer_sheet.png");
        entitysprite = new Sprite(spriteTexture);
        eX = x;
        startX = x;
        eY = y;
        startY = y;
        xHit = new Hitbox(eX);
        yHit = new Hitbox(eY);

        setBounds(x,y, entitysprite.getWidth(), entitysprite.getHeight());

        this.body = PlayerAnimation.createBody(PlayerX + 70, PlayerY + 50, 50, 50, false, world);
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
    public void modPos(double xMod, double yMod){
        eX = eX + xMod;
        xHit.update(eX);
        eY = eY + yMod;
        yHit.update(eY);
    }
    public void setPlayer(){
        player = true;

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

}
