package au.id.richardburgmann;
/**
 * Created by Richard Burgmann on 11/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Random;

/**
 * <p>This class holds the state of the world within the simulator.
 * See https://github.com/rburgmann/WumpusWorld/wiki/W2101-Data-Model:-World-State
 * for further details.</p>
 */

public class TheWorld {
    /**
     * Constant, usage is worldState[WUMPUS][4][4]
     * There can only be one Wumpus in the world at a time.
     * It doesn't move and eats Adventures.
     */
    public static final int WUMPUS = 0;    // Usage worldState[WUMPUS][4][4]
    /**
     * Constant, Usage worldState[STENCHES][4][4]
     * The Wumpus has a distinctive smell that permeates the surrounding areas.
     * The stench is your only warning of a nearby Wumpus.
     */
    public static final int STENCHES = 1;
    /**
     * Constant, Usage worldState[PITS][4][4]
     * Pits are deep and to fall into one is always fatal.
     */
    public static final int PITS = 2;
    /**
     * Constant, Usage worldState[BREEZES][4][4]
     * Due to peculiar yet fortunate geology the deadly pits have a breeze near them.
     */
    public static final int BREEZES = 3;
    /**
     * Constant, Usage worldState[GOLD][4][4]
     * The Wumpus scat contains large amounts of Gold. Is the Wumpus really an Orta ?
     */
    public static final int GOLD = 4;
    /**
     * Constant, Usage worldState[GLITTER][4][4]
     * Gold can be detected by its distinctive glitter in the dark caves. Remember to wear gloves when
     * collecting it, and wash your hands.
     */
    public static final int GLITTER = 5;
    /**
     * Constant, Usage worldState[ADVENTURER][4][4] The brave adventurer searching the cave system for gold.
     * An occupation recently made more
     * hazardous by the banning of ARROWS by the Wumpus Protection Society. Apparently the Wumpus
     * is on an 'at risk' list of species, gold hungry adventurers are not, of those we have plenty.
     */
    public static final int ADVENTURER = 6;
    /**
     * Constant, Usage worldState[WALLLS][4][4]
     * Try not to walk into them, they hurt.
     */
    public static final int WALLS = 7;
    /**
     * Constant, Usage worldState[VISITED][4][4]
     * Never explore a cave without a map.
     */
    public static final int VISITED = 8;
    public static final int RANDOM_START = 1;
    public static final int FIXED_START = 0;
    /**
     * Lables for constants, these are used to get related defaults from the application properties file and
     * for display purposes.
     */
    public static final String[] ENTITY_CONSTANTS = {"WUMPUS", "STENCHES", "PITS", "BREEZES", "GOLD", "GLITTER", "ADVENTURER",
            "WALLS", "VISITED"};
    public static final String[] ACTION_CONSTANTS = {"Move Left ", "Move Right ",
            "Move Up ", "Move Down "};
    public static final String[] LEGAL_MOVES = {"Can ", "Cannot "};

    /**
     * A lot of code will be referring to the n of the n row n grid.
     * So create a constant to keep it.
     */
    public static final int GRID_SIZE = 4;
    /**
     * Constant representing EMPTY_LOCATION = -1
     */
    public static final int EMPTY_LOCATION = -1;
    /**
     * Constant representing OCCUPIED_LOCATION = +1
     */
    public static final int OCCUPIED_LOCATION = 1;
    /**
     * Used by the logging system.
     */
    private static final Logger logger = LoggerFactory.getLogger(TheWorld.class);
    /**
     * Private constant used throughout. Essentially means success in whatever was done.
     */
    private static final int OK = 0;
    /**
     * Private constant used throughout. Essentially means failure in whatever was done.
     */
    private static final int FAILED = 1;
    /**
     * I want to be able to repeat experiments and so need the seed for the random number
     * generator to be fixed.
     */
    private static long RANDOM_SEED = 11235813;
    private static boolean USE_RANDOM_SEED = false;
    private static Random random;
    /**
     * worldState does what it says on the box, it holds the entire state of the world.
     * Contrary to recommended practice I'm making this a public variable. Not usually
     * regarded as the correct way to code variables. I may make it private and hide it
     * behind getter and setter methods later but for now it is a public variable.
     * The first element of the array identifies the thing the information is about.
     * Refer to the constants for details.
     * <p>
     * -1 means nothing is there.
     * +1 means something is there.
     * <p>
     * For example if worldState[WUMPUS][0][0] = -1 then the Wumpus is NOT in cell 0,0.
     * If  worldState[WUMPUS][0][0] = +1 then the Wumpus IS in cell 0,0.
     * <p>
     * Where is the Wumpus ?
     * <p>
     * -1,-1,-1,-1 <p>
     * -1,-1,-1,-1 <p>
     * -1,+1,-1,-1 <p>
     * -1,-1,-1,-1 <p>
     * </p>
     * Assume the following worldState[ENTITY][ROW][COL]<p>
     * worldState[WUMPUS][2][1] = +1 and so that is where the Wumpus is.
     */
    public int[][][] worldState = new int[9][GRID_SIZE][GRID_SIZE];
    public int worldStateValue;

