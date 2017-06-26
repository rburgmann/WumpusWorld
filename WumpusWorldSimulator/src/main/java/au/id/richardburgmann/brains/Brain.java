package au.id.richardburgmann.brains;
/**
 * Created by Richard Burgmann on 21/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import au.id.richardburgmann.CoOrdinate;
import au.id.richardburgmann.TheWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Brain {
    private static final Logger logger = LoggerFactory.getLogger(Brain.class);
    public int direction = 0;
    public CoOrdinate lastXY = new CoOrdinate();
    public CoOrdinate currXY = new CoOrdinate();

    public static void main(String[] args) {

    }
    public Brain(){
        lastXY.row = -1;
        lastXY.col = -1;
    }
    public CoOrdinate whereAmI(TheWorld state){


        CoOrdinate xy = new CoOrdinate();

        for (int r = 0; r < state.GRID_SIZE; r++) {
            for (int c = 0; c < state.GRID_SIZE; c++) {
                if (state.worldState[state.ADVENTURER][r][c] == state.OCCUPIED_LOCATION) {
                    xy.row = r;
                    xy.col = c;
                    break;
                }
            }
        }
        logger.debug("Where Am I? ("+xy.toCSV()+")");
        return xy;

    }
    public int think(TheWorld state) {
        logger.debug("think() Getting a legal random move for now.");
        int action = state.getALegalRandomMove(state.getEntityLocation(TheWorld.ADVENTURER));
        return action;
    }
    public TheWorld learn(TheWorld currState, int action, int reward) {
            return currState;
    }
    public void brainDump(){
        logger.debug("BRAIN DUMP");
    }
}
