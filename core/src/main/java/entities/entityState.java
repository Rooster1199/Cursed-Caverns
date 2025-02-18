package entities;

import com.badlogic.gdx.math.Vector2;

public enum entityState {
    STANDING,
    WALKN, WALKS, WALKE, WALKW,
    ATTACKE, ATTACKW,
    HEAL,
    DEATH,
    ;

    // this function is for testing purposes only
    // No real usage within the program
    public String determineAnimation(Entity e) {
        switch (e.state) {
            case WALKN:
                return "_WALKN.png";
            case WALKS:
                return "_walkingS.png";
            case WALKE:
                return "_walkingE.png";
            case ATTACKE:
                return "_attackE.png";
            case WALKW:
                return "_walkingW.png";
            case ATTACKW:
                return "_attackW.png";
            case HEAL:
               return "_heal.png";
            case DEATH:
                return "_death.png";
            default:
                return "_standing.png";
        }
    }

    public int determineIndex(Entity e) {
        switch (e.state) {
            case WALKN:
                return 1;
            case WALKS:
                return 2;
            case WALKE:
                return 3;
            case ATTACKE:
                return 5;
            case WALKW:
                return 4;
            case ATTACKW:
                return 6;
            case HEAL:
                return 7;
            case DEATH:
                return 7;
            default:
                return 0;
        }
    }

    public Vector2 calculateDirection()
    {
        // multiple cases not supported in java 8!!
        switch (this) {
            case STANDING:
                return new Vector2(0, 0);
            case WALKN:
                return new Vector2(0, 1);
            case HEAL:
                return new Vector2(0, 1);
            case WALKS:
                return new Vector2(0, -1);
            case WALKE:
                return new Vector2(1, 0);
            case ATTACKE:
                return new Vector2(1, 0);
            case WALKW:
                return new Vector2(-1, 0);
            case ATTACKW:
                return new Vector2(-1, 0);
            default:
                return new Vector2(0, 0);
        }
    }
}
