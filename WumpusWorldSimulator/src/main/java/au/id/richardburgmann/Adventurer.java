package au.id.richardburgmann;
/**
 * Created by Richard Burgmann on 14/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Adventurer {
    private Sprite sprite;
    private TheWorld theWorld;
    public int myX = 0;
    public int myY = 0;
    private int health = 100;


    private static final Logger logger = LoggerFactory.getLogger(Adventurer.class);

    public static void main(String[] args) {

    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setTheWorld(TheWorld theWorld) {
        this.theWorld = theWorld;
    }
    public void thinkActdo() {
        this.act(this.think());
    }
    public int think() {
        // Get my location.
        for(int r=0; r<theWorld.GRID_SIZE; r++) {
            for(int c=0; c<theWorld.GRID_SIZE; c++) {
                if (theWorld.worldState[theWorld.ADVENTURER][r][c] == theWorld.OCCUPIED_LOCATION) {
                    myX = r;
                    myY = c;
                }
            }
        }
        // Now decide where to move next.
        return this.theWorld.getRandom();

    }
    private void act(int Action) {

        theWorld.worldState[theWorld.ADVENTURER][myX][myY] = theWorld.EMPTY_LOCATION;

        switch (Action) {
            case 0: myX = moveLeft(myX); break;
            case 1: myX = moveRight(myX); break;
            case 2: myY = moveUp(myY); break;
            case 3: myY = moveDown(myY); break;
        }

        theWorld.worldState[theWorld.ADVENTURER][myX][myY] = theWorld.OCCUPIED_LOCATION;

        logger.info("Adventurers health: " + this.getHealth());

    }
    private int moveLeft(int X_Loc) {
        if(X_Loc - 1 >= 0) {
            logger.info("Just a step to the left.");
            return (X_Loc - 1);

        } else {
            logger.info("I think I'll just stand here a moment.");
            return X_Loc;
        }
    }
    private int moveRight(int X_Loc) {
        if(X_Loc + 1 < theWorld.GRID_SIZE) {
            logger.info("Just a step to the right.");
            return (X_Loc + 1);
        } else {
            logger.info("I think I'll just stand here a moment.");
            return X_Loc;
        }
    }
    private int moveUp(int Y_Loc) {
        if(Y_Loc - 1 >= 0) {
            logger.info("Heading north.");
            return (Y_Loc - 1);
        } else {
            logger.info("I think I'll just stand here a moment.");
            return Y_Loc;
        }
    }
    private int moveDown(int Y_Loc) {
        if(Y_Loc + 1 < theWorld.GRID_SIZE) {
            logger.info("Heading south.");
            return (Y_Loc + 1);
        } else {
            logger.info("I think I'll just stand here a moment.");
            return Y_Loc;
        }

    }

}
