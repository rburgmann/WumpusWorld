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

import static java.lang.Integer.parseInt;

/**
 * <p>The Wumpus World Simulator enables you to explore the world of the Wumpus and Adventurer with a GUI
 * representation of the world. A number of experiments are planned using reinforcement learning for
 * the Adventurer. </p>
 * <p>This is a learning project that I'm undertaking just for fun. You can follow my own adventure
 * during the project at https://github.com/rburgmann/WumpusWorld</p>
 */
public class WWSimulator {
    private static final Logger logger = LoggerFactory.getLogger(WWSimulator.class);
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
        WWSimulator wwSimulator = new WWSimulator();
        wwSimulator.loadProperties();
        gameState = new TheWorld(applicationProps);

        if (args.length > 0) {
            int runNtimes = parseInt(args[0]);
            wwSimulator.batchRun(runNtimes);
        } else {

            logger.info("main: Started.");
            wwSimulator.run();
            logger.info("main: Finished.");
        }
        wwSimulator.saveProperties();
    }

    private void batchRun(int runNtimes) {
        while (runNtimes > 0) {
            WWSimulator wwSimulator = new WWSimulator();
            logger.info("Runs left in batch:" + runNtimes--);
            wwSimulator.run();
        }

    }

    public void run() {

        JFrame frame = new JFrame("Wumpus World Simulator");
        frame.setMinimumSize(minSize);
        frame.setSize(defaultWidth, defaultHeight);
        GridPanel gridPanel = new GridPanel(4);

        this.gameState.clearWorldState();
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
                Thread.sleep(500);
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
                gameState.initEntity(TheWorld.ADVENTURER, TheWorld.FIXED_START, adventurersPrevXY.x,
                        adventurersPrevXY.y);

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
        Properties defaultProps = new Properties();
        FileInputStream in;
        try {
            in = new FileInputStream(WWSimulator.DEFAULT_PROPERTIES_FILE_LOCATION);
            defaultProps.load(in);
            in.close();
        } catch (Exception e) {
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
            e.printStackTrace();
        } catch (IOException ioe) {
            logger.warn(ioe.toString());
        }
    }

    /**
     * In case there are no default properties this method will initialise the critical properties and create a
     * default properties file.
     */
    private void createDefaultProperties() {

        gameState.setADVENTURER_PLACEMENT(TheWorld.RANDOM_START);
        gameState.initEntity(TheWorld.WUMPUS, TheWorld.RANDOM_START);
        gameState.initEntity(TheWorld.PITS, TheWorld.RANDOM_START);
        gameState.initEntity(TheWorld.GOLD, TheWorld.RANDOM_START);
        gameState.initEntity(TheWorld.WALLS, TheWorld.RANDOM_START);


        this.saveDefaultProperties();
    }
}
