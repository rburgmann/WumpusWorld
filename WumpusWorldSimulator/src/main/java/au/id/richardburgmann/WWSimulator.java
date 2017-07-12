package au.id.richardburgmann;
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

import au.id.richardburgmann.gui.GridPanel;
import au.id.richardburgmann.gui.GuiEventListener;
import au.id.richardburgmann.gui.MainWindow;
import au.id.richardburgmann.gui.Sprite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
<<<<<<< HEAD
import java.awt.event.WindowAdapter;
=======
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
>>>>>>> origin/master
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
<<<<<<< HEAD
    private TheWorld gameState;
    private static WWSimulator wwSimulator;
=======
    private static TheWorld gameState;
    private Font myTitleFont = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    private Font myMenuFont = new Font(Font.SANS_SERIF, Font.PLAIN, 28);
>>>>>>> origin/master
    private TheWorld prevGameState;
    private int timeSteps = 0;
<<<<<<< HEAD
    private MainWindow mainWindow;
    private GridPanel  gridPanel;
    private StringBuffer adventurerFinalState = new StringBuffer(1024);

    private Adventurer adventurer = new Adventurer();

    // Sprites are the guis of the things in the world.
    Sprite adventurerSprite;
    Sprite wumpusSprite;
    Sprite pitsSprite;
    Sprite goldSprite;
    Sprite wallsSprite;

    public static void main(String[] args) {
=======
    private boolean startRun = false;
    WWSimulator wwSimulator;
    JFrame frame;
    private StringBuffer adventurerFinalState = new StringBuffer(1024);

    private Dimension minSize = new Dimension(440, 440);
    private Adventurer agent;

    public static void main(String[] args) {
        logger.info("main: Started.");

        WWSimulator wwSimulator = new WWSimulator();
        wwSimulator.runBatch(16);

        experimentalData.logParams(applicationProps);
        logger.info("main: Finished.");
    }
    private void runBatch(int batchSize) {
        Adventurer myImmortalAdventurer = new Adventurer();
        for (int l = 0; l < batchSize; l++) {
            logger.info("Loop " + l);
            wwSimulator = new WWSimulator();
            wwSimulator.agent = myImmortalAdventurer;
            wwSimulator.agent.setHealth(100);
>>>>>>> origin/master

        int batchSize = 1;
        if (args.length > 0) {
            batchSize = Integer.parseInt(args[0]);
        }
        wwSimulator = new WWSimulator();
        wwSimulator.runSimulations(batchSize);
    }
    public void runSimulations(int numberOfTimes) {
        logger.info("Run simulation "+ numberOfTimes + " time(s)." );
        wwSimulator.startup();
        for (int l = 0; l < numberOfTimes; l++) {

            wwSimulator.initSimulator();
            wwSimulator.run();
            wwSimulator.logRunResults();
        }
<<<<<<< HEAD
        wwSimulator.shutdown();
    }

    private void startup() {
        loadProperties();
        createExperimentLog();
    }
=======
    }
    private void run() {

        frame = new JFrame("Wumpus World Simulator");
        frame.setFont(this.myTitleFont);

        frame.setJMenuBar(this.createMyMenus());

        frame.setMinimumSize(minSize);
        frame.setSize(defaultWidth, defaultHeight);
        GridPanel gridPanel = new GridPanel(4);

        Sprite adventurer = new Sprite(TheWorld.ADVENTURER, gameState, gridPanel);

        this.agent.setSprite(adventurer);
        this.agent.setTheWorld(gameState);
        gridPanel.setAdventurer(adventurer);

        Sprite wumpus = new Sprite(TheWorld.WUMPUS, gameState, gridPanel);
        gridPanel.setWumpus(wumpus);
>>>>>>> origin/master

    private void shutdown() {
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
    /**
     * Load default properties from local file into properties object.
     */

    private void createMainWindow() {
        mainWindow = new MainWindow(this);
        mainWindow.assignProperties(applicationProps);
        mainWindow.addWindowListener(new windowCloseListener());
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.addComponentListener(new GuiEventListener());
        mainWindow.createBufferStrategy(1);

    }

<<<<<<< HEAD
    private void initGameState() {
        gameState = new TheWorld(applicationProps);
    }
    private void run() {
=======
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.isDoubleBuffered();
        frame.addComponentListener(new GuiEventListener());
>>>>>>> origin/master

        CoOrdinate wumpusXY = gameState.getEntityLocation(TheWorld.WUMPUS);
        CoOrdinate pitXY = gameState.getEntityLocation(TheWorld.PITS);
        CoOrdinate wallXY = gameState.getEntityLocation(TheWorld.WALLS);
        CoOrdinate goldXY = gameState.getEntityLocation(TheWorld.GOLD);
        CoOrdinate adventurerXY = gameState.getEntityLocation(TheWorld.ADVENTURER);
<<<<<<< HEAD

        ArrayList<CoOrdinate> stenchesXY = gameState.getPerceptions(TheWorld.STENCHES);
        ArrayList<CoOrdinate> breezesXY = gameState.getPerceptions(TheWorld.BREEZES);

        this.adventurerFinalState = new StringBuffer();
=======
        ArrayList<CoOrdinate> stenchesXY = gameState.getPerceptions(TheWorld.STENCHES);
        ArrayList<CoOrdinate> breezesXY = gameState.getPerceptions(TheWorld.BREEZES);
>>>>>>> origin/master
        this.adventurerFinalState.append(adventurerXY.toCSV() + ",");
        this.adventurerFinalState.append(goldXY.toCSV() + ",");

<<<<<<< HEAD
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int reward = -1;
        timeSteps = 0;
        adventurer.setHealth(100);
        boolean runSim = true;
        while (runSim) {

            timeSteps = timeSteps + 1;
            logger.info("timestep " + timeSteps + " Adventurers health is " + this.adventurer.getHealth());

=======
        int reward = -1;
        int extraDelayWhenGoalReached = 5000;
        while (!startRun) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (runSim ) {
            timeSteps = timeSteps + 1;
            logger.debug("Explored " + gameState.getCountVisited() + " grid(s) " +
                    " timestep " + timeSteps);
            logger.debug("Agents health is " + agent.getHealth());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Store current game state so we can learn from the action we are taking next.
>>>>>>> origin/master

            // Store current game state so we can learn from the action we are taking next.
            prevGameState = TheWorld.cloneStateArray(gameState);

            // What action should we take ?
            int action = this.adventurer.think(gameState);

            // Take the action we chose.
            gameState = this.adventurer.act(gameState, action);

            updateMainWindow(gameState);

            CoOrdinate agentXY = gameState.getEntityLocation(TheWorld.ADVENTURER);
<<<<<<< HEAD
=======
            logger.debug("Agents location is " + agentXY.toCSV());
>>>>>>> origin/master

            reward = -1;

            if (agentXY.collision(stenchesXY)) {
                // This doesn't smell good.
<<<<<<< HEAD
                logger.info(" ");
                logger.info("****************************");
                logger.info("*** What a foul Stench ! ***");
                logger.info("****************************");
                logger.info(" ");
                reward = reward - 1;
            }
            if (agentXY.collision(breezesXY)) {
                logger.info(" ");
                logger.info("****************************");
                logger.info("***  I feel a BREEZE !   ***");
                logger.info("****************************");
                logger.info(" ");
                reward = reward - 1;
            }
=======
                logger.info("****************************");
                logger.info("*** What a foul Stench ! ***");
                logger.info("****************************");
                reward = reward - 2;

            }
            if (agentXY.collision(breezesXY)) {
                // This doesn't smell good.
                logger.info("****************************");
                logger.info("***  I feel a BREEZE !   ***");
                logger.info("****************************");
                reward = reward - 2;

            }

>>>>>>> origin/master
            if (agentXY.collision(pitXY)) {
                // Falling .... !
                logger.info("*************************");
                logger.info("***  Ahhhh falling !  ***");
                logger.info("*************************");
                reward = reward - 100;
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
                this.adventurerFinalState.append("WUMPUS,");
                runSim = false;
<<<<<<< HEAD
=======


>>>>>>> origin/master
            }
            if (agentXY.collision(goldXY)) {
                // Rich !
                logger.info(" ");
                logger.info("*************************");
                logger.info("*** Gold ! I'm rich ! ***");
                logger.info("*************************");
<<<<<<< HEAD
                logger.info(" ");
                reward = reward + 101;
                this.adventurerFinalState.append("GOLD,");
                runSim = false;
            }
            if (this.adventurer.getHealth() <= 0) {
=======
                reward = reward + 101;
                this.adventurerFinalState.append("GOLD,");
                runSim = false;


            }

            if (agent.getHealth() <= 0) {
>>>>>>> origin/master
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
<<<<<<< HEAD
                gameState = prevGameState; //move them back to whence they came.
=======
                //move them back to whence they came.
                gameState = prevGameState;
>>>>>>> origin/master

            }
            this.adventurer.setHealth(this.adventurer.getHealth() + reward);

            updateMainWindow(gameState);

<<<<<<< HEAD
            this.adventurer.learn(prevGameState, action, reward);
=======

            agent.learn(prevGameState, action, reward);

            // agent.brain.brainDump();
>>>>>>> origin/master

            if (!runSim) {
                logger.info("Final score " + this.adventurer.getHealth());
            } else {
<<<<<<< HEAD
                action = this.adventurer.think(gameState);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
=======
                action = agent.think(gameState);

>>>>>>> origin/master
            }
        }
        this.adventurerFinalState.append(Integer.toString(this.adventurer.getHealth()));
        this.adventurerFinalState.append(",");
        this.adventurerFinalState.append(timeSteps);

        try {
<<<<<<< HEAD
            Thread.sleep(2000);
=======
            Thread.sleep(5000);
>>>>>>> origin/master
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mainWindow.dispose();
        System.gc();
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
    public class windowCloseListener extends WindowAdapter {

        public void windowClosingEvent() {
            wwSimulator.shutdown();

        }
    }


    private JMenuBar createMyMenus() {
        JMenuBar myMenuBar = new JMenuBar();
        myMenuBar.setBorderPainted(true);
        myMenuBar.setBorder(new BorderUIResource.LineBorderUIResource(Color.BLACK));
        myMenuBar.setMargin(new Insets(20, 20, 20, 20));

        myMenuBar.setFont(myMenuFont);


        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.setFont(myMenuFont);
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setFont(myMenuFont);
        openMenuItem.addActionListener(new openMenuItemListener());

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setFont(myMenuFont);
        saveMenuItem.addActionListener(new saveMenuItemListener());

        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.setFont(myMenuFont);
        saveAsMenuItem.addActionListener(new saveAsMenuItemListener());

        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.setFont(myMenuFont);
        closeMenuItem.addActionListener(new closeMenuItemListener());

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setFont(myMenuFont);
        exitMenuItem.addActionListener(new exitMenuItemListener());

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(closeMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        JMenuItem editMenuItem = new JMenu("Edit");
        editMenuItem.setMnemonic(KeyEvent.VK_E);
        editMenuItem.setFont(myMenuFont);
        editMenuItem.addActionListener(new editMenuItemListener());

        JMenu runMenu = new JMenu("Run");
        runMenu.setMnemonic(KeyEvent.VK_R);
        runMenu.setFont(myMenuFont);
        runMenu.addMenuListener((MenuListener) new runMenuItemListener());

        myMenuBar.add(fileMenu);
        myMenuBar.add(runMenu);

        return myMenuBar;
    }

    /*
        Action Listener inner classes.
     */
    public class openMenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            logger.debug("openMenuItem ActionEvent " + e.getActionCommand().toString());

        }
    }

    public class saveMenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            logger.debug("saveMenuItem ActionEvent " + e.getActionCommand().toString());

        }
    }

    public class saveAsMenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            logger.debug("saveAsMenuItem ActionEvent " + e.getActionCommand().toString());

        }
    }
    public class closeMenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            logger.debug("closeMenuItem ActionEvent " + e.getActionCommand().toString());



        }
    }
    public class exitMenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            logger.debug("exitMenuItem ActionEvent " + e.getActionCommand().toString());
            try {
                frame.dispose();
                wwSimulator = null;
                System.gc();
                System.exit(0);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }


        }
    }
    public class editMenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            logger.debug("editMenuItem ActionEvent " + e.getActionCommand().toString());
            startRun = true;

        }
    }
    public class runMenuItemListener implements MenuListener {

        public void actionPerformed() {

        }

        @Override
        public void menuSelected(MenuEvent e) {
            logger.debug("runMenu MenuEvent Selected");
            if (startRun) {
                startRun = false;
            } else {
                startRun = true;
            }

        }

        @Override
        public void menuDeselected(MenuEvent e) {
            logger.debug("runMenu MenuEvent Deselected");

        }

        @Override
        public void menuCanceled(MenuEvent e) {
            logger.debug("runMenu MenuEvent Canceled");

        }
    }

}
