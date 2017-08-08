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


import au.id.richardburgmann.wws.TheWorld;
import au.id.richardburgmann.wws.WWSimulator;
import au.id.richardburgmann.wws.brains.QTableBrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * QStateViewer displays the learnt weights (the q values) as the Adventurer learns them.
 */

public class QStateViewer {
    private static final Logger logger = LoggerFactory.getLogger(QStateViewer.class);
    private static final int NUMBER_OF_PANES = 16;
    ArrayList<QTableBrain.BrainCell> learntQValues = new ArrayList<QTableBrain.BrainCell>(16);
    JPanel[] panes = new JPanel[NUMBER_OF_PANES];
    private JFrame myWindow;
    private ArrayList<QTableBrain.BrainCell> brainState;

    private Font myFont = new Font(Font.SANS_SERIF, Font.PLAIN, 28);
    private BorderLayout borderLayout = new BorderLayout(1, 1);
    private GridLayout gridLayout = new GridLayout(4, 4, 1, 1);

    public QStateViewer() {
        logger.trace("QStateViewer Constructor.");
        createWindow();
        initPanes();
    }

    public static void main(String[] args) {
        logger.trace("Test QPanel");
        QStateViewer viewer = new QStateViewer();
    }

    private void createWindow() {
        myWindow = new JFrame();
        myWindow.createBufferStrategy(1);
        int defaultHeight = 1200;
        int defaultWidth = 1200;
        try {
            defaultHeight = Integer.parseInt(WWSimulator.applicationProps.getProperty("defaultHeight"));
            defaultWidth = Integer.parseInt(WWSimulator.applicationProps.getProperty("defaultWidth"));
        } catch (Exception e) {
            logger.debug("Error parsing default width and height from properties object.");
            logger.debug(e.getMessage());
        }

        myWindow.setSize(defaultWidth, defaultHeight);
        myWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        myWindow.setLayout(gridLayout);
        myWindow.setFont(myFont);
        myWindow.setLocation(1200, 0);
        myWindow.setTitle("Q State Viewer");
        myWindow.setVisible(true);
    }

    private void initPanes() {
        for (int i = 0; i < NUMBER_OF_PANES; i++) {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(borderLayout);
            panes[i] = jPanel;
            myWindow.add(panes[i]);
        }
        for (int i = 0; i < NUMBER_OF_PANES; i++) {
            boolean useDefaults = true;
            double[] weights = new double[4];
            JTextField[] myTextFields = createTextFields(useDefaults, weights);
            resetJPanel(panes[i], myTextFields);
            myWindow.setVisible(true);
        }
        myWindow.setVisible(true);
    }

    public void setBrainState(ArrayList<QTableBrain.BrainCell> brainState) {
        if (brainState != null) {
            this.brainState = brainState;
            this.updateView();
        }
    }

    public void dispose() {
        myWindow.dispose();
    }

    public void updateView() {
        logger.trace("updateView");

        for (int i = 0; i < brainState.size(); i++) {
            // Get the specific q value weights and x,y location.
            QTableBrain.BrainCell aCell = brainState.get(i);
            int paneIndex = (aCell.cell.row * 4) + aCell.cell.col;
            double[] weights = aCell.weights;
            boolean useDefaults = false;
            JTextField[] jTextFields = createTextFields(useDefaults, weights);
            adjustBackgroundColourDependingOnQValues(jTextFields, weights);
            resetJPanel(panes[paneIndex], jTextFields);
            myWindow.validate();
            myWindow.setVisible(true);
        }
        for (int i = 0; i < NUMBER_OF_PANES; i++) {
            panes[i].repaint();
        }
        myWindow.setVisible(true);
    }

    public JTextField[] createTextFields(boolean useDefaults, double[] weights) {
        JTextField[] actionQValueTextField = new JTextField[TheWorld.GRID_SIZE];
        for (int i = 0; i < TheWorld.GRID_SIZE; i++) {
            actionQValueTextField[i] = new JTextField();
            actionQValueTextField[i].setFont(myFont);
            actionQValueTextField[i].setHorizontalAlignment(JTextField.CENTER);
            if (useDefaults) {
                actionQValueTextField[i].setText("???.???");
            } else {
                actionQValueTextField[i].setText(String.format("%1$06.3f", weights[i]));
            }
        }
        return actionQValueTextField;
    }

    public void resetJPanel(JPanel storedJPanel, JTextField[] textFields) {
        storedJPanel.removeAll();
        storedJPanel.setLayout(borderLayout);
        storedJPanel.add(textFields[0], BorderLayout.WEST);
        storedJPanel.add(textFields[1], BorderLayout.EAST);
        storedJPanel.add(textFields[2], BorderLayout.NORTH);
        storedJPanel.add(textFields[3], BorderLayout.SOUTH);
        storedJPanel.validate();
    }

    public void adjustBackgroundColourDependingOnQValues(JTextField[] actionQValueTextField, double[] weights) {

        double[] w = rescale(weights);

        double MaxQValue = getMaxQValue(w);
        double MinQValue = getMinQValue(w);


        for (int i = 0; i < w.length; i++) {
            actionQValueTextField[i].setVisible(false);
            actionQValueTextField[i].setBackground(Color.ORANGE);


            if (w[i] >= MaxQValue) {

                actionQValueTextField[i].setBackground(Color.GREEN);

            } else if (w[i] <= MinQValue) {

                actionQValueTextField[i].setBackground(Color.PINK);

            }
            if (weights[i] == -1000) actionQValueTextField[i].setBackground(Color.LIGHT_GRAY);
            if (weights[i] == -102) actionQValueTextField[i].setBackground(Color.PINK);
            actionQValueTextField[i].setVisible(true);
        }

    }

    private double[] rescale(double[] weights) {

        double adjustScale = ((getMaxQValue(weights) - getMinQValue(weights)) / 2);
        double[] rw = new double[weights.length];

        for (int i = 0; i < weights.length; i++) {
            rw[i] = weights[i] + adjustScale;
        }
        return rw;
    }


    private double getMaxQValue(double[] weights) {
        double maxSoFar = Double.MIN_VALUE;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] > maxSoFar) {
                maxSoFar = weights[i];
            }
        }
        return maxSoFar;
    }

    private double getMinQValue(double[] weights) {
        double minSoFar = Double.MAX_VALUE;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] < minSoFar) {
                minSoFar = weights[i];
            }
        }
        return minSoFar;
    }

    public double[] getRandomDoubleArray() {
        double[] doubles = new double[4];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = Math.random() * 100.00;
        }
        return doubles;
    }
}
