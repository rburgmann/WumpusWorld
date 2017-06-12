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

/**
 * <p>The Wumpus World Simulator enables you to explore the world of the Wumpus and Adventurer with a GUI
 * representation of the world. A number of experiments are planned using reinforcement learning for
 * the Adventurer. </p>
 * <p>This is a learning project that I'm undertaking just for fun. You can follow my own adventure
 * during the project at https://github.com/rburgmann/WumpusWorld</p>
 */
public class WWSimulator {
    private static final Logger logger = LoggerFactory.getLogger(WWSimulator.class);
    private int defaultWidth = 900;
    private int defaultHeight = 1200;
    private int defaultMargin = 20;
    private Dimension minSize = new Dimension(440, 440);

    public static void main(String[] args) {
           WWSimulator wwSimulator = new WWSimulator();
           logger.info("main: Started.");
           wwSimulator.run();
           logger.info("main: Finished.");
    }
    public void run() {

        JFrame frame = new JFrame("Wumpus World Simulator");
        frame.setMinimumSize(minSize);
        frame.setSize(defaultWidth, defaultHeight);
        GridPanel gridPanel = new GridPanel(4);
        frame.setVisible(true);
        frame.add(gridPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addComponentListener(new GuiEventListener());

    }

}
