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

import au.id.richardburgmann.WWSimulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Properties;

public class MainWindow extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);
    private Dimension minSize = new Dimension(440, 440);
    private Font myMenuFont = new Font(Font.SANS_SERIF, Font.PLAIN, 28);
    private int defaultWidth = 1200;
    private int defaultHeight = 1200;
    private int defaultMargin = 20;


    public MainWindow() {
        initMainWindow();
    }

    public MainWindow(WWSimulator simulator) {
        initMainWindow();
    }

    public static void main(String[] args) {
        logger.debug("Test");
        MainWindow myWindow = new MainWindow();
        myWindow.initMainWindow();
        GridPanel myPanel = new GridPanel();
        myWindow.add(myPanel);
        myWindow.setVisible(true);



    }

    public void initMainWindow() {
        setMinimumSize(minSize);
        setSize(defaultWidth, defaultHeight);
        setTitle("Wumpus World Simulator");
        setJMenuBar(createMyMenus());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentListener(new GuiEventListener());


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
    Getters and Setters
     */

    public Dimension getMinSize() {
        return minSize;
    }

    public void setMinSize(Dimension minSize) {
        this.minSize = minSize;
    }

    public Font getMyMenuFont() {
        return myMenuFont;
    }

    public void setMyMenuFont(Font myMenuFont) {
        this.myMenuFont = myMenuFont;
    }

    public int getDefaultWidth() {
        return defaultWidth;
    }

    public void setDefaultWidth(int defaultWidth) {
        this.defaultWidth = defaultWidth;
    }

    public int getDefaultHeight() {
        return defaultHeight;
    }

    public void setDefaultHeight(int defaultHeight) {
        this.defaultHeight = defaultHeight;
    }

    public int getDefaultMargin() {
        return defaultMargin;
    }

    public void setDefaultMargin(int defaultMargin) {
        this.defaultMargin = defaultMargin;
    }

    public void assignProperties(Properties applicationProps) {

        int temp;
        /**
         * Just in case there is no data in any of the property files, use the hardcoded
         * default. The try case block is so we don't destroy the hard coded value if we
         * have nothing in the property files.
         */
        try {
            temp = Integer.parseInt(applicationProps.getProperty("defaultMargin"));
            if (temp > 0) ;
        } catch (Exception e) {
            logger.warn("No defaultMargin defined in either properties file.");
            logger.warn(e.getMessage());
        }

        try {
            temp = 0; // reset temp as a precaution.
            temp = Integer.parseInt(applicationProps.getProperty("defaultHeight"));
            if (temp > 0) defaultHeight = temp;
        } catch (Exception e) {
            logger.warn("No defaultHeight defined in either properties file.");
            logger.warn(e.getMessage());
        }

        defaultWidth = Integer.parseInt(applicationProps.getProperty("defaultWidth"));
        try {
            temp = 0; // reset temp as a precaution.
            temp = Integer.parseInt(applicationProps.getProperty("defaultWidth"));
            if (temp > 0) defaultWidth = temp;
        } catch (Exception e) {
            logger.warn("No defaultWidth defined in either properties file.");
            logger.warn(e.getMessage());
        }
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
                //cleanUpAndShutDown();
                //frame.dispose();
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
            //startRun = true;

        }
    }

    public class runMenuItemListener implements MenuListener {

        public void actionPerformed() {

        }

        @Override
        public void menuSelected(MenuEvent e) {
            logger.debug("runMenu MenuEvent Selected");
            // if (startRun) {
            //   startRun = false;
            //  } else {
            //startRun = true;
            // }

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
