package au.id.richardburgmann.gui;
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


import au.id.richardburgmann.TheWorld;
import au.id.richardburgmann.brains.QTableBrain;
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

    //private double[] actionQValues = new double[4];
    //private JTextField[] actionQValueTextField = new JTextField[4];
    private Font myFont = new Font(Font.SANS_SERIF, Font.PLAIN, 28);
    private BorderLayout borderLayout = new BorderLayout(1, 1);
    private GridLayout gridLayout = new GridLayout(4, 4, 1, 1);

    public QStateViewer() {
        logger.debug("QStateViewer Constructor.");
        createWindow();
        initPanes();
    }

    public static void main(String[] args) {
        logger.debug("Test QPanel");
        QStateViewer viewer = new QStateViewer();
    }

    private void createWindow() {
        myWindow = new JFrame();
        myWindow.createBufferStrategy(1);
        myWindow.setSize(1200, 1200);
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
        logger.debug("updateView");

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
        for (int i=0; i<NUMBER_OF_PANES; i++) {
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
                actionQValueTextField[i].setText(String.format("%1$07.3f", weights[i]));
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

        int IxOfMaxQValue = getIxOfMaxQValue(weights);
        int IxOfMinQValue = getIxOfMinQValue(weights);

        for (int i = 0; i < weights.length; i++) {
            if (i == IxOfMaxQValue) {
                actionQValueTextField[i].setBackground(Color.GREEN);
            } else if (i == IxOfMinQValue) {
                actionQValueTextField[i].setBackground(Color.PINK);
            } else if (weights[i] == weights[IxOfMaxQValue]) {

                actionQValueTextField[i].setBackground(Color.GREEN);

            } else if (weights[i] == weights[IxOfMinQValue]) {

                actionQValueTextField[i].setBackground(Color.PINK);

            } else {

                actionQValueTextField[i].setBackground(Color.ORANGE);
            }
            if (weights[i] <= -100.00) {
                actionQValueTextField[i].setBackground(Color.PINK);
            }
        }

    }

    private int getIxOfMaxQValue(double[] weights) {
        int ixOfMaxQValue = 0;
        double maxSoFar = Double.MIN_VALUE;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] > maxSoFar) {
                maxSoFar = weights[i];
                ixOfMaxQValue = i;
            }
        }
        return ixOfMaxQValue;
    }

    private int getIxOfMinQValue(double[] weights) {
        int ixOfMinQValue = 0;
        double minSoFar = Double.MAX_VALUE;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] < minSoFar) {
                minSoFar = weights[i];
                ixOfMinQValue = i;
            }
        }
        return ixOfMinQValue;
    }

    public double[] getRandomDoubleArray() {
        double[] doubles = new double[4];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = Math.random() * 100.00;
        }
        return doubles;
    }
}
