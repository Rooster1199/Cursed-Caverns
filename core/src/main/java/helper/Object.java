package helper;

import com.badlogic.gdx.graphics.Texture;

public class Object {

    private String type;
    private Texture objectTexture;

    private int[] pixelPosition = new int[2];

    private int healAmount;

    public Object(String objectType, String filename, int pixelX, int pixelY, int healingFactor)
    {
        type = objectType;
        objectTexture = new Texture(filename);
        pixelPosition[0] = pixelX;
        pixelPosition[1] = pixelY;
        healAmount = healingFactor;

    }

}
