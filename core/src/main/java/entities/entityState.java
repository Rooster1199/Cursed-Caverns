package entities;

import com.badlogic.gdx.math.Vector2;

public enum entityState {
    STANDING,
    WALKING_N, WALKING_S, WALKING_E, WALKING_W,
    ATTACK_E, ATTACK_W,
    HEAL,
    ;

    public String determineAnimation(Entity e) {
        switch (e.state) {
            case STANDING:
                return "STANDING";
            case WALKING_N:
                return "WALKING_N";
            case WALKING_S:
                return "WALKING_S";
            case WALKING_E:
                return "WALKING_E";
            case ATTACK_E:
                return "ATTACK_E";
            case WALKING_W:
                return "WALKING_W";
            case ATTACK_W:
                return "ATTACK_W";
            case HEAL:
               return "HEAL";
            default:
                return "DEFAULT";
        }
    }

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