    // 0 is move left
    // 1 is move right
    // 2 is move up
    // 3 is move down.
    /**
     * <p>
     * 1,2,3,4 <p>
     * 5,6,7,8 <p>
     * 9,10,11,12 <p>
     * 13,14,15,16 <p>
     * </p>
     */
    //     matrixR[row][col]
    public int[][] matrixR = {
            // top row states 1 - 4
            {-1, 0, -1, 0},
            {0, 0, -1, 0},
            {0, 0, -1, 0},
            {0, -1, -1, 0},
            // 2nd row states 5 -8
            {-1, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, -1, 0, 0},
            // 3rd row states 9 -12
            {-1, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, -1, 0, 0},
            // 4th row states 13 -16
            {-1, 0, 0, -1},
            {0, 0, 0, -1},
            {0, 0, 0, -1},
            {0, -1, 0, -1},
    };
    /**
     * Hold the referance to the application properties object.
     */
    private Properties myProperties;
    /**
     * Decides if the Adventurer will start in the fixed location or in
     * a random location around the outside edge of the grid.
     * Initial values are loaded from the properties file.
     * 1 = Random starting location.
     * 0 = Fixed starting location.
     */
    private int ADVENTURER_PLACEMENT = 0;
    /**
     * Initial starting location loaded from properties file.
     */
    private int ADVENTURER_X = 0;
    /**
     * Initial starting location loaded from properties file.
     */
    private int ADVENTURER_Y = 0;

    /**
     * Decides if the Wumpus will start in the fixed location 2,2 or in
     * a random location on the grid.
     * 1 = Random starting location.
     * 0 = Fixed starting location at 2,2.
     */
    private int WUMPUS_PLACEMENT = 0;
    /**
     * Initial starting location loaded from properties file.
     */
    private int WUMPUS_X = 1;
    /**
     * Initial starting location loaded from properties file.
     */
    private int WUMPUS_Y = 1;
    /**
     * Decides if the Gold will start in the fixed location or in
     * a random location on the grid.
     * 1 = Random starting location.
     * 0 = Fixed starting location at 2,2.
     */
    private int GOLD_PLACEMENT = 1;
    /**
     * Initial starting location loaded from properties file.
     */
    private int GOLD_X = 3;
    /**
     * Initial starting location loaded from properties file.
     */
    private int GOLD_Y = 3;
    /**
     * Decides if the Walls will start in the fixed location or in
     * a random location on the grid.
     * 1 = Random starting location.
     * 0 = Fixed starting location at 2,2.
     */
    private int WALLS_PLACEMENT = 1;
    /**
     * Initial starting location loaded from properties file.
     */
    private int WALLS_X = 2;
    /**
     * Initial starting location loaded from properties file.
     */
    private int WALLS_Y = 3;
    /**
     * Decides if the Pits will start in the fixed location or in
     * a random location on the grid.
     * 1 = Random starting location.
     * 0 = Fixed starting location at 2,2.
     */
    private int PITS_PLACEMENT = 1;
    /**
     * Initial starting location loaded from properties file.
     */
    private int PITS_X = 2;
    /**
     * Initial starting location loaded from properties file.
     */
    private int PITS_Y = 2;

