/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.api.sources;

import java.util.function.Function;

import org.apache.http.client.methods.HttpGet;

/**
 * The {@link JenkinsBaseRequest} is responsible for defining how requests that use the 
 * base jenkins api model are executed given the jenkins location.
 */
public enum JenkinsBaseRequest {
   
   CurrentJobDetailsRequest( JenkinsApiRequests.get()::constructCurrentJobDetailsRequest ), 
   LastCompleteJobDetailsRequest( JenkinsApiRequests.get()::constructLastCompleteJobDetailsRequest );
   
   private final transient Function< String, HttpGet > executor;
   
   /**
    * Constructs a new {@link JobRequest}.
    * @param executor the {@link Function} to execute the request on the api.
    */
   private JenkinsBaseRequest( Function< String, HttpGet > executor ) {
      this.executor = executor;
   }//End Constructor

   /**
    * Method to execute the request for the given jenkins location.
    * @param jenkinsLocation the jenkins location.
    * @return the {@link HttpGet} to process.
    */
   public HttpGet execute( String jenkinsLocation ) {
      return executor.apply( jenkinsLocation );
   }//End Method
   
}//End Enum
