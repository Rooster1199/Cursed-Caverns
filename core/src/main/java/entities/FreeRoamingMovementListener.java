package entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.HashSet;
import java.util.Set;

public class FreeRoamingMovementListener extends InputListener {

    private final Entity entity;
    private final Set<Integer> pressedKeyCodes = new HashSet<>();

    public FreeRoamingMovementListener(Entity entity) {
        this.entity = entity;
    }

    private entityState getPlayerState() {
        if (pressedKeyCodes.contains(Input.Keys.UP)) {
            return entityState.WALKN;
        } else if (pressedKeyCodes.contains(Input.Keys.DOWN))
        {
            return entityState.WALKS;
        } else if (pressedKeyCodes.contains(Input.Keys.RIGHT))
        {
            return entityState.WALKE;
        } else if (pressedKeyCodes.contains(Input.Keys.LEFT))
        {
            return entityState.WALKW;
        } else
        {
            return null;
        }
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode)
    {
        pressedKeyCodes.add(keycode);

        entityState state = getPlayerState();
        if (state == null)
        {
            pressedKeyCodes.remove(keycode);
            return false;
        }

        Vector2 newVelocity = state.calculateDirection();

        entity.setStateAndVelocity(state, newVelocity);

        return true;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode)
    {
        pressedKeyCodes.remove(keycode);

        entityState state = getPlayerState();

        Vector2 newVelocity = Vector2.Zero;

        entity.setStateAndVelocity(state, newVelocity);

        return true;

    }
}
