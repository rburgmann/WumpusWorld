package au.id.richardburgmann.brains;
/*
   Copyright 2017 Richard Burgmann

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import au.id.richardburgmann.Adventurer;
import au.id.richardburgmann.CoOrdinate;
import au.id.richardburgmann.TheWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
     * experience. 0 means only immeadiate rewards are considered while 0.9 means longer term rewards are considered.
     */
    private double gamma = 0.95;
    private boolean adaptiveGamma = true;
    private double numberOfStepsToAdaptGamma = 200;
    private double gammaStartingValue = 0.1;
    private double gammaFinishingValue = 0.99;
    private double gammaCurrentStep = 0;


    public static void main(String[] args) {
        QTableBrain qTableBrain = new QTableBrain();
        qTableBrain.listR(new TheWorld());
    }
    public void setAdaptiveGamma(boolean OnOff) {
        adaptiveGamma = OnOff;
    }
    public void resetGammaCurrentStep(){
        gammaCurrentStep = 0;
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

        CoOrdinate XY = state.getEntityLocation(TheWorld.ADVENTURER);

        int action = state.getALegalRandomMove(XY); // Default value.

        if (this.qState.contains(state)) {
            logger.debug("...Been here before, whats the best move I know?");

            int ix = qState.indexOf(state);
            logger.debug("Found state at location " + ix);


            double[] allActions = (double[]) qAction.get(ix);
           // logger.debug("Q values are [" + allActions[0] + "," + allActions[1] + "," + allActions[2] + "," + allActions[3] + "]");

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
            logger.debug("Best action is " + state.ACTION_CONSTANTS[action] + " with a value of " + maxActionReward);
        }

        return action;

    }

    //@Override
    public TheWorld learn(TheWorld state, int action, int reward) {

        TheWorld gridState = state.cloneStateArray(state);
        double[] currentStateActions = new double[TheWorld.GRID_SIZE];
        double[] futureStateActions = new double[TheWorld.GRID_SIZE];
        double[] rMatrix = new double[TheWorld.GRID_SIZE];
        double[] qMatrix = new double[TheWorld.GRID_SIZE];

        if (adaptiveGamma) {
            if (gammaCurrentStep < numberOfStepsToAdaptGamma) {
                gammaCurrentStep = gammaCurrentStep + 1;
                gamma = gammaStartingValue + (gammaCurrentStep * ((gammaFinishingValue - gammaStartingValue) / numberOfStepsToAdaptGamma));

            }
        }

        if (qState.contains(gridState)) {
            int ix = qState.indexOf(gridState);
            currentStateActions = (double[]) rAction.get(qState.indexOf(gridState));
            currentStateActions[action] = reward;
            rAction.set(ix, currentStateActions);
            //logger.debug("Found state within qState at index " + ix);
        } else {
            qState.add(gridState);
            rMatrix = new double[TheWorld.GRID_SIZE];
            qMatrix = new double[TheWorld.GRID_SIZE];

            // Mark all illegal states as -1000 otherwise they will mess up with q calculations.
            for (int i=0; i<state.GRID_SIZE; i++){
                if (state.isThisMoveLegal(i, state.getEntityLocation(TheWorld.ADVENTURER))) {
                    rMatrix[i] = 0;
                    qMatrix[i] = gridState.getRandom(); // reward novelty  /  exploration.
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
        logger.debug("Adventurer(Row,Col) [Left, Right, Up, Down]");

        ArrayList brainDump = new ArrayList<BrainCell>(16);

        for (int i = 0; i < qState.size(); i++) {
            TheWorld theWorld = (TheWorld) qState.get(i);
            CoOrdinate coOrdinate = theWorld.getEntityLocation(TheWorld.ADVENTURER);
            double[] weights = (double[]) qAction.get(i);
            BrainCell brainCell;
            brainCell = new BrainCell(coOrdinate, weights);
            brainDump.add(brainCell);
        }
        Collections.sort(brainDump);
        for (Object n: brainDump) {
            BrainCell brainCell = (BrainCell) n;
            logger.debug("(" + brainCell.cell.row + "," + brainCell.cell.col + ")" +
                    " [" +
                    String.format( "%1$07.3f", brainCell.weights[0] ) + "," +
                    String.format( "%1$07.3f", brainCell.weights[1] ) + "," +
                    String.format( "%1$07.3f", brainCell.weights[2] ) + "," +
                    String.format( "%1$07.3f", brainCell.weights[3] ) + "]");
        }
    }
    public class BrainCell implements Comparable<BrainCell>, Comparator<BrainCell> {
        public CoOrdinate cell;
        public double[] weights;

        public BrainCell(CoOrdinate where, double[] actionWeights) {
            cell = where;
            weights = actionWeights;
        }
        public int compare(BrainCell b1, BrainCell b2) {
            return ((b1.cell.row - b2.cell.row) * TheWorld.GRID_SIZE ) +  (b1.cell.col - b2.cell.col);

        }
        public int compareTo(BrainCell b) {
            return ((this.cell.row - b.cell.row) * TheWorld.GRID_SIZE ) + (this.cell.col - b.cell.col);

        }
    }
}