    /**
     * Load defaults from properties object.
     */
    public TheWorld(Properties properties) {
        super();

        logger.debug("Load defaults from properties object.");

        this.clearWorldState();
        if (properties != null) {
            myProperties = properties;
            try {
                String temp = myProperties.getProperty("RANDOM_SEED");
                TheWorld.RANDOM_SEED = Long.parseLong(temp);
            } catch (Exception e) {
                logger.warn(e.getMessage());
                logger.warn("Could not load the seed for the random number generator from the properties object.");
                logger.warn("Using default hard coded value instead.");

            }

            try {
                String temp = myProperties.getProperty("USE_RANDOM_SEED");
                if (temp.equalsIgnoreCase("true")) {
                    TheWorld.USE_RANDOM_SEED = true;
                } else if (temp.equalsIgnoreCase("false")) {
                    TheWorld.USE_RANDOM_SEED = false;
                } else {
                    // Totally redundant but appeals to my sense of closure and completeness.
                    logger.warn(temp + " is not a valid input to this property.");
                    logger.warn("Acceptable values are true or false. Defaulting to false.");
                }
            } catch (Exception e) {
                logger.warn(e.getMessage());
                logger.warn("Could not find USE_RANDOM_SEED property.");
                logger.warn("Using default hard coded value instead.");

            }

            this.setADVENTURER_X((Integer.parseInt(myProperties.getProperty("ADVENTURER_X"))));
            this.setADVENTURER_Y((Integer.parseInt(myProperties.getProperty("ADVENTURER_Y"))));
            this.setADVENTURER_PLACEMENT((Integer.parseInt(myProperties.getProperty("ADVENTURER_PLACEMENT"))));
            this.initEntity(ADVENTURER, this.getADVENTURER_PLACEMENT(), this.getADVENTURER_X(), this.getADVENTURER_Y());

            this.setWUMPUS_X((Integer.parseInt(myProperties.getProperty("WUMPUS_X"))));
            this.setWUMPUS_Y((Integer.parseInt(myProperties.getProperty("WUMPUS_Y"))));
            this.setWUMPUS_PLACEMENT((Integer.parseInt(myProperties.getProperty("WUMPUS_PLACEMENT"))));
            this.initEntity(WUMPUS, this.getWUMPUS_PLACEMENT(), this.getWUMPUS_X(), this.getWUMPUS_Y());

            this.setPITS_X((Integer.parseInt(myProperties.getProperty("PITS_X"))));
            this.setPITS_Y((Integer.parseInt(myProperties.getProperty("PITS_Y"))));
            this.setPITS_PLACEMENT((Integer.parseInt(myProperties.getProperty("PITS_PLACEMENT"))));
            this.initEntity(PITS, this.getPITS_PLACEMENT(), this.getPITS_X(), this.getPITS_Y());

            this.setWALLS_X((Integer.parseInt(myProperties.getProperty("WALLS_X"))));
            this.setWALLS_Y((Integer.parseInt(myProperties.getProperty("WALLS_Y"))));
            this.setWALLS_PLACEMENT((Integer.parseInt(myProperties.getProperty("WALLS_PLACEMENT"))));
            this.initEntity(WALLS, this.getWALLS_PLACEMENT(), this.getWALLS_X(), this.getWALLS_Y());

            this.setGOLD_X((Integer.parseInt(myProperties.getProperty("GOLD_X"))));
            this.setGOLD_Y((Integer.parseInt(myProperties.getProperty("GOLD_Y"))));
            this.setGOLD_PLACEMENT((Integer.parseInt(myProperties.getProperty("GOLD_PLACEMENT"))));
            this.initEntity(GOLD, this.getGOLD_PLACEMENT(), this.getGOLD_X(), this.getGOLD_Y());


        }
    }

    public TheWorld() {
        //logger.debug("TheWorld default constructor.");
        this.clearWorldState();
    }

