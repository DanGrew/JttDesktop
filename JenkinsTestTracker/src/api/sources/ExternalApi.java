/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.sources;

import model.jobs.JenkinsJob;

/**
 * The {@link ExternalApi} provides an interface to an external source of data
 * that provides information for {@link JenkinsJob}s.
 */
public interface ExternalApi {

   /**
    * Method to get the current build state as a json {@link String}.
    * @param jenkinsJob the {@link JenkinsJob} to get the state for. 
    */
   public String getLastBuildBuildingState( JenkinsJob jenkinsJob );

   /**
    * Method to get the job details of the last build as a json {@link String}.
    * @param jenkinsJob the {@link JenkinsJob} to get the details for. 
    */
   public String getLastBuildJobDetails( JenkinsJob jenkinsJob );

}//End Interface
