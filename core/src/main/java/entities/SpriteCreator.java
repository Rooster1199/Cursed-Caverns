package entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import helper.Constants;

public class SpriteCreator {

    private Sprite sprites;

    private SpriteCreator() {
        //sprites = newTextureAtlas(Gdx.files.internal("spritesheets/idlePLayer.tpsheet"));
    }

//    public Sprite createSprite(String spriteFileName)
//    {
//        Sprite sprite = new Sprite(gameSprites.createSprite(spriteFileName));
//        resizeSprite(sprite);
//        return sprite;
//    }

    private void resizeSprite(Sprite sprite)
    {
        sprite.setSize(spriteUnitsToTileUnits(sprite.getWidth()), spriteUnitsToTileUnits(sprite.getHeight()));
    }

    public static float spriteUnitsToTileUnits(float number)
    {
        return number / Constants.TILE_SIZE;
    }
}