    public static TheWorld cloneStateArray(TheWorld theWorld) {
        // logger.debug("cloneStateArray()");

        TheWorld temp = new TheWorld();
        temp.worldStateValue = theWorld.worldStateValue;

        for (int e = 0; e < theWorld.worldState.length; e++) {
            for (int r = 0; r < TheWorld.GRID_SIZE; r++) {
                for (int c = 0; c < TheWorld.GRID_SIZE; c++) {
                    temp.worldState[e][r][c] = theWorld.worldState[e][r][c];
                }
            }
        }
        return temp;
    }

    /**
     * This method is used to load a previously defined world state from disk.
     *
     * @param filename Name of the file containing the world state you wish to load from disk.
     * @return 0 Ok 1 Failed.
     */
    public int loadWorldState(String filename) {
        // Define the return value as failed unless we are certain it worked.
        int returnValue = FAILED;

        // TODO

        returnValue = OK;

        return returnValue;
    }

    /**
     * Write the current world state to disk.
     *
     * @param filename Name of the file to persist the worlds state.
     * @return 0 Ok 1 Failed.
     */
    public int saveWorldState(String filename) {
        // Define the return value as failed unless we are certain it worked.
        int returnValue = FAILED;

        // TODO

        returnValue = OK;

        return returnValue;

    }

    /**
     * Has the Adventurer been initialized to start in a random location or the fixed
     * starting position?
     *
     * @return 1 = Random start somewhere on the outside edges of the grid world.
     * 0 = Fixed start at 0,0.
     */
    public int getADVENTURER_PLACEMENT() {
        return ADVENTURER_PLACEMENT;
    }

    /**
     * Sets the policy for how the Adventurer will be placed at the start of a run. Either randomly
     * along the outside edge or at a fixed location (0,0).
     *
     * @param ADVENTURER_PLACEMENT 1 = Random position along edge or 0 start at the fixed location.
     */
    public void setADVENTURER_PLACEMENT(int ADVENTURER_PLACEMENT) {
        this.ADVENTURER_PLACEMENT = ADVENTURER_PLACEMENT;
        if (myProperties != null) {
            myProperties.setProperty("ADVENTURER_PLACEMENT", Integer.toString((this.ADVENTURER_PLACEMENT)));
        }
    }

    public int getWUMPUS_PLACEMENT() {
        return WUMPUS_PLACEMENT;
    }

    public void setWUMPUS_PLACEMENT(int WUMPUS_PLACEMENT) {

        this.WUMPUS_PLACEMENT = WUMPUS_PLACEMENT;
        if (myProperties != null) {
            myProperties.setProperty("WUMPUS_PLACEMENT", Integer.toString((this.WUMPUS_PLACEMENT)));
        }

    }

    public int getGOLD_PLACEMENT() {
        return GOLD_PLACEMENT;
    }

    public void setGOLD_PLACEMENT(int GOLD_PLACEMENT) {

        this.GOLD_PLACEMENT = GOLD_PLACEMENT;
        if (myProperties != null) {
            myProperties.setProperty("GOLD_PLACEMENT", Integer.toString((this.GOLD_PLACEMENT)));
        }
    }

    public int getWALLS_PLACEMENT() {
        return WALLS_PLACEMENT;
    }

    public void setWALLS_PLACEMENT(int WALLS_PLACEMENT) {
        this.WALLS_PLACEMENT = WALLS_PLACEMENT;
        if (myProperties != null) {
            myProperties.setProperty("WALLS_PLACEMENT", Integer.toString((this.WALLS_PLACEMENT)));
        }
    }

    public int getPITS_PLACEMENT() {
        return PITS_PLACEMENT;
    }

    public void setPITS_PLACEMENT(int PITS_PLACEMENT) {
        this.PITS_PLACEMENT = PITS_PLACEMENT;
        if (myProperties != null) {
            myProperties.setProperty("PITS_PLACEMENT", Integer.toString((this.PITS_PLACEMENT)));
        }
    }

    public int getADVENTURER_X() {
        return ADVENTURER_X;
    }

    public void setADVENTURER_X(int ADVENTURER_X) {
        this.ADVENTURER_X = ADVENTURER_X;
        if (myProperties != null) {
            myProperties.setProperty("ADVENTURER_X", Integer.toString((this.ADVENTURER_X)));
        }
    }

    public int getADVENTURER_Y() {
        return ADVENTURER_Y;
    }

