package entities;

import com.badlogic.gdx.math.Vector2;

public enum entityState {
    STANDING,
    WALKING_N, WALKING_S, WALKING_E, WALKING_W,
    ATTACK_E, ATTACK_W,
    HEAL,
    ;

    public Vector2 calculateDirection()
    {
        // multiple cases not supported in java 8!!
        switch (this) {
            case STANDING:
                return new Vector2(0, 0);
            case WALKING_N:
                return new Vector2(0, 1);
            case HEAL:
                return new Vector2(0, 1);
            case WALKING_S:
                return new Vector2(0, -1);
            case WALKING_E:
                return new Vector2(1, 0);
            case ATTACK_E:
                return new Vector2(1, 0);
            case WALKING_W:
                return new Vector2(-1, 0);
            case ATTACK_W:
                return new Vector2(-1, 0);
            default:
                return new Vector2(0, 0);
        }
    }
}
