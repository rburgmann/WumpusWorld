package au.id.richardburgmann;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class LogExperiment {

    private static final Logger logger = LoggerFactory.getLogger(LogExperiment.class);
    private static String EXPERIMENT_LOG_DIR = ".";
    private String logFileName = "WWS_logfile.";
    private String logFileDataSuffix = "csv";
    private String logFileParamSuffix = "txt";


    public LogExperiment(Properties myProperties) {

        EXPERIMENT_LOG_DIR = myProperties.getProperty("experimentLogDir");
        deleteLogDataIfExists();
        logFileName = myProperties.getProperty("experimentLogFileName");
        logger.debug("experimentLogDir=" + EXPERIMENT_LOG_DIR);
        logger.debug("experimentLogFileName=" + logFileName);


    }

    public static void main(String[] args) {

    }

    public void logParams(Properties myProperties) {
        //
        // Create log files.
        //
        try {
            StringBuffer fn = new StringBuffer();
            fn.append(EXPERIMENT_LOG_DIR + logFileName + "Param." + logFileParamSuffix);
            FileOutputStream out = new FileOutputStream(fn.toString());
            myProperties.store(out, "Wumpus World Simulator Experiment Properties.");
            out.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }


    }

    public void logData(String logEntry) {
        //
        // Create log files.
        //
        try {
            StringBuffer fn = new StringBuffer();
            fn.append(EXPERIMENT_LOG_DIR + logFileName + "Data." + logFileDataSuffix);
            BufferedWriter bufferedWriter;
            FileWriter fileWriter;
            fileWriter = new FileWriter(fn.toString(), true); // append to eof.
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(logEntry);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException e) {
            logger.error("Failed to log data.");
            logger.error(e.getMessage());
        }
    }

    public void deleteLogDataIfExists() {

        String pathname = new String(EXPERIMENT_LOG_DIR + logFileName + "Data." + logFileDataSuffix);
        try {
            boolean ok = Files.deleteIfExists(Paths.get(pathname));
        } catch (IOException x) {
            System.err.println(x);
        }
    }

}
