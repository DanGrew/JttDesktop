/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.api.handling.live;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import uk.dangrew.jtt.desktop.api.handling.JenkinsFetcher;
import uk.dangrew.jtt.desktop.api.handling.JenkinsFetcherImpl;
import uk.dangrew.jtt.desktop.api.sources.ExternalApi;
import uk.dangrew.jtt.desktop.api.sources.JenkinsBaseRequest;
import uk.dangrew.jtt.desktop.data.json.conversion.ApiResponseToJsonConverter;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.SystemWideJenkinsDatabaseImpl;

/**
 * {@link LiveStateFetcher} provides a method of updating the live information of {@link JenkinsJob}s. */
public class LiveStateFetcher {

   private final JenkinsDatabase database;
   private final ExternalApi externalApi;
   private final ApiResponseToJsonConverter converter;
   private final JobDetailsParser parser;
   private final JenkinsFetcher fetcher;
   
   private final Map< JenkinsJob, Integer > unstableJobsLastBuildNumbersTestRetrievedFor;
   
   /**
    * Constructs a new {@link LiveStateFetcher}.
    * @param externalApi the {@link ExternalApi} to retrieve updates from.
    */
   public LiveStateFetcher( ExternalApi externalApi ) {
      this( 
               new SystemWideJenkinsDatabaseImpl().get(), 
               externalApi, 
               new ApiResponseToJsonConverter(), 
               new JobDetailsParser( new JobDetailsModel() ),
               new JenkinsFetcherImpl( externalApi )
      );
   }//End Constructor

   /**
    * Constructs a new {@link LiveStateFetcher}.
    * @param database the {@link JenkinsDatabase} to populate and update.
    * @param externalApi the {@link ExternalApi} to retrieve updates from.
    * @param converter the {@link ApiResponseToJsonConverter}.
    * @param parser the {@link JobDetailsParser}.
    * @param fetcher the {@link JenkinsFetcher}. 
    */
   LiveStateFetcher( 
            JenkinsDatabase database, 
            ExternalApi externalApi, 
            ApiResponseToJsonConverter converter,
            JobDetailsParser parser,
            JenkinsFetcher fetcher
   ) {
      if ( database == null || externalApi == null ) {
         throw new IllegalArgumentException( "Must supply non null parameters." );
      }
      
      this.database = database;
      this.externalApi = externalApi;
      this.converter = converter;
      this.parser = parser;
      this.fetcher = fetcher;
      
      this.unstableJobsLastBuildNumbersTestRetrievedFor = new HashMap<>();
   }//End Constructor

   /**
    * Method to load all job details for the last completed build of each {@link JenkinsJob}.
    */
   public void loadLastCompletedBuild() {
      updateState( JenkinsBaseRequest.LastCompleteJobDetailsRequest );
   }//End Method
   
   /**
    * Method to update the current build state of each {@link JenkinsJob}.
    */
   public void updateBuildState() {
      updateState( JenkinsBaseRequest.CurrentJobDetailsRequest );
   }//End Method
   
   /**
    * Method to update the details given the {@link JenkinsBaseRequest} to execute.
    * @param request the {@link JenkinsBaseRequest} to execute.
    */
   private void updateState( JenkinsBaseRequest request ) {
      String response = externalApi.executeRequest( request );
      
      JSONObject converted = converter.convert( response );
      if ( converted == null ) {
         return;
      }
      parser.parse( converted );
      
      database.jenkinsJobs().forEach( this::detectAndRequestTestResultUpdates );
   }//End Method
   
   /**
    * Method to detect where test results are required and to perform the update immediately.
    * @param job the {@link JenkinsJob} to check.
    */
   private void detectAndRequestTestResultUpdates( JenkinsJob job ){
      if ( job.buildStateProperty().get() == BuildState.Building ) {
         return;
      }
      BuildResultStatus current = job.getBuildStatus();
      if ( current != BuildResultStatus.UNSTABLE ) {
         unstableJobsLastBuildNumbersTestRetrievedFor.remove( job );
         return;
      }
      
      Integer lastNumberUpdatedFor = unstableJobsLastBuildNumbersTestRetrievedFor.get( job );
      if ( lastNumberUpdatedFor == null ) {
         unstableJobsLastBuildNumbersTestRetrievedFor.put( job, job.getBuildNumber() );
         fetcher.updateTestResults( job );
      } else if ( !lastNumberUpdatedFor.equals( job.getBuildNumber() ) ) {
         unstableJobsLastBuildNumbersTestRetrievedFor.put( job, job.getBuildNumber() );
         fetcher.updateTestResults( job );
      }
   }//End Method

}//End Class
