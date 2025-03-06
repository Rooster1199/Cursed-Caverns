package helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import entities.Entity;
import helper.Constants.*;

import static helper.Constants.potentialEnemyPositions;

public class Room {

    private Entity Player;
    private Array<Entity> enemies;

    private int positionIndex;

    private String roomType;

    private Texture chestTexture;
    private Sprite chestSprite;

    private Texture openChestTexture;
    private Sprite openChestSprite;

    public Room (Entity givenPlayer, int enemyCount, String roomType, int sizeFactor, int enemyHealth, int enemyStrength, World world)
    {
        Player = givenPlayer;

        if (roomType.equals("Normal")) {
            enemies = new Array<>();
            positionIndex = 0;

            for (int enemy = 0; enemy < enemyCount; enemy++) {
                enemies.add(new Entity(world, enemyHealth, enemyStrength, potentialEnemyPositions[positionIndex][0], potentialEnemyPositions[positionIndex][1], false, "W", 120, 140, sizeFactor));
                positionIndex = positionIndex > 5 ? 0 : positionIndex + 1;
            }
        } else if (roomType.equals("Chest"))
        {
            // TODO:
            chestTexture = new Texture("closedchest.png");

            openChestTexture = new Texture("openchest.png");
        }
    }

    public Array<Entity> getEnemyArray() {
        return enemies;
    }

    public void drawClosedChest(SpriteBatch batch) {
        batch.draw(chestTexture, -60, 0, 100, 100);
    }

    public void drawOpenChest(SpriteBatch batch)
    {
        batch.draw(openChestTexture, -60, 0, 100, 100);
    }


}
