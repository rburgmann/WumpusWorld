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
     *
     */
    public static final String[] ENTITY_CONSTANTS = {"WUMPUS","STENCHES","PITS","BREEZES","GOLD","GLITTER","ADVENTURER",
                                "WALLS","VISITED"};

    /**
     * A lot of code will be referring to the n of the n x n grid.
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
    private static final long RANDOM_SEED = 11235813;
    private static Random random;
    private static int lastRandomNumber = 0;
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
    private int ADVENTURER_PLACEMENT=0;
    /**
     * Initial starting location loaded from properties file.
     */
    private int ADVENTURER_X=0;
    /**
     * Initial starting location loaded from properties file.
     */
    private int ADVENTURER_Y=0;

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
    private int WUMPUS_X=1;
    /**
     * Initial starting location loaded from properties file.
     */
    private int WUMPUS_Y=1;
    /**
     * Decides if the Entity will start in the fixed location 2,2 or in
     * a random location on the grid.
     * 1 = Random starting location.
     * 0 = Fixed starting location at 2,2.
     */
    private int GOLD_PLACEMENT = 1;
    /**
     * Initial starting location loaded from properties file.
     */
    private int GOLD_X=3;
    /**
     * Initial starting location loaded from properties file.
     */
    private int GOLD_Y=3;
    //public int GOLD_PLACEMENT=0;
    /**
     * Decides if the Entity will start in the fixed location 2,2 or in
     * a random location on the grid.
     * 1 = Random starting location.
     * 0 = Fixed starting location at 2,2.
     */
    private int WALLS_PLACEMENT = 1;
    /**
     * Initial starting location loaded from properties file.
     */
    private int WALLS_X=2;
    /**
     * Initial starting location loaded from properties file.
     */
    private int WALLS_Y=3;
    /**
     * Decides if the Entity will start in the fixed location 2,2 or in
     * a random location on the grid.
     * 1 = Random starting location.
     * 0 = Fixed starting location at 2,2.
     */
    private int PITS_PLACEMENT = 1;
    /**
     * Initial starting location loaded from properties file.
     */
    private int PITS_X=2;
    /**
     * Initial starting location loaded from properties file.
     */
    private int PITS_Y=2;

    public void TheWorld() {
        logger.debug("TheWorld default constructor.");
        this.clearWorldState();
    }
    /**
     * Load defaults from properties object.
     */
     public TheWorld(Properties properties){
         logger.debug("Load defaults from properties object.");

         this.clearWorldState();
         if (properties != null) {
             myProperties = properties;

             this.setADVENTURER_X((Integer.parseInt(myProperties.getProperty("ADVENTURER_X"))));
             this.setADVENTURER_Y((Integer.parseInt(myProperties.getProperty("ADVENTURER_Y"))));
             this.setADVENTURER_PLACEMENT((Integer.parseInt(myProperties.getProperty("ADVENTURER_PLACEMENT"))));

             this.setWUMPUS_X((Integer.parseInt(myProperties.getProperty("WUMPUS_X"))));
             this.setWUMPUS_Y((Integer.parseInt(myProperties.getProperty("WUMPUS_Y"))));
             this.setWUMPUS_PLACEMENT((Integer.parseInt(myProperties.getProperty("WUMPUS_PLACEMENT"))));

             this.setPITS_X((Integer.parseInt(myProperties.getProperty("PITS_X"))));
             this.setPITS_Y((Integer.parseInt(myProperties.getProperty("PITS_Y"))));
             this.setPITS_PLACEMENT((Integer.parseInt(myProperties.getProperty("PITS_PLACEMENT"))));

             this.setWALLS_X((Integer.parseInt(myProperties.getProperty("WALLS_X"))));
             this.setWALLS_Y((Integer.parseInt(myProperties.getProperty("WALLS_Y"))));
             this.setWALLS_PLACEMENT((Integer.parseInt(myProperties.getProperty("WALLS_PLACEMENT"))));

             this.setGOLD_X((Integer.parseInt(myProperties.getProperty("GOLD_X"))));
             this.setGOLD_Y((Integer.parseInt(myProperties.getProperty("GOLD_Y"))));
             this.setGOLD_PLACEMENT((Integer.parseInt(myProperties.getProperty("GOLD_PLACEMENT"))));
         }
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

    }

    public int getGOLD_PLACEMENT() {
        return GOLD_PLACEMENT;
    }

    public void setGOLD_PLACEMENT(int GOLD_PLACEMENT) {
        this.GOLD_PLACEMENT = GOLD_PLACEMENT;
    }

    public int getWALLS_PLACEMENT() {
        return WALLS_PLACEMENT;
    }

    public void setWALLS_PLACEMENT(int WALLS_PLACEMENT) {
        this.WALLS_PLACEMENT = WALLS_PLACEMENT;
    }

    public int getPITS_PLACEMENT() {
        return PITS_PLACEMENT;
    }

    public void setPITS_PLACEMENT(int PITS_PLACEMENT) {
        this.PITS_PLACEMENT = PITS_PLACEMENT;
    }

    public int getADVENTURER_X() {
        return ADVENTURER_X;
    }

    public void setADVENTURER_X(int ADVENTURER_X) {
        this.ADVENTURER_X = ADVENTURER_X;
    }

    public int getADVENTURER_Y() {
        return ADVENTURER_Y;
    }

    public void setADVENTURER_Y(int ADVENTURER_Y) {
        this.ADVENTURER_Y = ADVENTURER_Y;
    }

    public int getGOLD_X() {
        return GOLD_X;
    }

    public void setGOLD_X(int GOLD_X) {
        this.GOLD_X = GOLD_X;
    }

    public int getGOLD_Y() {
        return GOLD_Y;
    }

    public void setGOLD_Y(int GOLD_Y) {
        this.GOLD_Y = GOLD_Y;
    }

    public int getWALLS_X() {
        return WALLS_X;
    }

    public void setWALLS_X(int WALLS_X) {
        this.WALLS_X = WALLS_X;
    }

    public int getPITS_X() {
        return PITS_X;
    }

    public void setPITS_X(int PITS_X) {
        this.PITS_X = PITS_X;
    }

    public int getPITS_Y() {
        return PITS_Y;
    }

    public void setPITS_Y(int PITS_Y) {
        this.PITS_Y = PITS_Y;
    }

    public int getWUMPUS_X() {
        return WUMPUS_X;
    }

    public void setWUMPUS_X(int WUMPUS_X) {
        this.WUMPUS_X = WUMPUS_X;
    }

    public int getWUMPUS_Y() {
        return WUMPUS_Y;
    }

    public void setWUMPUS_Y(int WUMPUS_Y) {
        this.WUMPUS_Y = WUMPUS_Y;
    }

    public int getWALLS_Y() {
        return WALLS_Y;
    }

    public void setWALLS_Y(int WALLS_Y) {
        this.WALLS_Y = WALLS_Y;
    }

    /**
     * Initialises the data model to -1 representing an empty state. Should be called at objects
     * creation and whenever you want to clear it of values. It will call the clear method for each of
     * the entities in the world.
     */
    public void clearWorldState() {
        logger.info("clearWorldState().");
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
        this.clearEntity(ADVENTURER);
        this.clearEntity(VISITED);
        if (this.ADVENTURER_PLACEMENT == FIXED_START ||
                this.ADVENTURER_PLACEMENT == RANDOM_START) {
            switch (this.ADVENTURER_PLACEMENT) {
                case FIXED_START:
                    this.worldState[ADVENTURER][0][0] = OCCUPIED_LOCATION;
                    this.worldState[VISITED][0][0] = OCCUPIED_LOCATION;
                    break;
                case RANDOM_START: {
                    int startLoc = getRandom();
                    int edgeLoc = getRandom();
                    if (startLoc == 0) {
                        // Adventurer will start somewhere on the left column.
                        this.worldState[ADVENTURER][edgeLoc][0] = OCCUPIED_LOCATION;
                        this.worldState[VISITED][edgeLoc][0] = OCCUPIED_LOCATION;
                    } else if (startLoc == 1) {
                        // Adventurer will start somewhere on the top row.
                        this.worldState[ADVENTURER][0][edgeLoc] = OCCUPIED_LOCATION;
                        this.worldState[VISITED][0][edgeLoc] = OCCUPIED_LOCATION;
                    } else if (startLoc == 2) {
                        // Adventurer will start somewhere on the back column.
                        this.worldState[ADVENTURER][edgeLoc][3] = OCCUPIED_LOCATION;
                        this.worldState[VISITED][edgeLoc][3] = OCCUPIED_LOCATION;
                    } else {
                        // Adventurer will start somewhere on the bottom row.
                        this.worldState[ADVENTURER][0][edgeLoc] = OCCUPIED_LOCATION;
                        this.worldState[VISITED][0][edgeLoc] = OCCUPIED_LOCATION;
                    }
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

    /**
     * Initialise the Adventurers starting position.
     * The ADVENTURER_PLACEMENT flag should be set prior to calling this method.
     */
    public void initWumpus() {
        this.clearEntity(WUMPUS);
        if (this.WUMPUS_PLACEMENT == FIXED_START ||
                this.WUMPUS_PLACEMENT == RANDOM_START) {
            switch (this.WUMPUS_PLACEMENT) {
                case FIXED_START:
                    this.worldState[WUMPUS][2][2] = OCCUPIED_LOCATION;
                    break;
                case RANDOM_START: {
                    int startX = getRandom();
                    int startY = getRandom();
                    boolean scanFlag = true;
                    boolean foundEmptyCell = true;
                    while (scanFlag) {
                        for (int e = 0; e < this.worldState.length; e++) {
                            if (this.worldState[e][startX][startY] == OCCUPIED_LOCATION) {
                                foundEmptyCell = false;
                            }
                        }
                        if (foundEmptyCell == true) {
                            scanFlag = false;
                        } else {
                            // get new positions to try.
                            startX = getRandom();
                            startY = getRandom();
                        }
                    }
                    this.setWumpusLocation(startX, startY);
                    break;
                }
            }
        } else {
            // Should never happen !
            logger.warn("Incorrect State: ADVENTURER_PLACEMENT flag contains an unknown value of " +
                    this.ADVENTURER_PLACEMENT + ", defaulting to a fixed starting position instead.");
            this.setADVENTURER_PLACEMENT(FIXED_START);
            this.initAdventurer();
        }
    }
    public void initEntity(int entity, int randomStart) {
        this.initEntity(entity, randomStart,0,0);
    }
    /**
     * Initialise the Entity starting position.
     * The randomStart flag should be set prior to calling this method.
     */
    public void initEntity(int entity, int randomStart, int fixedX, int fixedY) {
        logger.info("Init Entity: " + entity);
        this.clearEntity(entity);
        if (randomStart  == FIXED_START || randomStart == RANDOM_START) {
            switch (randomStart) {
                case FIXED_START:
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

                            if (attempts <16) {
                                logger.info(entity + " Attempt " + attempts + ", try again.");
                                // get new positions to try.
                                foundEmptyCell = true; // Reset flag for next attempt.
                                startX = getRandom();
                                startY = getRandom();
                                attempts++;
                            } else {
                                // search sequentially now.
                                logger.info("Giving up and now searching sequentially.");
                                for (int sx=0; sx < GRID_SIZE; sx++) {
                                    for (int sy=0; sy < GRID_SIZE; sy++) {
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
                    if (entity == ADVENTURER) this.initEntity(ADVENTURER,FIXED_START, startX, startY);
                    if (entity == WUMPUS) this.setWumpusLocation(startX, startY);
                    if (entity == PITS) this.setPitLocation(startX, startY);
                    if (entity == GOLD) this.setGoldLocation(startX, startY);
                    if (entity == WALLS) this.initEntity(WALLS,FIXED_START,startX,startY);
                    break;
                }
            }
        } else {
            // Should never happen !
            logger.warn("Incorrect State: ADVENTURER_PLACEMENT flag contains an unknown value of " +
                    this.ADVENTURER_PLACEMENT + ", defaulting to a fixed starting position instead.");
            this.setADVENTURER_PLACEMENT(FIXED_START);
            this.initAdventurer();
        }
    }
    public void setWumpusLocation(int cellX, int cellY) {
        this.clearEntity(WUMPUS);
        this.worldState[WUMPUS][cellX][cellY] = OCCUPIED_LOCATION;
        // Clear stenches.
        this.clearEntity(STENCHES);
        // set stenches.

        // Cell to the left.
        if ((cellX - 1) >= 0) {
            this.worldState[STENCHES][cellX - 1][cellY] = OCCUPIED_LOCATION;
        }
        // Cell to the right.
        if ((cellX + 1) < GRID_SIZE) {
            this.worldState[STENCHES][cellX + 1][cellY] = OCCUPIED_LOCATION;
        }
        // Cell above.
        if ((cellY - 1) >= 0) {
            this.worldState[STENCHES][cellX][cellY - 1] = OCCUPIED_LOCATION;
        }
        // Cell below.
        if ((cellY + 1) < GRID_SIZE) {
            this.worldState[STENCHES][cellX][cellY + 1] = OCCUPIED_LOCATION;
        }

    }
    public void setPitLocation(int cellX, int cellY) {
        this.clearEntity(PITS);
        this.worldState[PITS][cellX][cellY] = OCCUPIED_LOCATION;
        // Clear breezes.
        this.clearEntity(BREEZES);
        // set breezes.

        // Cell to the left.
        if ((cellX - 1) >= 0) {
            this.worldState[BREEZES][cellX - 1][cellY] = OCCUPIED_LOCATION;
        }
        // Cell to the right.
        if ((cellX + 1) < GRID_SIZE) {
            this.worldState[BREEZES][cellX + 1][cellY] = OCCUPIED_LOCATION;
        }
        // Cell above.
        if ((cellY - 1) >= 0) {
            this.worldState[BREEZES][cellX][cellY - 1] = OCCUPIED_LOCATION;
        }
        // Cell below.
        if ((cellY + 1) < GRID_SIZE) {
            this.worldState[BREEZES][cellX][cellY + 1] = OCCUPIED_LOCATION;
        }

    }
    public void setGoldLocation(int cellX, int cellY) {
        this.clearEntity(GOLD);
        this.worldState[GOLD][cellX][cellY] = OCCUPIED_LOCATION;
        // Clear breezes.
        this.clearEntity(GLITTER);
        // set breezes.

        // Cell to the left.
        if ((cellX - 1) >= 0) {
            this.worldState[GLITTER][cellX - 1][cellY] = OCCUPIED_LOCATION;
        }
        // Cell to the right.
        if ((cellX + 1) < GRID_SIZE) {
            this.worldState[GLITTER][cellX + 1][cellY] = OCCUPIED_LOCATION;
        }
        // Cell above.
        if ((cellY - 1) >= 0) {
            this.worldState[GLITTER][cellX][cellY - 1] = OCCUPIED_LOCATION;
        }
        // Cell below.
        if ((cellY + 1) < GRID_SIZE) {
            this.worldState[GLITTER][cellX][cellY + 1] = OCCUPIED_LOCATION;
        }

    }
    public CoOrdinate getEntityLocation(int entity) {
        CoOrdinate entityXY = new CoOrdinate();

        for (int sx=0; sx < GRID_SIZE; sx++) {
            for (int sy=0; sy < GRID_SIZE; sy++) {
                if (this.worldState[entity][sx][sy] == OCCUPIED_LOCATION) {
                    entityXY.x = sx;
                    entityXY.y = sy;
                    return  entityXY;
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
        /*if (newRandomNumber == lastRandomNumber) {
            getRandom();
        } else {
            lastRandomNumber = newRandomNumber;
        }
        return lastRandomNumber;
        */
        return newRandomNumber;
    }
}
