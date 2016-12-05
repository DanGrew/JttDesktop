/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.sources;

import java.util.function.BiFunction;

import org.apache.http.client.methods.HttpGet;

import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JenkinsApiJobRequest} is responsible for defining how request are executed
 * given the jenkins location and the {@link JenkinsJob}.
 */
public enum JenkinsApiJobRequest {
   
   LastBuildBuildingStateRequest( ( l, j ) -> JenkinsApiRequests.get().constructLastBuildBuildingStateRequest( l, j ) ),
   LastBuildJobDetailsRequest( ( l, j ) -> JenkinsApiRequests.get().constructLastBuildJobDetailsRequest( l, j ) ),
   LastBuildTestResultsWrappedRequest( ( l, j ) -> JenkinsApiRequests.get().constructLastBuildTestResultsWrappedRequest( l, j ) ),
   LastBuildTestResultsUnwrappedRequest( ( l, j ) -> JenkinsApiRequests.get().constructLastBuildTestResultsUnwrappedRequest( l, j ) );
   
   private final transient BiFunction< String, JenkinsJob, HttpGet > executor;
   
   /**
    * Constructs a new {@link JenkinsApiJobRequest}.
    * @param executor the {@link BiFunction} to execute the request on the api.
    */
   private JenkinsApiJobRequest( BiFunction< String, JenkinsJob, HttpGet > executor ) {
      this.executor = executor;
   }//End Constructor

   /**
    * Method to execute the request for the given jenkins location on the given {@link JenkinsJob}.
    * @param jenkinsLocation the jenkins location.
    * @param job the {@link JenkinsJob} to execute for.
    * @return the {@link HttpGet} to process.
    */
   public HttpGet execute( String jenkinsLocation, JenkinsJob job ) {
      return executor.apply( jenkinsLocation, job );
   }//End Method
   
}//End Enum
