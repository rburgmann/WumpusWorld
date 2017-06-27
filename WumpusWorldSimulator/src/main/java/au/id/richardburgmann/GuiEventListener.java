package au.id.richardburgmann;
/**
 * Created by Richard Burgmann on 10/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.*;

/**
 * This is the generic event listening class for all the Wumpus World Simulator App interactions.
 *
 */
public class GuiEventListener implements MouseListener, WindowStateListener, ComponentListener {
    private static final Logger logger = LoggerFactory.getLogger(GuiEventListener.class);



    public void mouseClicked(MouseEvent e) {
       // logger.info(e.getClass().getCanonicalName());

    }

    public void mousePressed(MouseEvent e) {
       // logger.info(e.getClass().getCanonicalName());

    }

    public void mouseReleased(MouseEvent e) {



    }

    public void mouseEntered(MouseEvent e) {
       // logger.info(e.getClass().getCanonicalName());

    }

    public void mouseExited(MouseEvent e) {
        //logger.info(e.getClass().getCanonicalName());


    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        logger.info(e.toString());
    }

    @Override
    public void componentResized(ComponentEvent e) {
        JFrame w = (JFrame) e.getSource();
        int width = (int)  w.getBounds().getWidth();
        int height = (int)  w.getBounds().getHeight();
        //logger.info("Width: " + String.valueOf(width));
        //logger.info("Height: " + String.valueOf(height));
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