    public void setADVENTURER_Y(int ADVENTURER_Y) {
        this.ADVENTURER_Y = ADVENTURER_Y;
        if (myProperties != null) {
            myProperties.setProperty("ADVENTURER_Y", Integer.toString((this.ADVENTURER_Y)));
        }
    }

    public int getGOLD_X() {
        return GOLD_X;
    }

    public void setGOLD_X(int GOLD_X) {

        this.GOLD_X = GOLD_X;
        if (myProperties != null) {
            myProperties.setProperty("GOLD_X", Integer.toString((this.GOLD_X)));
        }
    }

    public int getGOLD_Y() {
        return GOLD_Y;
    }

    public void setGOLD_Y(int GOLD_Y) {

        this.GOLD_Y = GOLD_Y;
        if (myProperties != null) {
            myProperties.setProperty("GOLD_Y", Integer.toString((this.GOLD_Y)));
        }
    }

    public int getWALLS_X() {
        return WALLS_X;
    }

    public void setWALLS_X(int WALLS_X) {

        this.WALLS_X = WALLS_X;
        if (myProperties != null) {
            myProperties.setProperty("WALLS_X", Integer.toString((this.WALLS_X)));
        }
    }

    public int getPITS_X() {
        return PITS_X;
    }

    public void setPITS_X(int PITS_X) {

        this.PITS_X = PITS_X;
        if (myProperties != null) {
            myProperties.setProperty("PITS_X", Integer.toString((this.PITS_X)));
        }
    }

    public int getPITS_Y() {
        return PITS_Y;
    }

    public void setPITS_Y(int PITS_Y) {

        this.PITS_Y = PITS_Y;
        if (myProperties != null) {
            myProperties.setProperty("PITS_Y", Integer.toString((this.PITS_Y)));
        }
    }

    public int getWUMPUS_X() {
        return WUMPUS_X;
    }

    public void setWUMPUS_X(int WUMPUS_X) {
        this.WUMPUS_X = WUMPUS_X;
        if (myProperties != null) {
            myProperties.setProperty("WUMPUS_X", Integer.toString((this.WUMPUS_X)));
        }
    }

    public int getWUMPUS_Y() {
        return WUMPUS_Y;
    }

    public void setWUMPUS_Y(int WUMPUS_Y) {

        this.WUMPUS_Y = WUMPUS_Y;
        if (myProperties != null) {
            myProperties.setProperty("WUMPUS_Y", Integer.toString((this.WUMPUS_Y)));
        }
    }

    public int getWALLS_Y() {
        return WALLS_Y;
    }

    public void setWALLS_Y(int WALLS_Y) {

        this.WALLS_Y = WALLS_Y;
        if (myProperties != null) {
            myProperties.setProperty("WALLS_Y", Integer.toString((this.WALLS_Y)));
        }
    }

    /**
     * Initialises the data model to -1 representing an empty state. Should be called at objects
     * creation and whenever you want to clear it of values. It will call the clear method for each of
     * the entities in the world.
     */
    public void clearWorldState() {
        //logger.info("clearWorldState().");
        this.clearEntity(WUMPUS);
        this.clearEntity(STENCHES);
        this.clearEntity(PITS);
        this.clearEntity(BREEZES);
        this.clearEntity(GOLD);
        this.clearEntity(GLITTER);
        this.clearEntity(ADVENTURER);
        this.clearEntity(WALLS);
        this.clearEntity(VISITED);

    }

    /**
     * Clear Entity location.
     *
     * @param entity Set which entity (WUMPUS, ADVENTURER, etc you wish to clear the locations values for.
     */
    public void clearEntity(int entity) {
        /**
         * Test that entity has a legal value, if it doesn't then just exit with no action.
         */
        if (entity == WUMPUS ||
                entity == STENCHES ||
                entity == PITS ||
                entity == BREEZES ||
                entity == GOLD ||
                entity == GLITTER ||
                entity == ADVENTURER ||
                entity == WALLS ||
                entity == VISITED) {
            for (int row = 0; row < this.worldState[entity].length; row++) {
                for (int col = 0; col < this.worldState[entity][row].length; col++) {
                    this.worldState[entity][row][col] = EMPTY_LOCATION;
                }
            }

        }

    }

