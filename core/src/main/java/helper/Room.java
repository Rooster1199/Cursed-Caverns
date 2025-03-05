package helper;

import com.badlogic.gdx.physics.box2d.World;
import entities.Entity;
import helper.Constants.*;

import static helper.Constants.potentialEnemyPositions;

public class Room {

    private Entity Player;
    private Entity[] enemies;

    private String enemyTypes;

    private int positionIndex;

    public Room (Entity givenPlayer, int enemyCount, String enemyTypes, int sizeFactor, int enemyHealth, int enemyStrength, World world)
    {
        Player = givenPlayer;
        enemies = new Entity[enemyCount];
        positionIndex = 0;

        for (int enemy = 0; enemy < enemyCount; enemy++)
        {
            enemies[enemy] = new Entity(world,enemyHealth,enemyStrength, potentialEnemyPositions[positionIndex][0],potentialEnemyPositions[positionIndex][1], false,"W",240,320, sizeFactor);
            positionIndex = positionIndex > 5 ? 0 : positionIndex + 1;
        }
    }

    private Entity[] getEnemyArray() {
        return enemies;
    }

}
