/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.data.json.jobs;

import uk.dangrew.jtt.api.handling.BuildState;
import uk.dangrew.jtt.api.handling.JenkinsFetcher;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * {@link JsonJobImportHandler} is responsible for handling import data being applied to the
 * {@link JenkinsDatabase} model.
 */
public class JsonJobImportHandler {
   
   private final JenkinsDatabase database;
   private final JenkinsFetcher jenkinsFetcher;
   
   /**
    * Constructs a new {@link JsonJobImportHandler}.
    * @param database the {@link JenkinsDatabase} to apply changes to.
    * @param jenkinsFetcher the {@link JenkinsFetcher} for a feedback loop when users need to 
    * be updated.
    */
   JsonJobImportHandler( JenkinsDatabase database, JenkinsFetcher jenkinsFetcher ) {
      if ( database == null ) throw new IllegalArgumentException( "Null database provided." );
      if ( jenkinsFetcher == null ) throw new IllegalArgumentException( "Null fetcher provided." );
      
      this.database = database;
      this.jenkinsFetcher = jenkinsFetcher;
   }//End Constructor

   /**
    * Method to handle the imported building state of the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to be updated.
    * @param isBuilding whether the {@link JenkinsJob} is currently building or built.
    */
   void handleBuildingState( JenkinsJob jenkinsJob, boolean isBuilding ) {
      if ( isBuilding ) {
         jenkinsJob.buildStateProperty().set( BuildState.Building );
      } else {
         jenkinsJob.currentBuildTimeProperty().set( 0 );
         jenkinsJob.buildStateProperty().set( BuildState.Built );
      }
   }//End Method

   /**
    * Method to handle the imported expected duration of the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to be updated.
    * @param estimatedDuration the estimated duration in milliseconds.
    */
   void handleExpectedDuration( JenkinsJob jenkinsJob, long estimatedDuration ) {
      jenkinsJob.expectedBuildTimeProperty().set( estimatedDuration );
   }//End Method

   /**
    * Method to handle the imported timestamp of the last build of the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to be updated.
    * @param timestamp the timestamp as milliseconds since epoch.
    */
   void handleBuildTimestamp( JenkinsJob jenkinsJob, long timestamp ) {
      jenkinsJob.currentBuildTimestampProperty().set( timestamp );
   }//End Method

   /**
    * Method to handle the imported build number of the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to be updated.
    * @param buildNumber the number of the current build.
    */
   void handleBuildNumber( JenkinsJob jenkinsJob, int buildNumber ) {
      jenkinsJob.currentBuildNumberProperty().set( buildNumber );
      
      if ( jenkinsJob.buildStateProperty().get() == BuildState.Built ) {
         jenkinsJob.lastBuildNumberProperty().set( buildNumber );   
      }
   }//End Method

   /**
    * Method to handle the imported build number and status of the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to be updated.
    * @param lastBuildNumber the number of the last build.
    * @param lastBuildResult the result of the last build.
    */
   void handleBuiltJobDetails( JenkinsJob jenkinsJob, int lastBuildNumber, BuildResultStatus lastBuildResult ) {
      jenkinsJob.lastBuildNumberProperty().set( lastBuildNumber );
      jenkinsJob.lastBuildStatusProperty().set( lastBuildResult );
   }//End Method

   /**
    * Method to handle beginning of the import for the potentially multiple {@link JenkinsUser}
    * culprits of the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to be updated.
    */
   void startImportingJobCulprits( JenkinsJob jenkinsJob ) {
      jenkinsJob.culprits().clear();
   }//End Method
   
   /**
    * Method to handle the culprit found for the given {@link JenkinsJob}. The {@link JenkinsUser} will be
    * retrieved from the {@link JenkinsDatabase} if present. If not the
    * {@link JenkinsFetcher} will be used to retrieve an updated list of {@link JenkinsUser}s.
    * @param jenkinsJob the {@link JenkinsJob} the culprit is for.
    * @param userName the name of the user being found.
    */
   void handleUserCulprit( JenkinsJob jenkinsJob, String userName ) {
      //first attempt
      JenkinsUser user = database.getJenkinsUser( userName );
      
      //if null request users loaded
      if ( user == null ) {
         jenkinsFetcher.fetchUsers();
         
         //try once more
         user = database.getJenkinsUser( userName );
         
         //if still null forget the user
         if ( user == null ) {
            return;
         }
      }
      
      //otherwise record culprit
      jenkinsJob.culprits().add( user );
   }//End Method

   /**
    * Method to handle the {@link JenkinsJob} name found. If a {@link JenkinsJob} does not already
    * exist and the name is valid a new {@link JenkinsJob} will be added to the {@link JenkinsDatabase}.
    * @param name the name of the {@link JenkinsJob} found.
    */
   void handleJobFound( String name ) {
      if ( name.trim().length() == 0 ) {
         return;
      }
      
      if ( database.hasJenkinsJob( name ) ) {
         return;
      }
      
      JenkinsJob jenkinsJob = new JenkinsJobImpl( name );
      database.store( jenkinsJob );
   }//End Method

}//End Class
