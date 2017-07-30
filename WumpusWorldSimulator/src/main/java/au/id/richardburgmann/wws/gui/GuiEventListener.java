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

package au.id.richardburgmann.wws.gui;

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
