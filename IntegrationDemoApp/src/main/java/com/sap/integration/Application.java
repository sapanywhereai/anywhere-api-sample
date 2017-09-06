package com.sap.integration;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sap.integration.anywhere.AccessTokenGetter;
import com.sap.integration.customer.CustomerWebhookRegister;
import com.sap.integration.scheduler.IntegrationJob;
import com.sap.integration.scheduler.JobScheduler;
import com.sap.integration.utils.configuration.Property;
import com.sap.integration.utils.configuration.PropertyLoader;

/**
 * Main class
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = { org.springframework.boot.autoconfigure.session.SessionAutoConfiguration.class })
public class Application {

    /**
     * Logger for logging purposes. Similar definition is used in all classes of
     * this application. You may configure it in log4j.properties configuration
     * file.
     */
    private static final Logger LOG = Logger.getLogger(Application.class);

    /**
     * Main method.
     * As input it takes two parameters, which represents path to configuration file and its name.
     * It should be written in following form:
     * <code>-configFile pathToConfigurationFileWithItsName</code>
     * When these two parameters are empty, default config.properties file attached to the
     * application will be taken as main configuration file.
     * 
     * @param args
     *            - in form of two parameters
     *            <code>-configFile pathToConfigurationFileWithItsName</code>
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Application.start(args);
    }

    private static void start(String[] args) {
        LOG.info("Integration demo application started");

        // process input parameters about configuration file
        processApplicationParameters(args);

        try {
            // retrieve and save Access Token
            AccessTokenGetter.runAccessToken();

            // if scheduler is allowed, integration will start according to set up of scheduler
            if (Property.getSchedulerActivation()) {
                // run scheduler
                JobScheduler.run();
            }
            // if scheduler is not allowed, integration will start only once
            else {
                IntegrationJob.runIntegration();
            }

            /*
             * needs define WEBHOOK_LISTEN_BASE_URL in file "config.properties"
             */
            String basicUrl = Property.getWebhookListenBaseURL();
            if (StringUtils.isNotEmpty(basicUrl) && !"notDefined".equals(basicUrl)) {
                CustomerWebhookRegister.registerWebhook(Property.getWebhookListenBaseURL());
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
