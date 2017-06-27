package au.id.richardburgmann.brains;
/**
 * Created by Richard Burgmann on 22/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import au.id.richardburgmann.Adventurer;
import au.id.richardburgmann.CoOrdinate;
import au.id.richardburgmann.TheWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class QTableBrain extends Brain {
    private static final Logger logger = LoggerFactory.getLogger(QTableBrain.class);
    /**
     * This is the learned experience of the agent..
     */
    private ArrayList qState = new ArrayList(16);
    private ArrayList rAction = new ArrayList(16);
    private ArrayList qAction = new ArrayList(16);

    /**
     * Setting gamma to 0.5 means we give equal weight to past and recent
     * experience.
     */
    private double gamma = 0.1;

    public static void main(String[] args) {
        QTableBrain qTableBrain = new QTableBrain();
        qTableBrain.listR(new TheWorld());
    }

    public void listR(TheWorld state) {
        logger.debug(String.valueOf(state.matrixR.length));

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                logger.debug("(" + r + "," + c + ")  " + "[" + state.matrixR[(r * 4) + c][0] + "," + state.matrixR[(r * 4) + c][1] + "," + state.matrixR[(r * 4) + c][2] + "," + state.matrixR[(r * 4) + c][3] + "]");


            }
        }
    }

    @Override
    public int think(TheWorld state) {
        logger.debug("think("+state.getEntityLocation(TheWorld.ADVENTURER).toCSV()+") Using Q Matrix.");

        //this.brainDump();

        TheWorld temp = state.cloneStateArray(state);
        CoOrdinate XY = state.getEntityLocation(TheWorld.ADVENTURER);

        int action = temp.getALegalRandomMove(XY); // Default value.

        if (this.qState.contains(temp)) {
            logger.debug("...Been here before, whats the best move I know?");

            int ix = qState.indexOf(temp);
            logger.debug("Found state at location " + ix);

            double[] allActions = (double[]) qAction.get(ix);
            logger.debug("Q values are [" + allActions[0] + "," + allActions[1] + "," + allActions[2] + "," + allActions[3] + "]");

            double maxActionReward = -1000;

            int maxAction = 0;
            for (int i = 0; i < TheWorld.GRID_SIZE; i++) {
                if (state.isThisMoveLegal(i, XY) &&
                        (allActions[i] > maxActionReward)) {
                    maxActionReward = allActions[i];
                    maxAction = i;
                }
            }
            action = maxAction;
            logger.debug("Best action is " + temp.ACTION_CONSTANTS[action] + " with a value of " + maxActionReward);
        }

        return action;

    }

    //@Override
    public TheWorld learn(TheWorld state, int action, int reward) {
        logger.debug("learn( Adv(" + state.getEntityLocation(TheWorld.ADVENTURER).toCSV() + ") Action is " +
                state.ACTION_CONSTANTS[action] + "(" + action + ") Reward is " + reward);
        /*
         Store the current state and the reward value. This represents where we are now, not how we got here.
         in once sense storing the reward against the action is wrong, because it is the action that got us to this
         state, not the reward for moving to the next state, so it should really be stored in the state that got us
         here. To work that out I need to know the inverse of each action.
         */
        TheWorld gridState = state.cloneStateArray(state);
        double[] currentStateActions = new double[TheWorld.GRID_SIZE];
        double[] futureStateActions = new double[TheWorld.GRID_SIZE];
        double[] rMatrix = new double[TheWorld.GRID_SIZE];
        double[] qMatrix = new double[TheWorld.GRID_SIZE];

        if (qState.contains(gridState)) {
            int ix = qState.indexOf(gridState);
            currentStateActions = (double[]) rAction.get(qState.indexOf(gridState));
            currentStateActions[action] = reward;
            rAction.set(ix, currentStateActions);
            logger.debug("Found state within qState at index " + ix);
        } else {
            qState.add(gridState);
            rMatrix = new double[TheWorld.GRID_SIZE];
            qMatrix = new double[TheWorld.GRID_SIZE];

            // Mark all illegal states as -1000 otherwise they will mess up with q calculations.
            for (int i=0; i<state.GRID_SIZE; i++){
                if (state.isThisMoveLegal(i, state.getEntityLocation(TheWorld.ADVENTURER))) {
                    rMatrix[i] = 0;
                    qMatrix[i] = 0;

                } else {
                    rMatrix[i] = -1000;
                    qMatrix[i] = -1000;
                }
            }
            rMatrix[action] = reward;
            qAction.add(qMatrix);
            rAction.add(rMatrix);
            logger.debug("Did not find state within qState. Adding it, qState size is " + qState.size());
        }
        // find next state based on state and the action.
        Adventurer tempAdventurer = new Adventurer();
        TheWorld futureState = tempAdventurer.act(gridState, action);
        // Have we seen this future state before?
        if (qState.contains(futureState)) {
            futureStateActions = (double[]) qAction.get(qState.indexOf(futureState));
        }
        // Get the max q value of all possible actions from here.

        double maxFutureReward = -1000;
        for (int i=0; i<TheWorld.GRID_SIZE; i++) {
            //logger.debug("maxFutureReward " + maxFutureReward + " futureActions["+i+"]"+futureStateActions[i]);
            if (futureStateActions[i] > maxFutureReward) {
                maxFutureReward = futureStateActions[i];
            }
        }
        // update qAction.
        rMatrix = (double[]) rAction.get(qState.indexOf(gridState));
        qMatrix = (double[]) qAction.get(qState.indexOf(gridState));
        qMatrix[action] = rMatrix[action] + (gamma * maxFutureReward);
        qAction.set(qState.indexOf(state), qMatrix);

        brainDump();
        return state;
    }


    public void brainDump() {
        logger.debug("BRAIN DUMP (Q Matrix)");
        logger.debug("=====================");
        logger.debug("Adv{Row,Col}(#Visited)[Left, Right, Up, Down]");

        for (int i = 0; i < qState.size(); i++) {
            double[] allActions = (double[]) qAction.get(i);
            TheWorld theWorld = (TheWorld) qState.get(i);
            CoOrdinate coOrdinate = theWorld.getEntityLocation(TheWorld.ADVENTURER);
            int v = theWorld.getCountVisited();
            logger.debug("{" + coOrdinate.row + "," + coOrdinate.col + "}" +
                    "(" + v + ")[" + allActions[0] + ","
                    + allActions[1] + "," + allActions[2] + "," + allActions[3] + "]");
        }


    }
}
