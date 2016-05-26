/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling;

/**
 * The {@link JenkinsProcessing} provides an expanded interface to the {@link JenkinsFetcher} that
 * provides more options for retrieving information involving logic to determine which information
 * to update.
 */
public interface JenkinsProcessing extends JenkinsFetcher {

   /**
    * Method to {@link #fetchJobs()} and {@link #updateJobDetails(JenkinsJob)} for all
    * {@link JenkinsJob}s.
    */
   public void fetchJobsAndUpdateDetails();

}//End Interface