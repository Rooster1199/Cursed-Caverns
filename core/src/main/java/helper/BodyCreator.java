package helper;

import com.badlogic.gdx.physics.box2d.*;

import static helper.Constants.PPM;

public class BodyCreator {

    public static Body createBody(float x, float y, float width, float height, boolean isStatic, World world) {
        BodyDef bodydef = new BodyDef();
        bodydef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bodydef.position.set(x/PPM, y / PPM);
        bodydef.fixedRotation = true;
        Body body = world.createBody(bodydef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();
        return body;
    }

}
