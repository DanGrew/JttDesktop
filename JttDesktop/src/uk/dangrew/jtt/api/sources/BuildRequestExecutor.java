/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.sources;

import org.apache.http.client.methods.HttpGet;

import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link BuildRequestExecutor} provides a function for executing {@link BuildRequest}s.
 */
@FunctionalInterface
public interface BuildRequestExecutor {
   
   /**
    * Method to turn the given parameters into a {@link HttpGet} request.
    * @param jenkinsLocation the location of jenkins.
    * @param job the {@link JenkinsJob} in question.
    * @param buildNumber the build number the request is for.
    * @return the {@link HttpGet} to execute.
    */
   public HttpGet execute( String jenkinsLocation, JenkinsJob job, int buildNumber );

}//End Interface
