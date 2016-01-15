/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.jobs;

import api.sources.ExternalApi;
import model.jobs.JenkinsJob;
import storage.database.JenkinsDatabase;

/**
 * The {@link JsonJobImporter} is responsible for importing {@link JenkinsJob} information from
 * the {@link ExternalApi}.
 */
public interface JsonJobImporter {

   /**
    * Method to update the state of the build in the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to update.
    * @param response the {@link String} response from the {@link ExternalApi}, in json format.
    */
   public void updateBuildState( JenkinsJob jenkinsJob, String response );//End Method

   /**
    * Method to update the job details in the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to update.
    * @param response the {@link String} response from the {@link ExternalApi}, in json format.
    */
   public void updateJobDetails( JenkinsJob jenkinsJob, String response );//End Method

   /**
    * Method to import the jobs from a json {@link String} into the given {@link JenkinsDatabase}.
    * @param response the response from the {@link ExternalApi}.
    */
   public void importJobs( String response );//End Method

}//End Interface