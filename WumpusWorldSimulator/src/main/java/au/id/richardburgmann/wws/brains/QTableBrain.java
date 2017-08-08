/*
 *
 *    Copyright 2017 Richard Burgmann
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *
 */

package au.id.richardburgmann.wws.brains;

import au.id.richardburgmann.wws.*;
import au.id.richardburgmann.wws.gui.QStateViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;

import static au.id.richardburgmann.wws.WWS.DIR_LOCATION_OF_BRAIN_Q_VALUES;
import static au.id.richardburgmann.wws.WWS.LOAD_BRAIN_Q_VALUES_FROM;
import static au.id.richardburgmann.wws.WWS.SAVE_BRAIN_Q_VALUES_TO;

public class QTableBrain implements Brain, Serializable {
    private static final Logger logger = LoggerFactory.getLogger(QTableBrain.class);
    private static QStateViewer qStateViewer = new QStateViewer();
    private static Properties applicationProps = new Properties();

    /**
     * This is the learned experience of the agent..
     */
    private ArrayList qState = new ArrayList(16);
    private ArrayList rAction = new ArrayList(16);
    private ArrayList qAction = new ArrayList(16);
    /**
     * Setting gamma to 0.5 means we give equal weight to past and recent
     * experience. 0 means only immediate rewards are considered while 0.9 means longer term rewards are considered.
     */
    private double gamma = 0.5;
    private boolean adaptiveGamma = false;
    private double numberOfStepsToAdaptGamma = 50;
    private double gammaStartingValue = 0.99;
    private double gammaFinishingValue = 0.9;
    private double gammaCurrentStep = 0;
    private double onceOffRewardForExploring = 5.0;
    private double rSaturationLimit = 100.00;

    public QTableBrain() {
        super();
    }

    public static void main(String[] args) {
        WWSimulator wwSimulator;
        wwSimulator = new WWSimulator();
        WWSimulator.APPLICATION_PROPERTIES_FILE_LOCATION =
                ".\\\\WumpusWorldSimulator\\\\src\\\\resources\\\\testProperties.properties";
        wwSimulator.startup();
        QTableBrain qTableBrain = new QTableBrain();
        qTableBrain.listR(new TheWorld());
        qTableBrain.persistBrain();
    }

