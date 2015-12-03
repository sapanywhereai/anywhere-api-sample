package com.sap.integration;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sap.integration.core.scheduler.AccessTokenJob;
import com.sap.integration.core.scheduler.IntegrationJob;
import com.sap.integration.core.scheduler.JobScheduler;
import com.sap.integration.utils.configuration.Property;
import com.sap.integration.utils.configuration.PropertyLoader;

/**
 * Main class containing methods and calls, which will start all integration.
 */
public class Main {

    /**
     * Logger for logging purposes. Similar definition is used in all classes of this application. You may configure it in
     * log4j.properties configuration file.
     */
    private static final Logger LOG = Logger.getLogger(Main.class);

    /**
     * Main method, which starts whole integration cycle. As input it takes two parameters, which represents path to configuration
     * file and its name. It should be written in following form: <code>-configFile pathToConfigurationFileWithItsName</code> When
     * these two parameters are empty, default config.properties file attached to the application will be taken as main
     * configuration file.
     * 
     * @param args - in form of two parameters <code>-configFile pathToConfigurationFileWithItsName</code>
     */
    public static void main(String[] args) {
        LOG.info("Integration demo application started");

        // process input parameters about configuration file
        processApplicationParameters(args);

        try {
            // retrieve and save Access Token
            AccessTokenJob.runAccessToken();

            // if scheduler is allowed, integration will start according to set up of scheduler
            if (Property.getSchedulerActivation()) {
                // run scheduler
                JobScheduler.run();
            }
            // if scheduler is not allowed, integration will start only once
            else {
                IntegrationJob.runIntegration();
            }
        } catch (Exception e) {
            LOG.info("Exception " + e.getMessage(), e);
        }
    }

    /**
     * Method, which loads and process application parameters.
     */
    private static void processApplicationParameters(String[] args) {

        LOG.debug("Processing input arguments");

        if (args != null && args.length > 0) {

            int indexOfConfigFile = Arrays.asList(args).indexOf("-configFile");

            if (indexOfConfigFile >= 0 && StringUtils.isNotBlank(args[indexOfConfigFile + 1])) {
                PropertyLoader.setConfigurationFileName(args[indexOfConfigFile + 1]);
                LOG.debug("Loaded property configFile with value " + args[indexOfConfigFile + 1] + " was saved.");
            }
        }
    }
}
