package entities;

/**
 * Represents a hitbox for collision detection, defined by a minimum and maximum coordinate range.
 * The hitbox is used to determine whether an object is within a certain area or has collided with another entity.
 */
public class Hitbox {
    public double min;
    public double max;

    /**
     * Constructs a new hitbox with the specified center coordinate and modifier.
     * The hitbox is defined by the center coordinate ± the modifier value.
     *
     * @param coord The center coordinate of the hitbox.
     * @param mod The modifier that determines the size of the hitbox around the center.
     */
    public Hitbox(double coord, double mod){
        this.min = coord - mod;
        this.max = coord + mod;
    }

    /**
     * Updates the hitbox boundaries with a new center coordinate and modifier.
     * The hitbox will be recalculated based on the new values.
     *
     * @param coord The new center coordinate of the hitbox.
     * @param mod The new modifier that determines the size of the hitbox around the center.
     */
    public void updateMod(double coord, double mod){
        this.min = coord - mod;
        this.max = coord + mod;
    }

}