    public void persistBrain() {

        try {
            String dir = WWSimulator.applicationProps.getProperty(DIR_LOCATION_OF_BRAIN_Q_VALUES.toString());
            File dfile = new File(dir);
            String file = WWSimulator.applicationProps.getProperty(SAVE_BRAIN_Q_VALUES_TO.toString());
            String myMind = new String(dfile.getCanonicalPath() + "\\" + file);
            FileOutputStream fileOutputStream = new FileOutputStream(myMind, false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (Exception e) {
            logger.debug("Unable to persist brain state.");
            logger.error(e.getMessage());
            System.exit(1);
        }
    }

    public QTableBrain loadBrain() {
        QTableBrain returnValue;
        returnValue = new QTableBrain();

        try {
            String dir = WWSimulator.applicationProps.getProperty(DIR_LOCATION_OF_BRAIN_Q_VALUES.toString());
            File dfile = new File(dir);
            String file = WWSimulator.applicationProps.getProperty(LOAD_BRAIN_Q_VALUES_FROM.toString());
            String myMind = new String(dfile.getCanonicalPath() + "\\" + file);

            FileInputStream fileInputStream = new FileInputStream(myMind);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            returnValue = (QTableBrain) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            logger.debug("Unable to load brain state.");
            logger.error(e.getMessage());
            //System.exit(1);
        }
        return returnValue;
    }
    public int getBrainSize() {
        return this.qState.size();
    }

    public void setAdaptiveGamma(boolean OnOff) {
        adaptiveGamma = OnOff;
    }

    public void resetGammaCurrentStep() {
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
        logger.debug("think(" + state.getEntityLocation(TheWorld.ADVENTURER).toCSV() + ") Using Q Matrix.");

        CoOrdinate XY = state.getEntityLocation(TheWorld.ADVENTURER);

        int action = state.getALegalRandomMove(XY); // Default value.

        if (this.qState.contains(state)) {
            int ix = qState.indexOf(state);
            double[] allActions = (double[]) qAction.get(ix);
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
            //logger.debug("Best action is " + state.ACTION_CONSTANTS[action] + " with a value of " + maxActionReward);
        }
        brainDump();
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
        } else {
            qState.add(gridState);
            rMatrix = new double[TheWorld.GRID_SIZE];
            qMatrix = new double[TheWorld.GRID_SIZE];

            // Mark all illegal states as -1000 otherwise they will mess up with q calculations.
            for (int i = 0; i < state.GRID_SIZE; i++) {
                if (state.isThisMoveLegal(i, state.getEntityLocation(TheWorld.ADVENTURER))) {
                    rMatrix[i] = 0;
                    qMatrix[i] = onceOffRewardForExploring;
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
        tempAdventurer.setBrain(new QTableBrain());
        TheWorld futureState = tempAdventurer.act(gridState, action);
        // Have we seen this future state before?
        if (qState.contains(futureState)) {
            futureStateActions = (double[]) qAction.get(qState.indexOf(futureState));
        }
        // Get the max q value of all possible actions from here.

        double maxFutureReward = -1000;
        for (int i = 0; i < TheWorld.GRID_SIZE; i++) {

            if (futureStateActions[i] > maxFutureReward) {
                maxFutureReward = futureStateActions[i];
            }
        }
        // update qAction.
        rMatrix = (double[]) rAction.get(qState.indexOf(gridState));
        qMatrix = (double[]) qAction.get(qState.indexOf(gridState));
        qMatrix[action] = rMatrix[action] + (gamma * maxFutureReward);
        /*
         * Not a part of the formal algorithm but I found the reward values grow unbounded
         * now that it persists brain state and loads learned experiences. So I check if qMatrix exceeds
         * a saturation limit and if so limit it to the saturation limit.
         */
        if (qMatrix[action] > rSaturationLimit ) qMatrix[action] = rSaturationLimit;

        qAction.set(qState.indexOf(state), qMatrix);

        brainDump();

        return state;
    }


    public void brainDump() {

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

        logger.debug("BRAIN DUMP (Q Matrix)");
        logger.debug("=====================");
        logger.debug("Adventurer(Row,Col) [Left, Right, Up, Down]");
        for (Object n : brainDump) {
            BrainCell brainCell = (BrainCell) n;
            logger.debug("(" + brainCell.cell.row + "," + brainCell.cell.col + ")" +
                    " [" +
                    String.format("%1$07.3f", brainCell.weights[0]) + "," +
                    String.format("%1$07.3f", brainCell.weights[1]) + "," +
                    String.format("%1$07.3f", brainCell.weights[2]) + "," +
                    String.format("%1$07.3f", brainCell.weights[3]) + "]");
        }
        qStateViewer.setBrainState(brainDump);
    }

    public class BrainCell implements Comparable<BrainCell>, Comparator<BrainCell> {
        public CoOrdinate cell;
        public double[] weights;

        public BrainCell(CoOrdinate where, double[] actionWeights) {
            cell = where;
            weights = actionWeights;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("(" + cell.row + "," + cell.col + ")");
            stringBuffer.append(" [");
            for (double d : weights) {
                stringBuffer.append(String.format("%1$07.3f", d));
                stringBuffer.append(",");
            }
            stringBuffer.trimToSize();
            return stringBuffer.replace(stringBuffer.length() - 1, stringBuffer.length(), "]").toString();
        }

        public int compare(BrainCell b1, BrainCell b2) {
            return ((b1.cell.row - b2.cell.row) * TheWorld.GRID_SIZE) + (b1.cell.col - b2.cell.col);

        }

        public int compareTo(BrainCell b) {
            return ((this.cell.row - b.cell.row) * TheWorld.GRID_SIZE) + (this.cell.col - b.cell.col);

        }
    }
}
