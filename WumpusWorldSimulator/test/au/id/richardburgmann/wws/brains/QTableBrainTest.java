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

package au.id.richardburgmann.wws.brains;

import au.id.richardburgmann.wws.WWS;
import au.id.richardburgmann.wws.WWSimulator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.Assert.fail;

public class QTableBrainTest {
    WWSimulator wwSimulator;
    private static final Logger logger = LoggerFactory.getLogger(QTableBrainTest.class);


    @Before
    public void setUp() throws Exception {

        wwSimulator = new WWSimulator();
        WWSimulator.APPLICATION_PROPERTIES_FILE_LOCATION =
                ".\\\\WumpusWorldSimulator\\\\src\\\\resources\\\\testProperties.properties";
        wwSimulator.startup();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_DIR_LOCATION_OF_BRAIN_Q_VALUES() throws Exception {

        String keyValue = WWSimulator.applicationProps.getProperty(WWS.DIR_LOCATION_OF_BRAIN_Q_VALUES.toString());
        logger.debug(WWS.DIR_LOCATION_OF_BRAIN_Q_VALUES.toString() + " : " + keyValue);
        String targetValue = ".\\WumpusWorldSimulator\\src\\resources\\brains\\";
        logger.debug("Target value : " + targetValue);
        if (!keyValue.equalsIgnoreCase(targetValue)) {
            fail("Property DIR_LOCATION_OF_BRAIN_Q_VALUES not correctly set up for tests.");
        }
    }
    @Test
    public void test_SAVE_BRAIN_Q_VALUES_TO() throws Exception {

        String keyValue = WWSimulator.applicationProps.getProperty(WWS.SAVE_BRAIN_Q_VALUES_TO.toString());
        logger.debug(WWS.SAVE_BRAIN_Q_VALUES_TO.toString() + " : " + keyValue);
        String targetValue = "BRAIN_DATA_WHITE_QUEEN.bin";
        logger.debug("Target value : " + targetValue);
        if (!keyValue.equalsIgnoreCase(targetValue)) {
            fail("Property SAVE_BRAIN_Q_VALUES_TO not correctly set up for tests.");
        }
    }

    @Test
    public void persistBrain() throws Exception {

        QTableBrain labRat = new QTableBrain();

        String dir = WWSimulator.applicationProps.getProperty(WWS.DIR_LOCATION_OF_BRAIN_Q_VALUES.toString());
        String filename = WWSimulator.applicationProps.getProperty(WWS.SAVE_BRAIN_Q_VALUES_TO.toString());

        File labRatsBrain = new File(dir + filename);

        if (labRatsBrain.exists()) labRatsBrain.delete();

        labRat.persistBrain();

        if (!labRatsBrain.exists()) {
            fail("Lab Rats Brain does not exist at "+ dir + filename);
        } else {
            labRatsBrain.delete();
        }


    }

    @Test
    public void loadBrain() throws Exception {
        QTableBrain labRat = new QTableBrain();

        String dir = WWSimulator.applicationProps.getProperty(WWS.DIR_LOCATION_OF_BRAIN_Q_VALUES.toString());
        String filename = WWSimulator.applicationProps.getProperty(WWS.LOAD_BRAIN_Q_VALUES_FROM.toString());

        if (!filename.equalsIgnoreCase("BRAIN_DATA_WHITE_QUEEN.bin")) {
            fail("Property LOAD_BRAIN_Q_VALUES_FROM not correctly set up for tests.");
        } else {
            File labRatsBrain = new File(dir + filename);
            if (labRatsBrain.exists()) labRatsBrain.delete();
            try {
            QTableBrain newBrain = labRat.loadBrain();
            } catch (Exception e) {
                fail("Unable to load QTableBrain from storage. " + e.getMessage());
            }
        }
    }


}
