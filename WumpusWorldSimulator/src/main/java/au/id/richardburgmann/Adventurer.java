package au.id.richardburgmann;
/**
 * Created by Richard Burgmann on 14/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import au.id.richardburgmann.brains.Brain;
import au.id.richardburgmann.brains.QTableBrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Adventurer {
    private static final Logger logger = LoggerFactory.getLogger(Adventurer.class);
    public Brain brain = new QTableBrain();
    private int health = 1;
    private int maxHealth = 1;

    public static void main(String[] args) {

    }
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (health > maxHealth) {
            maxHealth = health;
        }
    }
    public int getMaxHealth() {return this.maxHealth;}

   public int think(TheWorld currState) {
        return brain.think(currState);
    }

    public TheWorld act(TheWorld currState, int action) {
        logger.debug("Adv location is ("+currState.getEntityLocation(TheWorld.ADVENTURER).toCSV()+
                        ") action is " + TheWorld.ACTION_CONSTANTS[action]);
        // Is the action legal ?
        TheWorld newState = currState.cloneStateArray(currState);

        CoOrdinate myXY = newState.getEntityLocation(TheWorld.ADVENTURER);
        if (newState.isThisMoveLegal(action, myXY)) {


            newState.worldState[TheWorld.ADVENTURER][myXY.row][myXY.col] = TheWorld.EMPTY_LOCATION;
            newState.worldState[TheWorld.VISITED][myXY.row][myXY.col] = TheWorld.OCCUPIED_LOCATION;

            switch (action) {
                case 0:
                    myXY.col = moveLeft(myXY.col);
                    break;
                case 1:
                    myXY.col = moveRight(myXY.col);
                    break;
                case 2:
                    myXY.row = moveUp(myXY.row);
                    break;
                case 3:
                    myXY.row = moveDown(myXY.row);
                    break;
            }

            newState.worldState[TheWorld.ADVENTURER][myXY.row][myXY.col] = TheWorld.OCCUPIED_LOCATION;
            newState.worldState[TheWorld.VISITED][myXY.row][myXY.col] = TheWorld.OCCUPIED_LOCATION;
        }
        return newState;


    }

    public TheWorld learn(TheWorld state, int action, int reward) {
        logger.debug("learning...");
        TheWorld newState = state.cloneStateArray(state);
        int      newAction = action;
        int      newReward = reward;

        return this.brain.learn(newState, newAction, newReward);

    }

    private int moveLeft(int X_Loc) {
        if (X_Loc - 1 >= 0) {
            logger.info("Just a step to the left.");
            return (X_Loc - 1);

        } else {
            logger.info("Left grid wall. I think I'll just stand here a moment.");
            return X_Loc;
        }
    }

    private int moveRight(int X_Loc) {
        if (X_Loc + 1 < TheWorld.GRID_SIZE) {
            logger.info("Just a step to the right.");
            return (X_Loc + 1);
        } else {
            logger.info("Right grid wall. I think I'll just stand here a moment.");
            return X_Loc;

        }
    }

    private int moveUp(int Y_Loc) {
        if (Y_Loc - 1 >= 0) {
            logger.info("Moving up.");
            return (Y_Loc - 1);
        } else {
            logger.info("North grid limit. I think I'll just stand here a moment.");
            return Y_Loc;
        }
    }

    private int moveDown(int Y_Loc) {
        if (Y_Loc + 1 < TheWorld.GRID_SIZE) {
            logger.info("Moving down.");
            return (Y_Loc + 1);
        } else {
            logger.info("South grid limit. I think I'll just stand here a moment.");
            return Y_Loc;
        }

    }

}
