package helper;

public class Constants {

    public static final float PPM = 32.0f;
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    public static final int TILE_SIZE = 120;
    public static final int STARTX = -860;
    public static int STARTY = 20;
    public static int[][] playerColsAndRows = { {2,2}, // Standing
                                                {2,3}, // Walk N
                                                {2,3}, // Walk S
                                                {2,3}, // Walk E
                                                {2,3}, // Walk W
                                                {3,3}, // Attack E
                                                {3,3}, // ATTACK W
                                                {4,5}  // HEAL
                                             };
    public static int[][] enemyColsAndRows = {  {2,2}, // Standing
                                                {2,3}, // Walk N
                                                {2,3}, // Walk S
                                                {2,3}, // Walk E
                                                {2,3}, // Walk W
                                                {3,3}, // Attack E
                                                {3,3}, // ATTACK W
                                                {4,4}  // TO DO: DIE!
                                            };
    public static float[] playerAnimationSpeed = { .25f, // Standing
                                                    .20f, // Walk N
                                                    .20f, // Walk S
                                                    .20f, // Walk E
                                                    .20f, // Walk W
                                                    .20f, // Attack E
                                                    .20f, // ATTACK W
                                                    .15f  // HEAL
                                                    };
    public static float[] enemyAnimationSpeed = {  .25f, // Standing
                                                    .20f, // Walk N
                                                    .20f, // Walk S
                                                    .20f, // Walk E
                                                    .20f, // Walk W
                                                    .20f, // Attack E
                                                    .20f, // ATTACK W
                                                    .15f  // DEATH
                                                };

    public static int[][] potentialEnemyPositions = {
                                                    {410, 20},
                                                    {410, 90},
                                                    {410, 160},
                                                    {410, -50},
                                                    {410, -120},
                                                    {410, -190}
                                                };

    }