    /**
     * Initialise the Adventurers starting position.
     * The ADVENTURER_PLACEMENT flag should be set prior to calling this method.
     */
    public void initAdventurer() {
        logger.debug("initAdventurer()");
        this.clearEntity(ADVENTURER);
        this.clearEntity(VISITED);
        if (this.ADVENTURER_PLACEMENT == FIXED_START ||
                this.ADVENTURER_PLACEMENT == RANDOM_START) {
            switch (this.ADVENTURER_PLACEMENT) {
                case FIXED_START:
                    //logger.debug("Set Adventurer to " + ADVENTURER_X + "," + ADVENTURER_Y);
                    this.worldState[ADVENTURER][ADVENTURER_X][ADVENTURER_Y] = OCCUPIED_LOCATION;
                    this.worldState[VISITED][ADVENTURER_X][ADVENTURER_Y] = OCCUPIED_LOCATION;
                    //logger.debug("Set Visted at " + ADVENTURER_X + "," + ADVENTURER_Y + " to " + OCCUPIED_LOCATION);
                    break;
                case RANDOM_START: {
                    int startLoc = getRandom();
                    int edgeLoc = getRandom();
                    if (startLoc == 0) {
                        // Adventurer will start somewhere on the left column.
                        this.setADVENTURER_X(edgeLoc);
                        this.setADVENTURER_Y(0);
                    } else if (startLoc == 1) {
                        // Adventurer will start somewhere on the top row.
                        this.setADVENTURER_X(0);
                        this.setADVENTURER_Y(edgeLoc);
                    } else if (startLoc == 2) {
                        // Adventurer will start somewhere on the back column.
                        this.setADVENTURER_X(3);
                        this.setADVENTURER_Y(edgeLoc);
                    } else {
                        // Adventurer will start somewhere on the bottom row.
                        this.setADVENTURER_X(edgeLoc);
                        this.setADVENTURER_Y(3);
                    }
                    this.worldState[ADVENTURER][ADVENTURER_X][ADVENTURER_Y] = OCCUPIED_LOCATION;
                    this.worldState[VISITED][ADVENTURER_X][ADVENTURER_Y] = OCCUPIED_LOCATION;
                }
                break;
            }
        } else {
            // Should never happen !
            logger.warn("Incorrect State: ADVENTURER_PLACEMENT flag contains an unknown value of " +
                    this.ADVENTURER_PLACEMENT + ", defaulting to a fixed starting position instead.");
            this.setADVENTURER_PLACEMENT(FIXED_START);
            this.initAdventurer();
        }
    }

    public void moveEntityTo(int entity, int xLoc, int yLoc) {
        logger.info("Moving " + ENTITY_CONSTANTS[entity] + " to " +
                xLoc + "," + yLoc);
        this.clearEntity(entity);
        this.worldState[entity][xLoc][yLoc] = OCCUPIED_LOCATION;
    }

