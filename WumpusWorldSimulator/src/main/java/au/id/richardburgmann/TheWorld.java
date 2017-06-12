package au.id.richardburgmann;
/**
 * Created by Richard Burgmann on 11/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class holds the state of the world within the simulator.
 * See https://github.com/rburgmann/WumpusWorld/wiki/W2101-Data-Model:-World-State
 * for further details.
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
     *
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
    private static final double RANDOM_SEED = 11235813;
    /**
     * Decides if the Adventurer will start in the fixed location 0,0 or in
     * a random location around the outside edge of the grid.
     * 1 = Random starting location.
     * 0 = Fixed starting location at 0,0.
     */
    private int randomStartAdventurer = 0;
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
     * <p>
     * worldState[WUMPUS][2][1] = +1 and so that is where the Wumpus is.
     */
    public int[][][] worldState = new int[9][4][4];
    /**
     * Constant representing EMPTY_LOCATION = -1
     */
    public static final int  EMPTY_LOCATION = -1;
    /**
     * Constant representing OCCUPIED_LOCATION = +1
     */
    public static final int  OCCUPIED_LOCATION = 1;

    /**
     * Used for testing.
     */
    public static void main(String[] args) {

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
     * @return 1 = Random start somewhere on the outside edges of the grid world.
     *         0 = Fixed start at 0,0.
     */
    public int getRandomStartAdventurer() {
        return randomStartAdventurer;
    }

    /**
     * Sets the policy for how the Adventurer will be placed at the start of a run. Either randomly
     * along the outside edge or at a fixed location (0,0).
     * @param randomStartAdventurer 1 = Random position along edge or 0 start at the fixed location.
     */
    public void setRandomStartAdventurer(int randomStartAdventurer) {
        this.randomStartAdventurer = randomStartAdventurer;
    }
    /**
     * Initialises the data model to -1 representing an empty state. Should be called at objects
     * creation and whenever you want to clear it of values. It will call the clear method for each of
     * the entities in the world.
     */
    public void clearWorldState() {
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
            for (int row = 0 ; row < this.worldState[entity].length ; row++) {
                for (int col = 0 ; col < this.worldState[entity][row].length ; col++) {
                     this.worldState[entity][row][col] = EMPTY_LOCATION;
                }
            }

        }

    }
}
