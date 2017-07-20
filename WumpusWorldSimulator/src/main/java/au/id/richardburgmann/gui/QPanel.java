package au.id.richardburgmann.gui;
/*
 * Created by Richard Burgmann on 10/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import au.id.richardburgmann.brains.QTableBrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * QPanel displays the learnt weights (the q values) as the Adventurer learns them.
 */

public class QPanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(QPanel.class);
    private JFrame myWindow;
    ArrayList<QTableBrain.BrainCell> learntQValues = new ArrayList<QTableBrain.BrainCell>(16);
    ArrayList<JPanel> jPanelArrayList = new ArrayList<JPanel>(16);
    private double[] actionQValues = new double[4];
    private JTextField[] actionQValueTextField = new JTextField[4];
    private Font myFont = new Font(Font.SANS_SERIF, Font.PLAIN, 28);
    private BorderLayout borderLayout = new BorderLayout(1,1);
    private GridLayout gridLayout = new GridLayout(4, 4, 10, 10);

    public QPanel() {
        super();
        setFont(myFont);
        refreshTextFieldsWithQuestionMarks();
    }

    public static void main(String[] args) {
        logger.debug("Test QPanel");

        JFrame myWindow = new JFrame();
        myWindow.createBufferStrategy(1);
        myWindow.setTitle("Q Value Test Window");
        myWindow.setSize(1200,1200);
        myWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        myWindow.setLayout(new GridLayout(4,4, 1,1));

        JPanel[] panes = new JPanel[16];
        for (int i=0; i<16; i++) {
            QPanel qPanel = new QPanel();
            qPanel.refreshTextFieldsWithQuestionMarks();
            panes[i] = qPanel.getJPanelWithQValues();
            myWindow.add(panes[i]);
        }

        myWindow.setVisible(true);

        for (int i=0; i<16; i++) {
            QPanel grid = new QPanel();
            grid.setActionQValues(grid.getRandomDoubleArray());
            grid.refreshTextFields();
            grid.adjustBackgroundColourDependingOnQValues();
            panes[i] = grid.resetJPanel(panes[i]);
            myWindow.setVisible(true);
            try {
                Thread.sleep(500);
            } catch (Exception e) {

            }
        }



    }
    public void appendWeights(QTableBrain.BrainCell brainCell) {
        learntQValues.add(brainCell);
    }
    public void dispose() {
        myWindow.dispose();
    }
    public void displayQValues() {
        logger.debug("displayQValues");

        myWindow = new JFrame();
        myWindow.setTitle("Learnt Q Values");


        myWindow.setSize(1200,1200);
        myWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        myWindow.setLayout(new GridLayout(4,4, 1,1));

        // Now load real values
        for (int i=0; i<learntQValues.size(); i++) {
            QTableBrain.BrainCell brainCell = learntQValues.get(i);
            // Calculate which panel
            int panelIx = (brainCell.cell.row * 4) + brainCell.cell.col;
            double[] qValues = brainCell.weights;

            QPanel grid = new QPanel();
            grid.setActionQValues(qValues);
            grid.refreshTextFields();
            grid.adjustBackgroundColourDependingOnQValues();
            JPanel updatedPanel = grid.getJPanelWithQValues();
            myWindow.add(updatedPanel);
        }
        myWindow.setVisible(true);
        myWindow.setLocation(1200,0);
    }
    public void setActionQValues(double[] actionQValues) {
        this.actionQValues = actionQValues;
    }
    public void refreshTextFields() {
        for (int i=0; i<actionQValues.length; i++ ) {
            actionQValueTextField[i] = new JTextField();
            actionQValueTextField[i].setText(String.format("%1$07.3f", actionQValues[i]));
            actionQValueTextField[i].setFont(myFont);
            actionQValueTextField[i].setHorizontalAlignment(JTextField.CENTER);
        }
    }
    public void refreshTextFieldsWithQuestionMarks() {
        for (int i=0; i<actionQValues.length; i++ ) {
            actionQValueTextField[i] = new JTextField();
            actionQValueTextField[i].setText("?");
            actionQValueTextField[i].setFont(myFont);
            actionQValueTextField[i].setHorizontalAlignment(JTextField.CENTER);
        }
    }
    public JPanel getJPanelWithQValues() {
        JPanel actionQValuePanel = new JPanel();
        actionQValuePanel.setLayout(borderLayout);
        actionQValuePanel.add(actionQValueTextField[0], BorderLayout.WEST);
        actionQValuePanel.add(actionQValueTextField[1],BorderLayout.EAST);
        actionQValuePanel.add(actionQValueTextField[2], BorderLayout.NORTH);
        actionQValuePanel.add(actionQValueTextField[3], BorderLayout.SOUTH);
        return actionQValuePanel;
    }
    public JPanel resetJPanel(JPanel storedJPanel) {
        JPanel actionQValuePanel = storedJPanel;
        actionQValuePanel.setLayout(borderLayout);
        actionQValuePanel.add(actionQValueTextField[0], BorderLayout.WEST);
        actionQValuePanel.add(actionQValueTextField[1],BorderLayout.EAST);
        actionQValuePanel.add(actionQValueTextField[2], BorderLayout.NORTH);
        actionQValuePanel.add(actionQValueTextField[3], BorderLayout.SOUTH);
        return actionQValuePanel;

    }

    public void adjustBackgroundColourDependingOnQValues() {
        // Find the max
        int IxOfMaxQValue = getIxOfMaxQValue();
        int IxOfMinQValue = getIxOfMinQValue();

        for (int i=0; i<actionQValues.length; i++) {
            actionQValueTextField[i].setBackground(Color.ORANGE);
            if (actionQValues[i] == actionQValues[IxOfMaxQValue]) {
                actionQValueTextField[i].setBackground(Color.GREEN);
            }
            if (actionQValues[i] == actionQValues[IxOfMinQValue]) {
                actionQValueTextField[i].setBackground(Color.PINK);
            }
        }
    }
    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.black);
    }
    private int getIxOfMaxQValue() {
        int ixOfMaxQValue = 0;
        double maxSoFar = Double.MIN_VALUE;
        for (int i=0; i<actionQValues.length; i++) {
            if (actionQValues[i] > maxSoFar) {
                maxSoFar = actionQValues[i];
                ixOfMaxQValue = i;
            }
        }
        return ixOfMaxQValue;
    }
    private int getIxOfMinQValue() {
        int ixOfMinQValue = 0;
        double minSoFar = Double.MAX_VALUE;
        for (int i=0; i<actionQValues.length; i++) {
            if (actionQValues[i] < minSoFar) {
                minSoFar = actionQValues[i];
                ixOfMinQValue = i;
            }
        }
        return ixOfMinQValue;
    }
    public double[] getRandomDoubleArray() {
        double[] doubles = new double[4];
        for (int i=0; i<doubles.length; i++) {
            doubles[i] = Math.random() * 100.00;
        }
        return doubles;
    }
}
