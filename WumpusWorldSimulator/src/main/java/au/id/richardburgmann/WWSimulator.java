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

package au.id.richardburgmann;

import au.id.richardburgmann.gui.GridPanel;
import au.id.richardburgmann.gui.MainWindow;
import au.id.richardburgmann.gui.Sprite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;


/**
 * <p>The Wumpus World Simulator enables you to explore the world of the Wumpus and Adventurer with a GUI
 * representation of the world. A number of experiments are planned using reinforcement learning for
 * the Adventurer. </p>
 * <p>This is a learning project that I'm undertaking just for fun. You can follow my own adventure
 * during the project at https://github.com/rburgmann/WumpusWorld</p>
 */
public class WWSimulator implements WindowListener {
    private static final Logger logger = LoggerFactory.getLogger(WWSimulator.class);
    public static Properties defaultProps = new Properties();
    public static Properties applicationProps;
    public static String DEFAULT_PROPERTIES_FILE_LOCATION = ".\\WumpusWorldSimulator\\src\\resources\\default.Properties";
    public static String APPLICATION_PROPERTIES_FILE_LOCATION = ".\\WumpusWorldSimulator\\src\\resources\\wwsimulator.properties";
    private static LogExperiment experimentalData;
    /**
     * gameState holds the state of the game. Events update it and it is used by the render engine.
     */
    private TheWorld gameState;
    private static WWSimulator wwSimulator;

    private TheWorld prevGameState;
    private int timeSteps = 0;

    private MainWindow mainWindow;
    private GridPanel  gridPanel;
    private StringBuffer adventurerFinalState = new StringBuffer(1024);

    private Adventurer adventurer;

    // Sprites are the guis of the things in the world.
    Sprite adventurerSprite;
    Sprite wumpusSprite;
    Sprite pitsSprite;
    Sprite goldSprite;
    Sprite wallsSprite;

    public static void main(String[] args) {

        wwSimulator = new WWSimulator();
        wwSimulator.startup();

        int runSimulatorANumberOfTimes = 0;
        if (args.length > 0) {
            runSimulatorANumberOfTimes = Integer.parseInt(args[0]);
            wwSimulator.runSimulations(runSimulatorANumberOfTimes);
        } else {
            wwSimulator.initSimulator();
        }
    }

    public void runSimulations(int runSimulatorANumberOfTimes) {
        logger.info("Run simulation " + runSimulatorANumberOfTimes + " time(s).");

        for (int l = 0; l < runSimulatorANumberOfTimes; l++) {

            wwSimulator.initSimulator();
            wwSimulator.run();
            wwSimulator.logRunResults();
        }
    }

    private void startup() {
        loadProperties();
        createExperimentLog();
        adventurer = new Adventurer(applicationProps);
    }

    public void shutdown() {
        logger.info("Shutdown.");
        saveProperties();
        logRunProperties();
        mainWindow.dispose();
        System.exit(0);
    }

    private void initSimulator() {
        createMainWindow();
        initGameState();
        updateMainWindow(gameState);
    }

    private void updateMainWindow(TheWorld gameState) {

        gridPanel = createSpritesAndGridPanel(gameState);
        mainWindow.setContentPane(gridPanel);
        mainWindow.setVisible(true);
        mainWindow.repaint();
    }

    private void loadProperties() {
        //
        // create and load default properties
        //
        FileInputStream in;
        try {
            in = new FileInputStream(WWSimulator.DEFAULT_PROPERTIES_FILE_LOCATION);
            defaultProps.load(in);
            in.close();
        } catch (Exception e) {
            logger.error("Default property file was not found at expected location");
            logger.error("I expected to find it at: " + WWSimulator.DEFAULT_PROPERTIES_FILE_LOCATION);
        }
        //
        // create application properties with default
        //
        applicationProps = new Properties(defaultProps);
        //
        // now load properties from last invocation
        //
        try {
            in = new FileInputStream(WWSimulator.APPLICATION_PROPERTIES_FILE_LOCATION);
            applicationProps.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            logger.error("Application property file was not found at expected location");
            logger.error("I expected to find it at: " + WWSimulator.APPLICATION_PROPERTIES_FILE_LOCATION);
            e.printStackTrace();
        } catch (IOException ioe) {
            logger.warn(ioe.toString());
        }
    }

    private void createExperimentLog() {
        experimentalData = new LogExperiment(applicationProps);
    }


    private void createMainWindow() {
        // In case there are any old windows left from previous iteration.
        if (mainWindow!=null) {
            mainWindow.dispose();
        }
        mainWindow = new MainWindow(this);
        mainWindow.assignProperties(applicationProps);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.addWindowListener(this);
        mainWindow.createBufferStrategy(1);

    }

    private void initGameState() {
        gameState = new TheWorld(applicationProps);
    }

