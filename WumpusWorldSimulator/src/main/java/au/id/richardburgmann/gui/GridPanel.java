package au.id.richardburgmann.gui;
/**
 * Created by Richard Burgmann on 10/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import au.id.richardburgmann.TheWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static au.id.richardburgmann.TheWorld.GRID_SIZE;

/**
 * GridPanel is the canvas that Wumpus World is played on.
 * It dynamically adjusts the size of the grid to the available space
 * and draws the entities of Wumpus world on the grid.
 */

public class GridPanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(GridPanel.class);
    public int defaultMargin = 20;
    public int cellHeight = 100;
    public int cellWidth = 100;
<<<<<<< HEAD:WumpusWorldSimulator/src/main/java/au/id/richardburgmann/gui/GridPanel.java
    private int gridSize = 4; // gridSize by gridSize, n row n.
    private ArrayList<Sprite> mySprites = new ArrayList<Sprite>(5);
=======
    private int gridSize = GRID_SIZE; // gridSize by gridSize, n row n.
    private Sprite adventurer;
    private Sprite wumpus;
    private Sprite pits;
    private Sprite gold;
    private Sprite walls;
>>>>>>> origin/master:WumpusWorldSimulator/src/main/java/au/id/richardburgmann/GridPanel.java

    public GridPanel() {
        this.gridSize = TheWorld.GRID_SIZE;
    }

    public GridPanel(int gridSize) {
        this.gridSize = gridSize;
    }

    public void addSprite(Sprite sprite) {
        mySprites.add(sprite);
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.black);

        this.paintGrid(graphics);
<<<<<<< HEAD:WumpusWorldSimulator/src/main/java/au/id/richardburgmann/gui/GridPanel.java
=======
        this.walls.paint(graphics);
        this.gold.paint(graphics);
        this.wumpus.paint(graphics);
        this.pits.paint(graphics);
        this.adventurer.paint(graphics);

>>>>>>> origin/master:WumpusWorldSimulator/src/main/java/au/id/richardburgmann/GridPanel.java

        for (Sprite aSprite : mySprites){
            aSprite.paint(graphics);
        }
    }

    public void paintGrid(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        int h = this.getHeight();
        int w = this.getWidth();
        cellHeight = (h - (2 * defaultMargin)) / this.gridSize;
        cellWidth = (w - (2 * defaultMargin)) / this.gridSize;

        // Draw Rows
        // Row 0


        int x1 = defaultMargin + (0 * cellWidth);
        int y1 = defaultMargin + (0 * cellHeight);
        int x2 = defaultMargin + (4 * cellWidth);
        int y2 = defaultMargin + (0 * cellHeight);


        graphics2D.setStroke(new BasicStroke((5)));
        graphics2D.drawLine(x1, y1, x2, y2);

//        // Row 1

        x1 = defaultMargin + (0 * cellWidth);
        y1 = defaultMargin + (1 * cellHeight);
        x2 = defaultMargin + (4 * cellWidth);
        y2 = defaultMargin + (1 * cellHeight);
        graphics2D.drawLine(x1, y1, x2, y2);
//
//        // Row 2
        x1 = defaultMargin + (0 * cellWidth);
        y1 = defaultMargin + (2 * cellHeight);
        x2 = defaultMargin + (4 * cellWidth);
        y2 = defaultMargin + (2 * cellHeight);
        graphics2D.drawLine(x1, y1, x2, y2);

        // Row 3
        x1 = defaultMargin + (0 * cellWidth);
        y1 = defaultMargin + (3 * cellHeight);
        x2 = defaultMargin + (4 * cellWidth);
        y2 = defaultMargin + (3 * cellHeight);
        graphics2D.drawLine(x1, y1, x2, y2);

        // Row 4
        x1 = defaultMargin + (0 * cellWidth);
        y1 = defaultMargin + (4 * cellHeight);
        x2 = defaultMargin + (4 * cellWidth);
        y2 = defaultMargin + (4 * cellHeight);
        graphics2D.drawLine(x1, y1, x2, y2);

        // Do Columns

        // Column 0

        x1 = defaultMargin + (0 * cellWidth);
        y1 = defaultMargin + (0 * cellHeight);
        x2 = defaultMargin + (0 * cellWidth);
        y2 = defaultMargin + (4 * cellHeight);

        graphics2D.drawLine(x1, y1, x2, y2);

        // Column 1

        x1 = defaultMargin + (1 * cellWidth);
        y1 = defaultMargin + (0 * cellHeight);
        x2 = defaultMargin + (1 * cellWidth);
        y2 = defaultMargin + (4 * cellHeight);
        graphics2D.drawLine(x1, y1, x2, y2);

        // Column 2

        x1 = defaultMargin + (2 * cellWidth);
        y1 = defaultMargin + (0 * cellHeight);
        x2 = defaultMargin + (2 * cellWidth);
        y2 = defaultMargin + (4 * cellHeight);
        graphics2D.drawLine(x1, y1, x2, y2);

        // Column 3

        x1 = defaultMargin + (3 * cellWidth);
        y1 = defaultMargin + (0 * cellHeight);
        x2 = defaultMargin + (3 * cellWidth);
        y2 = defaultMargin + (4 * cellHeight);
        graphics2D.drawLine(x1, y1, x2, y2);

        // Column 4

        x1 = defaultMargin + (4 * cellWidth);
        y1 = defaultMargin + (0 * cellHeight);
        x2 = defaultMargin + (4 * cellWidth);
        y2 = defaultMargin + (4 * cellHeight);
        graphics2D.drawLine(x1, y1, x2, y2);

    }

}
