package au.id.richardburgmann;
/**
 * Created by Richard Burgmann on 8/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <p>The Wumpus World Simulator enables you to explore the world of the Wumpus and Adventurer with a GUI
 * representation of the world. A number of experiments are planned using reinforcement learning for
 * the Adventurer. </p>
 * <p>This is a learning project that I'm undertaking just for fun. You can follow my own adventure
 * during the project at https://github.com/rburgmann/WumpusWorld</p>
 */
public class WWSimulator {
    private static final Logger logger = LoggerFactory.getLogger(WWSimulator.class);
    private static Properties defaultProps = new Properties();
    private static Properties applicationProps;
    private static String DEFAULT_PROPERTIES_FILE_LOCATION = ".\\WumpusWorldSimulator\\src\\resources\\default.Properties";
    private static String APPLICATION_PROPERTIES_FILE_LOCATION = ".\\WumpusWorldSimulator\\src\\resources\\wwsimulator.properties";
    /**
     * gameState holds the state of the game. Events update it and it is used by the render engine.
     */
    private static TheWorld gameState;
    private int defaultWidth = 1200;
    private int defaultHeight = 1200;
    private int defaultMargin = 20;
    private Dimension minSize = new Dimension(440, 440);
    private Adventurer agent;

    public static void main(String[] args) {
        logger.info("main: Started.");

        WWSimulator wwSimulator = new WWSimulator();
        wwSimulator.loadProperties();
        wwSimulator.initialiseWWSimulatorVariables();
        gameState = new TheWorld(applicationProps);

        wwSimulator.run();

        wwSimulator.saveProperties();
        logger.info("main: Finished.");
    }

    private void run() {

        JFrame frame = new JFrame("Wumpus World Simulator");
        frame.setMinimumSize(minSize);
        frame.setSize(defaultWidth, defaultHeight);
        GridPanel gridPanel = new GridPanel(4);


        this.gameState.initAdventurer();
        Sprite adventurer = new Sprite(gameState.ADVENTURER, gameState, gridPanel);
        agent = new Adventurer();
        agent.setSprite(adventurer);
        agent.setTheWorld(gameState);
        gridPanel.setAdventurer(adventurer);

        Sprite wumpus = new Sprite(gameState.WUMPUS, gameState, gridPanel);
        gridPanel.setWumpus(wumpus);


        Sprite pits = new Sprite(gameState.PITS, gameState, gridPanel);
        gridPanel.setPits(pits);


        Sprite gold = new Sprite(gameState.GOLD, gameState, gridPanel);
        gridPanel.setGold(gold);


        Sprite walls = new Sprite(gameState.WALLS, gameState, gridPanel);
        gridPanel.setWalls(walls);

        frame.setVisible(true);
        frame.add(gridPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addComponentListener(new GuiEventListener());

        // Simulation Loop
        boolean runSim = true;
        CoOrdinate wumpusXY = this.gameState.getEntityLocation(TheWorld.WUMPUS);
        CoOrdinate pitXY = this.gameState.getEntityLocation(TheWorld.PITS);
        CoOrdinate wallXY = this.gameState.getEntityLocation(TheWorld.WALLS);
        CoOrdinate goldXY = this.gameState.getEntityLocation(TheWorld.GOLD);


        while (runSim) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CoOrdinate adventurersPrevXY = this.gameState.getEntityLocation(TheWorld.ADVENTURER);
            agent.thinkActdo();

            frame.repaint();

            agent.setHealth(agent.getHealth() - 1);


            if (agent.myX == wallXY.x && agent.myY == wallXY.y) {
                // Ouch !
                logger.info("Ouch a wall !");
                agent.setHealth(agent.getHealth() - 10);
                // move them back to whence they came.
                gameState.moveEntityTo(TheWorld.ADVENTURER, adventurersPrevXY.x, adventurersPrevXY.y);

            }
            if (agent.myX == pitXY.x && agent.myY == pitXY.y) {
                // Falling .... !
                logger.info("Ahhhh falling !");
                agent.setHealth(agent.getHealth() - 100);
                runSim = false;
            }
            if (agent.myX == wumpusXY.x && agent.myY == wumpusXY.y) {
                // Fighting .... !
                logger.info("The Wumpus !");
                agent.setHealth(agent.getHealth() - 100);
                runSim = false;
            }
            if (agent.myX == goldXY.x && agent.myY == goldXY.y) {
                // Rich !
                logger.info("Gold ! I'm rich !");
                agent.setHealth(agent.getHealth() + 100);
                runSim = false;
            }

            if (agent.getHealth() <= 0) {
                runSim = false;

            }
            if (!runSim) {
                logger.info("Final score " + agent.getHealth());
            }
        }
        //frame.dispose();
        System.gc();

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

    /**
     * Load default properties from local file into properties object.
     */
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
            logger.error("I will attempt to create a new file when I exit.");
            this.createDefaultProperties();
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
            logger.error("I will attempt to create a new file when I exit.");
            e.printStackTrace();
        } catch (IOException ioe) {
            logger.warn(ioe.toString());
        }
    }

    /**
     * Once the properties have been loaded into memory apply them to the various variables in the program.
     */
    private void initialiseWWSimulatorVariables() {
        int temp;
        /**
         * Just in case there is no data in any of the property files, use the hardcoded
         * default. The try case block is so we don't destroy the hard coded value if we
         * have nothing in the property files.
         */
        try {
            temp = Integer.parseInt((String) applicationProps.getProperty("defaultMargin"));
            if (temp > 0) defaultMargin = temp;
        } catch (Exception e) {
            logger.warn("No defaultMargin defined in either properties file.");
            logger.warn(e.getMessage());
        }

        try {
            temp = 0; // reset temp as a precaution.
            temp = Integer.parseInt((String) applicationProps.getProperty("defaultHeight"));
            if (temp > 0) defaultHeight = temp;
        } catch (Exception e) {
            logger.warn("No defaultHeight defined in either properties file.");
            logger.warn(e.getMessage());
        }

        defaultWidth = Integer.parseInt((String) applicationProps.getProperty("defaultWidth"));
        try {
            temp = 0; // reset temp as a precaution.
            temp = Integer.parseInt((String) applicationProps.getProperty("defaultWidth"));
            if (temp > 0) defaultWidth = temp;
        } catch (Exception e) {
            logger.warn("No defaultWidth defined in either properties file.");
            logger.warn(e.getMessage());
        }

    }

    /**
     * In case there are no default properties this method will initialise the critical properties and create a
     * default properties file.
     */
    private void createDefaultProperties() {
        gameState.initEntity(TheWorld.ADVENTURER, TheWorld.FIXED_START, 0,0);
        gameState.initEntity(TheWorld.WUMPUS, TheWorld.RANDOM_START, 0,0);
        gameState.initEntity(TheWorld.PITS, TheWorld.RANDOM_START, 0,0);
        gameState.initEntity(TheWorld.GOLD, TheWorld.RANDOM_START,0,0);
        gameState.initEntity(TheWorld.WALLS, TheWorld.RANDOM_START,0,0);

        this.saveDefaultProperties();
        this.loadProperties();
    }
}