    private void run() {

        CoOrdinate wumpusXY = gameState.getEntityLocation(TheWorld.WUMPUS);
        CoOrdinate pitXY = gameState.getEntityLocation(TheWorld.PITS);
        CoOrdinate wallXY = gameState.getEntityLocation(TheWorld.WALLS);
        CoOrdinate goldXY = gameState.getEntityLocation(TheWorld.GOLD);
        CoOrdinate adventurerXY = gameState.getEntityLocation(TheWorld.ADVENTURER);

        ArrayList<CoOrdinate> stenchesXY = gameState.getPerceptions(TheWorld.STENCHES);
        ArrayList<CoOrdinate> breezesXY = gameState.getPerceptions(TheWorld.BREEZES);

        adventurerFinalState = new StringBuffer();
        adventurerFinalState.append(adventurerXY.toCSV() + ",");
        adventurerFinalState.append(goldXY.toCSV() + ",");

        int reward = -1;
        timeSteps = 0;
        adventurer.setHealth(100);
        boolean runSim = true;
        while (runSim) {

            timeSteps = timeSteps + 1;
            logger.info("timestep " + timeSteps + " Adventurers health is " + this.adventurer.getHealth());

            // Store current game state so we can learn from the action we are taking next.
            prevGameState = TheWorld.cloneStateArray(gameState);

            // What action should we take ?
            int action = this.adventurer.think(gameState);

            // Take the action we chose.
            gameState = this.adventurer.act(gameState, action);

            updateMainWindow(gameState);

            CoOrdinate agentXY = gameState.getEntityLocation(TheWorld.ADVENTURER);

            reward = -1;

            if (agentXY.collision(stenchesXY)) {
                // This doesn't smell good.
                logger.info(" ");
                logger.info("****************************");
                logger.info("*** What a foul Stench ! ***");
                logger.info("****************************");
                logger.info(" ");
                wait(250);
                reward = reward - 1;
            }
            if (agentXY.collision(breezesXY)) {
                logger.info(" ");
                logger.info("****************************");
                logger.info("***  I feel a BREEZE !   ***");
                logger.info("****************************");
                logger.info(" ");
                wait(250);
                reward = reward - 1;
            }
            if (agentXY.collision(pitXY)) {
                // Falling .... !
                logger.info("*************************");
                logger.info("***  Ahhhh falling !  ***");
                logger.info("*************************");
                reward = reward - 100;
                wait(500);
                this.adventurerFinalState.append("PIT,");
                runSim = false;
            }
            if (agentXY.collision(wumpusXY)) {
                logger.info(" ");
                // Fighting .... !
                logger.info("*************************");
                logger.info("***  The Wumpus !     ***");
                logger.info("*************************");
                logger.info(" ");
                reward = reward - 100;
                wait(500);
                this.adventurerFinalState.append("WUMPUS,");
                runSim = false;
            }
            if (agentXY.collision(goldXY)) {
                // Rich !
                logger.info(" ");
                logger.info("*************************");
                logger.info("*** Gold ! I'm rich ! ***");
                logger.info("*************************");
                logger.info(" ");
                reward = reward + 101;
                wait(500);
                this.adventurerFinalState.append("GOLD,");
                runSim = false;
            }
            if (this.adventurer.getHealth() <= 0) {
                logger.info(" ");
                logger.info("*************************");
                logger.info("***  I'm Starving !   ***");
                logger.info("*************************");
                logger.info(" ");
                wait(250);
                this.adventurerFinalState.append("STARVATION,");
                reward = reward - 100;
                runSim = false;
            }
            if (agentXY.collision(wallXY)) {
                // Ouch !
                logger.info(" ");
                logger.info("*************************");
                logger.info("***   Ouch a wall  !  ***");
                logger.info("*************************");
                logger.info(" ");
                reward = reward - 10;
                wait(10);
                gameState = prevGameState; //move them back to whence they came.
            }
            adventurer.setHealth(adventurer.getHealth() + reward);

            updateMainWindow(gameState);

            adventurer.learn(prevGameState, action, reward);

            if (!runSim) {
                logger.info("Final score " + this.adventurer.getHealth());
            } else {
                wait(125);
            }
        }
        adventurerFinalState.append(Integer.toString(this.adventurer.getHealth()));
        adventurerFinalState.append(",");
        adventurerFinalState.append(timeSteps);
        System.gc();
    }

    public void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private GridPanel createSpritesAndGridPanel(TheWorld gameState) {
        gridPanel = new GridPanel(TheWorld.GRID_SIZE);
        createSprites(gameState, gridPanel);
        gridPanel = setSprites(gridPanel);
        return gridPanel;

    }

    private void createSprites(TheWorld gameState, GridPanel gridPanel) {

        adventurerSprite = new Sprite(TheWorld.ADVENTURER, gameState, gridPanel);
        wumpusSprite = new Sprite(TheWorld.WUMPUS, gameState, gridPanel);
        pitsSprite = new Sprite(TheWorld.PITS, gameState, gridPanel);
        goldSprite = new Sprite(TheWorld.GOLD, gameState, gridPanel);
        wallsSprite = new Sprite(TheWorld.WALLS, gameState, gridPanel);

    }

    private GridPanel setSprites(GridPanel gridPanel) {

        gridPanel.addSprite(wallsSprite);
        gridPanel.addSprite(goldSprite);
        gridPanel.addSprite(adventurerSprite);
        gridPanel.addSprite(wumpusSprite);
        gridPanel.addSprite(pitsSprite);

        this.gridPanel = gridPanel;

        return this.gridPanel;

    }

    private void logRunResults() {
        experimentalData.logData(adventurerFinalState.toString().trim());
    }

    private void logRunProperties() {
        experimentalData.logParams(applicationProps);
    }

    /**
     * Save properties from last invocation.
     */
    private void saveProperties() {
        try {
            FileOutputStream out = new FileOutputStream(WWSimulator.APPLICATION_PROPERTIES_FILE_LOCATION);
            applicationProps.store(out, "Wumpus World Simulator Last Invocation Properties.");
            out.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveDefaultProperties() {
        try {
            FileOutputStream out = new FileOutputStream(WWSimulator.DEFAULT_PROPERTIES_FILE_LOCATION);
            applicationProps.store(out, "Wumpus World Simulator Default Properties.");
            out.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        logger.debug("windowClosing Event.");
        shutdown();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
