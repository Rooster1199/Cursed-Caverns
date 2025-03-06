package helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import entities.Entity;

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

    /**
     * Represents a room in the game, which can either be a normal room containing enemies or a chest room.
     * Initializes the player, enemies, and chest textures based on the given parameters.
     *
     * @param givenPlayer The player entity in the room.
     * @param enemyCount The number of enemies to spawn in the room.
     * @param roomType The type of the room ("Normal" or "Chest").
     * @param sizeFactor A scaling factor for the size of the entities.
     * @param enemyHealth The health of the enemies in the room.
     * @param enemyStrength The strength of the enemies in the room.
     * @param world The world in which the enemies will exist.
     */
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

    /**
     * Returns the array of enemies present in the room.
     *
     * @return An Array of Entity objects representing the enemies in the room.
     */
    public Array<Entity> getEnemyArray() {
        return enemies;
    }

    /**
     * Draws the chest texture in its closed state on the given SpriteBatch at a specific position.
     *
     * @param batch The SpriteBatch used to draw the chest texture.
     */
    public void drawClosedChest(SpriteBatch batch) {
        batch.draw(chestTexture, -60, 0, 100, 100);
    }

    /**
     * Draws the chest texture in its open state on the given SpriteBatch at a specific position.
     *
     * @param batch The SpriteBatch used to draw the open chest texture.
     */
    public void drawOpenChest(SpriteBatch batch)
    {
        batch.draw(openChestTexture, -60, 0, 100, 100);
    }


}
