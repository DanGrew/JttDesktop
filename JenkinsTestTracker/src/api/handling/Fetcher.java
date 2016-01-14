/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.handling;

import api.sources.ExternalApi;
import model.jobs.JenkinsJob;
import storage.database.JenkinsDatabase;

/**
 * The {@link Fetcher} describes an object that can retrieve updated information
 * from an {@link ExternalApi} and update {@link JenkinsJob}s with responses.
 */
public interface Fetcher {

   /**
    * Method to update the build state of the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to update.
    */
   public void updateBuildState( JenkinsJob jenkinsJob );

   /**
    * Method to update the job details of the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to update.
    */
   public void updateJobDetails( JenkinsJob jenkinsJob );
   
   /**
    * Method to fetch the current jobs from the {@link ExternalApi} and populate them
    * in the {@link JenkinsDatabase}.
    * @param database the {@link JenkinsDatabase} to populate.
    */
   public void fetchJobs( JenkinsDatabase database );

}//End Interface
