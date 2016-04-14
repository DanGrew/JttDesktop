/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.jobs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.handling.BuildState;
import api.handling.JenkinsFetcher;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import model.users.JenkinsUser;
import storage.database.JenkinsDatabase;

/**
 * The {@link JsonJobImporterImpl} provides a method of converting json responses 
 * from an {@link ExternalApi} into {@link JenkinsJob} updates.
 */
public class JsonJobImporterImpl implements JsonJobImporter {
   
   private static final String BUILDING_KEY = "building";
   private static final String ESTIMATED_DURATION_KEY = "estimatedDuration";
   private static final String TIMESTAMP_KEY = "timestamp";
   private static final String NUMBER_KEY = "number";
   private static final String RESULT_KEY = "result";
   private static final String JOBS_KEY = "jobs";
   private static final String NAME_KEY = "name";
   private static final String CULPRITS_KEY = "culprits";
   private static final String FULL_NAME_KEY = "fullName";

   private final JenkinsDatabase database;
   private final JenkinsFetcher fetcher;
   
   /**
    * Constructs a new {@link JsonJobImporterImpl}.
    * @param database the {@link JenkinsDatabase} to import to.
    * @param fetcher the {@link JenkinsFetcher} providing a feedback loop for importing.
    */
   public JsonJobImporterImpl( JenkinsDatabase database, JenkinsFetcher fetcher ) {
      if ( fetcher == null ) throw new IllegalArgumentException( "Null fetcher provided." );
      
      this.database = database;
      this.fetcher = fetcher;
   }//End Constructor

   /**
    * Method to update the state of the build in the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to update.
    * @param response the {@link String} response from the {@link ExternalApi}, in json format.
    */
   @Override public void updateBuildState( JenkinsJob jenkinsJob, String response ) {
      if ( response == null ) return;
      try {
         JSONObject object = new JSONObject( response );
         if ( !object.has( BUILDING_KEY ) ) return;
         
         boolean isBuilding = object.getBoolean( BUILDING_KEY );
         if ( isBuilding ) {
            jenkinsJob.buildStateProperty().set( BuildState.Building );
         } else {
            jenkinsJob.currentBuildTimeProperty().set( 0 );
            jenkinsJob.buildStateProperty().set( BuildState.Built );
         }
         
         if ( object.has( ESTIMATED_DURATION_KEY ) ) {
            long estimatedDurection = object.optLong( ESTIMATED_DURATION_KEY );
            jenkinsJob.expectedBuildTimeProperty().set( estimatedDurection );
         }
         
         if ( object.has( TIMESTAMP_KEY ) ) {
            long timestamp = object.optLong( TIMESTAMP_KEY );
            jenkinsJob.currentBuildTimestampProperty().set( timestamp );
         }
         
         if ( object.has( NUMBER_KEY ) ) {
            int buildNumber = object.getInt( NUMBER_KEY );
            jenkinsJob.currentBuildNumberProperty().set( buildNumber );
            
            if ( jenkinsJob.buildStateProperty().get() == BuildState.Built ) {
               jenkinsJob.lastBuildNumberProperty().set( buildNumber );   
            }
         }
      } catch ( JSONException exception ) {
         return;
      }
   }//End Method

   /**
    * Method to update the job details in the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to update.
    * @param response the {@link String} response from the {@link ExternalApi}, in json format.
    */
   @Override public void updateJobDetails( JenkinsJob jenkinsJob, String response ) {
      if ( response == null || jenkinsJob == null ) return;
      try {
         JSONObject object = new JSONObject( response );
         
         if ( !object.has( NUMBER_KEY ) ) return;
         if ( !object.has( RESULT_KEY ) ) return;
         
         int lastBuildNumber = object.getInt( NUMBER_KEY );
         BuildResultStatus lastBuildResult = object.getEnum( BuildResultStatus.class, RESULT_KEY );
         
         jenkinsJob.lastBuildNumberProperty().set( lastBuildNumber );
         jenkinsJob.lastBuildStatusProperty().set( lastBuildResult );
         
         identifyCulprits( jenkinsJob, object );
      } catch ( JSONException exception ) {
         System.out.println( exception.getMessage() );
         return;
      }
   }//End Method
   
   /**
    * Method to identify the culprits for the given {@link JenkinsJob} from the 
    * given {@link JSONObject} of job details.
    * @param jenkinsJob the {@link JenkinsJob} the culprits are for.
    * @param object the {@link JSONObject} to extract the culprits from.
    */
   private void identifyCulprits( JenkinsJob jenkinsJob, JSONObject object ) {
      jenkinsJob.culprits().clear();
      
      if ( !object.has( CULPRITS_KEY ) ) {
         return;
      }
      
      try {
         JSONArray culprits = object.getJSONArray( CULPRITS_KEY );
         for ( int i = 0; i < culprits.length(); i++ ) {
            
            try {
               JSONObject culprit = culprits.getJSONObject( i );
               if ( !culprit.has( FULL_NAME_KEY ) ) continue;
               
               String userName = culprit.getString( FULL_NAME_KEY );
               retrieveAndApplyCulprit( userName, jenkinsJob );
            } catch ( JSONException exception ) {
               System.out.println( exception.getMessage() );
               continue;
            }
         }
      } catch ( JSONException exception ) {
         System.out.println( exception.getMessage() );
         return;
      }
   }//End Method
   
   /**
    * Method to retrieve the culprit from the {@link JenkinsDatabase} if present. If not the
    * {@link JenkinsFetcher} will be used to retrieve an updted list of {@link JenkinsUser}s.
    * @param userName the name of the user being found.
    * @param jenkinsJob the {@link JenkinsJob} the culprit is for.
    */
   private void retrieveAndApplyCulprit( String userName, JenkinsJob jenkinsJob ){
      //first attempt
      JenkinsUser user = database.getJenkinsUser( userName );
      
      //if null request users loaded
      if ( user == null ) {
         fetcher.fetchUsers();
      }
      
      //try once more
      user = database.getJenkinsUser( userName );
      //if still null forget the user
      if ( user == null ) return;
      
      //otherwise record culprit
      jenkinsJob.culprits().add( user );
      System.out.println( "Found culprit " + userName + " for " + jenkinsJob.nameProperty().get() );
   }//End Method
   
   /**
    * Method to import the jobs from a json {@link String} into the given {@link JenkinsDatabase}.
    * @param response the response from the {@link ExternalApi}.
    */
   @Override public void importJobs( String response ) {
      if ( response == null || database == null ) return;
      try {
         JSONObject object = new JSONObject( response );
         
         if ( !object.has( JOBS_KEY ) ) return;
         
         JSONArray jobs = object.getJSONArray( JOBS_KEY );
         for ( int i = 0; i < jobs.length(); i++ ) {
            try { //Protective wrapper for parse in constructor.
               JSONObject job = jobs.getJSONObject( i );
               if ( !job.has( NAME_KEY ) ) continue;
               
               String name = job.getString( NAME_KEY );
               if ( name.trim().length() == 0 ) continue;
               if ( database.hasJenkinsJob( name ) ) {
                  continue;
               }
               
               JenkinsJob jenkinsJob = new JenkinsJobImpl( name );
               database.store( jenkinsJob );
            } catch ( JSONException exception ) {
               System.out.println( exception.getMessage() );
               continue;
            }
         }
      } catch ( JSONException exception ) {
         System.out.println( exception.getMessage() );
         return;
      }
   }//End Method

}//End Class
