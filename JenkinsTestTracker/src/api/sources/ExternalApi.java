/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.sources;

import org.apache.http.impl.client.DefaultHttpClient;

import model.jobs.JenkinsJob;

/**
 * The {@link ExternalApi} provides an interface to an external source of data
 * that provides information for {@link JenkinsJob}s.
 */
public interface ExternalApi {

   /**
    * Method to attempt a login.
    * @param jenkinsLocation the location of Jenkins.
    * @param user the user name.
    * @param password the password.
    */
   public DefaultHttpClient attemptLogin( String jenkinsLocation, String user, String password );
   
   /**
    * Method to get the current build state as a json {@link String}.
    * @param jenkinsJob the {@link JenkinsJob} to get the state for.
    * @return the {@link String} response from the api. 
    */
   public String getLastBuildBuildingState( JenkinsJob jenkinsJob );

   /**
    * Method to get the job details of the last build as a json {@link String}.
    * @param jenkinsJob the {@link JenkinsJob} to get the details for.
    * @return the {@link String} response from the api. 
    */
   public String getLastBuildJobDetails( JenkinsJob jenkinsJob );

   /**
    * Method to get the list of job names currently available.
    * @return the {@link String} response from the api.
    */
   public String getJobsList();

   /**
    * Method to get the results of the latest build of the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} in question.
    * @return the {@link String} response from the api.
    */
   public String getLatestTestResults( JenkinsJob jenkinsJob );

}//End Interface
