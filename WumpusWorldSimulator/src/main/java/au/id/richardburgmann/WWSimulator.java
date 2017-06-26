package au.id.richardburgmann;

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
    private static LogExperiment experimentalData;
    /**
     * gameState holds the state of the game. Events update it and it is used by the render engine.
     */
    private static TheWorld gameState;
    private TheWorld prevGameState;
    private int defaultWidth = 1200;
    private int defaultHeight = 1200;
    private int timeSteps = 0;
    private StringBuffer adventurerFinalState = new StringBuffer(1024);

    private Dimension minSize = new Dimension(440, 440);
    private Adventurer agent;

    public static void main(String[] args) {
        logger.info("main: Started.");

        Adventurer myImortalAdventurer = new Adventurer();


        //agent = new Adventurer();
        for (int l = 0; l < 6; l++) {
            WWSimulator wwSimulator = new WWSimulator();
            wwSimulator.agent = myImortalAdventurer;
            wwSimulator.agent.setHealth(100);

            wwSimulator.loadProperties();
            wwSimulator.initialiseWWSimulatorVariables();
            gameState = new TheWorld(applicationProps);
            gameState.initAdventurer();

            experimentalData = new LogExperiment(applicationProps);
            wwSimulator.run();
            experimentalData.logData(wwSimulator);
        }


        //wwSimulator.saveProperties();
        experimentalData.logParams(applicationProps);
        // experimentalData.logData(wwSimulator);
        logger.info("main: Finished.");
    }

    private void run() {

        JFrame frame = new JFrame("Wumpus World Simulator");
        frame.setMinimumSize(minSize);
        frame.setSize(defaultWidth, defaultHeight);
        GridPanel gridPanel = new GridPanel(4);

        Sprite adventurer = new Sprite(TheWorld.ADVENTURER, gameState, gridPanel);

        this.agent.setSprite(adventurer);
        this.agent.setTheWorld(gameState);
        gridPanel.setAdventurer(adventurer);

        Sprite wumpus = new Sprite(TheWorld.WUMPUS, gameState, gridPanel);
        gridPanel.setWumpus(wumpus);


        Sprite pits = new Sprite(TheWorld.PITS, gameState, gridPanel);
        gridPanel.setPits(pits);


        Sprite gold = new Sprite(TheWorld.GOLD, gameState, gridPanel);
        gridPanel.setGold(gold);


        Sprite walls = new Sprite(TheWorld.WALLS, gameState, gridPanel);
        gridPanel.setWalls(walls);

        frame.setVisible(true);
        frame.add(gridPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addComponentListener(new GuiEventListener());

        // Simulation Loop
        boolean runSim = true;
        CoOrdinate wumpusXY = gameState.getEntityLocation(TheWorld.WUMPUS);
        CoOrdinate pitXY = gameState.getEntityLocation(TheWorld.PITS);
        CoOrdinate wallXY = gameState.getEntityLocation(TheWorld.WALLS);
        CoOrdinate goldXY = gameState.getEntityLocation(TheWorld.GOLD);
        CoOrdinate adventurerXY = gameState.getEntityLocation(TheWorld.ADVENTURER);
        this.adventurerFinalState.append(adventurerXY.toCSV() + ",");
        this.adventurerFinalState.append(goldXY.toCSV() + ",");
        //CoOrdinate adventurersPrevXY = new CoOrdinate();
        //adventurersPrevXY.row = adventurerXY.row;
        //adventurersPrevXY.col = adventurerXY.col;

        int reward = -1;

        while (runSim) {
            logger.debug("Explored " + gameState.getCountVisited() + " grid(s)");
            timeSteps = timeSteps + 1;
            logger.info("timestep " + timeSteps);
            logger.debug("Agents health is " + agent.getHealth());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Store current game state so we can learn from the action we are taking next.

            prevGameState = TheWorld.cloneStateArray(gameState);

            // What action should we take ?
            int action = agent.think(gameState);


            gameState = agent.act(gameState, action);
            CoOrdinate agentXY = gameState.getEntityLocation(TheWorld.ADVENTURER);
            logger.debug("Agents location is "+ agentXY.toCSV());

            reward = -1;
            if (agentXY.collision(pitXY)) {
                // Falling .... !
                logger.info("Ahhhh falling !");
                reward = reward - 100;
                this.adventurerFinalState.append("PIT,");
                runSim = false;
            }
            if (agentXY.collision(wumpusXY)) {
                // Fighting .... !
                logger.info("The Wumpus !");
                reward = reward - 100;
                this.adventurerFinalState.append("WUMPUS,");
                runSim = false;
            }
            if (agentXY.collision(goldXY)) {
                // Rich !
                logger.info("Gold ! I'm rich !");
                reward = reward + 100;
                this.adventurerFinalState.append("GOLD,");
                runSim = false;

            }
            if (agent.getHealth() <= 0) {
                this.adventurerFinalState.append("STARVATION,");
                reward = reward - 100;
                runSim = false;
            }
            if (agentXY.collision(wallXY)) {
                // Ouch !
                logger.info("Ouch a wall !");
                reward = reward - 10;
                //move them back to whence they came.
                //gameState.moveEntityTo(TheWorld.ADVENTURER, adventurersPrevXY.row, adventurersPrevXY.col);
                gameState = prevGameState;

            }

            agent.setHealth(agent.getHealth() + reward);
            this.agent.setTheWorld(gameState);
            frame.repaint();


            agent.learn(prevGameState, action, reward);

           // agent.brain.brainDump();

            if (!runSim) {
                logger.info("Final score " + agent.getHealth());
            } else {
                action = agent.think(gameState);
                //adventurersPrevXY.row = adventurerXY.row;
                //adventurersPrevXY.col = adventurerXY.col;
                //gameState = agent.act(gameState, action);
            }
        }
        this.adventurerFinalState.append(Integer.toString(agent.getHealth()));
        this.adventurerFinalState.append(",");
        this.adventurerFinalState.append(timeSteps);
        frame.dispose();
        System.gc();


    }

    /**
     * logs the data from this experimental run.
     */
    public String getLogData() {
        return this.adventurerFinalState.toString().trim();
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
            temp = Integer.parseInt(applicationProps.getProperty("defaultMargin"));
            if (temp > 0) ;
        } catch (Exception e) {
            logger.warn("No defaultMargin defined in either properties file.");
            logger.warn(e.getMessage());
        }

        try {
            temp = 0; // reset temp as a precaution.
            temp = Integer.parseInt(applicationProps.getProperty("defaultHeight"));
            if (temp > 0) defaultHeight = temp;
        } catch (Exception e) {
            logger.warn("No defaultHeight defined in either properties file.");
            logger.warn(e.getMessage());
        }

        defaultWidth = Integer.parseInt(applicationProps.getProperty("defaultWidth"));
        try {
            temp = 0; // reset temp as a precaution.
            temp = Integer.parseInt(applicationProps.getProperty("defaultWidth"));
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
        gameState.initEntity(TheWorld.ADVENTURER, TheWorld.FIXED_START, 0, 0);
        gameState.initEntity(TheWorld.WUMPUS, TheWorld.RANDOM_START, 0, 0);
        gameState.initEntity(TheWorld.PITS, TheWorld.RANDOM_START, 0, 0);
        gameState.initEntity(TheWorld.GOLD, TheWorld.RANDOM_START, 0, 0);
        gameState.initEntity(TheWorld.WALLS, TheWorld.RANDOM_START, 0, 0);

        this.saveDefaultProperties();
        this.loadProperties();
    }
}
