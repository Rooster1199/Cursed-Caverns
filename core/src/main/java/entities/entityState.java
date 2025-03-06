package entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Enum representing various states of an entity, such as movement, attack, healing, or death.
 */
public enum entityState {
    STANDING,
    WALKN, WALKS, WALKE, WALKW,
    ATTACKE, ATTACKW,
    HEAL,
    DEATH,
    ;

    /**
     * Determines the index associated with the entity's current state.
     *
     * @param e The entity whose state is used to determine the index.
     * @return The index corresponding to the entity's state.
     */
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

}
