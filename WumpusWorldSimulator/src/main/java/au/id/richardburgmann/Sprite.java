package au.id.richardburgmann;
/**
 * Created by Richard Burgmann on 12/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * The sprite class encapsulates all the common logic for displaying the things in the world.
 */
public class Sprite extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(Sprite.class);
    private int entity = -1; // -1 means it is undefined.
    private TheWorld myWorld;
    private GridPanel myPanel;

    /**
     * A sprite needs to know what entity in the grid world it is representing.
     *
     * @param entity    Which entity of the world are we the sprite for?
     * @param theWorld  The world state object so we know where in the world we are.
     * @param gridPanel The containing panel in which we will render the entity.
     */
    public Sprite(int entity, TheWorld theWorld, GridPanel gridPanel) {
        /**
         * First ensure the constructor has all the information it needs. Terminate now if we have
         * any null pointers or other fatal rubbish in the constructor.
         */
        if (entity >= 0 && entity <= 9) {
            this.entity = entity;
        } else {
            logger.error("Fatal Error: Unknown entity in method call, value " + entity);
            System.exit(1);
        }
        this.setGridState(theWorld);

        if (gridPanel != null) {
            this.myPanel = gridPanel;
        } else {
            logger.error("Fatal Error: GridPanel object undefined, null pointer in method call");
            System.exit(1);
        }
        //logger.info("Sprite created for entity " + this.entity);
    }
    public void setGridState(TheWorld currentState) {
        if (currentState != null) {
            this.myWorld = currentState;
        } else {
            logger.error("Fatal Error: World state object undefined, null pointer in method call");
            System.exit(1);
        }
    }
    /**
     * Draw me in the correct location
     */
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.BLACK);
        /*switch (this.entity) {
            case TheWorld.ADVENTURER: renderAdventurer(graphics);
                break;
            case TheWorld.WUMPUS: renderWumpus(graphics);
                renderStenches(graphics);
                break;
        }*/
        switch (this.entity) {
            case TheWorld.ADVENTURER:
                renderEntity(this.entity, graphics, 1, Color.BLUE);
                break;
            case TheWorld.WUMPUS:
                renderEntity(this.entity, graphics, 1, Color.RED);
                renderEntity(TheWorld.STENCHES, graphics, 0, Color.RED);
                break;
            case TheWorld.PITS:
                renderEntity(this.entity, graphics, 1, Color.BLACK);
                renderEntity(TheWorld.BREEZES, graphics, 0, Color.BLACK);
                break;
            case TheWorld.GOLD:
                renderEntity(this.entity, graphics, 1, Color.YELLOW);
                renderEntity(TheWorld.GLITTER, graphics, 0, Color.YELLOW);
                break;
            case TheWorld.WALLS:
                renderEntity(this.entity, graphics, 1, Color.darkGray);
        }

    }

    /**
     * Render anything.
     */
    public void renderEntity(int entity, Graphics graphics, int fillordraw, Color color) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        // Search through the world and find where we are located.
        graphics2D.setColor(color);
        int width = myPanel.cellWidth - fillordraw - entity;
        int height = myPanel.cellHeight - fillordraw - entity;
        for (int row = 0; row < TheWorld.GRID_SIZE; row++) {
            for (int col = 0; col < TheWorld.GRID_SIZE; col++) {
                if (this.myWorld.worldState[entity][row][col] == TheWorld.OCCUPIED_LOCATION) {

                    int xCenter = myPanel.defaultMargin + (col * myPanel.cellWidth) + fillordraw + entity;
                    int yCenter = myPanel.defaultMargin + (row * myPanel.cellHeight) + fillordraw + entity;
                    //int xCenter = myPanel.defaultMargin + (col * width);
                    //int yCenter = myPanel.defaultMargin + (row * height);
                    if (fillordraw == 1) {

                        graphics2D.fillOval(xCenter, yCenter, width, height);
                    } else {
                        graphics2D.drawOval(xCenter, yCenter, width, height);
                    }
                }
            }
        }

    }

    /**
     * Render the Adventurer.
     */
    public void renderAdventurer(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        // Search through the world and find where we are located.
        graphics2D.setColor(Color.BLUE);
        for (int row = 0; row < TheWorld.GRID_SIZE; row++) {
            for (int col = 0; col < TheWorld.GRID_SIZE; col++) {
                if (this.myWorld.worldState[this.entity][row][col] == TheWorld.OCCUPIED_LOCATION) {
                    // render the Adventurer.
                    //int xCenter = myPanel.defaultMargin ;
                    int xCenter = myPanel.defaultMargin + (col * myPanel.cellWidth);
                    int yCenter = myPanel.defaultMargin + (row * myPanel.cellHeight);


                    graphics2D.fillOval(xCenter, yCenter, myPanel.cellWidth, myPanel.cellHeight);
                }
            }
        }

    }

    public void renderWumpus(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.RED);
        // Search through the world and find where we are located.
        for (int row = 0; row < TheWorld.GRID_SIZE; row++) {
            for (int col = 0; col < TheWorld.GRID_SIZE; col++) {
                if (this.myWorld.worldState[this.entity][row][col] == TheWorld.OCCUPIED_LOCATION) {
                    // render the Wumpus.
                    //int xCenter = myPanel.defaultMargin ;
                    int xCenter = myPanel.defaultMargin + (col * myPanel.cellWidth);
                    int yCenter = myPanel.defaultMargin + (row * myPanel.cellHeight);


                    graphics2D.fillOval(xCenter, yCenter, myPanel.cellWidth, myPanel.cellHeight);
                }
            }
        }

    }

    public void renderStenches(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.RED);
        // Search through the world and find where we are located.
        for (int row = 0; row < TheWorld.GRID_SIZE; row++) {
            for (int col = 0; col < TheWorld.GRID_SIZE; col++) {
                if (this.myWorld.worldState[this.myWorld.STENCHES][row][col] == TheWorld.OCCUPIED_LOCATION) {
                    // render the Wumpus.
                    //int xCenter = myPanel.defaultMargin ;
                    int xCenter = myPanel.defaultMargin + (col * myPanel.cellWidth);
                    int yCenter = myPanel.defaultMargin + (row * myPanel.cellHeight);


                    graphics2D.drawOval(xCenter, yCenter, myPanel.cellWidth, myPanel.cellHeight);
                }
            }
        }
    }

    public void renderPits(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.BLACK);
        // Search through the world and find where we are located.
        for (int row = 0; row < TheWorld.GRID_SIZE; row++) {
            for (int col = 0; col < TheWorld.GRID_SIZE; col++) {
                if (this.myWorld.worldState[this.myWorld.PITS][row][col] == TheWorld.OCCUPIED_LOCATION) {
                    // render the Wumpus.
                    //int xCenter = myPanel.defaultMargin ;
                    int xCenter = myPanel.defaultMargin + (col * myPanel.cellWidth);
                    int yCenter = myPanel.defaultMargin + (row * myPanel.cellHeight);


                    graphics2D.fillOval(xCenter, yCenter, myPanel.cellWidth, myPanel.cellHeight);
                }
            }
        }
    }

    public void renderBreezes(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.BLACK);
        // Search through the world and find where we are located.
        for (int row = 0; row < TheWorld.GRID_SIZE; row++) {
            for (int col = 0; col < TheWorld.GRID_SIZE; col++) {
                if (this.myWorld.worldState[this.myWorld.BREEZES][row][col] == TheWorld.OCCUPIED_LOCATION) {
                    // render the Wumpus.
                    //int xCenter = myPanel.defaultMargin ;
                    int xCenter = myPanel.defaultMargin + (col * myPanel.cellWidth);
                    int yCenter = myPanel.defaultMargin + (row * myPanel.cellHeight);


                    graphics2D.drawOval(xCenter, yCenter, myPanel.cellWidth, myPanel.cellHeight);
                }
            }
        }
    }
}
