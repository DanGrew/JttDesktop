/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.data.json.jobs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.dangrew.jtt.api.handling.JenkinsFetcher;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link JsonJobImporterImpl} provides a method of converting json responses 
 * from an {@link ExternalApi} into {@link JenkinsJob} updates.
 */
public class JsonJobImporterImpl implements JsonJobImporter {
   
   static final String BUILDING_KEY = "building";
   static final String NODE_KEY = "builtOn";
   static final String ESTIMATED_DURATION_KEY = "estimatedDuration";
   static final String TIMESTAMP_KEY = "timestamp";
   static final String NUMBER_KEY = "number";
   static final String RESULT_KEY = "result";
   static final String JOBS_KEY = "jobs";
   static final String NAME_KEY = "name";
   static final String CULPRITS_KEY = "culprits";
   static final String FULL_NAME_KEY = "fullName";

   private final JsonJobImportHandler handler;
   
   /**
    * Constructs a new {@link JsonJobImporterImpl}.
    * @param database the {@link JenkinsDatabase} to import to.
    * @param fetcher the {@link JenkinsFetcher} providing a feedback loop for importing.
    */
   public JsonJobImporterImpl( JenkinsDatabase database, JenkinsFetcher fetcher ) {
      this( new JsonJobImportHandler( database, fetcher ) );
   }//End Constructor
   
   /**
    * Constructs a new {@link JsonJobImporterImpl}.
    * @param fetcher the {@link JenkinsFetcher} providing a feedback loop for importing.
    */
   JsonJobImporterImpl( JsonJobImportHandler handler ) {
      this.handler = handler;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void updateBuildState( JenkinsJob jenkinsJob, JSONObject object ) {
      if ( jenkinsJob == null || object == null ) {
         //not interested in empty responses and jobs
         return;
      }
      
      if ( !object.has( BUILDING_KEY ) ) {
         //not interested in details from a response that does not have the key info we need
         return;
      }
      
      try {
         boolean isBuilding = object.getBoolean( BUILDING_KEY );
         handler.handleBuildingState( jenkinsJob, isBuilding );
      } catch ( JSONException exception ) {
         //key is not present and do not want default value
         return;
      }
      
      if ( object.has( ESTIMATED_DURATION_KEY ) ) {
         long estimatedDuration = object.optLong( ESTIMATED_DURATION_KEY, jenkinsJob.expectedBuildTimeProperty().get() );
         handler.handleExpectedDuration( jenkinsJob, estimatedDuration );
      }
      
      if ( object.has( TIMESTAMP_KEY ) ) {
         long timestamp = object.optLong( TIMESTAMP_KEY, jenkinsJob.currentBuildTimestampProperty().get() );
         handler.handleBuildTimestamp( jenkinsJob, timestamp );
      }
      
      if ( object.has( NUMBER_KEY ) ) {
         int buildNumber = object.optInt( NUMBER_KEY, jenkinsJob.currentBuildNumberProperty().get() );
         handler.handleBuildNumber( jenkinsJob, buildNumber );
      }
      
      if ( object.has( NODE_KEY ) ) {
         JenkinsNode previousNode = jenkinsJob.lastBuiltOnProperty().get();
         String builtOn = object.optString( NODE_KEY, previousNode == null ? null : previousNode.nameProperty().get() );
         handler.handleBuiltOn( jenkinsJob, builtOn );
      }
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void updateJobDetails( JenkinsJob jenkinsJob, JSONObject object ) {
      if ( jenkinsJob == null || object == null ) {
         //not interested in empty responses and jobs
         return;
      }

      if ( !object.has( NUMBER_KEY ) ) return;
      if ( !object.has( RESULT_KEY ) ) return;
      
      int lastBuildNumber = object.optInt( NUMBER_KEY, jenkinsJob.currentBuildNumberProperty().get() );
      BuildResultStatus lastBuildResult = object.optEnum( BuildResultStatus.class, RESULT_KEY );
      if ( lastBuildResult == null ) {
         //ignore imports where there is no status
         return;
      }
      
      handler.handleBuiltJobDetails( jenkinsJob, lastBuildNumber, lastBuildResult );
      
      identifyCulprits( jenkinsJob, object );
   }//End Method
   
   /**
    * Method to identify the culprits for the given {@link JenkinsJob} from the 
    * given {@link JSONObject} of job details.
    * @param jenkinsJob the {@link JenkinsJob} the culprits are for.
    * @param object the {@link JSONObject} to extract the culprits from.
    */
   private void identifyCulprits( JenkinsJob jenkinsJob, JSONObject object ) {
      handler.startImportingJobCulprits( jenkinsJob );
      
      if ( !object.has( CULPRITS_KEY ) ) {
         return;
      }
   
      JSONArray culprits = object.optJSONArray( CULPRITS_KEY );
      if ( culprits == null ) {
         return;
      }
      
      for ( int i = 0; i < culprits.length(); i++ ) {
         JSONObject culprit = culprits.getJSONObject( i );
         if ( culprit == null ) {
            continue;
         }
         if ( !culprit.has( FULL_NAME_KEY ) ) {
            continue;
         }
         
         String userName = culprit.optString( FULL_NAME_KEY );
         if ( userName == null ) {
            continue;
         }
         handler.handleUserCulprit( jenkinsJob, userName );
      }
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void importJobs( JSONObject object ) {
      if ( object == null || !object.has( JOBS_KEY ) ) {
         return;
      }
         
      JSONArray jobs = object.optJSONArray( JOBS_KEY );
      if ( jobs == null ) {
         return;
      }
      
      for ( int i = 0; i < jobs.length(); i++ ) {
         JSONObject job = jobs.optJSONObject( i );
         if ( job == null || !job.has( NAME_KEY ) ) {
            continue;
         }
         
         String name = job.optString( NAME_KEY );
         if ( name == null ) {
            continue;
         }
         handler.handleJobFound( name );
      }
   }//End Method
   
}//End Class
