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
            case WALKING_N:
                return "_walkingN.png";
            case WALKING_S:
                return "_walkingS.png";
            case WALKING_E:
                return "_walkingE.png";
            case ATTACK_E:
                return "_attackE.png";
            case WALKING_W:
                return "_walkingW.png";
            case ATTACK_W:
                return "_attackW.png";
            case HEAL:
               return "_heal.png";
            default:
                return "_standing.png";
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
