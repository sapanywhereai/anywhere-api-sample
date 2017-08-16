package com.sap.integration.scheduler;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.sap.integration.utils.configuration.Property;

/**
 * Initialize scheduler, which starts integration cycle and retrieving access token.
 */
public class JobScheduler {

    private static final Logger LOG = Logger.getLogger(JobScheduler.class);

    public static void run() {
        LOG.info("Starting scheduler");
        try {
            JobDetail integrationJob = JobBuilder.newJob(IntegrationJob.class).build();

            Trigger integrationTrigger = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule(Property.getSchedulerIntegration())).build();

            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(integrationJob, integrationTrigger);
        } catch (Exception e) {
            LOG.error("Exception " + e.getMessage(), e);
        }
        LOG.info("Scheduler started. Wait until services will start according to set up from config.properties.");
    }
}
