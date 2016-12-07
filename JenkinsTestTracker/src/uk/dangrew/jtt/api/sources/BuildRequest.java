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
 * The {@link BuildRequest} is responsible for defining how requests are executed
 * given the jenkins location and the {@link JenkinsJob}, and the specific build number.
 */
public enum BuildRequest {
   
   HistoricDetails( ( l, j, n ) -> JenkinsApiRequests.get().constructHistoricDetailsRequest( l, j, n ) );
   
   
   private final transient BuildRequestExecutor executor;
   
   /**
    * Constructs a new {@link JenkinsApiJobRequest}.
    * @param executor the {@link BuildRequestExecutor} to execute the request on the api.
    */
   private BuildRequest( BuildRequestExecutor executor ) {
      this.executor = executor;
   }//End Constructor

   /**
    * Method to execute the request for the given jenkins location on the given {@link JenkinsJob}.
    * @param jenkinsLocation the jenkins location.
    * @param job the {@link JenkinsJob} to execute for.
    * @param buildNumber the build number in question.
    * @return the {@link HttpGet} to process.
    */
   public HttpGet execute( String jenkinsLocation, JenkinsJob job, int buildNumber ) {
      return executor.execute( jenkinsLocation, job, buildNumber );
   }//End Method
   
}//End Enum
