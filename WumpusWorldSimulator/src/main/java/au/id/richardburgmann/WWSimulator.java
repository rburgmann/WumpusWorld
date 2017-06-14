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
    private int defaultWidth = 1200;
    private int defaultHeight = 1200;
    private int defaultMargin = 20;
    private Dimension minSize = new Dimension(440, 440);
    /**
     * gameState holds the state of the game. Events update it and it is used by the render engine.
     */
    private TheWorld gameState = new TheWorld();
    private Adventurer agent;

    public static void main(String[] args) {
        WWSimulator wwSimulator = new WWSimulator();

           if (args.length > 0) {
              int runNtimes = parseInt(args[0]) ;
              wwSimulator.batchRun(runNtimes);
           } else {

               logger.info("main: Started.");
               wwSimulator.run();
               logger.info("main: Finished.");
           }
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

        this.gameState.setRandomStartAdventurer(TheWorld.RANDOM_START);

        //this.gameState.initEntity(TheWorld.ADVENTURER,TheWorld.RANDOM_START);
        this.gameState.initAdventurer();
        Sprite adventurer = new Sprite(gameState.ADVENTURER, gameState, gridPanel);
        agent = new Adventurer();
        agent.setSprite(adventurer);
        agent.setTheWorld(gameState);
        gridPanel.setAdventurer(adventurer);


        this.gameState.initEntity(TheWorld.WUMPUS,TheWorld.RANDOM_START);
        Sprite wumpus = new Sprite(gameState.WUMPUS, gameState, gridPanel);
        gridPanel.setWumpus(wumpus);

        this.gameState.initEntity(TheWorld.PITS,TheWorld.RANDOM_START);
        Sprite pits = new Sprite(gameState.PITS, gameState, gridPanel);
        gridPanel.setPits(pits);

        this.gameState.initEntity(TheWorld.GOLD,TheWorld.RANDOM_START);
        Sprite gold = new Sprite(gameState.GOLD, gameState, gridPanel);
        gridPanel.setGold(gold);

        this.gameState.initEntity(TheWorld.WALLS,TheWorld.RANDOM_START);
        Sprite walls = new Sprite(gameState.WALLS, gameState, gridPanel);
        gridPanel.setWalls(walls);

        //this.gameState.setRandomStartWumpus(TheWorld.RANDOM_START);
        //this.gameState.setRandomStartGold(TheWorld.RANDOM_START);
        //this.gameState.setRandomStartPits(TheWorld.RANDOM_START);

        //this.gameState.initWumpus();
        //this.gameState.initEntity(TheWorld.GOLD,TheWorld.RANDOM_START);
        //this.gameState.initEntity(TheWorld.PITS,TheWorld.RANDOM_START);

        //Sprite wumpus = new Sprite(gameState.WUMPUS, gameState, gridPanel);
        //Sprite pit = new Sprite(gameState.PITS, gameState, gridPanel);
        //Sprite gold = new Sprite(gameState.GOLD, gameState, gridPanel);

        //gridPanel.setWumpus(wumpus);
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


            if(agent.myX == wallXY.x && agent.myY == wallXY.y) {
                // Ouch !
                logger.info("Ouch a wall !");
                agent.setHealth(agent.getHealth() - 10);
                // move them back to whence they came.
                gameState.initEntity(TheWorld.ADVENTURER, TheWorld.FIXED_START, adventurersPrevXY.x,
                        adventurersPrevXY.y);

            }
            if(agent.myX == pitXY.x && agent.myY == pitXY.y) {
                // Falling .... !
                logger.info("Ahhhh falling !");
                agent.setHealth(agent.getHealth() - 100);
                runSim = false;
            }
            if(agent.myX == wumpusXY.x && agent.myY == wumpusXY.y) {
                // Fighting .... !
                logger.info("The Wumpus !");
                agent.setHealth(agent.getHealth() - 100);
                runSim = false;
            }
            if(agent.myX == goldXY.x && agent.myY == goldXY.y) {
                // Rich !
                logger.info("Gold ! I'm rich !");
                agent.setHealth(agent.getHealth() + 100);
                runSim = false;
            }

            if(agent.getHealth() <= 0) {
                runSim = false;

            }
            if (!runSim) {
                logger.info("Final score " + agent.getHealth());
            }
        }
        frame.dispose();
        System.gc();

    }

}