    /**
     * Initialise the Entity starting position.
     * The randomStart flag should be set prior to calling this method.
     */
    public void initEntity(int entity, int randomStart, int fixedX, int fixedY) {
        logger.info("Init Entity: " + ENTITY_CONSTANTS[entity]);

        this.clearEntity(entity);

        switch (randomStart) {
            case FIXED_START:
                logger.info(ENTITY_CONSTANTS[entity] + " will be placed in the fixed location of "
                        + fixedX + "," + fixedY);
                this.worldState[entity][fixedX][fixedY] = OCCUPIED_LOCATION;
                break;
            case RANDOM_START: {
                int startX = getRandom();
                int startY = getRandom();
                boolean scanFlag = true;
                boolean foundEmptyCell = true;
                int attempts = 0;
                while (scanFlag) {

                    for (int e = 0; e < this.worldState.length; e++) {
                        if (this.worldState[e][startX][startY] == OCCUPIED_LOCATION) {
                            foundEmptyCell = false;
                        }
                    }
                    if (foundEmptyCell == true) {
                        scanFlag = false;
                    } else {

                        if (attempts < 16) {
                            logger.info(entity + " Attempt " + attempts + ", try again.");
                            // get new positions to try.
                            foundEmptyCell = true; // Reset flag for next attempt.
                            startX = getRandom();
                            startY = getRandom();
                            attempts++;
                        } else {
                            // search sequentially now.
                            logger.info("Giving up and now searching sequentially.");
                            for (int sx = 0; sx < GRID_SIZE; sx++) {
                                for (int sy = 0; sy < GRID_SIZE; sy++) {
                                    if (this.worldState[ADVENTURER][sx][sy] == EMPTY_LOCATION &&
                                            this.worldState[WUMPUS][sx][sy] == EMPTY_LOCATION &&
                                            this.worldState[STENCHES][sx][sy] == EMPTY_LOCATION &&
                                            this.worldState[PITS][sx][sy] == EMPTY_LOCATION &&
                                            this.worldState[BREEZES][sx][sy] == EMPTY_LOCATION &&
                                            this.worldState[GOLD][sx][sy] == EMPTY_LOCATION &&
                                            this.worldState[GLITTER][sx][sy] == EMPTY_LOCATION &&
                                            this.worldState[WALLS][sx][sy] == EMPTY_LOCATION) {
                                        startX = sx;
                                        startY = sy;
                                        scanFlag = false;
                                    }
                                }
                            }
                        }
                    }
                }
                logger.info(ENTITY_CONSTANTS[entity] + " will be placed in the random location of "
                        + startX + "," + startY);
                this.worldState[entity][startX][startY] = OCCUPIED_LOCATION;

            }

        }

        if (entity == WUMPUS) this.waftSensations(WUMPUS, STENCHES);
        if (entity == PITS) this.waftSensations(PITS, BREEZES);
        if (entity == GOLD) this.waftSensations(GOLD, GLITTER);
    }

    public void waftSensations(int source, int sensation) {
        logger.info("Wafting " + ENTITY_CONSTANTS[sensation] + " from " + ENTITY_CONSTANTS[source] + " around dungeon.");
        // Clear sensations.
        this.clearEntity(sensation);
        // get the row and col locations of source of sensation.
        CoOrdinate sXY = this.getEntityLocation(source);
        //
        // Sensation is present in the sources location.
        this.worldState[sensation][sXY.row][sXY.col] = OCCUPIED_LOCATION;
        if (source == GOLD) {
            // Glittering is only in it's location.
            // I got this wrong before and had it in wafting.
            return;
        }
        // set sensations.

        // Cell to the left.
        if ((sXY.row - 1) >= 0) {
            this.worldState[sensation][sXY.row - 1][sXY.col] = OCCUPIED_LOCATION;
        }
        // Cell to the right.
        if ((sXY.row + 1) < GRID_SIZE) {
            this.worldState[sensation][sXY.row + 1][sXY.col] = OCCUPIED_LOCATION;
        }
        // Cell above.
        if ((sXY.col - 1) >= 0) {
            this.worldState[sensation][sXY.row][sXY.col - 1] = OCCUPIED_LOCATION;
        }
        // Cell below.
        if ((sXY.col + 1) < GRID_SIZE) {
            this.worldState[sensation][sXY.row][sXY.col + 1] = OCCUPIED_LOCATION;
        }

    }

