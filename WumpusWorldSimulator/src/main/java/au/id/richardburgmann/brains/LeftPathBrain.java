package au.id.richardburgmann.brains;
/**
 * Created by Richard Burgmann on 21/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import au.id.richardburgmann.TheWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeftPathBrain extends Brain {
    private static final Logger logger = LoggerFactory.getLogger(LeftPathBrain.class);


    public static void main(String[] args) {

    }
    public LeftPathBrain(){
        super();
    }


    @Override
    public int think(TheWorld state) {
        logger.debug("Current direction is "+direction);
        this.currXY = this.whereAmI(state);
        int action = this.direction;

        switch (direction) {
            case 0:
                // Move left.
                if ((currXY.row - 1) >= 0) {
                    // ok, keep moving left.
                    action = direction;
                    logger.debug("Keep moving left.");

                } else {
                    // change direction
                    direction = 3;

                    //action  =  this.think(state);
                    logger.debug("Reached left edge, change direction to " +direction);
                    //this.think(state); // because we may be in a corner.
                }
                break;
            case 1:
                // Move right
                if ((currXY.row + 1) < state.GRID_SIZE) {
                    // ok, keep moving right.
                    action  = direction;
                    logger.debug("Keep moving right.");
                } else {
                    // change direction
                    direction = 2;
                    //action = this.think(state);
                    logger.debug("Reached right edge, change direction to " +direction);
                    //this.think(state); // because we may be in a corner.
                }
                break;
            case 2:
                // Move up
                if ((currXY.col - 1) >= 0) {
                    // ok, keep moving up.
                    action = direction;
                    logger.debug("Keep moving up.");

                } else {
                    // change direction
                    direction = 0;
                    //action = this.think(state);
                    logger.debug("Reached top edge, change direction to " +direction);
                    //this.think(state); // because we may be in a corner.
                }
                break;
            case 3:
                // Move down.

                if ((currXY.col + 1) < state.GRID_SIZE) {
                    // ok, keep moving down.
                    action = direction;
                    logger.debug("Keep moving down.");
                } else {
                    // change direction

                    direction = 1;
                    //action = this.think(state);
                    logger.debug("Reached bottom edge, change direction to " +direction);
                    //this.think(state); // because we may be in a corner.
                }
                break;
        }

        // if im in the same place as last time change direction
        if ((lastXY.row == currXY.row) && (lastXY.col == currXY.col)) {
            switch (direction) {
                case 0:
                    direction = 3;
                    break;
                case 1:
                    direction = 2;
                    break;
                case 2:
                    direction = 0;
                    break;
                case 3:
                    direction = 1;
                    break;
            }
            logger.info("Path blocked, try another direction.");
        } else {
            lastXY = currXY;

        }
        action = direction;
        return action;
    }
}
