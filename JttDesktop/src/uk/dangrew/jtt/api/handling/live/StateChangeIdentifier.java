/*
 * ---------------------------------------- Jenkins Test Tracker
 * ---------------------------------------- Produced by Dan Grew 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling.live;

import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link StateChangeIdentifier} is responsible for identifying state changes in a
 * {@link JenkinsJob} for the {@link JobDetailsModel}.
 */
public interface StateChangeIdentifier {

   /**
    * Method to record the state of the {@link JenkinsJob} before the update.
    * @param job the {@link JenkinsJob} in question.
    */
   public void recordState( JenkinsJob job );

   /**
    * Method to identify the state changes of the associated {@link JenkinsJob}.
    * {@link #recordState(JenkinsJob)} must have been run prior to this call.
    */
   public void identifyStateChanges();

}//End Interface