    public CoOrdinate getEntityLocation(int entity) {
        CoOrdinate entityXY = new CoOrdinate();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (this.worldState[entity][row][col] == OCCUPIED_LOCATION) {
                    entityXY.row = row;
                    entityXY.col = col;
                    return entityXY;
                }
            }
        }
        return entityXY;
    }

    /**
     * Private pseudo random number sequencer.
     * Set it up once on first access.
     *
     * @return returns a pseudo random number between 0 and 3.
     */
    public int getRandom() {
        if (random == null) {
            random = new Random();
            //random.setSeed(RANDOM_SEED);
        }
        int newRandomNumber = random.nextInt(GRID_SIZE);

        return newRandomNumber;
    }

    /**
     * Random legal move generator. Uses matrixR to only return valid actions
     * from the current state. Means you want go off grid, not that there is
     * no hazard.
     *
     * @return int legal move from this grid location
     */
    public int getALegalRandomMove(CoOrdinate fromHereXY) {
        // logger.debug("get A Legal Random Move from " + fromHereXY.toCSV());

        int legalMove = -1;
        int[] availableLegalMoves = this.getAllLegalMovesFromHere(fromHereXY);
      /*  logger.debug("Available moves are " +
                availableLegalMoves[0] + "," +
                availableLegalMoves[1] + "," +
                availableLegalMoves[2] + "," +
                availableLegalMoves[3] + "."
        );*/

        while (legalMove < 0) {
            int candidateMove = getRandom();
            if (availableLegalMoves[candidateMove] >= 0) {
                legalMove = candidateMove;
            }
        }

        return legalMove;
    }

    public boolean isThisMoveLegal(int candidateMove, CoOrdinate fromHereXY) {
        //logger.debug("isThisMoveLegal(" + candidateMove + " , (" + fromHereXY.toCSV() + ")");
        boolean legalStatus = false;

        int[] availableLegalMoves = this.getAllLegalMovesFromHere(fromHereXY);
        // logger.debug("Available moves are " +
        //         TheWorld.LEGAL_MOVES[(availableLegalMoves[0] * availableLegalMoves[0])] + TheWorld.ACTION_CONSTANTS[0] + "," +
        //         TheWorld.LEGAL_MOVES[(availableLegalMoves[1] * availableLegalMoves[1])] + TheWorld.ACTION_CONSTANTS[1] + "," +
        //         TheWorld.LEGAL_MOVES[(availableLegalMoves[2] * availableLegalMoves[2])] + TheWorld.ACTION_CONSTANTS[2] + "," +
        //         TheWorld.LEGAL_MOVES[(availableLegalMoves[3] * availableLegalMoves[3])] + TheWorld.ACTION_CONSTANTS[3] + ".");

        if (availableLegalMoves[candidateMove] >= 0) {
            legalStatus = true;
        }
        //logger.debug(String.valueOf(legalStatus));
        return legalStatus;
    }

    public int[] getAllLegalMovesFromHere(CoOrdinate fromHereXY) {
        //logger.debug("getAllLegalMovesFromHere(" + fromHereXY.toCSV() + ")");

        return matrixR[((fromHereXY.row * GRID_SIZE) + fromHereXY.col)];
    }

    public int getCountVisited() {
        int count = 0;
        for (int r = 0; r < this.GRID_SIZE; r++) {
            for (int c = 0; c < this.GRID_SIZE; c++) {
                if (this.worldState[VISITED][r][c] == OCCUPIED_LOCATION) {
                    count = count + 1;
                }

            }
        }
        return count;
    }

    @Override
    public boolean equals(Object o) {

        //logger.debug("TheWorld equals method.");
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        TheWorld pstate = (TheWorld) o;
        boolean result = false;
        // Is the Adventurer in th same place?
        CoOrdinate thisAdv = this.getEntityLocation(ADVENTURER);
        CoOrdinate otherAdv = pstate.getEntityLocation(ADVENTURER);
        if ((thisAdv.row == otherAdv.row) && (thisAdv.col == otherAdv.col)) {
            // Keep looking.
            result = true; // So far.
        } else {
            return false;
        }

        // field comparison
/*
        for (int e = 0; e < this.worldState.length; e++) {
            for (int r = 0; r < this.GRID_SIZE; r++) {
                for (int c = 0; c < this.GRID_SIZE; c++) {
                    // Only compare up to were both are visited.
                    if ((this.worldState[VISITED][r][c] == this.worldState[VISITED][r][c]) &&
                            (this.worldState[VISITED][r][c] == OCCUPIED_LOCATION)) {
                        // where they have both had visit the grid, are they the same contents
                        // at least once. Only look where they have both visited.
                        if (this.worldState[e][r][c] == this.worldState[e][r][c]) {
                            result = true; // has to be at least one place the same.
                        } else {
                            result = false; // ok there was a difference, we can break out now.
                            break;
                        }

                    }

                }
            }
        }*/
/*
        if(result) {
            logger.debug("They are equal.");
        } else {
            logger.debug("They are NOT equal.");
        }*/
        return result;
    }
}
