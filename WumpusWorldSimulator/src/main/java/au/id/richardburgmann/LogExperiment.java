package au.id.richardburgmann;
/**
 * Created by Richard Burgmann on 18/06/2017.
 * Copyright Richard Burgmann (2017)
 * All Rights Reserved.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class LogExperiment {

    private static final Logger logger = LoggerFactory.getLogger(LogExperiment.class);
    private static String EXPERIMENT_LOG_DIR = ".";
    private String logFileName = "WWS_run_QRuleV2.";
    private String logFileDataSuffix = "csv";
    private String logFileParamSuffix = "txt";


    public LogExperiment(Properties myProperties) {

        EXPERIMENT_LOG_DIR = myProperties.getProperty("experimentLogDir");
        logger.debug("experimentLogDir=" + EXPERIMENT_LOG_DIR);


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

    public void logData(WWSimulator wwSimulator) {
        //
        // Create log files.
        //
        try {
            StringBuffer fn = new StringBuffer();
            fn.append(EXPERIMENT_LOG_DIR + logFileName + "Data." + logFileDataSuffix);
            BufferedWriter bufferedWriter;
            FileWriter fileWriter;
            fileWriter = new FileWriter(fn.toString(),true); // append to eof.
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(wwSimulator.getLogData());
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException e) {
            logger.error("Failed to log data.");
            logger.error(e.getMessage());
        }


    }

}
