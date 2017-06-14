package au.id.richardburgmann;
/**
 * Created by Richard Burgmann on 11/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * Decides if the Adventurer will start in the fixed location 0,0 or in
     * a random location around the outside edge of the grid.
     * 1 = Random starting location.
     * 0 = Fixed starting location at 0,0.
     */
    private int randomStartAdventurer = 0;
    /**
     * Decides if the Wumpus will start in the fixed location 2,2 or in
     * a random location on the grid.
     * 1 = Random starting location.
     * 0 = Fixed starting location at 2,2.
     */
    private int randomStartWumpus = 0;
    /**
     * Decides if the Entity will start in the fixed location 2,2 or in
     * a random location on the grid.
     * 1 = Random starting location.
     * 0 = Fixed starting location at 2,2.
     */
    private int randomStartGold = 1;
    private int randomStartWalls = 1;
    private int randomStartPits = 1;

    public void TheWorld() {
        logger.debug("TheWorld default constructor.");
        this.clearWorldState();
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
    public int getRandomStartAdventurer() {
        return randomStartAdventurer;
    }

    /**
     * Sets the policy for how the Adventurer will be placed at the start of a run. Either randomly
     * along the outside edge or at a fixed location (0,0).
     *
     * @param randomStartAdventurer 1 = Random position along edge or 0 start at the fixed location.
     */
    public void setRandomStartAdventurer(int randomStartAdventurer) {
        this.randomStartAdventurer = randomStartAdventurer;
    }

    public int getRandomStartWumpus() {
        return randomStartWumpus;
    }

    public void setRandomStartWumpus(int randomStartWumpus) {
        this.randomStartWumpus = randomStartWumpus;
    }

    public int getRandomStartGold() {
        return randomStartGold;
    }

    public void setRandomStartGold(int randomStartGold) {
        this.randomStartGold = randomStartGold;
    }

    public int getRandomStartWalls() {
        return randomStartWalls;
    }

    public void setRandomStartWalls(int randomStartWalls) {
        this.randomStartWalls = randomStartWalls;
    }

    public int getRandomStartPits() {
        return randomStartPits;
    }

    public void setRandomStartPits(int randomStartPits) {
        this.randomStartPits = randomStartPits;
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
     * The randomStartAdventurer flag should be set prior to calling this method.
     */
    public void initAdventurer() {
        this.clearEntity(ADVENTURER);
        this.clearEntity(VISITED);
        if (this.randomStartAdventurer == FIXED_START ||
                this.randomStartAdventurer == RANDOM_START) {
            switch (this.randomStartAdventurer) {
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
            logger.warn("Incorrect State: randomStartAdventurer flag contains an unknown value of " +
                    this.randomStartAdventurer + ", defaulting to a fixed starting position instead.");
            this.setRandomStartAdventurer(FIXED_START);
            this.initAdventurer();
        }
    }

    /**
     * Initialise the Adventurers starting position.
     * The randomStartAdventurer flag should be set prior to calling this method.
     */
    public void initWumpus() {
        this.clearEntity(WUMPUS);
        if (this.randomStartWumpus == FIXED_START ||
                this.randomStartWumpus == RANDOM_START) {
            switch (this.randomStartWumpus) {
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
            logger.warn("Incorrect State: randomStartAdventurer flag contains an unknown value of " +
                    this.randomStartAdventurer + ", defaulting to a fixed starting position instead.");
            this.setRandomStartAdventurer(FIXED_START);
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
            logger.warn("Incorrect State: randomStartAdventurer flag contains an unknown value of " +
                    this.randomStartAdventurer + ", defaulting to a fixed starting position instead.");
            this.setRandomStartAdventurer(FIXED_START);
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
