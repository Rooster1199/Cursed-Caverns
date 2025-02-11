package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

    public Entity(Vector2 startPosition, World world)
    {
        super();
        spriteTexture = new Texture("idlePlayer_sheet2.png");
        entitysprite = new Sprite(spriteTexture);

        setBounds(startPosition.x, startPosition.y, entitysprite.getWidth(), entitysprite.getHeight());

        this.body = PlayerAnimation.createBody(PlayerX + 70, PlayerY + 50, 50, 50, false, world);
        this.body.setUserData(this);

        addListener(new FreeRoamingMovementListener(this));

        state = entityState.STANDING;
   }

    @Override
    public void act(float delta) {
        super.act(delta);

        trackMovement(delta);
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

}
