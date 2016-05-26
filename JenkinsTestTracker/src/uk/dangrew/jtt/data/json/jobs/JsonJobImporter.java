/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.data.json.jobs;

import org.json.JSONObject;

import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JsonJobImporter} is responsible for importing {@link JenkinsJob} information from
 * the {@link ExternalApi}.
 */
public interface JsonJobImporter {

   /**
    * Method to update the state of the build in the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to update.
    * @param response the {@link JSONObject} response from the {@link ExternalApi}.
    */
   public void updateBuildState( JenkinsJob jenkinsJob, JSONObject response );//End Method

   /**
    * Method to update the job details in the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to update.
    * @param response the {@link JSONObject} response from the {@link ExternalApi}.
    */
   public void updateJobDetails( JenkinsJob jenkinsJob, JSONObject response );//End Method

   /**
    * Method to import the jobs from a {@link JSONObject} into the given {@link JenkinsDatabase}.
    * @param response the response from the {@link ExternalApi}.
    */
   public void importJobs( JSONObject response );//End Method

}//End Interface