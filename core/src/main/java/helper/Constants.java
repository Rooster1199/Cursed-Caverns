package helper;

public class Constants {

    public static final float PPM = 32.0f;
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1440;
    public static final int TILE_SIZE = 120;
    public static final int STARTX = -860;
    public static int STARTY = 20;
    public static int[][] playerColsAndRows = { {2,2}, // Standing
                                                {2,3}, // Walk N
                                                {2,3}, // Walk S
                                                {2,2}, // Walk E
                                                {2,3}, // Walk W
                                                {3,3}, // Attack E
                                                {3,3}, // ATTACK W
                                                {4,5}  // HEAL
                                             };
    public static int[][] enemyColsAndRows = {  {2,2}, // Standing
                                                {2,3}, // Walk N
                                                {2,3}, // Walk S
                                                {2,2}, // Walk E
                                                {2,3}, // Walk W
                                                {3,3}, // Attack E
                                                {3,3}, // ATTACK W
                                                {4,5}  // TO DO: DIE!
                                            };

    }
