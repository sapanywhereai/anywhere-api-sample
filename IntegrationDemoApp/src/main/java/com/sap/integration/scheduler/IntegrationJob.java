package com.sap.integration.scheduler;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sap.integration.product.ProductIntegration;
import com.sap.integration.salesorder.SalesOrderIntegration;
import com.sap.integration.utils.DateUtil;

public class IntegrationJob implements Job {

    /**
     * Logger for logging purposes. Similar definition is used in all classes of this application. You may configure it
     * in log4j.properties configuration file.
     */
    private static final Logger LOG = Logger.getLogger(IntegrationJob.class);

    /**
     * Variable isIntegrationRunning indicates, whether integration is running or not. Scheduler, which starts
     * integration cycles, is set up for certain time period. This time can be low number, higher number. It may happen
     * that integration will not be finished, but another cycle of integration will start. This may cause
     * inconsistencies during the integration process. So it is necessary to have variable, which blocks starting new
     * integration cycle until the previous one is finished. For this purposes serves following variable.
     */
    private static Boolean isIntegrationRunning = false;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        runIntegration();
    }

    /**
     * Provides integration between SAP Anywhere and custom APP.
     */
    public static void runIntegration() {

        // it will starts only when previous integration cycle ended. This variable is indicator of that status.
        if (!isIntegrationRunning) {
            isIntegrationRunning = true;

            LOG.info("New integration cycle is started");

            // log when integration started
            DateTime start = DateTime.now();
            LOG.info("Start of integration process at " + start);

            // TODO Needs info dev review it
            /*
             * Integration can from sap anywhere or to.
             */
            try {
                // MASTER DATA
                ProductIntegration.syncToSapAnywhere();

                // SALES ORDERS
                SalesOrderIntegration.syncFromSapAnywhere();

            } catch (Exception e) {
                LOG.error("Exception " + e.getMessage(), e);
            }

            // log when integration ended
            DateTime end = DateTime.now();
            LOG.info("End of integration process at " + end);
            LOG.info("Duration time: " + DateUtil.getDurationTime(start, end));

            // integration cycle ended
            isIntegrationRunning = false;
        }
    }
}
