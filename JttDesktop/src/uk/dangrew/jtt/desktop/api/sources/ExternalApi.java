/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.api.sources;

import org.apache.http.client.HttpClient;

import uk.dangrew.jtt.model.jobs.JenkinsJob;

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
    * Method to execute the given {@link JenkinsBaseRequest}.
    * @param request the {@link JenkinsBaseRequest} to execute.
    * @return the {@link String} response from the api.
    */
   public String executeRequest( JenkinsBaseRequest request );
   
   /**
    * Method to execute the given {@link JobRequest} against the given {@link JenkinsJob}.
    * @param request the {@link JobRequest} to execute.
    * @param job the {@link JenkinsJob} the request is for.
    * @return the {@link String} response from the api.
    */
   public String executeRequest( JobRequest request, JenkinsJob job );
   
   /**
    * Method to execute the given {@link BuildRequest} against the given {@link JenkinsJob}.
    * @param request the {@link BuildRequest} to execute.
    * @param job the {@link JenkinsJob} the request is for.
    * @param buildNumber the build number the request is for.
    * @return the {@link String} response from the api.
    */
   public String executeRequest( BuildRequest request, JenkinsJob job, int buildNumber );
   
   /**
    * Method to get the list of job names currently available.
    * @return the {@link String} response from the api.
    */
   public String getJobsList();
   
   /**
    * Method to get the list of users currently available.
    * @return the {@link String} response from the api.
    */
   public String getUsersList();

}//End Interface
