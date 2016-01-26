/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.sources;

import org.apache.http.client.HttpClient;

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
    * @return the {@link HttpClient} providing the connection to jenkins.
    */
   public HttpClient attemptLogin( String jenkinsLocation, String user, String password );
   
   /**
    * Method to determine if logged in to jenkins. If not, no requests can be performed.
    * @return true if logged in.
    */
   public boolean isLoggedIn();
   
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
    * Method to get the results of the latest build of the given {@link JenkinsJob} using the wrapped
    * api request.
    * @param jenkinsJob the {@link JenkinsJob} in question.
    * @return the {@link String} response from the api.
    */
   public String getLatestTestResultsWrapped( JenkinsJob jenkinsJob );
   
   /**
    * Method to get the results of the latest build of the given {@link JenkinsJob} using the unwrapped 
    * api request.
    * @param jenkinsJob the {@link JenkinsJob} in question.
    * @return the {@link String} response from the api.
    */
   public String getLatestTestResultsUnwrapped( JenkinsJob jenkinsJob );

}//End Interface
