package helper;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents an object in the game, which has a type, texture, pixel position, and healing factor.
 * The object can be used for various in-game purposes, such as healing or other interactions.
 */
public class Object {

    private String type;
    private Texture objectTexture;

    private int[] pixelPosition = new int[2];

    private int healAmount;

    /**
     * Constructs an Object with the specified type, texture file, position, and healing factor.
     *
     * @param objectType The type of the object (e.g., "health", "weapon").
     * @param filename The file path to the texture image used for the object.
     * @param pixelX The X position of the object in pixels.
     * @param pixelY The Y position of the object in pixels.
     * @param healingFactor The amount of healing the object provides when interacted with.
     */
    public Object(String objectType, String filename, int pixelX, int pixelY, int healingFactor)
    {
        type = objectType;
        objectTexture = new Texture(filename);
        pixelPosition[0] = pixelX;
        pixelPosition[1] = pixelY;
        healAmount = healingFactor;

    }

}
