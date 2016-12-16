/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling.live;

import java.util.ArrayList;
import java.util.List;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.nodes.JenkinsNodeImpl;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link JobDetailsModel} is the model for the {@link JobDetailsParser}, {@link uk.dangrew.jupa.json.parse.JsonParser}.
 */
class JobDetailsModel {
   
   static final String MASTER_ID = "";
   static final String MASTER_NAME = "Master Node";
   
   private final JenkinsDatabase database;
   private final StateChangeIdentifier stateChanges;
   
   private String jobName;
   private Integer buildNumber;
   private BuildState buildState;
   private BuildResultStatus result;
   private Long timestamp; 
   private Long duration;
   private Long estimatedDuration;
   private String builtOn;
   private Integer failCount;
   private Integer skipCount;
   private Integer totalTestCount;
   private List< String > culprits;
   
   /**
    * Constructs a new {@link JobDetailsModel}.
    * @param database the {@link JenkinsDatabase}.
    */
   public JobDetailsModel( JenkinsDatabase database ) {
      this( database, new StateChangeIdentifier() );
   }//End Constructor
   
   /**
    * Constructs a new {@link JobDetailsModel}.
    * @param database the {@link JenkinsDatabase}.
    * @param stateChanges the {@link StateChangeIdentifier}.
    */
   JobDetailsModel( JenkinsDatabase database, StateChangeIdentifier stateChanges ) {
      if ( database == null ) {
         throw new IllegalArgumentException( "Must supply non null database." );
      }
      this.database = database;
      this.culprits = new ArrayList<>();
      this.stateChanges = stateChanges;
   }//End Constructor
   
   /**
    * Start parsing the next job. This will clear all previously parsed values.
    * @param key the parsed key.
    */
   void startJob( String key ) {
      this.jobName = null;
      this.buildNumber = null;
      this.result = null;
      this.buildState = null;
      this.timestamp = null;
      this.duration = null;
      this.estimatedDuration = null;
      this.builtOn = null;
      this.failCount = null;
      this.skipCount = null;
      this.totalTestCount = null;
      this.culprits.clear();
   }//End Method
   
   /**
    * Finish parsing the current job. This will push the information through to the {@link JenkinsDatabase}.
    * @param key the parsed key.
    */
   void finishJob( String key ) {
      if ( jobName == null ) {
         return;
      }
      JenkinsJob job = database.getJenkinsJob( jobName );
      if ( job == null ) {
         job = new JenkinsJobImpl( jobName );
         database.store( job );
      }
      
      stateChanges.recordState( job );
      if ( buildState != null ) {
         job.buildStateProperty().set( buildState );
         if ( buildState == BuildState.Built ) {
            job.currentBuildTimeProperty().set( 0 );
         }
      }
      if ( buildNumber != null ) {
         job.currentBuildNumberProperty().set( buildNumber );
         job.setLastBuildNumber( buildNumber );
      }
      if ( result != null ) {
         job.setLastBuildStatus( result );
      }
      updateJobTimings( job );
      updateBuiltOn( job );
      updateTestResults( job );
      updateCulprits( job );
      
      stateChanges.identifyStateChanges();
   }//End Method
   
   /**
    * Method to update the {@link JenkinsJob} time related properties.
    * @param job the {@link JenkinsJob} to update.
    */
   private void updateJobTimings( JenkinsJob job ) {
      if ( timestamp != null ) {
         job.currentBuildTimestampProperty().set( timestamp );
      }
      if ( duration != null ) {
         job.totalBuildTimeProperty().set( duration );
      }
      if ( estimatedDuration != null ) {
         job.expectedBuildTimeProperty().set( estimatedDuration );
      }
   }//End Method
   
   /**
    * Method to update the {@link JenkinsNode} built on.
    * @param job the {@link JenkinsJob} to update.
    */
   private void updateBuiltOn( JenkinsJob job ) {
      if ( builtOn != null ) {
         JenkinsNode node = database.getJenkinsNode( builtOn );
         if ( node == null ) {
            node = new JenkinsNodeImpl( builtOn );
            database.store( node );
         }
         job.lastBuiltOnProperty().set( node );
      }
   }//End Method

   /**
    * Method to update the {@link JenkinsJob} test results.
    * @param job the {@link JenkinsJob} to update.
    */
   private void updateTestResults( JenkinsJob job ) {
      if ( failCount != null ) {
         job.testFailureCount().set( failCount );
      }
      if ( skipCount != null ) {
         job.testSkipCount().set( skipCount );
      }
      if ( totalTestCount != null ) {
         job.testTotalCount().set( totalTestCount );
      }
   }//End Method
   
   /**
    * Method to update the {@link JenkinsJob} {@link JenkinsUser} culprits.
    * @param job the {@link JenkinsJob} to update.
    */
   private void updateCulprits( JenkinsJob job ){
      List< JenkinsUser > cupritsIdentified = new ArrayList<>();
      for ( String user : culprits ) {
         JenkinsUser jenkinsUser = database.getJenkinsUser( user );
         if ( jenkinsUser == null ) {
            jenkinsUser = new JenkinsUserImpl( user );
            database.store( jenkinsUser );
         }
         cupritsIdentified.add( jenkinsUser );
      }
      
      if ( !cupritsIdentified.equals( job.culprits() ) ) {
         job.culprits().clear();
         job.culprits().addAll( cupritsIdentified );
      }
   }//End Method
   
   /**
    * Set the {@link JenkinsJob} name being parsed.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setJobName( String key, String value ) {
      this.jobName = value;
   }//End Method
   
   /**
    * Set the {@link BuildState} being parsed.
    * @param key the parsed key.
    * @param value the parsed value, true building, false built.
    */
   void setBuildingState( String key, Boolean value ) {
      this.buildState = value ? BuildState.Building : BuildState.Built;
   }//End Method
   
   /**
    * Set the {@link JenkinsNode} name being parsed.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setBuiltOn( String key, String value ) {
      if ( value == null ) {
         builtOn = null;
      } else if ( value.equals( MASTER_ID ) ) {
         builtOn = MASTER_NAME;
      } else {
         builtOn = value;
      }
   }//End Method
   
   /**
    * Set the duration of the build when completed.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setDuration( String key, Long value ) {
      this.duration = value;
   }//End Method
   
   /**
    * Set the estimated duration of the build.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setEstimatedDuration( String key, Long value ) {
      this.estimatedDuration = value;
   }//End Method
   
   /**
    * Set the number of test failures.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setFailCount( String key, Integer value ) {
      this.failCount = value;
   }//End Method
   
   /**
    * Add a culprit for the build.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void addCulprit( String key, String value ) {
      this.culprits.add( value );
   }//End Method
   
   /**
    * Set the build number of the job.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setBuildNumber( String key, Integer value ) {
      this.buildNumber = value;
   }//End Method
   
   /**
    * Set the {@link BuildResultStatus} being parsed.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setResultingState( String key, BuildResultStatus value ) {
      this.result = value;
   }//End Method
   
   /**
    * Set the number of tests skipped.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setSkipCount( String key, Integer value ) {
      this.skipCount = value;
   }//End Method
   
   /**
    * Set the timestamp of the job when started building.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setTimestamp( String key, Long value ) {
      this.timestamp = value;
   }//End Method
   
   /**
    * Set the total test count.
    * @param key the parsed key.
    * @param value the parsed value.
    */
   void setTotalTestCount( String key, Integer value ) {
      this.totalTestCount = value;
   }//End Method

}//End Class
